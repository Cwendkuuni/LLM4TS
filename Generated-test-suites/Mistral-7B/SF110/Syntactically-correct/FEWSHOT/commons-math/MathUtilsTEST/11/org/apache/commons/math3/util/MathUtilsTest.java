package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-1073741828, MathUtils.hash(Double.MIN_VALUE));
        assertEquals(0, MathUtils.hash(0.0));
        assertEquals(1305893251, MathUtils.hash(1.0));
        assertEquals(538870932, MathUtils.hash(Double.MAX_VALUE));
    }

    @Test
    public void testHashDoubleArray() {
        double[] array = {-1.0, 0.0, 1.0};
        assertEquals(-1351826833, MathUtils.hash(array));
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleInvalidArguments() {
        MathUtils.normalizeAngle(Double.POSITIVE_INFINITY, 0.0);
        MathUtils.normalizeAngle(-Double.POSITIVE_INFINITY, 0.0);
        MathUtils.normalizeAngle(Double.NaN, 0.0);
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.2, MathUtils.normalizeAngle(1.2, 0.0), 0.0001);
        assertEquals(3.5, MathUtils.normalizeAngle(3.5, 1.0), 0.0001);
        assertEquals(-3.5, MathUtils.normalizeAngle(-3.5, 1.0), 0.0001);
        assertEquals(4.5, MathUtils.normalizeAngle(4.5, 6.283185307179586), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReduceInvalidArguments() {
        MathUtils.reduce(Double.NaN, 1.0, 0.0);
        MathUtils.reduce(1.0, 0.0, 0.0);
        MathUtils.reduce(-1.0, 1.0, 0.0);
    }

    @Test
    public void testReduce() {
        assertEquals(0.5, MathUtils.reduce(1.5, 2.0, 0.0), 0.0001);
        assertEquals(-0.5, MathUtils.reduce(-1.5, 2.0, 0.0), 0.0001);
        assertEquals(1.0, MathUtils.reduce(1.5, 1.5, 0.0), 0.0001);
        assertEquals(-1.0, MathUtils.reduce(-1.5, 1.5, 0.0), 0.0001);
        assertEquals(2.5, MathUtils.reduce(3.5, 1.0, 1.0), 0.0001);
        assertEquals(-2.5, MathUtils.reduce(-3.5, 1.0, 1.0), 0.0001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignInvalidArguments() {
        byte magnitude = 128;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);

        short magnitude1 = 32768;
        short sign1 = -1;
        MathUtils.copySign(magnitude1, sign1);

        int magnitude2 = Integer.MIN_VALUE;
        int sign2 = 0;
        MathUtils.copySign(magnitude2, sign2);

        long magnitude3 = Long.MIN_VALUE;
        long sign3 = -1;
        MathUtils.copySign(magnitude3, sign3);

        byte magnitude4 = 1;
        byte sign4 = 128;
        MathUtils.copySign(magnitude4, sign4);

        short magnitude5 = 1;
        short sign5 = 32768;
        MathUtils.copySign(magnitude5, sign5);

        int magnitude6 = 1;
        int sign6 = Integer.MAX_VALUE;
        MathUtils.copySign(magnitude6, sign6);

        long magnitude7 = 1;
        long sign7 = Long.MAX_VALUE;
        MathUtils.copySign(magnitude7, sign7);

        byte magnitude8 = 1;
        byte sign8 = 129;
        MathUtils.copySign(magnitude8, sign8);

        short magnitude9 = 1;
        short sign9 = 32769;
        MathUtils.copySign(magnitude9, sign9);

        int magnitude10 = 1;
        int sign10 = 2147483648;
        MathUtils.copySign(magnitude10, sign10);

        long magnitude11 = 1;
        long sign11 = 9223372036854775808L;
        MathUtils.copySign(magnitude11, sign11);
    }

    @Test
    public void testCheckFinite() {
        MathUtils.checkFinite(Double.MIN_VALUE);
        MathUtils.checkFinite(Double.MAX_VALUE);
        MathUtils.checkFinite(Double.NaN);
        MathUtils.checkFinite(new double[0]);
        MathUtils.checkFinite(new double[] {Double.MIN_VALUE, Double.MAX_VALUE, Double.NaN});
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullArgument() {
        MathUtils.checkNotNull(null, "Argument can't be null");
    }

    @Test
    public void testCheckNotNull() {
        MathUtils.checkNotNull(new Object());
    }
}