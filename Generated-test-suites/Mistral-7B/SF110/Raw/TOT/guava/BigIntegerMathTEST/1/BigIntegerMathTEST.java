package com.google.common.math;

import com.google.common.base.Preconditions;
import com.google.common.math.BigIntegerMath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;
import java.math.RoundingMode;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger four = BigInteger.valueOf(4);
        BigInteger sixteen = BigInteger.valueOf(16);
        BigInteger negativeNumber = BigInteger.valueOf(-16);
        BigInteger nonPowerOfTwo = BigInteger.valueOf(5);

        assertTrue(BigIntegerMath.isPowerOfTwo(two));
        assertTrue(BigIntegerMath.isPowerOfTwo(four));
        assertTrue(BigIntegerMath.isPowerOfTwo(sixteen));
        assertFalse(BigIntegerMath.isPowerOfTwo(negativeNumber));
        assertFalse(BigIntegerMath.isPowerOfTwo(nonPowerOfTwo));
    }

    @Test
    public void testLog2WithInvalidInput() {
        BigInteger zero = BigInteger.ZERO;
        BigInteger negativeNumber = BigInteger.valueOf(-5);

        try {
            BigIntegerMath.log2(zero, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException with input zero");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BigIntegerMath.log2(negativeNumber, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException with negative input");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLog2RoundingModes() {
        BigInteger ten = BigInteger.TEN;
        BigInteger fifteen = ten.pow(3);
        BigInteger sixteen = ten.pow(4);

        int floorResult = BigIntegerMath.log2(fifteen, RoundingMode.FLOOR);
        int ceilingResult = BigIntegerMath.log2(fifteen, RoundingMode.CEILING);
        int halfDownResult = BigIntegerMath.log2(fifteen, RoundingMode.HALF_DOWN);
        int halfUpResult = BigIntegerMath.log2(fifteen, RoundingMode.HALF_UP);
        int halfEvenResult = BigIntegerMath.log2(fifteen, RoundingMode.HALF_EVEN);

        assertEquals(3, floorResult);
        assertEquals(3, ceilingResult);
        assertEquals(3, halfDownResult);
        assertEquals(4, halfUpResult);
        assertEquals(4, halfEvenResult);

        int log2Ten = BigIntegerMath.log2(ten, RoundingMode.UNNECESSARY);
        assertEquals(3, log2Ten);

        int log2Sixteen = BigIntegerMath.log2(sixteen, RoundingMode.UNNECESSARY);
        assertEquals(4, log2Sixteen);
    }

    @Test
    public void testLog10WithInvalidInput() {
        BigInteger negativeNumber = BigInteger.valueOf(-5);
        BigInteger zero = BigInteger.ZERO;
        BigInteger bigNumber = BigInteger.valueOf(Long.MAX_VALUE);

        try {
            BigIntegerMath.log10(negativeNumber, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException with negative input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BigIntegerMath.log10(zero, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException with zero input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BigIntegerMath.log10(bigNumber, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException: big number is too large");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testLog10RoundingModes() {
        BigInteger six = BigInteger.valueOf(6);
        BigInteger eleven = BigInteger.valueOf(11);
        BigInteger fiftyFive = BigInteger.valueOf(55);
        BigInteger sixty = BigInteger.valueOf(60);

        int floorResult = BigIntegerMath.log10(sixty, RoundingMode.FLOOR);
        int ceilingResult = BigIntegerMath.log10(sixty, RoundingMode.CEILING);
        int halfDownResult = BigIntegerMath.log10(sixty, RoundingMode.HALF_DOWN);
        int halfUpResult = BigIntegerMath.log10(sixty, RoundingMode.HALF_UP);
        int halfEvenResult = BigIntegerMath.log10(sixty, RoundingMode.HALF_EVEN);

        assertEquals(2, floorResult);
        assertEquals(2, ceilingResult);
        assertEquals(2, halfDownResult);
        assertEquals(3, halfUpResult);
        assertEquals(3, halfEvenResult);

        int log10Six = BigIntegerMath.log10(six, RoundingMode.UNNECESSARY);
        assertEquals(1, log10Six);

        int log10Eleven = BigIntegerMath.log10(eleven, RoundingMode.UNNECESSARY);
        assertEquals(1, log10Eleven);

        int log10FiftyFive = BigIntegerMath.log10(fiftyFive, RoundingMode.UNNECESSARY);
        assertEquals(2, log10FiftyFive);
    }

    @Test
    public void testSqrtWithInvalidInput() {
        BigInteger negativeNumber = BigInteger.valueOf(-16);
        BigInteger zero = BigInteger.ZERO;

        try {
            BigIntegerMath.sqrt(negativeNumber, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException with negative input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BigIntegerMath.sqrt(zero, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException with zero input");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSqrtRoundingModes() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger nine = BigInteger.valueOf(9);
        BigInteger sixteen = BigInteger.valueOf(16);
        BigInteger twentyFive = BigInteger.valueOf(25);

        BigInteger floorResult = BigIntegerMath.sqrt(sixteen, RoundingMode.FLOOR);
        BigInteger ceilingResult = BigIntegerMath.sqrt(sixteen, RoundingMode.CEILING);
        BigInteger halfDownResult = BigIntegerMath.sqrt(sixteen, RoundingMode.HALF_DOWN);
        BigInteger halfUpResult = BigIntegerMath.sqrt(sixteen, RoundingMode.HALF_UP);
        BigInteger halfEvenResult = BigIntegerMath.sqrt(sixteen, RoundingMode.HALF_EVEN);

        assertEquals(four, floorResult);
        assertEquals(four, ceilingResult);
        assertEquals(four, halfDownResult);
        assertEquals(five, halfUpResult);
        assertEquals(five, halfEvenResult);

        BigInteger sqrtTwo = BigIntegerMath.sqrt(two, RoundingMode.UNNECESSARY);
        assertEquals(BigInteger.valueOf(2), sqrtTwo);

        BigInteger sqrtTwentyFive = BigIntegerMath.sqrt(twentyFive, RoundingMode.UNNECESSARY);
        assertEquals(BigInteger.valueOf(5), sqrtTwentyFive);
    }

    @Test
    public void testFactorial() {
        BigInteger zero = BigInteger.ZERO;
        BigInteger five = BigInteger.valueOf(5);
        BigInteger twentyFour = BigInteger.valueOf(24);

        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(51090942171709440000L), BigIntegerMath.factorial(24));

        try {
            BigIntegerMath.factorial(BigInteger.valueOf(-5));
            fail("Expected IllegalArgumentException with negative input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BigIntegerMath.factorial(zero);
            fail("Expected IllegalArgumentException with zero input");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBinomial() {
        BigInteger five = BigInteger.valueOf(5);
        BigInteger ten = BigInteger.TEN;
        BigInteger fifteen = BigInteger.valueOf(15);
        BigInteger twenty = BigInteger.valueOf(20);

        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(5, 5));
        assertEquals(BigInteger.valueOf(252), BigIntegerMath.binomial(15, 5));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(20, 10));

        try {
            BigIntegerMath.binomial(-5, 5);
            fail("Expected IllegalArgumentException with negative n or k");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BigIntegerMath.binomial(5, 6);
            fail("Expected IllegalArgumentException with k greater than n");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDivide() {
        BigInteger nine = BigInteger.valueOf(9);
        BigInteger four = BigInteger.valueOf(4);
        BigInteger eight = BigInteger.valueOf(8);

        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(nine, four, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.divide(nine, eight, RoundingMode.UNNECESSARY));

        try {
            BigIntegerMath.divide(nine, BigInteger.valueOf(-4), RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException with negative divisor");
        } catch (ArithmeticException e) {
            // Expected
        }
    }
}