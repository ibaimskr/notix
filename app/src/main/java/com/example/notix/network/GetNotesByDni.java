package com.example.notix.network;

import com.example.notix.beans.Note;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetNotesByDni extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "notes/student/";
    private String dni;
    private String access = "";
    private ArrayList<Note> response = new ArrayList<>();

    public GetNotesByDni() { super(); }

    public GetNotesByDni(String dni, String access) {
        super();
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
                JSONArray notes = new JSONArray(theUnprocessedJSON);

                for (int i = 0; i < notes.length(); i++) {
                    JSONObject object = notes.getJSONObject(i);

                    Note note = new Note();
                    note.setStudent_dni(object.getString("studentDni"));
                    note.setSubject_id(object.getInt("subjectId"));
                    note.setEva1((float) object.getDouble("eva1"));
                    note.setEva2((float) object.getDouble("eva2"));
                    note.setEva3((float) object.getDouble("eva3"));
                    note.setFinal1(object.getInt("final1"));
                    note.setFinal2(object.getInt("final2"));
                    this.response.add(note);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    public ArrayList<Note> getResponse() {
        return response;
    }

}
