package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(1 << 31);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(1 << 32);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(2).pow(10);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(-1);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2() {
        BigInteger x = BigInteger.ONE;

        for (RoundingMode mode : RoundingMode.values()) {
            int result = BigIntegerMath.log2(x, mode);
            assertEquals(0, result);

            x = x.multiply(BigInteger.TEN);
            result = BigIntegerMath.log2(x, mode);
            assertEquals(1, result);

            x = x.add(BigInteger.ONE);
            result = BigIntegerMath.log2(x, mode);
            assertEquals(1, result);
        }

        x = BigInteger.valueOf(1 << 255);
        for (RoundingMode mode : RoundingMode.values()) {
            if (mode != RoundingMode.UNNECESSARY) {
                try {
                    BigIntegerMath.log2(x, mode);
                    fail("Expected ArithmeticException");
                } catch (ArithmeticException e) {
                    // Expected
                }
            }
        }
    }

    @Test
    public void testLog10() {
        BigInteger x = BigInteger.ONE;

        for (RoundingMode mode : RoundingMode.values()) {
            int result = BigIntegerMath.log10(x, mode);
            assertEquals(0, result);

            x = x.multiply(BigInteger.TEN);
            result = BigIntegerMath.log10(x, mode);
            assertEquals(1, result);

            x = x.add(BigInteger.ONE);
            result = BigIntegerMath.log10(x, mode);
            assertEquals(1, result);
        }

        x = BigInteger.valueOf(1234567890123456789L);
        int result = BigIntegerMath.log10(x, RoundingMode.HALF_UP);
        assertEquals(11, result);

        x = BigInteger.valueOf(1234567890123456789L).add(BigInteger.ONE);
        result = BigIntegerMath.log10(x, RoundingMode.HALF_UP);
        assertEquals(12, result);

        x = BigInteger.valueOf(1234567890123456788L);
        result = BigIntegerMath.log10(x, RoundingMode.HALF_DOWN);
        assertEquals(11, result);

        x = BigInteger.valueOf(1234567890123456788L);
        result = BigIntegerMath.log10(x, RoundingMode.HALF_EVEN);
        assertEquals(11, result);

        x = BigInteger.valueOf(1234567890123456787L);
        int expectedResult = 11;
        try {
            result = BigIntegerMath.log10(x, RoundingMode.HALF_UP);
            assertEquals(expectedResult, result);
        } finally {
            assertEquals(expectedResult + 1, BigIntegerMath.log10(x, RoundingMode.HALF_DOWN));
            assertEquals(expectedResult, BigIntegerMath.log10(x, RoundingMode.HALF_EVEN));
        }
    }

    @Test
    public void testSqrt() {
        BigInteger x = BigInteger.ONE;

        for (RoundingMode mode : RoundingMode.values()) {
            BigInteger sqrt = BigIntegerMath.sqrt(x, mode);
            assertEquals(x, sqrt.pow(2));

            x = x.multiply(BigInteger.TEN);
            sqrt = BigIntegerMath.sqrt(x, mode);
            BigInteger expected = x;
            expected = expected.shiftRight(1);
            expected = expected.add(BigInteger.ONE).shiftRight(1);
            assertEquals(expected, sqrt);
        }

        x = BigInteger.valueOf(1 << 31);
        BigInteger sqrt = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
        assertEquals(x, sqrt.pow(2));

        x = BigInteger.valueOf(Long.MAX_VALUE);
        sqrt = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
        assertEquals(x, sqrt.pow(2));

        x = BigInteger.valueOf(Long.MIN_VALUE);
        try {
            sqrt = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        x = BigInteger.valueOf(1 << 63 - 1);
        try {
            sqrt = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testDivide() {
        BigInteger dividend = BigInteger.valueOf(1234567890123456789L);
        BigInteger divisor = BigInteger.valueOf(12345);

        for (RoundingMode mode : RoundingMode.values()) {
            BigInteger quotient = BigIntegerMath.divide(dividend, divisor, mode);
            BigInteger expected = dividend.divide(divisor);
            assertEquals(expected, quotient);

            dividend = dividend.add(BigInteger.ONE);
            divisor = divisor.multiply(BigInteger.TEN);
        }

        dividend = BigInteger.valueOf(Long.MIN_VALUE);
        divisor = BigInteger.ONE;
        try {
            BigInteger quotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        dividend = BigInteger.valueOf(Long.MIN_VALUE);
        divisor = BigInteger.valueOf(Long.MIN_VALUE);
        try {
            BigInteger quotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        dividend = BigInteger.valueOf(Long.MAX_VALUE);
        divisor = BigInteger.valueOf(Long.MIN_VALUE);
        try {
            BigInteger quotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testFactorial() {
        for (int n = 0; n <= 10; ++n) {
            BigInteger expected = BigInteger.valueOf(LongMath.factorial(n));
            BigInteger result = BigIntegerMath.factorial(n);
            assertEquals(expected, result);
        }

        int n = 11;
        BigInteger expected = BigInteger.valueOf(LongMath.factorial(n));
        BigInteger result = BigIntegerMath.factorial(n);
        assertEquals(expected, result);

        n = Integer.MAX_VALUE;
        expected = BigIntegerMath.factorial(n);
        result = BigIntegerMath.factorial(n);
        assertNotEquals(expected, result);
        assertTrue(result.isProbablePrime(10));
    }

    @Test
    public void testBinomial() {
        for (int n = 0; n <= 10; ++n) {
            for (int k = 0; k <= n; ++k) {
                BigInteger result = BigIntegerMath.binomial(n, k);
                BigInteger expected = BigInteger.valueOf(LongMath.binomial(n, k));
                assertEquals(expected, result);
            }
        }

        int n = 11;
        int k = n - 1;
        BigInteger result = BigIntegerMath.binomial(n, k);
        BigInteger expected = BigInteger.valueOf(LongMath.binomial(n, k));
        assertEquals(expected, result);

        n = 20;
        k = n / 2;
        result = BigIntegerMath.binomial(n, k);
        expected = BigInteger.valueOf(LongMath.binomial(n, k));
        assertEquals(expected, result);

        n = 21;
        k = n / 2;
        result = BigIntegerMath.binomial(n, k);
        expected = BigInteger.valueOf(LongMath.binomial(n, k));
        assertEquals(expected, result);

        n = 31;
        k = 30;
        result = BigIntegerMath.binomial(n, k);
        expected = BigInteger.valueOf(LongMath.binomial(n, k));
        assertEquals(expected, result);

        n = 32;
        k = 31;
        result = BigIntegerMath.binomial(n, k);
        expected = BigInteger.valueOf(LongMath.binomial(n, k));
        assertNotEquals(expected, result);
        assertTrue(result.isProbablePrime(10));
    }
}

