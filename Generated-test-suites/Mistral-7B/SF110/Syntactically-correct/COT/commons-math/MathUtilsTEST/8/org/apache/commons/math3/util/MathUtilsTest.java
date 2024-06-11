package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 12345; // Any valid hash code can be used here for testing
        int actual = MathUtils.hash(value);
        assertEquals("Incorrect hash code for double.", expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 1234567; // Any valid hash code can be used here for testing
        int actual = MathUtils.hash(values);
        assertEquals("Incorrect hash code for double array.", expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = Math.PI;
        double center = 0.0;
        double expected = -Math.PI;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals("Incorrectly normalized angle.", expected, actual, 0.001);

        a = 3 * Math.PI;
        expected = Math.PI;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals("Incorrectly normalized angle.", expected, actual, 0.001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithNaN() {
        double a = Double.NaN;
        double center = 0.0;
        MathUtils.normalizeAngle(a, center); // Should throw an exception
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithInfinite() {
        double a = Double.POSITIVE_INFINITY;
        double center = 0.0;
        MathUtils.normalizeAngle(a, center); // Should throw an exception
    }

    @Test
    public void testReduce() {
        double a = Math.PI;
        double period = 2 * Math.PI;
        double offset = Math.PI;
        double expected = -Math.PI;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals("Incorrectly reduced angle.", expected, actual, 0.001);

        period = 3 * Math.PI;
        offset = 0.0;
        expected = Math.PI;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals("Incorrectly reduced angle.", expected, actual, 0.001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceWithNaN() {
        double a = Double.NaN;
        double period = 2 * Math.PI;
        double offset = 0.0;
        MathUtils.reduce(a, period, offset); // Should throw an exception
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceWithInfinitePeriod() {
        double a = 0.0;
        double period = Double.POSITIVE_INFINITY;
        double offset = 0.0;
        MathUtils.reduce(a, period, offset); // Should throw an exception
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudeAndPositiveSign() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // Should throw an exception
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudeAndNegativeSign() {
        byte magnitude = -5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign); // Should throw an exception
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, (LocalizedFormats) null);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullPattern() {
        MathUtils.checkNotNull(new Object(), null, new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullArgument() {
        MathUtils.checkNotNull(null);
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.2;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value); // Should throw an exception
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinite() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value); // Should throw an exception
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {1.2, Double.NaN, 3.4};
        MathUtils.checkFinite(values); // Should throw an exception
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {1.2, 2.3, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values); // Should throw an exception
    }
}