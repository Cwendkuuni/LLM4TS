package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(139596753, MathUtils.hash(1.2));
        assertEquals(362438029, MathUtils.hash(2.71828));
        assertEquals(943708719, MathUtils.hash(-3.14159265));
        assertEquals(-1637139742, MathUtils.hash(-1.0));
    }

    @Test
    public void testHashDoubleArray() {
        double[] array = {1.2, 2.71828, -3.14159265, -1.0};
        long expectedHashCode = 1375577558263632336L; // calculated using Java's Arrays.hashCode() method
        assertEquals(expectedHashCode, MathUtils.hash(array));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(-3.14159265, MathUtils.normalizeAngle(-6.283185307, 0.0));
        assertEquals(1.570796326794897, MathUtils.normalizeAngle(-3.14159265, 0.0));
        assertEquals(1.570796326794897, MathUtils.normalizeAngle(3.14159265, 0.0));
        assertEquals(1.047197551196598, MathUtils.normalizeAngle(4.71238898338063, 0.0));
        assertEquals(6.028821929634424, MathUtils.normalizeAngle(6.769236907884986, 3.14159265));
    }

    @Test
    public void testReduce() {
        assertEquals(1.2, MathUtils.reduce(1.5, 0.3, 0.1), 0.001);
        assertEquals(1.2, MathUtils.reduce(1.5, 0.4, 0.1), 0.001);
        assertEquals(1.5, MathUtils.reduce(1.8, 0.3, 0.1), 0.001);
        assertEquals(1.8, MathUtils.reduce(2.1, 0.3, 0.1), 0.001);
        assertEquals(-0.3, MathUtils.reduce(-0.6, 0.3, 0.1), 0.001);
        assertEquals(-0.6, MathUtils.reduce(-0.9, 0.3, 0.1), 0.001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositive() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignBytePositiveAndPositive() {
        byte magnitude = 5;
        byte sign = 1;
        assertEquals(5, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignBytePositiveAndNegative() {
        byte magnitude = 5;
        byte sign = -1;
        assertEquals(-5, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndNegative() {
        byte magnitude = -5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShortPositiveAndPositive() {
        short magnitude = 5;
        short sign = 1;
        assertEquals(5, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShortPositiveAndNegative() {
        short magnitude = 5;
        short sign = -1;
        assertEquals(-5, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShortNegativeAndPositive() {
        short magnitude = -5;
        short sign = 1;
        assertEquals(-5, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeAndNegative() {
        short magnitude = -5;
        short sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignIntPositiveAndPositive() {
        int magnitude = 5;
        int sign = 1;
        assertEquals(5, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignIntPositiveAndNegative() {
        int magnitude = 5;
        int sign = -1;
        assertEquals(-5, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignIntNegativeAndPositive() {
        int magnitude = -5;
        int sign = 1;
        assertEquals(-5, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeAndNegative() {
        int magnitude = -5;
        int sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongPositiveAndPositive() {
        long magnitude = 5;
        long sign = 1;
        assertEquals(5, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignLongPositiveAndNegative() {
        long magnitude = 5;
        long sign = -1;
        assertEquals(-5, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignLongNegativeAndPositive() {
        long magnitude = -5;
        long sign = 1;
        assertEquals(-5, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeAndNegative() {
        long magnitude = -5;
        long sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(1.23);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNegativeInfinite() {
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoublePositiveInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] array = {1.23, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN};
        MathUtils.checkFinite(array);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] array = {Double.NaN};
        MathUtils.checkFinite(array);
    }
}