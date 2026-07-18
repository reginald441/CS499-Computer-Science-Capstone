package com.example.reginaldkargboandroidapp;

import com.example.reginaldkargboandroidapp.util.ValidationUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationUtilsTest {
    @Test public void validItemReturnsNull() {
        assertNull(ValidationUtils.validateItem("Laptop", "5", "Room A"));
    }

    @Test public void nonNumericQuantityReturnsMessage() {
        assertNotNull(ValidationUtils.validateItem("Laptop", "five", "Room A"));
    }

    @Test public void negativeQuantityReturnsMessage() {
        assertNotNull(ValidationUtils.validateItem("Laptop", "-1", "Room A"));
    }

    @Test public void strongPasswordAccepted() {
        assertNull(ValidationUtils.validatePassword("Inventory9"));
    }

    @Test public void shortPasswordRejected() {
        assertNotNull(ValidationUtils.validatePassword("abc1"));
    }
}
