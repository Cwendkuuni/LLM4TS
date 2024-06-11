package org.junit.test;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 12345; // Replace with the expected hash code
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    // Test all edge cases and exception handling scenarios

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = Byte.MIN_VALUE;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = Short.MIN_VALUE;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinity() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNegativeInfinity() {
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, "Failed to check null object", "First argument");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullEmptyString() {
        MathUtils.checkNotNull("", "Failed to check null object", "Second argument");
    }

    // Test other methods with similar test cases

    // Add more test cases as needed
}