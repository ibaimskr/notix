package com.example.notix.Network.GradeEdition;


import com.example.notix.Network.Professor.beans.GradeEdition;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetGradeEditionByGradeEditionId extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "gradeEditions/";
    private String grade_edition_id;
    private GradeEdition gradeEdition ;
    private String access = "";

    public GetGradeEditionByGradeEditionId(String grade_edition_id, String access) {
        super();
        this.grade_edition_id = grade_edition_id;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + grade_edition_id);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
//            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 204) {
                this.gradeEdition = null;

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

                gradeEdition = new GradeEdition();
                gradeEdition.setGrade_edition_id(object.getInt("gradeEditionId"));
                gradeEdition.setFecha(object.getString("fecha"));
                gradeEdition.setGrade_id(object.getInt("gradeId"));
                gradeEdition.setTutor_dni(object.getString("tutorDni"));
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public GradeEdition getResponse() {
        return gradeEdition;
    }
}
