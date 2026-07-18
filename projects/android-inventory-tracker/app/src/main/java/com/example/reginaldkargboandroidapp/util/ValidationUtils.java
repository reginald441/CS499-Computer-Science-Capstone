package com.example.reginaldkargboandroidapp.util;

/** Centralized validation rules used by the view-model layer. */
public final class ValidationUtils {
    private ValidationUtils() {}

    public static String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) return "Username is required.";
        String clean = username.trim();
        if (clean.length() < 3 || clean.length() > 30) return "Username must be 3 to 30 characters.";
        if (!clean.matches("[A-Za-z0-9_.-]+")) return "Username can use letters, numbers, periods, dashes, and underscores.";
        return null;
    }

    public static String validatePassword(String password) {
        if (password == null || password.isEmpty()) return "Password is required.";
        if (password.length() < 8) return "Password must be at least 8 characters.";
        if (!password.matches(".*[A-Za-z].*")) return "Password must include a letter.";
        if (!password.matches(".*[0-9].*")) return "Password must include a number.";
        return null;
    }

    public static String validateItem(String name, String quantityText, String location) {
        if (name == null || name.trim().isEmpty()) return "Item name is required.";
        if (name.trim().length() > 60) return "Item name must be 60 characters or fewer.";
        if (location == null || location.trim().isEmpty()) return "Location is required.";
        if (location.trim().length() > 80) return "Location must be 80 characters or fewer.";
        try {
            int quantity = Integer.parseInt(quantityText == null ? "" : quantityText.trim());
            if (quantity < 0 || quantity > 1_000_000) return "Quantity must be between 0 and 1,000,000.";
        } catch (NumberFormatException ex) {
            return "Quantity must be a valid whole number.";
        }
        return null;
    }
}
