package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.MathPreconditions;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Test with positive BigInteger values
        BigInteger x = BigInteger.ONE;
        for (int i = 0; i < 30; i++) {
            boolean result = BigIntegerMath.isPowerOfTwo(x);
            if (i == 0 || i == 31) {
                assertTrue(result);
            } else {
                assertFalse(result);
            }
            x = x.shiftLeft(1);
        }

        // Test with zero
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));

        // Test with negative BigInteger values
        BigInteger negativeX = BigInteger.valueOf(-1);
        for (int i = 0; i < 16; i++) {
            negativeX = negativeX.shiftLeft(1);
            assertFalse(BigIntegerMath.isPowerOfTwo(negativeX));
        }
    }

    @Test
    public void testLog2() {
        // Test with positive BigInteger values
        BigInteger x = BigInteger.ONE;
        for (int i = 0; i < 30; i++) {
            int log2Result = BigIntegerMath.log2(x, RoundingMode.DOWN);
            if (i == 0 || i == 31) {
                assertEquals(i, log2Result);
            } else {
                assertTrue(log2Result >= i - 1 && log2Result <= i);
            }
            x = x.shiftLeft(1);
        }

        // Test with zero
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));

        // Test with negative BigInteger values
        BigInteger negativeX = BigInteger.valueOf(-1);
        for (int i = 0; i < 16; i++) {
            negativeX = negativeX.shiftLeft(1);
            assertEquals(-(i + 2), BigIntegerMath.log2(negativeX, RoundingMode.DOWN));
        }

        // Test with edge cases
        BigInteger maxValue = BigInteger.valueOf(Long.MAX_VALUE);
        assertEquals(31, BigIntegerMath.log2(maxValue, RoundingMode.DOWN));
        assertEquals(32, BigIntegerMath.log2(maxValue, RoundingMode.UP));
        assertEquals(31, BigIntegerMath.log2(maxValue, RoundingMode.CEILING));
        assertEquals(31, BigIntegerMath.log2(maxValue, RoundingMode.HALF_DOWN));
        assertEquals(31, BigIntegerMath.log2(maxValue, RoundingMode.HALF_UP));
        assertEquals(31, BigIntegerMath.log2(maxValue, RoundingMode.HALF_EVEN));
    }

    @Test
    public void testLog10() {
        // Test with positive BigInteger values
        BigInteger x = BigInteger.ONE;
        for (int i = 0; i < 20; i++) {
            int log10Result = BigIntegerMath.log10(x, RoundingMode.DOWN);
            if (i == 0) {
                assertEquals(0, log10Result);
            } else {
                int expectedLog2Result = BigIntegerMath.log2(x, RoundingMode.DOWN) / 3;
                int expectedLog10Result = (int) (Math.log10(x.longValue()) * 10);
                assertTrue(log10Result >= expectedLog10Result - 1 && log10Result <= expectedLog10Result);
            }
            x = x.multiply(BigInteger.TEN);
        }

        // Test with zero
        assertEquals(-1, BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.DOWN));

        // Test with negative BigInteger values
        BigInteger negativeX = BigInteger.valueOf(-1);
        for (int i = 0; i < 16; i++) {
            negativeX = negativeX.shiftLeft(1);
            assertEquals(-(i + 1), BigIntegerMath.log10(negativeX, RoundingMode.DOWN));
        }

        // Test with edge cases
        BigInteger minValue = BigInteger.valueOf(Long.MIN_VALUE);
        assertEquals(-126, BigIntegerMath.log10(minValue, RoundingMode.DOWN));
        assertEquals(-125, BigIntegerMath.log10(minValue.add(BigInteger.ONE), RoundingMode.DOWN));
    }

    @Test
    public void testSqrt() {
        // Test with positive BigInteger values
        BigInteger x = BigInteger.ONE;
        for (int i = 0; i < 10; i++) {
            BigInteger sqrtResult = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
            BigInteger expectedSqrt = x.shiftRight(1).add(BigInteger.ONE).divide(BigInteger.TWO);
            assertEquals(expectedSqrt, sqrtResult);
            x = x.multiply(BigInteger.TEN);
        }

        // Test with zero
        assertNull(BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.DOWN));

        // Test with negative BigInteger values
        BigInteger negativeX = BigInteger.valueOf(-1);
        for (int i = 0; i < 10; i++) {
            negativeX = negativeX.shiftLeft(1);
            assertNull(BigIntegerMath.sqrt(negativeX, RoundingMode.DOWN));
        }

        // Test with edge cases
        BigInteger maxValue = BigInteger.valueOf(Long.MAX_VALUE);
        BigInteger maxSquare = maxValue.multiply(maxValue);
        BigInteger maxSqrt = BigIntegerMath.sqrt(maxSquare, RoundingMode.UP);
        assertEquals(maxSquare.shiftRight(1), maxSqrt.multiply(maxSqrt));

        BigInteger negativeMaxValue = BigInteger.valueOf(Long.MIN_VALUE);
        BigInteger negativeMaxSquare = negativeMaxValue.multiply(negativeMaxValue);
        BigInteger negativeMaxSqrt = BigIntegerMath.sqrt(negativeMaxSquare, RoundingMode.DOWN);
        assertEquals(negativeMaxValue.negate(), negativeMaxSqrt.negate()).multiply(negativeMaxSqrt);
    }

    @Test
    public void testDivide() {
        // Test with positive BigInteger values
        BigInteger dividend = BigInteger.ONE;
        BigInteger divisor = BigInteger.valueOf(2);
        BigInteger quotient = BigInteger.ONE;
        BigInteger remainder = BigInteger.ZERO;
        for (int i = 0; i < 10; i++) {
            BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR);
            assertEquals(quotient, result);
            dividend = dividend.multiply(BigInteger.TEN);
            quotient = quotient.add(BigInteger.ONE);
            remainder = remainder.add(BigInteger.ONE);
            divisor = divisor.add(remainder);
        }

        // Test with zero divisor
        try {
            BigIntegerMath.divide(BigInteger.ONE, BigInteger.ZERO, RoundingMode.DOWN);
            fail("Division by zero exception expected");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Test with negative BigInteger values
        BigInteger negativeDividend = BigInteger.valueOf(-1);
        BigInteger negativeDivisor = BigInteger.valueOf(-2);
        BigInteger negativeQuotient = BigInteger.ONE;
        BigInteger negativeRemainder = BigInteger.ONE;
        for (int i = 0; i < 10; i++) {
            BigInteger result = BigIntegerMath.divide(negativeDividend, negativeDivisor, RoundingMode.FLOOR);
            assertEquals(negativeQuotient, result);
            negativeDividend = negativeDividend.negate();
            negativeDivisor = negativeDivisor.negate();
            negativeQuotient = negativeQuotient.negate();
            negativeRemainder = negativeRemainder.negate();
        }

        // Test with edge cases
        BigInteger maxDividend = BigInteger.valueOf(Long.MAX_VALUE);
        BigInteger maxDivisor = BigInteger.ONE;
        BigInteger maxQuotient = BigInteger.valueOf(Long.MAX_VALUE);
        BigInteger maxRemainder = BigInteger.ZERO;
        try {
            BigInteger result = BigIntegerMath.divide(maxDividend, maxDivisor, RoundingMode.DOWN);
            fail("Division by constant exception expected");
        } catch (ArithmeticException e) {
            // Expected
        }
        maxQuotient = maxDividend.divide(maxDivisor);
        maxRemainder = maxDividend.mod(maxDivisor);
        assertEquals(maxQuotient, result);
        assertEquals(maxRemainder, BigInteger.ZERO);

        BigInteger minDividend = BigInteger.valueOf(Long.MIN_VALUE);
        BigInteger minDivisor = BigInteger.ONE;
        BigInteger minQuotient = BigInteger.valueOf(Long.MIN_VALUE);
        BigInteger minRemainder = BigInteger.ZERO;
        try {
            BigInteger result = BigIntegerMath.divide(minDividend, minDivisor, RoundingMode.DOWN);
            fail("Division by constant exception expected");
        } catch (ArithmeticException e) {
            // Expected
        }
        minQuotient = minDividend.divide(minDivisor);
        minRemainder = minDividend.mod(minDivisor);
        assertEquals(minQuotient, result);
        assertEquals(minRemainder, BigInteger.ZERO);
    }

    @Test
    public void testFactorial() {
        // Test with small positive integers
        List<BigInteger> expected = Arrays.asList(BigInteger.ONE, BigInteger.TWO, BigInteger.SIX, BigInteger.TWENTY, BigInteger.TWELVE, BigInteger.SIXTY, BigInteger.SEVENTY_TWO, BigInteger.FIVE_HUNDRED_AND_TWENTY_FOUR, BigInteger.FIVE_THOUSAND_SIX_HUNDRED_AND_THIRTY_TWO, BigInteger.SIXTY_FOUR_THOUSAND_SIX_HUNDRED_AND_SIXTY);
        List<BigInteger> results = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            results.add(BigIntegerMath.factorial(i));
        }
        assertEquals(expected, results);

        // Test with larger positive integers
        List<BigInteger> largeExpected = new ArrayList<>();
        List<BigInteger> largeResults = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            long factorial = 1;
            for (int j = 1; j <= (long) (1 << i); j++) {
                factorial *= j;
            }
            largeExpected.add(BigInteger.valueOf(factorial));
            largeResults.add(BigIntegerMath.factorial((int) (1 << i)));
        }
        assertEquals(largeExpected, largeResults);
    }

    @Test
    public void testBinomial() {
        // Test with small positive integers
        List<BigInteger> expected = Arrays.asList(BigInteger.ONE, BigInteger.ONE, BigInteger.TWO, BigInteger.THREE, BigInteger.FIVE, BigInteger.EIGHT, BigInteger.FIVE, BigInteger.SEVEN, BigInteger.TWELVE, BigInteger.FIVE, BigInteger.SEVEN, BigInteger.TWELVE, BigInteger.THIRTY_FIVE, BigInteger.THIRTY_FIVE, BigInteger.FIVE_HUNDRED_AND_TWENTY_FIVE, BigInteger.TWO_THOUSAND_NINE_HUNDRED_AND_TWENTY_SEVEN, BigInteger.TWO_THOUSAND_NINE_HUNDRED_AND_TWENTY_SEVEN, BigInteger.FIVE_THOUSAND_SIXTY_ONE, BigInteger.FIVE_THOUSAND_SIXTY_ONE);
        List<BigInteger> results = new ArrayList<>();
        for (int n = 0; n < 11; n++) {
            for (int k = 0; k <= n; k++) {
                results.add(BigIntegerMath.binomial(n, k));
            }
        }
        assertEquals(expected, results);

        // Test with larger positive integers
        List<BigInteger> largeExpected = new ArrayList<>();
        List<BigInteger> largeResults = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int n = (int) (1 << (i + 3));
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;
            for (int j = 1; j <= n; j++) {
                numerator = numerator.multiply(BigInteger.valueOf(j));
            }
            for (int j = 1; j <= (n + 1) / 2; j++) {
                denominator = denominator.multiply(BigInteger.valueOf(n - j + 1));
                denominator = denominator.multiply(BigInteger.valueOf(j));
            }
            largeExpected.add(numerator);
            largeResults.add(BigIntegerMath.binomial(n, n / 2));
        }
        assertEquals(largeExpected, largeResults);
    }

    @Test
    public void testFitsInLong() {
        // Test with positive BigInteger values
        for (int i = 0; i < 30; i++) {
            boolean result = BigIntegerMath.fitsInLong(BigInteger.valueOf((long) (1 << i)));
            if (i < 29) {
                assertFalse(result);
            } else {
                assertTrue(result);
            }
        }

        // Test with zero
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.ZERO));

        // Test with negative BigInteger values
        BigInteger negativeX = BigInteger.valueOf(-1);
        for (int i = 0; i < 16; i++) {
            negativeX = negativeX.shiftLeft(1);
            assertFalse(BigIntegerMath.fitsInLong(negativeX));
        }
    }
}