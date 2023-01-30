package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notix.Network.Mail.PostMail;
import com.example.notix.Network.Student.GetStudentByDni;
import com.example.notix.Network.User.GetUserByDni;
import com.example.notix.Network.User.Login;
import com.example.notix.Network.User.PutUser;
import com.example.notix.beans.AuthRequest;
import com.example.notix.beans.AuthResponse;
import com.example.notix.beans.MailRequest;
import com.example.notix.beans.Student;
import com.example.notix.beans.User;

public class ContrasenaOlvidadaActivity extends AppCompatActivity {

    String email = " ";
    String token = " ";
    int code = 0;
    AuthResponse user = new AuthResponse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasena_olvidada);

        EditText editTextDni = findViewById(R.id.editTextDniContrasenaOlvidadaActivity);
        EditText editTextCodigo = findViewById(R.id.editTextCodigoContrasenaOlvidadaActivity);
        EditText editTextPass = findViewById(R.id.editTextTextPasswordContrasenaOlvidadaActivity);
        EditText editTextPass2 = findViewById(R.id.editTextTextPassword2ContrasenaOlvidadaActivity);
        Button buttonCodigo = findViewById(R.id.buttonEnviarCodigoContrasenaOlvidadaActivity);
        Button buttonModificar = findViewById(R.id.buttonModificarCOntrasenaOlvidadaActivity);

        buttonCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextDni.getText().equals(" ")) {
                    Toast.makeText(getApplicationContext(), R.string.error_blankField, Toast.LENGTH_SHORT).show();
                } else {
                    AuthRequest authRequest = new AuthRequest();
                    authRequest.setDni("admin");
                    authRequest.setPassword("admin");
                    AuthResponse response = checkLogin(authRequest);
                    token = response.getAccessToken();
                    if (isConnected()) {
                        GetStudentByDni getStudentByDni = new GetStudentByDni(editTextDni.getText().toString(), token);
                        Thread thread = new Thread(getStudentByDni);
                        try {
                            thread.start();
                            thread.join();
                        } catch (InterruptedException e) {
                        }
                        // Processing the answer
                        Student student = getStudentByDni.getResponse();

                        if (student == null) {
                            Toast.makeText(getApplicationContext(), "Este dni no esta registrado", Toast.LENGTH_LONG).show();
                        } else {
                            email = student.getEmail().toString();
                            code = getFiveDigitsNumber();
                            MailRequest mail = new MailRequest(email, "Su codigo de verificacion es: " + code, "Codigo de verificacion");
                            int responseMail = sendMail(mail);
                            if (responseMail == 400) {
                                //Toast
                            } else if (responseMail == 200) {
                                Toast.makeText(getApplicationContext(), R.string.toast_sended, Toast.LENGTH_SHORT).show();
                                buttonModificar.setEnabled(true);
                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "no me conecto al server", Toast.LENGTH_LONG).show();
                    }
                }

            }

            public int getFiveDigitsNumber() {
                double fiveDigits = 10000 + Math.random() * 90000;
                return (int) fiveDigits;
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



            private AuthResponse checkLogin(AuthRequest loginRequest) {
                AuthResponse response = new AuthResponse();
                if (isConnected()) {
                    Login getUser = new Login(loginRequest);
                    Thread thread = new Thread(getUser);
                    try {
                        thread.start();
                        thread.join();
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }
                    // Processing the answer
                    response = getUser.getResponse(loginRequest);
                }
                return response;
            }
        });

        buttonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    GetUserByDni getUserByDni = new GetUserByDni(editTextDni.getText().toString(),token);
                    Thread thread = new Thread(getUserByDni);
                    try {
                        thread.start();
                        thread.join();
                    } catch (InterruptedException e) {
                    }
                    // Processing the answer
                    user = getUserByDni.getResponse();

                    if (user == null) {
                        Toast.makeText(getApplicationContext(), "recibo null colega", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "no me conecto al server", Toast.LENGTH_LONG).show();
                }

                if (Integer.parseInt(editTextCodigo.getText().toString()) == code) {
                    if (editTextPass.getText().equals(" ") || editTextPass2.getText().equals(" ")){
                        Toast.makeText(getApplicationContext(), getString(R.string.error_blankField), Toast.LENGTH_SHORT).show();
                    } else {
                        if (editTextPass.getText().toString().equals(editTextPass2.getText().toString())) {
                            if (isConnected()) {
                                AuthRequest userRequest = new AuthRequest();
                                userRequest.setDni(user.getDni());
                                userRequest.setRoleId(user.getRoleId());
                                userRequest.setPassword(editTextPass.getText().toString());
                                PutUser putUser = new PutUser(userRequest, user.getDni(), token);
                                Thread thread = new Thread(putUser);
                                try {
                                    thread.start();
                                    thread.join();
                                } catch (InterruptedException e) {
                                }
                                // Processing the answer
                                int response = putUser.getResponse();

                                if (response == 204) {
                                    Toast.makeText(getApplicationContext(), "No existe el usuario", Toast.LENGTH_LONG).show();
                                } else if (response == 200){
                                    Toast.makeText(getApplicationContext(), "Contraseña modificada", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "no me conecto al server", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Codigo de verificacion incorrecto", Toast.LENGTH_SHORT).show();

                }
            }
        });
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
}