package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(Double.hashCode(1.0), MathUtils.hash(1.0));
        assertEquals(Double.hashCode(1.0d), MathUtils.hash(1.0d));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashDoubleNull() {
        MathUtils.hash(null);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = new double[]{1.0, 2.0, 3.0};
        assertEquals(Arrays.hashCode(values), MathUtils.hash(values));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHashDoubleArrayNull() {
        MathUtils.hash(null);
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.1, MathUtils.normalizeAngle(3.141592653589793 + 0.1, 0.0), 0.0001);
        assertEquals(-0.1, MathUtils.normalizeAngle(3.141592653589793 - 0.1, 0.0), 0.0001);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(3.141592653589793, 3.141592653589793), 0.0001);
    }

    @Test
    public void testReduce() {
        assertEquals(1.5, MathUtils.reduce(2.5, 1.5, 0.5), 0.001);
        assertEquals(1.5, MathUtils.reduce(-2.5, 1.5, 0.5), 0.001);
        assertEquals(1.5, MathUtils.reduce(3.5, 1.5, 0.5), 0.001);
        assertEquals(-1.5, MathUtils.reduce(-2.5, 1.5, 0.5), 0.001);
        assertEquals(-1.5, MathUtils.reduce(3.5, 1.5, 0.5), 0.001);
    }

    @Test
    public void testCopySignByte() {
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) 1));
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) -1));
        assertEquals((byte) -1, MathUtils.copySign((byte) 1, (byte) -1));
        assertEquals((byte) -1, MathUtils.copySign((byte) 1, (byte) 1));
        try {
            MathUtils.copySign((byte) 128, (byte) 1);
            fail();
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) 1));
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) -1));
        assertEquals((short) -1, MathUtils.copySign((short) 1, (short) -1));
        assertEquals((short) -1, MathUtils.copySign((short) 1, (short) 1));
        try {
            MathUtils.copySign((short) 32768, (short) 1);
            fail();
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testCopySignInt() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(1, MathUtils.copySign(1, -1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(-1, MathUtils.copySign(1, 1));
        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
            fail();
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testCopySignLong() {
        assertEquals(1, MathUtils.copySign(1L, 1L));
        assertEquals(1, MathUtils.copySign(1L, -1L));
        assertEquals(-1, MathUtils.copySign(1L, -1L));
        assertEquals(-1, MathUtils.copySign(1L, 1L));
        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
            fail();
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNull() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNull() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = new double[]{Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = new double[]{Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteDoubleArrayFinite() {
        double[] values = new double[]{1.0, 2.0, 3.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, "Unexpected null object");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullPattern() {
        MathUtils.checkNotNull(null, "%s is null", "pattern");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullArgs() {
        MathUtils.checkNotNull(null, "Unexpected null argument", new Object[0]);
    }
}