package jdstockmarket;

import java.util.HashMap;



public class Portfolio {

	// Instance variables
	// Idk about these, we'll see i guess
	private HashMap<String, Stock> stocks;
	
	// TODO is this a good constructor? idk
	public Portfolio(HashMap<String, Stock> stocks) {
	
		this.stocks = new HashMap<>();
		
	}

	/**
	 * @return the stocks
	 */
	public HashMap<String, Stock> getStocks() {
		return stocks;
	}

	/**
	 * @param stocks the stocks to set
	 */
	public void setStocks(HashMap<String, Stock> stocks) {
		this.stocks = stocks;
	}

	
}
