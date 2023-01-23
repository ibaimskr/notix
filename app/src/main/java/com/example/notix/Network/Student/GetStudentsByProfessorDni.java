package com.example.notix.Network.Student;

import com.example.notix.Network.NetConfiguration;
import com.example.notix.beans.Student;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetStudentsByProfessorDni extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "students/professor/";
    private String token = "";
    private String professor_dni;
    private Student student;

    public GetStudentsByProfessorDni() {
        super();
    }

    public GetStudentsByProfessorDni(String token, String professor_dni) {
        super();
        this.token = token;
        this.professor_dni = professor_dni;
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
                this.student = null;

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

                JSONArray students = new JSONArray(theUnprocessedJSON);

                for (int i = 0; i < students.length(); i++) {
                    JSONObject object = students.getJSONObject(i);

                    student = new Student();
                    student.setStudent_dni(object.getString("studentDni"));
                    student.setName(object.getString("name"));
                    student.setSurname(object.getString("surname"));
                    student.setBorn_date(object.getString("bornDate"));
                    student.setNationality(object.getString("nationality"));
                    student.setEmail(object.getString("email"));
                    student.setPhone(object.getString("phone"));
                    student.setPhoto(object.getString("photo"));
                    }
                }


        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public Student getResponse() {
        return student;
    }
}
