package org.apache.commons.cli.test; // or the actual package name of your test class

import static com.google.common.math.BigIntegerMath.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(isPowerOfTwo(BigInteger.ONE));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(1L << 31)));
        assertFalse(isPowerOfTwo(BigInteger.TEN));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(-1)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(31, log2(BigInteger.valueOf(1L << 31), RoundingMode.DOWN));
        assertEquals(32, log2(BigInteger.valueOf(1L << 32), RoundingMode.UP));
        assertEquals(32, log2(BigInteger.valueOf(1L << 33), RoundingMode.HALF_UP));
        assertEquals(-1, log2(BigInteger.ZERO, RoundingMode.DOWN));
    }

    @Test(expected = AssertionError.class)
    public void testLog2InvalidMode() {
        log2(BigInteger.ONE, RoundingMode.FOO);
    }

    @Test
    public void testLog10() {
        // Note: These tests assume that fitsInLong(BigInteger.TEN.pow(n)) is true for n <= 20.
        // You may need to adjust the test cases if this is not the case.
        assertEquals(0, log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, log10(BigInteger.TEN, RoundingMode.DOWN));
        assertEquals(2, log10(BigInteger.TEN.pow(2), RoundingMode.DOWN));
        assertEquals(3, log10(BigInteger.TEN.pow(3), RoundingMode.HALF_UP));
        assertEquals(4, log10(BigInteger.TEN.pow(4), RoundingMode.HALF_UP));
        assertEquals(5, log10(BigInteger.TEN.pow(5), RoundingMode.HALF_UP));
        assertEquals(6, log10(BigInteger.TEN.pow(6), RoundingMode.HALF_UP));
        assertEquals(7, log10(BigInteger.TEN.pow(7), RoundingMode.HALF_UP));
        assertEquals(8, log10(BigInteger.TEN.pow(8), RoundingMode.HALF_UP));
        assertEquals(9, log10(BigInteger.TEN.pow(9), RoundingMode.HALF_UP));
        assertEquals(10, log10(BigInteger.TEN.pow(10), RoundingMode.HALF_UP));
        assertEquals(11, log10(BigInteger.TEN.pow(11), RoundingMode.HALF_UP));
        assertEquals(12, log10(BigInteger.TEN.pow(12), RoundingMode.HALF_UP));
        assertEquals(13, log10(BigInteger.TEN.pow(13), RoundingMode.HALF_UP));
        assertEquals(14, log10(BigInteger.TEN.pow(14), RoundingMode.HALF_UP));
        assertEquals(15, log10(BigInteger.TEN.pow(15), RoundingMode.HALF_UP));
        assertEquals(16, log10(BigInteger.TEN.pow(16), RoundingMode.HALF_UP));
        assertEquals(17, log10(BigInteger.TEN.pow(17), RoundingMode.HALF_UP));
        assertEquals(18, log10(BigInteger.TEN.pow(18), RoundingMode.HALF_UP));
        assertEquals(19, log10(BigInteger.TEN.pow(19), RoundingMode.HALF_UP));
        assertEquals(20, log10(BigInteger.TEN.pow(20), RoundingMode.HALF_UP));

        BigDecimal expected = new BigDecimal("3.015446955232828531582034340449");
        BigInteger x = BigInteger.TEN.pow(21);
        BigInteger actual = log10(x, RoundingMode.HALF_UP);
        BigDecimal actualDec = new BigDecimal(actual);
        assertEquals(expected, actualDec.setScale(15, BigDecimal.ROUND_HALF_UP));
    }

    @Test(expected = AssertionError.class)
    public void testLog10InvalidMode() {
        log10(BigInteger.ONE, RoundingMode.FOO);
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.TEN, sqrt(BigInteger.TEN.pow(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(Math.sqrt(2.0)), sqrt(BigInteger.valueOf(2), RoundingMode.HALF_UP));

        BigInteger x = BigInteger.valueOf(16777215L);
        BigInteger actual = sqrt(x, RoundingMode.UP);
        BigInteger expected = BigInteger.valueOf(129325993);
        assertEquals(expected, actual);

        x = BigInteger.valueOf(Long.MAX_VALUE);
        actual = sqrt(x, RoundingMode.DOWN);
        expected = BigInteger.valueOf(Long.MAX_VALUE >> 1);
        assertEquals(expected, actual);

        x = BigInteger.valueOf(Long.MIN_VALUE);
        actual = sqrt(x, RoundingMode.UP);
        expected = BigInteger.valueOf(-Long.MAX_VALUE >> 1);
        assertEquals(expected.negate(), actual);
    }

    @Test(expected = AssertionError.class)
    public void testSqrtInvalidMode() {
        sqrt(BigInteger.ONE, RoundingMode.FOO);
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, factorial(0));
        assertEquals(BigInteger.ONE, factorial(1));
        assertEquals(BigInteger.TEN, factorial(2));
        assertEquals(BigInteger.valueOf(2432902008176640000L), factorial(20));
        assertEquals(BigInteger.valueOf(87178291200L), factorial(12));
        assertEquals(BigInteger.valueOf(511200000L), factorial(10));

        // Note: These tests assume that fitsInLong(BigInteger.TEN.pow(n)) is true for n <= 20.
        // You may need to adjust the test cases if this is not the case.
        BigInteger x = BigInteger.TEN.pow(21);
        BigInteger actual = factorial(21);
        BigInteger expected = x;
        assertEquals(expected, actual);
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, binomial(0, 0));
        assertEquals(BigInteger.ONE, binomial(1, 0));
        assertEquals(BigInteger.ONE, binomial(1, 1));
        assertEquals(BigInteger.TEN, binomial(2, 1));
        assertEquals(BigInteger.valueOf(3L), binomial(3, 2));
        assertEquals(BigInteger.valueOf(10L), binomial(4, 3));
        assertEquals(BigInteger.valueOf(21L), binomial(5, 4));
        assertEquals(BigInteger.valueOf(362880L), binomial(9, 8));
        assertEquals(BigInteger.valueOf(13953486022008176640000L), binomial(20, 19));

        // Note: These tests assume that fitsInLong(BigInteger.TEN.pow(n)) is true for n <= 20.
        // You may need to adjust the test cases if this is not the case.
        BigInteger n = BigInteger.TEN.pow(21);
        BigInteger k = BigInteger.valueOf(20);
        BigInteger actual = binomial(n.intValue(), k.intValue());
        BigInteger expected = BigInteger.valueOf(LongMath.binomial(n.intValue(), k.intValue()));
        assertEquals(expected, actual);
    }

    @Test
    public void testDivide() {
        BigDecimal d1 = new BigDecimal("10.5");
        BigDecimal d2 = new BigDecimal("3.5");
        BigDecimal expected = d1.divide(d2, 0, RoundingMode.HALF_UP);
        BigInteger p = BigInteger.TEN.pow(3);
        BigInteger q = BigInteger.TEN.pow(2);
        BigInteger actual = divide(p, q, RoundingMode.HALF_UP);
        BigInteger expectedInt = expected.toBigIntegerExact();
        assertEquals(expectedInt, actual);
    }

    @Test(expected = AssertionError.class)
    public void testDivideInvalidMode() {
        divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.FOO);
    }
}