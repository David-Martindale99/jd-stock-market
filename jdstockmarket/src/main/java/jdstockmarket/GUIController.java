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
	private static final Color SECONDARY_COLOR = new Color(207, 234, 234); // Blue
	private static final Color ACCENT_COLOR = new Color(200, 255, 255); // Mint
	private static final Color TEXT_COLOR = Color.BLACK;
	protected static final String TIME_SERIES_KEY = "Time Series (5min)";
	private static final String FILE_NAME = "portfolio.txt";
	
	// Instance variables for managing GUI and API calls
    private StockMarketAPI stockAPI;
    private JTextField stockSymbolField;
    private JTextArea stockInfoArea;
    private JTextArea portfolioArea;
    private JButton fetchButton;
    private StockJSONHandler jsonHandler;  
    
    /**
     * Constructor for GUIController.
     * Initializes instances of StockMarketAPI and JSONHandler.
     * Sets up the GUI components and their configurations.
     */
    public GUIController() {
        stockAPI = new StockMarketAPI();
        jsonHandler = new StockJSONHandler();
        
     // Setup for GUI JFrame
        setTitle("Stock Market App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(PRIMARY_COLOR); // Set the background color
        
        // Create a panel for the west components
        JPanel westPanel = createWestPanel();

        // Add the west panel to the main frame
        add(westPanel, BorderLayout.WEST);
        
        // Create the stock info and portfolio areas with labels
        JPanel stockInfoPanel = new JPanel(new BorderLayout());
        JPanel portfolioPanel = new JPanel(new BorderLayout());
        
        // Create and add labels
        JLabel stockInfoLabel = new JLabel("Stock Information");
        JLabel portfolioLabel = new JLabel("Your Portfolio");
        
        // Set the font to bold with a specific size
        Font labelFont = new Font("Arial", Font.BOLD, 14); // Adjust the font and size
        stockInfoLabel.setFont(labelFont);
        portfolioLabel.setFont(labelFont);
        
        // Add padding around the labels
        stockInfoLabel.setBorder(new EmptyBorder(5, 112, 4, 10)); // Top, left, bottom, right padding
        portfolioLabel.setBorder(new EmptyBorder(5, 130, 4, 10)); // Adjust values as needed
        
        stockInfoPanel.add(stockInfoLabel, BorderLayout.NORTH);
        stockInfoPanel.setBackground(PRIMARY_COLOR);
        portfolioPanel.add(portfolioLabel, BorderLayout.NORTH);
        portfolioPanel.setBackground(PRIMARY_COLOR);

        
        // Create and add the stockInfoArea portfolioArea display
        stockInfoArea = createStyledTextArea();
        portfolioArea = createStyledTextArea();
        
        // Add stockInfoArea and portfolioArea to their Panels
        stockInfoPanel.add(new JScrollPane(stockInfoArea), BorderLayout.CENTER);
        portfolioPanel.add(new JScrollPane(portfolioArea), BorderLayout.CENTER);
        
        // Add these panels to the main frame
        add(stockInfoPanel, BorderLayout.CENTER);
        add(portfolioPanel, BorderLayout.EAST);

        // Finalizing GUI setup
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
    
    private JPanel createWestPanel() {
        JPanel westPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        westPanel.setBackground(PRIMARY_COLOR);
        westPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding

        // Constraints for the components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Margin around components

        // Label for Stock Symbol
        JLabel stockSymbolLabel = createStyledLabel("Enter Stock Symbol: ");
        westPanel.add(stockSymbolLabel, gbc);

        // TextField for Stock Symbol
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
        JButton displayPortfolioButton = createStyledButton("Dispalay Portfolio");
        displayPortfolioButton.addActionListener(e -> updatePortfolioDisplay());
        westPanel.add(displayPortfolioButton, gbc);

        return westPanel;
    }

    private void fetchStockInfo() {
    	String stockSymbol = stockSymbolField.getText().toUpperCase();
        try {
            
        	 JSONObject stockJSON = jsonHandler.fetchStockData(stockAPI, stockSymbol);
            String displayText = jsonHandler.displayStockInfo(stockJSON, stockSymbol);
            stockInfoArea.setText(displayText);

	    } catch (IOException ioe) {
	        stockInfoArea.setText("\n Error fetching or parsing stock data." + ioe.getMessage());
	    } catch (JSONException je) {
	        stockInfoArea.setText("\n JSON parsing error: " + je.getMessage());
	    } catch (Exception ex) {
	        stockInfoArea.setText("\n An unexpected error occurred...that sucks [Exception] > " + ex.getMessage());
	    }
    }

    private void addStock(String sharesText) {
            try {
            	String stockSymbol = stockSymbolField.getText().toUpperCase();
            	
            	if (stockSymbol.isBlank()) {
            		stockInfoArea.setText("  Invalid Input: Please enter a stock ticker\n");
            	}
            	
                JSONObject stockJSON = jsonHandler.fetchStockData(stockAPI, stockSymbol);
                int shares = Integer.parseInt(sharesText);
                
                if (stockJSON.has("Time Series (5min)")) {
                	
                    JSONObject timeSeries = stockJSON.getJSONObject(TIME_SERIES_KEY);
                    ArrayList<String> timeStamps = new ArrayList<>(timeSeries.keySet());
                    Collections.sort(timeStamps);
                    String latestTimeStamp = timeStamps.get(timeStamps.size() - 1); 
                    JSONObject latestData = timeSeries.getJSONObject(latestTimeStamp);
                    Double mostRecentPrice = Double.parseDouble(latestData.getString("4. close"));
                    Stock stock = new Stock(stockSymbol, mostRecentPrice, shares);
                    
                    PortfolioManager.updatePortfolio(stock);
                    stockInfoArea.append("  Added " + shares + " shares of " + stockSymbol + "\n");
                    updatePortfolioDisplay();

                } else {
                    stockInfoArea.setText("  Time Series data not available for " + stockSymbol + " right now");
                }
                
                
            } catch (NumberFormatException ex) {
                stockInfoArea.append("  Invalid input: please enter a number of shares\n");
            } catch (JSONException je) {
            	stockInfoArea.setText("  Exeption (Action Listener): " + je.getMessage());
            } catch (IOException ioe) {
				stockInfoArea.append("  Add Stock: IOException: " + ioe.getMessage() + "\n");;
			}
    }

    public void updatePortfolioDisplay() {
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
                    formattedContent.append("  Ticker: ").append(ticker)
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
