package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 123.45;
        int expected = 974872321; // calculated using Java's Double.hashCode()
        int actual = MathUtils.hash(value);
        assertEquals("Wrong hash code for double value: " + value, expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expected = -1089195671; // calculated using Java's Arrays.hashCode()
        int actual = MathUtils.hash(values);
        assertEquals("Wrong hash code for double array", expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.1415; // radians
        double center = 0.0;
        double expected = 0.7854; // radians (PI/4)
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals("Wrong normalized angle value", expected, actual, 1e-12);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithInvalidAngle() {
        double a = Double.POSITIVE_INFINITY;
        double center = 0.0;
        MathUtils.normalizeAngle(a, center);
    }

    @Test
    public void testReduce() {
        double a = 1.5 * MathUtils.TWO_PI;
        double period = 2.0 * MathUtils.TWO_PI;
        double offset = MathUtils.TWO_PI;
        double expected = 0.5 * MathUtils.TWO_PI;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals("Wrong reduced angle value", expected, actual, 1e-12);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Wrong copied sign byte value", expected, actual);
    }

    @Test
    public void testCopySignByte_NegativeSign() {
        byte magnitude = 5;
        byte sign = -1;
        byte expected = -5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Wrong copied sign byte value with negative sign", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByte_Overflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 32767;
        short sign = 1;
        short expected = 32767;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Wrong copied sign short value", expected, actual);
    }

    @Test
    public void testCopySignShort_NegativeSign() {
        short magnitude = 32767;
        short sign = -1;
        short expected = -32767;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Wrong copied sign short value with negative sign", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShort_Overflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = Integer.MAX_VALUE;
        int sign = 1;
        int expected = Integer.MAX_VALUE;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Wrong copied sign int value", expected, actual);
    }

    @Test
    public void testCopySignInt_NegativeSign() {
        int magnitude = Integer.MAX_VALUE;
        int sign = -1;
        int expected = Integer.MIN_VALUE;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Wrong copied sign int value with negative sign", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignInt_Overflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = Long.MAX_VALUE;
        long sign = 1;
        long expected = Long.MAX_VALUE;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Wrong copied sign long value", expected, actual);
    }

    @Test
    public void testCopySignLong_NegativeSign() {
        long magnitude = Long.MAX_VALUE;
        long sign = -1;
        long expected = Long.MIN_VALUE;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Wrong copied sign long value with negative sign", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLong_Overflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDouble() {
        double x = 123.45;
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
        double[] values = {1.1, 2.2, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {1.1, 2.2, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {1.1, 2.2, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

    @Test
    public void testCheckNotNullObjectPattern() {
        Object o = new Object();
        String pattern = LocalizedFormats.OBJECT_NULL;
        MathUtils.checkNotNull(o, pattern);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectPatternNull() {
        Object o = null;
        String pattern = LocalizedFormats.OBJECT_NULL;
        MathUtils.checkNotNull(o, pattern);
    }
}