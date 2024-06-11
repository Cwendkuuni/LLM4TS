package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {

    @org.junit.Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = Double.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals("Hash code of double should be the same", expected, actual);
    }

    @org.junit.Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = Arrays.hashCode(values);
        int actual = MathUtils.hash(values);
        assertEquals("Hash code of double array should be the same", expected, actual);
    }

    @org.junit.Test
    public void testNormalizeAngle() {
        double a = 3.141592653589793;
        double center = 0.0;
        double expected = 0.7853981633974483;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals("Normalize angle should return the expected value", expected, actual, 1e-12);

        a = 6.283185307179586;
        center = 3.141592653589793;
        expected = 0.0;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals("Normalize angle should return the expected value", expected, actual, 1e-12);

        a = -3.141592653589793;
        center = 0.0;
        expected = -0.7853981633974483;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals("Normalize angle should return the expected value", expected, actual, 1e-12);
    }

    @org.junit.Test
    public void testReduce() {
        double a = 1.5;
        double period = 2.0;
        double offset = 1.0;
        double expected = 0.5;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals("Reduce should return the expected value", expected, actual, 1e-12);

        a = -1.5;
        expected = -0.5;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals("Reduce should return the expected value", expected, actual, 1e-12);

        a = 1.5;
        period = 0.5;
        expected = 0.0;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals("Reduce should return the expected value", expected, actual, 1e-12);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositive() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testCopySignBytePositiveAndNegative() {
        byte magnitude = 5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
        public void testCopySignBytePositiveAndOverflow() {
            byte magnitude = 127;
            byte sign = 1;
            MathUtils.copySign(magnitude, sign);
        }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @org.junit.Test
    public void testCopySignShortNegativeAndPositive() {
        short magnitude = -5;
        short sign = 1;
        short actual = MathUtils.copySign(magnitude, sign);
        short expected = magnitude;
        assertEquals("Copy sign should return the same value as the magnitude", expected, actual);
    }

    @org.junit.Test
    public void testCopySignShortPositiveAndNegative() {
        short magnitude = 5;
        short sign = -1;
        short actual = MathUtils.copySign(magnitude, sign);
        short expected = -magnitude;
        assertEquals("Copy sign should return the negative of the magnitude", expected, actual);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeAndOverflow() {
        short magnitude = -32767;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testCopySignShortPositiveAndOverflow() {
        short magnitude = 32767;
        short sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @org.junit.Test
    public void testCopySignIntNegativeAndPositive() {
        int magnitude = -5;
        int sign = 1;
        int actual = MathUtils.copySign(magnitude, sign);
        int expected = magnitude;
        assertEquals("Copy sign should return the same value as the magnitude", expected, actual);
    }

    @org.junit.Test
    public void testCopySignIntPositiveAndNegative() {
        int magnitude = 5;
        int sign = -1;
        int actual = MathUtils.copySign(magnitude, sign);
        int expected = -magnitude;
        assertEquals("Copy sign should return the negative of the magnitude", expected, actual);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeAndOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testCopySignIntPositiveAndOverflow() {
        int magnitude = Integer.MAX_VALUE;
        int sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @org.junit.Test
    public void testCopySignLongNegativeAndPositive() {
        long magnitude = -5;
        long sign = 1;
        long actual = MathUtils.copySign(magnitude, sign);
        long expected = magnitude;
        assertEquals("Copy sign should return the same value as the magnitude", expected, actual);
    }

    @org.junit.Test
    public void testCopySignLongPositiveAndNegative() {
        long magnitude = 5;
        long sign = -1;
        long actual = MathUtils.copySign(magnitude, sign);
        long expected = -magnitude;
        assertEquals("Copy sign should return the negative of the magnitude", expected, actual);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeAndOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @org.junit.Test(expected = MathArithmeticException.class)
    public void testCopySignLongPositiveAndOverflow() {
        long magnitude = Long.MAX_VALUE;
        long sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @org.junit.Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNegativeInfinity() {
        double value = Double.NEGATIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @org.junit.Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoublePositiveInfinity() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @org.junit.Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value);
    }

    @org.junit.Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayEmpty() {
        MathUtils.checkFinite(new double[0]);
    }

    @org.junit.Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @org.junit.Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {Double.NaN};
        MathUtils.checkFinite(values);
    }

    @org.junit.Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayMixed() {
        double[] values = {Double.NEGATIVE_INFINITY, 3.14, Double.POSITIVE_INFINITY, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @org.junit.Test
    public void testCheckNotNullObjectNull() {
        MathUtils.checkNotNull(null, "Argument cannot be null");
    }

    @org.junit.Test
    public void testCheckNotNullObjectNotNull() {
        MathUtils.checkNotNull(new Object(), "Argument cannot be null");
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNullPattern() {
        MathUtils.checkNotNull(null, "The argument is null", new Object[0]);
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNotNullPattern() {
        MathUtils.checkNotNull(new Object(), "The argument is null", new Object[] {"argument"});
    }
}