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
        int expectedHash = 313726832; // just an arbitrary value for testing
        int actualHash = MathUtils.hash(value);
        assertEquals("hash(1.23) should equal " + expectedHash, expectedHash, actualHash);
    }

    @Test(expected = NullArgumentException.class)
    public void testHashNullDouble() {
        MathUtils.hash(null);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expectedHash = 109132157; // just an arbitrary value for testing
        int actualHash = MathUtils.hash(values);
        assertEquals("hash([1.1, 2.2, 3.3]) should equal " + expectedHash, expectedHash, actualHash);
    }

    @Test(expected = NullArgumentException.class)
    public void testHashNullDoubleArray() {
        MathUtils.hash(null);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI / 4.0;
        double center = 0.0;
        double expectedResult = 0.78539816339745;
        double actualResult = MathUtils.normalizeAngle(angle, center);
        assertEquals("normalizeAngle(PI/4, 0) should equal " + expectedResult, expectedResult, actualResult, 1e-14);
    }

    @Test
    public void testNormalizeAngleWithCenterGreaterThan2PI() {
        double angle = Math.PI;
        double center = Math.PI * 2.0;
        double expectedResult = angle - 4.0 * Math.PI;
        double actualResult = MathUtils.normalizeAngle(angle, center);
        assertEquals("normalizeAngle(PI, 2PI) should equal " + expectedResult, expectedResult, actualResult, 1e-14);
    }

    @Test
    public void testReduce() {
        double a = 3.141592653589793;
        double period = 2.0 * Math.PI;
        double offset = Math.PI / 2.0;
        double expectedResult = 0.78539816339745; // just an arbitrary value for testing
        double actualResult = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(3.141592653589793, 2PI, PI/2) should equal " + expectedResult, expectedResult, actualResult, 1e-14);
    }

    @Test(expected = NullArgumentException.class)
    public void testCopySignByteNegativeAndPositive() {
        byte magnitude = -5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignBytePositiveAndPositive() {
        byte magnitude = 5;
        byte sign = 1;
        byte expectedResult = 5;
        byte actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(5, 1) should equal " + expectedResult, expectedResult, actualResult);
    }

    @Test
    public void testCopySignShortNegativeAndPositive() {
        short magnitude = -5;
        short sign = 1;
        short expectedResult = 5;
        short actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-5, 1) should equal " + expectedResult, expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignShortPositiveAndPositive() {
        short magnitude = 5;
        short sign = 1;
        short expectedResult = 5;
        short actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(5, 1) should equal " + expectedResult, expectedResult, actualResult);
    }

    @Test
    public void testCopySignIntNegativeAndPositive() {
        int magnitude = -5;
        int sign = 1;
        int expectedResult = 5;
        int actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-5, 1) should equal " + expectedResult, expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignIntPositiveAndPositive() {
        int magnitude = 5;
        int sign = 1;
        int expectedResult = 5;
        int actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(5, 1) should equal " + expectedResult, expectedResult, actualResult);
    }

    @Test
    public void testCopySignLongNegativeAndPositive() {
        long magnitude = -5;
        long sign = 1;
        long expectedResult = 5;
        long actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-5, 1) should equal " + expectedResult, expectedResult, actualResult);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongPositiveAndPositive() {
        long magnitude = 5;
        long sign = 1;
        long expectedResult = 5;
        long actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(5, 1) should equal " + expectedResult, expectedResult, actualResult);
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 0.12345;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinity() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {Double.NaN, 2.2, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinity() {
        double[] values = {1.1, Double.POSITIVE_INFINITY, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObject() {
        Object o = null;
        MathUtils.checkNotNull(o);
    }

    @Test
    public void testCheckNotNullString() {
        String s = "hello";
        MathUtils.checkNotNull(s, LocalizedFormats.OBJECT_IS_NULL, new Object[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullString() {
        String s = null;
        MathUtils.checkNotNull(s, LocalizedFormats.OBJECT_IS_NULL, new Object[0]);
    }
}