package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = 413284376; // This value will depend on the JVM and the input value.
        int actual = MathUtils.hash(value);
        assertEquals("hash(1.23)", expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.718};
        int expected = 885843189; // This value will depend on the JVM and the input values.
        int actual = MathUtils.hash(values);
        assertEquals("hash([1.23, 2.718])", expected, actual);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithInvalidArgument() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.141592653589793;
        double center = 0.0;
        double expected = 1.141592653589793;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals("normalizeAngle(3.141592653589793, 0.0)", expected, actual, 1e-14);
    }

    @Test
    public void testReduce() {
        double a = 1.5707963267948966;
        double period = 6.283185307179586;
        double offset = 0.0;
        double expected = 1.1102510243251992;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(1.5707963267948966, 6.283185307179586, 0.0)", expected, actual, 1e-14);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudePositiveSign() {
        byte magnitude = -1;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignBytePositiveMagnitudePositiveSign() {
        byte magnitude = 1;
        byte sign = 1;
        byte expected = magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(1, 1)", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudeNegativeSign() {
        byte magnitude = -1;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignBytePositiveMagnitudeNegativeSign() {
        byte magnitude = 1;
        byte sign = -1;
        byte expected = (byte) -magnitude;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(1, -1)", expected, actual);
    }

    @Test
    public void testCopySignShortNegativeMagnitudePositiveSign() {
        short magnitude = -12332;
        short sign = 1;
        MathUtils.checkFinite(magnitude);
        short expected = magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-12332, 1)", expected, actual);
    }

    @Test
    public void testCopySignShortNegativeMagnitudeNegativeSign() {
        short magnitude = -12332;
        short sign = -1;
        MathUtils.checkFinite(magnitude);
        short expected = (short) -magnitude;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-12332, -1)", expected, actual);
    }

    @Test
    public void testCopySignIntNegativeMagnitudePositiveSign() {
        int magnitude = -12345;
        int sign = 1;
        MathUtils.checkFinite(magnitude);
        int expected = magnitude;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-12345, 1)", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeMagnitudeNegativeSign() {
        int magnitude = -12345;
        int sign = -1;
        MathUtils.checkFinite(magnitude);
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongNegativeMagnitudePositiveSign() {
        long magnitude = -1234567890123L;
        long sign = 1;
        MathUtils.checkFinite(magnitude);
        long expected = magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-1234567890123, 1)", expected, actual);
    }

    @Test
    public void testCopySignLongNegativeMagnitudeNegativeSign() {
        long magnitude = -1234567890123L;
        long sign = -1;
        MathUtils.checkFinite(magnitude);
        long expected = -magnitude;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-1234567890123, -1)", expected, actual);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNullElement() {
        double[] values = null;
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfiniteElement() {
        double[] values = {Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteDoubleArrayNoInvalidElements() {
        double[] values = {1.23, 2.718};
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
        MathUtils.checkNotNull(o, LocalizedFormats.NULL_OBJECT, new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectPattern() {
        Object o = null;
        MathUtils.checkNotNull(o, LocalizedFormats.NULL_ARGUMENT, new Object[]{"o"});
    }
}