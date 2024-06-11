package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-1963178827, MathUtils.hash(0.0));
        assertEquals(-1383711479, MathUtils.hash(1.0));
        assertEquals(-736256665, MathUtils.hash(2.0));
        assertEquals(-1773523295, MathUtils.hash(-1.0));
        assertEquals(-1530021159, MathUtils.hash(-2.0));

        double value = Double.MAX_VALUE;
        assertEquals(-8539934523966563665L, MathUtils.hash(value));

        value = Double.MIN_VALUE;
        assertEquals(-8539934523966563665L, MathUtils.hash(value));

        value = Double.NaN;
        assertNotSame(-8539934523966563665L, MathUtils.hash(value));

        value = Double.POSITIVE_INFINITY;
        assertNotSame(-8539934523966563665L, MathUtils.hash(value));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {};
        assertEquals(0, MathUtils.hash(values));

        values = new double[]{0.0, 1.0, 2.0, -1.0, -2.0};
        long expectedHashCode = 1382124208L; // calculated using Java's Arrays.hashCode()
        assertEquals(expectedHashCode, MathUtils.hash(values));

        values = new double[]{Double.MAX_VALUE, Double.MIN_VALUE, Double.NaN, Double.POSITIVE_INFINITY};
        assertNotSame(expectedHashCode, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.1, MathUtils.normalizeAngle(1.6, 0.1), 0.001);
        assertEquals(5.7, MathUtils.normalizeAngle(5.9, 0.1), 0.001);
        assertEquals(7.9, MathUtils.normalizeAngle(7.9, 0.1), 0.001);
        assertEquals(0.1, MathUtils.normalizeAngle(0.6, 0.1), 0.001);
        assertEquals(6.283185307, MathUtils.normalizeAngle(6.283185307, 0.1), 0.001);
        assertEquals(MathUtils.TWO_PI - 0.1, MathUtils.normalizeAngle(MathUtils.TWO_PI - 0.1, 0.1), 0.001);
        assertEquals(-1.1, MathUtils.normalizeAngle(-1.6, 0.1), 0.001);
        assertEquals(-5.7, MathUtils.normalizeAngle(-5.9, 0.1), 0.001);
        assertEquals(-7.9, MathUtils.normalizeAngle(-7.9, 0.1), 0.001);
        assertEquals(-0.1, MathUtils.normalizeAngle(-0.6, 0.1), 0.001);
        assertEquals(-MathUtils.TWO_PI + 0.1, MathUtils.normalizeAngle(-MathUtils.TWO_PI + 0.1, 0.1), 0.001);
    }

    @Test
    public void testReduce() {
        assertEquals(0.4, MathUtils.reduce(0.6, 1.0, 0.2), 0.001);
        assertEquals(5.7, MathUtils.reduce(5.9, 1.0, 0.2), 0.001);
        assertEquals(7.9, MathUtils.reduce(7.9, 1.0, 0.2), 0.001);
        assertEquals(-0.4, MathUtils.reduce(-0.6, 1.0, 0.2), 0.001);
        assertEquals(-5.7, MathUtils.reduce(-5.9, 1.0, 0.2), 0.001);
        assertEquals(-7.9, MathUtils.reduce(-7.9, 1.0, 0.2), 0.001);
        assertEquals(0.6, MathUtils.reduce(0.6, 1.0, 0.0), 0.001);
        assertEquals(5.9, MathUtils.reduce(5.9, 1.0, 0.0), 0.001);
        assertEquals(7.9, MathUtils.reduce(7.9, 1.0, 0.0), 0.001);
        assertEquals(-0.6, MathUtils.reduce(-0.6, 1.0, 0.0), 0.001);
        assertEquals(-5.9, MathUtils.reduce(-5.9, 1.0, 0.0), 0.001);
        assertEquals(-7.9, MathUtils.reduce(-7.9, 1.0, 0.0), 0.001);
    }

    @Test
    public void testCopySignByte() {
        assertEquals((byte)3, MathUtils.copySign((byte)3, (byte)1));
        assertEquals((byte)-3, MathUtils.copySign((byte)3, (byte)-1));
        assertEquals((byte)127, MathUtils.copySign((byte)-128, (byte)1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((byte)-128, (byte)1));
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short)3, MathUtils.copySign((short)3, (short)1));
        assertEquals((short)-3, MathUtils.copySign((short)3, (short)-1));
        assertEquals((short)32767, MathUtils.copySign((short)-32768, (short)1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((short)-32768, (short)1));
    }

    @Test
    public void testCopySignInt() {
        assertEquals(3, MathUtils.copySign(3, 1));
        assertEquals(-3, MathUtils.copySign(3, -1));
        assertEquals(Integer.MIN_VALUE, MathUtils.copySign(Integer.MIN_VALUE, 1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(Integer.MIN_VALUE, 1));
    }

    @Test
    public void testCopySignLong() {
        assertEquals(3, MathUtils.copySign(3L, 1L));
        assertEquals(-3, MathUtils.copySign(3L, -1L));
        assertEquals(Long.MIN_VALUE, MathUtils.copySign(Long.MIN_VALUE, 1L));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(Long.MIN_VALUE, 1L));
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(2.0);
        MathUtils.checkFinite(-1.0);
        MathUtils.checkFinite(-2.0);

        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.NaN));
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.POSITIVE_INFINITY));
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {};
        MathUtils.checkFinite(values);

        values = new double[]{0.0, 1.0, 2.0, -1.0, -2.0};
        MathUtils.checkFinite(values);

        values = new double[]{Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        assertArrayEquals(new String[]{"Array element #0", "Array element #1", "Array element #2"},
                MathUtils.getMessages(MathUtils.checkFinite(values)));
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);

        obj = null;
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(obj, "Null Object"));
    }

    @Test
    public void testCheckNotNullObjectPattern() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, "Null Object {}", new Object[0]);

        obj = null;
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(obj, "Null Object {0}", new Object[]{"Test"}));
    }
}