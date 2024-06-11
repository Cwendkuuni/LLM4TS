package org.apache.commons.cli.test; // Assuming a test package is available for the org.apache.commons.cli package

import static com.google.common.math.BigIntegerMath.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(8)));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(15)));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(-16)));
        assertFalse(isPowerOfTwo(null));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLog2() {
        assertEquals(0, log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(3, log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(3, log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(3, log2(BigInteger.valueOf(16), RoundingMode.CEILING));
        assertEquals(3, log2(BigInteger.valueOf(16), RoundingMode.HALF_UP));
        assertEquals(3, log2(BigInteger.valueOf(16), RoundingMode.HALF_DOWN));
        assertEquals(3, log2(BigInteger.valueOf(16), RoundingMode.HALF_EVEN));
        assertNull(log2(null, RoundingMode.DOWN));
    }

    @Test
    public void testLog10() {
        assertEquals(0, log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(2, log10(BigInteger.valueOf(100), RoundingMode.DOWN));
        assertEquals(1, log10(BigInteger.valueOf(100), RoundingMode.UP));
        assertEquals(2, log10(BigInteger.valueOf(100), RoundingMode.CEILING));
        assertEquals(2, log10(BigInteger.valueOf(100), RoundingMode.HALF_UP));
        assertEquals(1, log10(BigInteger.valueOf(100), RoundingMode.HALF_DOWN));
        assertEquals(1, log10(BigInteger.valueOf(100), RoundingMode.HALF_EVEN));
        assertEquals(3, log10(BigInteger.valueOf(100000), RoundingMode.DOWN));
        assertEquals(4, log10(BigInteger.valueOf(100000), RoundingMode.UP));
        assertEquals(4, log10(BigInteger.valueOf(100000), RoundingMode.CEILING));
        assertEquals(4, log10(BigInteger.valueOf(100000), RoundingMode.HALF_UP));
        assertEquals(3, log10(BigInteger.valueOf(100000), RoundingMode.HALF_DOWN));
        assertEquals(3, log10(BigInteger.valueOf(100000), RoundingMode.HALF_EVEN));
        assertNull(log10(null, RoundingMode.DOWN));
    }

    @Test(expected = AssertionError.class)
    public void testLog10WithInvalidMode() {
        log10(BigInteger.ONE, RoundingMode.ABSURD);
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), sqrt(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5), sqrt(BigInteger.valueOf(25), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(6), sqrt(BigInteger.valueOf(36), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(7), sqrt(BigInteger.valueOf(49), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(8), sqrt(BigInteger.valueOf(64), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(9), sqrt(BigInteger.valueOf(81), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(10), sqrt(BigInteger.valueOf(100), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(11), sqrt(BigInteger.valueOf(121), RoundingMode.CEILING));
        assertNull(sqrt(null, RoundingMode.DOWN));
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
        assertEquals(BigInteger.valueOf(51120), factorial(7));
        assertEquals(BigInteger.valueOf(479001600L), factorial(10));
        assertEquals(BigInteger.valueOf(3628800), factorial(12));
        assertEquals(BigInteger.valueOf(39916800L), factorial(15));
        assertEquals(BigInteger.valueOf(511200000), factorial(17));
        assertEquals(BigInteger.valueOf(6227020800L), factorial(20));
        assertEquals(BigInteger.valueOf(87178291200L), factorial(23));
        assertEquals(BigInteger.valueOf(1307674368000L), factorial(26));
        assertEquals(BigInteger.valueOf(3422048129312151505600L), factorial(30));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, binomial(0, 0));
        assertEquals(BigInteger.ONE, binomial(1, 0));
        assertEquals(BigInteger.ONE, binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), binomial(4, 2));
        assertEquals(BigInteger.valueOf(10), binomial(6, 3));
        assertEquals(BigInteger.valueOf(20), binomial(7, 3));
        assertEquals(BigInteger.valueOf(63), binomial(8, 3));
        assertEquals(BigInteger.valueOf(120), binomial(10, 4));
        assertEquals(BigInteger.valueOf(28656), binomial(15, 5));
        assertEquals(BigInteger.valueOf(184756), binomial(16, 5));
        assertEquals(BigInteger.valueOf(30030), binomial(17, 5));
        assertEquals(BigInteger.valueOf(28960), binomial(18, 5));
        assertEquals(BigInteger.valueOf(362880), binomial(20, 5));
        assertEquals(BigInteger.valueOf(1307674368000L), binomial(30, 10));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(3), divide(BigInteger.valueOf(9), BigInteger.valueOf(3), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(4), divide(BigInteger.valueOf(16), BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5), divide(BigInteger.valueOf(25), BigInteger.valueOf(5), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(12), divide(BigInteger.valueOf(36), BigInteger.valueOf(3), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(13), divide(BigInteger.valueOf(49), BigInteger.valueOf(4), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(15), divide(BigInteger.valueOf(64), BigInteger.valueOf(5), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(17), divide(BigInteger.valueOf(81), BigInteger.valueOf(7), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(20), divide(BigInteger.valueOf(100), BigInteger.valueOf(10), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(22), divide(BigInteger.valueOf(121), BigInteger.valueOf(11), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(100), divide(BigInteger.valueOf(10000), BigInteger.valueOf(100), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(1000), divide(BigInteger.valueOf(1000000), BigInteger.valueOf(1000), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(1000000), divide(BigInteger.valueOf(1000000000), BigInteger.valueOf(1000000), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(1), divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(1), divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(1), divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(1), divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(1), divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(1), divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.HALF_EVEN));
        assertNull(divide(null, BigInteger.ONE, RoundingMode.DOWN));
        assertNull(divide(BigInteger.ONE, null, RoundingMode.DOWN));
    }
}