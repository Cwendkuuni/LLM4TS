package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 12345; // any valid hashcode
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test(expected = NullArgumentException.class)
    public void testHashNullDouble() {
        MathUtils.hash(null);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2};
        int expected = 123456; // any valid hashcode
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test(expected = NullArgumentException.class)
    public void testHashNullDoubleArray() {
        MathUtils.hash(null);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 3.141592653589793;
        double expected = -3.141592653589793;
        double actual = MathUtils.normalizeAngle(angle, 0.0);
        assertEquals(expected, actual, 1e-14); // tolerate small errors
    }

    @Test
    public void testNormalizeAngleWithCenter() {
        double angle = 4.5;
        double center = 2.5;
        double expected = 1.5;
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeAngleInvalidCenter() {
        double angle = 0.0;
        double center = 4.5;
        MathUtils.normalizeAngle(angle, center); // should throw exception
    }

    @Test
    public void testReduce() {
        double a = 3.141592653589793;
        double period = 2.0 * MathUtils.PI;
        double offset = MathUtils.PI;
        double expected = -0.6545038598221964; // calculated result
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 1e-14);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudeAndPositiveSign() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // should throw exception
    }

    @Test
    public void testCopySignByteValid() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignByteNegativeMagnitudeAndNegativeSign() {
        byte magnitude = -5;
        byte sign = -1;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteMagnitudeOverflow() {
        byte magnitude = 127;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // should throw exception
    }

    @Test
    public void testCopySignShortValid() {
        short magnitude = 5;
        short sign = 1;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignShortNegativeMagnitudeAndNegativeSign() {
        short magnitude = -5;
        short sign = -1;
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortMagnitudeOverflow() {
        short magnitude = 32767;
        short sign = 1;
        MathUtils.copySign(magnitude, sign); // should throw exception
    }

    @Test
    public void testCopySignIntValid() {
        int magnitude = 5;
        int sign = 1;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignIntNegativeMagnitudeAndNegativeSign() {
        int magnitude = -5;
        int sign = -1;
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntMagnitudeOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign); // should throw exception
    }

    @Test
    public void testCopySignLongValid() {
        long magnitude = 5L;
        long sign = 1;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignLongNegativeMagnitudeAndNegativeSign() {
        long magnitude = -5L;
        long sign = -1;
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongMagnitudeOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign); // should throw exception
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNullArray() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayElementInfinite() {
        double[] arr = {1.0, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(arr);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayElementNaN() {
        double[] arr = {1.0, Double.NaN};
        MathUtils.checkFinite(arr);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, "Object should not be null");
    }

    @Test
    public void testCheckNotNullValidObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}