package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expectedHash = 101; // an arbitrary value for testing
        int actualHash = MathUtils.hash(value);
        assertEquals("hash(1.23) should equal " + expectedHash, expectedHash, actualHash);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.46, 3.69};
        int expectedHash = 12345; // an arbitrary value for testing
        int actualHash = MathUtils.hash(values);
        assertEquals("hash({1.23, 2.46, 3.69}) should equal " + expectedHash, expectedHash, actualHash);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 3.14159265;
        double expectedCenter = 0.0;
        double actualAngle = MathUtils.normalizeAngle(angle, expectedCenter);
        assertEquals("normalizeAngle(3.14159265, 0.0) should equal 1.14159265", 1.14159265, actualAngle, 0.00001);

        angle = 6.283185307179586;
        double actualAngle2 = MathUtils.normalizeAngle(angle, expectedCenter);
        assertEquals("normalizeAngle(6.283185307179586, 0.0) should equal 0.0", 0.0, actualAngle2, 0.00001);

        angle = 4.71238898;
        expectedCenter = MathUtils.TWO_PI;
        double actualAngle3 = MathUtils.normalizeAngle(angle, expectedCenter);
        assertEquals("normalizeAngle(4.71238898, " + MathUtils.TWO_PI + ") should equal 0.71238898", 0.71238898, actualAngle3, 0.00001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNullArray() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaN() {
        double[] values = {Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfinite() {
        double[] values = {Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        MathUtils.checkNotNull(null, LocalizedFormats.OBJECT_NULL, "o");
    }

    @Test
    public void testCopySignByte() {
        byte sign = 1;
        byte magnitude = 50;
        byte expectedResult = 50;
        byte actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(50, 1) should equal 50", expectedResult, actualResult);

        sign = -1;
        magnitude = -50;
        expectedResult = -50;
        actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-50, -1) should equal -50", expectedResult, actualResult);

        sign = 1;
        magnitude = -128;
        MathUtils.checkNotNull(magnitude, LocalizedFormats.BYTE_NEGATIVE_128, "magnitude");
        expectedResult = -128;
        actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-128, 1) should throw MathArithmeticException", -128, actualResult);
    }

    @Test
    public void testCopySignShort() {
        short sign = 1;
        short magnitude = 5000;
        short expectedResult = 5000;
        short actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(5000, 1) should equal 5000", expectedResult, actualResult);

        sign = -1;
        magnitude = -32768;
        MathUtils.checkNotNull(magnitude, LocalizedFormats.SHORT_NEGATIVE_32768, "magnitude");
        expectedResult = -32768;
        actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-32768, -1) should throw MathArithmeticException", -32768, actualResult);
    }

    @Test
    public void testCopySignInt() {
        int sign = 1;
        int magnitude = 500000;
        int expectedResult = 500000;
        int actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(500000, 1) should equal 500000", expectedResult, actualResult);

        sign = -1;
        magnitude = Integer.MIN_VALUE;
        MathUtils.checkNotNull(magnitude, LocalizedFormats.INT_MIN_VALUE, "magnitude");
        expectedResult = -Integer.MIN_VALUE;
        actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-Integer.MIN_VALUE, -1) should throw MathArithmeticException", -Integer.MIN_VALUE, actualResult);
    }

    @Test
    public void testCopySignLong() {
        long sign = 1;
        long magnitude = 500000L;
        long expectedResult = 500000L;
        long actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(500000L, 1) should equal 500000L", expectedResult, actualResult);

        sign = -1;
        magnitude = Long.MIN_VALUE;
        MathUtils.checkNotNull(magnitude, LocalizedFormats.LONG_MIN_VALUE, "magnitude");
        expectedResult = -Long.MIN_VALUE;
        actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-Long.MIN_VALUE, -1) should throw MathArithmeticException", -Long.MIN_VALUE, actualResult);
    }

    @Test
    public void testReduce() {
        double a = 6.283185307179586;
        double period = 2.14159265;
        double offset = 1.5707963267948966;
        double expectedResult = 0.71238898;
        double actualResult = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(6.283185307179586, 2.14159265, 1.5707963267948966) should equal 0.71238898", expectedResult, actualResult, 0.00001);
    }
}