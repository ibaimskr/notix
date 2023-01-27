package com.example.notix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notix.Network.Student.GetStudentsBySubjectId;
import com.example.notix.Network.Subject.GetSubjectsByProfessorDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.adapters.ProfessorSubjectsAdapter;
import com.example.notix.adapters.SubjectsAdapter;
import com.example.notix.beans.Subject;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProfessorSubjectsActivity extends AppCompatActivity {
    String dni_profe;
    String token;
    ArrayList<Subject> subjectsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_subjects);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        token = session.getStringData("jwtToken");
        dni_profe = session.getStringData("dni");

        ListView listViewSubjects = findViewById(R.id.listViewProfessorSubjects);
        ProfessorSubjectsAdapter professorSubjectsAdapter = new ProfessorSubjectsAdapter(this, R.layout.professor_subjects_layout, subjectsArrayList);


        if (isConnected()) {
            GetSubjectsByProfessorDni getSubjectsByProfessorDni = new GetSubjectsByProfessorDni(dni_profe, token );
            Thread thread = new Thread(getSubjectsByProfessorDni);
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
            }
            // Processing the answer
            ArrayList<Subject> subjects = getSubjectsByProfessorDni.getResponse();

            if (subjects == null) {
                Toast.makeText(getApplicationContext(), "Recibo null", Toast.LENGTH_LONG).show();
            } else {
                subjectsArrayList.addAll(subjects);
                listViewSubjects.setAdapter(professorSubjectsAdapter);
            }

        } else {
            Toast.makeText(getApplicationContext(), "no me conecto al server", Toast.LENGTH_LONG).show();
        }





        BottomNavigationView navigation = findViewById(R.id.subjectsBottomNavigation);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.professor_nav_main:
                        Intent i = new Intent(ProfessorSubjectsActivity.this, MainProfessorActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.professor_nav_notes:
                        Intent i3 = new Intent(ProfessorSubjectsActivity.this, ProfessorAddNotesActivity.class);
                        startActivity(i3);
                        finish();
                        break;
                    case R.id.professor_nav_absences:
                        Intent i2 = new Intent(ProfessorSubjectsActivity.this, ProfessorAddAbsencesActivity.class);
                        startActivity(i2);
                        finish();
                        break;
                    case R.id.professor_nav_students:
                        Intent i4 = new Intent(ProfessorSubjectsActivity.this, ProfessorStudentsActivity.class);
                        startActivity(i4);
                        finish();
                        break;
                    case R.id.professor_nav_mail:
                        Intent i5 = new Intent(ProfessorSubjectsActivity.this, ProfessorMailActivity.class);
                        startActivity(i5);
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