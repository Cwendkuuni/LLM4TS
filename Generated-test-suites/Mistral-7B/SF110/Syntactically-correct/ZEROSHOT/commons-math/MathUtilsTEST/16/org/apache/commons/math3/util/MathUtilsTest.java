package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import java.util.Arrays;

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
        double[] values = {1.23, 2.46, 3.6};
        int expected = Arrays.hashCode(values);
        int result = MathUtils.hash(values);
        assertEquals(expected, result);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithNaN() {
        MathUtils.normalizeAngle(Double.NaN, 0.0);
    }

    @Test
    public void testNormalizeAngle() {
        double result = MathUtils.normalizeAngle(3.14, 0.0);
        assertEquals(0.14, result, 1e-12);

        result = MathUtils.normalizeAngle(-3.14, 0.0);
        assertEquals(-0.14, result, 1e-12);

        result = MathUtils.normalizeAngle(6.28, 0.0);
        assertEquals(0.28, result, 1e-12);

        result = MathUtils.normalizeAngle(6.283185307179586, 0.0);
        assertEquals(0.0, result, 1e-14);
    }

    @Test
    public void testReduce() {
        double result = MathUtils.reduce(0.5, MathUtils.TWO_PI, 0.0);
        assertEquals(0.5, result, 1e-12);

        result = MathUtils.reduce(1.5, MathUtils.TWO_PI, 0.0);
        assertEquals(1.5, result, 1e-12);

        result = MathUtils.reduce(1.5, MathUtils.TWO_PI, MathUtils.TWO_PI / 2.0);
        assertEquals(0.5, result, 1e-12);

        result = MathUtils.reduce(-1.5, MathUtils.TWO_PI, 0.0);
        assertEquals(-1.5, result, 1e-12);

        result = MathUtils.reduce(-1.5, MathUtils.TWO_PI, MathUtils.TWO_PI / 2.0);
        assertEquals(-0.5, result, 1e-12);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeZeroAndPositive() {
        MathUtils.copySign((byte)0, (byte)1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeNumAndNegative() {
        MathUtils.copySign((byte)-1, (byte)-1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeNumAndPositive() {
        MathUtils.copySign((byte)-1, (byte)1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignBytePositiveNumAndNegative() {
        MathUtils.copySign((byte)1, (byte)-1);
    }

    @Test
    public void testCopySignByte() {
        byte result = MathUtils.copySign((byte)1, (byte)1);
        assertEquals((byte)1, result);

        result = MathUtils.copySign((byte)-1, (byte)1);
        assertEquals((byte)-1, result);

        result = MathUtils.copySign((byte)-1, (byte)-1);
        assertEquals((byte)1, result);

        result = MathUtils.copySign((byte)1, (byte)-1);
        assertEquals((byte)-1, result);
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);

        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(-1.0);
        MathUtils.checkFinite(123.45);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayWithNaN() {
        double[] values = {1.23, Double.NaN, 3.6};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.23, 2.46, 3.6};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        MathUtils.checkNotNull(new Object());
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        MathUtils.checkNotNull(null);
    }

    @Test
    public void testCheckNotNullString() {
        MathUtils.checkNotNull("Hello World");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullStringNull() {
        MathUtils.checkNotNull(null);
    }
}