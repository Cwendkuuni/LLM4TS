package org.apache.commons.cli.test; // modify the package name if necessary

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.common.math.BigIntegerMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(256)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(4)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(5)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.CEILING));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.CEILING));
        assertEquals(5, BigIntegerMath.log2(BigInteger.valueOf(32), RoundingMode.DOWN));
        assertEquals(5, BigIntegerMath.log2(BigInteger.valueOf(32), RoundingMode.CEILING));
        assertEquals(Integer.MAX_VALUE, BigIntegerMath.log2(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.DOWN));
        assertEquals(Integer.MAX_VALUE, BigIntegerMath.log2(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.CEILING));
        assertEquals(Integer.MIN_VALUE, BigIntegerMath.log2(BigInteger.valueOf(Long.MIN_VALUE), RoundingMode.DOWN));
        assertEquals(Integer.MIN_VALUE, BigIntegerMath.log2(BigInteger.valueOf(Long.MIN_VALUE), RoundingMode.CEILING));
        assertEquals(Integer.MIN_VALUE, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(Integer.MIN_VALUE, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.CEILING));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.CEILING));
    }

    @Test
    public void testLog10() {
        // Implement testLog10() method when the @GwtIncompatible("TODO") annotation is removed.
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(17), BigIntegerMath.sqrt(BigInteger.valueOf(289), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(13), BigIntegerMath.sqrt(BigInteger.valueOf(169), RoundingMode.CEILING));

        // Implement test cases for other rounding modes and edge cases when the @GwtIncompatible("TODO") annotation is removed.
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.ZERO, BigIntegerMath.divide(BigInteger.ZERO, BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(6), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.divide(BigInteger.valueOf(16), BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(12), BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-2), BigIntegerMath.divide(BigInteger.valueOf(-4), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-3), BigIntegerMath.divide(BigInteger.valueOf(-6), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-5), BigIntegerMath.divide(BigInteger.valueOf(-10), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-4), BigIntegerMath.divide(BigInteger.valueOf(-16), BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-3), BigIntegerMath.divide(BigInteger.valueOf(-12), BigInteger.valueOf(4), RoundingMode.DOWN));

        // Implement test cases for other rounding modes and edge cases when the @GwtIncompatible("TODO") annotation is removed.
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1L));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2L));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3L));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4L));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5L));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.factorial(6));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.factorial(6L));
        assertEquals(BigInteger.valueOf(5040), BigIntegerMath.factorial(7));
        assertEquals(BigInteger.valueOf(5040), BigIntegerMath.factorial(7L));
        assertEquals(BigInteger.valueOf(40320), BigIntegerMath.factorial(8));
        assertEquals(BigInteger.valueOf(40320), BigIntegerMath.factorial(8L));
        assertEquals(BigInteger.valueOf(362880), BigIntegerMath.factorial(9));
        assertEquals(BigInteger.valueOf(362880), BigIntegerMath.factorial(9L));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(10));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(10L));

        // Implement test cases for larger numbers when the @GwtIncompatible("TODO") annotation is removed.
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.ZERO, BigIntegerMath.binomial(0, 2));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 2));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 0));
        assertEquals(BigInteger.TWO, BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.TWO, BigIntegerMath.binomial(2, 2));
        assertEquals(BigInteger.THREE, BigIntegerMath.binomial(3, 1));
        assertEquals(BigInteger.FIVE, BigIntegerMath.binomial(4, 2));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(21), BigIntegerMath.binomial(6, 2));
        assertEquals(BigInteger.valueOf(19683), BigIntegerMath.binomial(10, 3));
        assertEquals(BigInteger.valueOf(241784401), BigIntegerMath.binomial(20, 4));

        // Implement test cases for larger numbers and combinations when the @GwtIncompatible("TODO") annotation is removed.
    }

    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.ONE));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MIN_VALUE)));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE)));
    }
}