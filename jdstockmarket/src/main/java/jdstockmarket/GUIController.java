package jdstockmarket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.*;

/**
 * The {@code GUIController} class provides a graphical user interface (GUI) for interacting
 * with a stock market API and managing a user's stock portfolio.
 * It extends {@code JFrame} to utilize windowing components.
 * 
 * This class manages instances of {@link StockMarketAPI} and {@link Portfolio}
 * to fetch live stock data and track the user's portfolio respectively.
 * 
 * @author David Martindale
 * @author Jamshaid Ali
 * @version 1.0 (17 October 2023)
 * @see JFrame
 * @see StockMarketAPI
 * @see Portfolio
 */
public class GUIController extends JFrame {

	private static final long serialVersionUID = 1L;
	
	// Named constants
	private static final Color PRIMARY_COLOR = new Color(13, 148, 148); // Teal
	private static final Color ACCENT_COLOR = new Color(200, 255, 255); // Mint
	private static final Color TEXT_COLOR = Color.BLACK; // Black
	protected static final String TIME_SERIES_KEY = "Time Series (5min)"; //Key that holds the stock JSON data from API response
	private static final String FILE_NAME = "portfolio.txt"; // Txt file that holds portfolio data
	
	// Instance variables for managing GUI and API calls
    private StockMarketAPI stockAPI;
    private CongressStockAPI congressAPI;
    private StockJSONHandler jsonHandler;
    // Global JFrame elements
    private JTextField stockSymbolField;
    private JTextArea stockInfoArea;
    private JTextArea portfolioArea;
    private JButton fetchButton;
    private JToggleButton updatePricesToggle;
    private JButton pelosiButton;
    private JTextArea pelosiTextArea;
     
    /**
     * Constructor for GUIController.
     */
    public GUIController() {
        initializeComponents();
        setupGUI();
    }
    
    private void initializeComponents() {
        stockAPI = new StockMarketAPI();
        congressAPI = new CongressStockAPI();
        jsonHandler = new StockJSONHandler();

        stockSymbolField = createStyledTextField(10);
        stockInfoArea = createStyledTextArea();
        portfolioArea = createStyledTextArea();
        pelosiTextArea = createStyledTextArea();

        fetchButton = createStyledButton("Fetch Stock Info");
        pelosiButton = createStyledButton("Nancy Pelosi ?");
        updatePricesToggle = new JToggleButton("Off");

        fetchButton.addActionListener(e -> fetchStockInfo());
        pelosiButton.addActionListener(e -> fetchCongressInfo());
        updatePricesToggle.addActionListener(e -> updatePortfolioDisplay(updatePricesToggle.isSelected()));
    }
    
    private void setupGUI() {
        setTitle("Stock Market App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(PRIMARY_COLOR);

        add(createWestPanel(), BorderLayout.WEST);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthWestPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel createWestPanel() {
        JPanel westPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        westPanel.setBackground(PRIMARY_COLOR);
        westPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding (T, L, B, R)

        // Constraints for the components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Margin around components (T, L, B, R)

        // Label for Stock Symbol field
        JLabel stockSymbolLabel = createStyledLabel("Enter Stock Symbol: ");
        westPanel.add(stockSymbolLabel, gbc);

        // TextField for Stock Symbol field
        gbc.gridy++;
        stockSymbolField = createStyledTextField(10);
        stockSymbolField.addActionListener(e -> fetchStockInfo());
        westPanel.add(stockSymbolField, gbc);

        // Fetch Button
        gbc.gridy++;
        fetchButton = createStyledButton("Fetch Stock Info");
        fetchButton.addActionListener(e -> fetchStockInfo());
        westPanel.add(fetchButton, gbc);
        
        // Label for Number of Shares TextField
        gbc.gridy++;
        JLabel sharesFieldLabel = createStyledLabel("Enter Share Quantity:");
        westPanel.add(sharesFieldLabel, gbc);

        // TextField for Number of Shares
        gbc.gridy++;
        JTextField sharesField = createStyledTextField(10);
        sharesField.addActionListener(e -> addStock(sharesField.getText()));
        westPanel.add(sharesField, gbc);

        // Add Stock Button
        gbc.gridy++;
        JButton addStockButton = createStyledButton("Add Stock");
        addStockButton.addActionListener(e -> addStock(sharesField.getText()));
        westPanel.add(addStockButton, gbc);
        
        // Add Display Porfolio Button
        gbc.gridy++;
        JButton displayPortfolioButton = createStyledButton("Display Portfolio");
        displayPortfolioButton.addActionListener(e -> updatePortfolioDisplay(updatePricesToggle.isSelected()));
        westPanel.add(displayPortfolioButton, gbc);
        
        /// Initialize the toggle button
        updatePricesToggle = new JToggleButton("Off");
        
        // Set static button size to keep from knocking into other frame elements when clicked
        Dimension buttonSize = new Dimension(50, 30); // Set a fixed size (width, height)
        updatePricesToggle.setPreferredSize(buttonSize);
        updatePricesToggle.setMinimumSize(buttonSize);
        updatePricesToggle.setMaximumSize(buttonSize);

        // Toggle button action listener
        updatePricesToggle.addActionListener(e -> {
            if (updatePricesToggle.isSelected()) {
                updatePricesToggle.setText("On");
            } else {
                updatePricesToggle.setText("Off");
            }
        });
        
        // Label for "Live Portfolio" button
        JLabel updatePricesLabel = new JLabel(" Live Portfolio");

        // Add the label and toggle button to the panel
        gbc.gridx = 0; gbc.gridy = 10; // Set grid position for label
        westPanel.add(updatePricesLabel, gbc);
        gbc.gridx = 1; // Adjust grid position for toggle button
        gbc.insets = new Insets(0, -40, 5, 0); // (T, L, B, R) padding
        westPanel.add(updatePricesToggle, gbc);
        
        // Add Pelosi Button
        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 0, 5); // Margin around component (T, L, B, R)
        westPanel.add(pelosiButton, gbc);
     
        return westPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(PRIMARY_COLOR);

        // Stock Info Panel
        JPanel stockInfoPanel = new JPanel(new BorderLayout());
        JLabel stockInfoLabel = new JLabel("Stock Information");
        stockInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        stockInfoLabel.setBorder(new EmptyBorder(5, 112, 4, 10));
        stockInfoPanel.add(stockInfoLabel, BorderLayout.NORTH);
        stockInfoPanel.add(new JScrollPane(stockInfoArea), BorderLayout.CENTER);
        stockInfoPanel.setBackground(PRIMARY_COLOR);

        // Portfolio Panel
        JPanel portfolioPanel = new JPanel(new BorderLayout());
        JLabel portfolioLabel = new JLabel("Your Portfolio");
        portfolioLabel.setFont(new Font("Arial", Font.BOLD, 14));
        portfolioLabel.setBorder(new EmptyBorder(5, 130, 4, 10));
        portfolioPanel.add(portfolioLabel, BorderLayout.NORTH);
        portfolioPanel.add(new JScrollPane(portfolioArea), BorderLayout.CENTER);
        portfolioPanel.setBackground(PRIMARY_COLOR);
        portfolioPanel.setBorder(new EmptyBorder(0, 0, 0, 15));

        centerPanel.add(stockInfoPanel, BorderLayout.CENTER);
        centerPanel.add(portfolioPanel, BorderLayout.EAST);

        return centerPanel;
    }
    
    private JPanel createSouthWestPanel() {
        JPanel southWestPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        southWestPanel.setBackground(PRIMARY_COLOR);
        southWestPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Constraints for Pelosi TextArea
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(-10, 15, 5, 15); // (T, L, B, R)
        
        // Wrap the pelosiTextArea in a JScrollPane
        JScrollPane pelosiScrollPane = new JScrollPane(pelosiTextArea);
        pelosiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pelosiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        southWestPanel.add(pelosiScrollPane, gbc);

        return southWestPanel;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });

        return button;
    }
    
    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        return textField;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    private JTextArea createStyledTextArea() {
        JTextArea textArea = new JTextArea(15, 30);
        textArea.setEditable(false);
        
        // Creating a less saturated blue color
        float[] hsbValues = Color.RGBtoHSB(207, 234, 234, null);
        // Reduce saturation with #.#f value
        Color lessSaturatedBlue = Color.getHSBColor(hsbValues[0], 0.1f, hsbValues[2]); 
        
        textArea.setBackground(lessSaturatedBlue);
        textArea.setForeground(Color.BLACK);
        textArea.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 3));
        return textArea;
    }

    private void fetchStockInfo() {
    	String stockSymbol = stockSymbolField.getText().toUpperCase();
    	
    	if (stockSymbol.isBlank()) {
    		stockInfoArea.setText("  Invalid Input: Please enter a stock symbol\n");
    		return;
    	}
    	
        try {
            
        	JSONObject stockJSON = jsonHandler.fetchStockData(stockAPI, stockSymbol);
            String displayText = jsonHandler.displayStockInfo(stockJSON, stockSymbol);
            stockInfoArea.setText(displayText);

	    } catch (IOException ioe) {
	        stockInfoArea.setText("  IOException In fetchStockInfo() -> " + ioe.getMessage() + "\n");
	    } catch (JSONException je) {
	        stockInfoArea.setText("  JSON parsing error: " + je.getMessage() + "\n");
	    } catch (Exception ex) {
	        stockInfoArea.setText("  An unexpected error occurred...that sucks \n  [Exception] > " + ex.getMessage() + "\n");
	    }
    }
    
    private void fetchCongressInfo() {
    	String stockSymbol = stockSymbolField.getText().toUpperCase();
    	
    	if (stockSymbol.isEmpty()) {
    		pelosiTextArea.setText("  Try entering a stock symbol and see what happens?");
    		return;
    	}
    	
    	try {
    		
    		JSONArray stockJSON = jsonHandler.fetchCongressData(congressAPI, stockSymbol);
    		String displayText = jsonHandler.displayCongressInfo(stockJSON, stockSymbol);
    		pelosiTextArea.setText(displayText);
    		pelosiTextArea.setCaretPosition(0);
    	} catch (IOException ioe) {
    		
    	}
    }

    private void addStock(String sharesText) {
            try {
            	String stockSymbol = stockSymbolField.getText().toUpperCase();
            	
            	if (stockSymbol.isBlank()) {
            		stockInfoArea.setText("  Invalid Input: Please enter a stock ticker\n");
            		return;
            	}
            	
            	if (sharesText.isBlank()) {
            		stockInfoArea.setText("  Invalid Input: Please enter a quantity of shares\n");
            		return;
            	}
            	
                JSONObject stockJSON = jsonHandler.fetchStockData(stockAPI, stockSymbol);
                int shares = Integer.parseInt(sharesText);
                
                if (stockJSON.has(TIME_SERIES_KEY)) {
                	
                    JSONObject timeSeries = stockJSON.getJSONObject(TIME_SERIES_KEY);
                    ArrayList<String> timeStamps = new ArrayList<>(timeSeries.keySet());
                    Collections.sort(timeStamps);
                    String latestTimeStamp = timeStamps.get(timeStamps.size() - 1); 
                    JSONObject latestData = timeSeries.getJSONObject(latestTimeStamp);
                    Double mostRecentPrice = Double.parseDouble(latestData.getString("4. close"));
                    Stock stock = new Stock(stockSymbol, mostRecentPrice, shares);
                    
                    PortfolioManager.updatePortfolio(stock);
                    stockInfoArea.append("  Added " + shares + " shares of " + stockSymbol + "\n");
                    updatePortfolioDisplay(updatePricesToggle.isSelected());

                } else {
                    stockInfoArea.append("  Time Series data not available for " + stockSymbol + " right now\n");
                }
                
                
            } catch (NumberFormatException ex) {
                stockInfoArea.setText("  NumberFormatException: \n  " + ex.getMessage());
            } catch (JSONException je) {
            	stockInfoArea.setText("  JSONExeption: \n  " + je.getMessage());
            } catch (IOException ioe) {
				stockInfoArea.setText("  Add Stock: IOException:\n   " + ioe.getMessage());
			}
    }
    
    public void updatePortfolioDisplay(boolean updatePricesONorOFF) {
    	// Update portfolio with most recent prices with an API call
    	try {
			Portfolio portfolio = PortfolioManager.readPortfolioFromFile();
			PortfolioManager.updateStockPrices(portfolio, updatePricesONorOFF);
		} catch (NullPointerException npe) {
			portfolioArea.setText("  ERROR: Price update failed\n\n" + "  API call limit reached today...\n");
			return;
		} catch (Exception e) {
			portfolioArea.setText(e.getMessage());
			return;
		}
    	
        StringBuilder formattedContent = new StringBuilder();
        DecimalFormat numberFormat = new DecimalFormat("#,##0.00");

        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_NAME));
            Double portfolioTotal = 0.0;
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String ticker = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    int shares = Integer.parseInt(parts[2]);
                    Double totalValue = shares * price;
                    portfolioTotal += totalValue;
                    formattedContent.append("  Ticker: ").append("[ " + ticker + " ]")
                                    .append(",  Price: $").append(numberFormat.format(price))
                                    .append(",  Shares: ").append(shares)
                                    .append("\n  Stock Value: $").append(numberFormat.format(totalValue))
                                    .append("\n\n");
                }
            }
            // Check for empty portfolio and notify user if empty
            if (formattedContent.isEmpty()) {
            	formattedContent.append("  You have no stock holdings\n");
            }
            // Diaplay total Portfolio value to user
            formattedContent.append("\n  Portfolio Value: $").append(numberFormat.format(portfolioTotal));
            portfolioArea.setText(formattedContent.toString());
        } catch (IOException ioe) {
            portfolioArea.setText("Error loading portfolio data...\n");
            portfolioArea.append(ioe.getMessage());
        }
    }
    
    /**
     * Entry point of the application.
     * Invokes the GUIController constructor within the Swing event dispatch thread.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
    	// Set LookAndFeel of UI
    	try {
    	    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
    	        if ("Nimbus".equals(info.getName())) {
    	            UIManager.setLookAndFeel(info.getClassName());
    	            break;
    	        }
    	    }
    	} catch (Exception e) {
    	    // If Nimbus is not available, fall back to cross-platform
    	    try {
    	        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    	    } catch (Exception ex) {
    	        // handle exception
    	    }
    	}
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUIController();
            }
        });
    }
}
