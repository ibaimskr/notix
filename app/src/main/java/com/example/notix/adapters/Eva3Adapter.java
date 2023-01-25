package com.example.notix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.notix.R;
import com.example.notix.beans.Note;
import com.example.notix.beans.Subject;

import java.util.ArrayList;

public class Eva3Adapter extends ArrayAdapter<Note> {

    private final ArrayList<Note> notes;
    private final ArrayList<Subject> subjects;
    private final Context context;

    public Eva3Adapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> notes, ArrayList<Subject> subjects) {
        super(context, resource, notes);
        this.context = context;
        this.notes = notes;
        this.subjects = subjects;
    }

    @Override
    public int getCount() { return super.getCount(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.note_layout, null);

        ((TextView) view.findViewById(R.id.viewNoteLayoutSubject)).setText( " " + subjects.get(position).getName());
        ((TextView) view.findViewById(R.id.viewNoteLayoutNote)).setText(" " + notes.get(position).getEva3());

        return view;
    }
}
