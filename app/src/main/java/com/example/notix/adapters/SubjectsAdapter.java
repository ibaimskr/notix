package com.example.notix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.notix.R;
import com.example.notix.beans.Professor;
import com.example.notix.beans.Subject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SubjectsAdapter extends ArrayAdapter<Subject> {

    private final ArrayList<Subject> subjects;
    private final ArrayList<Professor> tutors;
    private final Context context;

    public SubjectsAdapter(@NonNull Context context, int resource, @NotNull ArrayList<Subject> subjects, ArrayList<Professor> tutors) {
        super(context, resource, subjects);
        this.context = context;
        this.subjects = subjects;
        this.tutors = tutors;
    }

    @Override
    public int getCount() { return super.getCount(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.subject_layout, null);

        ((TextView) view.findViewById(R.id.viewSubjectLayoutSubject)).setText(subjects.get(position).getName());
        ((TextView) view.findViewById(R.id.viewSubjectLayoutDuration)).setText(subjects.get(position).getName());
        ((TextView) view.findViewById(R.id.viewSubjectLayoutTutor)).setText(tutors.get(position).getName());

        return view;
    }
}
