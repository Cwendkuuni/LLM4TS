package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 12345;
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 1234567;
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithNaN() {
        MathUtils.normalizeAngle(Double.NaN, 0.0);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI;
        double expected = Math.PI - Math.floor(Math.PI / (2 * Math.PI)) * 2 * Math.PI;
        double actual = MathUtils.normalizeAngle(angle, 0.0);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testReduce() {
        double a = 2 * Math.PI;
        double period = Math.PI;
        double offset = Math.PI / 2;
        double expected = a - Math.PI * 2;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -1;
        sign = 1;
        expected = -1;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 1;
        sign = -1;
        expected = -1;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -128;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // expected
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
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // expected
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
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 5;
        long sign = 1;
        long expected = 5;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Long.MIN_VALUE;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayWithNaN() {
        double[] values = {1.2, Double.NaN, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, "Object should not be null");
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}