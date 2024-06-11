package org.apache.commons.cli.test; // replace with the correct package name if needed

import static com.google.common.math.BigIntegerMath.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(8)));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(isPowerOfTwo(BigInteger.ZERO));
        assertFalse(isPowerOfTwo(BigInteger.ONE.negate()));
    }

    @Test
    public void testLog2WithDifferentModes() {
        BigInteger x = BigInteger.valueOf(5);

        assertEquals(3, log2(x, RoundingMode.DOWN));
        assertEquals(3, log2(x, RoundingMode.CEILING));
        assertEquals(2, log2(x, RoundingMode.HALF_DOWN));
        assertEquals(3, log2(x, RoundingMode.HALF_UP));
        assertEquals(3, log2(x, RoundingMode.HALF_EVEN));
    }

    @Test
    public void testLog2WithLargeValue() {
        BigInteger x = BigInteger.valueOf(353557615);

        assertEquals(25, log2(x, RoundingMode.DOWN));
        assertEquals(26, log2(x, RoundingMode.CEILING));
        assertEquals(25, log2(x, RoundingMode.HALF_DOWN));
        assertEquals(26, log2(x, RoundingMode.HALF_UP));
        assertEquals(26, log2(x, RoundingMode.HALF_EVEN));
    }

    @Test(expected = AssertionError.class)
    public void testLog2WithInvalidMode() {
        log2(BigInteger.ONE, RoundingMode.valueOf("INVALID_MODE"));
    }

    @Test
    public void testLog10WithRoundingModes() {
        BigInteger x = BigInteger.valueOf(12345);

        assertEquals(4, log10(x, RoundingMode.DOWN));
        assertEquals(5, log10(x, RoundingMode.CEILING));
        assertEquals(4, log10(x, RoundingMode.HALF_DOWN));
        assertEquals(5, log10(x, RoundingMode.HALF_UP));
        assertEquals(5, log10(x, RoundingMode.HALF_EVEN));
    }

    @Test
    public void testLog10WithSmallValue() {
        BigInteger x = BigInteger.valueOf(123);

        assertEquals(2, log10(x, RoundingMode.DOWN));
        assertEquals(3, log10(x, RoundingMode.CEILING));
        assertEquals(2, log10(x, RoundingMode.HALF_DOWN));
        assertEquals(3, log10(x, RoundingMode.HALF_UP));
        assertEquals(3, log10(x, RoundingMode.HALF_EVEN));
    }

    @Test(expected = AssertionError.class)
    public void testLog10WithInvalidMode() {
        log10(BigInteger.ONE, RoundingMode.valueOf("INVALID_MODE"));
    }

    @Test
    public void testSqrtWithRoundingModes() {
        BigInteger x = BigInteger.valueOf(9);

        assertEquals(3, sqrt(x, RoundingMode.DOWN).intValue());
        assertEquals(3, sqrt(x, RoundingMode.CEILING).intValue());
        assertEquals(3, sqrt(x, RoundingMode.HALF_DOWN).intValue());
        assertEquals(3, sqrt(x, RoundingMode.HALF_UP).intValue());
        assertEquals(3, sqrt(x, RoundingMode.HALF_EVEN).intValue());
    }

    @Test
    public void testSqrtWithLargeValue() {
        BigInteger x = BigInteger.valueOf(16_025_200_001);

        assertEquals(1263, sqrt(x, RoundingMode.DOWN).intValue());
        assertEquals(1264, sqrt(x, RoundingMode.CEILING).intValue());
        assertEquals(1263, sqrt(x, RoundingMode.HALF_DOWN).intValue());
        assertEquals(1264, sqrt(x, RoundingMode.HALF_UP).intValue());
        assertEquals(1264, sqrt(x, RoundingMode.HALF_EVEN).intValue());
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, factorial(0));
        assertEquals(BigInteger.valueOf(1), factorial(1));
        assertEquals(BigInteger.valueOf(2), factorial(2));
        assertEquals(BigInteger.valueOf(6), factorial(3));
        assertEquals(BigInteger.valueOf(24), factorial(4));
        assertEquals(BigInteger.valueOf(120), factorial(5));
        assertEquals(BigInteger.valueOf(720), factorial(6));
        assertEquals(BigInteger.valueOf(5040), factorial(7));
        assertEquals(BigInteger.valueOf(40320), factorial(8));
        assertEquals(BigInteger.valueOf(362880), factorial(9));
        assertEquals(BigInteger.valueOf(3628800), factorial(10));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.valueOf(1), binomial(5, 0));
        assertEquals(BigInteger.ONE, binomial(5, 1));
        assertEquals(BigInteger.valueOf(5), binomial(5, 2));
        assertEquals(BigInteger.valueOf(10), binomial(5, 3));
        assertEquals(BigInteger.valueOf(10), binomial(5, 4));
        assertEquals(BigInteger.valueOf(10), binomial(5, 5));

        assertEquals(BigInteger.valueOf(21), binomial(6, 2));
        assertEquals(BigInteger.valueOf(20), binomial(6, 3));
        assertEquals(BigInteger.valueOf(15), binomial(6, 4));
        assertEquals(BigInteger.valueOf(6), binomial(6, 5));

        assertEquals(BigInteger.valueOf(20), binomial(10, 3));
        assertEquals(BigInteger.valueOf(120), binomial(10, 5));
        assertEquals(BigInteger.valueOf(252), binomial(10, 6));
        assertEquals(BigInteger.valueOf(62270), binomial(10, 8));
        assertEquals(BigInteger.valueOf(3628800), binomial(10, 10));
    }

    @Test
    public void testDivideWithRoundingModes() {
        BigInteger dividend = BigInteger.valueOf(100);
        BigInteger divisor = BigInteger.valueOf(5);

        assertEquals(BigInteger.valueOf(20), divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(21), divide(dividend, divisor, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(20), divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(21), divide(dividend, divisor, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(20), divide(dividend, divisor, RoundingMode.HALF_EVEN));
    }
}