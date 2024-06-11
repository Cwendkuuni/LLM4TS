package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(123456789L, MathUtils.hash(1.23456789));
        assertEquals(-123456789L, MathUtils.hash(-1.23456789));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        long expectedHash = 770487833L; // calculated using Apache Commons Math3's Arrays.hashCode()
        assertEquals(expectedHash, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        for (double angle : new double[]{-3.14159265, -1.57079633, -0.78539816, 0.78539816, 1.57079633, 2.35619449, 3.14159265, 4.71238898}) {
            double center = 0.0;
            double normalizedAngle = MathUtils.normalizeAngle(angle, center);
            assertEquals(Math.IEEEremainder(angle + 3.14159265, 6.283185307179586), Math.IEEEremainder(normalizedAngle + 3.14159265, 6.283185307179586), 0.000001);
        }
    }

    @Test
    public void testReduce() {
        double a = 1.5 * Math.PI;
        double period = 2.0 * Math.PI;
        double offset = Math.PI;
        double expectedResult = 0.5 * Math.PI;
        assertEquals(expectedResult, MathUtils.reduce(a, period, offset), 0.000001);

        a = -1.5 * Math.PI;
        period = 2.0 * Math.PI;
        offset = -Math.PI;
        expectedResult = -0.5 * Math.PI;
        assertEquals(expectedResult, MathUtils.reduce(a, period, offset), 0.000001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudePositiveSign() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudeNegativeSign() {
        byte magnitude = -5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignBytePositiveMagnitudeNegativeSign() {
        byte magnitude = 5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignByteValidInputs() {
        byte magnitude = 5;
        byte sign = 1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    // Repeat the above test cases for short, int, and long types

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);

        double finiteNumber = 1.23456789;
        MathUtils.checkFinite(finiteNumber);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInvalidElement() {
        double[] values = {1.1, Double.POSITIVE_INFINITY, 3.14159265};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteDoubleArrayAllFinite() {
        double[] values = {1.1, 2.2, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullArgument() {
        MathUtils.checkNotNull(null, "An argument is null.");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}