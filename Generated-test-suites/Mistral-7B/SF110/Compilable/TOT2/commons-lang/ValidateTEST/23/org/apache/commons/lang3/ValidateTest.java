package org.apache.commons.lang3;
import org.junit.Test;
import org.apache.commons.lang3.Validate;

public class ValidateTest {

    @Test
    public void testIsTrueWithBooleanAndMessage() {
        boolean expression = true;
        String message = "Test message";
        long value = 10L;
        Validate.isTrue(expression, message, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseBooleanAndMessage() {
        boolean expression = false;
        String message = "Test message";
        long value = 10L;
        Validate.isTrue(expression, message, value);
    }
}