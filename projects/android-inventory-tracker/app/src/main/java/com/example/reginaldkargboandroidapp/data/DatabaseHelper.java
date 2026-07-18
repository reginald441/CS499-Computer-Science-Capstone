package com.example.reginaldkargboandroidapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** Owns the SQLite schema. Query logic is kept in repositories. */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "project_two_inventory.db";
    public static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT, " +
                "password_hash TEXT, " +
                "password_salt TEXT)");
        db.execSQL("CREATE TABLE inventory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "owner TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL CHECK(quantity >= 0), " +
                "location TEXT NOT NULL)");
        db.execSQL("CREATE INDEX idx_inventory_owner ON inventory(owner)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            addColumnIfMissing(db, "users", "password_hash", "TEXT");
            addColumnIfMissing(db, "users", "password_salt", "TEXT");
            addColumnIfMissing(db, "inventory", "owner", "TEXT NOT NULL DEFAULT ''");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_inventory_owner ON inventory(owner)");
        }
    }

    private void addColumnIfMissing(SQLiteDatabase db, String table, String column, String type) {
        try { db.execSQL("ALTER TABLE " + table + " ADD COLUMN " + column + " " + type); }
        catch (Exception ignored) { /* Existing installations may already contain the column. */ }
    }
}
