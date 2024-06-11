package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Test cases for the BigIntegerMath class.
 */
public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = x.shiftLeft(32);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = x.add(BigInteger.ONE);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(5);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(-5);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2() {
        BigInteger x = BigInteger.ONE;
        int expected = 0;
        int actual = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(expected, actual);

        x = x.shiftLeft(10);
        expected = 10;
        actual = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(expected, actual);

        x = x.add(BigInteger.ONE);
        expected = 11;
        actual = BigIntegerMath.log2(x, RoundingMode.UP);
        assertEquals(expected, actual);

        x = BigInteger.valueOf(-5);
        try {
            BigIntegerMath.log2(x, RoundingMode.DOWN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        x = BigInteger.valueOf(-5).negate();
        expected = 4;
        actual = BigIntegerMath.log2(x, RoundingMode.HALF_UP);
        assertEquals(expected, actual);
    }

    @Test
    public void testLog10() {
        BigInteger x = BigInteger.ONE;
        RoundingMode mode = RoundingMode.DOWN;
        int expected = 0;
        int actual = BigIntegerMath.log10(x, mode).intValueExact();
        assertEquals(expected, actual);

        x = x.shiftLeft(10);
        expected = 3;
        actual = BigIntegerMath.log10(x, mode).intValueExact();
        assertEquals(expected, actual);

        mode = RoundingMode.UP;
        expected = 4;
        actual = BigIntegerMath.log10(x, mode).intValueExact();
        assertEquals(expected, actual);

        x = BigInteger.valueOf(-5);
        try {
            BigIntegerMath.log10(x, RoundingMode.DOWN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        x = BigInteger.valueOf(-5).negate();
        expected = 1;
        actual = BigIntegerMath.log10(x, RoundingMode.HALF_DOWN).intValueExact();
        assertEquals(expected, actual);
    }

    @Test
    public void testSqrt() {
        BigInteger x = BigInteger.ONE;
        RoundingMode mode = RoundingMode.DOWN;
        BigInteger expected = BigInteger.ONE;
        BigInteger actual = BigIntegerMath.sqrt(x, mode);
        assertEquals(expected, actual);

        x = x.shiftLeft(32);
        expected = x.shiftRight(1);
        actual = BigIntegerMath.sqrt(x, mode);
        assertEquals(expected, actual);

        x = BigInteger.valueOf(-1L);
        try {
            BigIntegerMath.sqrt(x, mode);
            fail();
        } catch (ArithmeticException e) {
            // Expected exception
        }

        x = BigInteger.valueOf(4);
        expected = BigInteger.valueOf(2);
        actual = BigIntegerMath.sqrt(x, mode);
        assertEquals(expected, actual);
    }

    @Test
    public void testFactorial() {
        int n = 0;
        assertSame(BigInteger.ONE, BigIntegerMath.factorial(n));

        n = 1;
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(n));

        n = 5;
        BigInteger expected = BigInteger.valueOf(120L);
        BigInteger actual = BigIntegerMath.factorial(n);
        assertEquals(expected, actual);

        n = 10;
        expected = BigInteger.valueOf(3628800L);
        actual = BigIntegerMath.factorial(n);
        assertEquals(expected, actual);

        n = Integer.MAX_VALUE;
        expected = BigInteger.valueOf(LongMath.factorials[Long.MAX_VALUE]);
        actual = BigIntegerMath.factorial(n);
        assertEquals(expected, actual);

        n = Integer.MAX_VALUE + 1;
        try {
            BigIntegerMath.factorial(n);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testBinomial() {
        int n = 5;
        int k = 2;
        int expected = 15;
        BigInteger actual = BigIntegerMath.binomial(n, k);
        assertEquals(expected, actual.intValueExact());

        n = 10;
        k = 4;
        expected = 3003;
        actual = BigIntegerMath.binomial(n, k);
        assertEquals(expected, actual.intValueExact());

        n = 21;
        k = 10;
        expected = BigInteger.valueOf(10228215521303322519L);
        actual = BigIntegerMath.binomial(n, k);
        assertEquals(expected, actual);

        n = 100;
        k = 50;
        expected = BigInteger.valueOf(BigInteger.ONE.longValue() << 75L);
        actual = BigIntegerMath.binomial(n, k);
        assertEquals(expected, actual);

        n = 200;
        k = 101;
        try {
            BigIntegerMath.binomial(n, k);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}