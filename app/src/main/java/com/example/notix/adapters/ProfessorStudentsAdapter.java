package com.example.notix.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        String base64String = "data:image/png;base64," + students.get(position).getPhoto() + "";
        String base64Image = base64String.split(",")[1];

        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ((ImageView) view.findViewById(R.id.imageViewStudentPhotoAdapter)).setImageBitmap(decodedByte);
        ((TextView) view.findViewById(R.id.viewStudentLayoutName)).setText("" + students.get(position).getName());
        ((TextView) view.findViewById(R.id.viewStudentLayoutSurname)).setText("" + students.get(position).getSurname());
        ((TextView) view.findViewById(R.id.viewStudentLayoutDni)).setText("" + students.get(position).getStudent_dni());
        ((TextView) view.findViewById(R.id.viewStudentLayoutPhone)).setText(students.get(position).getPhone());
        ((TextView) view.findViewById(R.id.viewStudentLayoutMail)).setText(students.get(position).getEmail());

        return view;
    }
}