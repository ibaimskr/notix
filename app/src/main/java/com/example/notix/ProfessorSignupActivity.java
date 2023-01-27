package com.example.notix;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.notix.Network.Mail.PostMail;
import com.example.notix.beans.AuthRequest;
import com.example.notix.beans.MailRequest;
import com.example.notix.beans.ProfessorRequest;
import com.example.notix.Network.Professor.ProfessorSignup;
import com.example.notix.Network.User.UserSignup;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ProfessorSignupActivity extends AppCompatActivity {

    String photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_signup);

        Button buttonRegister = findViewById(R.id.buttonProfessorSignup);
        Button buttonBack = findViewById(R.id.buttonProfessorSignupBack);

        EditText textDni = findViewById(R.id.textProfessorSignupDni);
        EditText textName = findViewById(R.id.textProfessorSignupName);
        EditText textSurname = findViewById(R.id.textProfessorSignupSurname);
        EditText textNationality = findViewById(R.id.textProfessorSignupNationality);
        Spinner spinnerNationality = findViewById(R.id.spinnerProfessorSignupNationality);
        EditText textEmail = findViewById(R.id.textProfessorSignupEmail);
        EditText textAdress = findViewById(R.id.textProfessorSignupAdress);
        EditText textPass = findViewById(R.id.textProfessorSignupPassword);
        EditText textPass2 = findViewById(R.id.textProfessorSignupPassword2);
        ImageButton imageButtonPhoto = findViewById(R.id.imageButtonProfessorSignupPhoto);
        CheckBox checkBoxSavePhoto = findViewById(R.id.checkBoxPhotoGuardadaProfessorSignUp);

        ArrayList<String> nacionalidades = new ArrayList<>();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    if ((activityResult.getResultCode() == RESULT_OK) && (activityResult.getData() != null)) {

                        // Get the image and displays it
                        // Note this method is called when the photo is taken!
                        Bundle bundle = activityResult.getData().getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        checkBoxSavePhoto.setChecked(true);
                        photo = bitmapToBase64(bitmap);
                    }
                });

        imageButtonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePhotoIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

                // Is there a camera? If yes, then intent!
                if (takePhotoIntent.resolveActivity( getPackageManager() ) != null) {
                    activityResultLauncher.launch(takePhotoIntent);
                } else {
                    Toast.makeText( getApplicationContext(), "error con la camara" , Toast.LENGTH_LONG ).show();
                }
            }
        });


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthRequest user = new AuthRequest();
                ProfessorRequest professor = new ProfessorRequest();

                if (textDni.getText().toString().equals("") || textName.getText().toString().equals("")
                    || textSurname.getText().toString().equals("") || textNationality.getText().toString().equals("")
                    || textEmail.getText().toString().equals("") || textAdress.getText().toString().equals("")
                    || textPass.getText().toString().equals("") || textPass2.getText().toString().equals("")) {
                    // Validacion photo en blanco
                    Toast.makeText(ProfessorSignupActivity.this, R.string.error_blankField, Toast.LENGTH_SHORT).show();
                } else if (textPass.length() >= 5) {
                    if (textPass.getText().toString().equals(textPass2.getText().toString())) {

                        user.setDni(textDni.getText().toString());
                        user.setPassword(textPass.getText().toString());
                        user.setRoleId(2);
                        professor.setProfessor_dni(textDni.getText().toString());
                        professor.setName(textName.getText().toString());
                        professor.setSurname(textSurname.getText().toString());
                        professor.setNationality(textNationality.getText().toString());
                        professor.setEmail(textEmail.getText().toString());
                        professor.setAdress(textAdress.getText().toString());
                        professor.setPhoto(photo);

                        int response = signupProfessor(user, professor);
                        if (response == 500) {
                            Toast.makeText(getApplicationContext(), R.string.error_duplicatedUser, Toast.LENGTH_SHORT).show();
                        } else {
                            Intent i = new Intent();
                            i.putExtra("dni", user.getDni());
                            i.putExtra("password", user.getPassword());
                            setResult(2, i);
                            finish();
                            Toast.makeText(getApplicationContext(), R.string.toast_created, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.error_samePass, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_dnipass, Toast.LENGTH_SHORT).show();
                }
            }

            private int signupProfessor(AuthRequest user, ProfessorRequest professor) {
                int registered = 0;
                if (isConnected()) {
                    UserSignup createUser = new UserSignup(user);
                    ProfessorSignup createProfessor = new ProfessorSignup(professor);
                    Thread thread1 = new Thread(createUser);
                    Thread thread2 = new Thread(createProfessor);
                    try {
                        thread1.start();
                        thread2.start();
                        thread1.join();
                        thread2.join();
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }
                    // Processing the answer
                    registered = createUser.getResponse();
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

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}