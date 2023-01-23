package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);

        Bundle extras = getIntent().getExtras();
        String access = extras.getString("access");
        String dni = extras.getString("dni");

        Button buttonNotes = findViewById(R.id.buttonNotes);
        Button buttonAbsences = findViewById(R.id.buttonAbsences);
        Button buttonSubjects = findViewById(R.id.buttonSubjects);

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
}