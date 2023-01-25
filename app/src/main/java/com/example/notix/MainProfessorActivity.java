package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainProfessorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professor);

        Bundle extras = getIntent().getExtras();
        String access = extras.getString("access");
        String dni = extras.getString("dni");

        TextView viewName = findViewById(R.id.viewProfessorMainName);
        Button buttonNotes = findViewById(R.id.buttonProfessorMainNotes);
        Button buttonAbsences = findViewById(R.id.buttonProfessorMainAbsences);
        Button buttonSubjects = findViewById(R.id.buttonProfessorMainSubjects);
        Button buttonStudents = findViewById(R.id.buttonProfessorMainStudents);
        Button buttonReunion = findViewById(R.id.buttonProfessorMainReunion);

        buttonNotes.setOnClickListener(view -> {
            Intent i = new Intent(MainProfessorActivity.this, ProfessorAddNotesActivity.class);
            i.putExtra("access", access);
            i.putExtra("dni", dni);
            startActivity(i);
        });

        buttonAbsences.setOnClickListener(view -> {
            Intent i = new Intent(MainProfessorActivity.this, ProfessorAddAbsencesActivity.class);
            i.putExtra("access", access);
            i.putExtra("dni", dni);
            startActivity(i);
        });

        buttonSubjects.setOnClickListener(view -> {
            Intent i = new Intent(MainProfessorActivity.this, ProfessorSubjectsActivity.class);
            i.putExtra("access", access);
            i.putExtra("dni", dni);
            startActivity(i);
        });

        buttonStudents.setOnClickListener(view -> {
            Intent i = new Intent(MainProfessorActivity.this, ProfessorStudentsActivity.class);
            i.putExtra("access", access);
            i.putExtra("dni", dni);
            startActivity(i);
        });

        buttonReunion.setOnClickListener(view -> {
            Intent i = new Intent(MainProfessorActivity.this, ProfessorReunionActivity.class);
            i.putExtra("access", access);
            i.putExtra("dni", dni);
            startActivity(i);
        });
    }
}