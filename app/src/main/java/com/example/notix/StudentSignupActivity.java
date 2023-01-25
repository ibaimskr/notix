package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notix.Network.Student.StudentSignup;
import com.example.notix.Network.User.UserSignup;
import com.example.notix.beans.AuthRequest;
import com.example.notix.beans.StudentRequest;

import java.util.ArrayList;

public class StudentSignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        Button buttonRegister = findViewById(R.id.buttonStudentSignup);
        Button buttonBack = findViewById(R.id.buttonStudentSignupBack);

        EditText textDni = findViewById(R.id.textStudentSignupDni);
        EditText textName = findViewById(R.id.textStudentSignupName);
        EditText textSurname = findViewById(R.id.textStudentSignupSurname);
        EditText textBornDate = findViewById(R.id.textStudentSignupBornDate);
        EditText textNationality = findViewById(R.id.textStudentSignupNationality);
        Spinner spinnerNationality = findViewById(R.id.spinnerStudentSignupNationality);
        EditText textEmail = findViewById(R.id.textStudentSignupEmail);
        EditText textPhone = findViewById(R.id.textStudentSingupPhone);
        EditText textPass = findViewById(R.id.textStudentSignupPassword);
        EditText textPass2 = findViewById(R.id.textStudentSignupPassword2);
        //Photo

        ArrayList<String> nacionalidades = new ArrayList<>();
        nacionalidades.add("España");
        nacionalidades.add("Francia");
        nacionalidades.add("Italia");
        nacionalidades.add("Marruecos");
        nacionalidades.add("Portugal");

        ArrayAdapter<String> nationalityAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nacionalidades);
        spinnerNationality.setAdapter(nationalityAdapter);

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //        R.array.planets_array, android.R.layout.simple_spinner_dropdown_item);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthRequest user = new AuthRequest();
                StudentRequest student = new StudentRequest();

                if (textDni.getText().toString().equals("") || textName.getText().toString().equals("")
                    || textSurname.getText().toString().equals("") || textBornDate.getText().toString().equals("")
                    || textNationality.getText().toString().equals("") || textEmail.getText().toString().equals("")
                    || textPhone.getText().toString().equals("") || textPass.getText().toString().equals("")
                    || textPass2.getText().toString().equals("")) {
                    // Validacion photo en blanco
                    Toast.makeText(StudentSignupActivity.this, R.string.error_blankField, Toast.LENGTH_SHORT).show();
                } else if (textPass.length() >= 5) {
                    if (textPass.getText().toString().equals(textPass2.getText().toString())) {

                        user.setDni(textDni.getText().toString());
                        user.setPassword(textPass.getText().toString());
                        user.setRoleId(3);
                        student.setStudent_dni(textDni.getText().toString());
                        student.setName(textName.getText().toString());
                        student.setSurname(textSurname.getText().toString());
                        student.setBornDate(textBornDate.getText().toString());
                        student.setNationality(textNationality.getText().toString());
                        student.setEmail(textEmail.getText().toString());
                        student.setPhone(textPhone.getText().toString());
                        //student.setPhoto();

                        int response = signupStudent(user, student);
                        if (response == 400) {
                            Toast.makeText(getApplicationContext(), R.string.error_duplicatedUser, Toast.LENGTH_SHORT).show();
                        } else {
                            Intent i = new Intent();
                            i.putExtra("dni", user.getDni());
                            i.putExtra("password", user.getPassword());
                            setResult(2, i);
                            finish();

                            Toast.makeText(getApplicationContext(), R.string.created, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.error_samePass, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_dnipass, Toast.LENGTH_SHORT).show();
                }
            }

            private int signupStudent(AuthRequest user, StudentRequest student) {
                int registered = 0;
                if (isConnected()) {
                    UserSignup createUser = new UserSignup(user);
                    StudentSignup createStudent = new StudentSignup(student);
                    Thread thread1 = new Thread(createUser);
                    Thread thread2 = new Thread(createStudent);
                    try {
                        thread1.start();
                        thread2.start();
                        thread1.join();
                        thread2.join();
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }
                    // Processing the answer
                    //System.out.println("RESPONSE DE USER: " + createUser.getResponse());
                    registered = createUser.getResponse();
                    int hola = createStudent.getResponse();
                    System.out.println("AAAAAAA++++++++++++++++++++++++++++++++++++++++"+hola);
                }
                return  registered;
            }

            public boolean isConnected() {
                boolean ret = false;
                try {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected())) {
                        ret = true;
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
                }
                return ret;
            }

        });

        buttonBack.setOnClickListener(view -> {
            Intent i = new Intent();
            setResult(1, i);
            finish();
        });
    }

}