package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 3.14;
        int expected = 12345; // Any valid hashcode can be used here for testing
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.14159265;
        double center = 1.5708; // PI/2
        double expected = -2.61803398; // Angle that is 3.14159265 radians ahead of PI/2
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -1;
        byte expected = -10;
        try {
            byte actual = MathUtils.copySign(magnitude, sign);
            assertEquals(expected, actual);
        } catch (MathArithmeticException e) {
            // This test case should not throw an exception
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Add tests for other methods, such as hashArray and copySign methods for short, int, long, checkFinite, checkNotNull.

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInf() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullArray() {
        Object[] o = null;
        MathUtils.checkNotNull(o);
    }
}