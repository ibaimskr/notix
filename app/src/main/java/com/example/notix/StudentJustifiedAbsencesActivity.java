package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notix.Network.User.SessionManager;
import com.example.notix.adapters.AbsencesAdapter;
import com.example.notix.Network.Professor.beans.Absence;
import com.example.notix.Network.Professor.beans.Subject;
import com.example.notix.Network.Absence.GetJustifiedAbsencesByDni;
import com.example.notix.Network.Subject.GetSubjectsByStudentDni;

import java.util.ArrayList;

public class StudentJustifiedAbsencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_justified_absences);

        Bundle extras = getIntent().getExtras();
        //String access = extras.getString("access");
        String dni = extras.getString("dni");

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");

        Button buttonBack = findViewById(R.id.buttonStudentJustifiedAbsencesBack);
        ListView listView = findViewById(R.id.listViewStudentJustifiedAbsences);

        ArrayList<Absence> absencesArrayList = new ArrayList<Absence>();
        ArrayList<Subject> subjectsArrayList = new ArrayList<>();
        AbsencesAdapter absencesAdapter = new AbsencesAdapter(this, R.layout.absence_layout, absencesArrayList, subjectsArrayList);
        listView.setAdapter(absencesAdapter);

        if (isConnected()) {
            GetJustifiedAbsencesByDni getAbsences = new GetJustifiedAbsencesByDni(dni, token);
            GetSubjectsByStudentDni getSubjects = new GetSubjectsByStudentDni(dni, token);
            Thread thread1 = new Thread(getAbsences);
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
            ArrayList<Absence> absences = getAbsences.getResponse();
            ArrayList<Subject> subjects = getSubjects.getResponse();

            if (absences != null) {
                absencesArrayList.addAll(absences);
                subjectsArrayList.addAll(subjects);
                ((ListView) findViewById(R.id.listViewStudentJustifiedAbsences)).setAdapter(absencesAdapter);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_data), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }

        buttonBack.setOnClickListener(view -> finish());
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