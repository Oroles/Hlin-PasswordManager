package com.example.oroles.hlin.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "PasswordDB";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creates query
        String CREATE_PASSWORD_TABLE = "CREATE TABLE passwords (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "website TEXT, " +
                "username TEXT, " +
                "password TEXT, " +
                "date INT, " +
                "expired BIT, " +
                "usedDate INT, " +
                "usedTimes INT, " +
                "duplicated BIT)";

        String CREATE_NOTE_TABLE = "CREATE TABLE notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "text TEXT, " +
                "date INT)";

        // executes query
        db.execSQL(CREATE_PASSWORD_TABLE);
        db.execSQL(CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop table
        db.execSQL("DROP TABLE IF EXISTS passwords");
        db.execSQL("DROP TABLE IF EXISTS notes");

        // creates a new fresh table
        this.onCreate(db);
    }

    // table names
    private static final String TABLE_PASSWORDS = "passwords";
    private static final String TABLE_NOTES = "notes";

    // table columns
    private static final String PASSWORD_KEY_ID = "id";
    private static final String PASSWORD_KEY_WEBSITE = "website";
    private static final String PASSWORD_KEY_USERNAME = "username";
    private static final String PASSWORD_KEY_PASSWORD = "password";
    private static final String PASSWORD_KEY_DATE = "date";
    private static final String PASSWORD_KEY_EXPIRED = "expired";
    private static final String PASSWORD_KEY_USED_DATE = "usedDate";
    private static final String PASSWORD_KEY_USED_TIMES = "usedTimes";
    private static final String PASSWORD_KEY_DUPLICATED = "duplicated";

    private static final String NOTE_KEY_ID = "id";
    private static final String NOTE_KEY_TITLE = "title";
    private static final String NOTE_KEY_TEXT = "text";
    private static final String NOTE_KEY_DATE = "date";

    // all columns
    private static final String[] PASSWORD_COLUMNS = {PASSWORD_KEY_ID,
            PASSWORD_KEY_WEBSITE,
            PASSWORD_KEY_USERNAME,
            PASSWORD_KEY_PASSWORD,
            PASSWORD_KEY_DATE,
            PASSWORD_KEY_EXPIRED,
            PASSWORD_KEY_USED_DATE,
            PASSWORD_KEY_USED_TIMES,
            PASSWORD_KEY_DUPLICATED};

    private static final String[] NOTES_COLUMNS = { NOTE_KEY_ID,
            NOTE_KEY_TITLE,
            NOTE_KEY_TEXT,
            NOTE_KEY_DATE};

    // add book
    public boolean addEntry(DatabaseEntry entry) {

        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(PASSWORD_KEY_WEBSITE, entry.getWebsite());
        values.put(PASSWORD_KEY_USERNAME, entry.getUsername());
        values.put(PASSWORD_KEY_PASSWORD, entry.getPassword());
        values.put(PASSWORD_KEY_DATE, entry.getTime());
        values.put(PASSWORD_KEY_EXPIRED, entry.getExpired());
        values.put(PASSWORD_KEY_USED_DATE, entry.getLastUsedTime());
        values.put(PASSWORD_KEY_USED_TIMES, entry.getUsedTimes());
        values.put(PASSWORD_KEY_DUPLICATED, entry.getDuplicated());

        try {
            //The password and the username can not exists in database to be added
            Cursor cursor = db.query(
                    TABLE_PASSWORDS, PASSWORD_COLUMNS, "website=? and username=?",
                    new String[]{entry.getWebsite(), entry.getUsername()}, null, null, null, null);
            if (cursor != null ) {
                if (cursor.getCount() == 0) {
                    return (db.insert(TABLE_PASSWORDS, null, values) != 0);
                }
                cursor.close();
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean addNote(DatabaseNote note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_KEY_TITLE, note.getTitle());
        values.put(NOTE_KEY_TEXT, note.getText());
        values.put(NOTE_KEY_DATE, note.getCreateTime());

        try {
            Cursor cursor = db.query(
                    TABLE_NOTES, NOTES_COLUMNS, "title=?",
                    new String[]{note.getTitle()}, null, null, null, null
            );
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    return  db.insert(TABLE_NOTES, null, values) != 0;
                }
                cursor.close();
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }

    }

    public String getNote(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(TABLE_NOTES, NOTES_COLUMNS, "title=?", new String[]{title},
                    null, null, null, null);
            String result;
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    result = cursor.getString(3);
                } else {
                    result = "";
                }
                cursor.close();
            } else {
                result = "";
            }
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String getPassword(String website, String username) {
        // get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // build query
            Cursor cursor = db.query(
                    TABLE_PASSWORDS, PASSWORD_COLUMNS, "website=? and username=?",
                    new String[]{website, username}, null, null, null, null);

            // if we got results move to first one
            String result;
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    result = cursor.getString(3);

                    //set the other 2 columns last used date and used times
                    updateUsedTimes(website, username, cursor.getLong(7));
                }
                else {
                    result = "";
                }
                cursor.close();
            }
            else {
                result = "";
            }
            return result;
        }
        catch(SQLException ex) {
            return "";
        }
    }

    public List<DatabaseEntry> getAllEntries() {
        List<DatabaseEntry> entries = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // get reference to DB
        try {
            Cursor cursor = db.query(TABLE_PASSWORDS, PASSWORD_COLUMNS, null, null, null, null, null, null);

            // go over each entry, build it and add it to the result
            DatabaseEntry entry;
            if (cursor.moveToFirst()) {
                do {
                    entry = new DatabaseEntry();
                    entry.setId(Integer.parseInt(cursor.getString(0)));
                    entry.setWebsite(cursor.getString(1));
                    entry.setUsername(cursor.getString(2));
                    entry.setPassword(cursor.getString(3));
                    entry.setTime(cursor.getLong(4));
                    entry.setExpired(cursor.getInt(5) > 0);
                    entry.setLastUsedTime(cursor.getLong(6));
                    entry.setUsedTimes(cursor.getLong(7));
                    entry.setDuplicated(cursor.getInt(5) > 0);

                    entries.add(entry);
                    if (cursor.isLast()) {
                        break;
                    }
                } while (cursor.moveToNext());
            }

            cursor.close();

            //return entries
            return entries;

        } catch (SQLException ex) {
            return entries;
        }
    }

    public List<DatabaseNote> getAllNotes() {
        List<DatabaseNote> notes = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query(TABLE_NOTES, NOTES_COLUMNS, null, null, null, null, null, null);
            DatabaseNote note;
            if (cursor.moveToFirst()) {
                do {
                    note = new DatabaseNote();
                    note.setId(Integer.parseInt(cursor.getString(0)));
                    note.setTitle(cursor.getString(1));
                    note.setText(cursor.getString(2));
                    note.setCreateTime(cursor.getLong(3));

                    notes.add(note);
                    if (cursor.isLast()) {
                        break;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            return notes;
        } catch (SQLException ex) {
            return notes;
        }
    }

    public boolean deleteNote(String title) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NOTES, NOTES_COLUMNS, "title=?",
                    new String[]{title}, null, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    return false;
                }
                cursor.close();
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_NOTES + " WHERE title=?", new String[]{title});
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    public boolean deleteEntry(String website, String username) {

        try {
            // check if we have the entry
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(
                    TABLE_PASSWORDS, PASSWORD_COLUMNS, "website=? and username=?",
                    new String[]{website, username}, null, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    return false;
                }
                cursor.close();
            }
            else {
                return false;
            }
        } catch(SQLException ex) {
            return false;
        }

        try {
            // delete the entry
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_PASSWORDS +
                    " WHERE website=? and username=?", new String[]{website,username});
        } catch (SQLException ex)
        {
            return false;
        }
        return true;
    }

    public boolean setExpireStatus(DatabaseEntry entry, boolean status) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("UPDATE " + TABLE_PASSWORDS + " SET " + PASSWORD_KEY_EXPIRED + " = " +
                    (status ? "1" : "0") + " WHERE website = '" + entry.getWebsite() +
                    "' and username = '" + entry.getUsername()+"';");

            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean setDuplicated(DatabaseEntry entry, boolean duplicated) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("UPDATE " + TABLE_PASSWORDS + " SET " + PASSWORD_KEY_DUPLICATED + " = " +
                    (duplicated ? "1" : "0") + " WHERE website = '" + entry.getWebsite() +
                    "' and username = '" + entry.getUsername()+"';");

            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    private boolean updateUsedTimes(String website, String username, long usedTimes) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE " + TABLE_PASSWORDS + " SET " + PASSWORD_KEY_USED_TIMES + " = " +
                String.valueOf(usedTimes+1) + ", " + PASSWORD_KEY_USED_DATE + " = " +
                    String.valueOf(System.currentTimeMillis()) + " WHERE website = '" + website +
                    "' and username = '" + username +"';");
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
