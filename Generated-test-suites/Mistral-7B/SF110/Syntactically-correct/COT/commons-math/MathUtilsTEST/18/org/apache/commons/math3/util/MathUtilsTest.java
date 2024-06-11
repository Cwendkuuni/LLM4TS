package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = new Double(value).hashCode();
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = Arrays.hashCode(values);
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double input = 3.14;
        double expected = 0.7853981633974483;
        double actual = MathUtils.normalizeAngle(input, 0);
        assertEquals(expected, actual, 0.0001);

        input = 6.28;
        expected = 0.1392636722835054;
        actual = MathUtils.normalizeAngle(input, 0);
        assertEquals(expected, actual, 0.0001);

        input = -3.14;
        expected = -0.7853981633974483;
        actual = MathUtils.normalizeAngle(input, 0);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleInvalidInput() {
        double input = Double.POSITIVE_INFINITY;
        MathUtils.normalizeAngle(input, 0);
    }

    @Test
    public void testReduce() {
        double input = 1.5;
        double period = 2.0;
        double offset = 1.0;
        double expected = 0.5;
        double actual = MathUtils.reduce(input, period, offset);
        assertEquals(expected, actual, 0.0001);

        input = 3.0;
        period = 2.0;
        offset = 1.0;
        expected = 1.0;
        actual = MathUtils.reduce(input, period, offset);
        assertEquals(expected, actual, 0.0001);

        input = 4.5;
        period = 2.0;
        offset = 1.5;
        expected = 0.5;
        actual = MathUtils.reduce(input, period, offset);
        assertEquals(expected, actual, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReduceInvalidPeriod() {
        double input = 1.0;
        double period = 0.0;
        MathUtils.reduce(input, period, 0.0);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -1;
        sign = 1;
        expected = -1;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -1;
        sign = -1;
        expected = -1;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 127;
        sign = 1;
        expected = 127;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -128;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 32765;
        short sign = 1;
        short expected = 32765;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -32766;
        sign = 1;
        expected = -32766;
        actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -32768;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 100;
        int sign = 1;
        int expected = 100;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = -2147483647;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 9223372036854775805L;
        long sign = 1;
        long expected = 9223372036854775805L;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);

        magnitude = 9223372036854775808L;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.2;
        MathUtils.checkFinite(value);

        value = Double.POSITIVE_INFINITY;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(value));

        value = Double.NaN;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(value));
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(values);

        values[0] = Double.POSITIVE_INFINITY;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(values));

        values[1] = Double.NaN;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(values));
    }

    @Test
    public void testCheckNotNullNullObject() {
        Object obj = null;
        String pattern = LocalizedFormats.OBJECT_NULL;
        MathUtils.checkNotNull(obj);
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(obj, pattern));
    }

    @Test
    public void testCheckNotNullNullArray() {
        Object[] obj = null;
        String pattern = LocalizedFormats.ARRAY_NULL;
        MathUtils.checkNotNull(obj);
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(obj, pattern));
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
        MathUtils.checkNotNull(obj, LocalizedFormats.OBJECT_NULL);
    }

    @Test
    public void testCheckNotNullNotNullArray() {
        Object[] obj = new Object[3];
        MathUtils.checkNotNull(obj);
        MathUtils.checkNotNull(obj, LocalizedFormats.ARRAY_NULL);
    }
}