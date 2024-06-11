package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedHashCode = Double.hashCode(value);
        int actualHashCode = MathUtils.hash(value);
        assertEquals("Hashcode of double should be the same.", expectedHashCode, actualHashCode);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expectedHashCode = Arrays.hashCode(values);
        int actualHashCode = MathUtils.hash(values);
        assertEquals("Hashcode of double array should be the same.", expectedHashCode, actualHashCode);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI + Math.PI;
        double center = MathUtils.TWO_PI / 2.0;
        double expectedNormalizedAngle = -MathUtils.TWO_PI / 2.0;
        double actualNormalizedAngle = MathUtils.normalizeAngle(angle, center);
        assertEquals("Normalized angle should be equals to expected value.", expectedNormalizedAngle, actualNormalizedAngle, 0.0001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceWithInvalidParameters() {
        double a = Double.NEGATIVE_INFINITY;
        double period = 1.0;
        double offset = 0.0;
        MathUtils.reduce(a, period, offset);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceWithNegativePeriod() {
        double a = 0.0;
        double period = -1.0;
        double offset = 0.0;
        MathUtils.reduce(a, period, offset);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceWithNegativeAngle() {
        double a = -MathUtils.TWO_PI;
        double period = 1.0;
        double offset = 0.0;
        MathUtils.reduce(a, period, offset);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expectedResult = 5;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(byte, byte) should return expected result.", expectedResult, result);

        magnitude = -1;
        sign = 1;
        expectedResult = -1;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(byte, byte) should return expected result.", expectedResult, result);

        magnitude = -1;
        sign = -1;
        expectedResult = -1;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(byte, byte) should return expected result.", expectedResult, result);

        magnitude = 127;
        sign = 1;
        expectedResult = 127;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(byte, byte) should return expected result.", expectedResult, result);

        magnitude = -128;
        sign = 1;
        MathUtils.checkFinite(sign); // Exception should be thrown
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 5;
        short sign = 1;
        short expectedResult = 5;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(short, short) should return expected result.", expectedResult, result);

        magnitude = -32767;
        sign = 1;
        expectedResult = -32767;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(short, short) should return expected result.", expectedResult, result);

        magnitude = -32768;
        sign = 1;
        MathUtils.checkFinite(sign); // Exception should be thrown

        magnitude = 32767;
        sign = -1;
        expectedResult = -32767;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(short, short) should return expected result.", expectedResult, result);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 5;
        int sign = 1;
        int expectedResult = 5;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(int, int) should return expected result.", expectedResult, result);

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        expectedResult = Integer.MIN_VALUE;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(int, int) should return expected result.", expectedResult, result);

        magnitude = Integer.MIN_VALUE;
        sign = -1;
        MathUtils.checkFinite(sign); // Exception should be thrown
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 5L;
        long sign = 1;
        long expectedResult = 5L;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(long, long) should return expected result.", expectedResult, result);

        magnitude = Long.MIN_VALUE;
        sign = 1;
        expectedResult = Long.MIN_VALUE;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(long, long) should return expected result.", expectedResult, result);

        magnitude = Long.MIN_VALUE;
        sign = -1;
        MathUtils.checkFinite(sign); // Exception should be thrown
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullArgument() {
        MathUtils.checkNotNull(null, "Null argument passed.");
    }

    @Test
    public void testCheckNotNullWithoutNullArgument() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testCheckNotNullWithNullPattern() {
        MathUtils.checkNotNull(null, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullWithNullArray() {
        double[] values = null;
        MathUtils.checkFinite(values);
        MathUtils.checkNotNull(values);
    }

    @Test
    public void testCheckNotNullWithArray() {
        double[] values = new double[3];
        MathUtils.checkFinite(values);
        MathUtils.checkNotNull(values);
    }
}