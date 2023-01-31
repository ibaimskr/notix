package com.example.notix.Network.Absence;

import android.util.Log;

import com.example.notix.Network.NetConfiguration;
import com.example.notix.beans.Absence;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetAbsencesByStudentDniAndSubjectId extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "absences/";
    private String student_dni;
    private int subject_id;
    private String access = "";
    private ArrayList<Absence> response = new ArrayList<>();

    public GetAbsencesByStudentDniAndSubjectId(String student_dni, int subject_id, String access) {
        super();
        this.subject_id = subject_id;
        this.student_dni = student_dni;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + student_dni + "/" + subject_id);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 204) {
                this.response = null;

            } else if (responseCode == HttpURLConnection.HTTP_OK) {
                // Response...
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer responseBuffer = new StringBuffer();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    responseBuffer.append(inputLine);
                }
                bufferedReader.close();

                // Processing the JSON...
                String theUnprocessedJSON = responseBuffer.toString();

                JSONArray absences = new JSONArray(theUnprocessedJSON);

                for (int i = 0; i < absences.length(); i++) {
                    JSONObject object = absences.getJSONObject(i);

                    Absence absence = new Absence();
                    absence.setStudent_dni(object.getString("studentDni"));
                    absence.setSubject_id(object.getInt("subjectId"));
                    absence.setFoul(object.getString("foul"));
                    absence.setJustified(object.getBoolean("justified"));
                    response.add(absence);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }

    }

    public ArrayList<Absence> getResponse() {
        return response;
    }
}


