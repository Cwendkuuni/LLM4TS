package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-1965491681, MathUtils.hash(0.12345));
        assertEquals(1497852793, MathUtils.hash(1.2345));
        assertEquals(-2083483494, MathUtils.hash(2.345));
        assertEquals(-152549356, MathUtils.hash(3.45));
        assertEquals(-1664834723, MathUtils.hash(4.56));
        assertEquals(353652285, MathUtils.hash(5.67));
        assertEquals(-1483407984, MathUtils.hash(6.78));
        assertEquals(-1643546349, MathUtils.hash(7.89));
        assertEquals(1535921268, MathUtils.hash(-1.2345));
        assertEquals(-1562325647, MathUtils.hash(-2.345));
        assertEquals(-1559285332, MathUtils.hash(-3.45));
        assertEquals(-1644722256, MathUtils.hash(-4.56));
        assertEquals(525636055, MathUtils.hash(-5.67));
        assertEquals(-1495228396, MathUtils.hash(-6.78));
        assertEquals(-1517547551, MathUtils.hash(-7.89));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {0.12345, 1.2345, 2.345, 3.45, 4.56, 5.67, 6.78, 7.89, -1.2345, -2.345, -3.45, -4.56, -5.67, -6.78, -7.89};
        long expectedHashCode = 893247094139435486L;
        assertEquals(expectedHashCode, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(3.141592653589793, 0.0), 0.0000001);
        assertEquals(-1.5707963267948966, MathUtils.normalizeAngle(6.341592653589793, 0.0), 0.0000001);
        assertEquals(1.0471975511965978, MathUtils.normalizeAngle(4.712385625, MathUtils.TWO_PI / 2), 0.0000001);
        assertEquals(-1.0471975511965978, MathUtils.normalizeAngle(5.712385625, MathUtils.TWO_PI / 2), 0.0000001);
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 0.0000001);
        assertEquals(-3.141592653589793, MathUtils.normalizeAngle(6.283185307179586 * 2, 0.0), 0.0000001);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(-6.283185307179586 * 2, 0.0), 0.0000001);
    }

    @Test
    public void testReduce() {
        assertEquals(1.2345, MathUtils.reduce(1.5, 1.2345, 0.0), 0.0000001);
        assertEquals(1.7654, MathUtils.reduce(2.345, 1.2345, 1.1111), 0.0000001);
        assertEquals(-1.2345, MathUtils.reduce(-1.5, 1.2345, 0.0), 0.0000001);
        assertEquals(-1.7654, MathUtils.reduce(-2.345, 1.2345, 1.1111), 0.0000001);
        assertEquals(1.5, MathUtils.reduce(1.5, 1.2345, 1.2345), 0.0000001);
        assertEquals(1.2345, MathUtils.reduce(1.5, 1.2345, 1.7654), 0.0000001);
        assertEquals(1.2345, MathUtils.reduce(1.5, 1.2345, -1.2345), 0.0000001);
        assertEquals(1.2345, MathUtils.reduce(1.5, 1.2345, -1.7654), 0.0000001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeOverflow() {
        byte magnitude = Byte.MIN_VALUE;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeNegative() {
        byte magnitude = -1;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignBytePositive() {
        assertEquals((byte)1, MathUtils.copySign((byte)1, (byte)1));
        assertEquals((byte)-1, MathUtils.copySign((byte)-1, (byte)1));
        assertEquals((byte)-1, MathUtils.copySign((byte)1, (byte)-1));
        assertEquals((byte)1, MathUtils.copySign((byte)-1, (byte)-1));
    }

    @Test
    public void testCopySignShortNegativeOverflow() {
        short magnitude = Short.MIN_VALUE;
        short sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            return;
        }
        fail();
    }

    @Test
    public void testCopySignShortNegativeNegative() {
        short magnitude = -1;
        short sign = -1;
        try {
            MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            return;
        }
        fail();
    }

    @Test
    public void testCopySignShortPositive() {
        assertEquals((short)1, MathUtils.copySign((short)1, (short)1));
        assertEquals((short)-1, MathUtils.copySign((short)-1, (short)1));
        assertEquals((short)-1, MathUtils.copySign((short)1, (short)-1));
        assertEquals((short)1, MathUtils.copySign((short)-1, (short)-1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeNegative() {
        int magnitude = -1;
        int sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignIntPositive() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(1, MathUtils.copySign(-1, -1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeNegative() {
        long magnitude = -1;
        long sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongPositive() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(1, MathUtils.copySign(-1, -1));
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(12345.0);
        MathUtils.checkFinite(-0.0);
        MathUtils.checkFinite(-1.0);
        MathUtils.checkFinite(-12345.0);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNegativeElement() {
        double[] values = {0.0, Double.NEGATIVE_INFINITY, 1.0, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteDoubleArrayAllFinite() {
        double[] values = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckNotNullNull() {
        try {
            MathUtils.checkNotNull(null, "null argument");
        } catch (NullArgumentException e) {
            return;
        }
        fail();
    }

    @Test
    public void testCheckNotNullNotNull() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

}