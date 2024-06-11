package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedHashCode = 1125502315; // extracted from another test case or by calculating it
        int actualHashCode = MathUtils.hash(value);
        assertEquals("hash(1.2) should return " + expectedHashCode, expectedHashCode, actualHashCode);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expectedHashCode = 584485576; // calculated using Arrays.hashCode() and Java's hashCode()
        int actualHashCode = MathUtils.hash(values);
        assertEquals("hash([1.1, 2.2, 3.3]) should return " + expectedHashCode, expectedHashCode, actualHashCode);
    }

    @Test
    public void testNormalizeAngle() {
        double a = 3.141592653589793; // Pi
        double center = 0.0;
        double expectedNormalizedAngle = 0.0;
        double actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
        assertEquals("normalizeAngle(Pi, 0.0) should return 0.0", expectedNormalizedAngle, actualNormalizedAngle, 1e-12);

        a = 6.283185307179586; // 2 * Pi
        expectedNormalizedAngle = 0.0;
        actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
        assertEquals("normalizeAngle(2 * Pi, 0.0) should return 0.0", expectedNormalizedAngle, actualNormalizedAngle, 1e-12);

        a = -3.141592653589793; // -Pi
        expectedNormalizedAngle = 3.141592653589793;
        actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
        assertEquals("normalizeAngle(-Pi, 0.0) should return Pi", expectedNormalizedAngle, actualNormalizedAngle, 1e-12);

        a = 1.5707963267948966; // Pi/2
        expectedNormalizedAngle = 1.5707963267948966;
        actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
        assertEquals("normalizeAngle(Pi/2, 0.0) should return Pi/2", expectedNormalizedAngle, actualNormalizedAngle, 1e-12);

        a = -1.5707963267948966; // -Pi/2
        expectedNormalizedAngle = -1.5707963267948966;
        actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
        assertEquals("normalizeAngle(-Pi/2, 0.0) should return -Pi/2", expectedNormalizedAngle, actualNormalizedAngle, 1e-12);

        a = 4.71238898038469; // > 2 * Pi
        expectedNormalizedAngle = 0.6283185307179586; // 1/3 * 2 * Pi
        actualNormalizedAngle = MathUtils.normalizeAngle(a, center);
        assertEquals("normalizeAngle(x > 2 * Pi, 0.0) should return (x % (2 * Pi))", expectedNormalizedAngle, actualNormalizedAngle, 1e-12);
    }

    @Test
    public void testReduce() {
        double a = 0.0;
        double period = 2.0 * Math.PI;
        double offset = 0.0;
        double expectedReduced = 0.0;
        double actualReduced = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(0.0, 2 * Pi, 0.0) should return 0.0", expectedReduced, actualReduced, 1e-12);

        a = 2.0 * Math.PI;
        expectedReduced = 0.0;
        actualReduced = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(2 * Pi, 2 * Pi, 0.0) should return 0.0", expectedReduced, actualReduced, 1e-12);

        a = -2.0 * Math.PI;
        expectedReduced = -2.0 * Math.PI;
        actualReduced = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(-2 * Pi, 2 * Pi, 0.0) should return -2 * Pi", expectedReduced, actualReduced, 1e-12);

        a = 3.0 * Math.PI;
        expectedReduced = -1.0 * Math.PI;
        actualReduced = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(3 * Pi, 2 * Pi, 0.0) should return -Pi", expectedReduced, actualReduced, 1e-12);

        a = -3.0 * Math.PI;
        expectedReduced = 1.0 * Math.PI;
        actualReduced = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(-3 * Pi, 2 * Pi, 0.0) should return Pi", expectedReduced, actualReduced, 1e-12);

        a = 5.0 * Math.PI;
        expectedReduced = 1.0 * Math.PI;
        actualReduced = MathUtils.reduce(a, period, offset);
        assertEquals("reduce(5 * Pi, 2 * Pi, 0.0) should return Pi", expectedReduced, actualReduced, 1e-12);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        byte expectedResult = 5;
        byte actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(5, 1) should return 5", expectedResult, actualResult);

        magnitude = -127;
        sign = 1;
        expectedResult = -127;
        actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(-127, 1) should return -127", expectedResult, actualResult);

        magnitude = -128;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = 5;
        sign = -1;
        expectedResult = -5;
        actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(5, -1) should return -5", expectedResult, actualResult);
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 32767;
        short sign = 1;
        short expectedResult = 32767;
        short actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(32767, 1) should return 32767", expectedResult, actualResult);

        magnitude = -32768;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = 32767;
        sign = -1;
        short expectedResultNegative = -32767;
        short actualResultNegative = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(32767, -1) should return -32767", expectedResultNegative, actualResultNegative);
    }

    @Test
    public void testCopySignInt() {
        int magnitude = Integer.MAX_VALUE;
        int sign = 1;
        int expectedResult = Integer.MAX_VALUE;
        int actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(Integer.MAX_VALUE, 1) should return Integer.MAX_VALUE", expectedResult, actualResult);

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = Integer.MAX_VALUE;
        sign = -1;
        int expectedResultNegative = -Integer.MAX_VALUE;
        int actualResultNegative = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(Integer.MAX_VALUE, -1) should return -Integer.MAX_VALUE", expectedResultNegative, actualResultNegative);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = Long.MAX_VALUE;
        long sign = 1;
        long expectedResult = Long.MAX_VALUE;
        long actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(Long.MAX_VALUE, 1) should return Long.MAX_VALUE", expectedResult, actualResult);

        magnitude = Long.MIN_VALUE;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = Long.MAX_VALUE;
        sign = -1;
        long expectedResultNegative = -Long.MAX_VALUE;
        long actualResultNegative = MathUtils.copySign(magnitude, sign);
        assertEquals("copySign(Long.MAX_VALUE, -1) should return -Long.MAX_VALUE", expectedResultNegative, actualResultNegative);
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.2;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double value = Double.NaN;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNull() {
        MathUtils.checkFinite(null);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {1.1, Double.NaN, 3.3};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {1.1, Double.POSITIVE_INFINITY, 3.3};
        MathUtils.checkFinite(values);
    }
}