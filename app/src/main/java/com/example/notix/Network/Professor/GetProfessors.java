package com.example.notix.Network.Professor;

import com.example.notix.beans.Professor;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetProfessors extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "professors";
    private ArrayList<Professor> response = new ArrayList<Professor>();
    private String access = "";

    public GetProfessors() {
        super();
    }

    public GetProfessors(String access) {
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

                JSONArray professors = new JSONArray(theUnprocessedJSON);

                for (int i = 0; i < professors.length(); i++) {
                    JSONObject object = professors.getJSONObject(i);

                    Professor professor = new Professor();
                    professor.setProfessor_dni(object.getString("professorDni"));
                    professor.setAdress(object.getString("addres"));
                    professor.setEmail(object.getString("email"));
                    professor.setName(object.getString("name"));
                    professor.setNationality(object.getString("nationality"));
                    professor.setPhoto(object.getString("photo"));
                    professor.setSurname(object.getString("surname"));
                    this.response.add(professor);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public ArrayList<Professor> getResponse() {
        return response;
    }
}
