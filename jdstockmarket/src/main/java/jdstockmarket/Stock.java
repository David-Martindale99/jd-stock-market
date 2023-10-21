package jdstockmarket;

/**
 * The {@code Stock} class encapsulates the essential data pertaining to a particular stock.
 * It contains fields to hold the stock's ticker symbol, company name, closing price, highest price,
 * and lowest price. This class provides a constructor for initializing a {@code Stock} object with
 * specific values and a set of getter and setter methods for accessing and modifying these fields.
 * 
 * <p>Example usage:
 * <pre>
 * {@code 
 * Stock appleStock = new Stock("AAPL", "Apple Inc.", 145.09, 146.00, 144.00);
 * String ticker = appleStock.getTicker();  // Returns "AAPL"
 * Double closingPrice = appleStock.getPriceClosing();  // Returns 145.09
 * }
 * </pre>
 * 
 * @author David Martindale
 * @author Jamshaid Ali
 * 
 * @version 1.0 (17 October 2023)
 */

public class Stock {
	
	private String ticker;
	private String compName;
	private Double priceClosing;
	private Double priceHigh;
	private Double priceLow;
	
	public Stock(String ticker, String name, Double closing, Double high, Double low) {
		
		this.ticker = ticker;
		this.compName = name;
		this.priceClosing = closing;
		this.priceHigh = high;
		this.priceLow = low;
		
	}

	/**
	 * @return the ticker
	 */
	public String getTicker() {
		return ticker;
	}

	/**
	 * @param ticker the ticker to set
	 */
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	/**
	 * @return the compName
	 */
	public String getCompName() {
		return compName;
	}

	/**
	 * @param compName the compName to set
	 */
	public void setCompName(String compName) {
		this.compName = compName;
	}

	/**
	 * @return the priceClosing
	 */
	public Double getPriceClosing() {
		return priceClosing;
	}

	/**
	 * @param priceClosing the priceClosing to set
	 */
	public void setPriceClosing(Double priceClosing) {
		this.priceClosing = priceClosing;
	}

	/**
	 * @return the priceHigh
	 */
	public Double getPriceHigh() {
		return priceHigh;
	}

	/**
	 * @param priceHigh the priceHigh to set
	 */
	public void setPriceHigh(Double priceHigh) {
		this.priceHigh = priceHigh;
	}

	/**
	 * @return the priceLow
	 */
	public Double getPriceLow() {
		return priceLow;
	}

	/**
	 * @param priceLow the priceLow to set
	 */
	public void setPriceLow(Double priceLow) {
		this.priceLow = priceLow;
	}
	
	

}
