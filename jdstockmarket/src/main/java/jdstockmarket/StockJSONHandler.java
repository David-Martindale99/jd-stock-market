package jdstockmarket;

import org.json.JSONObject;

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
}

