package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notix.adapters.PartialAdapter;
import com.example.notix.beans.Note;
import com.example.notix.beans.Subject;
import com.example.notix.network.GetNotesByDni;
import com.example.notix.network.GetSubjectsByDni;

import java.util.ArrayList;

public class StudentPartialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_partial);

        Bundle extras = getIntent().getExtras();
        String access = extras.getString("access");
        String dni = extras.getString("dni");

        Button buttonPrevious = findViewById(R.id.buttonPartialPrevious);
        Button buttonBack = findViewById(R.id.buttonPartialBack);
        ListView listView = findViewById(R.id.listViewPartial);

        ArrayList<Note> notesArrayList = new ArrayList<>();
        ArrayList<Subject> subjectsArrayList = new ArrayList<>();
        PartialAdapter notesAdapter = new PartialAdapter(this, R.layout.partial_layout, notesArrayList, subjectsArrayList);
        listView.setAdapter(notesAdapter);

        if (isConnected()) {
            GetNotesByDni getNotes = new GetNotesByDni(dni, access);
            GetSubjectsByDni getSubjects = new GetSubjectsByDni(dni, access);
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
                ((ListView) findViewById(R.id.listViewPartial)).setAdapter(notesAdapter);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_data), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }

        buttonPrevious.setOnClickListener(view -> finish());
/*
        buttonBack.setOnClickListener(view -> {
            Intent i = new Intent(StudentPartialActivity.this, StudentEva1Activity.class);
            startActivity(i);
            //finish();
        });

 */
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