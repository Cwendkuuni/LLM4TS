package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-1946055151, MathUtils.hash(0.0));
        assertEquals(123456789, MathUtils.hash(123.0));
        assertEquals(123456789, MathUtils.hash(123.5));
        assertEquals(318530717, MathUtils.hash(Math.PI));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testHashNullDouble() {
        MathUtils.hash(null);
    }

    @Test
    public void testHashDoubleArray() {
        double[] arr = {0.0, 123.0, 123.5, Math.PI};
        assertEquals(-2129635349, MathUtils.hash(arr));
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void testHashNullDoubleArray() {
        MathUtils.hash(null);
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(0.0), 0.00001);
        assertEquals(-3.141592653589793, MathUtils.normalizeAngle(6.283185307179586), 0.00001);
        assertEquals(1.570796326794897, MathUtils.normalizeAngle(4.71238898038469), 0.00001);
        assertEquals(-5.71238898038469, MathUtils.normalizeAngle(-11.283185307179586), 0.00001);
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testReduceWithNegativePeriod() {
        MathUtils.reduce(0.5, -3.141592653589793, 0.0);
    }

    @Test
    public void testReduce() {
        assertEquals(0.5, MathUtils.reduce(0.5, 6.283185307179586, 0.0), 0.00001);
        assertEquals(-1.570796326794897, MathUtils.reduce(-1.570796326794897, 6.283185307179586, 3.141592653589793), 0.00001);
        assertEquals(-2.570796326794897, MathUtils.reduce(-2.570796326794897, 6.283185307179586, 6.283185307179586), 0.00001);
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudePositiveSign() {
        MathUtils.copySign((byte) -1, (byte) 1);
    }

    @Test
    public void testCopySignByte() {
        assertEquals((byte) -5, MathUtils.copySign((byte) 5, (byte) -1));
        assertEquals((byte) 5, MathUtils.copySign((byte) 5, (byte) 1));
        assertEquals((byte) 5, MathUtils.copySign((byte) 5, (byte) 0));
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        MathUtils.copySign((byte) 127, (byte) 1);
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short) -5, MathUtils.copySign((short) 5, (short) -1));
        assertEquals((short) 5, MathUtils.copySign((short) 5, (short) 1));
        assertEquals((short) 5, MathUtils.copySign((short) 5, (short) 0));
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        MathUtils.copySign((short) 32767, (short) 1);
    }

    @Test
    public void testCopySignInt() {
        assertEquals(5, MathUtils.copySign(5, 1));
        assertEquals(-5, MathUtils.copySign(5, -1));
        assertEquals(-5, MathUtils.copySign(-5, 1));
        assertEquals(5, MathUtils.copySign(-5, -1));
        assertEquals(-2147483648, MathUtils.copySign(Integer.MIN_VALUE, 1));
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        MathUtils.copySign(Integer.MIN_VALUE, 1);
    }

    @Test
    public void testCopySignLong() {
        assertEquals(5, MathUtils.copySign(5, 1));
        assertEquals(-5, MathUtils.copySign(5, -1));
        assertEquals(-5, MathUtils.copySign(-5, 1));
        assertEquals(5, MathUtils.copySign(-5, -1));
        assertEquals(-9223372036854775807L, MathUtils.copySign(Long.MIN_VALUE, 1));
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        MathUtils.copySign(Long.MIN_VALUE, 1);
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNull() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testCheckFiniteDoubleFinite() {
        MathUtils.checkFinite(Double.NaN);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(123.0);
        MathUtils.checkFinite(Math.PI);
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNull() {
        MathUtils.checkFinite(null);
    }

    @Test
    public void testCheckFiniteDoubleArrayFinite() {
        double[] arr = {0.0, 123.0, Math.PI};
        MathUtils.checkFinite(arr);
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] arr = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN};
        MathUtils.checkFinite(arr);
    }

    @Test
    public void testCheckNotNullObject() {
        MathUtils.checkNotNull(new Object());
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        MathUtils.checkNotNull(null);
    }

    @Test
    public void testCheckNotNullLocalizablePattern() {
        MathUtils.checkNotNull(new Object(), LocalizedFormats.ARRAY_ELEMENT, 0);
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testCheckNotNullLocalizablePatternNull() {
        MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 0);
    }
}