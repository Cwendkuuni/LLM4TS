package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-1677721029, MathUtils.hash(1.0));
        assertEquals(-1488913424, MathUtils.hash(2.5));
        assertEquals(-918305331, MathUtils.hash(3.141592653589793));
        assertEquals(-1526325015, MathUtils.hash(-1.0));
        assertEquals(-1645549665, MathUtils.hash(-2.5));
        assertEquals(-382331176, MathUtils.hash(-3.141592653589793));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.0, 2.5, 3.141592653589793, -1.0, -2.5, -3.141592653589793};
        assertEquals(1637739543, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(0.421883455543721, MathUtils.normalizeAngle(0.421883455543721, 0), 0.0001);
        assertEquals(6.283185307179586, MathUtils.normalizeAngle(6.283185307179586, 0), 0.0001);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(3.141592653589793, 3.141592653589793), 0.0001);
        assertEquals(3.2169263201134135, MathUtils.normalizeAngle(3.2169263201134135, 3.141592653589793), 0.0001);
        assertEquals(2.131496063495576, MathUtils.normalizeAngle(2.131496063495576, 3.6), 0.0001);
        assertEquals(-2.131496063495576, MathUtils.normalizeAngle(-2.131496063495576, 3.6), 0.0001);
        assertEquals(-3.7829016628066705, MathUtils.normalizeAngle(-3.7829016628066705, 3.6), 0.0001);
        assertEquals(3.7829016628066705, MathUtils.normalizeAngle(3.7829016628066705, 3.6), 0.0001);
    }

    @Test
    public void testReduce() {
        assertEquals(1.2, MathUtils.reduce(1.6, 1.2, 0), 0.0001);
        assertEquals(-0.2, MathUtils.reduce(-1.2, 1.2, 0), 0.0001);
        assertEquals(0.2, MathUtils.reduce(0, 1.2, 0.6), 0.0001);
        assertEquals(2.4, MathUtils.reduce(3.6, 1.2, 0.6), 0.0001);
        assertEquals(-2.4, MathUtils.reduce(-3.6, 1.2, 0.6), 0.0001);
        assertEquals(1.2, MathUtils.reduce(1.6, 2.4, 0), 0.0001);
        assertEquals(1.2, MathUtils.reduce(1.6, 2.4, 1.2), 0.0001);
        assertEquals(1.2, MathUtils.reduce(1.6, 2.4, 3.6), 0.0001);
        assertEquals(-1.2, MathUtils.reduce(-1.6, 2.4, 0), 0.0001);
        assertEquals(-1.2, MathUtils.reduce(-1.6, 2.4, 1.2), 0.0001);
        assertEquals(-1.2, MathUtils.reduce(-1.6, 2.4, 3.6), 0.0001);
    }

    @Test
    public void testCopySignByte() {
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) 1));
        assertEquals((byte) -1, MathUtils.copySign((byte) -1, (byte) 1));
        assertEquals((byte) -1, MathUtils.copySign((byte) 1, (byte) -1));
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((byte) -128, (byte) 1));
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) 1));
        assertEquals((short) -1, MathUtils.copySign((short) -1, (short) 1));
        assertEquals((short) -1, MathUtils.copySign((short) 1, (short) -1));
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((short) -32768, (short) 1));
    }

    @Test
    public void testCopySignInt() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(1, MathUtils.copySign(1, -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(-Integer.MIN_VALUE, 1));
    }

    @Test
    public void testCopySignLong() {
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(-1L, MathUtils.copySign(-1L, 1L));
        assertEquals(-1L, MathUtils.copySign(1L, -1L));
        assertEquals(1L, MathUtils.copySign(1L, -1L));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(-Long.MIN_VALUE, 1L));
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.NaN));
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, LocalizedFormats.NULL_ARGUMENT, "object");
    }

    @Test
    public void testCheckNotNullObjectNull() {
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(null, LocalizedFormats.NULL_ARGUMENT, "object"));
    }
}