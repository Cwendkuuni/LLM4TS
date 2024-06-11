package org.junit.Test;

import static org.junit.Assert.*;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 12345; // Arbitrary value for testing
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 56789; // Arbitrary value for testing
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = Byte.MIN_VALUE;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // This should throw MathArithmeticException
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullInput() {
        MathUtils.checkNotNull(null, null);
    }

    @Test
    public void testCheckNotNullEmptyInput() {
        Object[] emptyInput = new Object[0];
        MathUtils.checkNotNull(emptyInput);
    }

    @Test
    public void testNormalizeAngleNegative() {
        double a = -3.14;
        double center = 0;
        double expected = -3.14 + 6.283185307179586; // Arbitrary value for testing
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual);
    }

    @Test
    public void testReducePeriodZero() {
        double a = 1.5;
        double period = 0;
        double offset = 0;
        double expected = a; // No reduction should be applied
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngleEdgeCase() {
        double a = 6.283185307179586;
        double center = 3.14;
        double expected = 0;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testReducePeriodEdgeCase() {
        double a = 1.5;
        double period = 0.1;
        double offset = -0.5;
        double expected = 0.6;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteException() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // This should throw MathArithmeticException
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
    public void testCheckFiniteDoubleArrayInvalidElement() {
        double[] values = {1.2, Double.POSITIVE_INFINITY, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNullInput() {
        MathUtils.checkNotNull(null, "Object should not be null");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectEmptyArrayInput() {
        Object[] emptyInput = new Object[0];
        MathUtils.checkNotNull(emptyInput, "Array should not be empty");
    }

}