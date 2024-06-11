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
        double value = 1.23;
        int expectedHashCode = 1314877451; // just an arbitrary value for testing
        int actualHashCode = MathUtils.hash(value);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = NullArgumentException.class)
    public void testHashNullDouble() {
        MathUtils.hash(null);
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = {1.23, 2.46, 3.78};
        int expectedHashCode = 251953881; // just an arbitrary value for testing
        int actualHashCode = MathUtils.hash(value);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = NullArgumentException.class)
    public void testHashNullDoubleArray() {
        MathUtils.hash(null);
    }

    @Test
    public void testNormalizeAngle() {
        double a = Math.PI * 1.5;
        double center = Math.PI;
        double expectedResult = 1.5707963267948966; // just an arbitrary value for testing
        double actualResult = MathUtils.normalizeAngle(a, center);
        assertEquals(expectedResult, actualResult, 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeAngleInvalidCenter() {
        double a = Math.PI * 1.5;
        double center = Math.PI * 2.0;
        MathUtils.normalizeAngle(a, center);
    }

    @Test
    public void testReduce() {
        double a = Math.PI * 2.0;
        double period = Math.PI * 0.5;
        double offset = Math.PI * 0.25;
        double expectedResult = Math.PI * 1.75; // just an arbitrary value for testing
        double actualResult = MathUtils.reduce(a, period, offset);
        assertEquals(expectedResult, actualResult, 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReduceInvalidPeriod() {
        double a = 0.0;
        double period = 0.0;
        MathUtils.reduce(a, period, 0.0);
    }

    @Test
    public void testCopySignByte() throws MathArithmeticException {
        byte magnitude = 5;
        byte sign = -1;
        byte expectedResult = 5; // just an arbitrary value for testing
        byte actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() throws MathArithmeticException {
        byte magnitude = -129;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShort() throws MathArithmeticException {
        short magnitude = 32765;
        short sign = -1;
        short expectedResult = 32765; // just an arbitrary value for testing
        short actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() throws MathArithmeticException {
        short magnitude = -32767;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignInt() throws MathArithmeticException {
        int magnitude = 100000;
        int sign = -1;
        int expectedResult = 100000; // just an arbitrary value for testing
        int actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() throws MathArithmeticException {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLong() throws MathArithmeticException {
        long magnitude = 900000000000L;
        long sign = -1;
        long expectedResult = 900000000000L; // just an arbitrary value for testing
        long actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() throws MathArithmeticException {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.23;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaNDouble() {
        double value = Double.NaN;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfiniteDouble() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.23, 2.46, 3.78};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteNaNDoubleArrayElement() {
        double[] values = {1.23, Double.NaN, 3.78};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteInfiniteDoubleArrayElement() {
        double[] values = {1.23, Double.POSITIVE_INFINITY, 3.78};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullNullObject() throws NullArgumentException {
        MathUtils.checkNotNull(null, "argument cannot be null");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObjectPattern() throws NullArgumentException {
        MathUtils.checkNotNull(null, "argument{0} cannot be null", "argument");
    }

    @Test
    public void testCheckNotNullNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

}