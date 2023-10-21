package jdstockmarket;

import java.util.HashMap;



public class Portfolio {

	// Instance variables
	// Idk about these, we'll see i guess
	private HashMap<String, Stock> stocks;
	
	public Portfolio() {
	
		this.stocks = new HashMap<>();
		
	}
	
	// addStock method
	public void addStock(String ticker, int numberOfShares, double lastPrice) {
	    if (stocks.containsKey(ticker)) {
	    	
	    	//Update the existing stock information, if stock already in portfolio
	    	Stock existingStock = stocks.get(ticker);
	    	existingStock.setShares(existingStock.getShares() + numberOfShares);
	    	existingStock.setPrice(lastPrice);
	    	
	    } else {
	        Stock newStock = new Stock(ticker, "", lastPrice, numberOfShares);
	        // set initial prices as the lastPrice
	        stocks.put(ticker, newStock);
	    }
	}

	// getStockInfo Method
	public Stock getStockInfo(String ticker) {
	    return stocks.get(ticker);
	}

	
	
}
