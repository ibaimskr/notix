package com.example.notix.Network.Professor;


import com.example.notix.beans.Professor;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetProfessorByDni extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "professors/";
    private String professor_dni;
    private Professor professor ;
    private String access = "";

    public GetProfessorByDni(String professor_dni, String access) {
        super();
        this.professor_dni = professor_dni;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + professor_dni);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);

            // Sending...
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == 204) {
                this.professor = null;

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

                professor = new Professor();
                professor.setProfessor_dni(object.getString("professorDni"));
                professor.setAdress(object.getString("addres"));
                professor.setEmail(object.getString("email"));
                professor.setName(object.getString("name"));
                professor.setNationality(object.getString("nationality"));
                professor.setPhoto(object.getString("photo"));
                professor.setSurname(object.getString("surname"));
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public Professor getResponse() {
        return professor;
    }
}
