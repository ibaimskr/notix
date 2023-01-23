package com.example.notix.network;

import com.example.notix.beans.Note;
import com.example.notix.beans.Subject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetSubjectsByDni extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "subjects/student/";
    private String dni;
    private String access = "";
    private ArrayList<Subject> response = new ArrayList<>();

    GetSubjectsByDni() { super(); }

    public GetSubjectsByDni(String dni, String access) {
        super();
        this.dni = dni;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + dni);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 418) {
                this.response = null;
            } else if (responseCode == HttpURLConnection.HTTP_OK) {
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
                    subject.setProfessor_dni(object.getString("professorDni"));
                    subject.setGrade_edition_id(object.getInt("gradeEditionId"));
                    subject.setName(object.getString("name"));
                    subject.setDuration(object.getInt("duration"));

                    this.response.add(subject);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public ArrayList<Subject> getResponse() { return response; }
}
