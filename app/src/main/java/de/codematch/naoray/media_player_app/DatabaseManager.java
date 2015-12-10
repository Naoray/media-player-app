package de.codematch.naoray.media_player_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {
    // Database Settings
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "UserData.db";
    public static final String TABLE_USER = "user_table";
    public static final String ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    private Cursor query;
    private String username;
    private String password;

    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Takes String Query -> work like normal SQL
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USER + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_USERNAME + " TEXT, " + KEY_PASSWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Deletes Table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void handleUserInput(String username, String pw) {
        this.username = username;
        this.password = pw;
        if (checkIfUserExists()) {
            if (checkIfPasswordHasChanged()) {
                updateUserPassword();
            }
        } else {
            addUserInfo();
        }

        // for debugging purposes
        query = getReadableDatabase().query(TABLE_USER, null, null, null, null, null, null);
        while (query.moveToNext()) {
            Log.d("Ausgabe", query.getString(1) + " " + query.getString(2));
        }
    }

    private void updateUserPassword() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PASSWORD, password);
        getWritableDatabase().update(TABLE_USER, contentValues, KEY_USERNAME + " = ?",
                new String[]{username});
    }

    private Boolean checkIfPasswordHasChanged() {
        query.moveToFirst();
        String pwData = query.getString(1);
        query.close();
        return !password.equals(pwData);
    }

    private void addUserInfo() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USERNAME, username);
        contentValues.put(KEY_PASSWORD, password);
        getWritableDatabase().insert(TABLE_USER, null, contentValues);
    }

    private Boolean checkIfUserExists() {
        query = getUserData(username);

        return query != null && query.getCount() == 1;
    }

    // returns Data of a specific user
    public Cursor getUserData(String username) {
        return getReadableDatabase().rawQuery("SELECT username, password FROM " + TABLE_USER + " WHERE LOWER(" + KEY_USERNAME + ") = LOWER(?)", new String[]{String.valueOf(username)});
    }

    public Boolean verifyPassword(String username, String pw) {
        query = getUserData(username);

        if (query != null && query.getCount() > 0) {
            query.moveToFirst();
            String pw2 = query.getString(1);
            query.close();
            if (pw.equals(pw2)) {
                return true;
            }
        }
        return false;
    }
}
