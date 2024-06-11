package org.apache.commons.lang3;
import org.junit.Test;
import org.junit.Assert.*; // for Assert.assertTrue() and Assert.assertThrows()

public class ValidateTest {

    @Test
    public void testIsTrueWithTrueValues() {
        boolean expression1 = true;
        boolean expression2 = true;
        boolean expression3 = true;

        Validate.isTrue(expression1);
        Validate.isTrue(expression2);
        Validate.isTrue(expression3);

        // No exception thrown, so the test is successful
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithFalseValues() {
        boolean expression1 = false;
        boolean expression2 = false;
        boolean expression3 = false;

        Validate.isTrue(expression1);
        Validate.isTrue(expression2);
        Validate.isTrue(expression3);

        // Three IllegalArgumentException exceptions should be thrown
    }
}