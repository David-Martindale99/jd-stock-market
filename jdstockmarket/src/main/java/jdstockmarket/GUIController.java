package jdstockmarket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.*;
import java.io.IOException;
import java.util.*;

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
	
	// Instance variables for managing GUI and API calls
    private StockMarketAPI stockAPI;
    private JTextField stockSymbolField;
    private JTextArea stockInfoArea;
    private JButton fetchButton;
    private JSONHandler jsonHandler;  
    
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
        setLayout(new FlowLayout());
        
        // Instantiate objects for GUI elements to add to JFrame
        stockSymbolField = new JTextField(10);
        stockInfoArea = new JTextArea(10, 30);
        stockInfoArea.setEditable(false);
        fetchButton = new JButton("Fetch Stock Info");
        
        // ActionListener for fetchButton 
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stockSymbol = stockSymbolField.getText();
                try {
                    // Fetching stock data as a string
                    String stockData = stockAPI.fetchLiveStockData(stockSymbol);
                    // Parsing the string data into a JSONObject
                    JSONObject stockJSON = jsonHandler.parseJSON(stockData);
                    // Extracting the "Time Series (5min)" JSONObject from the stock data
                    JSONObject timeSeries = jsonHandler.getValue(stockJSON, "Time Series (5min)");
                    
                    // Obtaining the keys (timestamps) and sorting them
                    ArrayList<String> timeStamps = new ArrayList<>(timeSeries.keySet());
                    Collections.sort(timeStamps);
                    
                    // If the stock market is still open, get the most recent stock price
                    String latestTimeStamp = timeStamps.get(timeStamps.size() - 1);
                    JSONObject latestData = timeSeries.getJSONObject(latestTimeStamp);
                    String mostRecentPrice = latestData.getString("4. close");
                    
                    // Displaying the most recent / closing price
                    stockInfoArea.setText(stockSymbol + ": Current price $" + mostRecentPrice);       
                    
                } catch (IOException ioe) {
                    stockInfoArea.setText("Error fetching or parsing stock data.");
                }
            }
        });
        
        // ActionListener for add stock to portfolio button
        // TODO
        
        // Adding elements to GUI JFrame
        add(new JLabel("Enter Stock Symbol: "));
        add(stockSymbolField);
        add(fetchButton);
        add(new JScrollPane(stockInfoArea));
        
        // Finalizing GUI setup
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
