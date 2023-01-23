package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

public class ProfessorAddAbsencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_add_absences);

        Spinner spinnerStudents = findViewById(R.id.spinnerProfessorAddAbsencesStudent);
        Spinner spinnerSubjects = findViewById(R.id.spinnerProfessorAddAbsencesSubject);
        Button buttonAddAbsence = findViewById(R.id.buttonProfessorAddAbsencesAdd);
        Button buttonSeeAbsences = findViewById(R.id.buttonProfessorAddAbsencesAbsences);



    }
}