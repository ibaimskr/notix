package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notix.Network.Student.GetStudentByDni;
import com.example.notix.Network.Student.PutStudent;
import com.example.notix.Network.User.GetUserByDni;
import com.example.notix.Network.User.PutUser;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.AuthRequest;
import com.example.notix.beans.AuthResponse;
import com.example.notix.beans.Student;
import com.example.notix.beans.StudentRequest;
import com.example.notix.beans.User;

import java.util.concurrent.ExecutionException;

public class StudentNewPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_new_password);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        TextView viewName = findViewById(R.id.viewNewPasswordName);
        EditText textPassword = findViewById(R.id.textStudentNewPasswordPass);
        EditText textPassword2 = findViewById(R.id.textStudentNewPasswordPass2);

        Button buttonChange = findViewById(R.id.buttonNewPasswordChange);

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
            Student student = getStudent.getResponse();
            String studentName = (student.getName() + " " + student.getSurname());
            viewName.setText(studentName);
        }

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int response;
                AuthRequest request = new AuthRequest();
                request.setDni(dni);
                request.setPassword(textPassword.toString());
                request.setRoleId(3);

                if (textPassword.getText().equals(" ") || textPassword2.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_blankField), Toast.LENGTH_SHORT).show();
                } else {
                    if (textPassword.getText().toString().equals(textPassword2.getText().toString())) {
                        PutUser putUser = new PutUser(request, token, dni);
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