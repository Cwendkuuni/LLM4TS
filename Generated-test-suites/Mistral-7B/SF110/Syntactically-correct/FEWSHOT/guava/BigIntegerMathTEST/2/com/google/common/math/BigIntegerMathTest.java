package org.apache.commons.cli.test; // or your desired package name

import static com.google.common.math.BigIntegerMath.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(isPowerOfTwo(BigInteger.ONE));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(16)));
        assertFalse(isPowerOfTwo(BigInteger.ONE.add(BigInteger.ONE)));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(13)));
    }

    @Test
    public void testLog2() {
        assertEquals(3, log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(4, log2(BigInteger.valueOf(16), RoundingMode.DOWN));
        assertEquals(3, log2(BigInteger.valueOf(27), RoundingMode.UP));
        assertEquals(4, log2(BigInteger.valueOf(256), RoundingMode.CEILING));
        assertEquals(31, log2(BigInteger.valueOf(2147483648L), RoundingMode.HALF_UP));
    }

    @Test
    public void testLog10() {
        assertEquals(1, log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(2, log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(1, log10(BigInteger.valueOf(100), RoundingMode.UP));
        assertEquals(3, log10(BigInteger.valueOf(1000), RoundingMode.CEILING));
        assertEquals(11, log10(BigInteger.valueOf(1023), RoundingMode.HALF_UP));
    }

    @Test(expected = AssertionError.class)
    public void testLog10InvalidMode() {
        log10(BigInteger.ONE, RoundingMode.valueOf("INVALID_MODE"));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(2), sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), sqrt(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertNull(sqrt(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(17), sqrt(BigInteger.valueOf(289), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(16), sqrt(BigInteger.valueOf(256), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(16), sqrt(BigInteger.valueOf(256), RoundingMode.HALF_EVEN));
    }

    @Test(expected = AssertionError.class)
    public void testSqrtInvalidMode() {
        sqrt(BigInteger.ONE, RoundingMode.valueOf("INVALID_MODE"));
    }

    @Test
    public void testDivide() {
        BigInteger dividend = BigInteger.valueOf(12345678901234567890L);
        BigInteger divisor = BigInteger.valueOf(3);

        assertEquals(BigInteger.valueOf(41145486011558863643), divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(41145486011558863644), divide(dividend, divisor, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(41145486011558863643L), divide(dividend, divisor, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(41145486011558863643L), divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(41145486011558863642), divide(dividend, divisor, RoundingMode.HALF_EVEN));
        assertEquals(BigInteger.valueOf(41145485958124272848), divide(dividend, divisor, RoundingMode.HALF_UP));

        BigDecimal dividendDec = new BigDecimal(dividend.toString());
        BigDecimal divisorDec = new BigDecimal(divisor.toString());
        assertEquals(dividendDec.divide(divisorDec, 0, RoundingMode.DOWN), divide(dividend, divisor, RoundingMode.DOWN).toBigDecimal());
    }

    @Test(expected = AssertionError.class)
    public void testDivideInvalidMode() {
        divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.valueOf("INVALID_MODE"));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, factorial(0));
        assertEquals(BigInteger.valueOf(1), factorial(1));
        assertEquals(BigInteger.valueOf(2), factorial(2));
        assertEquals(BigInteger.valueOf(24), factorial(4));
        assertEquals(BigInteger.valueOf(120), factorial(6));
        assertEquals(BigInteger.valueOf(511200), factorial(12));
        assertEquals(BigInteger.valueOf(6227020800), factorial(16));
        assertEquals(BigInteger.valueOf(1307674368000), factorial(20));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, binomial(0, 0));
        assertEquals(BigInteger.ONE, binomial(0, 1));
        assertEquals(BigInteger.ONE, binomial(1, 0));
        assertEquals(BigInteger.ONE, binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), binomial(3, 2));
        assertEquals(BigInteger.valueOf(20), binomial(4, 3));
        assertEquals(BigInteger.valueOf(120), binomial(6, 5));
        assertEquals(BigInteger.valueOf(2002), binomial(7, 5));
        assertEquals(BigInteger.valueOf(15504), binomial(8, 4));
        assertEquals(BigInteger.valueOf(3628800), binomial(10, 5));
        assertEquals(BigInteger.valueOf(237582561), binomial(12, 7));
        assertEquals(BigInteger.valueOf(24709364815), binomial(15, 8));
        assertEquals(BigInteger.valueOf(3527187883401), binomial(17, 9));
    }

    @Test
    public void testFitsInLong() {
        assertFalse(fitsInLong(BigInteger.valueOf(2147483648L).add(BigInteger.ONE)));
        assertTrue(fitsInLong(BigInteger.valueOf(2147483647L)));
    }
}