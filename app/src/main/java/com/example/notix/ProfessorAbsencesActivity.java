package com.example.notix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfessorAbsencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_absences);

        BottomNavigationView navigation = findViewById(R.id.professorBottomNavigation);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.professor_nav_notes:
                        Intent i = new Intent(ProfessorAbsencesActivity.this, ProfessorAddNotesActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.professor_nav_absences:
                        Intent i2 = new Intent(ProfessorAbsencesActivity.this, ProfessorAddAbsencesActivity.class);
                        startActivity(i2);
                        finish();
                        break;
                    case R.id.professor_nav_subjects:
                        Intent i3 = new Intent(ProfessorAbsencesActivity.this, ProfessorSubjectsActivity.class);
                        startActivity(i3);
                        finish();
                        break;
                    case R.id.professor_nav_students:
                        Intent i4 = new Intent(ProfessorAbsencesActivity.this, ProfessorStudentsActivity.class);
                        startActivity(i4);
                        finish();
                        break;
                    case R.id.professor_nav_mail:
                        Intent i5 = new Intent(ProfessorAbsencesActivity.this, ProfessorMailActivity.class);
                        startActivity(i5);
                        finish();
                        break;
                    default:
                }
                return true;
            }
        });
    }
}