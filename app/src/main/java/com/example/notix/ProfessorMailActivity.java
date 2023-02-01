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

import com.example.notix.Network.Mail.PostMail;
import com.example.notix.Network.Student.GetStudentsByProfessorDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.MailRequest;
import com.example.notix.beans.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ProfessorMailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_mail);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        Spinner spinnerReceiper = findViewById(R.id.spinnerProfessorMailReceiper);
        EditText textSubject = findViewById(R.id.textProfesessorMailSubject);
        EditText textBody = findViewById(R.id.textProfessorMailBody);
        Button buttonSend = findViewById(R.id.buttonProfessorMailSend);
        BottomNavigationView navigation = findViewById(R.id.mailBottomNavigation);

        ArrayList<Student> studentsArrayList;

        if (isConnected()) {
            GetStudentsByProfessorDni getStudents = new GetStudentsByProfessorDni(dni, token);
            Thread thread = new Thread(getStudents);
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                // Nothing to do here
            }
            // Processing the answer
            studentsArrayList = getStudents.getResponse();

            if (studentsArrayList != null) {
                ArrayAdapter studentsAdapter = new ArrayAdapter(ProfessorMailActivity.this, android.R.layout.simple_spinner_dropdown_item, studentsArrayList);
                spinnerReceiper.setAdapter(studentsAdapter);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_server_null_receipt), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_LONG).show();
        }

        spinnerReceiper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MailRequest mail = new MailRequest();
                Student selectedSelected = (Student) spinnerReceiper.getSelectedItem();
                String studentEmail = selectedSelected.getEmail();

                if (spinnerReceiper == null || textSubject.getText().toString().equals("")
                        || textBody.getText().toString().equals("")) {
                    Toast.makeText(ProfessorMailActivity.this, R.string.error_blankField, Toast.LENGTH_SHORT).show();
                } else {
                    mail.setReceiper(studentEmail);
                    mail.setSubject(textSubject.getText().toString());
                    mail.setMsgBody(textBody.getText().toString());

                    int response = sendMail(mail);
                    if (response == 400) {
                        //Toast
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.toast_sended, Toast.LENGTH_SHORT).show();
                        textSubject.setText("");
                        textBody.setText("");
                    }
                }
            }

            private int sendMail(MailRequest mail) {
                int sended = 0;
                if (isConnected()) {
                    PostMail sendMail = new PostMail(mail);
                    Thread thread = new Thread(sendMail);
                    try {
                        thread.start();
                        thread.join();
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }
                    // Processing the answer
                    sended = sendMail.getResponse();
                }
                return sended;
            }
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.professor_nav_main:
                        Intent i = new Intent(ProfessorMailActivity.this, MainProfessorActivity.class);
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