package com.example.notix.Network.Absence;

import com.example.notix.beans.Absence;
import com.example.notix.Network.NetConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostAbsence extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "absences";
    private Absence absence;
    private int response;
    private String access;

    public PostAbsence(Absence absenceRequest, String access) {
        this.absence = absenceRequest;
        this.access = access;
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
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            String jsonInputString = absence.toString();
            try (OutputStream postsend = httpURLConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                postsend.write(input, 0, input.length);
            }

            // Sending
            int responseCode = httpURLConnection.getResponseCode();

             if (responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                bufferedReader.close();
                this.response = 201;
            } else if (responseCode == 400) {
                 this.response = 400;
             } else if (responseCode == 409) {
                 this.response = 409;
             }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public int getResponse() {
        return response;
    }
}
