package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.MathPreconditions;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.math.BigIntegerMath;
import com.google.common.math.BigIntegerMath.MathPreconditions;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Test with valid power of two numbers
        for (int i = 0; i < 10; i++) {
            BigInteger powerOfTwo = BigInteger.ONE.shiftLeft(i);
            assertTrue(BigIntegerMath.isPowerOfTwo(powerOfTwo));
        }

        // Test with non-power of two numbers
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(13)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(15)));

        // Test with null number
        assertNull(BigIntegerMath.isPowerOfTwo(null));
        assertNull(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
    }

    @Test
    public void testLog2() {
        // Test with power of two numbers
        for (int i = 0; i < 10; i++) {
            BigInteger powerOfTwo = BigInteger.ONE.shiftLeft(i);
            int expectedResult = i;
            int actualResult = BigIntegerMath.log2(powerOfTwo, RoundingMode.DOWN);
            assertEquals("log2(" + powerOfTwo + ") should be " + expectedResult + ", but was " + actualResult, expectedResult, actualResult);
        }

        // Test with non-power of two numbers
        BigInteger number = BigInteger.valueOf(15);
        int expectedResult = 4;
        int actualResult = BigIntegerMath.log2(number, RoundingMode.DOWN);
        assertEquals("log2(" + number + ") should be " + expectedResult + ", but was " + actualResult, expectedResult, actualResult);

        // Test with negative number
        assertNull(BigIntegerMath.log2(BigInteger.valueOf(-1), RoundingMode.DOWN));

        // Test with null number
        assertNull(BigIntegerMath.log2(null, RoundingMode.DOWN));

        // Test with invalid RoundingMode
        assertNull(BigIntegerMath.log2(number, RoundingMode.TO_UNSIGNED_LONG));
    }

    @Test
    public void testLog10() {
        // Test with numbers that fit in long
        for (long i = 1; i <= Long.MAX_VALUE; i *= 10) {
            BigInteger number = BigInteger.valueOf(i);
            double expectedResult = Math.log10(i);
            BigInteger actualResult = BigIntegerMath.log10(number, RoundingMode.DOWN);
            assertEquals("log10(" + number + ") should be " + expectedResult + ", but was " + actualResult, BigInteger.valueOf((long) Math.round(expectedResult * 1000000.0)), actualResult);
        }

        // Test with non-long numbers
        BigInteger number = BigInteger.valueOf(13 * 1000 * 10000 * 100000);
        double expectedResult = Math.log10(number.longValueExact() * 1000000.0);
        BigInteger actualResult = BigIntegerMath.log10(number, RoundingMode.DOWN);
        assertEquals("log10(" + number + ") should be " + expectedResult + ", but was " + actualResult, actualResult, actualResult.multiply(BigInteger.TEN.pow(BigIntegerMath.log2(BigInteger.TEN, RoundingMode.DOWN).intValue())));

        // Test with negative number
        assertNull(BigIntegerMath.log10(BigInteger.valueOf(-1), RoundingMode.DOWN));

        // Test with null number
        assertNull(BigIntegerMath.log10(null, RoundingMode.DOWN));

        // Test with invalid RoundingMode
        assertNull(BigIntegerMath.log10(number, RoundingMode.TO_UNSIGNED_LONG));
    }

    @Test
    public void testSqrt() {
        // Test with perfect squares
        for (int i = 0; i < 10; i++) {
            BigInteger number = BigInteger.valueOf(Math.pow(2, i));
            BigInteger expectedResult = BigInteger.valueOf((long) Math.sqrt(number.longValueExact()));
            BigInteger actualResult = BigIntegerMath.sqrt(number, RoundingMode.DOWN);
            assertEquals("sqrt(" + number + ") should be " + expectedResult + ", but was " + actualResult, expectedResult, actualResult);
        }

        // Test with non-perfect squares
        BigInteger number = BigInteger.valueOf(13);
        BigInteger expectedResult = BigInteger.valueOf(3).multiply(BigInteger.valueOf(3).add(BigInteger.ONE).shiftRight(1));
        BigInteger actualResult = BigIntegerMath.sqrt(number, RoundingMode.DOWN);
        assertEquals("sqrt(" + number + ") should be " + expectedResult + ", but was " + actualResult, expectedResult, actualResult);

        // Test with negative number
        assertNull(BigIntegerMath.sqrt(BigInteger.valueOf(-1), RoundingMode.DOWN));

        // Test with null number
        assertNull(BigIntegerMath.sqrt(null, RoundingMode.DOWN));

        // Test with invalid RoundingMode
        assertNull(BigIntegerMath.sqrt(number, RoundingMode.TO_UNSIGNED_LONG));
    }

    @Test
    public void testFactorial() {
        // Test with small numbers
        for (int i = 0; i <= 5; i++) {
            int number = i;
            BigInteger expectedResult = BigInteger.valueOf(LongMath.factorials[i]);
            BigInteger actualResult = BigIntegerMath.factorial(number);
            assertEquals("factorial(" + number + ") should be " + expectedResult + ", but was " + actualResult, expectedResult, actualResult);
        }

        // Test with large numbers
        ListMultimap<Integer, BigInteger> results = ArrayListMultimap.create();
        BigInteger number = BigInteger.ONE;
        for (int i = 6; i <= 15; i++) {
            BigInteger actualResult = BigIntegerMath.factorial(i);
            results.put(i, actualResult);
            number = number.multiply(actualResult);
        }
        BigInteger expectedResult = number;
        for (Map.Entry<Integer, BigInteger> entry : results.entrySet()) {
            int key = entry.getKey();
            BigInteger value = entry.getValue();
            assertEquals("factorial(" + key + ") should be " + expectedResult.divide(value) + ", but was " + value, expectedResult.divide(value), value);
            expectedResult = expectedResult.divide(value);
        }

        // Test with negative number
        assertNull(BigIntegerMath.factorial(-1));

        // Test with null number
        assertNull(BigIntegerMath.factorial(null));
    }

    @Test
    public void testDivide() {
        // Test with long numbers
        for (long dividend = 1; dividend <= Long.MAX_VALUE; dividend *= 10) {
            long divisor = 5;
            BigInteger expectedResult = BigInteger.valueOf(dividend / divisor);
            BigInteger actualResult = BigIntegerMath.divide(BigInteger.valueOf(dividend), BigInteger.valueOf(divisor), RoundingMode.DOWN);
            assertEquals("divide(" + dividend + ", " + divisor + ") should be " + expectedResult + ", but was " + actualResult, expectedResult, actualResult);
            divisor *= 10;
        }

        // Test with large numbers
        BigInteger dividend = BigInteger.valueOf(13 * 1000 * 10000 * 100000);
        BigInteger divisor = BigInteger.valueOf(3);
        BigInteger expectedResult = dividend.divide(divisor);
        BigInteger actualResult = BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
        assertEquals("divide(" + dividend + ", " + divisor + ") should be " + expectedResult + ", but was " + actualResult, expectedResult, actualResult);

        // Test with negative numbers
        BigInteger a = BigInteger.valueOf(-5);
        BigInteger b = BigInteger.valueOf(3);
        BigInteger expectedResultNegative = a.negate().divide(b);
        BigInteger actualResultNegative = BigIntegerMath.divide(a, b, RoundingMode.DOWN).negate();
        assertEquals("divide(" + a + ", " + b + ") should be " + expectedResultNegative + ", but was " + actualResultNegative, expectedResultNegative, actualResultNegative);

        // Test with null numbers
        assertNull(BigIntegerMath.divide(null, BigInteger.ONE, RoundingMode.DOWN));
        assertNull(BigIntegerMath.divide(BigInteger.ONE, null, RoundingMode.DOWN));

        // Test with invalid RoundingMode
        assertNull(BigIntegerMath.divide(BigInteger.valueOf(13), BigInteger.valueOf(3), RoundingMode.TO_UNSIGNED_LONG));
    }

    @Test
    public void testBinomial() {
        // Test with small numbers
        for (int n = 0; n <= 5; n++) {
            for (int k = 0; k <= n; k++) {
                BigInteger actualResult = BigIntegerMath.binomial(n, k);
                long expectedResult = LongMath.binomial(n, k);
                assertEquals("binomial(" + n + ", " + k + ") should be " + expectedResult + ", but was " + actualResult, BigInteger.valueOf(expectedResult), actualResult);
            }
        }

        // Test with large numbers
        BigInteger actualResultLarge = BigIntegerMath.binomial(100, 50);
        long expectedResultLarge = LongMath.binomial(100, 50);
        BigInteger expectedResultLargeBigInteger = BigInteger.valueOf(expectedResultLarge);
        assertEquals("binomial(" + 100 + ", " + 50 + ") should be " + expectedResultLargeBigInteger + ", but was " + actualResultLarge, expectedResultLargeBigInteger, actualResultLarge);

        // Test with negative numbers
        assertNull(BigIntegerMath.binomial(-1, 0));
        assertNull(BigIntegerMath.binomial(0, -1));

        // Test with invalid numbers
        assertNull(BigIntegerMath.binomial(1, 2));

        // Test with null numbers
        assertNull(BigIntegerMath.binomial(null, 0));
        assertNull(BigIntegerMath.binomial(0, null));
    }
}