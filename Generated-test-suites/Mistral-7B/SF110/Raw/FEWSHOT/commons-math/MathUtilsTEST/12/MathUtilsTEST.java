package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(131291719, MathUtils.hash(0.123));
        assertEquals(131291719, MathUtils.hash(0.123d));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        assertEquals(220582595, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(3.141592653589793, 0.0), 0.0000001);
        assertEquals(4.7123889803846905, MathUtils.normalizeAngle(-3.141592653589793, MathUtils.TWO_PI), 0.0000001);
        assertEquals(6.283185307179586, MathUtils.normalizeAngle(MathUtils.TWO_PI, 0.0), 0.0000001);
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, MathUtils.TWO_PI), 0.0000001);
    }

    @Test
    public void testReduce() {
        assertEquals(-1.5707963267948966, MathUtils.reduce(3.141592653589793, MathUtils.TWO_PI, 0.0), 0.0000001);
        assertEquals(-2.356194490192345, MathUtils.reduce(3.856194490192345, MathUtils.TWO_PI, 1.5707963267948966), 0.0000001);
        assertEquals(1.5707963267948966, MathUtils.reduce(-3.141592653589793, MathUtils.TWO_PI, 0.0), 0.0000001);
        assertEquals(1.141592653589793, MathUtils.reduce(-3.141592653589793, MathUtils.TWO_PI, -1.5707963267948966), 0.0000001);
        assertEquals(1.5707963267948966, MathUtils.reduce(-3.141592653589793, MathUtils.TWO_PI, MathUtils.TWO_PI - 1.5707963267948966), 0.0000001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeWithPositive() {
        MathUtils.copySign((byte) -1, (byte) 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeWithNegativeAndOverflow() {
        MathUtils.copySign((byte) -129, (byte) 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeWithNegativeAndUnderflow() {
        MathUtils.copySign((byte) -127, (byte) 1);
    }

    @Test
    public void testCopySignBytePositiveWithPositive() {
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) 1));
    }

    @Test
    public void testCopySignBytePositiveWithNegative() {
        assertEquals((byte) -1, MathUtils.copySign((byte) 1, (byte) -1));
    }

    @Test
    public void testCopySignShortNegativeWithPositive() {
        MathUtils.copySign((short) -1, (short) 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeWithNegativeAndOverflow() {
        MathUtils.copySign((short) -32768, (short) 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeWithNegativeAndUnderflow() {
        MathUtils.copySign((short) -32767, (short) 1);
    }

    @Test
    public void testCopySignShortPositiveWithPositive() {
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) 1));
    }

    @Test
    public void testCopySignShortPositiveWithNegative() {
        assertEquals((short) -1, MathUtils.copySign((short) 1, (short) -1));
    }

    @Test
    public void testCopySignIntNegativeWithPositive() {
        MathUtils.copySign((int) -1, (int) 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeWithNegativeAndOverflow() {
        MathUtils.copySign((int) -2147483648, (int) 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeWithNegativeAndUnderflow() {
        MathUtils.copySign((int) -2147483647, (int) 1);
    }

    @Test
    public void testCopySignIntPositiveWithPositive() {
        assertEquals(1, MathUtils.copySign(1, 1));
    }

    @Test
    public void testCopySignIntPositiveWithNegative() {
        assertEquals(-1, MathUtils.copySign(1, -1));
    }

    @Test
    public void testCopySignLongNegativeWithPositive() {
        MathUtils.copySign((long) -1, (long) 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeWithNegativeAndOverflow() {
        MathUtils.copySign((long) -9223372036854775808L, (long) 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeWithNegativeAndUnderflow() {
        MathUtils.copySign((long) -9223372036854775807L, (long) 1);
    }

    @Test
    public void testCopySignLongPositiveWithPositive() {
        assertEquals(1, MathUtils.copySign(1, 1));
    }

    @Test
    public void testCopySignLongPositiveWithNegative() {
        assertEquals(-1, MathUtils.copySign(1, -1));
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);

        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.0);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNull() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInvalid() {
        double[] values = {Double.NEGATIVE_INFINITY, 1.0, Double.POSITIVE_INFINITY, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteDoubleArrayValid() {
        double[] values = {0.0, 1.0, 2.0};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObjectNull() {
        try {
            MathUtils.checkNotNull(null, "Object is null");
            fail();
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCheckNotNullObjectValid() {
        MathUtils.checkNotNull(new Object(), "Object is valid");
    }
}