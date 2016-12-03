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

/**
 * Created by oroles on 10/17/16.
 */
public class SearchEntriesAdapter extends ArrayAdapter<DatabaseEntry> {

    private FragmentActivity mActivity;
    private String mQuery;
    private String mAction;

    public SearchEntriesAdapter(FragmentActivity context, List<DatabaseEntry> entries, String query, String action) {
        super(context, 0, entries);
        mActivity = context;
        mAction = action;
        mQuery = query;

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

        return convertView;
    }
}
