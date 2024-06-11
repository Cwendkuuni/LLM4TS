package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {
    // Test case for isPowerOfTwo method
    @Test
    public void testIsPowerOfTwo() {
        BigInteger testNum = BigInteger.valueOf(1);
        BigInteger powerOfTwo = BigInteger.ONE.shiftLeft(1);

        // Test positive cases
        for (int i = 0; i < 10; i++) {
            testNum = powerOfTwo.multiply(BigInteger.TEN);
            assertTrue(BigIntegerMath.isPowerOfTwo(testNum));
        }

        // Test negative cases
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.negate()));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1)));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("10101")));
    }

    // Test case for log2 method with RoundingMode.DOWN, RoundingMode.Floor, RoundingMode.UP, RoundingMode.Ceiling, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, and RoundingMode.HALF_EVEN
    @Test
    public void testLog2() {
        BigInteger testNum = BigInteger.valueOf(1);
        RoundingMode[] roundingModes = {RoundingMode.DOWN, RoundingMode.FLOOR, RoundingMode.UP, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN};

        // Test positive cases
        for (int i = 0; i < 10; i++) {
            testNum = testNum.shiftLeft(1);
            for (RoundingMode roundingMode : roundingModes) {
                int expectedResult = i;
                int actualResult = BigIntegerMath.log2(testNum, roundingMode).intValue();
                assertEquals(expectedResult, actualResult);
            }
        }

        // Test negative cases
        assertThrows(AssertionError.class, () -> BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.UNNECESSARY));
        assertThrows(AssertionError.class, () -> BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.UNNECESSARY));
    }

    // Test case for log10 method with RoundingMode.DOWN, RoundingMode.Floor, RoundingMode.UP, RoundingMode.Ceiling, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, and RoundingMode.HALF_EVEN
    @Test
    public void testLog10() {
        BigInteger testNum = BigInteger.valueOf(1);
        RoundingMode[] roundingModes = {RoundingMode.DOWN, RoundingMode.FLOOR, RoundingMode.UP, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN};

        // Test positive cases
        for (int i = 0; i < 10; i++) {
            testNum = testNum.multiply(BigInteger.TEN);
            for (RoundingMode roundingMode : roundingModes) {
                double expectedResult = Math.log10((double) testNum.intValue());
                int actualResult = BigIntegerMath.log10(testNum, roundingMode).intValue();
                assertEquals(expectedResult, actualResult, 0.001);
            }
        }

        // Test negative cases
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.UNNECESSARY));
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.log10(BigInteger.ONE.negate(), RoundingMode.UNNECESSARY));
        assertThrows(AssertionError.class, () -> BigIntegerMath.log10(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.UNNECESSARY));
    }

    // Test case for sqrt method with RoundingMode.DOWN, RoundingMode.Floor, RoundingMode.UP, RoundingMode.Ceiling, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, and RoundingMode.HALF_EVEN
    @Test
    public void testSqrt() {
        BigInteger testNum = BigInteger.valueOf(1);
        RoundingMode[] roundingModes = {RoundingMode.DOWN, RoundingMode.FLOOR, RoundingMode.UP, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN};

        // Test positive cases
        for (int i = 0; i < 10; i++) {
            testNum = testNum.shiftLeft(1);
            for (RoundingMode roundingMode : roundingModes) {
                BigInteger expectedResult = testNum;
                BigInteger actualResult = BigIntegerMath.sqrt(testNum, roundingMode);
                assertEquals(expectedResult, actualResult);
            }
        }

        // Test negative cases
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.UNNECESSARY));
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.sqrt(BigInteger.ONE.negate(), RoundingMode.UNNECESSARY));
        assertThrows(AssertionError.class, () -> BigIntegerMath.sqrt(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.UNNECESSARY));
    }

    // Test case for divide method
    @Test
    public void testDivide() {
        BigInteger dividend = BigInteger.valueOf(10);
        BigInteger divisor = BigInteger.valueOf(2);
        RoundingMode roundingMode = RoundingMode.FLOOR;

        BigDecimal dividendDec = new BigDecimal(dividend);
        BigDecimal divisorDec = new BigDecimal(divisor);
        BigDecimal expectedResultDec = dividendDec.divide(divisorDec, 0, roundingMode);

        BigInteger expectedResult = expectedResultDec.toBigIntegerExact();
        BigInteger actualResult = BigIntegerMath.divide(dividend, divisor, roundingMode);

        assertEquals(expectedResult, actualResult);

        // Test dividing by zero and negative numbers
        assertThrows(ArithmeticException.class, () -> BigIntegerMath.divide(BigInteger.ZERO, divisor, roundingMode));
        assertThrows(ArithmeticException.class, () -> BigIntegerMath.divide(dividend, BigInteger.ZERO, roundingMode));
        assertThrows(ArithmeticException.class, () -> BigIntegerMath.divide(dividend, divisor.negate(), roundingMode));
    }

    // Test case for factorial method
    @Test
    public void testFactorial() {
        BigInteger testNum = BigInteger.valueOf(5);
        BigInteger expectedResult = BigInteger.valueOf(120);

        BigInteger actualResult = BigIntegerMath.factorial(testNum.intValue());

        assertEquals(expectedResult, actualResult);

        testNum = BigInteger.valueOf(25);
        expectedResult = BigInteger.valueOf(15504000000000000L);

        actualResult = BigIntegerMath.factorial(testNum.intValue());

        assertEquals(expectedResult, actualResult);

        // Test negative numbers and large numbers
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.factorial(-5));
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.factorial(Integer.MAX_VALUE));
    }

    // Test case for binomial method
    @Test
    public void testBinomial() {
        int n = 5;
        int k = 3;
        BigInteger expectedResult = BigInteger.valueOf(10);

        BigInteger actualResult = BigIntegerMath.binomial(n, k);

        assertEquals(expectedResult, actualResult);

        n = 10;
        k = 7;
        expectedResult = BigInteger.valueOf(210);

        actualResult = BigIntegerMath.binomial(n, k);

        assertEquals(expectedResult, actualResult);

        // Test negative numbers and large numbers
        n = -5;
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.binomial(n, k));
        n = 50;
        k = 30;
        assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.binomial(n, k));
    }
}