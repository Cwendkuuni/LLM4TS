package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

import java.util.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = 1312985253; // computed using Java's hashCode method
        int actual = MathUtils.hash(value);
        assertEquals("Incorrect hash code for double value.", expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.46, 3.69};
        int expected = 291488329; // computed using Java's Arrays.hashCode method
        int actual = MathUtils.hash(values);
        assertEquals("Incorrect hash code for double array.", expected, actual);
    }

    @Test
    public void testNormalizeAngle() {
        double angle = 3.14159265;
        double center = 2.1;
        double expected = -1.14159265;
        double actual = MathUtils.normalizeAngle(angle, center);
        assertEquals("Incorrect normalized angle.", expected, actual, 0.00001);
    }

    @Test(expected = MathArithmeticException.class)
    public void testReduceWithNegativePeriod() {
        double angle = 3.14159265;
        double period = -6.283185307179586;
        double offset = 2.1;
        MathUtils.reduce(angle, period, offset);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeSign() {
        byte magnitude = 5;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() {
        byte magnitude = 127;
        byte sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignByteValid() {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5;
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect result for copySign(byte, byte).", expected, actual);
    }

    @Test
    public void testCopySignShortValid() {
        short magnitude = 5;
        short sign = 1;
        short expected = 5;
        short actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect result for copySign(short, short).", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignShortOverflow() {
        short magnitude = 32767;
        short sign = -1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignIntValid() {
        int magnitude = 5;
        int sign = 1;
        int expected = 5;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect result for copySign(int, int).", expected, actual);
    }

    @Test
    public void testCopySignIntNegativeSign() {
        int magnitude = 5;
        int sign = -1;
        int expected = -5;
        int actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect result for copySign(int, int) with negative sign.", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignIntOverflow() {
        int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
    }

    @Test
    public void testCopySignLongValid() {
        long magnitude = 5;
        long sign = 1;
        long expected = 5;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect result for copySign(long, long).", expected, actual);
    }

    @Test
    public void testCopySignLongNegativeSign() {
        long magnitude = 5;
        long sign = -1;
        long expected = -5;
        long actual = MathUtils.copySign(magnitude, sign);
        assertEquals("Incorrect result for copySign(long, long) with negative sign.", expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignLongOverflow() {
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
        double[] values = {1.23, 2.46, Double.NaN, 3.69, Double.POSITIVE_INFINITY};
        MathUtils.checkFinite(values);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaNElement() {
        double[] values = {1.23, 2.46, Double.NaN, 3.69};
        MathUtils.checkFinite(values, 2);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfElement() {
        double[] values = {1.23, 2.46, Double.POSITIVE_INFINITY, 3.69};
        MathUtils.checkFinite(values, 2);
    }

    @Test
    public void testCheckNotNullObject() {
        Object obj = new Object();
        MathUtils.checkNotNull(obj);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullObjectNull() {
        Object obj = null;
        MathUtils.checkNotNull(obj);
    }

    @Test
    public void testCheckNotNullString() {
        String str = "Test string";
        MathUtils.checkNotNull(str);
    }

    @Test(expected = NullArgumentException.class)
    public void testCheckNotNullStringNull() {
        String str = null;
        MathUtils.checkNotNull(str);
    }
}