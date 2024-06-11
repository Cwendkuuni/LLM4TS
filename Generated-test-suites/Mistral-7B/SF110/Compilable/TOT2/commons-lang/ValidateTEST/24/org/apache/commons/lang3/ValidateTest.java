package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        // Test with boolean value and empty message
        Validate.isTrue(true);

        // Test with boolean value and custom message
        Validate.isTrue(true, "Expected true, but got false");

        // Test with boolean value, value and custom message
        Validate.isTrue(true, "Expected true, but got %s", false);

        // Test with boolean value, value, and empty message
        Validate.isTrue(true, "%s", false);

        // Test with boolean value, value, and custom message
        Validate.isTrue(true, "%s", "Expected true, but got false");

        // Test with false value and empty message
        try {
            Validate.isTrue(false);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with false value and custom message
        try {
            Validate.isTrue(false, "Expected true, but got false");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with false value, value and empty message
        try {
            Validate.isTrue(false, "%s", true);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with false value, value, and custom message
        try {
            Validate.isTrue(false, "%s", "Expected true, but got false");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}