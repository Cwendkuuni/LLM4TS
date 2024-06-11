package com.google.common.math;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {
    // Basic tests

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        x = BigInteger.valueOf(16);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        x = BigInteger.valueOf(-16);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
        x = BigInteger.valueOf(27);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2() {
        BigInteger x = BigInteger.ONE;
        int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(0, result);

        x = BigInteger.valueOf(2);
        result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(1, result);

        x = BigInteger.valueOf(128);
        result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(7, result);

        x = BigInteger.valueOf(129);
        result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(7, result);

        x = BigInteger.valueOf(130);
        result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(8, result);
    }

    @Test
    public void testLog10() {
        BigInteger x = BigInteger.ONE;
        int result = BigIntegerMath.log10(x, RoundingMode.DOWN);
        assertEquals(0, result);

        x = BigInteger.valueOf(10);
        result = BigIntegerMath.log10(x, RoundingMode.DOWN);
        assertEquals(1, result);

        x = BigInteger.valueOf(100);
        result = BigIntegerMath.log10(x, RoundingMode.DOWN);
        assertEquals(2, result);

        x = BigInteger.valueOf(1000);
        result = BigIntegerMath.log10(x, RoundingMode.DOWN);
        assertEquals(3, result);

        x = BigInteger.valueOf(10000);
        result = BigIntegerMath.log10(x, RoundingMode.DOWN);
        assertEquals(4, result);
    }

    @Test
    public void testSqrt() {
        BigInteger x = BigInteger.ONE;
        BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(BigInteger.ONE, result);

        x = BigInteger.valueOf(4);
        result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(BigInteger.valueOf(2), result);

        x = BigInteger.valueOf(25);
        result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(BigInteger.valueOf(5), result);

        x = BigInteger.valueOf(26);
        result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(BigInteger.valueOf(5), result);

        x = BigInteger.valueOf(27);
        result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(BigInteger.valueOf(5).add(BigInteger.ONE), result);
    }

    @Test
    public void testDivide() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(2);
        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(BigInteger.valueOf(5), result);

        p = BigInteger.valueOf(101);
        q = BigInteger.valueOf(2);
        result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(BigInteger.valueOf(50), result);

        p = BigInteger.valueOf(100);
        q = BigInteger.valueOf(-2);
        result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(BigInteger.valueOf(-50), result);

        p = BigInteger.valueOf(100);
        q = BigInteger.valueOf(0);
        try {
            result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testFactorial() {
        int n = 0;
        BigInteger result = BigIntegerMath.factorial(n);
        assertEquals(BigInteger.ONE, result);

        n = 1;
        result = BigIntegerMath.factorial(n);
        assertEquals(BigInteger.valueOf(1), result);

        n = 5;
        result = BigIntegerMath.factorial(n);
        assertEquals(BigInteger.valueOf(120), result);

        n = 10;
        result = BigIntegerMath.factorial(n);
        assertEquals(BigInteger.valueOf(3628800), result);
    }

    @Test
    public void testBinomial() {
        int n = 0;
        int k = 0;
        BigInteger result = BigIntegerMath.binomial(n, k);
        assertEquals(BigInteger.ONE, result);

        n = 5;
        k = 2;
        result = BigIntegerMath.binomial(n, k);
        assertEquals(BigInteger.valueOf(10), result);

        n = 5;
        k = 5;
        result = BigIntegerMath.binomial(n, k);
        assertEquals(BigInteger.valueOf(1), result);

        n = 10;
        k = 3;
        result = BigIntegerMath.binomial(n, k);
        assertEquals(BigInteger.valueOf(142506), result);

        n = 20;
        k = 19;
        result = BigIntegerMath.binomial(n, k);
        assertEquals(BigInteger.valueOf(1377780783126280186564896000000), result);
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testIsPowerOfTwoNegativeNumber() {
        BigInteger x = BigInteger.valueOf(-16);
        BigIntegerMath.isPowerOfTwo(x);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLog2NegativeNumber() {
        BigInteger x = BigInteger.valueOf(-16);
        BigIntegerMath.log2(x, RoundingMode.DOWN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLog10NegativeNumber() {
        BigInteger x = BigInteger.valueOf(-10);
        BigIntegerMath.log10(x, RoundingMode.DOWN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSqrtNegativeNumber() {
        BigInteger x = BigInteger.valueOf(-16);
        BigIntegerMath.sqrt(x, RoundingMode.DOWN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideByZero() {
        BigInteger p = BigInteger.ONE;
        BigInteger q = BigInteger.ZERO;
        try {
            BigIntegerMath.divide(p, q, RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivideByOne() {
        BigInteger p = BigInteger.ONE;
        BigInteger q = BigInteger.ONE;
        try {
            BigIntegerMath.divide(p, q, RoundingMode.DOWN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFactorialLargeNumber() {
        int n = 100000;
        BigInteger result = BigIntegerMath.factorial(n);
        // This test doesn't assert anything, BigIntegerMath just calculates the factorial
        // It's important to note that this test takes a significant amount of time to complete
    }
}