package com.example.notix.Network.Student;

import com.example.notix.Network.NetConfiguration;
import com.example.notix.beans.Student;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetStudentsBySubjectId extends NetConfiguration implements Runnable {


    private final String theUrl = theBaseUrl + "students/subject/";
    private Integer subject_id;
    private Student student;
    private String access = "";

    private ArrayList<Student> response = new ArrayList<Student>();
    public GetStudentsBySubjectId() {
        super();
    }

    public GetStudentsBySubjectId(Integer subject_id, String access) {
        super();
        this.subject_id = subject_id;
        this.access = access;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(theUrl+subject_id);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + access);

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

                JSONArray students = new JSONArray(theUnprocessedJSON);

                for (int i = 0; i < students.length(); i++) {
                    JSONObject object = students.getJSONObject(i);

                    Student student = new Student();
                    student.setStudent_dni(object.getString("studentDni"));
                    student.setName(object.getString("name"));
                    student.setSurname(object.getString("surname"));
                    student.setBorn_date(object.getString("bornDate"));
                    student.setNationality(object.getString("nationality"));
                    student.setEmail(object.getString("email"));
                    student.setPhone(object.getString("phone"));
                    student.setPhoto(object.getString("photo"));
                    this.response.add(student);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public ArrayList<Student> getResponse() {
        return response;
    }


}
