package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = Double.hashCode(value);
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testHashNullDouble() {
        MathUtils.hash(null);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = Arrays.hashCode(values);
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testHashNullDoubleArray() {
        MathUtils.hash(null);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 3.1415;
        double center = 1.5708; // PI/2
        double expected = 0.6825; // PI/4
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testReduce() {
        double value = 1.5;
        double period = 2.5;
        double offset = 0.5;
        double expected = 0.625;
        double actual = MathUtils.reduce(value, period, offset);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -128;
        sign = 1;
        expected = -128;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = -1;
        expected = 127;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        try {
            sign = -1;
            magnitude = -128;
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 5;
        short sign = 1;
        short expected = 5;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -32768;
        sign = 1;
        expected = -32768;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = -1;
        expected = 32767;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        try {
            sign = -1;
            magnitude = -32768;
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 5;
        int sign = 1;
        int expected = 5;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        expected = Integer.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = -1;
        expected = -5;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        try {
            sign = -1;
            magnitude = Integer.MIN_VALUE;
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 5L;
        long sign = 1;
        long expected = 5L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Long.MIN_VALUE;
        sign = 1;
        expected = Long.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = -1;
        expected = -5L;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        try {
            sign = -1;
            magnitude = Long.MIN_VALUE;
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCheckFiniteNullDouble() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaNDouble() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfiniteDouble() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckFiniteNullDoubleArray() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaNDoubleArray() {
        double[] values = {1.2, Double.NaN, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfiniteDoubleArray() {
        double[] values = {1.2, Double.POSITIVE_INFINITY, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullNullObject() {
        try {
            MathUtils.checkNotNull(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }
}