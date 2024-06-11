package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = 1315686233; // hashcode for 1.23
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = {1.23, 2.46, 3.78};
        long expected = 1432321285L; // hashcode for {1.23, 2.46, 3.78}
        long actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 10.5;
        double center = 0.0;
        double expected = 0.5;
        double actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.001);

        a = 180.5;
        expected = -5.999;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.001);

        a = -180.5;
        expected = 5.999;
        actual = MathUtils.normalizeAngle(a, center);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceInfiniteInput() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceNaNInput() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test
    public void testReduce() {
        double a = 10.5;
        double period = 2 * MathUtils.TWO_PI;
        double offset = 3.141592653589793;
        double expected = 1.5707963267948966;
        double actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.001);

        a = 10.5 + MathUtils.TWO_PI;
        expected = -3.141592653589793;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.001);

        a = 10.5 - MathUtils.TWO_PI;
        expected = 4.712388980380089;
        actual = MathUtils.reduce(a, period, offset);
        assertEquals(expected, actual, 0.001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeSignPositiveMagnitude() {
        MathUtils.checkFinite((byte)127);
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
        fail("Expected MathArithmeticException");
    }

    @Test
    public void testCopySignByteValid() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -5;
        sign = 1;
        expected = 5;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 5;
        sign = -1;
        expected = -5;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -5;
        sign = -1;
        expected = -5;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = Byte.MIN_VALUE;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
        fail("Expected MathArithmeticException");
    }

    @Test
    public void testCopySignShortValid() {
        short magnitude = Short.MIN_VALUE;
        short sign = 1;
        short expected = Short.MIN_VALUE;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -Short.MIN_VALUE;
        sign = 1;
        expected = -Short.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Short.MAX_VALUE;
        sign = 1;
        expected = Short.MAX_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 0;
        sign = -1;
        expected = 0;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = Short.MIN_VALUE;
        short sign = 0;
        MathUtils.copySign(magnitude, sign);
        fail("Expected MathArithmeticException");
    }

    @Test
    public void testCopySignIntValid() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        int expected = Integer.MIN_VALUE;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -Integer.MIN_VALUE;
        sign = 1;
        expected = -Integer.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Integer.MAX_VALUE;
        sign = 1;
        expected = Integer.MAX_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 0;
        sign = -1;
        expected = 0;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 0;
        MathUtils.copySign(magnitude, sign);
        fail("Expected MathArithmeticException");
    }

    @Test
    public void testCopySignLongValid() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        long expected = Long.MIN_VALUE;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -Long.MIN_VALUE;
        sign = 1;
        expected = -Long.MIN_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = Long.MAX_VALUE;
        sign = 1;
        expected = Long.MAX_VALUE;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 0;
        sign = -1;
        expected = 0;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 0;
        MathUtils.copySign(magnitude, sign);
        fail("Expected MathArithmeticException");
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.23;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        MathUtils.checkFinite(Double.NaN);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testCheckFiniteDoubleArrayFinite() {
        double[] values = {1.23, 2.46, 3.78};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {1.23, Double.NaN, 3.78};
        MathUtils.checkFinite(values);
        fail("Expected NotFiniteNumberException for NaN element");
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {1.23, Double.POSITIVE_INFINITY, 3.78};
        MathUtils.checkFinite(values);
        fail("Expected NotFiniteNumberException for infinite element");
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
        fail("Expected NullArgumentException");
    }

    @Test
    public void testCheckNotNullArrayObjects() {
        Object[] objArray = {new Object(), new Object(), new Object()};
        MathUtils.checkNotNull(objArray);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullArrayObjectsNull() {
        Object[] objArray = {new Object(), null, new Object()};
        MathUtils.checkNotNull(objArray);
        fail("Expected NullArgumentException");
    }
}