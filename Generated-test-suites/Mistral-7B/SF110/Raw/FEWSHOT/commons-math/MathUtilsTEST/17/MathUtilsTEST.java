package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-1541126525, MathUtils.hash(0.0));
        assertEquals(-1722014487, MathUtils.hash(1.23));
        assertEquals(1402995415, MathUtils.hash(-1.0));
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositive() {
        MathUtils.copySign((byte)1, (byte)1);
        MathUtils.copySign((byte)-1, (byte)1);
        MathUtils.copySign((byte)-1, (byte)-1);
        MathUtils.copySign((byte)-128, (byte)1);
    }

    @Test
    public void testCopySignBytePositiveAndPositive() {
        assertEquals((byte)1, MathUtils.copySign((byte)1, (byte)1));
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignByteNegativeAndNegative() {
        MathUtils.copySign((byte)-1, (byte)-1);
    }

    @Test(expected = org.apache.commons.math3.exception.MathArithmeticException.class)
    public void testCopySignByteNegativeAndZero() {
        MathUtils.copySign((byte)-1, (byte)0);
    }

    @Test
    public void testCopySignByteZeroAndPositive() {
        assertEquals((byte)0, MathUtils.copySign((byte)0, (byte)1));
    }

    @Test
    public void testCopySignByteZeroAndNegative() {
        assertEquals((byte)0, MathUtils.copySign((byte)0, (byte)-1));
    }

    @Test
    public void testCopySignShort() {
        // Test positive cases
        assertEquals((short)1, MathUtils.copySign((short)1, (short)1));
        assertEquals((short)-1, MathUtils.copySign((short)-1, (short)1));
        assertEquals((short)-1, MathUtils.copySign((short)-1, (short)-1));

        // Test negative cases
        try {
            MathUtils.copySign((short)-32768, (short)1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            MathUtils.copySign((short)-32768, (short)-1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            MathUtils.copySign((short)-32768, (short)0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testCopySignInt() {
        // Test positive cases
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(-1, MathUtils.copySign(-1, -1));

        // Test negative cases
        try {
            MathUtils.copySign(Integer.MIN_VALUE, 1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            MathUtils.copySign(Integer.MIN_VALUE, -1);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            MathUtils.copySign(Integer.MIN_VALUE, 0);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testCopySignLong() {
        // Test positive cases
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(-1L, MathUtils.copySign(-1L, 1L));
        assertEquals(-1L, MathUtils.copySign(-1L, -1L));

        // Test negative cases
        try {
            MathUtils.copySign(Long.MIN_VALUE, 1L);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            MathUtils.copySign(Long.MIN_VALUE, -1L);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }

        try {
            MathUtils.copySign(Long.MIN_Value, 0L);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected
        }
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNegative() {
        MathUtils.checkFinite(-Double.NaN);
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testCheckFiniteDoubleFinite() {
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.23);
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNegative() {
        double[] values = {-Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = org.apache.commons.math3.exception.NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteDoubleArrayFinite() {
        double[] values = {0.0, 1.23};
        MathUtils.checkFinite(values);
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        MathUtils.checkNotNull(null, "Object should not be null");
    }

    @Test
    public void testCheckNotNullObjectNotNull() {
        MathUtils.checkNotNull(new Object(), "Object should not be null");
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testCheckNotNullPatternAndArgsNull() {
        MathUtils.checkNotNull(null, "Pattern and args should not be null", new Object[0]);
    }

    @Test(expected = org.apache.commons.math3.exception.NullArgumentException.class)
    public void testCheckNotNullPatternNull() {
        MathUtils.checkNotNull(new Object(), null, new Object[0]);
    }

    @Test
    public void testCheckNotNullObjectAndArgsNotNull() {
        MathUtils.checkNotNull(new Object(), "Object and args should not be null", new Object[]{"arg1", "arg2"});
    }
}