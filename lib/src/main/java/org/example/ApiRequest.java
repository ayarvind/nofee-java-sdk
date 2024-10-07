package org.example;

import java.io.IOException;
import org.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiRequest {
    private final OkHttpClient client;
    private final JSONObject payload;
    private final String xAuthToken;
    private final String url;

    // Media type for JSON payload
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    // Constructor to initialize the URL, API key, and payload
    public ApiRequest(String url, String xAuthToken, JSONObject payload) {
        this.client = new OkHttpClient();
        System.out.println("URL: " + url);
        this.url = url;
        this.xAuthToken = xAuthToken;
        this.payload = payload;
    }

    // Method to perform the POST request
    public JSONObject post() {
        RequestBody body = RequestBody.create(payload.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("x-auth-token", xAuthToken)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            // Check if the request was successful
            if (!response.isSuccessful()) {
                String errorMessage = response.body().string();
                System.out.println("Request failed: " + errorMessage);
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("error", "Request failed: " + errorMessage);
                return errorResponse;
            }

            // Get the response body as a string
            String responseData = response.body().string();
            return new JSONObject(responseData);

        } catch (IOException e) {
            // Handle any I/O exceptions during the API request
            JSONObject errorResponse = new JSONObject();
            try {
                errorResponse.put("error", "Request failed: " + e.getMessage());
            } catch (org.json.JSONException jsonException) {
                jsonException.printStackTrace();
            }
            return errorResponse;
        } catch (org.json.JSONException e) {
            // Handle JSON parsing issues
            e.printStackTrace();
            JSONObject errorResponse = new JSONObject();
            try {
                errorResponse.put("error", "JSON parsing failed: " + e.getMessage());
            } catch (org.json.JSONException jsonException) {
                jsonException.printStackTrace();
            }
            return errorResponse;
        }
    }
}
