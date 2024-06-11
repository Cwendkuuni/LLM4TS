package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.Precision;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 3.14;
        int expectedHashCode = Double.hashCode(value);
        int actualHashCode = MathUtils.hash(value);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testHashDoubleArray() {
        double[] value = {1.2, 2.718, 3.14159265};
        int expectedHashCode = Arrays.hashCode(value);
        int actualHashCode = MathUtils.hash(value);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI * 3.5;
        double center = Math.PI;
        double expectedResult = -2.141592653589793;
        double result = MathUtils.normalizeAngle(angle, center);
        assertEquals(expectedResult, result, Precision.DOUBLE_EPSILON);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithNaN() {
        double angle = Double.NaN;
        double center = 0.0;
        MathUtils.normalizeAngle(angle, center);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithInfinite() {
        double angle = Double.POSITIVE_INFINITY;
        double center = 0.0;
        MathUtils.normalizeAngle(angle, center);
    }

    @Test
    public void testReduce() {
        double value = Math.PI * 2.5;
        double period = Math.PI;
        double offset = Math.PI / 2.0;
        double expectedResult = 0.3141592653589793;
        double result = MathUtils.reduce(value, period, offset);
        assertEquals(expectedResult, result, Precision.DOUBLE_EPSILON);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = -5;
        byte sign = 1;
        byte expectedResult = 5;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCopySignByteWithOverflow() {
        byte magnitude = -128;
        byte sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShort() {
        short magnitude = -32768;
        short sign = 1;
        short expectedResult = 32767;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCopySignShortWithOverflow() {
        short magnitude = -32768;
        short sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignInt() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        int expectedResult = -Integer.MIN_VALUE;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCopySignLong() {
        long magnitude = Long.MIN_VALUE;
        long sign = 1;
        long expectedResult = -Long.MIN_VALUE;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 3.14;
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

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, 2.718, 3.14159265};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] values = {1.2, 2.718, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] values = {1.2, 2.718, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, () -> "Object is null");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object obj = null;
        MathUtils.checkNotNull(obj, () -> "Object is null");
    }

    @Test
    public void testCheckNotNullObjectEmptyPattern() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNullPattern() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testCheckNotNullObjectArray() {
        Object[] objArray = new Object[3];
        MathUtils.checkNotNull(objArray, () -> "Object array is null");
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectArrayNull() {
        Object[] objArray = null;
        MathUtils.checkNotNull(objArray, () -> "Object array is null");
    }
}