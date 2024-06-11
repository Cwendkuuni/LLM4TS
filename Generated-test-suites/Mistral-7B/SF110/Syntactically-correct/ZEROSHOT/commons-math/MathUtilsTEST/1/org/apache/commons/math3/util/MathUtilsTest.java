package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = 1235617641; // Hashcode for the given double value
        int actual = MathUtils.hash(value);
        assertEquals("Incorrect hashcode for double value", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testHashDoubleThrowsExceptionForNegativeValue() {
        double value = -1.23;
        MathUtils.hash(value); // This line should throw MathArithmeticException
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.718, 3.14159265};
        int expected = 360315288; // Hashcode for the given double array
        int actual = MathUtils.hash(values);
        assertEquals("Incorrect hashcode for double array", expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI + Math.PI;
        double center = MathUtils.TWO_PI / 2;
        double expected = -Math.PI / 2;
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals("Incorrect normalized angle", expected, actual, 0.0001);
    }

    @Test
    public void testReduce() {
        double angle = Math.PI * 2 + Math.PI / 3;
        double period = 2 * Math.PI;
        double offset = Math.PI / 4;
        double expected = Math.PI / 12;
        double actual = MathUtils.reduce(angle, period, offset);
        assertEquals("Incorrect reduced angle", expected, actual, 0.0001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteThrowsExceptionForIncompatibleSignsAndNonNegativeMagnitude() {
        byte sign = -1;
        byte magnitude = 5;
        MathUtils.copySign(magnitude, sign); // This line should throw MathArithmeticException
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteThrowsExceptionForNegativeMagnitudeAndNonNegativeSign() {
        byte sign = 1;
        byte magnitude = -5;
        MathUtils.copySign(magnitude, sign); // This line should throw MathArithmeticException
    }

    @Test
    public void testCopySignByte() {
        byte sign = -1;
        byte magnitude = 5;
        byte expected = -5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect copied sign for byte", expected, actual);
    }

    @Test
    public void testCopySignShort() {
        short sign = -1;
        short magnitude = 5;
        short expected = -5;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect copied sign for short", expected, actual);
    }

    @Test
    public void testCopySignInt() {
        int sign = -1;
        int magnitude = 5;
        int expected = -5;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect copied sign for int", expected, actual);
    }

    @Test
    public void testCopySignLong() {
        long sign = -1;
        long magnitude = 5;
        long expected = -5;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect copied sign for long", expected, actual);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleThrowsExceptionForInfiniteValue() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value); // This line should throw NotFiniteNumberException
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayThrowsExceptionForInvalidElement() {
        double[] values = {1.23, Double.NaN, 3.14159265};
        MathUtils.checkFinite(values); // This line should throw NotFiniteNumberException
    }

    @Test
    public void testCheckNotNullObject() {
        MathUtils mathUtils = new MathUtils();
        MathUtils.checkNotNull(mathUtils);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObjectThrowsException() {
        MathUtils.checkNotNull(null); // This line should throw NullArgumentException
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullStringPatternThrowsException() {
        String pattern = null;
        MathUtils.checkNotNull(new Object(), pattern, new Object[0]); // This line should throw NullArgumentException
    }

    @Test
    public void testCheckNotNullNonNullObject() {
        MathUtils mathUtils = new MathUtils();
        MathUtils.checkNotNull(mathUtils, new Object[0]);
    }

    @Test
    public void testCheckNotNullNonNullObjectAndPattern() {
        MathUtils mathUtils = new MathUtils();
        MathUtils.checkNotNull(mathUtils, new Object[]{"Expected message"}, new Object[0]);
    }
}