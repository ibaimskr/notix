package com.example.notix.Network.Note;

import com.example.notix.beans.Note;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetNotesByProfessorDni extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "notes/professor/";
    private String token = "";
    private String professor_dni;
    private ArrayList<Note> response = new ArrayList<Note>();

    public GetNotesByProfessorDni() {
        super();
    }

    public GetNotesByProfessorDni(String student_dni, String token) {
        super();
        this.professor_dni = professor_dni;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + professor_dni);
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
