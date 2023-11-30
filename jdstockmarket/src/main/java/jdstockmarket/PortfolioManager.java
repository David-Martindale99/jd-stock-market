package jdstockmarket;

import java.io.*;
import java.util.HashMap;


// TODO Fix so file is updated and not overwirtten each time
public class PortfolioManager {

    private static final String FILE_NAME = "portfolio.txt";

    public static void updatePortfolio(Stock newStock) {
        Portfolio portfolio = readPortfolioFromFile();
        
        Stock existingStock = portfolio.getStocks().get(newStock.getTicker());
        if (existingStock != null) {
        	int updateShares = existingStock.getShares() + newStock.getShares();
        	existingStock.setShares(updateShares);
        } else {
        	// Update the portfolio with the new stock
        	portfolio.getStocks().put(newStock.getTicker(), newStock);
        }

        // Write the updated portfolio back to the file
        writePortfolioToFile(portfolio);

    }

    private static Portfolio readPortfolioFromFile() {
        File file = new File(FILE_NAME);
        HashMap<String, Stock> stocks = new HashMap<>();

        if (!file.exists() || file.length() == 0) {
            return new Portfolio(new HashMap<>()); 
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read and parse the file content to reconstruct the Portfolio object
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse the line to create Stock objects and add them to the stocks map
                // Example: AAPL,150.00,10
                String[] parts = line.split(",");
                String ticker = parts[0];
                double price = Double.parseDouble(parts[1]);
                int shares = Integer.parseInt(parts[2]);
                stocks.put(ticker, new Stock(ticker, price, shares));
            }
            return new Portfolio(stocks);
        } catch (IOException e) {
            e.printStackTrace();
            return new Portfolio(new HashMap<>()); 
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
}
