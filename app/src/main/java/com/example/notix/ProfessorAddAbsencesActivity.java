package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notix.Network.Absence.PostAbsence;
import com.example.notix.Network.Note.GetNotesByStudentDniAndSubjectId;
import com.example.notix.Network.Student.GetStudentsByProfessorDni;
import com.example.notix.Network.Student.GetStudentsBySubjectId;
import com.example.notix.Network.Subject.GetSubjectsByProfessorDni;
import com.example.notix.Network.Subject.GetSubjectsByStudentDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.Absence;
import com.example.notix.beans.Note;
import com.example.notix.beans.Student;
import com.example.notix.beans.StudentForAbsences;
import com.example.notix.beans.Subject;

import java.util.ArrayList;

public class ProfessorAddAbsencesActivity extends AppCompatActivity {

    String dni_alumno;
    String dni_profe;
    int subject_id;
    String date;
    Boolean justified = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_add_absences);

        Spinner spinnerStudents = findViewById(R.id.spinnerProfessorAddAbsencesStudent);
        Spinner spinnerSubjects = findViewById(R.id.spinnerProfessorAddAbsencesSubject);
        Spinner spinnerFoul = findViewById(R.id.spinnerProfessorAddAbsencesFoul);
        Button buttonAddAbsence = findViewById(R.id.buttonProfessorAddAbsencesAdd);
        Button buttonSeeAbsences = findViewById(R.id.buttonProfessorAddAbsencesAbsences);
        EditText editeTexDate = findViewById(R.id.editTextDateProfessorAddAbsences);

        ArrayList<String> foulArrayList = new ArrayList<>();
        foulArrayList.add("True");
        foulArrayList.add("False");
        ArrayAdapter foulAdapter = new ArrayAdapter(ProfessorAddAbsencesActivity.this, android.R.layout.simple_spinner_dropdown_item, foulArrayList);
        spinnerFoul.setAdapter(foulAdapter);

        ArrayList<Subject> subjectsArrayList;

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        dni_profe = session.getStringData("dni");


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

        spinnerStudents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Subject selectedSubject = (Subject) spinnerSubjects.getSelectedItem();
                subject_id = selectedSubject.getSubject_id();
                Student selectedStudent = (Student) spinnerStudents.getSelectedItem();
                dni_alumno = selectedStudent.getStudent_dni();
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