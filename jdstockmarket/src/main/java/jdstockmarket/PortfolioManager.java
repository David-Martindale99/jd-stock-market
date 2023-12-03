package jdstockmarket;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;


public class PortfolioManager {

    private static final String FILE_NAME = "portfolio.txt";

    public static void updatePortfolio(Stock newStock) {
        Portfolio portfolio = readPortfolioFromFile();
        
        Stock existingStock = portfolio.getStocks().get(newStock.getTicker());
        if (existingStock != null) {
        	// Update existing stock with new shares
        	int updateShares = existingStock.getShares() + newStock.getShares();
        	existingStock.setShares(updateShares);
        } else {
        	// Update the portfolio with the new stock if stock not there
        	portfolio.getStocks().put(newStock.getTicker(), newStock);
        }

        // Write the updated portfolio back to the file
        writePortfolioToFile(portfolio);

    }

    private static Portfolio readPortfolioFromFile() {
        File file = new File(FILE_NAME);
        TreeMap<String, Stock> stocks = new TreeMap<>();

        if (!file.exists() || file.length() == 0) {
            return new Portfolio(new TreeMap<>()); 
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read and parse the file content to reconstruct the Portfolio object
            String line;
            while ((line = reader.readLine()) != null) {
            	
            	// Check for and skip any empty lines
            	if (line.trim().isEmpty()) {
            		continue;
            	}
            	
                String[] parts = line.split(",");
                
                // Check to ensure that each line has the correct number of comma-separated values.
                if (parts.length == 3) {
                	// Parse the line to create Stock objects and add them to the stocks map
                    // Example: AAPL,150.00,10
					String ticker = parts[0];
					double price = Double.parseDouble(parts[1]);
					int shares = Integer.parseInt(parts[2]);
					stocks.put(ticker, new Stock(ticker, price, shares));
				} else {
					System.out.println("Warning: Malformed line '" + line + "' in file. Expected format: Ticker,Price,Shares");
				}
                
            }
            return new Portfolio(stocks);
        } catch (IOException e) {
            e.printStackTrace();
            return new Portfolio(new TreeMap<>()); 
        }
    }

    private static void writePortfolioToFile(Portfolio portfolio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Stock stock : portfolio.getStocks().values()) {
                writer.write(stock.getTicker() + "," + stock.getPrice() + "," + stock.getShares());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // TODO imlement this method 
    private static Set<String> collectTickersFromPortfolio(Portfolio portfolio) {
    	return new HashSet<>(portfolio.getStocks().keySet());
    }
    
    // TODO Implent this method
    private static void updateStockPrices(Set<String> tickers) {
    	
    }
}
