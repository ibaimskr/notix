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
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notix.Network.Absence.DeleteAbsence;
import com.example.notix.Network.Absence.GetAbsencesByStudentDniAndSubjectId;
import com.example.notix.Network.Absence.PutAbsence;
import com.example.notix.Network.Student.GetStudentsBySubjectIdAndProfessorDni;
import com.example.notix.Network.Subject.GetSubjectsByProfessorDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.adapters.AbsencesAdapter;
import com.example.notix.beans.Absence;
import com.example.notix.beans.Student;
import com.example.notix.beans.Subject;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ProfessorAbsencesActivity extends AppCompatActivity {
    String dni_alumno;
    String dni_profe;
    int subject_id;
    Subject selectedSubject = new Subject();
    Student selectedStudent = new Student();
    ArrayList<Subject> subjectsArrayList = new ArrayList<>();
    ArrayList<Student> studentsArrayList = new ArrayList<>();
    ArrayList<Absence> absencesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_absences);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        dni_profe = session.getStringData("dni");

        BottomNavigationView navigation = findViewById(R.id.professorBottomNavigation);
        Spinner spinnerStudents = findViewById(R.id.spinnerStudentsProfessorAbsences);
        Spinner spinnerSubjects = findViewById(R.id.spinnerSubjectsProfessorAbsences);
        ListView listViewAbsences = findViewById(R.id.listViewProfessorAbsences);



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
                ArrayAdapter subjectAdapter = new ArrayAdapter(ProfessorAbsencesActivity.this, android.R.layout.simple_spinner_dropdown_item, subjectsArrayList);
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
                    arrayListVacio.add(student);
                    ArrayAdapter studentAdapter = new ArrayAdapter(ProfessorAbsencesActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayListVacio);
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
                        Toast.makeText(getApplicationContext(), getString(R.string.error_server_null_receipt), Toast.LENGTH_LONG).show();
                    } else {
                        ArrayAdapter studentAdapter = new ArrayAdapter(ProfessorAbsencesActivity.this, android.R.layout.simple_spinner_dropdown_item, studentsArrayList);
                        spinnerStudents.setAdapter(studentAdapter);
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
                selectedStudent = (Student) spinnerStudents.getSelectedItem();
                String token = session.getStringData("jwtToken");
                if (isConnected()) {
                    GetAbsencesByStudentDniAndSubjectId getAbsencesByStudentDniAndSubjectId = new GetAbsencesByStudentDniAndSubjectId(selectedStudent.getStudent_dni(), selectedSubject.getSubject_id(), token );
                    Thread thread = new Thread(getAbsencesByStudentDniAndSubjectId);
                    try {
                        thread.start();
                        thread.join();
                    } catch (InterruptedException e) {
                    }
                    // Processing the answer
                    absencesArrayList = getAbsencesByStudentDniAndSubjectId.getResponse();

                    if (absencesArrayList == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_server_null_receipt), Toast.LENGTH_LONG).show();
                    } else {
                        AbsencesAdapter absencesAdapter = new AbsencesAdapter(ProfessorAbsencesActivity.this, R.layout.absence_layout, absencesArrayList, subjectsArrayList);
                        listViewAbsences.setAdapter(absencesAdapter);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listViewAbsences.setOnItemClickListener((adapterView, view, position, l) -> {

            Absence clickedAbsence = absencesArrayList.get(position);
            String dni_alumno = clickedAbsence.getStudent_dni();
            int subject_id = clickedAbsence.getSubject_id();
            String date = clickedAbsence.getFoul();


            PopupMenu popupMenu = new PopupMenu(ProfessorAbsencesActivity.this, (findViewById(R.id.listViewProfessorAbsences)));
            popupMenu.getMenuInflater().inflate(R.menu.menu_delete_absence, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == (R.id.delete_absence)) {
                    if (isConnected()) {
                        DeleteAbsence deleteAbsence = new DeleteAbsence(dni_alumno, subject_id, date, token );
                        Thread thread = new Thread(deleteAbsence);
                        try {
                            thread.start();
                            thread.join();
                        } catch (InterruptedException e) {
                        }
                        // Processing the answer
                        int response = deleteAbsence.getResponse();

                        if (response == 204) {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_server_null_receipt), Toast.LENGTH_LONG).show();
                        } else if (response == 200) {
                            Toast.makeText(ProfessorAbsencesActivity.this, getString(R.string.toast_absence_deleted), Toast.LENGTH_SHORT).show();
                            absencesArrayList.remove(clickedAbsence);
                            AbsencesAdapter absencesAdapter = new AbsencesAdapter(ProfessorAbsencesActivity.this, R.layout.absence_layout, absencesArrayList, subjectsArrayList);
                            listViewAbsences.setAdapter(absencesAdapter);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_LONG).show();
                    }


                } else if (item.getItemId() == (R.id.justificate_absence)) {
                    Boolean justified = clickedAbsence.getJustified();
                    if(justified){
                        Toast.makeText(getApplicationContext(), getString(R.string.error_absence_is_justified), Toast.LENGTH_LONG).show();
                    } else {
                        clickedAbsence.setJustified(true);
                        if (isConnected()) {
                            PutAbsence putAbsence = new PutAbsence(dni_alumno, clickedAbsence, subject_id, date, token );
                            Thread thread = new Thread(putAbsence);
                            try {
                                thread.start();
                                thread.join();
                            } catch (InterruptedException e) {
                            }
                            // Processing the answer
                            int response = putAbsence.getResponse();

                            if (response == 204) {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_server_null_receipt), Toast.LENGTH_LONG).show();
                            } else if (response == 200) {
                                Toast.makeText(ProfessorAbsencesActivity.this, getString(R.string.toast_absence_updated), Toast.LENGTH_SHORT).show();
                                AbsencesAdapter absencesAdapter = new AbsencesAdapter(ProfessorAbsencesActivity.this, R.layout.absence_layout, absencesArrayList, subjectsArrayList);
                                listViewAbsences.setAdapter(absencesAdapter);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_LONG).show();
                        }

                    }

                }
                return true;
            });
            popupMenu.show();

        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.professor_nav_main:
                        Intent i = new Intent(ProfessorAbsencesActivity.this, MainProfessorActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.professor_nav_absences:
                        Intent i2 = new Intent(ProfessorAbsencesActivity.this, ProfessorAddAbsencesActivity.class);
                        startActivity(i2);
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
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
}