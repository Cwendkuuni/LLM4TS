package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-1531985275, MathUtils.hash(0.123));
        assertEquals(-1122281729, MathUtils.hash(0.25));
        assertEquals(154323456, MathUtils.hash(3.1415));
        assertEquals(1936371148, MathUtils.hash(Math.E));
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNullArray() {
        MathUtils.checkFinite(null);
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);
        MathUtils.checkFinite(0.0);
        MathUtils.checkFinite(1.23);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteArrayNegativeElement() {
        double[] values = {Double.NEGATIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckFiniteArrayAllFinite() {
        double[] values = {0.1, 2.3, 3.1415};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullNullObject() {
        try {
            MathUtils.checkNotNull(null, "Unexpected null object");
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        MathUtils.checkNotNull(new Object(), "Unexpected null object");
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(0.0), 1e-12);
        assertEquals(6.283185307179586, MathUtils.normalizeAngle(3.141592653589793), 1e-12);
        assertEquals(-3.141592653589793, MathUtils.normalizeAngle(-3.141592653589793), 1e-12);
        assertEquals(6.283185307179586, MathUtils.normalizeAngle(6.283185307179586), 1e-12);
        assertEquals(3.141592653589793, MathUtils.normalizeAngle(6.283185307179586 - 3.141592653589793), 1e-12);
    }

    @Test
    public void testReduce() {
        assertEquals(-1.2, MathUtils.reduce(-1.5, 2.0, -0.3), 1e-12);
        assertEquals(-1.5, MathUtils.reduce(-1.8, 2.0, -0.3), 1e-12);
        assertEquals(-1.8, MathUtils.reduce(-2.1, 2.0, -0.3), 1e-12);
        assertEquals(-2.1, MathUtils.reduce(-2.4, 2.0, -0.3), 1e-12);
        assertEquals(-2.4, MathUtils.reduce(-2.7, 2.0, -0.3), 1e-12);

        assertEquals(1.2, MathUtils.reduce(1.5, 2.0, -0.3), 1e-12);
        assertEquals(1.5, MathUtils.reduce(1.8, 2.0, -0.3), 1e-12);
        assertEquals(1.8, MathUtils.reduce(2.1, 2.0, -0.3), 1e-12);
        assertEquals(2.1, MathUtils.reduce(2.4, 2.0, -0.3), 1e-12);
        assertEquals(2.4, MathUtils.reduce(2.7, 2.0, -0.3), 1e-12);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeMagnitudePositiveSign() {
        byte magnitude = -1;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignBytePositiveMagnitudeNegativeSign() {
        byte magnitude = 1;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignBytePositiveMagnitudePositiveSign() {
        byte magnitude = 1;
        byte sign = 1;
        assertEquals(1, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShortNegativeMagnitudePositiveSign() {
        short magnitude = -1;
        short sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            fail("Unexpected MathArithmeticException");
        }
        assertEquals(-1, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShortPositiveMagnitudePositiveSign() {
        short magnitude = 1;
        short sign = 1;
        assertEquals(1, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortNegativeMagnitudeNegativeSign() {
        short magnitude = -1;
        short sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortPositiveMagnitudeNegativeSign() {
        short magnitude = 1;
        short sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignIntNegativeMagnitudePositiveSign() {
        int magnitude = -1;
        int sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            fail("Unexpected MathArithmeticException");
        }
        assertEquals(-1, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignIntPositiveMagnitudePositiveSign() {
        int magnitude = 1;
        int sign = 1;
        assertEquals(1, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntNegativeMagnitudeNegativeSign() {
        int magnitude = -1;
        int sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntPositiveMagnitudeNegativeSign() {
        int magnitude = 1;
        int sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongNegativeMagnitudePositiveSign() {
        long magnitude = -1L;
        long sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            fail("Unexpected MathArithmeticException");
        }
        assertEquals(-1, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignLongPositiveMagnitudePositiveSign() {
        long magnitude = 1L;
        long sign = 1;
        assertEquals(1, MathUtils.copySign(magnitude, sign));
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongNegativeMagnitudeNegativeSign() {
        long magnitude = -1L;
        long sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongPositiveMagnitudeNegativeSign() {
        long magnitude = 1L;
        long sign = -1;
        MathUtils.copySign(magnitude, sign);
    }
}