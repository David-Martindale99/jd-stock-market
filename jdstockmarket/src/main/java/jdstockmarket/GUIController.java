package jdstockmarket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUIController extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    // Instance variables for objects needed to run GUI and API calls
	private StockMarketAPI stockAPI;
    private Portfolio userPortfolio;
    private JTextField stockSymbolField;
    private JTextArea stockInfoArea;
    private JButton fetchButton;
    
    // GUIController Constructor
    public GUIController() {
        stockAPI = new StockMarketAPI();
        userPortfolio = new Portfolio();
        
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
                } catch (IOException ex) {
                    stockInfoArea.setText("Error fetching stock data.");
                }
            }
        });
        
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
