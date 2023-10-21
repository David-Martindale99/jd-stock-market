package jdstockmarket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

/**
 * The {@code StockMarketAPI} class encapsulates the interactions with the Alpha Vantage API for 
 * retrieving live stock data. It leverages the OkHttp library for handling HTTP requests and responses.
 * 
 * <p>The primary method {@code fetchLiveStockData} is designed to fetch live stock data based on a 
 * provided stock symbol, using a specified API key to authenticate with the Alpha Vantage service.
 * 
 * <p>Example usage:
 * <pre>
 * {@code 
 * StockMarketAPI api = new StockMarketAPI();
 * String stockData = api.fetchLiveStockData("AAPL");
 * }
 * </pre>
 * 
 * @author David Martindale
 * @author Jamshaid Ali
 * 
 * @version 1.0 (17 October 2023)
 * 
 * @see OkHttpClient
 * @see Request
 * @see Response
 */

public class StockMarketAPI {

    private static final String API_KEY = "AG1H5T16XJ3W5TS1";
    private OkHttpClient client;

    public StockMarketAPI() {
        this.client = new OkHttpClient();
    }

    public String fetchLiveStockData(String stockSymbol) throws IOException {
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + stockSymbol + "&interval=5min&apikey=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }


}
