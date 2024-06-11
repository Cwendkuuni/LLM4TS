package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(123.4, MathUtils.hash(123.4), 0);
        assertEquals(-123.4, MathUtils.hash(-123.4), 0);
        assertEquals(Double.NaN, MathUtils.hash(Double.NaN), 0);
        assertEquals(Double.POSITIVE_INFINITY, MathUtils.hash(Double.POSITIVE_INFINITY), 0);
        assertEquals(Double.NEGATIVE_INFINITY, MathUtils.hash(Double.NEGATIVE_INFINITY), 0);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = new double[]{1.1, 2.2, 3.3};
        long expectedHash = Arrays.hashCode(values);
        assertEquals(expectedHash, MathUtils.hash(values), 0);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = MathUtils.normalizeAngle(Math.PI / 2 + Math.PI, Math.PI);
        assertEquals(Math.PI / 2, angle, 0.0001);

        angle = MathUtils.normalizeAngle(3 * Math.PI / 2, Math.PI);
        assertEquals(-Math.PI / 2, angle, 0.0001);

        angle = MathUtils.normalizeAngle(-Math.PI / 2, Math.PI);
        assertEquals(Math.PI / 2, angle, 0.0001);

        angle = MathUtils.normalizeAngle(-3 * Math.PI / 2, Math.PI);
        assertEquals(-Math.PI / 2, angle, 0.0001);

        angle = MathUtils.normalizeAngle(Math.PI + 3 * Math.PI / 2, Math.PI);
        assertEquals(Math.PI / 2, angle, 0.0001);

        angle = MathUtils.normalizeAngle(-(Math.PI / 2 + 3 * Math.PI / 2), Math.PI);
        assertEquals(-Math.PI / 2, angle, 0.0001);
    }

    @Test
    public void testReduce() {
        double value = MathUtils.reduce(1.5 * Math.PI, 2 * Math.PI, Math.PI / 4);
        assertEquals(-Math.PI / 4, value, 0.0001);

        value = MathUtils.reduce(3 * Math.PI / 2, 2 * Math.PI, Math.PI / 4);
        assertEquals(Math.PI / 4, value, 0.0001);

        value = MathUtils.reduce(-1.5 * Math.PI, 2 * Math.PI, Math.PI / 4);
        assertEquals(-Math.PI / 4, value, 0.0001);

        value = MathUtils.reduce(-3 * Math.PI / 2, 2 * Math.PI, Math.PI / 4);
        assertEquals(-Math.PI / 4, value, 0.0001);

        value = MathUtils.reduce(1.5 * Math.PI, 2 * Math.PI, -Math.PI / 4);
        assertEquals(Math.PI / 4, value, 0.0001);

        value = MathUtils.reduce(-1.5 * Math.PI, 2 * Math.PI, -Math.PI / 4);
        assertEquals(-Math.PI / 4, value, 0.0001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = -1;

        try {
            byte result = MathUtils.copySign(magnitude, sign);
            assertEquals(-5, result);
        } catch (MathArithmeticException e) {
            // Expected
        }

        magnitude = 127;
        sign = -1;

        try {
            byte result = MathUtils.copySign(magnitude, sign);
            assertEquals(-127, result);
        } catch (MathArithmeticException e) {
            // Expected
        }

        magnitude = 1;
        sign = 1;

        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(1, result);

        magnitude = -1;
        sign = 1;

        result = MathUtils.copySign(magnitude, sign);
        assertEquals(-1, result);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 32767;
        short sign = -1;

        try {
            short result = MathUtils.copySign(magnitude, sign);
            assertEquals(-32767, result);
        } catch (MathArithmeticException e) {
            // Expected
        }

        magnitude = 1;
        sign = 1;

        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(1, result);

        magnitude = -1;
        sign = 1;

        result = MathUtils.copySign(magnitude, sign);
        assertEquals(-1, result);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;

        try {
            int result = MathUtils.copySign(magnitude, sign);
            fail("MathArithmeticException expected");
        } catch (MathArithmeticException e) {
            // Expected
        }

        magnitude = 1;
        sign = 1;

        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(1, result);

        magnitude = -1;
        sign = 1;

        result = MathUtils.copySign(magnitude, sign);
        assertEquals(-1, result);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;

        try {
            long result = MathUtils.copySign(magnitude, sign);
            fail("MathArithmeticException expected");
        } catch (MathArithmeticException e) {
            // Expected
        }

        magnitude = 1;
        sign = 1;

        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(1, result);

        magnitude = -1;
        sign = 1;

        result = MathUtils.copySign(magnitude, sign);
        assertEquals(-1, result);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArray() {
        double[] values = new double[]{Double.NaN, 1.1, 2.2, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testCheckNotNullObjectPattern() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, "Expected object not null", new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectPatternNull() {
        Object obj = null;
        MathUtils.checkNotNull(obj, "Expected object not null", new Object[0]);
    }
}