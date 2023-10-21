package jdstockmarket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

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
