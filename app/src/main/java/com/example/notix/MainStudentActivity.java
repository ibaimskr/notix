package com.example.notix;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notix.Network.Student.GetStudentByDni;
import com.example.notix.Network.User.SessionManager;
import com.example.notix.beans.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);

        SessionManager session;
        session = new SessionManager(getApplicationContext());
        String token = session.getStringData("jwtToken");
        String dni = session.getStringData("dni");

        ImageButton imagePhoto = findViewById(R.id.imageStudentMain);
        TextView viewName = findViewById(R.id.viewStudentMainName);
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
            Student student = getStudent.getResponse();
            String studentName = (student.getName() + " " + student.getSurname());
            viewName.setText(studentName);

            String base64String = "data:image/png;base64," + student.getPhoto() + "";
            String base64Image = base64String.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imagePhoto.setImageBitmap(decodedByte);
        }

        imagePhoto.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainStudentActivity.this, (findViewById(R.id.imageStudentMain)));
            popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == (R.id.menuEditProfileItem)) {
                    Intent i = new Intent(MainStudentActivity.this, StudentEditProfileActivity.class);
                    startActivity(i);
                } else if (item.getItemId() == (R.id.menuLogoutItem)){
                    Intent i = new Intent(MainStudentActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                return true;
            });
            popupMenu.show();
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.student_nav_notes:
                        Intent i = new Intent(MainStudentActivity.this, StudentNotesActivity.class);
                        startActivity(i);
                        break;
                    case R.id.student_nav_absences:
                        Intent i2 = new Intent(MainStudentActivity.this, StudentAbsencesActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.student_nav_subjects:
                        Intent i3 = new Intent(MainStudentActivity.this, StudentSubjectsActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.student_nav_mail:
                        Intent i4 = new Intent(MainStudentActivity.this, StudentMailActivity.class);
                        startActivity(i4);
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