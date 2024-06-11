package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(Double.hashCode(1.0), MathUtils.hash(1.0));
        assertEquals(Double.hashCode(-1.0), MathUtils.hash(-1.0));
        assertEquals(Double.hashCode(Double.NaN), MathUtils.hash(Double.NaN));
        assertEquals(Double.hashCode(Double.POSITIVE_INFINITY), MathUtils.hash(Double.POSITIVE_INFINITY));
        assertEquals(Double.hashCode(Double.NEGATIVE_INFINITY), MathUtils.hash(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testHashDoubleArray() {
        double[] arr = {1.0, 2.0, 3.0};
        assertEquals(Arrays.hashCode(arr), MathUtils.hash(arr));
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithNaN() {
        MathUtils.normalizeAngle(Double.NaN, 0.0);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithInfinite() {
        MathUtils.normalizeAngle(Double.POSITIVE_INFINITY, 0.0);
        MathUtils.normalizeAngle(Double.NEGATIVE_INFINITY, 0.0);
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.1, MathUtils.normalizeAngle(0.6, 1.0), 0.001);
        assertEquals(-0.3, MathUtils.normalizeAngle(-1.6, 1.0), 0.001);
        assertEquals(-6.2831853, MathUtils.normalizeAngle(-6.8, 1.0), 0.001);
        assertEquals(3.1415926, MathUtils.normalizeAngle(3.8, 1.0), 0.001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMaxValueAndPositiveSign() {
        MathUtils.copySign((byte) -128, (byte) 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeMaxValueAndPositiveSign() {
        MathUtils.copySign((short) -32768, (short) 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeMinValueAndPositiveSign() {
        MathUtils.copySign(Integer.MIN_VALUE, 1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeMinValueAndPositiveSign() {
        MathUtils.copySign(Long.MIN_VALUE, 1);
    }

    @Test
    public void testCopySign() {
        assertEquals((byte) 5, MathUtils.copySign((byte) 5, (byte) 1));
        assertEquals((byte) -5, MathUtils.copySign((byte) 5, (byte) -1));
        assertEquals((byte) -5, MathUtils.copySign((byte) -5, (byte) 1));
        assertEquals((byte) 5, MathUtils.copySign((byte) -5, (byte) -1));

        assertEquals((short) 5, MathUtils.copySign((short) 5, (short) 1));
        assertEquals((short) -5, MathUtils.copySign((short) 5, (short) -1));
        assertEquals((short) -5, MathUtils.copySign((short) -5, (short) 1));
        assertEquals((short) 5, MathUtils.copySign((short) -5, (short) -1));

        assertEquals(5, MathUtils.copySign(5, 1));
        assertEquals(-5, MathUtils.copySign(5, -1));
        assertEquals(-5, MathUtils.copySign(-5, 1));
        assertEquals(5, MathUtils.copySign(-5, -1));

        assertEquals(5L, MathUtils.copySign(5L, 1L));
        assertEquals(-5L, MathUtils.copySign(5L, -1L));
        assertEquals(-5L, MathUtils.copySign(-5L, 1L));
        assertEquals(5L, MathUtils.copySign(-5L, -1L));
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNullArray() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaNElement() {
        double[] arr = {1.0, Double.NaN, 3.0};
        MathUtils.checkFinite(arr);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfElement() {
        double[] arr = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(arr);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, LocalizedFormats.OBJECT, new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullArrayNullElement() {
        int[] arr = {1, null, 3};
        MathUtils.checkNotNull(arr, LocalizedFormats.ARRAY, 1);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullArrayNullObject() {
        Object[] arr = {1, null};
        MathUtils.checkNotNull(arr);
    }

    @Test
    public void testCheckNotNull() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}