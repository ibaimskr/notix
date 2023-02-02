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
import com.example.notix.Network.Student.PutStudent;
import com.example.notix.Network.User.PutUser;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.AuthRequest;
import com.example.notix.beans.Student;
import com.example.notix.beans.StudentRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Base64;

public class StudentEditProfileActivity extends AppCompatActivity {

    Student student = new Student();

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
        EditText textMail = findViewById(R.id.textStudentEditProfileMail);
        EditText textPhone = findViewById(R.id.textStudentEditProfilePhone);
        EditText textPassword = findViewById(R.id.textStudentEditProfilePassword);
        EditText textPassword2 = findViewById(R.id.textStudentEditProfilePassword2);
        Button buttonChange = findViewById(R.id.buttonStudentEditProfileModify);
        BottomNavigationView navigation = findViewById(R.id.studentBottomNavigation);

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
                StudentRequest studentRequest = new StudentRequest();
                AuthRequest userRequest = new AuthRequest();
                int studentResponse;
                int userResponse;

                if (textMail.getText().toString().equals("") && textPhone.getText().toString().equals("")
                        && textPassword.getText().toString().equals("") && textPassword2.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_any_field_modify), Toast.LENGTH_SHORT).show();
                } else if (!textMail.getText().toString().equals("") || !textPhone.getText().toString().equals("")) {
                    studentRequest.setName(student.getName());
                    studentRequest.setSurname(student.getSurname());
                    String bornDate = student.getBorn_date().substring(0,10);
                    studentRequest.setBornDate(bornDate);
                    studentRequest.setNationality(student.getNationality());
                    studentRequest.setEmail(student.getEmail());
                    studentRequest.setPhone(student.getPhone());
                    studentRequest.setPhoto(student.getPhoto());

                    if (!textMail.getText().toString().equals("")) {
                        studentRequest.setEmail(textMail.getText().toString());
                    }
                    if (!textPhone.getText().toString().equals("")) {
                        studentRequest.setPhone(textPhone.getText().toString());
                    }

                    if (textPhone.getText().toString().length() == 9) {
                        PutStudent putStudent = new PutStudent(studentRequest, dni, token);
                        Thread thread2 = new Thread(putStudent);
                        try {
                            thread2.start();
                            thread2.join();
                        } catch (InterruptedException e) {
                            // Nothing to do here...
                        }
                        // Processing the answer
                        studentResponse = putStudent.getResponse();
                        if (studentResponse == 200) {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_student_datas_modified), Toast.LENGTH_SHORT).show();
                            setEmptyField();
                            Intent i = new Intent(StudentEditProfileActivity.this, StudentEditProfileActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_no_data_modified), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_phone_length), Toast.LENGTH_SHORT).show();
                    }

                } else if (!textPassword.getText().toString().equals("") || textPassword2.getText().toString().equals("")) {
                    if (textPassword.getText().toString().equals(textPassword2.getText().toString())) {

                        CifradoRSA cifradoRSA = new CifradoRSA();
                        byte[] encoded64 = Base64.getEncoder().encode(cifradoRSA.cifrarTexto(textPassword.getText().toString()));
                        String passBase64= new String(encoded64);

                        userRequest.setDni(dni);
                        userRequest.setPassword(passBase64);
                        userRequest.setRoleId(3);
                        PutUser putUser = new PutUser(userRequest, dni, token);
                        Thread thread = new Thread(putUser);
                        try {
                            thread.start();
                            thread.join();
                        } catch (InterruptedException e) {
                            // Nothing to do here...
                        }
                        // Processing the answer
                        userResponse = putUser.getResponse();
                        if (userResponse == 200) {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_modified_password), Toast.LENGTH_SHORT).show();
                            setEmptyField();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_modify_password), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_samePass), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        public void setEmptyField() {
            textMail.setText("");
            textPhone.setText("");
            textPassword.setText("");
            textPassword2.setText("");
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