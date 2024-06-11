package org.apache.commons.math3.util;

import static org.apache.commons.math3.util.MathUtils.TWO_PI;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    // Test hash methods

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expectedHashCode = 42; // arbitrary value for testing
        int actualHashCode = MathUtils.hash(value);
        assertEquals("hash(double) should return expected hash code", expectedHashCode, actualHashCode);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.46, 3.69};
        int expectedHashCode = 12345; // arbitrary value for testing
        int actualHashCode = MathUtils.hash(values);
        assertEquals("hash(double[]) should return expected hash code", expectedHashCode, actualHashCode);
    }

    // Test normalizeAngle method

    @Test
    public void testNormalizeAngle() {
        double angle = 3.1415926;
        double expectedResult = -3.1415926;
        double result = MathUtils.normalizeAngle(angle, 0);
        assertEquals("normalizeAngle(angle, 0) should return expected result", expectedResult, result, 0.001);

        angle = 6.2831853;
        expectedResult = 0;
        result = MathUtils.normalizeAngle(angle, 0);
        assertEquals("normalizeAngle(angle, 0) should return expected result", expectedResult, result, 0.001);

        angle = -3.1415926;
        expectedResult = 3.1415926;
        result = MathUtils.normalizeAngle(angle, 0);
        assertEquals("normalizeAngle(angle, 0) should return expected result", expectedResult, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeAngleInvalidCenter() {
        double angle = Math.PI;
        double center = -Math.PI;
        MathUtils.normalizeAngle(angle, center);
    }

    // Test reduce method

    @Test
    public void testReduce() {
        double a = 1.5708;
        double period = TWO_PI;
        double offset = 0;
        double expectedResult = 0.5708;
        double result = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(a, period, offset) should return expected result", expectedResult, result, 0.001);

        a = 4.7123;
        period = TWO_PI / 2;
        offset = 0;
        expectedResult = 0.7123;
        result = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(a, period, offset) should return expected result", expectedResult, result, 0.001);

        a = -2.0;
        period = Math.PI;
        offset = Math.PI / 2;
        expectedResult = -2.5;
        result = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(a, period, offset) should return expected result", expectedResult, result, 0.001);
    }

    // Test copySign methods

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expectedResult = 5;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(magnitude, sign) should return expected result", expectedResult, result);

        magnitude = -128;
        sign = 1;
        expectedResult = -128;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(magnitude, sign) should throw MathArithmeticException", true, result instanceof MathArithmeticException);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 32767;
        short sign = 1;
        short expectedResult = 32767;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(magnitude, sign) should return expected result", expectedResult, result);

        magnitude = -32768;
        sign = 1;
        expectedResult = -32768;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(magnitude, sign) should throw MathArithmeticException", true, result instanceof MathArithmeticException);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = Integer.MAX_VALUE;
        int sign = 1;
        int expectedResult = Integer.MAX_VALUE;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(magnitude, sign) should return expected result", expectedResult, result);

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        expectedResult = Integer.MIN_VALUE;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(magnitude, sign) should throw MathArithmeticException", true, result instanceof MathArithmeticException);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = Long.MAX_VALUE;
        long sign = 1;
        long expectedResult = Long.MAX_VALUE;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(magnitude, sign) should return expected result", expectedResult, result);

        magnitude = Long.MIN_VALUE;
        sign = 1;
        expectedResult = Long.MIN_VALUE;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(magnitude, sign) should throw MathArithmeticException", true, result instanceof MathArithmeticException);

        magnitude = Long.MIN_VALUE;
        sign = -1;
        expectedResult = Long.MIN_VALUE;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(magnitude, sign) should return expected result", expectedResult, result);
    }

    // Test checkFinite methods

    @Test
    public void testCheckFiniteDouble() {
        double value = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(value);
            fail("checkFinite(Double.POSITIVE_INFINITY) should throw NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // expected
        }

        value = Double.NEGATIVE_INFINITY;
        try {
            MathUtils.checkFinite(value);
            fail("checkFinite(Double.NEGATIVE_INFINITY) should throw NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // expected
        }

        value = Double.NaN;
        try {
            MathUtils.checkFinite(value);
            fail("checkFinite(Double.NaN) should throw NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // expected
        }

        value = 123.45;
        MathUtils.checkFinite(value);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.23, 2.46, 3.69};
        MathUtils.checkFinite(values);

        values[0] = Double.POSITIVE_INFINITY;
        try {
            MathUtils.checkFinite(values);
            fail("checkFinite(values) should throw NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // expected
        }

        values[1] = Double.NEGATIVE_INFINITY;
        try {
            MathUtils.checkFinite(values);
            fail("checkFinite(values) should throw NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // expected
        }

        values[2] = Double.NaN;
        try {
            MathUtils.checkFinite(values);
            fail("checkFinite(values) should throw NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // expected
        }
    }

    // Test checkNotNull methods

    @Test(expected = NullPointerException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, LocalizedFormats.NULL_ARGUMENT, new Object[0]);
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }
}