package com.example.notix;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notix.Network.RSA.CifradoRSA;
import com.example.notix.Network.Student.GetStudentByDni;
import com.example.notix.Network.User.PutUser;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.AuthRequest;
import com.example.notix.beans.Student;
import com.example.notix.beans.StudentRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Base64;

public class StudentEditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_profile);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        TextView viewDni = findViewById(R.id.viewStudentEditProfileDni);
        TextView viewName = findViewById(R.id.viewStudentEditProfileName);
        TextView viewSurname = findViewById(R.id.viewStudentEditProfileSurname);
        TextView viewBornDate = findViewById(R.id.viewStudentEditProfileBornDate);
        TextView viewNationality = findViewById(R.id.viewStudentEditProfileNationality);
        TextView viewMail = findViewById(R.id.viewStudentEditProfileMail);
        TextView viewPhone = findViewById(R.id.viewStudentEditProfilePhone);
        EditText textCorreo = findViewById(R.id.textStudentEditProfileMail);
        EditText textPhone = findViewById(R.id.textStudentEditProfilePhone);
        EditText textPassword = findViewById(R.id.textStudentEditProfilePassword);
        EditText textPassword2 = findViewById(R.id.textStudentEditProfilePassword2);
        Button buttonChange = findViewById(R.id.buttonStudentEditProfileModify);
        BottomNavigationView navigation = findViewById(R.id.studentBottomNavigation);

        Student student = new Student();

        if (isConnected()) {
            GetStudentByDni getStudent = new GetStudentByDni(dni, token);
            Thread thread = new Thread(getStudent);
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            student = getStudent.getResponse();
            viewDni.setText(student.getStudent_dni());
            viewName.setText(student.getName());
            viewSurname.setText(student.getSurname());
            viewBornDate.setText(student.getBorn_date());
            viewNationality.setText(student.getNationality());
            viewMail.setText(student.getEmail());
            viewPhone.setText(student.getPhone());
        }

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int response;

                if (textCorreo.getText().toString().equals("") && textPhone.getText().toString().equals("")
                    && textPassword.getText().toString().equals("") && textPassword2.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_any_field_modify), Toast.LENGTH_SHORT).show();
                } else {
                    if ((textPassword.getText().toString() != null && textPassword2.getText().toString() != null)
                        && (textPassword.getText().toString().equals(textPassword2.getText().toString()))) {
                        AuthRequest user = new AuthRequest();
                        StudentRequest student = new StudentRequest();

                        CifradoRSA cifradoRSA = new CifradoRSA();
                        byte[] encoded64 = Base64.getEncoder().encode(cifradoRSA.cifrarTexto(textPassword.getText().toString()));
                        String passBase64= new String(encoded64);

                        user.setDni(dni);
                        user.setPassword(passBase64);
                        user.setRoleId(3);

                        PutUser putUser = new PutUser(user, token, dni);
                        Thread thread = new Thread(putUser);
                        try {
                            thread.start();
                            thread.join();
                        } catch (InterruptedException e) {
                            // Nothing to do here...
                        }
                        // Processing the answer
                        response = putUser.getResponse();
                        if (response == 201) {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_modified_password), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_modify_password), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_samePass), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.student_nav_main:
                        Intent i = new Intent(StudentEditProfileActivity.this, MainStudentActivity.class);
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