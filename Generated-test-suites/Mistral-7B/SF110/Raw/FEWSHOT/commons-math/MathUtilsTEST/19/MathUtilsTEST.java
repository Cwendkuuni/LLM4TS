package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHash() {
        assertEquals(133193551, MathUtils.hash(1.2));
        double[] values = {1.1, 2.2, 3.3};
        assertEquals(-1496443445, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(3.141592653589793, 0.0), 0.00001);
        assertEquals(2.0943951023931953, MathUtils.normalizeAngle(5.477225575051661, MathUtils.TWO_PI), 0.00001);
        assertEquals(2.886810834615123, MathUtils.normalizeAngle(-4.440892098334023, MathUtils.TWO_PI), 0.00001);
    }

    @Test
    public void testReduce() {
        assertEquals(0.5, MathUtils.reduce(1.5, 2.0), 0.001);
        assertEquals(MathUtils.TWO_PI - 2.0, MathUtils.reduce(MathUtils.TWO_PI + 2.0, MathUtils.TWO_PI), 0.001);
        assertEquals(-MathUtils.TWO_PI + 2.0, MathUtils.reduce(-MathUtils.TWO_PI - 2.0, MathUtils.TWO_PI), 0.001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteThrow() {
        MathUtils.copySign((byte)1, (byte)-129);
    }

    @Test
    public void testCopySignByte() {
        assertEquals((byte)1, MathUtils.copySign((byte)1, (byte)1));
        assertEquals((byte)-1, MathUtils.copySign((byte)-1, (byte)1));
        assertEquals((byte)-1, MathUtils.copySign((byte)1, (byte)-1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortThrow() {
        MathUtils.copySign((short)1, (short)-32769);
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short)1, MathUtils.copySign((short)1, (short)1));
        assertEquals((short)-1, MathUtils.copySign((short)-1, (short)1));
        assertEquals((short)-1, MathUtils.copySign((short)1, (short)-1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntThrow() {
        MathUtils.copySign(1, -2147483649);
    }

    @Test
    public void testCopySignInt() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongThrow() {
        MathUtils.copySign(1L, -9223372036854775808L);
    }

    @Test
    public void testCopySignLong() {
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(-1L, MathUtils.copySign(-1L, 1L));
        assertEquals(-1L, MathUtils.copySign(1L, -1L));
    }

    @Test
    public void testCheckFinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);
        double[] valuesFinite = {1.1, 2.2, 3.3};
        MathUtils.checkFinite(valuesFinite);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayElementNaN() {
        double[] valuesNaN = {1.1, Double.NaN, 3.3};
        MathUtils.checkFinite(valuesNaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayElementInfinite() {
        double[] valuesInfinite = {1.1, Double.POSITIVE_INFINITY, 3.3};
        MathUtils.checkFinite(valuesInfinite);
    }

    @Test
    public void testCheckNotNull() {
        MathUtils.checkNotNull(new Object());
        MathUtils.checkNotNull("testString");
        MathUtils.checkNotNull(new double[] {1.1, 2.2, 3.3});
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNull() {
        MathUtils.checkNotNull(null);
    }
}