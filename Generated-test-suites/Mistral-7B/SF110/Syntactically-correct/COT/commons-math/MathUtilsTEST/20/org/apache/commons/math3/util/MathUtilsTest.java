package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 12345; // Replace with actual hashcode value for comparison
        int actual = MathUtils.hash(value);
        assertEquals("Hashcode for double value is incorrect.", expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 4321; // Replace with actual hashcode value for comparison
        int actual = MathUtils.hash(values);
        assertEquals("Hashcode for double array is incorrect.", expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI + 1.5;
        double center = 3.14;
        double expected = 0.3141592653589793; // Replace with actual result for comparison
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals("Normalize angle function returns incorrect result.", expected, actual, 0.000001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithInvalidAngle() {
        double angle = Double.POSITIVE_INFINITY;
        double center = 3.14;
        MathUtils.normalizeAngle(angle, center); // Exception should be thrown
    }

    @Test
    public void testReduce() {
        double angle = 5.1;
        double period = 2.0;
        double offset = 1.5;
        double expected = 1.1; // Replace with actual result for comparison
        double actual = MathUtils.reduce(angle, period, offset);
        assertEquals("Reduce function returns incorrect result.", expected, actual, 0.000001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteWithInvalidValues() {
        byte magnitude = 5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign); // Exception should be thrown
    }

    @Test
    public void testCopySignShortWithValidValues() {
        short magnitude = 10;
        short sign = 1;
        short expected = 10;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign function returns incorrect result for valid values.", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortWithNegativeMagnitudeAndPositiveSign() {
        short magnitude = -10;
        short sign = 1;
        MathUtils.copySign(magnitude, sign); // Exception should be thrown
    }

    @Test
    public void testCopySignIntWithValidValues() {
        int magnitude = 20;
        int sign = 1;
        int expected = 20;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign function returns incorrect result for valid values.", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntWithNegativeMagnitudeAndPositiveSign() {
        int magnitude = -20;
        int sign = 1;
        MathUtils.copySign(magnitude, sign); // Exception should be thrown
    }

    @Test
    public void testCopySignLongWithValidValues() {
        long magnitude = 30L;
        long sign = 1;
        long expected = 30L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign function returns incorrect result for valid values.", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongWithNegativeMagnitudeAndPositiveSign() {
        long magnitude = -30L;
        long sign = 1;
        MathUtils.copySign(magnitude, sign); // Exception should be thrown
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.2;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value); // Exception should be thrown
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinity() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value); // Exception should be thrown
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {1.2, Double.NaN, 3.4};
        MathUtils.checkFinite(values); // Exception should be thrown
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinity() {
        double[] values = {1.2, Double.POSITIVE_INFINITY, 3.4};
        MathUtils.checkFinite(values); // Exception should be thrown
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object obj = null;
        MathUtils.checkNotNull(obj); // Exception should be thrown
    }

    @Test
    public void testCheckNotNullObjectPattern() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, "nullObject", "Object cannot be null.");
        assertEquals("Message for nullObject does not match expected.", "Object cannot be null.", MathUtils.getMessage(obj, "nullObject"));
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectPatternInvalidMessage() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, "invalidObject", "Invalid object.");
        assertEquals("Message for invalidObject does not match expected.", "Object cannot be null.", MathUtils.getMessage(obj, "invalidObject"));
    }
}