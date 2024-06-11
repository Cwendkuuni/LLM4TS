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
        int expected = 123; // any valid hashcode
        int actual = MathUtils.hash(value);
        assertEquals("hash(double) should return consistent value for same input", expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 123; // any valid hashcode
        int actual = MathUtils.hash(values);
        assertEquals("hash(double[]) should return consistent value for same input", expected, actual);
    }

    @Test(expected = NullPointerException.class)
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
        assertEquals("normalizeAngle(a, center) should return correct value", expected, actual, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReduceNegativePeriod() {
        double a = 0.0;
        double period = -1.0;
        double offset = 0.0;
        MathUtils.reduce(a, period, offset);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReduceNegativeOffset() {
        double a = 0.0;
        double period = 1.0;
        double offset = -1.0;
        MathUtils.reduce(a, period, offset);
    }

    @Test
    public void testCopySignByte() {
        byte sign = 1;
        byte magnitude = 50;
        byte expected = 50;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(byte, byte) should return correct value for positive numbers", expected, actual);

        sign = -1;
        magnitude = 50;
        expected = -50;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(byte, byte) should return correct value for negative numbers", expected, actual);

        sign = 1;
        magnitude = Byte.MIN_VALUE;
        try {
            actual = MathUtils.copySign(magnitude, sign);
            fail("copySign(byte, byte) should throw MathArithmeticException for overflow");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignShort() {
        short sign = 1;
        short magnitude = 32767;
        short expected = 32767;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(short, short) should return correct value for positive numbers", expected, actual);

        sign = -1;
        magnitude = 32767;
        expected = -32767;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(short, short) should return correct value for negative numbers", expected, actual);

        sign = 1;
        magnitude = Short.MIN_VALUE;
        try {
            actual = MathUtils.copySign(magnitude, sign);
            fail("copySign(short, short) should throw MathArithmeticException for overflow");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignInt() {
        int sign = 1;
        int magnitude = Integer.MAX_VALUE;
        int expected = Integer.MAX_VALUE;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(int, int) should return correct value for positive numbers", expected, actual);

        sign = -1;
        magnitude = Integer.MAX_VALUE;
        expected = Integer.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(int, int) should return correct value for negative numbers", expected, actual);

        sign = 1;
        magnitude = Integer.MIN_VALUE;
        try {
            actual = MathUtils.copySign(magnitude, sign);
            fail("copySign(int, int) should throw MathArithmeticException for overflow");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignLong() {
        long sign = 1;
        long magnitude = Long.MAX_VALUE;
        long expected = Long.MAX_VALUE;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(long, long) should return correct value for positive numbers", expected, actual);

        sign = -1;
        magnitude = Long.MAX_VALUE;
        expected = Long.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(long, long) should return correct value for negative numbers", expected, actual);

        sign = 1;
        magnitude = Long.MIN_VALUE;
        try {
            actual = MathUtils.copySign(magnitude, sign);
            fail("copySign(long, long) should throw MathArithmeticException for overflow");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckFiniteNullValue() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinite() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNull() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullPattern() {
        MathUtils.checkNotNull(null, "should not be null", new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObjectAndPattern() {
        MathUtils.checkNotNull(null);
    }
}