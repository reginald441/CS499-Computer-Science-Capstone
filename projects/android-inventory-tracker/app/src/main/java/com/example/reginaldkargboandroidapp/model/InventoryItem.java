package com.example.reginaldkargboandroidapp.model;

/** Immutable inventory record displayed by the UI. */
public class InventoryItem {
    private final long id;
    private final String owner;
    private final String name;
    private final int quantity;
    private final String location;

    public InventoryItem(long id, String owner, String name, int quantity, String location) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.quantity = quantity;
        this.location = location;
    }

    public long getId() { return id; }
    public String getOwner() { return owner; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getLocation() { return location; }
}
