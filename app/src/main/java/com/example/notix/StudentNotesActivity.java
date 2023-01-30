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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notix.Network.Note.GetNotesByStudentDni;
import com.example.notix.Network.Subject.GetSubjectsByStudentDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.adapters.Eva1Adapter;
import com.example.notix.adapters.Eva2Adapter;
import com.example.notix.adapters.Eva3Adapter;
import com.example.notix.beans.Note;
import com.example.notix.beans.Subject;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StudentNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_notes);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        Button buttonPartial = findViewById(R.id.buttonStudentNotesPartial);
        Spinner spinnerEvaluation = findViewById(R.id.spinnerStudentNotesEvaluation);

        ListView listView = findViewById(R.id.listViewStudentNotes);
        BottomNavigationView navigation = findViewById(R.id.notesBottomNavigation);

        ArrayList<String> evaluaciones = new ArrayList<>();
        evaluaciones.add("Evaluación");
        evaluaciones.add("1ª");
        evaluaciones.add("2ª");
        evaluaciones.add("3ª");
        ArrayAdapter<String> evaluationAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, evaluaciones);
        spinnerEvaluation.setAdapter(evaluationAdapter);

        ArrayList<Note> notesArrayList = new ArrayList<>();
        ArrayList<Subject> subjectsArrayList = new ArrayList<>();
        Eva1Adapter eva1Adapter = new Eva1Adapter(this, R.layout.note_layout, notesArrayList, subjectsArrayList);
        Eva2Adapter eva2Adapter = new Eva2Adapter(this, R.layout.note_layout, notesArrayList, subjectsArrayList);
        Eva3Adapter eva3Adapter = new Eva3Adapter(this, R.layout.note_layout, notesArrayList, subjectsArrayList);

        spinnerEvaluation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isConnected()) {
                    GetNotesByStudentDni getNotes = new GetNotesByStudentDni(dni, token);
                    GetSubjectsByStudentDni getSubjects = new GetSubjectsByStudentDni(dni, token);
                    Thread thread1 = new Thread(getNotes);
                    Thread thread2 = new Thread(getSubjects);
                    try {
                        thread1.start();
                        thread2.start();
                        thread1.join();
                        thread2.join();
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }
                    // Processing the answer
                    ArrayList<Note> notes = getNotes.getResponse();
                    ArrayList<Subject> subjects = getSubjects.getResponse();

                    if (notes.size() != 0 || subjects.size() != 0) {
                        notesArrayList.addAll(notes);
                        subjectsArrayList.addAll(subjects);

                        switch (spinnerEvaluation.getSelectedItem().toString()) {
                            case "1ª":
                                listView.setAdapter(eva1Adapter);
                                ((ListView) findViewById(R.id.listViewStudentNotes)).setAdapter(eva1Adapter);
                                break;
                            case "2ª":
                                listView.setAdapter(eva2Adapter);
                                ((ListView) findViewById(R.id.listViewStudentNotes)).setAdapter(eva2Adapter);
                                break;
                            case "3ª":
                                listView.setAdapter(eva3Adapter);
                                ((ListView) findViewById(R.id.listViewStudentNotes)).setAdapter(eva3Adapter);
                                break;
                            default:
                        }
                    } else {
                        //
                    }

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        buttonPartial.setOnClickListener(view -> {
            Intent i = new Intent(StudentNotesActivity.this, StudentPartialActivity.class);
            startActivity(i);
            finish();
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.student_nav_main:
                        Intent i = new Intent(StudentNotesActivity.this, MainStudentActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.student_nav_absences:
                        Intent i2 = new Intent(StudentNotesActivity.this, StudentAbsencesActivity.class);
                        startActivity(i2);
                        finish();
                        break;
                    case R.id.student_nav_subjects:
                        Intent i3 = new Intent(StudentNotesActivity.this, StudentSubjectsActivity.class);
                        startActivity(i3);
                        finish();
                        break;
                    case R.id.student_nav_mail:
                        Intent i4 = new Intent(StudentNotesActivity.this, StudentMailActivity.class);
                        startActivity(i4);
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