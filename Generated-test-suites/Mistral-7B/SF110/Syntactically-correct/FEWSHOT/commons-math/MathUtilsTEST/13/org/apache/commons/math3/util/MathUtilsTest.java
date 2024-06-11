package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrowable;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import java.util.Arrays;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        assertEquals(-1531020485, MathUtils.hash(0.0));
        assertEquals(1402658481, MathUtils.hash(1.0));
        assertEquals(1821354915, MathUtils.hash(-1.0));
        assertEquals(-1520340455, MathUtils.hash(Double.NaN));
        assertEquals(-1831764641, MathUtils.hash(Double.POSITIVE_INFINITY));
        assertEquals(-1831764640, MathUtils.hash(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.0, 2.0, 3.0};
        long expected = 2132552559611791129L;
        assertEquals(expected, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.0, MathUtils.normalizeAngle(1.0, 0.0), 1e-14);
        assertEquals(1.5707963267948966, MathUtils.normalizeAngle(3.141592653589793, 0.0), 1e-14);
        assertEquals(MathUtils.TWO_PI - 1.5707963267948966, MathUtils.normalizeAngle(MathUtils.TWO_PI + 3.141592653589793, 0.0), 1e-14);
        assertEquals(MathUtils.TWO_PI, MathUtils.normalizeAngle(MathUtils.TWO_PI + 6.283185307179586, 0.0), 1e-14);
        assertNotEquals(2.0, MathUtils.normalizeAngle(1.0, MathUtils.TWO_PI), 1e-14);
    }

    @Test
    public void testReduce() {
        assertEquals(0.1, MathUtils.reduce(0.1, 1.0, 0.0), 1e-14);
        assertEquals(-0.1, MathUtils.reduce(0.1, 1.0, -0.1), 1e-14);
        assertEquals(1.1, MathUtils.reduce(1.6, 1.0, 0.5), 1e-14);
        assertEquals(6.183185307179586, MathUtils.reduce(7.413273654794897, 1.0, 0.2), 1e-14);
        assertEquals(0.0, MathUtils.reduce(3.141592653589793, 6.283185307179586, 3.141592653589793), 1e-14);
    }

    @Test
    public void testCopySignByte() {
        byte magnitude = 5;
        byte sign = 1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = -5;
        sign = 1;
        assertEquals((byte)-magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = -128;
        sign = 1;
        assertThrowable(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = 5;
        sign = -1;
        assertEquals((byte)-magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignShort() {
        short magnitude = 32767;
        short sign = 1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = -32768;
        sign = 1;
        assertThrowable(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = 32767;
        sign = -1;
        assertEquals((short)-magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignInt() {
        int magnitude = Integer.MAX_VALUE;
        int sign = 1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = Integer.MIN_VALUE;
        sign = 1;
        assertThrowable(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = Integer.MAX_VALUE;
        sign = -1;
        assertEquals(-magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCopySignLong() {
        long magnitude = Long.MAX_VALUE;
        long sign = 1;
        assertEquals(magnitude, MathUtils.copySign(magnitude, sign));

        magnitude = Long.MIN_VALUE;
        sign = 1;
        assertThrowable(MathArithmeticException.class, () -> MathUtils.copySign(magnitude, sign));

        magnitude = Long.MAX_VALUE;
        sign = -1;
        assertEquals(-magnitude, MathUtils.copySign(magnitude, sign));
    }

    @Test
    public void testCheckFiniteDouble() {
        MathUtils.checkFinite(1.0);
        MathUtils.checkFinite(Double.POSITIVE_INFINITY);
        MathUtils.checkFinite(Double.NEGATIVE_INFINITY);
        MathUtils.checkFinite(Double.NaN);

        assertThrowable(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.longBitsToDouble(Long.MAX_LONG)));
        assertThrowable(NotFiniteNumberException.class, () -> MathUtils.checkFinite(Double.longBitsToDouble(Long.MIN_LONG)));
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] values = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(values);

        double[] valuesWithNaN = {1.0, 2.0, Double.NaN};
        assertThrowable(NotFiniteNumberException.class, () -> MathUtils.checkFinite(valuesWithNaN));

        double[] valuesWithInf = {1.0, Double.POSITIVE_INFINITY, 3.0};
        assertThrowable(NotFiniteNumberException.class, () -> MathUtils.checkFinite(valuesWithInf));

        double[] valuesWithInfAndNaN = {1.0, Double.POSITIVE_INFINITY, Double.NaN, 3.0};
        assertThrowable(NotFiniteNumberException.class, () -> MathUtils.checkFinite(valuesWithInfAndNaN));
    }

    @Test
    public void testCheckNotNullObject() {
        Object o = new Object();
        MathUtils.checkNotNull(o);

        Object oNull = null;
        assertThrowable(NullArgumentException.class, () -> MathUtils.checkNotNull(oNull));
    }

    @Test
    public void testCheckNotNullObjectWithMessage() {
        Object o = new Object();
        MathUtils.checkNotNull(o, "Object cannot be null");

        Object oNull = null;
        assertThrowable(NullArgumentException.class, () -> MathUtils.checkNotNull(oNull, "Object cannot be null"));
    }
}