package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(16);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(25);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2() {
        BigInteger x = BigInteger.ONE;
        assertEquals(0, BigIntegerMath.log2(x, RoundingMode.DOWN));

        x = BigInteger.valueOf(32);
        assertEquals(5, BigIntegerMath.log2(x, RoundingMode.DOWN));

        x = BigInteger.valueOf(33);
        assertEquals(6, BigIntegerMath.log2(x, RoundingMode.UP));

        x = BigInteger.valueOf(1023);
        assertEquals(10, BigIntegerMath.log2(x, RoundingMode.DOWN));

        try {
            BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLog10() {
        BigInteger x = BigInteger.ONE;
        assertEquals(0, BigIntegerMath.log10(x, RoundingMode.DOWN));

        x = BigInteger.valueOf(10);
        assertEquals(1, BigIntegerMath.log10(x, RoundingMode.DOWN));

        x = BigInteger.valueOf(100);
        assertEquals(2, BigIntegerMath.log10(x, RoundingMode.DOWN));

        try {
            BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BigIntegerMath.log10(BigInteger.valueOf(-1), RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSqrt() {
        BigInteger x = BigInteger.valueOf(4);
        BigInteger sqrt = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(2, sqrt.intValue());

        x = BigInteger.valueOf(26);
        sqrt = BigIntegerMath.sqrt(x, RoundingMode.UP);
        assertEquals(5, sqrt.intValue());

        x = BigInteger.valueOf(27);
        sqrt = BigIntegerMath.sqrt(x, RoundingMode.UP);
        assertEquals(5, sqrt.intValue());

        x = BigInteger.valueOf(28);
        sqrt = BigIntegerMath.sqrt(x, RoundingMode.UP);
        assertEquals(6, sqrt.intValue());

        try {
            BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BigIntegerMath.sqrt(BigInteger.ONE.negate(), RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.factorial(6));
        assertEquals(BigInteger.valueOf(5040), BigIntegerMath.factorial(7));
        assertEquals(BigInteger.valueOf(479001600), BigIntegerMath.factorial(10));

        // test with large input
        assertEquals(BigInteger.valueOf(514960611258179795213633034544366476884437764156896051200000000000000000000000000000000), BigIntegerMath.factorial(30));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(6, 5));
        assertEquals(BigInteger.valueOf(20), BigIntegerMath.binomial(10, 5));
        assertEquals(BigInteger.valueOf(210), BigIntegerMath.binomial(11, 5));
        assertEquals(BigInteger.valueOf(1729), BigIntegerMath.binomial(10, 9));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.binomial(15, 10));
    }

    @Test
    public void testDivide() {
        BigInteger dividend = BigInteger.valueOf(10);
        BigInteger divisor = BigInteger.valueOf(2);

        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(dividend, divisor, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(dividend, divisor, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN));

        divisor = BigInteger.valueOf(0);
        try {
            BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        dividend = BigInteger.valueOf(1);
        divisor = BigInteger.ZERO;
        try {
            BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }
}