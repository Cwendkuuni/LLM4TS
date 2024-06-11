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
        int expected = Double.doubleToIntBitMask(value) ^ 0x2A05456E; // Arbitrary non-zero constant
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expected = Arrays.hashCode(values);
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.14159265;
        double center = 0.0;
        double expected = -3.14159265; // Angle reduced by one full cycle
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 1e-15);

        a = -6.28318531; // Angle beyond the reduce range
        expected = 2.00615725; // Angle reduced by two full cycles
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 1e-15);

        a = 0.0;
        expected = 0.0;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 1e-15);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleNaN() {
        double a = Double.NaN;
        MathUtils.normalizeAngle(a, 0.0);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleInfinite() {
        double a = Double.POSITIVE_INFINITY;
        MathUtils.normalizeAngle(a, 0.0);
    }

    @Test
    public void testReduce() {
        double a = 3.5;
        double period = 2.0;
        double offset = 1.0;
        double expected = 1.5;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 1e-15);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceNaN() {
        double a = Double.NaN;
        MathUtils.reduce(a, 2.0, 1.0);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceInfinite() {
        double a = Double.POSITIVE_INFINITY;
        MathUtils.reduce(a, 2.0, 1.0);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = -1;
        byte expected = -5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 127;
        sign = -1;
        expected = -127;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 1;
        expected = 5;
        actual = MathUtils.copySign(magnitude, sign);
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
        short magnitude = 32767;
        short sign = -1;
        short expected = -32767;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 1;
        expected = 32767;
        actual = MathUtils.copySign(magnitude, sign);
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
        int magnitude = Integer.MAX_VALUE;
        int sign = -1;
        int expected = -Integer.MIN_VALUE;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 1;
        expected = Integer.MAX_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
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
        long magnitude = Long.MAX_VALUE;
        long sign = -1;
        long expected = -Long.MIN_VALUE;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 1;
        expected = Long.MAX_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDouble() {
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
        double[] val = {1.1, 2.2, 3.3};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] val = {1.1, Double.NaN, 3.3};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNull() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullPattern() {
        Object o = null;
        MathUtils.checkNotNull(o, LocalizedFormats.ARRAY_ELEMENT, 0);
    }

    @Test
    public void testCheckNotNullNotNull() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }
}