package com.google.common.math.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.common.math.BigIntegerMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @org.junit.Test
    public void testIsPowerOfTwo() {
        // Basic functionality test
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.add(BigInteger.ONE)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));

        // Edge cases
        BigInteger bigInteger = BigInteger.valueOf(-1);
        try {
            BigIntegerMath.isPowerOfTwo(bigInteger);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.isPowerOfTwo(bigInteger);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(Long.MIN_VALUE);
        try {
            BigIntegerMath.isPowerOfTwo(bigInteger);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @org.junit.Test
    public void testLog2() {
        // Basic functionality test
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483647L), RoundingMode.UNNECESSARY));

        // Edge cases and exceptions
        BigInteger bigInteger = BigInteger.ZERO;
        try {
            BigIntegerMath.log2(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(Long.MIN_VALUE);
        try {
            BigIntegerMath.log2(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.log2(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(-1);
        try {
            BigIntegerMath.log2(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(0x1p1000_0000_0000_0000_0000_0000_0000_0001L); // 10^1000
        try {
            BigIntegerMath.log2(bigInteger, RoundingMode.UP);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }
    }

    @org.junit.Test
    public void testLog10() {
        // Basic functionality test
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UNNECESSARY));

        // Edge cases and exceptions
        BigInteger bigInteger = BigInteger.ZERO;
        try {
            BigIntegerMath.log10(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(Long.MIN_VALUE);
        try {
            BigIntegerMath.log10(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.log10(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(-1);
        try {
            BigIntegerMath.log10(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(0x1p1000_0000_0000_0000_0000_0000_0000_0001L); // 10^1000
        try {
            BigIntegerMath.log10(bigInteger, RoundingMode.UP);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }
    }

    @org.junit.Test
    public void testSqrt() {
        // Basic functionality test
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));

        // Edge cases and exceptions
        BigInteger bigInteger = BigInteger.ZERO;
        try {
            BigIntegerMath.sqrt(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(Long.MIN_VALUE);
        try {
            BigIntegerMath.sqrt(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.sqrt(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(-1);
        try {
            BigIntegerMath.sqrt(bigInteger, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(Long.MIN_VALUE + 1);
        try {
            BigIntegerMath.sqrt(bigInteger, RoundingMode.UP);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.sqrt(bigInteger, RoundingMode.UP);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }

        bigInteger = BigInteger.valueOf(Long.MAX_VALUE + 1);
        try {
            BigIntegerMath.sqrt(bigInteger, RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }
    }

    @org.junit.Test
    public void testDivide() {
        // Basic functionality test
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.valueOf(10), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(12), BigInteger.valueOf(4), RoundingMode.UNNECESSARY));

        // Edge cases and exceptions
        BigInteger dividend = BigInteger.ZERO;
        BigInteger divisor = BigInteger.ONE;
        try {
            BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }

        dividend = BigInteger.valueOf(Long.MIN_VALUE);
        divisor = BigInteger.valueOf(2);
        try {
            BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }

        dividend = BigInteger.valueOf(Long.MAX_VALUE);
        divisor = BigInteger.valueOf(2);
        try {
            BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }

        dividend = BigInteger.valueOf(-1);
        divisor = BigInteger.ONE;
        try {
            BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        dividend = BigInteger.valueOf(Long.MIN_VALUE);
        divisor = BigInteger.valueOf(Long.MIN_VALUE);
        try {
            BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }

        dividend = BigInteger.valueOf(Long.MIN_VALUE);
        divisor = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }

        dividend = BigInteger.valueOf(Long.MAX_VALUE);
        divisor = BigInteger.valueOf(Long.MIN_VALUE);
        try {
            BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }

        dividend = BigInteger.valueOf(Long.MAX_VALUE);
        divisor = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Pass
        }
    }

    @org.junit.Test
    public void testFactorial() {
        // Basic functionality test
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(2432902008176640000), BigIntegerMath.factorial(13));

        // Edge cases and exceptions
        try {
            BigIntegerMath.factorial(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            BigIntegerMath.factorial(Long.MAX_VALUE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @org.junit.Test
    public void testBinomial() {
        // Basic functionality test
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(6, 3));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.binomial(12, 5));

        // Edge cases and exceptions
        try {
            BigIntegerMath.binomial(-1, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            BigIntegerMath.binomial(-1, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            BigIntegerMath.binomial(0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            BigIntegerMath.binomial(1, 13);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            BigIntegerMath.binomial(12, 14);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}