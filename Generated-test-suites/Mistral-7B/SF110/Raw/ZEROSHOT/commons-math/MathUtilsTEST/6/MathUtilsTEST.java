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
        int expected = Double.hashCode(value);
        int result = MathUtils.hash(value);
        assertEquals(expected, result);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.46, 3.78};
        int expected = Arrays.hashCode(values);
        int result = MathUtils.hash(values);
        assertEquals(expected, result);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI * 3.5;
        double center = Math.PI;
        double expected = angle - 6.283185307179586 * FastMath.floor((angle + 3.141592653589793 - center) / 6.283185307179586);
        double result = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, result, 1e-14);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceInvalidArgument() {
        double a = Double.NaN;
        double period = 2.0;
        double offset = 0.0;
        MathUtils.reduce(a, period, offset);
    }

    @Test
    public void testReduce() {
        double a = 1.5 * Math.PI;
        double period = 2.0 * Math.PI;
        double offset = Math.PI / 2.0;
        double expected = a - period * FastMath.floor((a - offset) / period) - offset;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expected, result, 1e-14);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositive() {
        byte magnitude = -1;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignByteValid() {
        byte magnitude = 1;
        byte sign = 1;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = Byte.MIN_VALUE;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShortValid() {
        short magnitude = 1;
        short sign = 1;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = Short.MIN_VALUE;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignIntValid() {
        int magnitude = 1;
        int sign = 1;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongValid() {
        long magnitude = 1;
        long sign = 1;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNull() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInvalid() {
        double[] values = {Double.NaN, 1.23, 3.14};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, LocalizedFormats.ANY_OBJECT);
    }

    @Test
    public void testCheckNotNullValidObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}