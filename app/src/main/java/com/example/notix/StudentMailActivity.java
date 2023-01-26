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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notix.Network.Mail.PostMail;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.MailRequest;
import com.example.notix.beans.Professor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StudentMailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_mail);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        Spinner spinnerReceiper = findViewById(R.id.spinnerStudentMailReceiper);
        EditText textSubject = findViewById(R.id.textStudentMailSubject);
        EditText textBody = findViewById(R.id.textStudentMailBody);
        Button buttonSend = findViewById(R.id.buttonStudentMailSend);
        BottomNavigationView navigation = findViewById(R.id.mailBottomNavigation);

        ArrayList<Professor> professorsArrayList;

        if (isConnected()) {

        } else {
            Toast.makeText(getApplicationContext(), "no me conecto al server", Toast.LENGTH_LONG).show();
        }

        spinnerReceiper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isConnected()) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MailRequest mail = new MailRequest();

               if (textSubject.getText().toString().equals("") || textBody.getText().toString().equals("")) {
                   Toast.makeText(StudentMailActivity.this, R.string.error_blankField, Toast.LENGTH_SHORT).show();
               } else {
                   //mail.setRecipient();
                   mail.setSubject(textSubject.getText().toString());
                   mail.setMsgBody(textBody.getText().toString());

                   int response = sendMail(mail);
                   if (response == 400) {
                       //Toast
                   } else {
                       Toast.makeText(getApplicationContext(), R.string.sended, Toast.LENGTH_SHORT).show();
                   }
               }
            }

            private int sendMail(MailRequest mail) {
                int sended = 0;
                if (isConnected()) {
                    PostMail sendMail = new PostMail(mail, token);
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
                    case R.id.student_nav_main:
                        Intent i = new Intent(StudentMailActivity.this, MainStudentActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.student_nav_notes:
                        Intent i2 = new Intent(StudentMailActivity.this, StudentNotesActivity.class);
                        startActivity(i2);
                        finish();
                        break;
                    case R.id.student_nav_absences:
                        Intent i3 = new Intent(StudentMailActivity.this, StudentAbsencesActivity.class);
                        startActivity(i3);
                        finish();
                        break;
                    case R.id.student_nav_subjects:
                        Intent i4 = new Intent(StudentMailActivity.this, StudentSubjectsActivity.class);
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