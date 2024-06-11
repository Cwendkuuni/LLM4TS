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
        double value = 3.14;
        int expectedHash = 12345; // Replace this with the expected hash value for the given double
        int actualHash = MathUtils.hash(value);
        assertEquals("Hash of double should be equal", expectedHash, actualHash);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expectedHash = 56789; // Replace this with the expected hash value for the given double array
        int actualHash = MathUtils.hash(values);
        assertEquals("Hash of double array should be equal", expectedHash, actualHash);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 3.141592653589793;
        double center = 1.5707963267948966;
        double expectedAngle = 0.615243595529561; // Replace this with the expected result for the given angle and center
        double actualAngle = MathUtils.normalizeAngle(angle, center);
        assertEquals("Normalize angle should be equal", expectedAngle, actualAngle, 0.00001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceWithInfiniteInput() {
        double input = Double.POSITIVE_INFINITY;
        double period = 1.0;
        double offset = 0.0;
        MathUtils.reduce(input, period, offset);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testReduceWithNaNInput() {
        double input = Double.NaN;
        double period = 1.0;
        double offset = 0.0;
        MathUtils.reduce(input, period, offset);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = -1;
        byte expectedResult = -5; // Replace this with the expected result for the given magnitude and sign
        byte actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign for byte should be equal", expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteWithOverflow() {
        byte magnitude = -129;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = -3000;
        short sign = 1;
        short expectedResult = 3000; // Replace this with the expected result for the given magnitude and sign
        short actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign for short should be equal", expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortWithOverflow() {
        short magnitude = -32769;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = -1000;
        int sign = 1;
        int expectedResult = 1000; // Replace this with the expected result for the given magnitude and sign
        int actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign for int should be equal", expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntWithOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = -1000000L;
        long sign = 1;
        long expectedResult = 1000000L; // Replace this with the expected result for the given magnitude and sign
        long actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("Copy sign for long should be equal", expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongWithOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDouble() {
        double input = 1.23;
        MathUtils.checkFinite(input);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaNDouble() {
        double input = Double.NaN;
        MathUtils checkFinite = MathUtils::checkFinite;
        checkFinite.accept(input);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] inputs = {1.23, 2.46, 3.7};
        MathUtils.checkFinite(inputs);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaNDoubleArrayElement() {
        double[] inputs = {1.23, Double.NaN, 3.7};
        MathUtils.checkFinite(inputs);
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }
}