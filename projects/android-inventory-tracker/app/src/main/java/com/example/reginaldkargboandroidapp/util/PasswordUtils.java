package com.example.reginaldkargboandroidapp.util;

import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

/** Creates salted password hashes so plain-text passwords are not stored. */
public final class PasswordUtils {
    private PasswordUtils() {}

    public static String createSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.encodeToString(salt, Base64.NO_WRAP);
    }

    public static String hash(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(Base64.decode(salt, Base64.NO_WRAP));
            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(hashed, Base64.NO_WRAP);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to hash password.", ex);
        }
    }

    public static boolean matches(String password, String salt, String expectedHash) {
        if (salt == null || expectedHash == null) return false;
        return constantTimeEquals(hash(password, salt), expectedHash);
    }

    private static boolean constantTimeEquals(String left, String right) {
        if (left == null || right == null || left.length() != right.length()) return false;
        int result = 0;
        for (int i = 0; i < left.length(); i++) result |= left.charAt(i) ^ right.charAt(i);
        return result == 0;
    }
}
