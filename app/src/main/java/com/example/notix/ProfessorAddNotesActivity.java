package com.example.notix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.notix.Network.Note.GetNotesByStudentDniAndSubjectId;
import com.example.notix.Network.Student.GetStudentsBySubjectId;
import com.example.notix.Network.Subject.GetSubjectsByProfessorDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.Note;
import com.example.notix.beans.Student;
import com.example.notix.beans.Subject;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ProfessorAddNotesActivity extends AppCompatActivity {

    String dni_profe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_add_notes);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        dni_profe = session.getStringData("dni");

        EditText eva1= findViewById(R.id.editTextNumberDecimalEva1);
        EditText eva2= findViewById(R.id.editTextNumberDecimalEva2);
        EditText eva3= findViewById(R.id.editTextNumberDecimalEva3);
        EditText final1= findViewById(R.id.editTextNumberDecimalFinal1);
        EditText final2= findViewById(R.id.editTextNumberDecimalFinal2);
        Spinner spinnerStudents = findViewById(R.id.spinnerProfessorAddAbsencesStudent);
        Spinner spinnerSubjects = findViewById(R.id.spinnerProfessorAddAbsencesSubject);
        Button buttonAddNote = findViewById(R.id.buttonProfessorAddANotesAdd);
        Button buttonNotes = findViewById(R.id.buttonProfessorAddNotesNotes);
        BottomNavigationView navigation = findViewById(R.id.notesBottomNavigation);

        ArrayList<Subject> subjectsArrayList ;

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
                ArrayAdapter subjectAdapter = new ArrayAdapter(ProfessorAddNotesActivity.this, android.R.layout.simple_spinner_dropdown_item, subjectsArrayList);
                spinnerSubjects.setAdapter(subjectAdapter);
            }

        } else {
            Toast.makeText(getApplicationContext(), "no me conecto al server", Toast.LENGTH_LONG).show();
        }



        spinnerStudents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String token = session.getStringData("jwtToken");
//                ArrayList<Student> studentsArrayList = new ArrayList<>();
//                studentsArrayList.clear();
                Subject selectedSubject = (Subject) spinnerSubjects.getSelectedItem();
                Student selectedStudent = (Student) spinnerStudents.getSelectedItem();





                if (isConnected()) {
                    GetNotesByStudentDniAndSubjectId getNotesByStudentDniAndSubjectId = new GetNotesByStudentDniAndSubjectId(selectedStudent.getStudent_dni(),selectedSubject.getSubject_id(),token );



                    Thread thread = new Thread(getNotesByStudentDniAndSubjectId);
                    try {
                        thread.start();
                        thread.join();
                    } catch (InterruptedException e) {
                    }
                    // Processing the answer
                    Note note = getNotesByStudentDniAndSubjectId.getResponse();


                    if (note == null) {
                        Toast.makeText(getApplicationContext(), "Recibo nullIOIOIOIO", Toast.LENGTH_LONG).show();
                    } else {


                       eva1.setText(String.valueOf(note.getEva1()));
                       eva2.setText(String.valueOf(note.getEva2()));
                       eva3.setText(String.valueOf(note.getEva3()));
                        final1.setText(String.valueOf(note.getFinal1()));
                        final2.setText(String.valueOf(note.getFinal2()));
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "no me conecto al server", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        spinnerSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String token = session.getStringData("jwtToken");
                ArrayList<Student> studentsArrayList = new ArrayList<>();
                studentsArrayList.clear();
                Subject selectedSubject = (Subject) spinnerSubjects.getSelectedItem();
                if (isConnected()) {
                    GetStudentsBySubjectId getStudentsBySubjectId = new GetStudentsBySubjectId(selectedSubject.getSubject_id(),token );
                    Thread thread = new Thread(getStudentsBySubjectId);
                    try {
                        thread.start();
                        thread.join();
                    } catch (InterruptedException e) {
                    }
                    // Processing the answer
                    studentsArrayList = getStudentsBySubjectId.getResponse();

                    if (studentsArrayList == null) {
                        Toast.makeText(getApplicationContext(), "Recibo null", Toast.LENGTH_LONG).show();
                    } else {
                        ArrayAdapter studentAdapter = new ArrayAdapter(ProfessorAddNotesActivity.this, android.R.layout.simple_spinner_dropdown_item, studentsArrayList);
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

        buttonNotes.setOnClickListener(view -> {
            Intent i = new Intent(ProfessorAddNotesActivity.this, ProfessorNotesActivity.class);
            startActivity(i);
            finish();
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.professor_nav_main:
                        Intent i = new Intent(ProfessorAddNotesActivity.this, MainProfessorActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.professor_nav_absences:
                        Intent i2 = new Intent(ProfessorAddNotesActivity.this, ProfessorAddAbsencesActivity.class);
                        startActivity(i2);
                        finish();
                        break;
                    case R.id.professor_nav_subjects:
                        Intent i3 = new Intent(ProfessorAddNotesActivity.this, ProfessorSubjectsActivity.class);
                        startActivity(i3);
                        finish();
                        break;
                    case R.id.professor_nav_students:
                        Intent i4 = new Intent(ProfessorAddNotesActivity.this, ProfessorStudentsActivity.class);
                        startActivity(i4);
                        finish();
                        break;
                    case R.id.professor_nav_mail:
                        Intent i5 = new Intent(ProfessorAddNotesActivity.this, ProfessorMailActivity.class);
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