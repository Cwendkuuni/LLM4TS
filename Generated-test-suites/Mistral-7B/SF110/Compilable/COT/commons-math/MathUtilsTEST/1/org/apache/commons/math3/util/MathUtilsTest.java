package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = 1123104353; // hashcode for 1.23
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.23, 2.718, 0.1};
        int expected = 314662885; // hashcode for {1.23, 2.718, 0.1}
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeAngleBasic() {
        double input = 3.14;
        double output = 0.252; // 3.14 - 6.28 * floor((3.14 + 3.14) / 6.28)
        double actual = MathUtils.normalizeAngle(input, 0);
        assertEquals(output, actual, 1e-12);
    }

    @Test
    public void testNormalizeAngleEdgeCases() {
        double input = 0;
        double output = 6.28; // 0 - 6.28 * floor(0 / 6.28)
        double actual = MathUtils.normalizeAngle(input, 0);
        assertEquals(output, actual, 1e-12);

        input = Math.PI;
        output = -3.14; // 3.14 - 6.28 * floor((3.14 + 3.14) / 6.28)
        actual = MathUtils.normalizeAngle(input, 0);
        assertEquals(output, actual, 1e-12);

        input = -Math.PI;
        output = 3.14; // -3.14 - 6.28 * floor((-3.14 + 3.14) / 6.28)
        actual = MathUtils.normalizeAngle(input, 0);
        assertEquals(output, actual, 1e-12);

        input = Math.PI * 2;
        output = 0; // 6.28 * floor((6.28 * 2 + 3.14) / 6.28)
        actual = MathUtils.normalizeAngle(input, 0);
        assertEquals(output, actual, 1e-12);
    }

    @Test
    public void testReduceBasic() {
        double input = 1.5;
        double period = 2.0;
        double offset = 0.5;
        double output = 0.5; // input - period * floor((input - offset) / period) - offset
        double actual = MathUtils.reduce(input, period, offset);
        assertEquals(output, actual, 1e-12);
    }

    @Test
    public void testReduceEdgeCases() {
        double input = 0;
        double period = 1;
        double offset = 0;
        double output = 0; // 0 - 1 * floor(0 / 1) - 0
        double actual = MathUtils.reduce(input, period, offset);
        assertEquals(output, actual, 1e-12);

        input = 1;
        period = 1;
        output = 0; // 1 - 1 * floor(1 / 1) - 0
        actual = MathUtils.reduce(input, period, offset);
        assertEquals(output, actual, 1e-12);

        input = 1;
        period = 2;
        output = 1; // 1 - 2 * floor(1 / 2) - 0
        actual = MathUtils.reduce(input, period, offset);
        assertEquals(output, actual, 1e-12);

        input = 2;
        period = 2;
        output = 0; // 2 - 2 * floor(2 / 2) - 0
        actual = MathUtils.reduce(input, period, offset);
        assertEquals(output, actual, 1e-12);

        input = -1;
        period = 2;
        output = -1; // -1 - 2 * floor(-1 / 2) - 0
        actual = MathUtils.reduce(input, period, offset);
        assertEquals(output, actual, 1e-12);
    }

    @Test
    public void testCopySignByte() throws MathArithmeticException {
        byte magnitude = 5;
        byte sign = 1;
        byte expected = 5; // copySign(magnitude, sign)
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignByteNegativeMagnitudeAndPositiveSign() throws MathArithmeticException {
        byte magnitude = -5;
        byte sign = 1;
        byte expected = -5; // copySign(magnitude, sign)
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test
    public void testCopySignByteNegativeMagnitudeAndNegativeSign() throws MathArithmeticException {
        byte magnitude = -5;
        byte sign = -1;
        byte expected = 5; // copySign(magnitude, sign)
        byte actual = MathUtils.copySign(magnitude, sign);
        assertEquals(expected, actual);
    }

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteOverflow() throws MathArithmeticException {
        byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // expected exception
    }

    // Repeat the above test cases for short, int, long

    @Test
    public void testCheckFiniteDouble() {
        double input = 1.23;
        MathUtils.checkFinite(input);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleNaN() {
        double input = Double.NaN;
        MathUtils.checkFinite(input); // expected exception
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleInfinite() {
        double input = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(input); // expected exception
    }

    @Test
    public void testCheckFiniteDoubleArray() {
        double[] input = {1.23, 2.718, 0.1};
        MathUtils.checkFinite(input);
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayNaN() {
        double[] input = {Double.NaN, 2.718, 0.1};
        MathUtils.checkFinite(input); // expected exception
    }

    @Test(expected = NotFiniteNumberException.class)
    public void testCheckFiniteDoubleArrayInfinite() {
        double[] input = {1.23, Double.POSITIVE_INFINITY, 0.1};
        MathUtils.checkFinite(input); // expected exception
    }
}