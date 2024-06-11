package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    // Test for hash(final double value) method
    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedHashCode = 123; // Any valid hash code
        int actualHashCode = MathUtils.hash(value);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashDoubleWithNegativeValue() {
        double value = -1.2;
        MathUtils.hash(value); // The method should throw an IllegalArgumentException
    }

    // Test for hash(final double[] value) method
    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expectedHashCode = 12345; // Any valid hash code
        int actualHashCode = MathUtils.hash(values);
        assertEquals(expectedHashCode, actualHashCode);
    }

    // Test for normalizeAngle(final double a, final double center) method
    @Test
    public void testNormalizeAngle() {
        double a = 3.141592653589793;
        double center = 0.0;
        double expectedNormalizedAngle = -3.141592653589793;
        double actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedNormalizedAngle, actualNormalizedAngle, 0.0000001);
    }

    @Test
    public void testNormalizeAngleWithPositiveAngleAndZeroCenter() {
        double a = 0.0;
        double center = 0.0;
        double expectedNormalizedAngle = 0.0;
        double actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedNormalizedAngle, actualNormalizedAngle, 0.0000001);
    }

    @Test
    public void testNormalizeAngleWithNegativeAngleAndZeroCenter() {
        double a = -3.141592653589793;
        double center = 0.0;
        double expectedNormalizedAngle = 3.141592653589793;
        double actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedNormalizedAngle, actualNormalizedAngle, 0.0000001);
    }

    @Test
    public void testNormalizeAngleWithLargeAngleAndZeroCenter() {
        double a = 6.283185307179586 * 2.0; // 2 * PI
        double center = 0.0;
        double expectedNormalizedAngle = a % (2 * Math.PI);
        double actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedNormalizedAngle, actualNormalizedAngle, 0.0000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeAngleWithNaN() {
        double a = Double.NaN;
        double center = 0.0;
        MathUtils.normalizeAngle(a, center); // The method should throw an IllegalArgumentException
    }

    // Test for reduce(final double a, final double period, final double offset) method
    @Test
    public void testReduce() {
        double a = 1.5;
        double period = 2.0;
        double offset = 0.5;
        double expectedReducedValue = 0.5;
        double actualReducedValue = MathUtils.reduce(a, period, offset);
        assertEquals(expectedReducedValue, actualReducedValue, 0.0000001);
    }

    @Test
    public void testReduceWithNegativeOffset() {
        double a = 1.5;
        double period = 2.0;
        double offset = -0.5;
        double expectedReducedValue = 2.5;
        double actualReducedValue = MathUtils.reduce(a, period, offset);
        assertEquals(expectedReducedValue, actualReducedValue, 0.0000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReduceWithNegativePeriod() {
        double a = 1.5;
        double period = -2.0;
        double offset = 0.0;
        MathUtils.reduce(a, period, offset); // The method should throw an IllegalArgumentException
    }

    // Test for copySign(final byte magnitude, final byte sign) method
    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expectedResult = 5;
        byte actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCopySignByteWithNegativeSign() {
        byte magnitude = 5;
        byte sign = -1;
        byte expectedResult = -5;
        byte actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteWithOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // The method should throw a MathArithmeticException
    }

    // Test for copySign(final short magnitude, final short sign) method
    @Test
    public void testCopySignShort() {
        short magnitude = 5;
        short sign = 1;
        short expectedResult = 5;
        short actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCopySignShortWithNegativeSign() {
        short magnitude = 5;
        short sign = -1;
        short expectedResult = -5;
        short actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortWithOverflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign); // The method should throw a MathArithmeticException
    }

    // Test for copySign(final int magnitude, final int sign) method
    @Test
    public void testCopySignInt() {
        int magnitude = 5;
        int sign = 1;
        int expectedResult = 5;
        int actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCopySignIntWithNegativeSign() {
        int magnitude = 5;
        int sign = -1;
        int expectedResult = -5;
        int actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntWithOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign); // The method should throw a MathArithmeticException
    }

    // Test for copySign(final long magnitude, final long sign) method
    @Test
    public void testCopySignLong() {
        long magnitude = 5L;
        long sign = 1;
        long expectedResult = 5L;
        long actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCopySignLongWithNegativeSign() {
        long magnitude = 5L;
        long sign = -1;
        long expectedResult = -5L;
        long actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongWithOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign); // The method should throw a MathArithmeticException
    }

    // Test for checkFinite(final double x) method
    @Test
    public void testCheckFiniteDouble() {
        double x = 1.2;
        MathUtils.checkFinite(x);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double x = Double.NaN;
        MathUtils.checkFinite(x); // The method should throw a NotFiniteNumberException
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinite() {
        double x = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(x); // The method should throw a NotFiniteNumberException
    }

    // Test for checkFinite(final double[] val) method
    @Test
    public void testCheckFiniteDoubleArrayNull() {
        double[] val = null;
        MathUtils.checkFinite(val); // The method should not throw an exception for null arrays
    }

    @Test
    public void testCheckFiniteDoubleArrayFinite() {
        double[] val = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(val);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] val = {1.2, Double.NaN, 3.4};
        MathUtils.checkFinite(val); // The method should throw a NotFiniteNumberException
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] val = {1.2, Double.POSITIVE_INFINITY, 3.4};
        MathUtils.checkFinite(val); // The method should throw a NotFiniteNumberException
    }
}