package com.google.common.math;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger notPowerOfTwo = BigInteger.valueOf(3);
        BigInteger zero = BigInteger.ZERO;

        // Positive test cases
        assertTrue(BigIntegerMath.isPowerOfTwo(two));
        assertTrue(BigIntegerMath.isPowerOfTwo(two.shiftRight(1)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));

        // Negative test cases
        assertFalse(BigIntegerMath.isPowerOfTwo(notPowerOfTwo));
        assertFalse(BigIntegerMath.isPowerOfTwo(zero));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1)));
    }

    @Test
    public void testLog2() {
        BigInteger x = BigInteger.valueOf(16);
        RoundingMode[] modes = {RoundingMode.DOWN, RoundingMode.UP, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN};

        for (RoundingMode mode : modes) {
            int expected = 4; // log base 2 of 16 is 4
            int actual = BigIntegerMath.log2(x, mode);
            if (mode == RoundingMode.HALF_DOWN || mode == RoundingMode.HALF_UP || mode == RoundingMode.HALF_EVEN) {
                int halfExpected = expected + 1;
                int halfActual = actual;
                while (halfActual % 2 != 0) {
                    halfActual >>= 1;
                }
                assertEquals("Expected half-rounded result for mode " + mode, halfExpected, halfActual);
            } else {
                assertEquals(expected, actual);
            }
        }

        // Test edge cases and exceptions
        try {
            BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.ANY_VALUE);
            fail("Expected IllegalArgumentException for zero");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.ANY_VALUE);
            fail("Expected IllegalArgumentException for negative number");
        } catch (IllegalArgumentException e) {
            // expected
        }

        BigInteger bigNumber = BigInteger.valueOf(Long.MAX_VALUE);
        try {
            BigIntegerMath.log2(bigNumber, RoundingMode.ANY_VALUE);
            fail("Expected ArithmeticException for too large number");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    // Add other test cases for the remaining methods (log10, sqrt, divide, factorial, binomial) following the same pattern as the log2 test case.

    
    // Add any additional initialization code or setup methods here if needed
    
}