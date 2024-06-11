package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.*;
import com.google.common.annotations.*;
import java.math.*;
import java.util.*;

@GwtCompatible(emulated = true)
public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(8)));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(isPowerOfTwo(BigInteger.ZERO));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(-1)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, log2(BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(3, log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(3, log2(BigInteger.valueOf(16), RoundingMode.DOWN));
        assertEquals(4, log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(4, log2(BigInteger.valueOf(25), RoundingMode.UP));
        assertEquals(5, log2(BigInteger.valueOf(32), RoundingMode.UP));
        assertEquals(-1, log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(-1, log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(4, log2(BigInteger.valueOf(15), RoundingMode.HALF_UP));
        assertEquals(5, log2(BigInteger.valueOf(31), RoundingMode.HALF_UP));
    }

    @Test(expected = AssertionError.class)
    public void testLog2InvalidMode() {
        log2(BigInteger.ONE, RoundingMode.INVALID);
    }

    @Test
    public void testLog10() {
        assertEquals(0, log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(2, log10(BigInteger.valueOf(100), RoundingMode.DOWN));
        assertEquals(3, log10(BigInteger.valueOf(1000), RoundingMode.DOWN));
        assertEquals(3, log10(BigInteger.valueOf(10000), RoundingMode.DOWN));
        assertEquals(4, log10(BigInteger.valueOf(100000), RoundingMode.UP));
        assertEquals(5, log10(BigInteger.valueOf(1000000), RoundingMode.UP));
        assertEquals(6, log10(BigInteger.valueOf(10000000), RoundingMode.UP));
        assertEquals(7, log10(BigInteger.valueOf(100000000), RoundingMode.UP));
        assertEquals(8, log10(BigInteger.valueOf(1000000000), RoundingMode.UP));
        assertEquals(-1, log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(-1, log10(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(2, log10(BigInteger.valueOf(1024), RoundingMode.HALF_UP));
        assertEquals(3, log10(BigInteger.valueOf(16843009), RoundingMode.HALF_UP));
    }

    @Test(expected = AssertionError.class)
    public void testLog10InvalidMode() {
        log10(BigInteger.ONE, RoundingMode.INVALID);
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), sqrt(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5), sqrt(BigInteger.valueOf(25), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(6), sqrt(BigInteger.valueOf(36), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(13), sqrt(BigInteger.valueOf(169), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(17), sqrt(BigInteger.valueOf(289), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(10), sqrt(BigInteger.valueOf(100), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(20), sqrt(BigInteger.valueOf(400), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(30), sqrt(BigInteger.valueOf(900), RoundingMode.HALF_UP));
        assertEquals(null, sqrt(BigInteger.ZERO, RoundingMode.ANY));
        assertEquals(null, sqrt(BigInteger.NEGATIVE_ONE, RoundingMode.ANY));
    }

    @Test(expected = AssertionError.class)
    public void testSqrtInvalidMode() {
        sqrt(BigInteger.ONE, RoundingMode.INVALID);
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(1), divide(BigInteger.valueOf(2), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), divide(BigInteger.valueOf(6), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(4), divide(BigInteger.valueOf(8), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(5), divide(BigInteger.valueOf(10), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(7), divide(BigInteger.valueOf(14), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(8), divide(BigInteger.valueOf(16), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(10), divide(BigInteger.valueOf(20), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(11), divide(BigInteger.valueOf(22), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(12), divide(BigInteger.valueOf(24), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(15), divide(BigInteger.valueOf(30), BigInteger.valueOf(2), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(16), divide(BigInteger.valueOf(32), BigInteger.valueOf(2), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(17), divide(BigInteger.valueOf(34), BigInteger.valueOf(2), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(-1), divide(BigInteger.valueOf(-2), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-2), divide(BigInteger.valueOf(-4), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-3), divide(BigInteger.valueOf(-6), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-4), divide(BigInteger.valueOf(-8), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(-5), divide(BigInteger.valueOf(-10), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(-7), divide(BigInteger.valueOf(-14), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(-8), divide(BigInteger.valueOf(-16), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(-10), divide(BigInteger.valueOf(-20), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(-11), divide(BigInteger.valueOf(-22), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(-12), divide(BigInteger.valueOf(-24), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(-15), divide(BigInteger.valueOf(-30), BigInteger.valueOf(2), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(-16), divide(BigInteger.valueOf(-32), BigInteger.valueOf(2), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(-17), divide(BigInteger.valueOf(-34), BigInteger.valueOf(2), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(0), divide(BigInteger.ZERO, BigInteger.ONE, RoundingMode.ANY));
        assertEquals(BigInteger.ZERO, divide(BigInteger.valueOf(0), BigInteger.ONE, RoundingMode.ANY));
        assertEquals(BigInteger.ZERO, divide(BigInteger.valueOf(0), BigInteger.ZERO, RoundingMode.ANY));
        assertEquals(BigInteger.ONE, divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.ANY));
        assertEquals(BigInteger.ONE, divide(BigInteger.ONE, BigInteger.valueOf(-1), RoundingMode.DOWN));
        assertEquals(BigInteger.ONE, divide(BigInteger.ONE, BigInteger.valueOf(-1), RoundingMode.UP));
        assertEquals(BigInteger.ONE, divide(BigInteger.valueOf(-1), BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.ONE, divide(BigInteger.valueOf(-1), BigInteger.ONE, RoundingMode.UP));
        assertEquals(BigInteger.ONE, divide(BigInteger.ONE, BigInteger.valueOf(10000), RoundingMode.DOWN));
        assertEquals(BigInteger.ONE, divide(BigInteger.ONE, BigInteger.valueOf(10000), RoundingMode.UP));
    }

    @Test(expected = AssertionError.class)
    public void testDivideInvalidMode() {
        divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.INVALID);
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, factorial(0));
        assertEquals(BigInteger.ONE, factorial(1));
        assertEquals(BigInteger.valueOf(2), factorial(2));
        assertEquals(BigInteger.valueOf(6), factorial(3));
        assertEquals(BigInteger.valueOf(24), factorial(4));
        assertEquals(BigInteger.valueOf(120), factorial(5));
        assertEquals(BigInteger.valueOf(720), factorial(6));
        assertEquals(BigInteger.valueOf(5040), factorial(7));
        assertEquals(BigInteger.valueOf(40320), factorial(8));
        assertEquals(BigInteger.valueOf(362880), factorial(9));
        assertEquals(BigInteger.valueOf(3628800), factorial(10));
        assertEquals(BigInteger.valueOf(39916800), factorial(11));
        assertEquals(BigInteger.valueOf(479001600), factorial(12));
        assertEquals(BigInteger.valueOf(6227020800), factorial(13));
        assertEquals(BigInteger.valueOf(87178291200), factorial(14));
        assertEquals(BigInteger.valueOf(1307674368000), factorial(15));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, binomial(0, 0));
        assertEquals(BigInteger.ONE, binomial(1, 0));
        assertEquals(BigInteger.ONE, binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), binomial(3, 1));
        assertEquals(BigInteger.valueOf(24), binomial(4, 1));
        assertEquals(BigInteger.valueOf(120), binomial(5, 1));
        assertEquals(BigInteger.valueOf(720), binomial(6, 1));
        assertEquals(BigInteger.valueOf(5040), binomial(7, 1));
        assertEquals(BigInteger.valueOf(40320), binomial(8, 1));
        assertEquals(BigInteger.valueOf(362880), binomial(9, 1));
        assertEquals(BigInteger.valueOf(3628800), binomial(10, 1));
        assertEquals(BigInteger.valueOf(39916800), binomial(11, 1));
        assertEquals(BigInteger.valueOf(479001600), binomial(12, 1));
        assertEquals(BigInteger.valueOf(6227020800), binomial(13, 1));
        assertEquals(BigInteger.valueOf(87178291200), binomial(14, 1));
        assertEquals(BigInteger.valueOf(1307674368000), binomial(15, 1));
        assertEquals(BigInteger.valueOf(3), binomial(3, 2));
        assertEquals(BigInteger.valueOf(15), binomial(4, 2));
        assertEquals(BigInteger.valueOf(105), binomial(5, 2));
        assertEquals(BigInteger.valueOf(1005), binomial(6, 2));
        assertEquals(BigInteger.valueOf(1320), binomial(7, 2));
        assertEquals(BigInteger.valueOf(2835), binomial(8, 2));
        assertEquals(BigInteger.valueOf(16804), binomial(9, 2));
        assertEquals(BigInteger.valueOf(65728), binomial(10, 2));
        assertEquals(BigInteger.valueOf(362880), binomial(11, 2));
        assertEquals(BigInteger.valueOf(230060480), binomial(12, 2));
        assertEquals(BigInteger.valueOf(1847566400), binomial(13, 2));
        assertEquals(BigInteger.valueOf(14345636800), binomial(14, 2));
        assertEquals(BigInteger.valueOf(114409690721600), binomial(15, 2));
    }

    @Test
    public void testFitsInLong() {
        assertTrue(fitsInLong(BigInteger.ONE));
        assertTrue(fitsInLong(BigInteger.valueOf(1234567890123456L)));
        assertFalse(fitsInLong(BigInteger.valueOf(1234567890123457L)));
    }
}