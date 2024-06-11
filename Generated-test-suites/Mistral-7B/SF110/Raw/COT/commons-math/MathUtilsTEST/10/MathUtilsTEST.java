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
        int expected = 12345; // arbitrary but non-zero value
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.46, 3.78};
        int expected = 123456789; // arbitrary but non-zero value
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngleSimple() {
        double angle = 0.5 * MathUtils.TWO_PI;
        double expected = 1.1047182417045537;
        double actual = MathUtils.normalizeAngle(angle, MathUtils.TWO_PI);
        assertEquals(expected, actual, 1e-11);
    }

    @Test
    public void testNormalizeAngleWrapped() {
        double angle = 2.0 * MathUtils.TWO_PI;
        double expected = 0.0;
        double actual = MathUtils.normalizeAngle(angle, MathUtils.TWO_PI);
        assertEquals(expected, actual, 1e-11);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleNaN() {
        double angle = Double.NaN;
        MathUtils.normalizeAngle(angle, MathUtils.TWO_PI);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleInfinite() {
        double angle = Double.POSITIVE_INFINITY;
        MathUtils.normalizeAngle(angle, MathUtils.TWO_PI);
    }

    @Test
    public void testReduceSimple() {
        double a = 2.5;
        double period = 1.2;
        double offset = 0.3;
        double expected = 1.2;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 1e-11);
    }

    @Test
    public void testReducePeriodWrapped() {
        double a = 5.0;
        double period = 2.0 * MathUtils.TWO_PI;
        double offset = 1.0 * MathUtils.TWO_PI;
        double expected = 1.0;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 1e-11);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceNaN() {
        double a = Double.NaN;
        double period = 1.2;
        double offset = 0.3;
        MathUtils.reduce(a, period, offset);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceInfinite() {
        double a = Double.POSITIVE_INFINITY;
        double period = 1.2;
        double offset = 0.3;
        MathUtils.reduce(a, period, offset);
    }

    @Test
    public void testCopySignByte() {
        byte sign = 1;
        byte magnitude = 5;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 0;
        magnitude = 5;
        expected = 5;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -128;
        sign = 1;
        expected = -128;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 1;
        magnitude = -128;
        expected = -128;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte sign = 1;
        byte magnitude = -129;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() {
        short sign = 1;
        short magnitude = 5;
        short expected = 5;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 0;
        magnitude = 5;
        expected = 5;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -32768;
        sign = 1;
        expected = -32768;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 1;
        magnitude = -32768;
        expected = -32768;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short sign = 1;
        short magnitude = -32769;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() {
        int sign = 1;
        int magnitude = 5;
        int expected = 5;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 0;
        magnitude = 5;
        expected = 5;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        expected = Integer.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 1;
        magnitude = Integer.MIN_VALUE;
        expected = Integer.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int sign = 1;
        int magnitude = Integer.MIN_VALUE + 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() {
        long sign = 1L;
        long magnitude = 5L;
        long expected = 5L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 0L;
        magnitude = 5L;
        expected = 5L;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Long.MIN_VALUE;
        sign = 1L;
        expected = Long.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 1L;
        magnitude = Long.MIN_VALUE;
        expected = Long.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long sign = 1L;
        long magnitude = Long.MIN_VALUE + 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDouble() {
        double x = Math.E;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {Double.POSITIVE_INFINITY, 3.14, Math.E, -Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {Double.NaN, 3.14, Math.E, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o, LocalizedFormats.NON_NULL, "o");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object o = null;
        MathUtils.checkNotNull(o, LocalizedFormats.NON_NULL, "o");
    }

    @Test
    public void testCheckNotNullObjects() {
        Object o1 = new Object();
        Object o2 = new Object();
        MathUtils.checkNotNull(o1, o2, LocalizedFormats.NON_NULL, "o1", "o2");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectsNull() {
        Object o1 = null;
        Object o2 = new Object();
        MathUtils.checkNotNull(o1, o2, LocalizedFormats.NON_NULL, "o1", "o2");
    }
}