package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(123.0d.hashCode(), MathUtils.hash(123.0d));
        assertEquals(-123.0d.hashCode(), MathUtils.hash(-123.0d));
        assertEquals(Double.NaN.hashCode(), MathUtils.hash(Double.NaN));
        assertEquals(Double.POSITIVE_INFINITY.hashCode(), MathUtils.hash(Double.POSITIVE_INFINITY));
        assertEquals(Double.NEGATIVE_INFINITY.hashCode(), MathUtils.hash(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.0, 2.0, 3.0, 4.0};
        int hashCode = Arrays.hashCode(values);
        assertEquals(hashCode, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.0, MathUtils.normalizeAngle(0.0, 0.0), 1e-10);
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(1.5707963267948966, 0.0), 1e-10);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(3.141592653589793, 0.0), 1e-10);
        assertEquals(-1.5707963267948966, MathUtils.normalizeAngle(-1.5707963267948966, 0.0), 1e-10);
        assertEquals(-3.141592653589793, MathUtils.normalizeAngle(-3.141592653589793, 0.0), 1e-10);
        assertEquals(3.7123937624883713, MathUtils.normalizeAngle(3.7123937624883713, 3.141592653589793), 1e-10);
        assertEquals(0.8726646280948772, MathUtils.normalizeAngle(0.8726646280948772, 6.283185307179586), 1e-10);
    }

    @Test
    public void testReduce() {
        assertEquals(0.0, MathUtils.reduce(0.0, 1.0, 0.0), 1e-10);
        assertEquals(-1.0, MathUtils.reduce(1.0, 1.0, 0.0), 1e-10);
        assertEquals(-1.0, MathUtils.reduce(1.0, 1.0, 1.0), 1e-10);
        assertEquals(1.0, MathUtils.reduce(-1.0, 1.0, 0.0), 1e-10);
        assertEquals(1.0, MathUtils.reduce(-1.0, 1.0, 1.0), 1e-10);
        assertEquals(2.582634064542357, MathUtils.reduce(6.582634064542357, 2.0 * MathUtils.TWO_PI, 1.0), 1e-10);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByte() {
        MathUtils.copySign((byte)1, (byte)-1);
    }

    @Test
    public void testCopySignByteSuccess() {
        assertEquals((byte)1, MathUtils.copySign((byte)1, (byte)1));
        assertEquals((byte)-1, MathUtils.copySign((byte)-1, (byte)1));
        assertEquals((byte)-1, MathUtils.copySign((byte)1, (byte)-1));
        assertEquals((byte)1, MathUtils.copySign((byte)-1, (byte)-1));
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short)1, MathUtils.copySign((short)1, (short)1));
        assertEquals((short)-1, MathUtils.copySign((short)-1, (short)1));
        assertEquals((short)-1, MathUtils.copySign((short)1, (short)-1));
        assertEquals((short)1, MathUtils.copySign((short)-1, (short)-1));
    }

    @Test
    public void testCopySignInt() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(1, MathUtils.copySign(-1, -1));
    }

    @Test
    public void testCopySignLong() {
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(-1L, MathUtils.copySign(-1L, 1L));
        assertEquals(-1L, MathUtils.copySign(1L, -1L));
        assertEquals(1L, MathUtils.copySign(-1L, -1L));
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(Double.NaN);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.0, 2.0, 3.0, 4.0};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, "Objects should not be null");
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}