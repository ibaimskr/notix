package com.example.notix;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notix.Network.Professor.GetProfessorByDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.Professor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainProfessorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professor);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        TextView viewName = findViewById(R.id.viewProfessorMainName);
        BottomNavigationView navigation = findViewById(R.id.professorBottomNavigation);
        ImageButton buttonCloseSession = findViewById(R.id.imageButton);

        buttonCloseSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainProfessorActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        if (isConnected()) {
            GetProfessorByDni getProfessor = new GetProfessorByDni(dni, token);
            Thread thread = new Thread(getProfessor);
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            Professor professor = getProfessor.getResponse();
            String professorName = (professor.getName() + " " + professor.getSurname());
            viewName.setText(professorName);
        }

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.professor_nav_notes:
                        Intent i = new Intent(MainProfessorActivity.this, ProfessorAddNotesActivity.class);
                        startActivity(i);
                        break;
                    case R.id.professor_nav_absences:
                        Intent i2 = new Intent(MainProfessorActivity.this, ProfessorAddAbsencesActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.professor_nav_subjects:
                        Intent i3 = new Intent(MainProfessorActivity.this, ProfessorSubjectsActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.professor_nav_students:
                        Intent i4 = new Intent(MainProfessorActivity.this, ProfessorStudentsActivity.class);
                        startActivity(i4);
                        break;
                    case R.id.professor_nav_mail:
                        Intent i5 = new Intent(MainProfessorActivity.this, ProfessorMailActivity.class);
                        startActivity(i5);
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