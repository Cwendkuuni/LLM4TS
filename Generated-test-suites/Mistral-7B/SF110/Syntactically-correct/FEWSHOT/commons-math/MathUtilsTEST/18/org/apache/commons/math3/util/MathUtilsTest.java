package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-1505541911, MathUtils.hash(1.2));
        assertEquals(-1125284558, MathUtils.hash(Double.NaN));
        assertEquals(-1215412383, MathUtils.hash(Double.POSITIVE_INFINITY));
        assertEquals(-853993456, MathUtils.hash(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testHashDoubleArray() {
        double[] testArray = {1.2, 2.3, Double.NaN, 3.4, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        assertEquals(11180259316461047598L, MathUtils.hash(testArray));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(3.141592653589793, 0.0), 0.0000001);
        assertEquals(2.0943951023931955, MathUtils.normalizeAngle(5.0, 0.0), 0.0000001);
        assertEquals(6.283185307179586, MathUtils.normalizeAngle(7.0, 3.141592653589793), 0.0000001);
        assertEquals(-1.5707963267948966, MathUtils.normalizeAngle(-3.141592653589793, 0.0), 0.0000001);
        assertEquals(-2.0943951023931955, MathUtils.normalizeAngle(-5.0, 0.0), 0.0000001);
        assertEquals(-6.283185307179586, MathUtils.normalizeAngle(-7.0, 3.141592653589793), 0.0000001);
    }

    @Test
    public void testReduce() {
        assertEquals(0.5, MathUtils.reduce(1.5, 1.0, 0.5), 0.0000001);
        assertEquals(-0.5, MathUtils.reduce(1.5, 1.0, -0.5), 0.0000001);
        assertEquals(1.5, MathUtils.reduce(2.5, 1.0, 0.5), 0.0000001);
        assertEquals(-1.5, MathUtils.reduce(2.5, 1.0, -0.5), 0.0000001);
        assertEquals(0.0, MathUtils.reduce(1.5, 1.0, 1.0), 0.0000001);
        assertEquals(1.5, MathUtils.reduce(1.5, 1.0, 0.0), 0.0000001);
        assertEquals(-1.5, MathUtils.reduce(1.5, 1.0, -1.5), 0.0000001);
    }

    @Test
    public void testCopySignByte() {
        assertEquals((byte)1, MathUtils.copySign((byte)1, (byte)1));
        assertEquals((byte)-1, MathUtils.copySign((byte)1, (byte)-1));
        assertEquals((byte)-1, MathUtils.copySign((byte)-1, (byte)1));
        assertEquals((byte)1, MathUtils.copySign((byte)-1, (byte)-1));
        try {
            MathUtils.copySign((byte)128, (byte)1);
            fail();
        } catch (MathArithmeticException e) {
            //expected
        }
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short)1, MathUtils.copySign((short)1, (short)1));
        assertEquals((short)-1, MathUtils.copySign((short)1, (short)-1));
        assertEquals((short)-1, MathUtils.copySign((short)-1, (short)1));
        assertEquals((short)1, MathUtils.copySign((short)-1, (short)-1));
        try {
            MathUtils.copySign((short)32767, (short)1);
            fail();
        } catch (MathArithmeticException e) {
            //expected
        }
    }

    @Test
    public void testCopySignInt() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(1, MathUtils.copySign(-1, -1));
        try {
            MathUtils.copySign(Integer.MAX_VALUE, 1);
            fail();
        } catch (MathArithmeticException e) {
            //expected
        }
    }

    @Test
    public void testCopySignLong() {
        assertEquals(1L, MathUtils.copySign(1L, 1L));
        assertEquals(-1L, MathUtils.copySign(1L, -1L));
        assertEquals(-1L, MathUtils.copySign(-1L, 1L));
        assertEquals(1L, MathUtils.copySign(-1L, -1L));
        try {
            MathUtils.copySign(Long.MAX_VALUE, 1L);
            fail();
        } catch (MathArithmeticException e) {
            //expected
        }
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(Double.NaN);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] testArray = {1.2, 2.3, Double.NaN, 3.4, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        try {
            MathUtils.checkFinite(testArray);
            fail();
        } catch (NotFiniteNumberException e) {
            //expected
        }
    }

    @Test
    public void testCheckNotNullObject() {
        MathUtils.checkNotNull(new Object(), LocalizedFormats.NULL_ARGUMENT, "o");
        MathUtils.checkNotNull(new Object());
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        MathUtils.checkNotNull(null, LocalizedFormats.NULL_ARGUMENT, "o");
    }
}