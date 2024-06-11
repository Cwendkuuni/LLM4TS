package org.example;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 123; // some random hash code
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 123456; // some random hash code
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositiveSign() {
        byte magnitude = -1;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // no exception should be thrown here

        sign = -1;
        try {
            MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            // expected exception
        }
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndNegativeSign() {
        byte magnitude = -1;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign); // no exception should be thrown here

        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            // expected exception
        }
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double x = Double.NaN;
        try {
            MathUtils.checkFinite(x);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // expected exception
        }
    }

    @Test
    public void testNormalizeAngleZero() {
        double a = 0;
        double center = 0;
        double expected = 0;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 1e-14);
    }

    // ... other tests
}