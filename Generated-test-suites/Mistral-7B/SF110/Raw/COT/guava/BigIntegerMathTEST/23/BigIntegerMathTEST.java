package com.google.common.math; // import statements go here if needed

import static org.junit.Assert.*;

import org.junit.Test;

public class BigIntegerMathTest {
    

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(128);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(129);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(-128);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2() {
        BigInteger x = BigInteger.ONE;
        int expected = 0;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.DOWN));

        x = BigInteger.valueOf(3);
        expected = 1;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.DOWN));

        x = BigInteger.valueOf(8);
        expected = 3;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.UP));

        x = BigInteger.valueOf(10);
        expected = 3;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.CEILING));

        x = BigInteger.valueOf(11);
        expected = 4;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.HALF_UP));

        x = BigInteger.valueOf(12);
        expected = 4;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.HALF_UP));

        x = BigInteger.valueOf(Long.MAX_VALUE);
        expected = 64;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.DOWN));

        x = BigInteger.valueOf(Long.MAX_VALUE.longValue() << 1);
        expected = 65;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.UP));

        x = BigInteger.valueOf(Long.MAX_VALUE.longValue() << 1 | 1);
        expected = 65;
        assertEquals(expected, BigIntegerMath.log2(x, RoundingMode.CEILING));
    }

    @Test(expected = AssertionError.class)
    public void testLog2WithInvalidRoundingMode() {
        BigInteger x = BigInteger.ONE;
        BigIntegerMath.log2(x, RoundingMode.MIDPOINT); // Unsupported rounding mode
    }

    @Test
    public void testLog10() {
        BigInteger x = BigInteger.ONE;
        double expected = 0.0;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.DOWN), 0.0);

        x = BigInteger.valueOf(10);
        expected = 1.0;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.DOWN), 0.0);

        x = BigInteger.valueOf(100);
        expected = 2.0;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.DOWN), 0.0);

        x = BigInteger.valueOf(1024);
        expected = 3.0;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.UP), 0.0);

        x = BigInteger.valueOf(4096);
        expected = 3.0;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.CEILING), 0.0);

        x = BigInteger.valueOf(8128);
        expected = 3.0;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.HALF_UP), 0.0);

        x = BigInteger.valueOf(Long.MAX_VALUE);
        expected = 31.0;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.DOWN), 0.0);

        x = BigInteger.valueOf(Long.MAX_VALUE.longValue() << 1);
        expected = 32.0;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.UP), 0.0);

        x = BigInteger.valueOf(Long.MAX_VALUE.longValue() << 1 | 1);
        expected = 32.0;
        assertEquals(expected, BigIntegerMath.log10(x, RoundingMode.CEILING), 0.0);
    }

    @Test(expected = AssertionError.class)
    public void testLog10WithInvalidRoundingMode() {
        BigInteger x = BigInteger.ONE;
        BigIntegerMath.log10(x, RoundingMode.MIDPOINT); // Unsupported rounding mode
    }

    @Test
    public void testSqrt() {
        BigInteger x = BigInteger.ONE;
        BigInteger expected = BigInteger.ONE;
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));

        x = BigInteger.valueOf(4);
        expected = BigInteger.valueOf(2);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));

        x = BigInteger.valueOf(16);
        expected = BigInteger.valueOf(4);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));

        x = BigInteger.valueOf(25);
        expected = BigInteger.valueOf(5);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));

        x = BigInteger.valueOf(289);
        expected = BigInteger.valueOf(17);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.UP));

        x = BigInteger.valueOf(1225);
        expected = BigInteger.valueOf(35);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.CEILING));

        x = BigInteger.valueOf(122881);
        expected = BigInteger.valueOf(350);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.HALF_UP));

        x = BigInteger.valueOf(Long.MAX_VALUE);
        expected = BigInteger.valueOf(Long.MAX_VALUE);
        assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));
    }

    @Test(expected = AssertionError.class)
    public void testSqrtWithInvalidRoundingMode() {
        BigInteger x = BigInteger.ONE;
        BigIntegerMath.sqrt(x, RoundingMode.MIDPOINT); // Unsupported rounding mode
    }

    @Test
    public void testFactorial() {
        int n = 0;
        BigInteger expected = BigInteger.ONE;
        assertEquals(expected, BigIntegerMath.factorial(n));

        n = 1;
        expected = BigInteger.ONE;
        assertEquals(expected, BigIntegerMath.factorial(n));

        n = 2;
        expected = BigInteger.TEN;
        assertEquals(expected, BigIntegerMath.factorial(n));

        n = 5;
        expected = BigInteger.TEN.multiply(BigInteger.TEN.multiply(BigInteger.THREE).multiply(BigInteger.FIVE));
        assertEquals(expected, BigIntegerMath.factorial(n));

        n = 10;
        expected = factorial(10); // Manually computed factorial for comparison
        assertEquals(expected, BigIntegerMath.factorial(n));

        n = Integer.MAX_VALUE;
        expected = BigInteger.valueOf(LongMath.factorial(n));
        assertEquals(expected, BigIntegerMath.factorial(n));
    }

    @Test
    public void testBinomial() {
        int n = 0;
        int k = 0;
        BigInteger expected = BigInteger.ONE;
        assertEquals(expected, BigIntegerMath.binomial(n, k));

        n = 1;
        k = 1;
        expected = BigInteger.ONE;
        assertEquals(expected, BigIntegerMath.binomial(n, k));

        n = 2;
        k = 1;
        expected = BigInteger.TEN;
        assertEquals(expected, BigIntegerMath.binomial(n, k));

        n = 2;
        k = 2;
        expected = BigInteger.TEN.add(BigInteger.ONE);
        assertEquals(expected, BigIntegerMath.binomial(n, k));

        n = 5;
        k = 3;
        expected = BigInteger.valueOf(120);
        assertEquals(expected, BigIntegerMath.binomial(n, k));

        n = 12;
        k = 7;
        expected = BigInteger.valueOf(924);
        assertEquals(expected, BigIntegerMath.binomial(n, k));

        n = 21;
        k = 12;
        expected = BigInteger.valueOf(BigInteger.valueOf(21).multiply(BigInteger.valueOf(21).shiftLeft(1)).multiply(BigInteger.valueOf(11).multiply(BigInteger.valueOf(11).shiftLeft(1))).multiply(BigInteger.valueOf(11).shiftLeft(3)));
        assertEquals(expected, BigIntegerMath.binomial(n, k));
    }

    
}