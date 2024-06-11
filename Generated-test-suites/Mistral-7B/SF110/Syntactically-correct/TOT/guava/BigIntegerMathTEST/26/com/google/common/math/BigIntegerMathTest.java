package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger nonPowerOfTwo = BigInteger.valueOf(3);
        BigInteger zero = BigInteger.ZERO;
        BigInteger negativeNumber = BigInteger.valueOf(-1);

        assertTrue(BigIntegerMath.isPowerOfTwo(two));
        assertFalse(BigIntegerMath.isPowerOfTwo(nonPowerOfTwo));
        assertFalse(BigIntegerMath.isPowerOfTwo(zero));
        assertFalse(BigIntegerMath.isPowerOfTwo(negativeNumber));
    }

    @Test
    public void testLog2() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger four = BigInteger.valueOf(4);
        BigInteger sixteen = BigInteger.valueOf(16);

        assertEquals(1, BigIntegerMath.log2(two, RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log2(four, RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(sixteen, RoundingMode.DOWN));

        assertEquals(1, BigIntegerMath.log2(two, RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log2(eight, RoundingMode.UP));
        assertEquals(5, BigIntegerMath.log2(thirtyTwo, RoundingMode.UP));

        assertEquals(2, BigIntegerMath.log2(four, RoundingMode.CEILING));
        assertEquals(4, BigIntegerMath.log2(sixteen, RoundingMode.CEILING));
        assertEquals(6, BigIntegerMath.log2(sixtyFour, RoundingMode.CEILING));

        try {
            BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UP);
            fail("Expected RoundingException");
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN);
            fail("Expected RoundingException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testLog10() {
        BigInteger ten = BigInteger.TEN;
        BigInteger hundred = BigInteger.valueOf(100);
        BigInteger thousand = BigInteger.valueOf(1000);
        BigInteger tenMillion = BigInteger.valueOf(10000000L);

        assertEquals(1, BigIntegerMath.log10(ten, RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(hundred, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(thousand, RoundingMode.DOWN));
        assertEquals(7, BigIntegerMath.log10(tenMillion, RoundingMode.DOWN));

        assertEquals(1, BigIntegerMath.log10(ten, RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(hundred, RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log10(thousand, RoundingMode.UP));
        assertEquals(8, BigIntegerMath.log10(tenMillion, RoundingMode.UP));

        assertEquals(2, BigIntegerMath.log10(hundred, RoundingMode.CEILING));
        assertEquals(4, BigIntegerMath.log10(thousand, RoundingMode.CEILING));
        assertEquals(8, BigIntegerMath.log10(tenMillion, RoundingMode.CEILING));

        assertEquals(1, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.HALF_DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.HALF_UP));
        assertEquals(1, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.HALF_EVEN));

        try {
            BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BigIntegerMath.log10(BigInteger.valueOf(Integer.MIN_VALUE), RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testSqrt() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger four = BigInteger.valueOf(4);
        BigInteger nine = BigInteger.valueOf(9);
        BigInteger sixteen = BigInteger.valueOf(16);
        BigInteger twentyFive = BigInteger.valueOf(25);

        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(two, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(two, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(two, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(two, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(two, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(two, RoundingMode.HALF_EVEN));

        try {
            BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UP);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            BigIntegerMath.sqrt(nine, RoundingMode.DOWN);
            assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(nine, RoundingMode.UP));
            assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(nine, RoundingMode.CEILING));
            assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(nine, RoundingMode.HALF_DOWN));
            assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(nine, RoundingMode.HALF_UP));
            assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(nine, RoundingMode.HALF_EVEN));
        } catch (ArithmeticException e) {
            fail("Unexpected ArithmeticException");
        }

        try {
            BigIntegerMath.sqrt(twentyFive, RoundingMode.DOWN);
            assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(twentyFive, RoundingMode.UP));
            assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(twentyFive, RoundingMode.CEILING));
            assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(twentyFive, RoundingMode.HALF_DOWN));
            assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(twentyFive, RoundingMode.HALF_UP));
            assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(twentyFive, RoundingMode.HALF_EVEN));
        } catch (ArithmeticException e) {
            fail("Unexpected ArithmeticException");
        }

        try {
            BigIntegerMath.sqrt(sixteen, RoundingMode.UP);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            BigIntegerMath.sqrt(four, RoundingMode.CEILING);
            assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(four, RoundingMode.DOWN));
            assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(four, RoundingMode.UP));
            assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(four, RoundingMode.CEILING));
            assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(four, RoundingMode.HALF_DOWN));
            assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(four, RoundingMode.HALF_UP));
            assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(four, RoundingMode.HALF_EVEN));
        } catch (ArithmeticException e) {
            fail("Unexpected ArithmeticException");
        }
    }

    @Test
    public void testFactorial() {
        BigInteger zero = BigInteger.ZERO;
        BigInteger one = BigInteger.ONE;
        BigInteger two = BigInteger.valueOf(2);
        BigInteger three = BigInteger.valueOf(3);
        BigInteger five = BigInteger.valueOf(5);
        BigInteger ten = BigInteger.valueOf(10);
        BigInteger twelve = BigInteger.valueOf(12);

        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(10));

        try {
            BigIntegerMath.factorial(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BigIntegerMath.factorial(12);
            assertEquals(BigInteger.valueOf(7920), BigIntegerMath.factorial(12, 0, 12));
            assertEquals(BigInteger.valueOf(479001600), BigIntegerMath.factorial(12, 1, 12));
            assertEquals(BigInteger.valueOf(378040960000), BigIntegerMath.factorial(12, 2, 12));
            assertEquals(BigInteger.valueOf(31557600000000), BigIntegerMath.factorial(12, 3, 12));
            assertEquals(BigInteger.valueOf(26318880000000000), BigIntegerMath.factorial(12, 4, 12));
            assertEquals(BigInteger.valueOf(21089600000000000000), BigIntegerMath.factorial(12, 5, 12));
            assertEquals(BigInteger.valueOf(184467440737095516160000), BigIntegerMath.factorial(12, 6, 12));
        } catch (ArithmeticException e) {
            fail("Unexpected ArithmeticException");
        }
    }
}