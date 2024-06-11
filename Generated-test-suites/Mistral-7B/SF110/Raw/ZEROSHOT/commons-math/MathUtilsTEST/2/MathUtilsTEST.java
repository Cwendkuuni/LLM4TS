package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedHash = 12345;
        int actualHash = MathUtils.hash(value);
        assertEquals("Hashcode for double value is not as expected.", expectedHash, actualHash);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expectedHash = 123456;
        int actualHash = MathUtils.hash(values);
        assertEquals("Hashcode for double array is not as expected.", expectedHash, actualHash);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = Math.PI;
        double expectedResult = 2.141592653589793;
        double result = MathUtils.normalizeAngle(angle, 0);
        assertEquals("NormalizeAngle method does not return expected result.", expectedResult, result, 0.000001);

        angle = Math.PI + 3 * Math.PI;
        expectedResult = -1.0471975511965978;
        result = MathUtils.normalizeAngle(angle, 0);
        assertEquals("NormalizeAngle method does not return expected result.", expectedResult, result, 0.000001);
    }

    @Test
    public void testReduce() {
        double value = 3.14;
        double period = 2 * Math.PI;
        double offset = Math.PI;
        double expectedResult = 0.141592653589793;
        double result = MathUtils.reduce(value, period, offset);
        assertEquals("Reduce method does not return expected result.", expectedResult, result, 0.000001);

        value = 3.14;
        period = 2 * Math.PI;
        offset = 0;
        expectedResult = 0.7853981633974483;
        result = MathUtils.reduce(value, period, offset);
        assertEquals("Reduce method does not return expected result.", expectedResult, result, 0.000001);

        value = 3.14;
        period = -2 * Math.PI;
        offset = Math.PI;
        assertThrows(IllegalArgumentException.class, () -> MathUtils.reduce(value, period, offset));
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 10;
        byte sign = 1;
        byte expectedResult = 10;
        byte result = MathUtils.copySign(magnitude, sign);
        assertEquals("CopySign method does not return expected result for byte.", expectedResult, result);

        magnitude = -127;
        sign = 1;
        expectedResult = -127;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("CopySign method does not return expected result for byte.", expectedResult, result);

        magnitude = -128;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 32765;
        short sign = 1;
        short expectedResult = 32765;
        short result = MathUtils.copySign(magnitude, sign);
        assertEquals("CopySign method does not return expected result for short.", expectedResult, result);

        magnitude = -32767;
        sign = 1;
        expectedResult = -32767;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("CopySign method does not return expected result for short.", expectedResult, result);

        magnitude = -32768;
        sign = 1;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignInt() {
        int magnitude = 123456;
        int sign = 1;
        int expectedResult = 123456;
        int result = MathUtils.copySign(magnitude, sign);
        assertEquals("CopySign method does not return expected result for int.", expectedResult, result);

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        expectedResult = Integer.MIN_VALUE;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("CopySign method does not return expected result for int.", expectedResult, result);

        magnitude = Integer.MIN_VALUE;
        sign = -1;
        expectedResult = -Integer.MIN_VALUE;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("CopySign method does not return expected result for int.", expectedResult, result);

        magnitude = Integer.MIN_VALUE;
        sign = 0;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignLong() {
        long magnitude = 1234567898765432L;
        long sign = 1;
        long expectedResult = 1234567898765432L;
        long result = MathUtils.copySign(magnitude, sign);
        assertEquals("CopySign method does not return expected result for long.", expectedResult, result);

        magnitude = Long.MIN_VALUE;
        sign = 1;
        expectedResult = Long.MIN_VALUE;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("CopySign method does not return expected result for long.", expectedResult, result);

        magnitude = Long.MIN_VALUE;
        sign = -1;
        expectedResult = -Long.MIN_VALUE;
        result = MathUtils.copySign(magnitude, sign);
        assertEquals("CopySign method does not return expected result for long.", expectedResult, result);

        magnitude = Long.MIN_VALUE;
        sign = 0;
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCheckFiniteDouble() {
        double value = 1.2;
        MathUtils.checkFinite(value);

        value = Double.MAX_VALUE;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(value));

        value = Double.NEGATIVE_INFINITY;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(value));

        value = Double.NaN;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(value));
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        MathUtils.checkFinite(values);

        values[0] = Double.MAX_VALUE;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(values));

        values[1] = Double.NEGATIVE_INFINITY;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(values));

        values[2] = Double.NaN;
        assertThrows(NotFiniteNumberException.class, () -> MathUtils.checkFinite(values));
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testCheckNotNullObjectNull() {
        Object obj = null;
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(obj));
    }

    @Test
    public void testCheckNotNullObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        MathUtils.checkNotNull(obj1, LocalizedFormats.FIRST_ELEMENT, obj2);
    }

    @Test
    public void testCheckNotNullObjectsNull() {
        Object obj1 = null;
        Object obj2 = new Object();
        assertThrows(NullArgumentException.class, () -> MathUtils.checkNotNull(obj1, LocalizedFormats.FIRST_ELEMENT, obj2));
    }
}