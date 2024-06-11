package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedHash = 1234567; // use an arbitrary expected hash value for testing
        int actualHash = MathUtils.hash(value);
        assertEquals(expectedHash, actualHash);
    }

    @Test(expected = NullArgumentException.class)
    public void testHashNullDouble() {
        MathUtils.hash(null);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expectedHash = 1234567; // use an arbitrary expected hash value for testing
        int actualHash = MathUtils.hash(values);
        assertEquals(expectedHash, actualHash);
    }

    @Test(expected = NullArgumentException.class)
    public void testHashNullDoubleArray() {
        MathUtils.hash(null);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 3.14;
        double center = 0.0;
        double expectedResult = -3.14;
        double result = MathUtils.normalizeAngle(angle, center);
        assertEquals(expectedResult, result, 0.001);

        angle = 6.28;
        expectedResult = 0.0;
        result = MathUtils.normalizeAngle(angle, center);
        assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNormalizeAngleNullAngle() {
        MathUtils.normalizeAngle(null, 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testNormalizeAngleNullCenter() {
        double angle = 0.0;
        MathUtils.normalizeAngle(angle, null);
    }

    @Test
    public void testReduce() {
        double a = 2.5;
        double period = 3.14;
        double offset = 1.57;
        double expectedResult = 0.35;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals(expectedResult, result, 0.001);

        a = 6.28;
        period = 3.14;
        offset = 1.57;
        expectedResult = 1.21;
        result = MathUtils.reduce(a, period, offset);
        assertEquals(expectedResult, result, 0.001);

        a = -2.5;
        period = 3.14;
        offset = 1.57;
        expectedResult = 1.83;
        result = MathUtils.reduce(a, period, offset);
        assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testReduceNullA() {
        double period = 3.14;
        double offset = 1.57;
        MathUtils.reduce(null, period, offset);
    }

    @Test(expected = NullArgumentException.class)
    public void testReduceNullPeriod() {
        double a = 2.5;
        double offset = 1.57;
        MathUtils.reduce(a, null, offset);
    }

    @Test(expected = NullArgumentException.class)
    public void testReduceNullOffset() {
        double a = 2.5;
        double period = 3.14;
        MathUtils.reduce(a, period, null);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudePositiveSign() {
        byte magnitude = -1;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignBytePositiveMagnitudeNegativeSign() {
        byte magnitude = 1;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeSignAndMagnitudeMinusOneTwelfth() {
        byte magnitude = -1;
        byte sign = -128;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShortNegativeMagnitudePositiveSign() {
        short magnitude = -1;
        short sign = 1;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);
    }

    @Test
    public void testCopySignShortPositiveMagnitudeNegativeSign() {
        short magnitude = 1;
        short sign = -1;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(-magnitude, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeSignAndMagnitudeMinusOneHalf() {
        short magnitude = -1;
        short sign = -32768;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignIntNegativeMagnitudePositiveSign() {
        int magnitude = -1;
        int sign = 1;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);
    }

    @Test
    public void testCopySignIntPositiveMagnitudeNegativeSign() {
        int magnitude = 1;
        int sign = -1;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(-magnitude, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeSignAndMagnitudeMinusOne() {
        int magnitude = -1;
        int sign = Integer.MIN_VALUE;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongNegativeMagnitudePositiveSign() {
        long magnitude = -1;
        long sign = 1;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);
    }

    @Test
    public void testCopySignLongPositiveMagnitudeNegativeSign() {
        long magnitude = 1;
        long sign = -1;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(-magnitude, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeSignAndMagnitudeMinusOne() {
        long magnitude = -1;
        long sign = Long.MIN_VALUE;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.2;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArray() {
        double[] values = {Double.NaN, 1.2, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNullElement() {
        double[] values = {1.2, null, 3.14};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        MathUtils.checkNotNull(null);
    }

    @Test
    public void testCheckNotNullObjectPattern() {
        Object obj = new Object();
        String pattern = LocalizedFormats.OBJECT_NULL;
        MathUtils.checkNotNull(obj, pattern);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectPatternNull() {
        MathUtils.checkNotNull(null, "Object is null");
    }

    @Test
    public void testCheckNotNullObjectArray() {
        Object[] objArray = new Object[3];
        MathUtils.checkNotNull(objArray);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectArrayNull() {
        MathUtils.checkNotNull(null);
    }
}