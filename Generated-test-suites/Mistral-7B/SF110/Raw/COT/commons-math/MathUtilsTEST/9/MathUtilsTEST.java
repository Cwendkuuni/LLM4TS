package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedHash = 11; // arbitrary value for testing
        int result = MathUtils.hash(value);
        assertEquals("hash of double value should match expected value", expectedHash, result);
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = {1.2, 2.3, 3.4};
        int expectedHash = 123; // arbitrary value for testing
        int result = MathUtils.hash(value);
        assertEquals("hash of double array should match expected value", expectedHash, result);
    }

    @Test
    public void testNormalizeAngle() {
        double result = MathUtils.normalizeAngle(3.141592653589793, 0.0);
        double expectedResult = 1.141592653589793;
        assertEquals("normalizeAngle should return expected value for given arguments", expectedResult, result);

        result = MathUtils.normalizeAngle(6.283185307179586, 3.141592653589793);
        expectedResult = 0.0;
        assertEquals("normalizeAngle should return expected value for given arguments", expectedResult, result);

        result = MathUtils.normalizeAngle(-3.141592653589793, 0.0);
        expectedResult = -3.141592653589793;
        assertEquals("normalizeAngle should return expected value for given arguments", expectedResult, result);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithInvalidArgument() {
        MathUtils.normalizeAngle(Double.POSITIVE_INFINITY, 0.0);
    }

    @Test
    public void testReduce() {
        double result = MathUtils.reduce(1.2, 3.141592653589793, 0.0);
        double expectedResult = -1.931592653589793;
        assertEquals("reduce should return expected value for given arguments", expectedResult, result);

        result = MathUtils.reduce(-1.2, 3.141592653589793, 0.0);
        expectedResult = 1.931592653589793;
        assertEquals("reduce should return expected value for given arguments", expectedResult, result);

        result = MathUtils.reduce(1.2, -3.141592653589793, 0.0);
        expectedResult = -1.931592653589793;
        assertEquals("reduce should return expected value for given arguments", expectedResult, result);

        result = MathUtils.reduce(1.2, 3.141592653589793, 1.5707963267948966);
        expectedResult = 0.6307963267948966;
        assertEquals("reduce should return expected value for given arguments", expectedResult, result);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceWithInvalidArgument() {
        MathUtils.reduce(Double.POSITIVE_INFINITY, 3.141592653589793, 0.0);
    }

    @Test
    public void testCopySignByte() {
        byte sign = 1;
        byte magnitude = -127;
        byte expectedResult = -magnitude;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return expected value for given arguments", expectedResult, result);

        sign = -1;
        magnitude = 127;
        expectedResult = magnitude;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return expected value for given arguments", expectedResult, result);

        magnitude = 128;
        try {
            result = MathUtils.copySign(magnitude, 1);
            fail("copySign should throw an exception for given arguments");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignShort() {
        short sign = 1;
        short magnitude = -32767;
        short expectedResult = -magnitude;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return expected value for given arguments", expectedResult, result);

        sign = -1;
        magnitude = 32767;
        expectedResult = magnitude;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return expected value for given arguments", expectedResult, result);

        magnitude = 32768;
        try {
            result = MathUtils.copySign(magnitude, 1);
            fail("copySign should throw an exception for given arguments");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignInt() {
        int sign = 1;
        int magnitude = Integer.MIN_VALUE;
        int expectedResult = -magnitude;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return expected value for given arguments", expectedResult, result);

        sign = -1;
        magnitude = Integer.MAX_VALUE;
        expectedResult = magnitude;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return expected value for given arguments", expectedResult, result);

        magnitude = Integer.MIN_VALUE + 1;
        try {
            result = MathUtils.copySign(magnitude, 1);
            fail("copySign should throw an exception for given arguments");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCopySignLong() {
        long sign = 1;
        long magnitude = Long.MIN_VALUE;
        long expectedResult = -magnitude;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return expected value for given arguments", expectedResult, result);

        sign = -1;
        magnitude = Long.MAX_VALUE;
        expectedResult = magnitude;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign should return expected value for given arguments", expectedResult, result);

        magnitude = Long.MIN_VALUE + 1;
        try {
            result = MathUtils.copySign(magnitude, 1);
            fail("copySign should throw an exception for given arguments");
        } catch (MathArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(Double.NaN);
        try {
            MathUtils.checkFinite(Double.POSITIVE_INFINITY);
            fail("checkFinite should throw an exception for given argument");
        } catch (NotFiniteNumberException e) {
            // expected
        }
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, 2.3, 3.4, Double.NaN};
        try {
            MathUtils.checkFinite(values);
            fail("checkFinite should throw an exception for given argument");
        } catch (NotFiniteNumberException e) {
            // expected
        }

        try {
            MathUtils.checkFinite(new double[] {Double.POSITIVE_INFINITY});
            fail("checkFinite should throw an exception for given argument");
        } catch (NotFiniteNumberException e) {
            // expected
        }
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);

        Object nullObj = null;
        try {
            MathUtils.checkNotNull(nullObj);
            fail("checkNotNull should throw an exception for given argument");
        } catch (NullArgumentException e) {
            // expected
        }

        String pattern = LocalizedFormats.NULL_ARGUMENT;
        Object[] args = new Object[0];
        try {
            MathUtils.checkNotNull(nullObj, pattern, args);
            fail("checkNotNull should throw an exception for given arguments");
        } catch (NullArgumentException e) {
            // expected
        }

        pattern = LocalizedFormats.ARRAY_ELEMENT;
        args = new Object[] {1};
        try {
            MathUtils.checkNotNull(new Object[0], pattern, args);
            fail("checkNotNull should throw an exception for given arguments");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCheckNotNullObjectNull() {
        MathUtils.checkNotNull(null);
        try {
            MathUtils.checkNotNull(null, LocalizedFormats.NULL_ARGUMENT, new Object[0]);
            fail("checkNotNull should throw an exception for given arguments");
        } catch (NullArgumentException e) {
            // expected
        }
    }
}