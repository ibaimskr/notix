package com.example.notix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.notix.R;
import com.example.notix.Network.Professor.beans.Absence;
import com.example.notix.Network.Professor.beans.Subject;

import java.util.ArrayList;

public class AbsencesAdapter extends ArrayAdapter<Absence> {

    private final ArrayList<Absence> absences;
    private final ArrayList<Subject> subjects;
    private final Context context;

    public AbsencesAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Absence> absences, ArrayList<Subject> subjects) {
        super(context, resource, absences);
        this.context = context;
        this.absences = absences;
        this.subjects = subjects;
    }

    @Override
    public int getCount() { return super.getCount(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.absence_layout, null);

        ((TextView) view.findViewById(R.id.viewAbsenceLayoutSubject)).setText( " " + subjects.get(position).getName());
        ((TextView) view.findViewById(R.id.viewAbsenceLayoutAbsence)).setText("" + absences.get(position).getFoul());
        ((TextView) view.findViewById(R.id.viewAbsenceLayoutJustified)).setText("" + absences.get(position).getJustified());

        return view;
    }
}
