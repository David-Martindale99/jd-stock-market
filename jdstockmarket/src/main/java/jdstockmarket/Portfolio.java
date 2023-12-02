package jdstockmarket;

import java.util.TreeMap;



public class Portfolio {

	// Instance variables
	// Idk about these, we'll see i guess
	private TreeMap<String, Stock> stocks;
	
	// TODO is this a good constructor? idk
	public Portfolio(TreeMap<String, Stock> stocks) {
	
		this.stocks = stocks;
		
	}

	/**
	 * @return the stocks
	 */
	public TreeMap<String, Stock> getStocks() {
		return stocks;
	}

	/**
	 * @param stocks the stocks to set
	 */
	public void setStocks(TreeMap<String, Stock> stocks) {
		this.stocks = stocks;
	}

	
}
