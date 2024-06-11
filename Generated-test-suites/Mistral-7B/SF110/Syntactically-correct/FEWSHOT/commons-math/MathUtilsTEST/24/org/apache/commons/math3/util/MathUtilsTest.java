package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-2121216169, MathUtils.hash(1.23));
        assertEquals(-1488352656, MathUtils.hash(Double.NaN));
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNullArray() {
        MathUtils.checkFinite(null);
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(1.23);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNegativeIndex() {
        double[] arr = {};
        MathUtils.checkFinite(arr, -1);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] arr = {1.23, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN};
        MathUtils.checkFinite(arr);
    }

    @Test
    public void testCheckNotNullNullObject() {
        try {
            MathUtils.checkNotNull(null, () -> "Null object.");
        } catch (NullArgumentException e) {
            assertEquals("Null object.", e.getMessage());
        }
    }

    @Test
    public void testCheckNotNullNonnullObject() {
        MathUtils.checkNotNull(new Object());
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullPattern() {
        MathUtils.checkNotNull(new Object(), null);
    }

    @Test
    public void testCheckNotNullValidObject() {
        MathUtils.checkNotNull(new Object());
    }

    @Test
    public void testCopySignBytePositiveMagnitudePositiveSign() {
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) 1));
    }

    @Test
    public void testCopySignByteNegativeMagnitudeNegativeSign() {
        assertEquals((byte) -1, MathUtils.copySign((byte) -1, (byte) -1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudePositiveSign() {
        MathUtils.copySign((byte) -1, (byte) 1);
    }

    @Test
    public void testCopySignShortPositiveMagnitudePositiveSign() {
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) 1));
    }

    @Test
    public void testCopySignShortNegativeMagnitudeNegativeSign() {
        assertEquals((short) -1, MathUtils.copySign((short) -1, (short) -1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeMagnitudePositiveSign() {
        MathUtils.copySign((short) -1, (short) 1);
    }

    @Test
    public void testCopySignIntPositiveMagnitudePositiveSign() {
        assertEquals(1, MathUtils.copySign(1, 1));
    }

    @Test
    public void testCopySignIntNegativeMagnitudeNegativeSign() {
        assertEquals(-1, MathUtils.copySign(-1, -1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeMagnitudePositiveSign() {
        MathUtils.copySign(-1, 1);
    }

    @Test
    public void testCopySignLongPositiveMagnitudePositiveSign() {
        assertEquals(1L, MathUtils.copySign(1L, 1L));
    }

    @Test
    public void testCopySignLongNegativeMagnitudeNegativeSign() {
        assertEquals(-1L, MathUtils.copySign(-1L, -1L));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeMagnitudePositiveSign() {
        MathUtils.copySign(-1L, 1L);
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.1, MathUtils.normalizeAngle(1.6, 2.1), 0.001);
        assertEquals(0.7, MathUtils.normalizeAngle(-1.7, 2.1), 0.001);
        assertEquals(0.1, MathUtils.normalizeAngle(0.1, 0), 0.001);
        assertEquals(2.1, MathUtils.normalizeAngle(2.1, 0), 0.001);
        assertEquals(4.3, MathUtils.normalizeAngle(6.5, 2.2), 0.001);
    }

    @Test
    public void testReduce() {
        assertEquals(1.1, MathUtils.reduce(1.6, 2.1, 0), 0.001);
        assertEquals(1.3, MathUtils.reduce(-1.7, 2.1, 0), 0.001);
        assertEquals(1.1, MathUtils.reduce(0.1, 0, 0), 0.001);
        assertEquals(2.1, MathUtils.reduce(2.1, 0, 0), 0.001);
        assertEquals(2.3, MathUtils.reduce(4.5, 2.2, 0), 0.001);
    }

    @Test
    public void testHashDoubleArray() {
        double[] arr = {1.23, 2.46, 3.7};
        assertEquals(-1214828914, MathUtils.hash(arr));
    }
}