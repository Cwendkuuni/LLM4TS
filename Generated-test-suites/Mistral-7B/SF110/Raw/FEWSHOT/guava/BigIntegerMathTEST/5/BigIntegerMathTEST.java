package org.apache.commons.cli.math; // assuming you have added the necessary imports for the test case

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import com.google.common.base.MathPreconditions;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.math.BigDecimal;
import com.google.common.math.BigInteger;
import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.TEN));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(256), RoundingMode.HALF_UP));
    }

    @Test(expected = AssertionError.class)
    public void testLog2InvalidMode() {
        BigIntegerMath.log2(BigInteger.ONE, RoundingMode.POPCOUNT);
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(81), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(8100), RoundingMode.HALF_UP));
    }

    @Test(expected = AssertionError.class)
    public void testLog10InvalidMode() {
        BigIntegerMath.log10(BigInteger.ONE, RoundingMode.POPCOUNT);
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.HALF_UP));
    }

    @Test(expected = AssertionError.class)
    public void testSqrtInvalidMode() {
        BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.POPCOUNT);
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.TEN, BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(17), BigIntegerMath.divide(BigInteger.valueOf(34), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(21), BigIntegerMath.divide(BigInteger.valueOf(42), BigInteger.valueOf(2), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(15), BigIntegerMath.divide(BigInteger.valueOf(21), BigInteger.valueOf(1.5), RoundingMode.HALF_EVEN));
    }

    @Test(expected = AssertionError.class)
    public void testDivideInvalidMode() {
        BigIntegerMath.divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.POPCOUNT);
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(511280), BigIntegerMath.factorial(12));

        long factorialLimit = LongMath.factorials.length;
        for (int i = factorialLimit + 1; i <= 20; ++i) {
            BigInteger expected = BigInteger.valueOf(LongMath.factorial(i));
            BigInteger actual = BigIntegerMath.factorial(i);
            assertEquals("Factorial test failed for i = " + i, expected, actual);
        }
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.binomial(3, 1));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(4, 2));
        assertEquals(BigInteger.valueOf(210), BigIntegerMath.binomial(6, 3));
        assertEquals(BigInteger.valueOf(1716), BigIntegerMath.binomial(7, 4));

        long binomialLimit = LongMath.biggestBinomials.length;
        for (int i = binomialLimit + 1; i <= 20; ++i) {
            BigInteger binomialExpected = BigInteger.valueOf(LongMath.binomial(i, i / 2));
            BigInteger binomialActual = BigIntegerMath.binomial(i, i / 2);
            assertEquals("Binomial test failed for i = " + i, binomialExpected, binomialActual);
        }
    }

    @Test
    public void testLogicalOperations() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.shiftLeft(32)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.shiftLeft(33)));

        int log2Expected = 32;
        int log2Actual = BigIntegerMath.log2(BigInteger.ONE.shiftLeft(32), RoundingMode.DOWN);
        assertEquals(log2Expected, log2Actual);
    }

    @Test
    public void testCheckArguments() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Preconditions.checkArgument(false, "expected failure"));
        assertEquals("expected failure", exception.getMessage());

        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> Preconditions.checkArgument(BigInteger.ONE.equals(BigInteger.ONE), "Unexpected result"));
        assertNull(exception1);

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
                () -> Preconditions.checkArgument(BigInteger.ONE.equals(BigInteger.ZERO), "Unexpected result"));
        assertNotNull(exception2);

        MathIllegalArgumentException exception3 = assertThrows(MathIllegalArgumentException.class,
                () -> MathPreconditions.checkPositive(BigInteger.valueOf(-1), "Expected positive number"));
        assertNotNull(exception3);
    }

    @Test
    public void testDivisionByZero() {
        ArithmeticException exception = assertThrows(ArithmeticException.class,
                () -> BigIntegerMath.divide(BigInteger.ONE, BigInteger.ZERO, RoundingMode.DOWN));
        assertNotNull(exception);
    }

    @Test
    public void testLogWithBigNumbers() {
        BigInteger bigNumber = BigInteger.valueOf(Long.MAX_VALUE);
        BigInteger result = BigIntegerMath.log10(bigNumber, RoundingMode.HALF_UP);
        assertEquals(String.format("%d", result), String.format("%d", Long.decode("1e%d", 32 + Long.numberOfTrailingZeros(Long.MAX_VALUE))), result);
    }

    @Test
    public void testSquareRootWithBigNumbers() {
        BigInteger bigNumber = BigInteger.valueOf(Long.MAX_VALUE * 2L);
        BigInteger expectedSqrt = BigInteger.valueOf(Long.decode("1e%d", 32 + Long.numberOfTrailingZeros(Long.MAX_VALUE * 2L)) * 0.5);
        BigInteger actualSqrt = BigIntegerMath.sqrt(bigNumber, RoundingMode.HALF_UP);
        assertEquals(expectedSqrt, actualSqrt);
    }
}