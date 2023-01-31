package com.example.notix;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notix.Network.Absence.PostAbsence;
import com.example.notix.Network.Student.GetStudentsBySubjectIdAndProfessorDni;
import com.example.notix.Network.Subject.GetSubjectsByProfessorDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.Absence;
import com.example.notix.beans.Student;
import com.example.notix.beans.Subject;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ProfessorAddAbsencesActivity extends AppCompatActivity {

    String dni_alumno;
    String dni_profe;
    int subject_id;
    String date;
    Boolean justified = false;
    Subject selectedSubject = new Subject();
    Student selectedStudent = new Student();
    ArrayList<Subject> subjectsArrayList = new ArrayList<>();
    ArrayList<Student> studentsArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_add_absences);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        dni_profe = session.getStringData("dni");

        Spinner spinnerStudents = findViewById(R.id.spinnerProfessorAddNotesStudent);
        Spinner spinnerSubjects = findViewById(R.id.spinnerProfessorAddNotesSubject);
        Spinner spinnerFoul = findViewById(R.id.spinnerProfessorAddAbsencesFoul);
        Button buttonAddAbsence = findViewById(R.id.buttonProfessorAddAbsencesAdd);
        Button buttonAbsences = findViewById(R.id.buttonProfessorAddAbsencesAbsences);
        EditText editeTexDate = findViewById(R.id.editTextDateProfessorAddAbsences);
        BottomNavigationView navigation = findViewById(R.id.absencesBottomNavigation);


        ArrayList<String> foulArrayList = new ArrayList<>();
        foulArrayList.add("True");
        foulArrayList.add("False");
        ArrayAdapter foulAdapter = new ArrayAdapter(ProfessorAddAbsencesActivity.this, android.R.layout.simple_spinner_dropdown_item, foulArrayList);
        spinnerFoul.setAdapter(foulAdapter);



        if (isConnected()) {
            GetSubjectsByProfessorDni getSubjectsByProfessorDni = new GetSubjectsByProfessorDni(dni_profe,token);
            Thread thread2 = new Thread(getSubjectsByProfessorDni);
            try {
                thread2.start();
                thread2.join();
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            subjectsArrayList = getSubjectsByProfessorDni.getResponse();

            if (subjectsArrayList == null) {
                Toast.makeText(getApplicationContext(), "Recibo null", Toast.LENGTH_LONG).show();
            } else {
                ArrayAdapter subjectAdapter = new ArrayAdapter(ProfessorAddAbsencesActivity.this, android.R.layout.simple_spinner_dropdown_item, subjectsArrayList);
                spinnerSubjects.setAdapter(subjectAdapter);
            }
        } else {
            Toast.makeText(getApplicationContext(), "no me conecto al server", Toast.LENGTH_LONG).show();
        }

        spinnerSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String token = session.getStringData("jwtToken");
                if(studentsArrayList != null) {
                    studentsArrayList.clear();
                    ArrayList<Student> arrayListVacio = new ArrayList<>();
                    Student student = new Student();
                    student.setName("");
                    student.setSurname("");
                    arrayListVacio.add(student);
                    ArrayAdapter studentAdapter = new ArrayAdapter(ProfessorAddAbsencesActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListVacio);
                    spinnerStudents.setAdapter(studentAdapter);
                }

                selectedSubject = (Subject) spinnerSubjects.getSelectedItem();
                if (isConnected()) {
                    GetStudentsBySubjectIdAndProfessorDni getStudentsBySubjectIdAndProfessorDni = new GetStudentsBySubjectIdAndProfessorDni(selectedSubject.getSubject_id(), dni_profe ,token );
                    Thread thread = new Thread(getStudentsBySubjectIdAndProfessorDni);
                    try {
                        thread.start();
                        thread.join();
                    } catch (InterruptedException e) {
                    }
                    // Processing the answer
                    studentsArrayList = getStudentsBySubjectIdAndProfessorDni.getResponse();

                    if (studentsArrayList == null) {
                        Toast.makeText(getApplicationContext(), "Recibo null", Toast.LENGTH_LONG).show();
                    } else {
                        ArrayAdapter studentAdapter = new ArrayAdapter(ProfessorAddAbsencesActivity.this, android.R.layout.simple_spinner_dropdown_item, studentsArrayList);
                        spinnerStudents.setAdapter(studentAdapter);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "no me conecto al server", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerStudents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStudent = (Student) spinnerStudents.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFoul.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerFoul.getSelectedItem().toString().equalsIgnoreCase("True")){
                    justified = true;
                } else {
                    justified = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonAddAbsence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date = editeTexDate.getText().toString();
                dni_alumno = selectedStudent.getStudent_dni();
                subject_id = selectedSubject.getSubject_id();

                Absence absence = new Absence();
                absence.setStudent_dni(dni_alumno);
                absence.setSubject_id(subject_id);
                absence.setFoul(date);
                absence.setJustified(justified);

                if (isConnected()) {
                    PostAbsence absenceService = new PostAbsence(absence, token);
                    Thread thread = new Thread(absenceService);
                    try {
                        thread.start();
                        thread.join();
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }
                    // Processing the answer
                   int response = absenceService.getResponse();

                    if (response == 409) {
                        Toast.makeText(getApplicationContext(), "Ya existe esta falta", Toast.LENGTH_LONG).show();
                    } else if (response == 201) {
                        Toast.makeText(getApplicationContext(), "Falta creada correctamente", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "no me conecto al server", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonAbsences.setOnClickListener(view -> {
            Intent i = new Intent(ProfessorAddAbsencesActivity.this, ProfessorAbsencesActivity.class);
            startActivity(i);
            finish();
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.professor_nav_main:
                        Intent i = new Intent(ProfessorAddAbsencesActivity.this, MainProfessorActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.professor_nav_notes:
                        Intent i2 = new Intent(ProfessorAddAbsencesActivity.this, ProfessorAddNotesActivity.class);
                        startActivity(i2);
                        finish();
                        break;
                    case R.id.professor_nav_subjects:
                        Intent i3 = new Intent(ProfessorAddAbsencesActivity.this, ProfessorSubjectsActivity.class);
                        startActivity(i3);
                        finish();
                        break;
                    case R.id.professor_nav_students:
                        Intent i4 = new Intent(ProfessorAddAbsencesActivity.this, ProfessorStudentsActivity.class);
                        startActivity(i4);
                        finish();
                        break;
                    case R.id.professor_nav_mail:
                        Intent i5 = new Intent(ProfessorAddAbsencesActivity.this, ProfessorMailActivity.class);
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