package com.example.oroles.hlin.Controllers;

import android.os.Environment;

import com.example.oroles.hlin.Database.DatabaseEntry;
import com.example.oroles.hlin.Database.DatabaseManager;
import com.example.oroles.hlin.Database.DatabaseNote;
import com.example.oroles.hlin.Database.DatabasePasswords;
import com.example.oroles.hlin.InterfacesControllers.IRepository;
import com.example.oroles.hlin.InterfacesControllers.IStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class RepositoryController implements IRepository {

    private IStore mStore;
    private DatabaseManager mDatabaseManager;
    private DatabasePasswords mDatabasePasswords;

    public RepositoryController(IStore store) {
        mStore = store;
        mDatabaseManager = new DatabaseManager(mStore.getContext());
        mDatabasePasswords = new DatabasePasswords(mStore.getContext());
    }

    @Override
    public boolean addEntry(DatabaseEntry entry) {
        boolean result = mDatabaseManager.addEntry(entry);
        if (result) {
            updateDuplicatePasswords();
        }
        return result;
    }

    @Override
    public boolean addNote(DatabaseNote note) {
        return mDatabaseManager.addNote(note);
    }

    @Override
    public boolean deleteEntry(String website, String username) {
        boolean result = mDatabaseManager.deleteEntry(website, username);
        if (result) {
            updateDuplicatePasswords();
        }
        return false;
    }

    @Override
    public boolean deleteNote(String title) {
        return mDatabaseManager.deleteNote(title);
    }

    @Override
    public List<DatabaseEntry> getEntries() {
        return mDatabaseManager.getAllEntries();
    }

    @Override
    public List<DatabaseNote> getNotes() {
        return mDatabaseManager.getAllNotes();
    }

    @Override
    public String getPassword(String website, String username) {
        return mDatabaseManager.getPassword(website, username);
    }

    @Override
    public String getNote(String title) {
        return mDatabaseManager.getNote(title);
    }

    @Override
    public boolean existsPasswordEntry(String website, String username) {
        List<DatabaseEntry> entries = mDatabaseManager.getAllEntries();
        for (DatabaseEntry entry : entries) {
            if (entry.getWebsite().equals(website) && entry.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsNote(String title) {
        List<DatabaseNote> notes = mDatabaseManager.getAllNotes();
        for (DatabaseNote note : notes) {
            if (note.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setExpireStatus(DatabaseEntry entry, boolean status) {
        return mDatabaseManager.setExpireStatus(entry, status);
    }

    @Override
    public List<DatabaseEntry> getExpiredEntries() {
        List<DatabaseEntry> entries = mDatabaseManager.getAllEntries();
        List<DatabaseEntry> result = new LinkedList<>();
        for (DatabaseEntry entry: entries) {
            if (entry.getExpired()) {
                result.add(entry);
            }
        }
        return result;
    }

    @Override
    public List<DatabaseEntry> getEntries(String filter, String field) {
        List<DatabaseEntry> entries = mDatabaseManager.getAllEntries();

        List<DatabaseEntry> result = new LinkedList<>();
        for(DatabaseEntry entry : entries) {
            if (field.equals("Both") &&
                    ((entry.getWebsite().contains(filter)) || (entry.getUsername().contains(filter)))) {
                result.add(entry);
            }
            if (field.equals("Website") && entry.getWebsite().contains(filter)) {
                result.add(entry);
            }
            if (field.equals("Username") && entry.getUsername().contains(filter)) {
                result.add(entry);
            }
        }
        return  result;
    }

    @Override
    public List<DatabaseNote> getNotes(String filter) {
        List<DatabaseNote> notes = mDatabaseManager.getAllNotes();
        List<DatabaseNote> result = new LinkedList<>();

        for (DatabaseNote note : notes) {
            if (note.getTitle().contains(filter)) {
                result.add(note);
            }
        }
        return result;
    }

    @Override
    public boolean createBackUp(String fileName) {
        List<DatabaseEntry> entries = mDatabaseManager.getAllEntries();
        List<DatabaseNote> notes = mDatabaseManager.getAllNotes();

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, fileName);

        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            for (DatabaseEntry entry : entries) {
                br.write((entry.toString() + "\n"));
            }
            for (DatabaseNote note : notes) {
                br.write(note.toString() + "\n");
            }
            br.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean commonPassword(String password) {
        return mDatabasePasswords.commonPassword(password);
    }

    private void updateDuplicatePasswords() {
        List<DatabaseEntry> entries =  mDatabaseManager.getAllEntries();
        for(DatabaseEntry entry : entries) {
            mDatabaseManager.setDuplicated(entry, false);
        }

        for (int i = 0; i < entries.size() - 1; ++i) {
            for (int j = i + 1; j < entries.size(); ++j) {
                mDatabaseManager.setDuplicated(entries.get(i), true);
                mDatabaseManager.setDuplicated(entries.get(i), true);
            }
        }
    }
}
