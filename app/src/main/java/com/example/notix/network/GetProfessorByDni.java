package com.example.notix.network;

import com.example.notix.beans.Absence;
import com.example.notix.beans.Professor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetProfessorByDni extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "professors/";
    private String dni;
    private String access = "";
    private ArrayList<Professor> response = new ArrayList<>();

    public GetProfessorByDni() { super(); }

    public GetProfessorByDni(String dni, String access) {
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

                JSONArray professors = new JSONArray(theUnprocessedJSON);

                for (int i = 0; i < professors.length(); i++) {
                    JSONObject object = professors.getJSONObject(i);

                    Professor professor = new Professor();
                    professor.setProfessor_dni(object.getString("professorDni"));
                    professor.setName(object.getString("name"));
                    professor.setSurname(object.getString("surname"));
                    professor.setNationality(object.getString("nationality"));
                    professor.setEmail(object.getString("email"));
                    professor.setAdress(object.getString("adress"));
                    professor.setPhoto(object.getString("photo"));
                    this.response.add(professor);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public ArrayList<Professor> getResponse() { return response; }
}
