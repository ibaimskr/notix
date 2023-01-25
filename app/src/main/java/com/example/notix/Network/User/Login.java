package com.example.notix.Network.User;

import android.content.Context;
import android.util.Log;

import com.example.notix.Network.NetConfiguration;
import com.example.notix.beans.AuthRequest;
import com.example.notix.beans.AuthResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "users/login";
    private AuthRequest request;
    private AuthResponse response;
    Context context;

    public Login(AuthRequest userComp) {
        super();
        this.request = userComp;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            String jsonInputString = request.toString();
            try (OutputStream postsend = httpURLConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                postsend.write(input, 0, input.length);
            }

            // Sending
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 400) {
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.setError(responseCode);
                this.response = errorResponse;

            } else if (responseCode == 401) {
                AuthResponse errorResponse = new AuthResponse();
                errorResponse.setError(responseCode);
                this.response = errorResponse;

            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                // Response...
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }

                bufferedReader.close();

                // Processing the JSON...
                String theUnprocessedJSON = response.toString();
                AuthResponse response1 = new Gson().fromJson(theUnprocessedJSON, AuthResponse.class);

                this.response = response1;
            }
 //
        } catch (Exception e) {
            Log.e("ERROR: ", e.getMessage());
        }
    }

    public AuthResponse getResponse(AuthRequest request) {
        return response; }
}
