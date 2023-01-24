package com.example.notix.Network.Grade;

import com.example.notix.beans.Grade;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetGrades extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "grades";
    private ArrayList<Grade> response = new ArrayList<Grade>();
    private String access = "";

    public GetGrades(String access) {
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

                JSONArray grades = new JSONArray(theUnprocessedJSON);

                for (int i = 0; i < grades.length(); i++) {
                    JSONObject object = grades.getJSONObject(i);

                    Grade grade = new Grade();
                    grade.setGrade_id(object.getInt("gradeId"));
                    grade.setLanguage(object.getString("language"));
                    grade.setName(object.getString("name"));
                    this.response.add(grade);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public ArrayList<Grade> getResponse() {
        return response;
    }
}
