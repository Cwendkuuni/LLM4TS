package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedHashCode = 1047668301; // Example hash code for 1.2
        int actualHashCode = MathUtils.hash(value);
        assertEquals("Hash code for double should be correct", expectedHashCode, actualHashCode);
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
    public void testNormalizeAngleBasic() {
        double angle = 3.14; // 90 degrees
        double center = 0.0;
        double expectedResult = 1.57; // 90 degrees - 180 degrees = 90 degrees - 360 degrees + 180 degrees = 270 degrees
        double result = MathUtils.normalizeAngle(angle, center);
        assertEquals("Normalize angle should return expected value", expectedResult, result, 1e-12);
    }

    @Test
    public void testReduceBasic() {
        double a = 3.14;
        double period = 6.283185307179586;
        double offset = 0.0;
        double expectedResult = 0.14;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals("Reduce method should return expected value", expectedResult, result, 1e-12);
    }

    @Test
    public void testCopySignBytePositive() {
        byte magnitude = 5;
        byte sign = 1;
        byte expectedResult = 5;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign method for byte should return expected value", expectedResult, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeWithPositiveSign() {
        byte magnitude = 5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShortPositive() {
        short magnitude = 5;
        short sign = 1;
        short expectedResult = 5;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign method for short should return expected value", expectedResult, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeWithPositiveSign() {
        short magnitude = 5;
        short sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignIntPositive() {
        int magnitude = 5;
        int sign = 1;
        int expectedResult = 5;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign method for int should return expected value", expectedResult, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeWithPositiveSign() {
        int magnitude = 5;
        int sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongPositive() {
        long magnitude = 5;
        long sign = 1;
        long expectedResult = 5;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign method for long should return expected value", expectedResult, result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeWithPositiveSign() {
        long magnitude = 5;
        long sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testHashArrayDoubleBasic() {
        double[] values = {1.1, 2.2, 3.3};
        int expectedHashCode = 687316079; // Example hash code for {1.1, 2.2, 3.3}
        int actualHashCode = MathUtils.hash(values);
        assertEquals("Hash code for double array should be correct", expectedHashCode, actualHashCode);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, LocalizedFormats.OBJECT_NULL, new Object[0]);
    }

    @Test
    public void testCheckFiniteDoubleArrayBasic() {
        double[] values = {1.1, 2.2, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaNElement() {
        double[] values = {1.1, Double.NaN, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }
}