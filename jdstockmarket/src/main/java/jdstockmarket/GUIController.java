package jdstockmarket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	private static final Color BORDER_COLOR = new Color(128, 0, 32);  // Burgundy
	private static final Color BORDER_COLOR_2 = new Color(50, 50, 255);  // Blue
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

        // Create a panel for the west components and set its layout to GridBagLayout
        JPanel westPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        westPanel.setBorder(new EmptyBorder(10, 10, 10, 10));  // top, left, bottom, right
        
        // Add JLabel and JTextField for Stock Symbol
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        westPanel.add(new JLabel("Enter Stock Symbol: "), gbc);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        stockSymbolField = new JTextField(10);
        westPanel.add(stockSymbolField, gbc);

        // Add JButton for Fetching Stock Info
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        fetchButton = new JButton("Fetch Stock Info");
        westPanel.add(fetchButton, gbc);

        // Add JTextField and JButton for Adding Stock
        gbc.gridy = 3;
        JTextField sharesField = new JTextField(3);
        westPanel.add(sharesField, gbc);
        gbc.gridy = 4;
        JButton addStockButton = new JButton("Add Stock");
        westPanel.add(addStockButton, gbc);

        // Add the west panel to the main frame
        add(westPanel, BorderLayout.WEST);

        // Create and add the stockInfoArea
        stockInfoArea = new JTextArea(10, 30);
        stockInfoArea.setEditable(false);
        stockInfoArea.setBorder(new LineBorder(BORDER_COLOR, 5));  // color, thickness
        add(new JScrollPane(stockInfoArea), BorderLayout.CENTER);
        
        // Create Portfolio display area
        portfolioArea = new JTextArea(10, 30);
        portfolioArea.setEditable(false);
        portfolioArea.setBorder(new LineBorder(BORDER_COLOR_2, 5));  // color, thickness
        add(new JScrollPane(portfolioArea), BorderLayout.EAST); // Adjust layout as needed

        // ActionListener for fetchButton
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 String stockSymbol = stockSymbolField.getText().toUpperCase();
                 try {
                     
                 	 JSONObject stockJSON = jsonHandler.fetchStockData(stockAPI, stockSymbol);
                     String displayText = jsonHandler.displayStockInfo(stockJSON, stockSymbol);
                     stockInfoArea.setText(displayText);
  
 			    } catch (IOException ioe) {
 			        stockInfoArea.setText("Error fetching or parsing stock data." + ioe.getMessage());
 			    } catch (JSONException je) {
 			        stockInfoArea.setText("JSON parsing error: " + je.getMessage());
 			    } catch (Exception ex) {
 			        stockInfoArea.setText("An unexpected error occurred...that sucks [Exception] > " + ex.getMessage());
 			    }
            }
        });

        // ActionListener for addStockButton
        addStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stockSymbol = stockSymbolField.getText().toUpperCase();
                String sharesText = sharesField.getText();
                int shares = Integer.parseInt(sharesText);
                
                try {
                	
                    JSONObject stockJSON = jsonHandler.fetchStockData(stockAPI, stockSymbol);
                    
                    if (stockJSON.has("Time Series (5min)")) {
                    	
                        JSONObject timeSeries = stockJSON.getJSONObject(TIME_SERIES_KEY);
                        ArrayList<String> timeStamps = new ArrayList<>(timeSeries.keySet());
                        Collections.sort(timeStamps);
                        String latestTimeStamp = timeStamps.get(timeStamps.size() - 1); 
                        JSONObject latestData = timeSeries.getJSONObject(latestTimeStamp);
                        Double mostRecentPrice = Double.parseDouble(latestData.getString("4. close"));
                        Stock stock = new Stock(stockSymbol, mostRecentPrice, shares);
                        
                        PortfolioManager.updatePortfolio(stock);
                        stockInfoArea.append("\nAdded " + shares + " shares of " + stockSymbol);
                        updatePortfolioDisplay();

                    } else {
                        stockInfoArea.setText("\nTime Series data not available for " + stockSymbol + "right now");
                    }
                    
                    
                } catch (NumberFormatException ex) {
                    stockInfoArea.append("\nInvalid number of shares");
                } catch (JSONException je) {
                	stockInfoArea.append("\nExeption (Action Listener) " + je.getMessage());
                } catch (IOException ioe) {
					ioe.printStackTrace();
				}
            }
        });

        // Finalizing GUI setup
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
    }
    
    // TODO format the info in the portfolio area
    public void updatePortfolioDisplay() {
        StringBuilder formattedContent = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_NAME));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String ticker = parts[0];
                    String price = parts[1];
                    String shares = parts[2];
                    Double totalValue = Double.parseDouble(shares) * Double.parseDouble(price);
                    formattedContent.append("Ticker: ").append(ticker)
                                    .append(",  Price: $").append(price)
                                    .append(",  Shares: ").append(shares)
                                    .append("\nStock Value: $" + totalValue)
                                    .append("\n");
                }
            }
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUIController();
            }
        });
    }
}
