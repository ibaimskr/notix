package com.example.notix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notix.Network.User.SessionManager;
import com.example.notix.adapters.AbsencesAdapter;
import com.example.notix.beans.Absence;
import com.example.notix.beans.Subject;
import com.example.notix.Network.Absence.GetAbsencesByStudentDni;
import com.example.notix.Network.Subject.GetSubjectsByStudentDni;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StudentAbsencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_absences);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        Button buttonJustified = findViewById(R.id.buttonStudentAbsencesJustified);
        ListView listView = findViewById(R.id.listViewStudentAbsences);
        BottomNavigationView navigation = findViewById(R.id.absencesBottomNavigation);

        ArrayList<Absence> absencesArrayList = new ArrayList<>();
        ArrayList<Subject> subjectsArrayList = new ArrayList<>();
        AbsencesAdapter absencesAdapter = new AbsencesAdapter(this, R.layout.absence_layout, absencesArrayList, subjectsArrayList);
        listView.setAdapter(absencesAdapter);

        if (isConnected()) {
            GetAbsencesByStudentDni getAbsences = new GetAbsencesByStudentDni(dni, token);
            GetSubjectsByStudentDni getSubjects = new GetSubjectsByStudentDni(dni, token);
            Thread thread1 = new Thread(getAbsences);
            Thread thread2 = new Thread(getSubjects);
            try {
                thread1.start();
                thread2.start();
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            ArrayList<Absence> absences = getAbsences.getResponse();
            ArrayList<Subject> subjects = getSubjects.getResponse();

            if (absences != null) {
                absencesArrayList.addAll(absences);
                subjectsArrayList.addAll(subjects);
                ((ListView) findViewById(R.id.listViewStudentAbsences)).setAdapter(absencesAdapter);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_server_data), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }

        buttonJustified.setOnClickListener(view -> {
            Intent i = new Intent(StudentAbsencesActivity.this, StudentJustifiedAbsencesActivity.class);
            startActivity(i);
            finish();
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.student_nav_main:
                        Intent i = new Intent(StudentAbsencesActivity.this, MainStudentActivity.class);
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