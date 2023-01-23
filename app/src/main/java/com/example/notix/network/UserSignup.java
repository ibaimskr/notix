package com.example.notix.network;

import com.example.notix.beans.AuthRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserSignup extends NetConfiguration implements Runnable {
    private final String theUrl = theBaseUrl + "users/signup";
    private int response;
    AuthRequest user;

    public UserSignup(AuthRequest user) {
        super();
        this.user = user;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(theUrl);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            String jsonInputString = user.toString();
            try (OutputStream postSend = httpURLConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                postSend.write(input, 0, input.length);
            }

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 500) {
                this.response = 500;
            } else if (responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                bufferedReader.close();
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    public int getResponse() {
        return response;
    }

}
