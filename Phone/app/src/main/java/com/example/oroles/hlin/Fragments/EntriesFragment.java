package com.example.oroles.hlin.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.oroles.hlin.Controllers.StoreController;
import com.example.oroles.hlin.Database.DatabaseEntry;
import com.example.oroles.hlin.Fragments.Adapters.EntriesAdapter;
import com.example.oroles.hlin.Fragments.Dialogs.DetailsViewFragment;
import com.example.oroles.hlin.R;
import com.example.oroles.hlin.Utils.Requests;
import com.example.oroles.hlin.Utils.UIUpdater;

import java.util.List;

public class EntriesFragment extends Fragment
{
    private ListView mListView;
    private EntriesAdapter mArrayAdapter;
    private List<DatabaseEntry> mListEntries;
    private SearchView searchView = null;

    public static boolean sHasRunExpired = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Requests.REQUEST_STORE_IN_BUFFER) {
                Toast.makeText(getActivity(), (String)msg.obj, Toast.LENGTH_LONG).show();
            }
            if (msg.what == Requests.REQUEST_ADD_DATABASE_ENTRY) {
                addEntry((DatabaseEntry) msg.obj);
            }
            if (msg.what == Requests.REQUEST_EXPIRED_ENTRIES) {
                updateExpireEntries();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_entries, container, false);

        mListView = (ListView) V.findViewById(R.id.listViews);
        mListEntries = StoreController.getInstance(getContext()).getRepository().getEntries();
        mArrayAdapter = new EntriesAdapter(getActivity(), mListEntries);
        mListView.setAdapter(mArrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DatabaseEntry entry = mListEntries.get(position);

                if (StoreController.getInstance(getActivity()).getBluetoothConnection().isConnected()) {
                    if (StoreController.getInstance(getActivity())
                            .getBluetoothConnection()
                            .write(StoreController.getInstance(getActivity())
                                    .getSenderProcessor()
                                    .createGetPasswordRequest(StoreController.getInstance(getActivity())
                                            .getRepository()
                                            .getPassword(entry.getWebsite(),
                                                    entry.getUsername())))) {
                        StoreController.getInstance(getActivity()).getEncryption().appendHash("AddGenerated");
                        //Toast.makeText(getContext(), "Press button on the device", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "First connect to bluetooth", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "First connect to bluetooth", Toast.LENGTH_LONG).show();
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                updateEntries();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                DetailsViewFragment dialog = DetailsViewFragment.newInstance(mListEntries.get(position));
                dialog.setTargetFragment(
                        getActivity().getSupportFragmentManager().findFragmentByTag("Entries"),
                        Requests.REQUEST_DELETE_ENTRY);
                dialog.show(fm, DetailsViewFragment.TITLE);
                return true;
            }
        });

        if (PreferenceManager.getDefaultSharedPreferences(getContext())
            .getBoolean(SettingsFragment.CHECK_EXPIRED_PASSWORDS, false) && !sHasRunExpired) {
            StoreController.getInstance(getActivity()).getExpireEntry().start();
            sHasRunExpired = true;
        }

        setHasOptionsMenu(true);

        UIUpdater.getInstance().register(handler);
        UIUpdater.getInstance().startThread(StoreController.getInstance(getActivity()));

        return V;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        UIUpdater.getInstance().deregister(handler);
        StoreController.getInstance(getActivity()).getExpireEntry().stop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Requests.REQUEST_DELETE_ENTRY) {
            if (resultCode == DetailsViewFragment.DELETED_OK) {
               updateEntries();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_entries, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView)searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                updateEntries();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }

                searchImplementation(query);

                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchImplementation(s);
                return false;
            }
        });
    }

    private void searchImplementation(String query) {
        String action = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(SettingsFragment.SEARCH_ACTION, "Both");

        mListEntries = StoreController.getInstance(getContext())
                .getRepository()
                .getEntries(query, action);
        mArrayAdapter = new EntriesAdapter(getActivity(), mListEntries);
        mListView.setAdapter(mArrayAdapter);
    }

    private void updateEntries() {
        mListEntries = StoreController.getInstance(getContext()).getRepository().getEntries();
        mArrayAdapter = new EntriesAdapter(getActivity(), mListEntries);
        mListView.setAdapter(mArrayAdapter);
    }

    private void addEntry(DatabaseEntry entry) {
        mListEntries.add(entry);
        mArrayAdapter.notifyDataSetChanged();
    }

    private void updateExpireEntries() {
        String action = PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getString(SettingsFragment.EXPIRED_PASSWORDS_ACTION, "");

        if (action.equals("DisplayDialog")) {
            List<DatabaseEntry> list = StoreController
                    .getInstance(getContext())
                    .getRepository()
                    .getExpiredEntries();
            if (list.size() != 0) {
                EntriesAdapter adapter = new EntriesAdapter(getActivity(), list);
                new AlertDialog.Builder(getActivity()).setAdapter(adapter, null).show();
            }
        } else {
            updateEntries();
        }
    }
}
