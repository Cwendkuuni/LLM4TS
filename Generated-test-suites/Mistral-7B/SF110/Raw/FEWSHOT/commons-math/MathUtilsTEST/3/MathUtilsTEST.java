package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(13, MathUtils.hash(1.0));
        assertEquals(41, MathUtils.hash(1.23));
        assertEquals(-19, MathUtils.hash(-1.0));
        assertEquals(-37, MathUtils.hash(-1.23));
    }

    @Test
    public void testHashDoubleArray() {
        double[] arr = {1.0, 1.23, 2.0, 2.3};
        assertEquals(1460388320123399637L, MathUtils.hash(arr));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(0.0), 0.0001);
        assertEquals(-3.141592653589793, MathUtils.normalizeAngle(6.5), 0.0001);
        assertEquals(2.0943951023931953, MathUtils.normalizeAngle(360.1), 0.0001);
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(315.0), 0.0001);
        assertEquals(0.0, MathUtils.normalizeAngle(0.0), 0.0001);
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(540.0), 0.0001);
        assertEquals(2.0943951023931953, MathUtils.normalizeAngle(720.0), 0.0001);
        assertThrows(IllegalArgumentException.class, () -> MathUtils.normalizeAngle(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> MathUtils.normalizeAngle(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> MathUtils.normalizeAngle(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testReduce() {
        assertEquals(1.23, MathUtils.reduce(1.45, 2.0, 0.0), 0.001);
        assertEquals(0.78, MathUtils.reduce(1.57, 2.0, 0.5), 0.001);
        assertEquals(-1.23, MathUtils.reduce(1.45, 2.0, 3.6), 0.001);
        assertEquals(2.23, MathUtils.reduce(3.45, 2.0, 1.2), 0.001);
        assertEquals(3.78, MathUtils.reduce(5.0, 2.0, 3.2), 0.001);
        assertThrows(IllegalArgumentException.class, () -> MathUtils.reduce(Double.NaN, 2.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> MathUtils.reduce(1.0, 0.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> MathUtils.reduce(1.0, 2.0, Double.NaN));
    }

    @Test
    public void testCopySignByte() {
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) 1));
        assertEquals((byte) -1, MathUtils.copySign((byte) -1, (byte) 1));
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) -1));
        assertEquals((byte) -1, MathUtils.copySign((byte) -1, (byte) -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((byte) 1, (byte) 0));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((byte) -1, (byte) 0));
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) 1));
        assertEquals((short) -1, MathUtils.copySign((short) -1, (short) 1));
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) -1));
        assertEquals((short) -1, MathUtils.copySign((short) -1, (short) -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((short) 1, (short) 0));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((short) -1, (short) 0));
    }

    @Test
    public void testCopySignInt() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(1, MathUtils.copySign(1, -1));
        assertEquals(-1, MathUtils.copySign(-1, -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(1, 0));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(-1, 0));
    }

    @Test
    public void testCopySignLong() {
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(-1L, MathUtils.copySign(-1L, 1L));
        assertEquals(1L, MathUtils.copySign(1L, -1L));
        assertEquals(-1L, MathUtils.copySign(-1L, -1L));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(1L, 0L));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(-1L, 0L));
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(Double.MAX_VALUE);
        MathUtils.checkFinite(Double.MIN_VALUE);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.longBitsToDouble(Long.MIN_VALUE)));
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] arr = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(arr);
        arr[1] = Double.NaN;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(arr));
    }

    @Test
    public void testCheckNotNullObject() {
        MathUtils.checkNotNull(new Object());
        MathUtils.checkNotNull(new String("Test"));
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(null));
    }

    @Test
    public void testCheckNotNullArray() {
        double[] arr = {1.0, 2.0, 3.0};
        MathUtils.checkNotNull(arr);
        MathUtils.checkNotNull(Arrays.copyOfRange(arr, 1, 3));
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(null));
    }
}