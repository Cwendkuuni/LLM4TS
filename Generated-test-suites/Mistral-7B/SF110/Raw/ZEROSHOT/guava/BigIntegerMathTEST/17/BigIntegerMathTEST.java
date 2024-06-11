package com.google.common.math;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.math.BigDecimalMath;
import com.google.common.math.BigInteger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @org.junit.BeforeClass
    public static void setup() {
        BigIntegerMath.SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
        BigIntegerMath.LN_10 = Math.log(10.0);
        BigIntegerMath.LN_2 = Math.log(2.0);
    }

    @org.junit.Test
    public void testIsPowerOfTwo() {
        BigInteger two = BigInteger.ONE.shiftLeft(1);
        BigInteger notTwo = BigInteger.ONE.shiftLeft(2).add(BigInteger.ONE);

        assertTrue(BigIntegerMath.isPowerOfTwo(two));
        assertFalse(BigIntegerMath.isPowerOfTwo(notTwo));
    }

    @org.junit.Test
    public void testLog2RoundingModes() {
        BigInteger x = BigInteger.valueOf(17);
        ListMultimap<RoundingMode, Integer> results = ArrayListMultimap.create();

        for (RoundingMode mode : RoundingMode.values()) {
            int log2 = BigIntegerMath.log2(x, mode);
            results.put(mode, log2);
            assertEquals("Unexpected log2 result for mode: " + mode, 4, log2);
        }
    }

    @org.junit.Test
    public void testLog10RoundingModes() {
        BigInteger tenThousand = BigInteger.TEN.pow(3);
        ListMultimap<RoundingMode, Integer> results = ArrayListMultimap.create();

        for (RoundingMode mode : RoundingMode.values()) {
            int log10 = BigIntegerMath.log10(tenThousand, mode);
            results.put(mode, log10);
            assertEquals("Unexpected log10 result for mode: " + mode, 3, log10);
        }
    }

    @org.junit.Test
    public void testSqrtRoundingModes() {
        BigInteger nine = BigInteger.valueOf(9);
        BigInteger twelveOne = BigInteger.valueOf(121);
        ListMultimap<RoundingMode, BigInteger> results = ArrayListMultimap.create();

        for (RoundingMode mode : RoundingMode.values()) {
            BigInteger sqrt = BigIntegerMath.sqrt(nine, mode);
            results.put(mode, sqrt);
            assertEquals("Unexpected sqrt result for mode: " + mode, BigInteger.valueOf(3), sqrt);

            sqrt = BigIntegerMath.sqrt(twelveOne, mode);
            results.put(mode, sqrt);
            assertEquals("Unexpected sqrt result for mode: " + mode, BigInteger.valueOf(11), sqrt);
        }
    }

    @org.junit.Test
    public void testDivide() {
        BigInteger dividend = BigInteger.valueOf(10);
        BigInteger divisor = BigInteger.valueOf(2);
        BigInteger expected = BigInteger.valueOf(5);

        BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR);
        assertEquals("Unexpected result of division using RoundingMode.FLOOR", expected, result);

        result = BigIntegerMath.divide(dividend, divisor, RoundingMode.UP);
        assertEquals("Unexpected result of division using RoundingMode.UP", expected.add(BigInteger.ONE), result);

        result = BigIntegerMath.divide(dividend, divisor, RoundingMode.CEILING);
        assertEquals("Unexpected result of division using RoundingMode.CEILING", expected.add(BigInteger.ONE), result);

        result = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN);
        BigDecimal dividendDec = new BigDecimal(dividend.toString());
        BigDecimal divisorDec = new BigDecimal(divisor.toString());
        BigDecimal resultDec = dividendDec.divide(divisorDec, 0, RoundingMode.HALF_DOWN);
        BigInteger halfDownResult = resultDec.toBigIntegerExact();
        assertEquals("Unexpected result of division using RoundingMode.HALF_DOWN", halfDownResult, result);

        result = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP);
        BigDecimal halfUpResultDec = dividendDec.divide(divisorDec, 0, RoundingMode.HALF_UP);
        BigInteger halfUpResult = halfUpResultDec.toBigIntegerExact();
        assertEquals("Unexpected result of division using RoundingMode.HALF_UP", halfUpResult.add(BigInteger.ONE), result);

        result = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN);
        BigDecimal halfEvenResultDec = dividendDec.divide(divisorDec, 0, RoundingMode.HALF_EVEN);
        BigInteger halfEvenResult = halfEvenResultDec.toBigIntegerExact();
        assertEquals("Unexpected result of division using RoundingMode.HALF_EVEN", halfEvenResult, result);
    }

    @org.junit.Test
    public void testFactorial() {
        BigInteger five = BigInteger.valueOf(5);
        BigInteger ten = BigInteger.valueOf(10);
        BigInteger expectedFive = BigInteger.valueOf(120);
        BigInteger expectedTen = BigInteger.valueOf(3628800);

        BigInteger resultFive = BigIntegerMath.factorial(5);
        assertEquals("Unexpected result of factorial for n = 5", expectedFive, resultFive);

        BigInteger resultTen = BigIntegerMath.factorial(10);
        assertEquals("Unexpected result of factorial for n = 10", expectedTen, resultTen);
    }

    @org.junit.Test
    public void testBinomial() {
        BigInteger five = BigInteger.valueOf(5);
        BigInteger ten = BigInteger.valueOf(10);
        BigInteger three = BigInteger.valueOf(3);
        BigInteger expectedFiveCThree = BigInteger.valueOf(10);
        BigInteger expectedTenC3 = BigInteger.valueOf(1140);

        BigInteger resultFiveCThree = BigIntegerMath.binomial(5, 3);
        assertEquals("Unexpected result of binomial for n = 5 and k = 3", expectedFiveCThree, resultFiveCThree);

        BigInteger resultTenC3 = BigIntegerMath.binomial(10, 3);
        assertEquals("Unexpected result of binomial for n = 10 and k = 3", expectedTenC3, resultTenC3);
    }

    @org.junit.Test
    public void testFitsInLong() {
        BigInteger smallNumber = BigInteger.ONE;
        BigInteger bigNumber = BigInteger.valueOf(Long.MAX_VALUE);

        assertTrue(BigIntegerMath.fitsInLong(smallNumber));
        assertFalse(BigIntegerMath.fitsInLong(bigNumber));
    }
}