package org.apache.commons.cli.test; // Assuming you want to place the test cases in a package named "org.apache.commons.cli.test"

import static org.junit.Assert.*;

import com.google.common.math.*;
import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(13)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-16)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(5, BigIntegerMath.log2(BigInteger.valueOf(32), RoundingMode.HALF_UP));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UP));
    }

    @Test(expected = AssertionError.class)
    public void testLog2WithInvalidRoundingMode() {
        BigIntegerMath.log2(BigInteger.ONE, RoundingMode.THREE_HALF);
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.HALF_UP));
        assertEquals(-1, BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UP));
    }

    @Test(expected = AssertionError.class)
    public void testLog10WithInvalidRoundingMode() {
        BigIntegerMath.log10(BigInteger.ONE, RoundingMode.THREE_HALF);
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(16), BigIntegerMath.sqrt(BigInteger.valueOf(256), RoundingMode.HALF_UP));
        assertEquals(null, BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(null, BigIntegerMath.sqrt(BigInteger.ONE.negate(), RoundingMode.UP));
    }

    @Test(expected = AssertionError.class)
    public void testSqrtWithInvalidRoundingMode() {
        BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.THREE_HALF);
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.factorial(6));
        assertEquals(BigInteger.valueOf(51120), BigIntegerMath.factorial(7));
        assertEquals(BigInteger.valueOf(479001600L), BigIntegerMath.factorial(10));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.ZERO, BigIntegerMath.divide(BigInteger.ZERO, BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(6), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.divide(BigInteger.valueOf(8), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(7), BigIntegerMath.divide(BigInteger.valueOf(14), BigInteger.valueOf(2), RoundingMode.HALF_UP));
        assertEquals(null, BigIntegerMath.divide(BigInteger.ONE, BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(null, BigIntegerMath.divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.UP));
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideWithArithmeticException() {
        BigIntegerMath.divide(BigInteger.ONE, BigInteger.ZERO, RoundingMode.DOWN);
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(4, 2));
        assertEquals(BigInteger.valueOf(20), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(7, 3));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.binomial(6, 5));
    }

    @Test
    public void testBinomialWithLargeInputs() {
        assertEquals(BigInteger.valueOf(479001600L), BigIntegerMath.binomial(10, 5));
        assertEquals(BigInteger.valueOf(270725), BigIntegerMath.binomial(15, 5));
        assertEquals(BigInteger.valueOf(87178291200L), BigIntegerMath.binomial(25, 10));
    }

    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.ONE));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE + 1)));
    }
}