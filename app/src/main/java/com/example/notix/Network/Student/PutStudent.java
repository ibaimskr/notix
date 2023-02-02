package com.example.notix.Network.Student;

import com.example.notix.beans.Student;
import com.example.notix.Network.NetConfiguration;
import com.example.notix.beans.StudentRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PutStudent extends NetConfiguration implements Runnable {
    private final String theUrl = theBaseUrl + "students/";
    private Student student;
    private String student_dni;
    private String token;
    private int response;

    public PutStudent(Student studentRequest, String student_dni, String token) {
        this.student = studentRequest;
        this.student_dni = student_dni;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + student_dni);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            String jsonInputString = student.toString();
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