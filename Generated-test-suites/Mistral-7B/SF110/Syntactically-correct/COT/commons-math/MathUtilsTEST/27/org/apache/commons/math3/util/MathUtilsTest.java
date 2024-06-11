package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.0;
        int expected = 1729013537; // Hashcode for 1.0
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.0, 2.0, 3.0};
        int expected = 123456789; // Arbitrary hashcode for an array of length 3
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 3.14;
        double center = 0.0;
        double expected = -3.14;
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.001);

        angle = 6.283;
        expected = 0.0;
        actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeAngleInvalidCenter() {
        double angle = 0.0;
        double center = Double.NaN;
        MathUtils.normalizeAngle(angle, center);
    }

    @Test
    public void testReduce() {
        double angle = 3.14;
        double period = 6.283;
        double offset = 0.0;
        double expected = 1.14;
        double actual = MathUtils.reduce(angle, period, offset);
        assertEquals(expected, actual, 0.001);

        angle = 4.712;
        period = 6.283;
        offset = 3.14;
        expected = -0.712;
        actual = MathUtils.reduce(angle, period, offset);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReduceInvalidOffset() {
        double angle = 0.0;
        double period = 6.283;
        double offset = Double.NaN;
        MathUtils.reduce(angle, period, offset);
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
        expected = 128;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 127;
        sign = -1;
        expected = -128;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = 127;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);

        magnitude = 127;
        sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 32767;
        short sign = 1;
        short expected = 32767;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -32768;
        sign = 1;
        expected = -32768;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = -1;
        expected = 32768;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expecte, actual);

        magnitude = 32766;
        sign = -1;
        expected = -32766;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = 32767;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);

        magnitude = 32767;
        sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = Integer.MAX_VALUE;
        int sign = 1;
        int expected = Integer.MAX_VALUE;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        expected = Integer.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = -1;
        expected = -Integer.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Integer.MAX_VALUE - 1;
        sign = -1;
        expected = -Integer.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MAX_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);

        magnitude = Integer.MAX_VALUE;
        sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = Long.MAX_VALUE;
        long sign = 1;
        long expected = Long.MAX_VALUE;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Long.MIN_VALUE;
        sign = 1;
        expected = Long.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = -1;
        expected = -Long.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Long.MAX_VALUE - 1;
        sign = -1;
        expected = -Long.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MAX_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);

        magnitude = Long.MAX_VALUE;
        sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.0;
        MathUtils.checkFinite(value);

        value = Double.NaN;
        try {
            MathUtils.checkFinite(value);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected
        }
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(values);

        values[0] = Double.NaN;
        try {
            MathUtils.checkFinite(values);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object o = null;
        MathUtils.checkNotNull(o, "The object should not be null", new Object[0]);
        fail("Expected NullArgumentException");
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o, "The object should not be null");
    }
}