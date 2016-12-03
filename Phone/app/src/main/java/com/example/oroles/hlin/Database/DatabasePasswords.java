package com.example.oroles.hlin.Database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import com.example.oroles.hlin.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DatabasePasswords extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 10;
    public static final String DATABASE_NAME = "CommonPasswordsDB";
    private Context mContext;

    public DatabasePasswords(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PASSWORD_TABLE = "Create TABLE commonPasswords (" +
                "password TEXT)";
        db.execSQL(CREATE_PASSWORD_TABLE);

        Toast.makeText(mContext, "Loading database", Toast.LENGTH_LONG).show();

        try {
            InputStreamReader reader =  new InputStreamReader( mContext.getResources().openRawResource(R.raw.commonwords) );
            BufferedReader br = new BufferedReader(reader);
            String line;

            while ((line = br.readLine()) != null) {
                String aux = "INSERT INTO commonPasswords VALUES('" + line + "')";
                db.execSQL(aux);
            }
            br.close();
        }
        catch (IOException e) {

            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS commonPasswords");

        this.onCreate(db);
    }

    private static final String TABLE_PASSWORDS = "commonPasswords";
    private static final String KEY_PASSWORD = "password";
    private static final String[] COLUMNS = {KEY_PASSWORD};

    public boolean commonPassword(String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(
                    TABLE_PASSWORDS, COLUMNS, "password=?",
                    new String[]{password}, null, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    cursor.close();
                    return true;
                }
                cursor.close();
                return false;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
}
