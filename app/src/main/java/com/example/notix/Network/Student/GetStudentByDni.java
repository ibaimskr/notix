package com.example.notix.Network.Student;

import com.example.notix.beans.Student;
import com.example.notix.Network.NetConfiguration;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetStudentByDni extends NetConfiguration implements Runnable{
    private final String theUrl = theBaseUrl + "students/";
    private String token = "";
    private String student_dni;
    private Student student;

    public GetStudentByDni() {
        super();
    }

    public GetStudentByDni(String token) {
        super();
        this.token = token;
    }

    @Override
    public void run() {
        try {
            // The URL
            URL url = new URL(theUrl + student_dni);
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

                    JSONObject object = new JSONObject(theUnprocessedJSON);

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


        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public Student getResponse() {
        return student;
    }
}
