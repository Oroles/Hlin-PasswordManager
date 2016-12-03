package com.example.oroles.hlin.Fragments.Adapters;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.oroles.hlin.Database.DatabaseNote;
import com.example.oroles.hlin.R;

import java.util.Date;
import java.util.List;

public class NotesAdapter extends ArrayAdapter<DatabaseNote> {

    private FragmentActivity mActivity;

    public NotesAdapter(FragmentActivity context, List<DatabaseNote> notes) {
        super(context, 0, notes);
        mActivity = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_note, null);
        }

        DatabaseNote note = getItem(position);
        ((TextView) convertView.findViewById(R.id.titleNoteTextView)).setText(note.getTitle());

        TextView dateTextView = (TextView)convertView.findViewById(R.id.dateTextView);
        dateTextView.setText(android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date(note.getCreateTime())));

        return convertView;
    }
}
