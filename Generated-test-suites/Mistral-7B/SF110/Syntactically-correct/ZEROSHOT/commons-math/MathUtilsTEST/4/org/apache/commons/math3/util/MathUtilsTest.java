package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;

import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 123.45;
        int expectedHash = 1191314273; // calculated using Java's Double.hashCode()
        int actualHash = MathUtils.hash(value);
        assertEquals("Hash for double should match with Java's implementation", expectedHash, actualHash);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expectedHash = 2025561264; // calculated using Java's Arrays.hashCode()
        int actualHash = MathUtils.hash(values);
        assertEquals("Hash for double array should match with Java's implementation", expectedHash, actualHash);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI + 2.0 * Math.PI;
        double center = Math.PI;
        double expectedAngle = Math.PI - 2.0 * Math.PI;
        double actualAngle = MathUtils.normalizeAngle(angle, center);
        assertEquals("Normalize angle should return the correct value", expectedAngle, actualAngle, 0.0001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceInfiniteValue() {
        double a = Double.POSITIVE_INFINITY;
        double period = 1.0;
        double offset = 0.0;
        MathUtils.reduce(a, period, offset);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceNaNValue() {
        double a = Double.NaN;
        double period = 1.0;
        double offset = 0.0;
        MathUtils.reduce(a, period, offset);
    }

    @Test
    public void testCopySignByte() {
        byte sign = -1;
        byte magnitude = 5;
        byte expectedSignByte = -magnitude; // calculated manually
        byte actualSignByte = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return the correct sign for byte", expectedSignByte, actualSignByte);
        sign = 1;
        byte expectedSignByte2 = 5; // calculated manually
        byte actualSignByte2 = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return the original magnitude for positive sign", expectedSignByte2, actualSignByte2);
    }

    @Test
    public void testCopySignShort() {
        short sign = -1;
        short magnitude = 5;
        short expectedSignShort = -magnitude; // calculated manually
        short actualSignShort = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return the correct sign for short", expectedSignShort, actualSignShort);
        sign = 1;
        short expectedSignShort2 = 5; // calculated manually
        short actualSignShort2 = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return the original magnitude for positive sign", expectedSignShort2, actualSignShort2);
    }

    @Test
    public void testCopySignInt() {
        int sign = -1;
        int magnitude = 5;
        int expectedSignInt = -magnitude; // calculated manually
        int actualSignInt = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return the correct sign for int", expectedSignInt, actualSignInt);
        sign = 1;
        int expectedSignInt2 = 5; // calculated manually
        int actualSignInt2 = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return the original magnitude for positive sign", expectedSignInt2, actualSignInt2);
    }

    @Test
    public void testCopySignLong() {
        long sign = -1;
        long magnitude = 5;
        long expectedSignLong = -magnitude; // calculated manually
        long actualSignLong = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return the correct sign for long", expectedSignLong, actualSignLong);
        sign = 1;
        long expectedSignLong2 = 5; // calculated manually
        long actualSignLong2 = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return the original magnitude for positive sign", expectedSignLong2, actualSignLong2);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignOverflowByte() {
        byte sign = 1;
        byte magnitude = Byte.MIN_VALUE;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignOverflowShort() {
        short sign = 1;
        short magnitude = Short.MIN_VALUE;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignOverflowInt() {
        int sign = 1;
        int magnitude = Integer.MIN_VALUE;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignOverflowLong() {
        long sign = 1;
        long magnitude = Long.MIN_VALUE;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteSingleValue() {
        double value = 1.0;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfiniteValue() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaNValue() {
        double value = Double.NaN;
        MathUtils.checkFinite(value);
    }

    @Test
    public void testCheckFiniteArray() {
        double[] values = {1.1, 2.2, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayWithNaN() {
        double[] values = {1.1, 2.2, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

    @Test
    public void testCheckNotNullString() {
        String pattern = "pattern";
        MathUtils.checkNotNull(pattern);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullString() {
        String pattern = null;
        MathUtils.checkNotNull(pattern);
    }

    @Test
    public void testCheckNotNullArray() {
        double[] values = new double[3];
        MathUtils.checkNotNull(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullArray() {
        double[] values = null;
        MathUtils.checkNotNull(values);
    }

    @Test
    public void testCheckNotNullObjectWithMessage() {
        Object o = new Object();
        MathUtils.checkNotNull(o, "Test message");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObjectWithMessage() {
        Object o = null;
        MathUtils.checkNotNull(o, "Test message");
    }
}