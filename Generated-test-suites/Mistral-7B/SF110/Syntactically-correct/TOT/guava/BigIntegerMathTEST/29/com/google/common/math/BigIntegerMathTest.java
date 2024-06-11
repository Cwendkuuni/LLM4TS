package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger powerOfTwo = BigInteger.ONE.shiftLeft(32);
        assertTrue(BigIntegerMath.isPowerOfTwo(powerOfTwo));
        
        BigInteger notPowerOfTwo = BigInteger.TEN;
        assertFalse(BigIntegerMath.isPowerOfTwo(notPowerOfTwo));

        BigInteger negativeNumber = BigInteger.valueOf(-1);
        assertFalse(BigIntegerMath.isPowerOfTwo(negativeNumber));
    }

    @Test
    public void testLog2WithPowerOfTwo() {
        BigInteger powerOfTwo = BigInteger.ONE.shiftLeft(32);
        int expectedLog2 = 32;
        assertEquals(expectedLog2, BigIntegerMath.log2(powerOfTwo, RoundingMode.DOWN));

        BigInteger powerOfTwoMinusOne = powerOfTwo.subtract(BigInteger.ONE);
        int expectedLog2MinusOne = 31;
        assertEquals(expectedLog2MinusOne, BigIntegerMath.log2(powerOfTwoMinusOne, RoundingMode.DOWN));
    }

    @Test
    public void testLog2WithNonPowerOfTwo() {
        BigInteger nonPowerOfTwo = BigInteger.valueOf(27);
        int expectedLog2 = 5;
        assertEquals(expectedLog2, BigIntegerMath.log2(nonPowerOfTwo, RoundingMode.DOWN));
    }

    @Test(expected = AssertionError.class)
    public void testLog2WithInvalidRoundingMode() {
        BigInteger number = BigInteger.ONE;
        BigIntegerMath.log2(number, RoundingMode.CHOP);
    }

    @Test
    public void testLog10WithSmallNumber() {
        BigInteger number = BigInteger.valueOf(10);
        int expectedLog10 = 1;
        assertEquals(expectedLog10, BigIntegerMath.log10(number, RoundingMode.DOWN).intValue());
    }

    @Test
    public void testLog10WithLargeNumber() {
        BigInteger largeNumber = BigInteger.valueOf(1234567890123456789L);
        int expectedLog10 = 18;
        assertEquals(expectedLog10, BigIntegerMath.log10(largeNumber, RoundingMode.DOWN).intValue());
    }

    @Test
    public void testSqrtWithPerfectSquare() {
        BigInteger perfectSquare = BigInteger.valueOf(16);
        BigInteger expectedSqrt = BigInteger.valueOf(4);
        assertEquals(expectedSqrt, BigIntegerMath.sqrt(perfectSquare, RoundingMode.UP));
    }

    @Test
    public void testSqrtWithNonPerfectSquare() {
        BigInteger nonPerfectSquare = BigInteger.valueOf(21);
        BigInteger expectedSqrt = BigInteger.valueOf(4); // Approximate value
        assertEquals(expectedSqrt, BigIntegerMath.sqrt(nonPerfectSquare, RoundingMode.HALF_UP));
    }

    @Test(expected = AssertionError.class)
    public void testSqrtWithNegativeNumber() {
        BigInteger negativeNumber = BigInteger.valueOf(-1);
        BigIntegerMath.sqrt(negativeNumber, RoundingMode.UP);
    }

    @Test
    public void testFactorial() {
        BigInteger n = BigInteger.valueOf(5);
        BigInteger expectedFactorial = BigInteger.valueOf(120);
        assertEquals(expectedFactorial, BigIntegerMath.factorial(n.intValue()));
    }

    @Test
    public void testBinomial() {
        int n = 5;
        int k = 2;
        BigInteger expectedBinomial = BigInteger.valueOf(10);
        assertEquals(expectedBinomial, BigIntegerMath.binomial(n, k));
    }

    @Test
    public void testDivide() {
        BigInteger dividend = BigInteger.valueOf(21);
        BigInteger divisor = BigInteger.valueOf(7);
        BigInteger expectedQuotient = BigInteger.valueOf(3);
        BigInteger quotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
        assertEquals(expectedQuotient, quotient);
    }
}