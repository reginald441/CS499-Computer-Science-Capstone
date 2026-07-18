package com.example.reginaldkargboandroidapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.reginaldkargboandroidapp.model.User;
import com.example.reginaldkargboandroidapp.util.PasswordUtils;

/** Handles account persistence and authentication. */
public class AuthRepository {
    private final DatabaseHelper helper;

    public AuthRepository(DatabaseHelper helper) { this.helper = helper; }

    public boolean createUser(String username, String password) {
        String salt = PasswordUtils.createSalt();
        ContentValues values = new ContentValues();
        values.put("username", username.trim());
        values.put("password_hash", PasswordUtils.hash(password, salt));
        values.put("password_salt", salt);
        values.putNull("password");
        return helper.getWritableDatabase().insert("users", null, values) != -1;
    }

    public User authenticate(String username, String password) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try (Cursor cursor = db.query("users", new String[]{"id", "username", "password", "password_hash", "password_salt"},
                "username = ?", new String[]{username.trim()}, null, null, null, "1")) {
            if (!cursor.moveToFirst()) return null;
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String storedUser = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String legacyPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            String hash = cursor.getString(cursor.getColumnIndexOrThrow("password_hash"));
            String salt = cursor.getString(cursor.getColumnIndexOrThrow("password_salt"));

            if (PasswordUtils.matches(password, salt, hash)) return new User(id, storedUser);
            if (legacyPassword != null && legacyPassword.equals(password)) {
                migrateLegacyPassword(db, id, password);
                return new User(id, storedUser);
            }
            return null;
        }
    }

    private void migrateLegacyPassword(SQLiteDatabase db, long id, String password) {
        String salt = PasswordUtils.createSalt();
        ContentValues values = new ContentValues();
        values.put("password_hash", PasswordUtils.hash(password, salt));
        values.put("password_salt", salt);
        values.putNull("password");
        db.update("users", values, "id = ?", new String[]{String.valueOf(id)});
    }
}
