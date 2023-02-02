package com.example.notix;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notix.Network.Professor.ProfessorSignup;
import com.example.notix.Network.RSA.CifradoRSA;
import com.example.notix.Network.User.UserSignup;
import com.example.notix.beans.AuthRequest;
import com.example.notix.beans.ProfessorRequest;
import com.example.notix.beans.UserRemember;
import com.example.notix.db.DataManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;

public class ProfessorSignupActivity extends AppCompatActivity {

    Bitmap bitmap;
    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_signup);

        Button buttonRegister = findViewById(R.id.buttonProfessorSignup);
        Button buttonBack = findViewById(R.id.buttonProfessorSignupBack);

        EditText textDni = findViewById(R.id.textProfessorSignupDni);
        EditText textName = findViewById(R.id.textProfessorSignupName);
        EditText textSurname = findViewById(R.id.textProfessorSignupSurname);
        Spinner spinnerNationality = findViewById(R.id.spinnerProfessorSignupNationality);
        EditText textEmail = findViewById(R.id.textProfessorSignupEmail);
        EditText textAdress = findViewById(R.id.textProfessorSignupAdress);
        EditText textPass = findViewById(R.id.textProfessorSignupPassword);
        EditText textPass2 = findViewById(R.id.textProfessorSignupPassword2);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    if ((activityResult.getResultCode() == RESULT_OK) && (activityResult.getData() != null)) {
                        // Get the image and displays it
                        // Note this method is called when the photo is taken!
                        Bundle bundle = activityResult.getData().getExtras();
                        bitmap = (Bitmap) bundle.get( "data" );
                    }
                } );

        ImageButton imageButton = findViewById(R.id.imageButtonProfessorSignupPhoto);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent takePhotoIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

                    // Is there a camera? If yes, then intent!
                    if (takePhotoIntent.resolveActivity( getPackageManager() ) != null) {
                        activityResultLauncher.launch(takePhotoIntent);
                    } else {
                        Toast.makeText( getApplicationContext(), getString(R.string.error_camera), Toast.LENGTH_LONG ).show();
                    }
                }
        });

        ArrayList<String> nacionalidades = new ArrayList<>();
        nacionalidades.add("");
        nacionalidades.add(getString(R.string.option_nationality_spain));
        nacionalidades.add(getString(R.string.option_nationality_france));
        nacionalidades.add(getString(R.string.option_nationality_italy));
        nacionalidades.add(getString(R.string.option_nationality_morocco));
        nacionalidades.add(getString(R.string.option_nationality_andorra));
        nacionalidades.add(getString(R.string.option_nationality_murcia));
        nacionalidades.add(getString(R.string.option_nationality_portugal));

        ArrayAdapter<String> nationalityAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nacionalidades);
        spinnerNationality.setAdapter(nationalityAdapter);

        buttonBack.setOnClickListener(view -> {
            Intent i = new Intent();
            setResult(1, i);
            finish();
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String nationality = spinnerNationality.getSelectedItem().toString();
                AuthRequest user = new AuthRequest();
                ProfessorRequest professor = new ProfessorRequest();

                if (textDni.getText().toString().equals("") || textName.getText().toString().equals("")
                        || textSurname.getText().toString().equals("") || nationality.equals("")
                        || textEmail.getText().toString().equals("") || textAdress.getText().toString().equals("")
                        || textPass.getText().toString().equals("") || textPass2.getText().toString().equals("")) {
                    Toast.makeText(ProfessorSignupActivity.this, R.string.error_blankField, Toast.LENGTH_SHORT).show();
                } else if (textPass.length() >= 5) {
                    if (textPass.getText().toString().equals(textPass2.getText().toString())) {

                        CifradoRSA cifradoRSA = new CifradoRSA();
                        byte[] encoded64 = Base64.getEncoder().encode(cifradoRSA.cifrarTexto(textPass.getText().toString()));
                        String passBase64= new String(encoded64);

                        if (bitmap == null) {
                            Toast.makeText(ProfessorSignupActivity.this, R.string.error_photo, Toast.LENGTH_SHORT).show();
                        } else {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] photoByte = stream.toByteArray();
                            bitmap.recycle();
                            byte[] encodedPhoto64 = Base64.getEncoder().encode(photoByte);
                            String photoBase64 = new String(encodedPhoto64);

                            user.setDni(textDni.getText().toString());
                            user.setPassword(passBase64);
                            user.setRoleId(2);
                            professor.setProfessor_dni(textDni.getText().toString());
                            professor.setName(textName.getText().toString());
                            professor.setSurname(textSurname.getText().toString());
                            professor.setNationality(nationality);
                            professor.setEmail(textEmail.getText().toString());
                            professor.setAdress(textAdress.getText().toString());
                            professor.setPhoto(photoBase64);

                            int response = signupProfessor(user, professor);
                            if (response == 500) {
                                Toast.makeText(getApplicationContext(), R.string.error_duplicated_user, Toast.LENGTH_SHORT).show();
                                setEmptyField();
                            } else if (response == 400) {
                                Toast.makeText(getApplicationContext(), R.string.error_no_create_user, Toast.LENGTH_SHORT).show();
                                setEmptyField();
                            } else if (response == 201) {
                                Toast.makeText(getApplicationContext(), R.string.toast_created, Toast.LENGTH_SHORT).show();
                                UserRemember userRemember = new UserRemember();
                                userRemember.setDni(user.getDni().toString());
                                userRemember.setPass(textPass.getText().toString());

                                dataManager = new DataManager(getApplicationContext());
                                dataManager.insert(userRemember);

                                Intent i = new Intent(ProfessorSignupActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.error_samePass, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_password_length, Toast.LENGTH_SHORT).show();
                }
            }

            private int signupProfessor(AuthRequest user, ProfessorRequest professor) {
                int professorResponse = 0;
                if (isConnected()) {
                    ProfessorSignup createProfessor = new ProfessorSignup(professor);
                    Thread thread1 = new Thread(createProfessor);
                    try {
                        thread1.start();
                        thread1.join();
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }
                    // Processing the answer
                    professorResponse = createProfessor.getResponse();

                    if (professorResponse == 201) {
                        UserSignup createUser = new UserSignup(user);
                        Thread thread2 = new Thread(createUser);
                        try {
                            thread2.start();
                            thread2.join();
                        } catch (InterruptedException e) {
                            // Nothing to do here...
                        }
                    } else if (professorResponse == 400) {
                        Toast.makeText(getApplicationContext(), R.string.error_invalid_data, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.error_no_create_user, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_communication, Toast.LENGTH_SHORT).show();
                }
                return  professorResponse;
            }

            public void setEmptyField() {
                textDni.setText("");
                textName.setText("");
                textSurname.setText("");
                textAdress.setText("");
                textEmail.setText("");
                textPass.setText("");
                textPass2.setText("");
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
    }

}