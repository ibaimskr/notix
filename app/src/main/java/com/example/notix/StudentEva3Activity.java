package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notix.Network.User.SessionManager;
import com.example.notix.adapters.Eva3Adapter;
import com.example.notix.Network.Professor.beans.Note;
import com.example.notix.Network.Professor.beans.Subject;
import com.example.notix.Network.Note.GetNotesByStudentDni;
import com.example.notix.Network.Subject.GetSubjectsByStudentDni;

import java.util.ArrayList;

public class StudentEva3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_eva3);

        Bundle extras = getIntent().getExtras();
        //String access = extras.getString("access");
        String dni = extras.getString("dni");

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");

        Button buttonPrevious = findViewById(R.id.buttonEva3Previous);
        Button buttonNext = findViewById(R.id.buttonEva3Next);
        ListView listView = findViewById(R.id.listViewEva3);

        ArrayList<Note> notesArrayList = new ArrayList<>();
        ArrayList<Subject> subjectsArrayList = new ArrayList<>();
        Eva3Adapter notesAdapter = new Eva3Adapter(this, R.layout.note_layout, notesArrayList, subjectsArrayList);
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

            if (notes != null ) {
                notesArrayList.addAll(notes);
                subjectsArrayList.addAll(subjects);
                ((ListView) findViewById(R.id.listViewEva3)).setAdapter(notesAdapter);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_data), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }

        buttonPrevious.setOnClickListener(view -> finish());

        buttonNext.setOnClickListener(view -> {
            Intent i = new Intent(StudentEva3Activity.this, StudentPartialActivity.class);
            //i.putExtra("access", access);
            i.putExtra("dni", dni);
            startActivity(i);
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