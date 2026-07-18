package com.example.reginaldkargboandroidapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.reginaldkargboandroidapp.model.InventoryItem;
import java.util.ArrayList;
import java.util.List;

/** Provides user-scoped CRUD operations for inventory records. */
public class InventoryRepository {
    private final DatabaseHelper helper;
    public InventoryRepository(DatabaseHelper helper) { this.helper = helper; }

    public boolean add(String owner, String name, int quantity, String location) {
        ContentValues values = values(owner, name, quantity, location);
        return helper.getWritableDatabase().insert("inventory", null, values) != -1;
    }

    public boolean update(long id, String owner, String name, int quantity, String location) {
        return helper.getWritableDatabase().update("inventory", values(owner, name, quantity, location),
                "id = ? AND owner = ?", new String[]{String.valueOf(id), owner}) == 1;
    }

    public boolean delete(long id, String owner) {
        return helper.getWritableDatabase().delete("inventory", "id = ? AND owner = ?",
                new String[]{String.valueOf(id), owner}) == 1;
    }

    public List<InventoryItem> getForUser(String owner) {
        List<InventoryItem> items = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        try (Cursor cursor = db.query("inventory", new String[]{"id", "owner", "name", "quantity", "location"},
                "owner = ?", new String[]{owner}, null, null, "name COLLATE NOCASE ASC")) {
            while (cursor.moveToNext()) {
                items.add(new InventoryItem(
                        cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("owner")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("location"))));
            }
        }
        return items;
    }

    private ContentValues values(String owner, String name, int quantity, String location) {
        ContentValues values = new ContentValues();
        values.put("owner", owner);
        values.put("name", name.trim());
        values.put("quantity", quantity);
        values.put("location", location.trim());
        return values;
    }
}
