package com.google.common.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        for (int i = 0; i < 31; i++) {
            assertTrue(BigIntegerMath.isPowerOfTwo(x));
            x = x.shiftLeft(1);
        }
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.TEN));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1)));
    }

    @Test
    public void testLog2() {
        for (int i = 0; i < 32; i++) {
            BigInteger x = BigInteger.valueOf(1 << i);
            int expected = i;
            int actual;
            RoundingMode roundingMode = RoundingMode.DOWN; // or any other mode
            actual = BigIntegerMath.log2(x, roundingMode);
            assertEquals(expected, actual);
        }
        assertNull(BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertThrows(AssertionError.class, () -> BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.DOWN));
    }

    @Test
    public void testLog2WithEdgeCases() {
        BigInteger x = BigInteger.valueOf(-1);
        RoundingMode roundingMode = RoundingMode.DOWN;
        assertThrows(AssertionError.class, () -> BigIntegerMath.log2(x, roundingMode));

        x = BigInteger.ZERO;
        assertNull(BigIntegerMath.log2(x, roundingMode));

        x = BigInteger.ONE;
        assertEquals(0, BigIntegerMath.log2(x, roundingMode));

        x = BigInteger.valueOf(1L << 63); // long maximum value
        assertEquals(63, BigIntegerMath.log2(x, roundingMode));

        x = BigInteger.valueOf(Long.MAX_VALUE.longValue() + 1); // long maximum value + 1
        assertEquals(-1, BigIntegerMath.log2(x, roundingMode));
    }

    @Test
    public void testLog10() {
        BigInteger x = BigInteger.ONE;
        for (int i = 1; i <= 10; i++) {
            int expected = (int) Math.log10((double) i);
            int actual;
            RoundingMode roundingMode = RoundingMode.DOWN; // or any other mode
            actual = BigIntegerMath.log10(x, roundingMode).intValue();
            assertEquals(expected, actual);
            x = x.multiply(BigInteger.TEN);
        }
        assertNull(BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.DOWN));
        assertThrows(AssertionError.class, () -> BigIntegerMath.log10(BigInteger.ONE.negate(), RoundingMode.DOWN));
    }

    @Test
    public void testSqrt() {
        BigInteger x = BigInteger.ONE;
        for (int i = 1; i <= 10; i++) {
            BigInteger expected = x.pow(2);
            BigInteger actual;
            RoundingMode roundingMode = RoundingMode.DOWN; // or any other mode
            actual = BigIntegerMath.sqrt(x, roundingMode);
            assertEquals(expected, actual);
            x = x.add(BigInteger.ONE);
        }
        assertThrows(AssertionError.class, () -> BigIntegerMath.sqrt(BigInteger.ONE.negate(), RoundingMode.DOWN));

        MathContext context = new MathContext(100, RoundingMode.HALF_UP);
        BigDecimal onePointSeven = new BigDecimal("1.7");
        BigDecimal fourPointSquareRoot = new BigDecimal("4.0").pow(0.5, context);
        BigDecimal expectedSquareRoot = new BigDecimal(BigIntegerMath.sqrt(new BigInteger("4", 10), RoundingMode.HALF_UP)).toString(context);
        assertEquals(expectedSquareRoot, onePointSeven);
    }

    @Test
    public void testDivide() {
        BigInteger dividend = BigInteger.valueOf(12345678901234567890L);
        BigInteger divisor = BigInteger.valueOf(3);

        RoundingMode roundingMode = RoundingMode.DOWN;
        BigInteger expected = BigInteger.valueOf(41145555170455170455L);
        BigInteger actual = BigIntegerMath.divide(dividend, divisor, roundingMode);
        assertEquals(expected, actual);

        dividend = BigInteger.valueOf(-12345678901234567890L);
        divisor = new BigInteger("3");
        expected = BigInteger.valueOf(-41145555170455170454L);
        actual = BigIntegerMath.divide(dividend, divisor, roundingMode);
        assertEquals(expected, actual);

        dividend = BigInteger.valueOf(12345678901234567890L);
        divisor = BigInteger.ZERO;
        assertNull(BigIntegerMath.divide(dividend, divisor, roundingMode));

        dividend = BigInteger.valueOf(-12345678901234567890L);
        divisor = BigInteger.valueOf(-3);
        expected = BigInteger.valueOf(41145555170455170455L);
        actual = BigIntegerMath.divide(dividend, divisor, roundingMode);
        assertEquals(expected, actual);
    }

    @Test
    public void testFactorial() {
        for (int i = 0; i <= 10; i++) {
            BigInteger expected = BigInteger.valueOf(LongMath.factorials[i]);
            BigInteger actual = BigIntegerMath.factorial(i);
            assertEquals(expected, actual);
        }

        int n = 11;
        BigInteger actual = BigIntegerMath.factorial(n);
        BigInteger expected = BigInteger.valueOf(LongMath.factorials[n].longValue() << 1);
        assertEquals(expected, actual);
    }

    @Test
    public void testBinomial() {
        for (int n = 0; n <= 5; n++) {
            for (int k = 0; k <= n; k++) {
                BigInteger actual = BigIntegerMath.binomial(n, k);
                BigInteger expected = BigInteger.valueOf(LongMath.binomial(n, k));
                assertEquals(expected, actual);
            }
        }

        int n = 6, k = 3;
        BigInteger actual = BigIntegerMath.binomial(n, k);
        BigInteger expected = BigInteger.valueOf(LongMath.binomial(n, k)).shiftLeft(1);
        assertEquals(expected, actual);
    }

    @Test
    public void testFitsInLong() {
        for (int i = 0; i <= 63; i++) {
            BigInteger x = BigInteger.valueOf(1L << i);
            assertTrue(BigIntegerMath.fitsInLong(x));
        }

        BigInteger x = BigInteger.valueOf(Long.MAX_VALUE.longValue() + 1);
        assertFalse(BigIntegerMath.fitsInLong(x));
    }
}