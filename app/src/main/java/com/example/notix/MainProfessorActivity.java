package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainProfessorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_professor);

        Bundle extras = getIntent().getExtras();
        String access = extras.getString("access");
        String dni = extras.getString("dni");

        Button buttonNotes = findViewById(R.id.buttonProfessorNotes);
        Button buttonAbsences = findViewById(R.id.buttonProfessorAbsences);
        Button buttonReunion = findViewById(R.id.buttonProfessorReunion);

        buttonNotes.setOnClickListener(view -> {
            Intent i = new Intent(MainProfessorActivity.this, ProfessorNotesActivity.class);
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

    }
}