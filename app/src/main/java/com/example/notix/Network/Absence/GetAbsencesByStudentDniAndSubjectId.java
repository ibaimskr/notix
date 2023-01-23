package com.example.notix.Network.Absence;

import com.example.notix.beans.Absence;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetAbsencesByStudentDniAndSubjectId extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "absences/";
    private String token = "";
    private String student_dni;
    private int subject_id;
    private Absence response;

    public GetAbsencesByStudentDniAndSubjectId() {
        super();
    }

    public GetAbsencesByStudentDniAndSubjectId(String student_dni, int subject_id, String token) {
        super();
        this.subject_id = subject_id;
        this.student_dni = student_dni;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + student_dni + "/" + subject_id);
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

                    Absence absence = new Absence();
                    absence.setStudent_dni(object.getString("studentDni"));
                    absence.setSubject_id(object.getInt("subjectId"));
                    absence.setFoul(object.getString("foul"));
                    absence.setJustified(object.getBoolean("justified"));

                }


        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public Absence getResponse() {
        return response;
    }
}