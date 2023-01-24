package com.example.notix.Network.Student;

import com.example.notix.Network.Professor.beans.GradeEdition;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetGradeEditionByStudentDni extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "students/";
    private String token = "";
    private String student_dni;
    private ArrayList<GradeEdition> response = new ArrayList<GradeEdition>();

    public GetGradeEditionByStudentDni() {
        super();
    }

    public GetGradeEditionByStudentDni(String student_dni, String token) {
        super();
        this.student_dni = student_dni;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + student_dni + "/gradeEditions");
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

                JSONArray gradeEditions = new JSONArray(theUnprocessedJSON);

                for (int i = 0; i < gradeEditions.length(); i++) {
                    JSONObject object = gradeEditions.getJSONObject(i);

                    GradeEdition gradeEdition = new GradeEdition();
                    gradeEdition.setGrade_id(object.getInt("gradeId"));
                    gradeEdition.setTutor_dni(object.getString("tutorDni"));
                    gradeEdition.setFecha(object.getString("fecha"));
                    gradeEdition.setGrade_edition_id(object.getInt("gradeEdId"));
                    this.response.add(gradeEdition);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public ArrayList<GradeEdition> getResponse() {
        return response;
    }
}
