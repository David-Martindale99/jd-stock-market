package jdstockmarket;

import java.io.IOException;

import org.json.JSONArray;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CongressStockAPI {

    // Quiver API key for authentication
	private static final String API_KEY = "b360f7ff3b906aefab37ba412b7ad38ad487ac07";
    // OkHttp Client object variable
	private OkHttpClient client;

	
	// Constructor
	public CongressStockAPI() {
		this.client = new OkHttpClient();
	}
	
	
	public String fetchCongressTrades(String stockSymbol) throws IOException {
	    try {
	        String url = "https://api.quiverquant.com/beta/historical/congresstrading/" + stockSymbol;
	        Request request = new Request.Builder()
	                .url(url)
	                .get()
	                .addHeader("Accept", "application/json")
	                .addHeader("Authorization", "Bearer " + API_KEY)
	                .build();

	        try (Response response = client.newCall(request).execute()) {
	            if (!response.isSuccessful()) {
	                throw new IOException("Error: Response failed...\n" + response);
	            }

	            String responseBody = response.body().string();
	            if (responseBody.startsWith("[")) {
	            	// Parse the response as a JSON array
	                JSONArray jsonArray = new JSONArray(responseBody);
	                // Covert array to string
	                return jsonArray.toString();
	            } else {
	                throw new IOException("Unexpected response format: " + responseBody);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error fetching data";
	    }
	}
}
