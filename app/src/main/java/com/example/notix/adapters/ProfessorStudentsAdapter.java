package com.example.notix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.notix.R;
import com.example.notix.beans.Student;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProfessorStudentsAdapter extends ArrayAdapter<Student> {

    private final ArrayList<Student> students;

    private final Context context;

    public ProfessorStudentsAdapter(@NonNull Context context, int resource, @NotNull ArrayList<Student> students) {
        super(context, resource, students);
        this.context = context;
        this.students = students;
    }

    @Override
    public int getCount() { return super.getCount(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.professor_student_layout, null);

//        ((ImageView) view.findViewById(R.id.imageViewStudentPhotoAdapter)).;
        ((TextView) view.findViewById(R.id.textViewStudentNameAdapter)).setText("" + students.get(position).getName() + " " + students.get(position).getSurname());
        ((TextView) view.findViewById(R.id.textViewStudentDniAdapter)).setText("" + students.get(position).getStudent_dni());
        ((TextView) view.findViewById(R.id.textViewStudentNationalityAdapter)).setText("" + students.get(position).getNationality());
        ((TextView) view.findViewById(R.id.textViewStudentBornDateAdapter)).setText(students.get(position).getBorn_date());
        ((TextView) view.findViewById(R.id.textViewStudentMailAdapter)).setText(students.get(position).getEmail());
        ((TextView) view.findViewById(R.id.textViewStudentPhoneAdapter)).setText(students.get(position).getPhone());

        return view;
    }
}
