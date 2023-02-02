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

import com.example.notix.Network.RSA.CifradoRSA;
import com.example.notix.Network.Student.StudentSignup;
import com.example.notix.Network.User.UserSignup;
import com.example.notix.beans.AuthRequest;
import com.example.notix.beans.StudentRequest;
import com.example.notix.beans.UserRemember;
import com.example.notix.db.DataManager;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;

public class StudentSignupActivity extends AppCompatActivity {

    Bitmap bitmap;
    DataManager dataManager;

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
        Spinner spinnerNationality = findViewById(R.id.spinnerStudentSignupNationality);
        EditText textEmail = findViewById(R.id.textStudentSignupEmail);
        EditText textPhone = findViewById(R.id.textStudentSingupPhone);
        EditText textPass = findViewById(R.id.textStudentSignupPassword);
        EditText textPass2 = findViewById(R.id.textStudentSignupPassword2);

        ArrayList<String> nacionalidades = new ArrayList<>();
        nacionalidades.add("");
        nacionalidades.add("España");
        nacionalidades.add("Francia");
        nacionalidades.add("Italia");
        nacionalidades.add("Marruecos");
        nacionalidades.add("Portugal");

        ArrayAdapter<String> nationalityAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nacionalidades);
        spinnerNationality.setAdapter(nationalityAdapter);
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

        ImageButton imageButton = findViewById(R.id.imageButtonPhotoStudentSignUp);
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
                StudentRequest student = new StudentRequest();

                if (textDni.getText().toString().equals("") || textName.getText().toString().equals("")
                        || textSurname.getText().toString().equals("") || textBornDate.getText().toString().equals("")
                        || nationality.equals("") || textEmail.getText().toString().equals("")
                        || textPhone.getText().toString().equals("") || textPass.getText().toString().equals("")
                        || textPass2.getText().toString().equals("")) {
                    Toast.makeText(StudentSignupActivity.this, R.string.error_blankField, Toast.LENGTH_SHORT).show();
                } else if (textPass.length() >= 5) {
                    if (textPass.getText().toString().equals(textPass2.getText().toString())) {

                        CifradoRSA cifradoRSA = new CifradoRSA();
                        byte[] encoded64 = Base64.getEncoder().encode(cifradoRSA.cifrarTexto(textPass.getText().toString()));
                        String passBase64= new String(encoded64);

                        if (bitmap == null) {
                            Toast.makeText(StudentSignupActivity.this, R.string.error_photo, Toast.LENGTH_SHORT).show();
                        } else {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] photoByte = stream.toByteArray();
                            bitmap.recycle();
                            byte[] encodedPhoto64 = Base64.getEncoder().encode(photoByte);
                            String photoBase64 = new String(encodedPhoto64);

                            user.setDni(textDni.getText().toString());
                            user.setPassword(passBase64);
                            user.setRoleId(3);
                            student.setStudent_dni(textDni.getText().toString());
                            student.setName(textName.getText().toString());
                            student.setSurname(textSurname.getText().toString());
                            student.setBornDate(textBornDate.getText().toString());
                            student.setNationality(nationality);
                            student.setEmail(textEmail.getText().toString());
                            student.setPhone(textPhone.getText().toString());
                            student.setPhoto(photoBase64);

                            int response = signupStudent(user, student);
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

                                Intent i = new Intent(StudentSignupActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.error_samePass, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_dniPass, Toast.LENGTH_SHORT).show();
                }
            }

            private int signupStudent(AuthRequest user, StudentRequest student) {
                int studentResponse = 0;
                if (isConnected()) {
                    StudentSignup createStudent = new StudentSignup(student);
                    Thread thread1 = new Thread(createStudent);
                    try {
                        thread1.start();
                        thread1.join();
                    } catch (InterruptedException e) {
                        // Nothing to do here...
                    }
                    // Processing the answer
                    studentResponse = createStudent.getResponse();

                    if (studentResponse == 200) {
                        UserSignup createUser = new UserSignup(user);
                        Thread thread2 = new Thread(createUser);
                        try {
                            thread2.start();
                            thread2.join();
                        } catch (InterruptedException e) {
                            // Nothing to do here...
                        }
                    } else if (studentResponse == 400) {
                            Toast.makeText(getApplicationContext(), R.string.error_invalid_data, Toast.LENGTH_SHORT).show();
                    } else {
                            Toast.makeText(getApplicationContext(), R.string.error_no_create_user, Toast.LENGTH_SHORT).show();
                        }

                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_communication, Toast.LENGTH_SHORT).show();
                }
                return  studentResponse;
            }

            public void setEmptyField() {
                textDni.setText("");
                textName.setText("");
                textSurname.setText("");
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