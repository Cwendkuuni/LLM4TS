package org.apache.commons.lang3;
import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrue() {
        Validate.isTrue(true);
        Validate.isTrue(false, "Expected true but got false");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithMessage() {
        Validate.isTrue(false, "Invalid message: %s", "Test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithValues() {
        Validate.isTrue(false, "Invalid value: %d", 1);
    }
}