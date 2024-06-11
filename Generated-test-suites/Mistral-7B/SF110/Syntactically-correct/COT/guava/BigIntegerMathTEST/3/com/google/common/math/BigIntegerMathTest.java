package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.math.BigIntegerMath;
import com.google.common.math.MathPreconditions;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger two = BigInteger.valueOf(2);
        assertTrue(BigIntegerMath.isPowerOfTwo(two));

        BigInteger notPowerOfTwo = BigInteger.valueOf(3);
        assertFalse(BigIntegerMath.isPowerOfTwo(notPowerOfTwo));

        BigInteger negativeNumber = BigInteger.valueOf(-3);
        assertFalse(BigIntegerMath.isPowerOfTwo(negativeNumber));
    }

    @Test
    public void testLog2() {
        BigInteger two = BigInteger.valueOf(2);
        assertEquals(0, BigIntegerMath.log2(two, RoundingMode.DOWN));

        BigInteger five = BigInteger.valueOf(5);
        assertEquals(2, BigIntegerMath.log2(five, RoundingMode.DOWN));

        BigInteger negativeNumber = BigInteger.valueOf(-3);
        try {
            BigIntegerMath.log2(negativeNumber, RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        BigInteger zero = BigInteger.ZERO;
        try {
            BigIntegerMath.log2(zero, RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        BigInteger largeNumber = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.log2(largeNumber, RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testLog10() {
        BigInteger ten = BigInteger.TEN;
        assertEquals(0, BigIntegerMath.log10(ten, RoundingMode.DOWN));

        BigInteger hundred = BigInteger.valueOf(100);
        assertEquals(2, BigIntegerMath.log10(hundred, RoundingMode.DOWN));

        BigInteger negativeNumber = BigInteger.valueOf(-3);
        try {
            BigIntegerMath.log10(negativeNumber, RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        BigInteger zero = BigInteger.ZERO;
        try {
            BigIntegerMath.log10(zero, RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        BigInteger largeNumber = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.log10(largeNumber, RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSqrt() {
        BigInteger four = BigInteger.valueOf(4);
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(four, RoundingMode.DOWN));

        BigInteger nine = BigInteger.valueOf(9);
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(nine, RoundingMode.DOWN));

        BigInteger twoPointTwo = BigInteger.valueOf(2).pow(2);
        assertEquals(twoPointTwo, BigIntegerMath.sqrt(twoPointTwo, RoundingMode.DOWN));

        BigInteger negativeNumber = BigInteger.valueOf(-4);
        try {
            BigIntegerMath.sqrt(negativeNumber, RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        BigInteger zero = BigInteger.ZERO;
        try {
            BigIntegerMath.sqrt(zero, RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        BigInteger largeNumber = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.sqrt(largeNumber, RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));

        BigInteger largeNumber = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.factorial(largeNumber.intValue());
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(3, 2));
        assertEquals(BigInteger.valueOf(210), BigIntegerMath.binomial(10, 5));

        BigInteger n = BigInteger.valueOf(21);
        assertEquals(BigInteger.valueOf(1715), BigIntegerMath.binomial(n, 5));
        assertEquals(BigInteger.valueOf(1140), BigIntegerMath.binomial(n, 6));

        BigInteger k = BigInteger.valueOf(11);
        assertEquals(BigInteger.valueOf(39916800), BigIntegerMath.binomial(20, k));
    }

    @Test
    public void testDivide() {
        BigInteger ten = BigInteger.TEN;
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(ten, BigInteger.ONE, RoundingMode.DOWN));

        BigInteger thirty = BigInteger.valueOf(30);
        assertEquals(BigInteger.TEN, BigIntegerMath.divide(thirty, BigInteger.valueOf(3), RoundingMode.DOWN));

        BigInteger negativeNumberDividedByPositiveNumber = BigInteger.valueOf(-3).divide(BigInteger.valueOf(2), RoundingMode.DOWN);
        assertEquals(BigInteger.valueOf(-1), negativeNumberDividedByPositiveNumber);

        BigInteger positiveNumberDividedByNegativeNumber = BigInteger.valueOf(3).divide(BigInteger.valueOf(-2), RoundingMode.DOWN);
        assertEquals(BigInteger.valueOf(-1).negate(), positiveNumberDividedByNegativeNumber);

        BigInteger dividend = BigInteger.valueOf(8);
        BigInteger divisor = BigInteger.valueOf(2);
        BigInteger expectedQuotient = BigInteger.valueOf(4);
        BigInteger actualQuotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
        assertEquals(expectedQuotient, actualQuotient);

        dividend = BigInteger.valueOf(8);
        divisor = BigInteger.valueOf(3);
        expectedQuotient = BigInteger.valueOf(2);
        actualQuotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
        assertEquals(expectedQuotient, actualQuotient);

        dividend = BigInteger.valueOf(8);
        divisor = BigInteger.valueOf(3);
        expectedQuotient = BigInteger.valueOf(2);
        actualQuotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.UP);
        assertEquals(expectedQuotient, actualQuotient);

        dividend = BigInteger.valueOf(8);
        divisor = BigInteger.valueOf(3);
        expectedQuotient = BigInteger.valueOf(3);
        actualQuotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.UP);
        assertEquals(expectedQuotient, actualQuotient);

        dividend = BigInteger.valueOf(8);
        divisor = BigInteger.valueOf(3);
        expectedQuotient = BigInteger.valueOf(3);
        actualQuotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.CEILING);
        assertEquals(expectedQuotient, actualQuotient);

        dividend = BigInteger.valueOf(8);
        divisor = BigInteger.valueOf(3);
        expectedQuotient = BigInteger.valueOf(2);
        actualQuotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN);
        assertEquals(expectedQuotient, actualQuotient);

        dividend = BigInteger.valueOf(8);
        divisor = BigInteger.valueOf(3);
        expectedQuotient = BigInteger.valueOf(3);
        actualQuotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP);
        assertEquals(expectedQuotient, actualQuotient);

        dividend = BigInteger.valueOf(8);
        divisor = BigInteger.valueOf(3);
        expectedQuotient = BigInteger.valueOf(3);
        actualQuotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN);
        assertEquals(expectedQuotient, actualQuotient);

        dividend = BigInteger.valueOf(8);
        divisor = BigInteger.valueOf(3);
        expectedQuotient = BigInteger.valueOf(3);
        actualQuotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
        assertEquals(expectedQuotient, actualQuotient);

        BigInteger largeNumberDividend = BigInteger.valueOf(Long.MAX_VALUE);
        BigInteger divisor = BigInteger.valueOf(2);
        try {
            BigIntegerMath.divide(largeNumberDividend, divisor, RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
}