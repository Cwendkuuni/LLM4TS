package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = Double.doubleToLongBits(value) ^ 0xabcdef12; // arbitrary non-zero value
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = new double[] { 1.23, 2.46, 3.69 };
        int expected = Arrays.hashCode(values);
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.141592653589793; // pi
        double center = 0.0;
        double expected = 1.5707963267948966; // pi/2
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 1e-12);

        a = -3.141592653589793; // -pi
        expected = -1.5707963267948966; // -pi/2
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 1e-12);

        a = 4.71238898038469; // 2.5 * pi
        expected = 0.7853981633974483; // pi/4
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 1e-12);

        a = -4.71238898038469; // -2.5 * pi
        expected = -0.7853981633974483; // -pi/4
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 1e-12);
    }

    @Test
    public void testReduce() {
        double a = 1.23;
        double period = 2.1;
        double offset = 0.5;
        double expected = 0.18;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 1e-12);

        a = 3.2;
        period = 1.7;
        offset = 0.2;
        expected = 1.2;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 1e-12);

        a = -4.5;
        period = 3.14;
        offset = 1.57;
        expected = -1.11; // 3 * pi/2 - pi/2 = 5 * pi/4
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 1e-12);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteSignNegative() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteMagnitudeMinusOne() {
        byte magnitude = -1;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -1;
        sign = -1;
        expected = -1;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 5000;
        short sign = 1;
        short expected = 5000;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -32768;
        sign = -1;
        expected = -32768;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = Integer.MAX_VALUE; // 2^31 - 1
        int sign = 1;
        int expected = Integer.MAX_VALUE;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Integer.MIN_VALUE; // -2^31
        sign = 1;
        expected = -Integer.MIN_VALUE; // -(2^31)
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = Long.MAX_VALUE; // 2^63 - 1
        long sign = 1;
        long expected = Long.MAX_VALUE;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Long.MIN_VALUE; // -2^63
        sign = 1;
        expected = -Long.MIN_VALUE; // -(2^63)
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = new double[] { Double.NaN };
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = new double[] { Double.POSITIVE_INFINITY };
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNull() {
        String str = "hello";
        MathUtils.checkNotNull(str, null, new Object[0]);

        byte[] bytes = new byte[10];
        MathUtils.checkNotNull(bytes, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullString() {
        String str = null;
        MathUtils.checkNotNull(str, null, new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object obj = null;
        MathUtils.checkNotNull(obj, null);
    }
}