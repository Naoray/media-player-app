package de.codematch.naoray.media_player_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by krish on 18.11.2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    // Database Settings
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "UserData.db";
    public static final String TABLE_USER = "user_table";
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Takes String Query -> work like normal SQL
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME + " TEXT, " + PASSWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Deletes Table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addUserInfo(String name, String pw) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME, name);
        contentValues.put(PASSWORD, pw);
        getWritableDatabase().insert(TABLE_USER, null, contentValues);
    }

    public Boolean verifyPassword(String username, String pw) {
        Cursor query = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_USER + " WHERE LOWER(" + USERNAME + ") = LOWER(?)", new String[]{String.valueOf(username)});

        if (query != null && query.getCount() > 0) {
            query.moveToFirst();
            String pw2 = query.getString(2);
            query.close();
            if (pw.equals(pw2)) {
                return true;
            }
        }
        return false;
    }
}
