package com.example.notix.Network.Subject;

import com.example.notix.Network.NetConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteSubject extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "subjectsUpdate/";

    private int subject_id;
    private int response;
    private String token;

    public DeleteSubject(int subject_id, String token) {
        this.subject_id = subject_id;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + subject_id);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("DELETE");
//            httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);

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
