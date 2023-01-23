package com.example.notix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.notix.Network.User.Login;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.AuthRequest;
import com.example.notix.beans.AuthResponse;
import com.example.notix.beans.UserRemember;
import com.example.notix.db.DataManager;

import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    EditText textDni;
    EditText textPassword;
    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button buttonIdioma = findViewById(R.id.buttonLoginLanguage);
        Button buttonPassword = findViewById(R.id.buttonLoginRemember);
        Button buttonRegister = findViewById(R.id.buttonLoginSignup);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        CheckBox checkRemember = findViewById(R.id.checkLoginRemember);
        textDni = findViewById(R.id.textLoginDni);
        textPassword = findViewById(R.id.textLoginPassword);

        dataManager = new DataManager(this);
        UserRemember userRem = new UserRemember();
        AuthRequest request = new AuthRequest();

        if (!dataManager.isEmpty()) {
            List<UserRemember> users = dataManager.selectAllUsers();
            for (UserRemember userRemember : users) {
                textDni.setText(userRemember.getDni());
                textPassword.setText(userRemember.getPass());
                checkRemember.setChecked(true);
            }
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                request.setDni(textDni.getText().toString());
                request.setPassword(textPassword.getText().toString());
                userRem.setDni(request.getDni());
                userRem.setPass(request.getPassword());

                int error;
                if(textDni.getText().toString().equals("") || textPassword.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, R.string.error_blankField, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        AuthResponse response = checkLogin(request);
                        error = response.getError();
                        String access = response.getAccessToken();
                        String dni = response.getDni();
                        if (error == 401) {
                            Toast.makeText(getApplicationContext(), R.string.error_dnipass, Toast.LENGTH_SHORT).show();
                            textDni.setText("");
                            textPassword.setText("");
                        } else if (error == 400) {
                            Toast.makeText(getApplicationContext(), R.string.error_password, Toast.LENGTH_SHORT).show();
                            textPassword.setText("");
                        } else {
                            rememberMe();
                            Context context = getApplicationContext();
                            SessionManager session = new SessionManager(context);
                            session.saveStringData ("jwtToken", response.getAccessToken());
                            Intent i = new Intent(LoginActivity.this, MainStudentActivity.class);
                            i.putExtra("access", access);
                            i.putExtra("dni", dni);
                            startActivityForResult(i, 1);
                        }
                    } catch(NullPointerException e) {
                        Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        textDni.setText("");
                        textPassword.setText("");
                    }
                }
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

            private void rememberMe() {
                UserRemember remembered = dataManager.selectUser();
                if (checkRemember.isChecked()) {
                    if (dataManager.isEmpty()) {
                        dataManager.insert(remembered);
                    } else {
                        dataManager.delete(remembered.getDni());
                    }
                } else if (!dataManager.isEmpty()) {
                    dataManager.delete(remembered.getDni());
                }
            }

        });

        // BUTTON REGISTER //
        buttonRegister.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(LoginActivity.this, (findViewById(R.id.buttonLoginSignup)));
            popupMenu.getMenuInflater().inflate(R.menu.signup_user_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == (R.id.menuProfesorItem)) {
                    Intent i = new Intent(LoginActivity.this, ProfessorSignupActivity.class);
                    startActivityForResult(i, 2);
                } else {
                    Intent i = new Intent(LoginActivity.this, StudentSignupActivity.class);
                    startActivityForResult(i, 2);
                }
                return true;
            });
            popupMenu.show();
        });

        // BUTTON LANGUAGE //
        buttonIdioma.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(LoginActivity.this, (findViewById(R.id.buttonLoginLanguage)));
            popupMenu.getMenuInflater().inflate(R.menu.language_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                String language;
                if (item.getItemId() == (R.id.menuSpanishItem)) {
                    language = "es";
                    setLang(language);
                    this.recreate();
                } else if (item.getItemId() == (R.id.menuEnglishItem)) {
                    language = "in";
                    setLang(language);
                    this.recreate();
                } else if (item.getItemId() == (R.id.menuBasqueItem)) {
                    language = "eu";
                    setLang(language);
                    this.recreate();
                }
                return true;
            });
            popupMenu.show();
        });

    }

    private void setLang(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            String dni = data.getStringExtra("dni");
            String pass = data.getStringExtra("password");

            textDni.setText(dni);
            textPassword.setText(pass);
        }
        if (requestCode == 1) {
            if (!dataManager.isEmpty()) {
                List<UserRemember> users = dataManager.selectAllUsers();
                for (UserRemember userRemember : users) {
                    textDni.setText(userRemember.getDni());
                    textPassword.setText(userRemember.getPass());
                }
            } else {
                    textDni.setText("");
                    textPassword.setText("");
                }
            }
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