package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 3.14;
        int expected = 1234567; // Any valid hash code as a placeholder
        int actual = MathUtils.hash(value);
        assertEquals("Hash code for double should be consistent", expected, actual);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteThrowsWithNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteThrowsWithInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNormalizeAngleZero() {
        double a = 0.0;
        double center = 0.0;
        double expected = 0.0;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals("Normalize angle should return the same angle when center is zero", expected, actual, 1e-12);
    }

    @Test
    public void testNormalizeAnglePiPlus() {
        double a = MathUtils.TWO_PI + 1.0;
        double center = 0.0;
        double expected = -5.24; // Approximated value
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals("Normalize angle should return the correct value for a > 2 * pi", expected, actual, 1e-12);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleNaN() {
        double a = Double.NaN;
        double center = 0.0;
        MathUtils.normalizeAngle(a, center);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleInfinite() {
        double a = Double.POSITIVE_INFINITY;
        double center = 0.0;
        MathUtils.normalizeAngle(a, center);
    }

    @Test
    public void testReduceSimple() {
        double a = 0.5;
        double period = 2.0;
        double offset = 1.0;
        double expected = 0.5;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals("Reduce method should return the correct value for a < period", expected, actual, 1e-12);
    }

    @Test
    public void testReducePeriodLessThanOne() {
        double a = 0.5;
        double period = 0.5;
        double offset = 0.0;
        double expected = 0.0;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals("Reduce method should return 0 when period is less than one", expected, actual, 1e-12);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositive() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndNegativeOverflow() {
        byte magnitude = -129;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.0, 2.0, 3.0};
        int expected = 1234567; // Any valid hash code as a placeholder
        int actual = MathUtils.hash(values);
        assertEquals("Hash code for double array should be consistent", expected, actual);
    }

    @Test
    public void testCheckFiniteArray() {
        double[] values = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayNaN() {
        double[] values = {1.0, Double.NaN, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, "Object should not be null");
    }

    @Test
    public void testCheckNotNullValidObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}