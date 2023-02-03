package com.example.notix.Network.Grade;

import com.example.notix.Network.NetConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteGrade extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "gradesAdmin/";
    private int grade_id;
    private String access;
    private int response;

    public DeleteGrade(int grade_id, String access) {
        this.grade_id = grade_id;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + grade_id);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("DELETE");
//            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 204) {
                this.response = 204;
            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                bufferedReader.close();
                this.response = 200;
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public int getResponse() {
        return response;
    }
}
