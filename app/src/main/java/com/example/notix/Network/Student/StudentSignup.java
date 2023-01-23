package com.example.notix.Network.Student;

import com.example.notix.beans.Student;
import com.example.notix.Network.NetConfiguration;
import com.example.notix.beans.StudentRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StudentSignup extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "students";
    private Student student;
    private int response;
    private String token;

    public StudentSignup(Student studentRequest, String token) {
        this.student = studentRequest;
        this.token = token;
    }

    public StudentSignup(StudentRequest student) {
        super();
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
//            httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            String jsonInputString = student.toString();
            try (OutputStream postsend = httpURLConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                postsend.write(input, 0, input.length);
            }

            // Sending
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 409) {
                this.response = 409;
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