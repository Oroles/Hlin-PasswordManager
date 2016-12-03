package com.example.oroles.hlin.Fragments.Adapters;

import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.oroles.hlin.Database.DatabaseEntry;
import com.example.oroles.hlin.Fragments.SettingsFragment;
import com.example.oroles.hlin.R;

import java.util.Date;
import java.util.List;

public class EntriesAdapter extends ArrayAdapter<DatabaseEntry> {

    private FragmentActivity mActivity;

    public EntriesAdapter(FragmentActivity context, List<DatabaseEntry> entries) {
        super(context, 0, entries);
        mActivity = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_entry, null);
        }

        DatabaseEntry entry = getItem(position);
        TextView websiteTextView = (TextView)convertView.findViewById(R.id.websiteTextView);
        websiteTextView.setText(entry.getWebsite());

        TextView usernameTextView = (TextView)convertView.findViewById(R.id.usernameTextView);
        usernameTextView.setText(entry.getUsername());

        TextView dateTextView = (TextView)convertView.findViewById(R.id.dateTextView);
        dateTextView.setText(android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date(entry.getTime())));

        if (PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(SettingsFragment.EXPIRED_PASSWORDS_ACTION, "")
                .equals("Mark"))
        {
            if (entry.getExpired()) {
                convertView.setBackgroundColor(0xFFFF0000);
            }
        }

        return convertView;
    }
}