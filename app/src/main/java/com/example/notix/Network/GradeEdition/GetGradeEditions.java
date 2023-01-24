package com.example.notix.Network.GradeEdition;


import com.example.notix.Network.Professor.beans.GradeEdition;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetGradeEditions extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "gradeEditions";
    private ArrayList<GradeEdition> response = new ArrayList<GradeEdition>();
    private String access = "";

    public GetGradeEditions(String access) {
        super();
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
//            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);

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
                    gradeEdition.setGrade_edition_id(object.getInt("gradeEditionId"));
                    gradeEdition.setFecha(object.getString("fecha"));
                    gradeEdition.setGrade_id(object.getInt("gradeId"));
                    gradeEdition.setTutor_dni(object.getString("tutorDni"));
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
