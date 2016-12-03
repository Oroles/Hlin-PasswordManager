package com.example.oroles.hlin.InterfacesControllers;

import com.example.oroles.hlin.Database.DatabaseEntry;
import com.example.oroles.hlin.Database.DatabaseNote;

import java.util.List;


public interface IRepository {

    boolean addEntry(DatabaseEntry entry);
    boolean addNote(DatabaseNote note);
    boolean deleteEntry(String website, String username);
    boolean deleteNote(String title);

    List<DatabaseEntry> getEntries();
    List<DatabaseNote> getNotes();

    String getPassword(String website, String username);
    String getNote(String title);

    boolean existsPasswordEntry(String website, String username);
    boolean existsNote(String title);


    boolean setExpireStatus(DatabaseEntry entry, boolean status);
    List<DatabaseEntry> getExpiredEntries();
    List<DatabaseEntry> getEntries(String filter, String field);
    List<DatabaseNote> getNotes(String filter);
    boolean createBackUp(String fileName);
    boolean commonPassword(String password);
}
