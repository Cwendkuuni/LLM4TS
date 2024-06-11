package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class ValidateTest {

    @Test
    public void testIsTrueWithTrueValue() {
        boolean expression = true;
        String message = "The value %d is true";
        long value = 42;
        Validate.isTrue(expression, message, value);
        // No exception thrown, test passes
        assertTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseValue() {
        boolean expression = false;
        String message = "The value %d is true";
        long value = 42;
        Validate.isTrue(expression, message, value);
        // Exception should be thrown
    }
}