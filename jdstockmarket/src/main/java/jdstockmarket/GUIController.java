package jdstockmarket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.json.*;
import java.io.IOException;

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
	
	// Instance variables for managing GUI and API calls
    private StockMarketAPI stockAPI;
    private JTextField stockSymbolField;
    private JTextArea stockInfoArea;
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
        
        
        /*
         * In GUI setup below,I used GridBagLayout for the westPanel to have more 
         * control over the positioning and size of each component. 
         * 
         * you can add a border around the JTextArea using the setBorder method. You 
         * can use various types of borders provided by Swing, such as LineBorder
         * 
         * You can specify custom colors using the Color class in Java's AWT package. 
         * The Color class allows you to define a color using its RGB (Red, Green, Blue) 
         * components.
         */
        
        // Create a panel for the west components and set its layout to GridBagLayout
        JPanel westPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Add an empty border to create a cushion around the westPanel
        westPanel.setBorder(new EmptyBorder(10, 10, 10, 10));  // top, left, bottom, right
        
        // Add JLabel centered above the JTextField
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        westPanel.add(new JLabel("Enter Stock Symbol: "), gbc);

        // Add JTextField
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        stockSymbolField = new JTextField(10);
        westPanel.add(stockSymbolField, gbc);

        // Add JButton centered
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        fetchButton = new JButton("Fetch Stock Info");
        westPanel.add(fetchButton, gbc);

        // Add the west panel to the main frame
        add(westPanel, BorderLayout.WEST);

        // Create and add the stockInfoArea to the main frame
        stockInfoArea = new JTextArea(10, 30);
        stockInfoArea.setEditable(false);
        add(new JScrollPane(stockInfoArea), BorderLayout.CENTER);
        
        // Add a line border around the JTextArea
        stockInfoArea.setBorder(new LineBorder(BORDER_COLOR, 5));  // color, thickness
        
        

        /*
         *  ActionListener for fetchButton 
         */
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stockSymbol = stockSymbolField.getText().toUpperCase();
                try {
                    
                	JSONObject stockJSON = jsonHandler.fetchStockData(stockAPI, stockSymbol);
                    String displayText = jsonHandler.displayStockInfo(stockJSON, stockSymbol);
                    stockInfoArea.setText(displayText);
 
				/*
				 * 	Use multiple catch blocks to handle different types of exceptions separately.
				 *  This will allow you to provide more informative error messages. This includes 
				 *  User Feedback
				*/	
			    } catch (IOException ioe) {
			        stockInfoArea.setText("Error fetching or parsing stock data.");
			    } catch (JSONException je) {
			        stockInfoArea.setText("JSON parsing error: " + je.getMessage());
			    } catch (Exception ex) {
			        stockInfoArea.setText("An unexpected error occurred...that sucks [Exception] > " + ex);
			    }
			}
        });
        
        // ActionListener for add stock to portfolio button
        // TODO
        
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
