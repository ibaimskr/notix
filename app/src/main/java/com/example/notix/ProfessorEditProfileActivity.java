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

import com.example.notix.Network.Professor.GetProfessorByDni;
import com.example.notix.Network.Professor.PutProfessor;
import com.example.notix.Network.RSA.CifradoRSA;
import com.example.notix.Network.User.PutUser;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.AuthRequest;
import com.example.notix.beans.Professor;
import com.example.notix.beans.ProfessorRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Base64;

public class ProfessorEditProfileActivity extends AppCompatActivity {

    Professor professor = new Professor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_edit_profile);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        TextView viewDni = findViewById(R.id.viewProfessorEditProfileDni);
        TextView viewName = findViewById(R.id.viewProfessorEditProfileName);
        TextView viewSurname = findViewById(R.id.viewProfessorEditProfileSurname);
        TextView viewNationality = findViewById(R.id.viewProfessorEditProfileNationality);
        TextView viewMail = findViewById(R.id.viewProfessorEditProfileMail);
        TextView viewAddress = findViewById(R.id.viewProfessorEditProfileAddress);
        EditText textMail = findViewById(R.id.textProfessorEditProfileMail);
        EditText textAddress = findViewById(R.id.textProfessorEditProfileAddress);
        EditText textPassword = findViewById(R.id.textProfessorEditProfilePassword);
        EditText textPassword2 = findViewById(R.id.textProfessorEditProfilePassword2);
        Button buttonChange = findViewById(R.id.buttonProfessorEditProfileModify);
        BottomNavigationView navigation = findViewById(R.id.professorBottomNavigation);

        if (isConnected()) {
            GetProfessorByDni getProfessor = new GetProfessorByDni(dni, token);
            Thread thread = new Thread(getProfessor);
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                // Nothing to do here...
            }
            // Processing the answer
            professor = getProfessor.getResponse();
            viewDni.setText(professor.getProfessor_dni());
            viewName.setText(professor.getName());
            viewSurname.setText(professor.getSurname());
            viewNationality.setText(professor.getNationality());
            viewMail.setText(professor.getEmail());
            viewAddress.setText(professor.getAdress());
        }

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                ProfessorRequest professorRequest = new ProfessorRequest();
                AuthRequest userRequest = new AuthRequest();
                int professorResponse;
                int userResponse;

                if (textMail.getText().toString().equals("") && textAddress.getText().toString().equals("")
                        && textPassword.getText().toString().equals("") && textPassword2.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_any_field_modify), Toast.LENGTH_SHORT).show();
                } else if (!textMail.getText().toString().equals("") || !textAddress.getText().toString().equals("")) {
                    professorRequest.setName(professor.getName());
                    professorRequest.setSurname(professor.getSurname());
                    professorRequest.setNationality(professor.getNationality());
                    professorRequest.setEmail(professor.getEmail());
                    professorRequest.setAdress(professor.getAdress());;
                    professorRequest.setPhoto(professor.getPhoto());

                    if (!textMail.getText().toString().equals("")) {
                        professorRequest.setEmail(textMail.getText().toString());
                    }
                    if (!textAddress.getText().toString().equals("")) {
                        professorRequest.setAdress(textAddress.getText().toString());
                    }

                    PutProfessor putProfessor = new PutProfessor(professorRequest, dni, token);
                    Thread thread2 = new Thread(putProfessor);
                    try {
                        thread2.start();
                        thread2.join();
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }
                    // Processing the answer
                    professorResponse = putProfessor.getResponse();
                    if (professorResponse == 200) {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_professor_datas_modified), Toast.LENGTH_SHORT).show();
                        setEmptyField();
                        Intent i = new Intent(ProfessorEditProfileActivity.this, ProfessorEditProfileActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_no_data_modified), Toast.LENGTH_SHORT).show();
                    }

                } else if (!textPassword.getText().toString().equals("") || textPassword2.getText().toString().equals("")) {
                    if (textPassword.getText().toString().equals(textPassword2.getText().toString())) {

                        CifradoRSA cifradoRSA = new CifradoRSA();
                        byte[] encoded64 = Base64.getEncoder().encode(cifradoRSA.cifrarTexto(textPassword.getText().toString()));
                        String passBase64 = new String(encoded64);

                        userRequest.setDni(dni);
                        userRequest.setPassword(passBase64);
                        userRequest.setRoleId(2);
                        PutUser putUser = new PutUser(userRequest, dni, token);
                        Thread thread1 = new Thread(putUser);
                        try {
                            thread1.start();
                            thread1.join();
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
                textAddress.setText("");
                textPassword.setText("");
                textPassword2.setText("");
            }
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.professor_nav_main:
                        Intent i = new Intent(ProfessorEditProfileActivity.this, MainProfessorActivity.class);
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