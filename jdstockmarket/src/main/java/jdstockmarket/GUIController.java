package jdstockmarket;

import javax.swing.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * The {@code GUIController} class serves as the graphical user interface controller for interacting
 * with a stock market API and managing a user's stock portfolio. It extends {@code JFrame} to provide
 * the windowing components and manages instances of {@link StockMarketAPI} and {@link Portfolio} to 
 * fetch live stock data and track the user's portfolio respectively.
 *
 * <p>This class utilizes a simple FlowLayout and provides fields for user input, buttons for 
 * triggering actions, and text areas for displaying fetched stock data.
 *
 * @author David Martindale
 * @author Jamshaid Ali
 *
 * @version 1.0 (17 October 2023)
 *
 * @see JFrame
 * @see StockMarketAPI
 * @see Portfolio
 * @see <a href="https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving">
 *      Morelli, R., & Walde, R. (2016). Java, Java, Java: Object-Oriented Problem Solving</a>
 */

public class GUIController extends JFrame {
    
    private static final long serialVersionUID = 1L;
   
    // Instance variables for objects needed to run GUI and API calls
	private StockMarketAPI stockAPI;
    private Portfolio userPortfolio;
    private JTextField stockSymbolField;
    private JTextArea stockInfoArea;
    private JButton fetchButton;
    private JTextArea portfolioArea;
    
    // GUIController Constructor
    public GUIController() {
        stockAPI = new StockMarketAPI();
        
        // Setup for GUI JFrame
        setTitle("Stock Market App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        
        // Instantiate objetcs for GUI elements to add to JFrame
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
                    String stockData = stockAPI.fetchLiveStockData(stockSymbol);
                    // Parsing the stockData and adding it to Portfolio can be done here
                    stockInfoArea.setText(stockData);
                } catch (IOException ioe) {
                    stockInfoArea.setText("Error fetching stock data.");
                }
            }
        });
        
        // ActionListener for add stock to portfolio button
        // TODO
        
        // add elements to GUI JFrame
        add(new JLabel("Enter Stock Symbol: "));
        add(stockSymbolField);
        add(fetchButton);
        add(new JScrollPane(stockInfoArea));
        
        // Finalize GUI 
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUIController();
            }
        });
    }
}
