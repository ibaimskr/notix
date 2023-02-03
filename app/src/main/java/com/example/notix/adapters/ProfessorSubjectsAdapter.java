package com.example.notix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.notix.R;
import com.example.notix.beans.Subject;

import java.util.ArrayList;

public class ProfessorSubjectsAdapter extends ArrayAdapter<Subject> {
    private final ArrayList<Subject> subjects;
    private final Context context;

    public ProfessorSubjectsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Subject> subjects) {
        super(context, resource,subjects);
        this.context = context;
        this.subjects = subjects;
    }

    @Override
    public int getCount() { return super.getCount(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.professor_subjects_layout, null);

        ((TextView) view.findViewById(R.id.viewProfessorSubjectsLayoutName)).setText( " " + subjects.get(position).getName());
        ((TextView) view.findViewById(R.id.viewProfessorSubjectsLayoutDuration)).setText("" + subjects.get(position).getDuration());

        return view;
    }
}
