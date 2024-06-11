package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-1107735582, MathUtils.hash(0.0));
        assertEquals(-1627686587, MathUtils.hash(1.23));
        assertEquals(1637259619, MathUtils.hash(3.14));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {0.0, 1.23, 3.14};
        assertEquals(-1509488935, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(3.141592653589793, 0.0), 0.0000001);
        assertEquals(-1.5707963267948966, MathUtils.normalizeAngle(-3.141592653589793, 0.0), 0.0000001);
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(6.85505981214817, 3.141592653589793), 0.0000001);
        assertEquals(-1.5707963267948966, MathUtils.normalizeAngle(-6.85505981214817, 3.141592653589793), 0.0000001);
    }

    @Test
    public void testReduce() {
        assertEquals(-2.528860536554273, MathUtils.reduce(-5.0, 2.0, 0.0), 0.0000001);
        assertEquals(0.6283185307179586, MathUtils.reduce(1.23, 1.28, 0.0), 0.0000001);
        assertEquals(1.28, MathUtils.reduce(2.5, 1.28, 1.28), 0.0000001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegative() {
        MathUtils.copySign((byte)5, (byte)-1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        MathUtils.copySign((byte)-128, (byte)1);
    }

    @Test
    public void testCopySignByte() {
        assertEquals((byte)5, MathUtils.copySign((byte)5, (byte)1));
        assertEquals((byte)-5, MathUtils.copySign((byte)5, (byte)-1));
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short)5, MathUtils.copySign((short)5, (short)1));
        assertEquals((short)-5, MathUtils.copySign((short)5, (short)-1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegative() {
        MathUtils.copySign((short)5, (short)-1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        MathUtils.copySign((short)-32768, (short)1);
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short)5, MathUtils.copySign((short)5, (short)1));
        assertEquals((short)-5, MathUtils.copySign((short)5, (short)-1));
    }

    @Test
    public void testCopySignInt() {
        assertEquals(5, MathUtils.copySign(5, 1));
        assertEquals(-5, MathUtils.copySign(5, -1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegative() {
        MathUtils.copySign(5, -1);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        MathUtils.copySign(Integer.MIN_VALUE, 1);
    }

    @Test
    public void testCopySignLong() {
        assertEquals(5L, MathUtils.copySign(5L, 1L));
        assertEquals(-5L, MathUtils.copySign(5L, -1L));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegative() {
        MathUtils.copySign(5L, -1L);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        MathUtils.copySign(Long.MIN_VALUE, 1L);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {0.0, 1.23, 3.14};
        MathUtils.checkFinite(values);
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
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {0.0, 1.23, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {0.0, 1.23, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObjectNull() {
        try {
            MathUtils.checkNotNull(null, "Test pattern", new Object[0]);
            fail();
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCheckNotNullObject() {
        MathUtils.checkNotNull(new Object(), "Test pattern", new Object[0]);
    }
}