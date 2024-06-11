
package org.apache.commons.math3.util;
import org.junit.Test;
import org.junit.Assert.*;

public class MathUtilsTest {

    // Tests for hash method
    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = 12345; // replace this with the actual hashcode for the given double value
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testHashNullDouble() {
        double value = null;
        MathUtils.hash(value); // should throw a NullPointerException
    }

    @Test
    public void testHashArrayOfDoubles() {
        double[] values = {1.23, 2.34, 3.5};
        int expected = 123456789; // replace this with the actual hashcode for the given double array
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    // Tests for normalizeAngle method
    @Test
    public void testNormalizeAngle() {
        double angle = 10.5;
        double center = 0.0;
        double expected = 1.5; // the angle 10.5 degrees normalized around the center 0.0 degrees
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testNormalizeAngleWithNegativeAngleAndPositiveCenter() {
        double angle = -5.5;
        double center = 0.0;
        double expected = -4.0; // the angle -5.5 degrees normalized around the center 0.0 degrees
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.0001);
    }

    @Test
    public void testNormalizeAngleWithBigAngle() {
        double angle = 361.5;
        double center = 0.0;
        double expected = 0.1; // the angle 361.5 degrees normalized around the center 0.0 degrees
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals(expected, actual, 0.0001);
    }

    // Tests for copySign methods
    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignByteWithNegativeSign() {
        byte magnitude = 5;
        byte sign = -1;
        byte expected = -5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteWithNegativeMagnitudeAndPositiveSign() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // should throw a MathArithmeticException
    }

    // Add similar test cases for short, int, and long data types

    // Tests for checkFinite method
    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value); // should throw a NotFiniteNumberException
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinite() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value); // should throw a NotFiniteNumberException
    }

    @Test
    public void testCheckFiniteFinite() {
        double value = 3.141592653589793;
        MathUtils.checkFinite(value);
    }

    // Add similar test cases for the checkFinite method that accepts an array of doubles

    // Tests for checkNotNull method
    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object obj = null;
        MathUtils.checkNotNull(obj); // should throw a NullArgumentException
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullArray() {
        Object[] obj = null;
        MathUtils.checkNotNull(obj); // should throw a NullArgumentException
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testCheckNotNullNotNullArray() {
        Object[] obj = new Object[10];
        MathUtils.checkNotNull(obj);
    }
}