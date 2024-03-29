package com.example.notix.Network.Absence;

import com.example.notix.Network.NetConfiguration;
import com.example.notix.beans.Absence;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetJustifiedAbsencesByDni extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "absences/";
    private String dni;
    private String access = "";
    private ArrayList<Absence> response = new ArrayList<>();

    public GetJustifiedAbsencesByDni(String dni, String access){
        super();
        this.dni = dni;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + dni + "/justified");
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

                JSONArray absences = new JSONArray(theUnprocessedJSON);

                for (int i = 0; i < absences.length(); i++) {
                    JSONObject object = absences.getJSONObject(i);

                    Absence absence = new Absence();
                    absence.setStudent_dni(object.getString("studentDni"));
                    absence.setSubject_id(object.getInt("subjectId"));
                    String dni = absence.getStudent_dni();
                    absence.setFoul(object.getString("foul"));
                    absence.setJustified(object.getBoolean("justified"));
                    this.response.add(absence);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public ArrayList<Absence> getResponse() { return response; }
}
