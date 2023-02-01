package com.example.notix.Network.Absence;

import com.example.notix.beans.Absence;
import com.example.notix.Network.NetConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PutAbsence extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "absences/";
    private String date;
    private int subject_id;
    private String student_dni;
    private String access;
    private Absence absenceRequest;
    private int response;

    public PutAbsence(String student_dni, Absence absenceRequest, int subject_id, String date, String access) {
        this.absenceRequest = absenceRequest;
        this.subject_id = subject_id;
        this.date = date;
        this.student_dni = student_dni;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + student_dni + "/" + subject_id + "/" + date);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            String jsonInputString = absenceRequest.toString();
            try (OutputStream postsend = httpURLConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                postsend.write(input, 0, input.length);
            }

            // Sending
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