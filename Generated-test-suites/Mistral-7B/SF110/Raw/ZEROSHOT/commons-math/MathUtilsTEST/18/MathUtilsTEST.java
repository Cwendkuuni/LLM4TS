package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = Double.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.46, 3.67};
        int expected = Arrays.hashCode(values);
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 3.14;
        double center = 0.0;
        double expected = 0.14;
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.001);

        angle = 6.28;
        expected = 0.28;
        actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.001);

        angle = -3.14;
        expected = -0.46;
        actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.001);

        angle = -6.28;
        expected = -1.18;
        actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testReduce() {
        double a = 1.57;
        double period = 6.28;
        double offset = 1.57;
        double expected = 0.11;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.001);

        a = 7.12;
        period = 3.14;
        offset = 1.57;
        expected = 1.27;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.001);

        a = -5.01;
        period = 2.0;
        offset = 0.5;
        expected = -3.51;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositive() {
        byte magnitude = -50;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndNegative() {
        byte magnitude = -50;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignByteValid() {
        byte magnitude = 50;
        byte sign = 1;
        byte expected = 50;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -50;
        sign = -1;
        expected = -50;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeAndPositive() {
        short magnitude = -5000;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeAndNegative() {
        short magnitude = -5000;
        short sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShortValid() {
        short magnitude = 5000;
        short sign = 1;
        short expected = 5000;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -5000;
        sign = -1;
        expected = -5000;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeAndPositive() {
        int magnitude = -50000;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignIntValid() {
        int magnitude = 50000;
        int sign = 1;
        int expected = 50000;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -50000;
        sign = -1;
        expected = -50000;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeAndPositive() {
        long magnitude = -500000L;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongValid() {
        long magnitude = 500000L;
        long sign = 1;
        long expected = 500000L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -500000L;
        sign = -1;
        expected = -500000L;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.23;
        MathUtils.checkFinite(value);
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
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.23, 2.46, 3.67};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {1.23, Double.NaN, 3.67};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {1.23, 2.46, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullNullObject() {
        Object obj = null;
        MathUtils.checkNotNull(obj, LocalizedFormats.UNKNOWN_OBJECT, new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObjectPattern() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}