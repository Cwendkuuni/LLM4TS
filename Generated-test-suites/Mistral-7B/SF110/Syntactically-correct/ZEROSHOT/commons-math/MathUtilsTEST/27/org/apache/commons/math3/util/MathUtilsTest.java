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
        int expectedHashCode = 584251015; // An arbitrary expected hash code value
        int actualHashCode = MathUtils.hash(value);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.718, 3.14159265};
        int expectedHashCode = 801754421; // An arbitrary expected hash code value
        int actualHashCode = MathUtils.hash(values);
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 3.14159265;
        double center = 0.0;
        double expectedNormalizedAngle = 0.785398163205225;
        double actualNormalizedAngle = MathUtils.normalizeAngle(angle, center);
        assertEquals(expectedNormalizedAngle, actualNormalizedAngle, 0.00000001);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testNormalizeAngleWithInvalidAngle() {
        double angle = Double.POSITIVE_INFINITY;
        double center = 0.0;
        MathUtils.normalizeAngle(angle, center);
    }

    @Test
    public void testReduce() {
        double angle = 6.283185307179586;
        double period = 2.0 * Math.PI;
        double offset = Math.PI;
        double expectedReducedAngle = 0.283185307179586;
        double actualReducedAngle = MathUtils.reduce(angle, period, offset);
        assertEquals(expectedReducedAngle, actualReducedAngle, 0.00000001);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = -1;
        byte expectedResult = -10;
        byte actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);

        magnitude = Byte.MIN_VALUE;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testCopySignShort() {
        short magnitude = Short.MAX_VALUE;
        short sign = -1;
        short expectedResult = -Short.MAX_VALUE;
        short actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);

        magnitude = Short.MIN_VALUE;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testCopySignInt() {
        int magnitude = Integer.MAX_VALUE;
        int sign = -1;
        int expectedResult = -Integer.MAX_VALUE;
        int actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testCopySignLong() {
        long magnitude = Long.MAX_VALUE;
        long sign = -1;
        long expectedResult = -Long.MAX_VALUE;
        long actualResult = MathUtils.copySign(magnitude, sign);
        assertEquals(expectedResult, actualResult);

        magnitude = Long.MIN_VALUE;
        sign = 1;
        try {
            MathUtils.copySign(magnitude, sign);
            fail("Expected MathArithmeticException");
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testCheckFiniteDouble() {
        double finiteNumber = 1.23;
        double notFiniteNumber = Double.NaN;
        MathUtils.checkFinite(finiteNumber);
        try {
            MathUtils.checkFinite(notFiniteNumber);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] finiteNumbers = {1.23, 2.718, 3.14159265};
        MathUtils.checkFinite(finiteNumbers);

        double[] notFiniteNumbers = {Double.NaN, Double.NaN, Double.NaN};
        try {
            MathUtils.checkFinite(notFiniteNumbers);
            fail("Expected NotFiniteNumberException");
        } catch (NotFiniteNumberException e) {
            // Expected exception
        }
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
        fail("Expected NullArgumentException");
    }

    @Test
    public void testCheckNotNullObjectPattern() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj, LocalizedFormats.OBJECT_NULL);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullNullObjectPatternWithArgs() {
        Object obj = null;
        MathUtils.checkNotNull(obj, LocalizedFormats.ARRAY_ELEMENT, "element");
        fail("Expected NullArgumentException");
    }
}