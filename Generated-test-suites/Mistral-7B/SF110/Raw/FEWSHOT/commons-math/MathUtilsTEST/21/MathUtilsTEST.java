package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 123.45;
        int expectedHashCode = 105823377; // calculated using Java's Double.hashCode(value)
        assertEquals(expectedHashCode, MathUtils.hash(value));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expectedHashCode = 2130791295; // calculated using Java's Arrays.hashCode(values)
        assertEquals(expectedHashCode, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI * 1.5;
        double center = 0.0;
        double expectedResult = Math.PI / 6.0;
        assertEquals(expectedResult, MathUtils.normalizeAngle(angle, center), 1e-14);
    }

    @Test
    public void testReduce() {
        double value = 2.5 * Math.PI;
        double period = 2.0 * Math.PI;
        double offset = Math.PI / 2.0;
        double expectedResult = Math.PI / 4.0;
        assertEquals(expectedResult, MathUtils.reduce(value, period, offset), 1e-14);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositive() {
        byte magnitude = -1;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndNegative() {
        byte magnitude = -1;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignBytePositive() {
        byte magnitude = 5;
        byte sign = 1;
        byte expectedResult = 5;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, result);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNull() {
        MathUtils.checkFinite(null);
    }

    @Test
    public void testCheckFiniteArrayEmpty() {
        MathUtils.checkFinite(new double[0]);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayInvalid() {
        double[] values = {Double.NaN, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteArrayValid() {
        double[] values = {1.1, 2.2, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullPattern() {
        MathUtils.checkNotNull(null, "", new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObjectAndPattern() {
        MathUtils.checkNotNull(null, "{}", new Object[0]);
    }

    @Test
    public void testCheckNotNullValidObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullInvalidArgument() {
        MathUtils.checkNotNull(null, "{}", new Object[]{1});
    }
}