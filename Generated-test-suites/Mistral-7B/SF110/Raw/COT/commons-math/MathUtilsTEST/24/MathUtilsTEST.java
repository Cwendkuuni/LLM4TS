package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 1312981175; // just an example value
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNegativeInfinite() {
        MathUtils.checkFinite(-Double.POSITIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test
    public void testNormalizeAngleSimpleCase() {
        double a = 1.5;
        double center = 2.5;
        double expected = -0.7853981633974483;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000000000000001);
    }

    @Test
    public void testNormalizeAngleEdgeCases() {
        double a = 0.0;
        double center = 2.5;
        double expected = -2.356194490192345;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000000000000001);

        a = 6.283185307179586; // 2 * PI
        expected = 0.0;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.000000000000001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleNaN() {
        double a = Double.NaN;
        double center = 2.5;
        MathUtils.normalizeAngle(a, center);
    }

    @Test
    public void testReduceSimpleCase() {
        double a = 2.5;
        double period = 3.141592653589793;
        double offset = 1.5707963267948966;
        double expected = 0.283185307179586;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000000000000001);
    }

    @Test
    public void testReducePeriodZero() {
        double a = 2.5;
        double period = 0.0;
        double offset = 1.5707963267948966;
        double expected = 2.5; // a itself
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.000000000000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReduceNegativePeriod() {
        double a = 2.5;
        double period = -3.141592653589793;
        double offset = 1.5707963267948966;
        MathUtils.reduce(a, period, offset);
    }

    @Test
    public void testCopySignByte() {
        byte sign = 1;
        byte magnitude = 5;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = 1;
        magnitude = -128;
        expected = -128;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        sign = -1;
        magnitude = 5;
        expected = -5;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte sign = 1;
        byte magnitude = -129;
        MathUtils.copySign(magnitude, sign);
    }

    // Similar test cases for copySign short, int, and long methods

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNullNotNullObject() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testCheckNotNullArrayNull() {
        double[] array = null;
        MathUtils.checkFinite(array);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCheckNotNullArrayInvalidIndex() {
        double[] array = new double[10];
        MathUtils.checkNotNull(array, LocalizedFormats.ARRAY_ELEMENT, -1);
    }

}