package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedHashCode = 123; // Any valid hash code for testing purpose
        int actualHashCode = MathUtils.hash(value);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expectedHashCode = 456; // Any valid hash code for testing purpose
        int actualHashCode = MathUtils.hash(values);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testNormalizeAngleSimpleCase() {
        double a = 0.0;
        double center = 0.0;
        double expectedResult = 0.0;
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedResult, result, 0.0);
    }

    @Test
    public void testNormalizeAngleEdgeCase1() {
        double a = 6.283185307179586; // 2 * PI
        double center = 0.0;
        double expectedResult = 0.0;
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedResult, result, 0.0);
    }

    @Test
    public void testNormalizeAngleEdgeCase2() {
        double a = -6.283185307179586; // -2 * PI
        double center = 0.0;
        double expectedResult = 0.0;
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedResult, result, 0.0);
    }

    @Test
    public void testNormalizeAngleEdgeCase3() {
        double a = 3.141592653589793; // PI
        double center = 2.1 * MathUtils.TWO_PI; // PI + PI
        double expectedResult = -3.141592653589793; // -PI
        double result = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedResult, result, 0.0);
    }

    @Test
    public void testReduceSimpleCase() {
        double a = 0.0;
        double period = 2.0 * Math.PI;
        double offset = 0.0;
        double expectedResult = 0.0;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expectedResult, result, 0.0);
    }

    @Test
    public void testReduceEdgeCase1() {
        double a = 2.0 * Math.PI;
        double period = 2.0 * Math.PI;
        double offset = 0.0;
        double expectedResult = 0.0; // Or 4.0 * Math.PI, the result is periodic
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expectedResult, result, 0.0);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositiveSignOverflow() {
        byte magnitude = -129;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignBytePositiveAndNegativeOverflow() {
        byte magnitude = 127;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignByteValidCase() {
        byte magnitude = 127;
        byte sign = 1;
        byte expectedResult = magnitude;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, result);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, LocalizedFormats.OBJECT_NULL, "testObject");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObjectWithoutMessage() {
        MathUtils.checkNotNull(null);
    }

    @Test
    public void testCheckNotNullValidObject() {
        Object testObject = new Object();
        MathUtils.checkNotNull(testObject);
    }
}