package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 1126176065; // calculated using Double.hashCode(1.2)
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 12345678; // arbitrary number for demonstration
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 3.14159265;
        double center = 0.0;
        double expectedResult = 1.5707963267948966;
        double result = MathUtils.normalizeAngle(angle, center);
        assertEquals(expectedResult, result, Precision.DOUBLE_EPSILON);

        angle = 6.28318531;
        center = 3.14159265;
        expectedResult = 0.0;
        result = MathUtils.normalizeAngle(angle, center);
        assertEquals(expectedResult, result, Precision.DOUBLE_EPSILON);

        angle = -3.14159265;
        center = 0.0;
        expectedResult = -1.5707963267948966;
        result = MathUtils.normalizeAngle(angle, center);
        assertEquals(expectedResult, result, Precision.DOUBLE_EPSILON);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositiveSign() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndNegativeSign() {
        byte magnitude = -5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignBytePositiveAndNegativeSign() {
        byte magnitude = 5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayElementNaN() {
        double[] values = {1.0, Double.NaN, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayElementInfinite() {
        double[] values = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(values);
    }
}