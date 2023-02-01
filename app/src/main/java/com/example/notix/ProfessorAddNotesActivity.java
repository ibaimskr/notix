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

import com.example.notix.Network.Note.GetNotesByStudentDniAndSubjectId;
import com.example.notix.Network.Note.PutNote;
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
    String dni_student;
    int id_subject;
    Note noteBD;
    ArrayList<Subject> subjectsArrayList = new ArrayList<>();
    ArrayList<Student> studentsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_add_notes);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        dni_profe = session.getStringData("dni");

        EditText eva1= findViewById(R.id.textProfessorAddNotesEva);
        EditText eva2= findViewById(R.id.textProfessorAddNotesEva2);
        EditText eva3= findViewById(R.id.textProfessorAddNotesEva3);
        EditText final1= findViewById(R.id.textProfessorAddNotesFinal1);
        EditText final2= findViewById(R.id.textProfessorAddNoteaFinal2);
        Spinner spinnerStudents = findViewById(R.id.spinnerProfessorAddNotesStudent);
        Spinner spinnerSubjects = findViewById(R.id.spinnerProfessorAddNotesSubject);
        Button buttonAddNote = findViewById(R.id.buttonProfessorAddANotesAdd);
        BottomNavigationView navigation = findViewById(R.id.notesBottomNavigation);



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
                Toast.makeText(getApplicationContext(), getString(R.string.error_server_null_receipt), Toast.LENGTH_LONG).show();
            } else {
                ArrayAdapter subjectAdapter = new ArrayAdapter(ProfessorAddNotesActivity.this, android.R.layout.simple_spinner_dropdown_item, subjectsArrayList);
                spinnerSubjects.setAdapter(subjectAdapter);
            }

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_LONG).show();
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
                    eva1.setText("");
                    eva1.setEnabled(false);
                    eva2.setText("");
                    eva2.setEnabled(false);
                    eva3.setText("");
                    eva3.setEnabled(false);
                    final1.setText("");
                    final1.setEnabled(false);
                    final2.setText("");
                    final2.setEnabled(false);
                    arrayListVacio.add(student);
                    ArrayAdapter studentAdapter = new ArrayAdapter(ProfessorAddNotesActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListVacio);
                    spinnerStudents.setAdapter(studentAdapter);
                }
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
                        Toast.makeText(getApplicationContext(), getString(R.string.error_server_null_receipt), Toast.LENGTH_LONG).show();
                    } else {
                        ArrayAdapter studentAdapter = new ArrayAdapter(ProfessorAddNotesActivity.this, android.R.layout.simple_spinner_dropdown_item, studentsArrayList);
                        spinnerStudents.setAdapter(studentAdapter);
                        eva1.setEnabled(true);
                        eva2.setEnabled(true);
                        eva3.setEnabled(true);
                        final1.setEnabled(true);
                        final2.setEnabled(true);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerStudents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String token = session.getStringData("jwtToken");
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
                    noteBD = getNotesByStudentDniAndSubjectId.getResponse();

                    if (noteBD == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_server_null_receipt), Toast.LENGTH_LONG).show();
                    } else {
                        eva1.setText(String.valueOf(noteBD.getEva1()));
                        eva2.setText(String.valueOf(noteBD.getEva2()));
                        eva3.setText(String.valueOf(noteBD.getEva3()));
                        final1.setText(String.valueOf(noteBD.getFinal1()));
                        final2.setText(String.valueOf(noteBD.getFinal2()));
                    }

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = session.getStringData("jwtToken");
                Subject selectedSubject = (Subject) spinnerSubjects.getSelectedItem();
                id_subject = selectedSubject.getSubject_id();
                Student selectedStudent = (Student) spinnerStudents.getSelectedItem();
                dni_student = selectedStudent.getStudent_dni();

                float eva1text = Float.parseFloat(String.valueOf(eva1.getText()));
                float eva2text = Float.parseFloat(String.valueOf(eva2.getText()));
                float eva3text = Float.parseFloat(String.valueOf(eva3.getText()));
                int final1text = Integer.parseInt(String.valueOf(final1.getText()));
                int final2text = Integer.parseInt(String.valueOf(final2.getText()));

                Note note = new Note();
                note.setStudent_dni(dni_student);
                note.setSubject_id(id_subject);
                note.setEva1(eva1text);
                note.setEva2(eva2text);
                note.setEva3(eva3text);
                note.setFinal1(final1text);
                note.setFinal2(final2text);

                if (isConnected()) {
                    PutNote putNote = new PutNote(dni_student, id_subject, note, token);
                    Thread thread2 = new Thread(putNote);
                    try {
                        thread2.start();
                        thread2.join();
                    } catch (InterruptedException e) {
                    }
                    // Processing the answer
                    int response = putNote.getResponse();

                    if (response == 401) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_not_authorized), Toast.LENGTH_LONG).show();
                    } else if (response == 409){
                        Toast.makeText(getApplicationContext(), getString(R.string.error_conflict), Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_note_updated), Toast.LENGTH_LONG).show();
                    }
                }




            }
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