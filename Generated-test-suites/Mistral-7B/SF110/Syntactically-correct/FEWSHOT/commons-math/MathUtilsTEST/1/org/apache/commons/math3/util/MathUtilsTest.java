package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-123456789L, MathUtils.hash(1.23456789));
        assertEquals(-123456789L, MathUtils.hash(-1.23456789));
    }

    @Test
    public void testHashDoubleArray() {
        double[] input = {1.1, 2.2, 3.3, 4.4};
        assertEquals(-18333333333333223L, MathUtils.hash(input));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(3.141592653589793, 0.0), 0.000001);
        assertEquals(-1.5707963267948966, MathUtils.normalizeAngle(-3.141592653589793, 0.0), 0.000001);
        assertEquals(0.7853981633974483, MathUtils.normalizeAngle(-1.5707963267948966, Math.PI), 0.000001);
    }

    @Test
    public void testReduce() {
        assertEquals(1.1, MathUtils.reduce(1.6, 1.5, 0.1), 0.00001);
        assertEquals(4.5, MathUtils.reduce(5.6, 1.5, 0.1), 0.00001);
        assertEquals(3.0, MathUtils.reduce(3.5, 1.0, 0.0), 0.00001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        assertEquals(5, MathUtils.copySign(magnitude, sign));

        magnitude = -128;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = 5;
        sign = -1;
        assertEquals((byte) -5, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 32767;
        short sign = 1;
        assertEquals(32767, MathUtils.copySign(magnitude, sign));

        magnitude = -32768;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = 32767;
        sign = -1;
        assertEquals((short) -32767, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignInt() {
        int magnitude = Integer.MAX_VALUE;
        int sign = 1;
        assertEquals(Integer.MAX_VALUE, MathUtils.copySign(magnitude, sign));

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = Integer.MAX_VALUE;
        sign = -1;
        assertEquals(-Integer.MAX_VALUE, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignLong() {
        long magnitude = Long.MAX_VALUE;
        long sign = 1;
        assertEquals(Long.MAX_VALUE, MathUtils.copySign(magnitude, sign));

        magnitude = Long.MIN_VALUE;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = Long.MAX_VALUE;
        sign = -1;
        assertEquals(-Long.MAX_VALUE, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(1.1);
        MathUtils.checkFinite(-1.1);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] input = {1.1, 2.2, 3.3, 4.4};
        MathUtils.checkFinite(input);
    }

    @Test
    public void testCheckNotnullObject() {
        MathUtils.checkNotNull(new Object());
    }

    @Test
    public void testCheckNotnullObjectWithMessage() {
        Object obj = null;
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(obj, "Object cannot be null.", new Object[0]));
    }

    @Test
    public void testCheckNotnullObjectWithMessageAndArgs() {
        Object obj = null;
        double[] arr = new double[0];
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(obj, "Array cannot be null.", arr));
    }
}