package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notix.Network.Subject.GetSubjectsByStudentDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.adapters.SubjectsAdapter;
import com.example.notix.Network.Professor.beans.Professor;
import com.example.notix.Network.Professor.beans.Subject;

import java.util.ArrayList;

public class StudentSubjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subjects);

        Bundle extras = getIntent().getExtras();
        //String access = extras.getString("access");
        String dni = extras.getString("dni");

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");

        ListView listView = findViewById(R.id.listViewStudentSubjects);

        ArrayList<Subject> subjectsArrayList = new ArrayList<>();
        ArrayList<Professor> tutorsArrayList = new ArrayList<>();
        SubjectsAdapter subjectsAdapter = new SubjectsAdapter(this, R.layout.subject_layout, subjectsArrayList, tutorsArrayList);
        listView.setAdapter(subjectsAdapter);

        if (isConnected()) {
            GetSubjectsByStudentDni getSubjects = new GetSubjectsByStudentDni(dni, token);
            //GetProfessorByDni getTutors = new GetProfessorByDni(dni, access);
            Thread thread = new Thread(getSubjects);
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            ArrayList<Subject> subjects = getSubjects.getResponse();

            if (subjects != null) {
                subjectsArrayList.addAll(subjects);
                ((ListView) findViewById(R.id.listViewStudentSubjects)).setAdapter(subjectsAdapter);

            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_data), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isConnected() {
        boolean ret = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
}