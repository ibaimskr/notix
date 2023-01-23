package com.example.notix.Network.Subject;

import com.example.notix.beans.Subject;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetSubjectById extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "subjects/";
    private String token = "";
    private int subject_id;
    private Subject response;

    public GetSubjectById() {
        super();
    }

    public GetSubjectById(int subject_id, String token) {
        super();
        this.subject_id = subject_id;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + subject_id);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
//            httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 204) {
                this.response = null;

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

                JSONObject object = new JSONObject(theUnprocessedJSON);

                    Subject subject = new Subject();
                    subject.setSubject_id(object.getInt("subjectId"));
                    subject.setGrade_edition_id(object.getInt("gradeEditionId"));
                    subject.setProfessor_dni(object.getString("professorDni"));
                    subject.setName(object.getString("name"));
                    subject.setDuration(object.getInt("duration"));

            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public Subject getResponse() {
        return response;
    }
}
