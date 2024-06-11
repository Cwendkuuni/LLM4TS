package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = Double.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = Arrays.hashCode(values);
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test(expected = NullArgumentException.class)
    public void testNormalizeAngleNullCenter() {
        double a = Math.PI;
        MathUtils.normalizeAngle(a, null);
    }

    @Test
    public void testNormalizeAngle() {
        double a = Math.PI;
        double center = 0.0;
        double expected = Math.PI - 2 * Math.PI;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.00001);
    }

    @Test
    public void testReduce() {
        double a = Math.PI;
        double period = 2 * Math.PI;
        double offset = Math.PI / 2;
        double expected = Math.PI / 2;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.00001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudeAndPositiveSign() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignBytePositiveMagnitudeAndPositiveSign() {
        byte magnitude = 5;
        byte sign = 1;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, actual);
    }

    @Test
    public void testCopySignBytePositiveMagnitudeAndNegativeSign() {
        byte magnitude = 5;
        byte sign = -1;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals((byte)(-magnitude), actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudeAndNegativeSign() {
        byte magnitude = -128;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShortPositiveMagnitudeAndPositiveSign() {
        short magnitude = 5;
        short sign = 1;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, actual);
    }

    @Test
    public void testCopySignShortPositiveMagnitudeAndNegativeSign() {
        short magnitude = 5;
        short sign = -1;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals((short)(-magnitude), actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeMagnitudeAndPositiveSign() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignIntPositiveMagnitudeAndPositiveSign() {
        int magnitude = 5;
        int sign = 1;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, actual);
    }

    @Test
    public void testCopySignIntPositiveMagnitudeAndNegativeSign() {
        int magnitude = 5;
        int sign = -1;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(-magnitude, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeMagnitudeAndPositiveSign() {
        int magnitude = -Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongPositiveMagnitudeAndPositiveSign() {
        long magnitude = 5L;
        long sign = 1;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, actual);
    }

    @Test
    public void testCopySignLongPositiveMagnitudeAndNegativeSign() {
        long magnitude = 5L;
        long sign = -1;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(-magnitude, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeMagnitudeAndPositiveSign() {
        long magnitude = -Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFinite() {
        double x = 1.2;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinite() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, 2.3, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullPattern() {
        String pattern = null;
        Object[] args = new Object[0];
        MathUtils.checkNotNull(new Object(), pattern, args);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullPatternAndArguments() {
        String pattern = null;
        Object[] args = {1, 2, 3};
        MathUtils.checkNotNull(new Object(), pattern, args);
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}