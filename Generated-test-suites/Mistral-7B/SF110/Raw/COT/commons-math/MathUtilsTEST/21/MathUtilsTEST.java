package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 123.45;
        int expectedHashCode = 12345;
        int actualHashCode = MathUtils.hash(value);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expectedHashCode = 1234567;
        int actualHashCode = MathUtils.hash(values);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.141592653589793;
        double center = 0.0;
        double expectedResult = 0.141592653589793;
        double actualResult = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedResult, actualResult, 0.00000000000001);

        a = 3.141592653589793 + MathUtils.TWO_PI;
        expectedResult = -2.999985849410416;
        actualResult = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedResult, actualResult, 0.00000000000001);
    }

    @Test
    public void testNormalizeAngleWithCenterGreaterThanZero() {
        double a = 3.141592653589793;
        double center = Math.PI;
        double expectedResult = 2.098592152171092;
        double actualResult = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedResult, actualResult, 0.00000000000001);
    }

    @Test
    public void testReduce() {
        double a = 3.141592653589793;
        double period = 2.0;
        double offset = 1.5707963267948966;
        double expectedResult = 1.141592653589793;
        double actualResult = MathUtils.reduce(a, period, offset);
        assertEquals(expectedResult, actualResult, 0.00000000000001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeSignPositiveMagnitude() {
        byte magnitude = 5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudePositiveSign() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteMaxValuePositiveSign() {
        byte magnitude = 127;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDouble() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayElementNaN() {
        double[] values = {Double.POSITIVE_INFINITY, 1.1, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayElementInfinite() {
        double[] values = {Double.POSITIVE_INFINITY, 1.1, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

    @Test
    public void testCheckNotNullStringPattern() {
        Object o = new Object();
        String pattern = "Illegal argument: %s";
        MathUtils.checkNotNull(o, pattern, "argument");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullStringPatternNull() {
        Object o = null;
        String pattern = "Illegal argument: %s";
        MathUtils.checkNotNull(o, pattern, "argument");
    }
}