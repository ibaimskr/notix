package com.example.notix.Network.Subject;

import com.example.notix.beans.Subject;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetSubjectsByStudentDni extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "subjects/student/";
    private String token = "";
    private String student_dni;
    private ArrayList<Subject> subjectResponse = new ArrayList<Subject>();

    public GetSubjectsByStudentDni() {
        super();
    }

    public GetSubjectsByStudentDni(String student_dni, String token) {
        super();
        this.student_dni = student_dni;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + student_dni);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 204) {
                subjectResponse = null;

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

                JSONArray subjects = new JSONArray(theUnprocessedJSON);

                for (int i = 0; i < subjects.length(); i++) {
                    JSONObject object = subjects.getJSONObject(i);

                    Subject subject = new Subject();
                    subject.setSubject_id(object.getInt("subjectId"));
                    subject.setGrade_edition_id(object.getInt("gradeEditionId"));
                    subject.setProfessor_dni(object.getString("professorDni"));
                    subject.setName(object.getString("name"));
                    subject.setDuration(object.getInt("duration"));
                    subjectResponse.add(subject);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public ArrayList<Subject> getResponse() {
        return subjectResponse;
    }
}
