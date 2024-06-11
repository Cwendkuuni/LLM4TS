package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = 1314342681; // Hash code for 1.23
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.718, 3.14159265};
        int expected = 234282824; // Hash code for the given array
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 6.28 + Math.PI;
        double center = Math.PI;
        double expected = 0.7853981633974483; // Result of Math.sin(0.7853981633974483)
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 1e-10);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithNaN() {
        double angle = Double.NaN;
        double center = Math.PI;
        MathUtils.normalizeAngle(angle, center);
    }

    @Test
    public void testReduce() {
        double a = Math.PI * 2 + Math.PI / 4;
        double period = Math.PI;
        double offset = Math.PI / 2;
        double expected = Math.PI / 4;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 1e-10);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignByteNegativeSign() {
        byte magnitude = 5;
        byte sign = -1;
        byte expected = -5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 5;
        short sign = 1;
        short expected = 5;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignShortNegativeSign() {
        short magnitude = 5;
        short sign = -1;
        short expected = -5;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 5;
        int sign = 1;
        int expected = 5;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignIntNegativeSign() {
        int magnitude = 5;
        int sign = -1;
        int expected = -5;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 5L;
        long sign = 1L;
        long expected = 5L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignLongNegativeSign() {
        long magnitude = 5L;
        long sign = -1L;
        long expected = -5L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1L;
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

    @Test
    public void testCheckFiniteDoubleFinite() {
        double value = 42.0;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = new double[]{Double.NaN, 3.14};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = new double[]{Double.POSITIVE_INFINITY, 3.14};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteDoubleArrayFinite() {
        double[] values = new double[]{3.14, 5.6};
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
    public void testCheckNotNullObjectLocalizedPattern() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, LocalizedFormats.INVALID_ARGUMENT, new Object[]{"object"});
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectLocalizedPatternArgs() {
        Object obj = null;
        MathUtils.checkNotNull(obj, LocalizedFormats.INVALID_ARGUMENT, new Object[]{"expectedObject", "actualObject"});
    }
}