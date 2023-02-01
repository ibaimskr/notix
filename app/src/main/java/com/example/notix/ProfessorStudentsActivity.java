package com.example.notix;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notix.Network.Student.GetStudentsByProfessorDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.adapters.ProfessorStudentsAdapter;
import com.example.notix.beans.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ProfessorStudentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_students);

        BottomNavigationView navigation = findViewById(R.id.professorBottomNavigation);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni_profe = session.getStringData("dni");

        ListView listStudents = findViewById(R.id.listViewStudentsProfessor);
        ArrayList<Student> students = new ArrayList<>();
        ProfessorStudentsAdapter studentsAdapter = new ProfessorStudentsAdapter(this, R.layout.professor_student_layout, students);
        listStudents.setAdapter(studentsAdapter);

        if (isConnected()) {
            GetStudentsByProfessorDni getStudentsByProfessorDni = new GetStudentsByProfessorDni(dni_profe, token);
            Thread thread = new Thread(getStudentsByProfessorDni);
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
            }
            // Processing the answer
            ArrayList<Student> studentsArrayList = getStudentsByProfessorDni.getResponse();

            if (students == null) {
                Toast.makeText(getApplicationContext(), "recibo null colega", Toast.LENGTH_LONG).show();
            } else {
                students.addAll(studentsArrayList);
            }

        } else {
            Toast.makeText(getApplicationContext(), "no me conecto al server", Toast.LENGTH_LONG).show();
        }

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.professor_nav_main:
                        Intent i = new Intent(ProfessorStudentsActivity.this, MainProfessorActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    default:
                }
                return true;
            }
        });
    }

    public boolean isConnected() {
        boolean ret = false;
        try {
            ConnectivityManager connectivityManager;
            connectivityManager = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "comunication error", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
}