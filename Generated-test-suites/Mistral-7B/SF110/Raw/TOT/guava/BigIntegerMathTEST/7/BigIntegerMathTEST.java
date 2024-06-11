package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger ten = BigInteger.TEN;
        BigInteger negativeTwo = BigInteger.valueOf(-2);
        BigInteger zero = BigInteger.ZERO;
        BigInteger five = BigInteger.valueOf(5);

        // Positive numbers
        assertTrue(isPowerOfTwo(two));
        assertTrue(isPowerOfTwo(ten.pow(2)));
        assertTrue(isPowerOfTwo(ten.pow(3)));

        // Zero and negative numbers
        assertFalse(isPowerOfTwo(zero));
        assertFalse(isPowerOfTwo(negativeTwo));

        // Numbers not power of two
        assertFalse(isPowerOfTwo(five));
        assertFalse(isPowerOfTwo(ten.add(BigInteger.ONE)));
    }

    @Test
    public void testLog2() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger four = BigInteger.valueOf(4);
        BigInteger eight = BigInteger.valueOf(8);
        BigInteger sixteen = BigInteger.valueOf(16);
        BigInteger thirtyTwo = BigInteger.valueOf(32);
        BigInteger sixtyFour = BigInteger.valueOf(64);
        BigInteger oneHundredAndTwentyEight = BigInteger.valueOf(128);
        BigInteger twoFiftySix = BigInteger.valueOf(256);
        BigInteger fiveOneTwoSeventySixSixFourSixtyFive = BigInteger.valueOf("512766465");
        BigInteger negativeOne = BigInteger.valueOf(-1);
        BigInteger negativeTwo = BigInteger.valueOf(-2);

        // Positive numbers
        assertEquals(0, log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, log2(two, RoundingMode.DOWN));
        assertEquals(2, log2(four, RoundingMode.DOWN));
        assertEquals(3, log2(eight, RoundingMode.DOWN));
        assertEquals(4, log2(sixteen, RoundingMode.DOWN));
        assertEquals(5, log2(thirtyTwo, RoundingMode.DOWN));
        assertEquals(6, log2(sixtyFour, RoundingMode.DOWN));
        assertEquals(7, log2(oneHundredAndTwentyEight, RoundingMode.DOWN));
        assertEquals(8, log2(twoFiftySix, RoundingMode.DOWN));

        // Negative numbers
        assertEquals(-1, log2(negativeOne, RoundingMode.DOWN));
        assertEquals(-1, log2(negativeTwo, RoundingMode.DOWN));

        // Zero
        assertEquals(-1, log2(zero, RoundingMode.DOWN));

        // Numbers not power of two
        assertEquals(6, log2(fiveOneTwoSeventySixSixFourSixtyFive, RoundingMode.DOWN));
    }

    @Test
    public void testLog10() {
        BigInteger ten = BigInteger.TEN;
        BigInteger hundred = BigInteger.valueOf(100);
        BigInteger thousand = BigInteger.valueOf(1000);
        BigInteger tenThousand = BigInteger.valueOf(10000);
        BigInteger hundredThousand = BigInteger.valueOf(100000);
        BigInteger oneMillion = BigInteger.valueOf(1000000);
        BigInteger tenMillion = BigInteger.valueOf(10000000);
        BigInteger hundredMillion = BigInteger.valueOf(100000000);
        BigInteger oneBillion = BigInteger.valueOf(1000000000);
        BigInteger negativeOne = BigInteger.valueOf(-1);
        BigInteger negativeTen = BigInteger.valueOf(-10);
        BigInteger negativeOneHundred = BigInteger.valueOf(-100);
        BigInteger negativeOneThousand = BigInteger.valueOf(-1000);
        BigInteger negativeTenThousand = BigInteger.valueOf(-10000);
        BigInteger negativeHundredThousand = BigInteger.valueOf(-100000);
        BigInteger negativeOneMillion = BigInteger.valueOf(-1000000);
        BigInteger negativeTenMillion = BigInteger.valueOf(-100000000);
        BigInteger negativeHundredMillion = BigInteger.valueOf(-1000000000);
        BigInteger negativeOneBillion = BigInteger.valueOf(-10000000000L);
        BigInteger five = BigInteger.valueOf(5);
        BigInteger twentyFive = BigInteger.valueOf(25);
        BigInteger oneHundredAndTwentyFive = BigInteger.valueOf(125);
        BigInteger negativeFive = BigInteger.valueOf(-5);
        BigInteger negativeTwentyFive = BigInteger.valueOf(-25);
        BigInteger negativeOneHundredAndTwentyFive = BigInteger.valueOf(-125);

        // Positive numbers
        assertEquals(0, log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, log10(ten, RoundingMode.DOWN));
        assertEquals(2, log10(hundred, RoundingMode.DOWN));
        assertEquals(3, log10(thousand, RoundingMode.DOWN));
        assertEquals(4, log10(tenThousand, RoundingMode.DOWN));
        assertEquals(5, log10(hundredThousand, RoundingMode.DOWN));
        assertEquals(6, log10(oneMillion, RoundingMode.DOWN));
        assertEquals(7, log10(tenMillion, RoundingMode.DOWN));
        assertEquals(8, log10(hundredMillion, RoundingMode.DOWN));
        assertEquals(9, log10(oneBillion, RoundingMode.DOWN));

        // Large numbers
        assertEquals(13, log10(BigInteger.valueOf("100000000000000000000"), RoundingMode.DOWN));
        assertEquals(16, log10(BigInteger.valueOf("1000000000000000000000000"), RoundingMode.DOWN));

        // Negative numbers
        assertEquals(-1, log10(negativeOne, RoundingMode.DOWN));
        assertEquals(-1, log10(negativeTen, RoundingMode.DOWN));
        assertEquals(-2, log10(negativeOneHundred, RoundingMode.DOWN));
        assertEquals(-3, log10(negativeOneThousand, RoundingMode.DOWN));
        assertEquals(-4, log10(negativeTenThousand, RoundingMode.DOWN));
        assertEquals(-5, log10(negativeHundredThousand, RoundingMode.DOWN));
        assertEquals(-6, log10(negativeOneMillion, RoundingMode.DOWN));
        assertEquals(-7, log10(negativeTenMillion, RoundingMode.DOWN));
        assertEquals(-8, log10(negativeHundredMillion, RoundingMode.DOWN));
        assertEquals(-9, log10(negativeOneBillion, RoundingMode.DOWN));

        // Numbers not power of ten
        assertEquals(1, log10(five, RoundingMode.DOWN));
        assertEquals(2, log10(twentyFive, RoundingMode.DOWN));
        assertEquals(3, log10(oneHundredAndTwentyFive, RoundingMode.DOWN));

        // Numbers smaller than one
        assertEquals(Double.POSITIVE_INFINITY, log10(BigInteger.ZERO, RoundingMode.DOWN));

        // Errors
        try {
            log10(null, RoundingMode.DOWN);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
        }

        try {
            log10(BigInteger.valueOf(Long.MIN_VALUE), RoundingMode.DOWN);
            fail("Expected ArithmeticException: number too small");
        } catch (ArithmeticException e) {
        }

        try {
            log10(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.DOWN);
            fail("Expected ArithmeticException: number too large");
        } catch (ArithmeticException e) {
        }
    }

    private boolean isPowerOfTwo(final BigInteger x) {
        // Implementation of isPowerOfTwo method from the given class
    }

    private int log2(final BigInteger x, final RoundingMode mode) {
        // Implementation of log2 method from the given class
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void MathPreconditions_checkPositive(final String name, final BigInteger value) {
        // Implementation of MathPreconditions.checkPositive method from the given class
    }
}