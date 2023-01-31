package com.example.notix;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notix.Network.Student.GetStudentByDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        TextView viewName = findViewById(R.id.viewStudentMainName);
        Button button = findViewById(R.id.button);
        BottomNavigationView navigation = findViewById(R.id.studentBottomNavigation);
        ImageButton buttonCloseSession = findViewById(R.id.imageButton2);

        buttonCloseSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainStudentActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        if (isConnected()) {
            GetStudentByDni getStudent = new GetStudentByDni(dni, token);
            Thread thread = new Thread(getStudent);
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            Student student = getStudent.getResponse();
            String studentName = (student.getName() + " " + student.getSurname());
            viewName.setText(studentName);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainStudentActivity.this, StudentNewPasswordActivity.class);
                startActivity(i);
            }
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.student_nav_notes:
                        Intent i = new Intent(MainStudentActivity.this, StudentNotesActivity.class);
                        startActivity(i);
                        break;
                    case R.id.student_nav_absences:
                        Intent i2 = new Intent(MainStudentActivity.this, StudentAbsencesActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.student_nav_subjects:
                        Intent i3 = new Intent(MainStudentActivity.this, StudentSubjectsActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.student_nav_mail:
                        Intent i4 = new Intent(MainStudentActivity.this, StudentMailActivity.class);
                        startActivity(i4);
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