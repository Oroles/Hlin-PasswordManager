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
import com.example.oroles.hlin.Database.DatabaseNote;
import com.example.oroles.hlin.Fragments.Adapters.EntriesAdapter;
import com.example.oroles.hlin.Fragments.Adapters.NotesAdapter;
import com.example.oroles.hlin.Fragments.Dialogs.DetailsViewFragment;
import com.example.oroles.hlin.Fragments.Dialogs.NoteDetailsViewFragment;
import com.example.oroles.hlin.R;
import com.example.oroles.hlin.Utils.Requests;
import com.example.oroles.hlin.Utils.UIUpdater;

import java.util.List;


public class NotesFragment extends Fragment {

    private ListView mListView;
    private List<DatabaseNote> mListNotes;
    private NotesAdapter mNotesAdapter;
    private SearchView searchView = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Requests.REQUEST_STORE_IN_BUFFER) {
                Toast.makeText(getActivity(), (String) msg.obj, Toast.LENGTH_LONG).show();
            }
            if (msg.what == Requests.REQUEST_ADD_DATABASE_NOTE) {
                addNote((DatabaseNote)msg.obj);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        mListView = (ListView) v.findViewById(R.id.notesListView);
        mListNotes = StoreController.getInstance(getContext()).getRepository().getNotes();
        mNotesAdapter = new NotesAdapter(getActivity(), mListNotes);
        mListView.setAdapter(mNotesAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseNote note = mListNotes.get(position);

                if (StoreController.getInstance(getActivity()).getBluetoothConnection().isConnected()) {
                    if (StoreController.getInstance(getActivity())
                            .getBluetoothConnection()
                            .write(StoreController.getInstance(getActivity())
                                    .getSenderProcessor()
                                    .createGetNoteRequest(note.getText()))) {
                        StoreController.getInstance(getActivity()).getEncryption().appendHash("AddNote");
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
                FragmentManager fm = getActivity().getSupportFragmentManager();
                NoteDetailsViewFragment dialog = NoteDetailsViewFragment.newInstance(mListNotes.get(position));
                dialog.setTargetFragment(
                        getActivity().getSupportFragmentManager().findFragmentByTag("Notes"),
                        Requests.REQUEST_DELETE_NOTE);
                dialog.show(fm, NoteDetailsViewFragment.TITLE);
                return true;
            }
        });

        setHasOptionsMenu(true);

        UIUpdater.getInstance().register(handler);
        UIUpdater.getInstance().startThread(StoreController.getInstance(getActivity()));

        return v;
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
                updateNotes();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UIUpdater.getInstance().deregister(handler);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Requests.REQUEST_DELETE_NOTE) {
            if (resultCode == DetailsViewFragment.DELETED_OK) {
                updateNotes();
            }
        }
    }

    private void updateNotes() {
        mListNotes = StoreController.getInstance(getContext()).getRepository().getNotes();
        mNotesAdapter = new NotesAdapter(getActivity(), mListNotes);
        mListView.setAdapter(mNotesAdapter);
    }

    private void addNote(DatabaseNote note) {
        mListNotes.add(note);
        mNotesAdapter.notifyDataSetChanged();
    }

    private void searchImplementation(String query) {
        mListNotes = StoreController.getInstance(getContext())
                .getRepository()
                .getNotes(query);
        mNotesAdapter = new NotesAdapter(getActivity(), mListNotes);
        mListView.setAdapter(mNotesAdapter);
    }
}
