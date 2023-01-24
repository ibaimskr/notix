package com.example.notix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.notix.R;
import com.example.notix.Network.Professor.beans.Note;
import com.example.notix.Network.Professor.beans.Subject;

import java.util.ArrayList;

public class PartialAdapter extends ArrayAdapter<Note> {

    private final ArrayList<Note> notes;
    private final ArrayList<Subject> subjects;
    private final Context context;

    public PartialAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> notes, ArrayList<Subject> subjects) {
        super(context, resource, notes);
        this.context = context;
        this.notes = notes;
        this.subjects = subjects;
    }

    @Override
    public int getCount() { return super.getCount(); }

    @Override
    public View getView(int position, android.view.View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.partial_layout, null);

        ((TextView) view.findViewById(R.id.viewPartialLayoutSubject)).setText( " " + subjects.get(position).getName());
        ((TextView) view.findViewById(R.id.viewPartialLayoutEva1)).setText(" " + notes.get(position).getEva1());
        ((TextView) view.findViewById(R.id.viewPartialLayoutEva2)).setText(" " + notes.get(position).getEva2());
        ((TextView) view.findViewById(R.id.viewPartialLayoutEva3)).setText(" " + notes.get(position).getEva3());
        ((TextView) view.findViewById(R.id.viewPartialLayoutFinal1)).setText(" " + notes.get(position).getFinal1());
        ((TextView) view.findViewById(R.id.viewPartialLayoutFinal2)).setText(" " + notes.get(position).getFinal2());

        return view;
    }
}
