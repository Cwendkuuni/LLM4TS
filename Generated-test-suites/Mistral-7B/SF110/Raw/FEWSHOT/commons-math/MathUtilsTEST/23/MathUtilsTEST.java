package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(123, MathUtils.hash(123.0));
        assertEquals(-789345, MathUtils.hash(-789345.0));
        assertEquals(Double.hashCode(123.0), MathUtils.hash(123.0));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {123.0, 456.0, 789.0};
        assertEquals(Arrays.hashCode(values), MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.0, MathUtils.normalizeAngle(Math.PI / 2.0, 0.0), 0.001);
        assertEquals(4.71238898038469, MathUtils.normalizeAngle(-3.141592653589793, MathUtils.TWO_PI), 0.001);
        assertEquals(5.0, MathUtils.normalizeAngle(MathUtils.TWO_PI + 0.1, MathUtils.TWO_PI), 0.001);
    }

    @Test
    public void testReduce() {
        assertEquals(1.2, MathUtils.reduce(1.5, 3.0, 1.0), 0.001);
        assertEquals(3.14, MathUtils.reduce(3.14, 6.283185307179586, 0.0), 0.001);
        assertEquals(0.0, MathUtils.reduce(0.1, 3.0, 3.0), 0.001);
        assertEquals(Math.PI, MathUtils.reduce(Math.PI * 2.0, 6.283185307179586, 3.141592653589793), 0.001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositive() {
        byte sign = 1;
        byte magnitude = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignBytePositiveAndPositive() {
        byte sign = 1;
        byte magnitude = 1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignBytePositiveAndNegative() {
        byte sign = -1;
        byte magnitude = 1;
        assertEquals((byte) -magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndNegative() {
        byte sign = 1;
        byte magnitude = -128;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShortPositiveAndPositive() {
        short sign = 1;
        short magnitude = 1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShortPositiveAndNegative() {
        short sign = -1;
        short magnitude = 1;
        assertEquals((short) -magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShortNegativeAndPositive() {
        short sign = 1;
        short magnitude = -1;
        assertEquals((short) -magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeAndNegative() {
        short sign = 1;
        short magnitude = -32768;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignIntPositiveAndPositive() {
        int sign = 1;
        int magnitude = 1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignIntPositiveAndNegative() {
        int sign = -1;
        int magnitude = 1;
        assertEquals(-magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignIntNegativeAndPositive() {
        int sign = 1;
        int magnitude = -1;
        assertEquals(-magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeAndNegative() {
        int sign = 1;
        int magnitude = Integer.MIN_VALUE;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongPositiveAndPositive() {
        long sign = 1;
        long magnitude = 1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignLongPositiveAndNegative() {
        long sign = -1;
        long magnitude = 1;
        assertEquals(-magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignLongNegativeAndPositive() {
        long sign = 1;
        long magnitude = -1;
        assertEquals(-magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeAndNegative() {
        long sign = 1;
        long magnitude = Long.MIN_VALUE;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);

        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(Double.MAX_VALUE);
        MathUtils.checkFinite(Double.MIN_VALUE);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArray() {
        double[] values = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN};
        MathUtils.checkFinite(values);
    }
}