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

    @Test
    public void testNormalizeAngle() {
        double a = 3.141592653589793;
        double center = 0.0;
        double expected = 0.7853981633974483;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0000001);

        a = 6.283185307179586;
        center = 3.141592653589793;
        expected = 0.0;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0);

        a = -3.141592653589793;
        center = 0.0;
        expected = -0.7853981633974483;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0000001);
    }

    @Test
    public void testReduce() {
        double a = 1.5;
        double period = 2.0;
        double offset = 0.5;
        double expected = 0.3;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0000001);

        a = 5.5;
        period = 2.0;
        offset = 0.5;
        expected = 1.5;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0000001);

        a = -1.5;
        period = 2.0;
        offset = 0.5;
        expected = -0.3;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.0000001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositive() {
        byte magnitude = -1;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndNegative() {
        byte magnitude = -1;
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
    public void testCopySignShort() {
        short magnitude = -123;
        short sign = 1;
        short expected = 123;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -32768;
        sign = 1;
        expected = 32768;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -32768;
        sign = -1;
        expected = -32768;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeAndPositive() {
        short magnitude = -1;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeAndNegative() {
        short magnitude = -1;
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
    public void testCopySignInt() {
        int magnitude = -123;
        int sign = 1;
        int expected = 123;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        expected = Integer.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Integer.MIN_VALUE;
        sign = -1;
        expected = -Integer.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeAndPositive() {
        int magnitude = -1;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeAndNegative() {
        int magnitude = -1;
        int sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = -123L;
        long sign = 1;
        long expected = 123L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Long.MIN_VALUE;
        sign = 1;
        expected = Long.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Long.MIN_VALUE;
        sign = -1;
        expected = -Long.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeAndPositive() {
        long magnitude = -1;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeAndNegative() {
        long magnitude = -1;
        long sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNullElement() {
        double[] values = new double[1];
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaNElement() {
        double[] values = new double[1];
        values[0] = Double.NaN;
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfiniteElement() {
        double[] values = new double[1];
        values[0] = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayMixed() {
        double[] values = new double[1];
        values[0] = 1.0;
        MathUtils.checkFinite(values);

        values[0] = Double.NaN;
        try {
            MathUtils.checkFinite(values);
            fail();
        } catch (NotFiniteNumberException e) {
            // expected
        }

        values[0] = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(values);
            fail();
        } catch (NotFiniteNumberException e) {
            // expected
        }
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
        double value = 1.0;
        MathUtils.checkFinite(value);
    }
}