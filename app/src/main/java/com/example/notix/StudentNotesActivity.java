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
import android.widget.Toast;

import com.example.notix.Network.Note.GetNotesByStudentDni;
import com.example.notix.Network.Subject.GetSubjectsByStudentDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.Note;
import com.example.notix.beans.Subject;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StudentNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notes);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        Button buttonEva1 = findViewById(R.id.buttonStudentNotesEva1);
        Button buttonEva2 = findViewById(R.id.buttonStudentNotesEva2);
        Button buttonEva3 = findViewById(R.id.buttonStudentNotesEva3);
        Button buttonPartial = findViewById(R.id.buttonStudentNotesPartial);
        BottomNavigationView navigation = findViewById(R.id.notesBottomNavigation);

        buttonEva1.setOnClickListener(view -> {
            Intent i = new Intent(StudentNotesActivity.this, StudentEva1Activity.class);
            startActivity(i);
            finish();
        });

        buttonEva2.setOnClickListener(view -> {
            Intent i = new Intent(StudentNotesActivity.this, StudentEva2Activity.class);
            startActivity(i);
            finish();
        });

        buttonEva3.setOnClickListener(view -> {
            Intent i = new Intent(StudentNotesActivity.this, StudentEva3Activity.class);
            startActivity(i);
            finish();
        });

        buttonPartial.setOnClickListener(view -> {
            Intent i = new Intent(StudentNotesActivity.this, StudentPartialActivity.class);
            startActivity(i);
            finish();
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.student_nav_main:
                        Intent i = new Intent(StudentNotesActivity.this, MainStudentActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.student_nav_absences:
                        Intent i2 = new Intent(StudentNotesActivity.this, StudentAbsencesActivity.class);
                        startActivity(i2);
                        finish();
                        break;
                    case R.id.student_nav_subjects:
                        Intent i3 = new Intent(StudentNotesActivity.this, StudentSubjectsActivity.class);
                        startActivity(i3);
                        finish();
                        break;
                    case R.id.student_nav_mail:
                        Intent i4 = new Intent(StudentNotesActivity.this, StudentMailActivity.class);
                        startActivity(i4);
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