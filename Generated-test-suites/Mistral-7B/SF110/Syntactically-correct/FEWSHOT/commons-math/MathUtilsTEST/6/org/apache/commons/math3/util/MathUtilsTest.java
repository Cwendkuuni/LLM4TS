package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(1312934529, MathUtils.hash(0.12345));
        assertEquals(1527342357, MathUtils.hash(Math.PI));
        assertEquals(1234567890, MathUtils.hash(123.456));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        long expected = 1234567890L;
        assertEquals(expected, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.toRadians(350.0);
        double center = Math.toRadians(180.0);
        double normalizedAngle = MathUtils.normalizeAngle(angle, center);
        assertEquals(Math.toRadians(70.0), normalizedAngle);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithNaN() {
        double angle = Double.NaN;
        double center = Math.toRadians(180.0);
        MathUtils.normalizeAngle(angle, center);
    }

    @Test
    public void testReduce() {
        double a = Math.PI * 2.0;
        double period = Math.PI;
        double offset = 0.0;
        double reduced = MathUtils.reduce(a, period, offset);
        assertEquals(0.0, reduced, 0.001);

        a = Math.PI * 1.5;
        period = Math.PI;
        offset = 0.0;
        reduced = MathUtils.reduce(a, period, offset);
        assertEquals(Math.PI / 2.0, reduced, 0.001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);

        magnitude = -127;
        sign = -1;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);

        magnitude = 1;
        sign = -1;
        try {
            result = MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 30000;
        short sign = 1;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);

        magnitude = -32767;
        sign = -1;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);

        magnitude = 1;
        sign = -1;
        try {
            result = MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 1000000;
        int sign = 1;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        try {
            result = MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // expected
        }

        magnitude = 1;
        sign = -1;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals(-magnitude, result);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 1000000000L;
        long sign = 1;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(magnitude, result);

        magnitude = Long.MIN_VALUE;
        sign = 1;
        try {
            result = MathUtils.copySign(magnitude, sign);
            fail();
        } catch (MathArithmeticException e) {
            // expected
        }

        magnitude = 1;
        sign = -1;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals(-magnitude, result);
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 123.456;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {1.1, Double.NaN, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, "obj is null");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object obj = null;
        MathUtils.checkNotNull(obj, "obj is null");
    }

    @Test
    public void testCheckNotNullSimple() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullSimpleNull() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }
}