package com.example.notix.Network.Absence;

import com.example.notix.Network.NetConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteAbsence extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "absencesDelete/";
    private String student_dni;
    private int subject_id;
    private String date;
    private int response;
    private String access;

    public DeleteAbsence(String student_dni, int subject_id, String date, String access) {
        this.student_dni = student_dni;
        this.subject_id = subject_id;
        this.date = date;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + student_dni + "/" + subject_id + "/" + date);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("DELETE");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);

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

