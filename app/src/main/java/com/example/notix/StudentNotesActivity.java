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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        evaluaciones.add(getString(R.string.text_evaluation));
        evaluaciones.add(getString(R.string.text_eva1));
        evaluaciones.add(getString(R.string.text_eva2));
        evaluaciones.add(getString(R.string.text_eva3));
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
                        notesArrayList.clear();
                        subjectsArrayList.clear();
                        notesArrayList.addAll(notes);
                        subjectsArrayList.addAll(subjects);

                        switch (spinnerEvaluation.getSelectedItem().toString()) {
                            case "Eva1":
                                listView.setAdapter(eva1Adapter);
                                ((ListView) findViewById(R.id.listViewStudentNotes)).setAdapter(eva1Adapter);
                                break;
                            case "Eva2":
                                listView.setAdapter(eva2Adapter);
                                ((ListView) findViewById(R.id.listViewStudentNotes)).setAdapter(eva2Adapter);
                                break;
                            case "Eva3":
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