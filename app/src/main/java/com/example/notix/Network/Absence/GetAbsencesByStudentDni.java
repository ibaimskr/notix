package com.example.notix.Network.Absence;

import com.example.notix.beans.Absence;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetAbsencesByStudentDni extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "absences/";
    private String token = "";
    private String student_dni;
    private ArrayList<Absence> response = new ArrayList<Absence>();

    public GetAbsencesByStudentDni() {
        super();
    }

    public GetAbsencesByStudentDni(String student_dni, String token) {
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

                JSONArray absences = new JSONArray(theUnprocessedJSON);

                for (int i = 0; i < absences.length(); i++) {
                    JSONObject object = absences.getJSONObject(i);

                    Absence absence = new Absence();
                    absence.setStudent_dni(object.getString("studentDni"));
                    absence.setSubject_id(object.getInt("subjectId"));
                    absence.setFoul(object.getString("foul"));
                    absence.setJustified(object.getBoolean("justified"));
                    this.response.add(absence);
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