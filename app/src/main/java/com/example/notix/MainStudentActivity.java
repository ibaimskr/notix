package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notix.Network.Student.GetStudentByDni;
import com.example.notix.Network.Professor.beans.Student;

public class MainStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);

        Bundle extras = getIntent().getExtras();
        String access = extras.getString("access");
        String dni = extras.getString("dni");

        TextView viewName = findViewById(R.id.viewStudentMainName);
        Button buttonNotes = findViewById(R.id.buttonStudentMainNotes);
        Button buttonAbsences = findViewById(R.id.buttonStudentMainAbsences);
        Button buttonSubjects = findViewById(R.id.buttonStudentMainSubjects);
//
        if (isConnected()) {
            GetStudentByDni getStudent = new GetStudentByDni(dni, access);
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
//

        buttonNotes.setOnClickListener(view -> {
            Intent i = new Intent(MainStudentActivity.this, StudentEva1Activity.class);
            i.putExtra("access", access);
            i.putExtra("dni", dni);
            startActivity(i);
        });

        buttonAbsences.setOnClickListener(view -> {
            Intent i = new Intent(MainStudentActivity.this, StudentAbsencesActivity.class);
            i.putExtra("access", access);
            i.putExtra("dni", dni);
            startActivity(i);
        });

        buttonSubjects.setOnClickListener(view -> {
            Intent i = new Intent(MainStudentActivity.this, StudentSubjectsActivity.class);
            i.putExtra("access", access);
            i.putExtra("dni", dni);
            startActivity(i);
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