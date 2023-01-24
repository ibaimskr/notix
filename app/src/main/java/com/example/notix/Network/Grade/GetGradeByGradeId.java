package com.example.notix.Network.Grade;

import com.example.notix.beans.Grade;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetGradeByGradeId extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "grades/";
    private int grade_id;
    private Grade grade;
    private String access = "";

    public GetGradeByGradeId(int grade_id, String access) {
        super();
        this.grade_id = grade_id;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + grade_id);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
//            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 204) {
                this.grade = null;

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

                grade = new Grade();
                grade.setGrade_id(object.getInt("gradeId"));
                grade.setLanguage(object.getString("language"));
                grade.setName(object.getString("name"));
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public Grade getResponse() {
        return grade;
    }
}
