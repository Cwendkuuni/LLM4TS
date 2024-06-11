package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(139452583486745L, MathUtils.hash(1.23));
        assertEquals(79563848644223L, MathUtils.hash(3.141592653589793));
        assertEquals(12345, MathUtils.hash(5));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 3.141592653589793, 5.0};
        assertEquals(-1826428852291585673L, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.23, MathUtils.normalizeAngle(1.23, 0), 0.00001);
        assertEquals(MathUtils.TWO_PI - 1.23, MathUtils.normalizeAngle(1.23, MathUtils.TWO_PI), 0.00001);
        assertEquals(MathUtils.TWO_PI + 1.23, MathUtils.normalizeAngle(1.23, MathUtils.TWO_PI + 1), 0.00001);
        assertEquals(1.23, MathUtils.normalizeAngle(MathUtils.TWO_PI + 1.23, MathUtils.TWO_PI * 2), 0.00001);
        assertEquals(-1.23, MathUtils.normalizeAngle(-1.23, MathUtils.TWO_PI), 0.00001);
        assertEquals(-1.23, MathUtils.normalizeAngle(MathUtils.TWO_PI - 1.23, 0), 0.00001);
    }

    @Test
    public void testReduce() {
        assertEquals(0.1, MathUtils.reduce(0.61803398875, 1.0, 0), 0.00001);
        assertEquals(0.40901639344, MathUtils.reduce(2.5782838121, 1.0, 0.5), 0.00001);
        assertEquals(MathUtils.TWO_PI - 0.1, MathUtils.reduce(MathUtils.TWO_PI + 0.1, 1.0, 0), 0.00001);
        assertEquals(-MathUtils.TWO_PI + 0.1, MathUtils.reduce(MathUtils.TWO_PI - 0.1, 1.0, 0), 0.00001);
        assertEquals(MathUtils.TWO_PI - 0.5, MathUtils.reduce(MathUtils.TWO_PI + 0.5, 1.0, 0.5), 0.00001);
        assertEquals(-MathUtils.TWO_PI + 0.5, MathUtils.reduce(MathUtils.TWO_PI - 0.5, 1.0, 0.5), 0.00001);
    }

    @Test
    public void testCopySignByte() {
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) 1));
        assertEquals((byte) -1, MathUtils.copySign((byte) -1, (byte) 1));
        assertEquals((byte) -1, MathUtils.copySign((byte) 1, (byte) -1));
        assertEquals((byte) 1, MathUtils.copySign((byte) 1, (byte) -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((byte) -128, (byte) 1));
    }

    @Test
    public void testCopySignShort() {
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) 1));
        assertEquals((short) -1, MathUtils.copySign((short) -1, (short) 1));
        assertEquals((short) -1, MathUtils.copySign((short) 1, (short) -1));
        assertEquals((short) 1, MathUtils.copySign((short) 1, (short) -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign((short) -32768, (short) 1));
    }

    @Test
    public void testCopySignInt() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(1, MathUtils.copySign(1, -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(-2147483648, 1));
    }

    @Test
    public void testCopySignLong() {
        assertEquals(1, MathUtils.copySign(1, 1));
        assertEquals(-1, MathUtils.copySign(-1, 1));
        assertEquals(-1, MathUtils.copySign(1, -1));
        assertEquals(1, MathUtils.copySign(1, -1));
        assertThrows(MathArithmeticException.class, () -> MathUtils.copySign(-9223372036854775807L, 1));
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(1.23);
        MathUtils.checkFinite(3.141592653589793);
        MathUtils.checkFinite(Double.NaN);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.23, 3.141592653589793, Double.NaN};
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNullObject() {
        assertNotNull(new Object());
    }

    @Test
    public void testCheckNotNullObjectWithMessage() {
        String message = "Invalid argument";
        MathUtils.checkNotNull(new Object(), LocalizedFormats.NULL_ARGUMENT, message.toCharArray());
    }
}