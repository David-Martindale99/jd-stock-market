package jdstockmarket;

import java.util.*;
import org.json.JSONObject;
import java.io.IOException;

/**
 * The {@code StockJSONHandler} class provides a concrete implementation of the {@link JSONHandler} interface
 * for parsing JSON data and retrieving values from JSON objects. This class is specifically tailored to handle
 * JSON data in the context of stock market information.
 *
 * <p>This class implements the {@code parseJSON} method to convert a JSON string into a {@code JSONObject},
 * and the {@code getValue} method to retrieve a {@code JSONObject} associated with a specified key from a
 * given {@code JSONObject}.
 *
 * <p>Example usage:
 * <pre>
 * {@code 
 * JSONHandler jsonHandler = new StockJSONHandler();
 * JSONObject jsonObject = jsonHandler.parseJSON(jsonString);
 * JSONObject timeSeries = jsonHandler.getValue(jsonObject, "Time Series (5min)");
 * }
 * </pre>
 *
 * @author David Martindale
 * @author Jamshaid Ali
 * @version 1.0 (17 October 2023)
 * @see JSONHandler
 * @see JSONObject
 */

public class StockJSONHandler implements JSONHandler {
	
	// Named constants
	private static final String TIME_SERIES_KEY = "Time Series (5min)";

    /**
     * Parses the specified JSON string and returns a {@code JSONObject}.
     *
     * @param jsonData The JSON string to be parsed.
     * @return A {@code JSONObject} representing the parsed JSON data.
     */
    @Override
    public JSONObject parseJSON(String jsonData) {
        return new JSONObject(jsonData);
    }

    /**
     * Retrieves a {@code JSONObject} associated with the specified key from the given {@code JSONObject}.
     *
     * @param jsonObject The {@code JSONObject} from which to retrieve the value.
     * @param key The key whose associated value is to be returned.
     * @return A {@code JSONObject} representing the value associated with the specified key.
     */
    @Override
    public JSONObject getValue(JSONObject jsonObject, String key) {
        return jsonObject.getJSONObject(key);
    }
    
    public JSONObject fetchStockData(StockMarketAPI stockAPI, String stockSymbol) throws IOException {
        String stockData = stockAPI.fetchLiveStockData(stockSymbol);
        return parseJSON(stockData);
        
    }

    public String displayStockInfo(JSONObject stockJSON, String stockSymbol) {
    	
    	/*
         * The code logic below is wrapped in an 'if' statment so we can 
         * Check for Null or Missing Keys: Before attempting to access a 
         * key in a JSONObject, you can use the has method to check if the 
         * key actually exists. This will prevent JSONException from being 
         * thrown
         * 
         * notice in the 'if' condition the has() method that checks the JSONObject
         * for a key, this method is made avaliable to us through the JSONObject class
         * of stockJSON varibale has been declaired as above
        */
        if (stockJSON.has("Time Series (5min)")) {
            JSONObject timeSeries = getValue(stockJSON, TIME_SERIES_KEY);
            ArrayList<String> timeStamps = new ArrayList<>(timeSeries.keySet());
            Collections.sort(timeStamps);
            String latestTimeStamp = timeStamps.get(timeStamps.size() - 1);
            JSONObject latestData = timeSeries.getJSONObject(latestTimeStamp);
            String mostRecentPrice = latestData.getString("4. close");
            String todaysHigh = latestData.getString("2. high");
            String todaysLow = latestData.getString("3. low");
            
            return formatStockInfo(stockSymbol, mostRecentPrice, todaysHigh, todaysLow);
        } else if (stockJSON.has("Information")){
            return "API request limit reached today...";
        } else {
        	return "Other errod with StockJSONHandler in displayStockInfo method";
        }
    }

    public String formatStockInfo(String stockSymbol, String mostRecentPrice, String todaysHigh, String todaysLow) {
        return " STOCK TICKER > " + stockSymbol + "\n\n" 
                + "  - Current price: $" + mostRecentPrice + "\n"
                + "  - Today's high:  $" + todaysHigh + "\n" 
                + "  - Today's low:   $" + todaysLow + "\n";
    }

}

