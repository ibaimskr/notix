package com.example.notix.Network.Note;

import com.example.notix.Network.Professor.beans.Note;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetNotesByStudentDniAndSubjectId extends NetConfiguration implements Runnable {

    private final String theUrl = theBaseUrl + "notes";
    private String student_dni;
    private int subject_id;
    private String access = "";
    private Note response;

    public GetNotesByStudentDniAndSubjectId(String student_dni, int subject_id, String access) {
        super();
        this.subject_id = subject_id;
        this.student_dni = student_dni;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + student_dni);
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

                JSONObject object = new JSONObject(theUnprocessedJSON);

                    Note note = new Note();
                    note.setStudent_dni(object.getString("studentDni"));
                    note.setSubject_id(object.getInt("subjectId"));
                    note.setEva1((float) object.getDouble("eva1"));
                    note.setEva2((float) object.getDouble("eva2"));
                    note.setEva3((float) object.getDouble("eva3"));
                    note.setFinal1(object.getInt("final1"));
                    note.setFinal2(object.getInt("final2"));
                }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public Note getResponse() {
        return response;
    }
}
