package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;

import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 871234421; // Just an arbitrary value for testing
        int actual = MathUtils.hash(value);
        assertEquals("Hash of double value should be equal", expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 211627836; // Just an arbitrary value for testing
        int actual = MathUtils.hash(values);
        assertEquals("Hash of double array should be equal", expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.141592653589793;
        double center = 0.0;
        double expected = 1.141592653589793;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals("Normalize angle should return correct value", expected, actual, 0.001);

        a = 10.5;
        center = 2.5;
        expected = 7.783185307179586;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals("Normalize angle should return correct value", expected, actual, 0.001);
    }

    @Test
    public void testReduce() {
        double a = 2.5;
        double period = 3.141592653589793;
        double offset = 1.5707963267948966;
        double expected = 0.38268343236533435;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals("Reduce should return correct value", expected, actual, 0.001);

        a = 6.283185307179586;
        period = 2.5;
        offset = 0.5;
        expected = 4.215231628721996;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals("Reduce should return correct value", expected, actual, 0.001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegative() {
        byte sign = 1;
        byte magnitude = -127;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignBytePositive() {
        byte sign = -1;
        byte magnitude = 127;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test
    public void testCopySignByteValid() {
        byte sign = 1;
        byte magnitude = 5;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign should return correct value for byte", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegative() {
        short sign = 1;
        short magnitude = -32767;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortPositive() {
        short sign = -1;
        short magnitude = 32767;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test
    public void testCopySignShortValid() {
        short sign = 1;
        short magnitude = 5;
        short expected = 5;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign should return correct value for short", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegative() {
        int sign = 1;
        int magnitude = Integer.MIN_VALUE;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntPositive() {
        int sign = -1;
        int magnitude = Integer.MAX_VALUE;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test
    public void testCopySignIntValid() {
        int sign = 1;
        int magnitude = 5;
        int expected = 5;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign should return correct value for int", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegative() {
        long sign = 1;
        long magnitude = Long.MIN_VALUE;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongPositive() {
        long sign = -1;
        long magnitude = Long.MAX_VALUE;
        MathUtils.copySign(magnitude, sign); // This should throw an exception
    }

    @Test
    public void testCopySignLongValid() {
        long sign = 1;
        long magnitude = 5;
        long expected = 5;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign should return correct value for long", expected, actual);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinitePositive() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfiniteNegative() {
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testCheckFiniteDoubleFinite() {
        double value = 1.2;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteDoubleArrayFinite() {
        double[] values = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testCheckNotNullPattern() {
        Object obj = new Object();
        String pattern = LocalizedFormats.NULL_ARGUMENT;
        MathUtils.checkNotNull(obj, pattern);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullPatternNull() {
        Object obj = null;
        String pattern = LocalizedFormats.NULL_ARGUMENT;
        MathUtils.checkNotNull(obj, pattern);
    }

    @Test
    public void testCheckNotNullPatternAndArgs() {
        Object obj = new Object();
        String pattern = LocalizedFormats.ARRAY_ELEMENT;
        int index = 0;
        MathUtils.checkNotNull(obj, pattern, index);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullPatternAndArgsNull() {
        Object obj = null;
        String pattern = LocalizedFormats.ARRAY_ELEMENT;
        int index = 0;
        MathUtils.checkNotNull(obj, pattern, index);
    }
}