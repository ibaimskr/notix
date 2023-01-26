package com.example.notix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notix.Network.User.SessionManager;
import com.example.notix.adapters.PartialAdapter;
import com.example.notix.beans.Note;
import com.example.notix.beans.Subject;
import com.example.notix.Network.Note.GetNotesByStudentDni;
import com.example.notix.Network.Subject.GetSubjectsByStudentDni;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StudentPartialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_partial);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        ListView listView = findViewById(R.id.listViewPartial);
        BottomNavigationView navigation = findViewById(R.id.studentBottomNavigation);

        ArrayList<Note> notesArrayList = new ArrayList<>();
        ArrayList<Subject> subjectsArrayList = new ArrayList<>();
        PartialAdapter notesAdapter = new PartialAdapter(this, R.layout.partial_layout, notesArrayList, subjectsArrayList);
        listView.setAdapter(notesAdapter);

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
                ((ListView) findViewById(R.id.listViewPartial)).setAdapter(notesAdapter);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_partial), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(StudentPartialActivity.this, StudentNotesActivity.class);
                startActivity(i);
                finish();
            }

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.student_nav_notes:
                        Intent i = new Intent(StudentPartialActivity.this, StudentNotesActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.student_nav_absences:
                        Intent i2 = new Intent(StudentPartialActivity.this, StudentAbsencesActivity.class);
                        startActivity(i2);
                        finish();
                        break;
                    case R.id.student_nav_subjects:
                        Intent i3 = new Intent(StudentPartialActivity.this, StudentSubjectsActivity.class);
                        startActivity(i3);
                        finish();
                        break;
                    case R.id.student_nav_mail:
                        Intent i4 = new Intent(StudentPartialActivity.this, StudentMailActivity.class);
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