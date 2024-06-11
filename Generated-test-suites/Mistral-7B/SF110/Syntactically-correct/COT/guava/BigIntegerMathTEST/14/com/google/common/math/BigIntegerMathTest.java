package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.math.BigIntegerMath;
import com.google.common.math.MathPreconditions;

public class BigIntegerMathTest {
    // Test for isPowerOfTwo method
    @Test
    public void testIsPowerOfTwo() {
        BigInteger testNumber = BigInteger.ONE;
        BigInteger powerOfTwo = BigInteger.ONE;
        for (int i = 0; i < 10; i++) {
            powerOfTwo = powerOfTwo.multiply(BigInteger.TEN);
            assertTrue(BigIntegerMath.isPowerOfTwo(powerOfTwo));
            testNumber = testNumber.add(BigInteger.ONE);
        }

        // Test negative number
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.negate()));

        // Test zero and non-power-of-two numbers
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
    }

    // Test for log2 method with different rounding modes
    @Test
    public void testLog2WithDifferentRoundingModes() {
        BigInteger testNumber = BigInteger.ONE;
        for (int i = 0; i < 10; i++) {
            testNumber = testNumber.multiply(BigInteger.TEN);
            int log2WithFloor = BigIntegerMath.log2(testNumber, RoundingMode.FLOOR);
            int log2WithCeiling = BigIntegerMath.log2(testNumber, RoundingMode.CEILING);
            assertEquals(log2WithFloor, log2WithCeiling - 1);
            testNumber = testNumber.add(BigInteger.ONE);
        }

        // Test zero and negative numbers
        assertNull(BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.ANY_VALUE));
        MathPreconditions.checkArgument(BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.ANY_VALUE) < 0);
    }

    // Test for log10 method with different rounding modes
    @Test
    public void testLog10WithDifferentRoundingModes() {
        BigInteger testNumber = BigInteger.ONE;
        BigInteger ten = BigInteger.TEN;
        for (int i = 0; i < 10; i++) {
            testNumber = testNumber.multiply(ten);
            double log10WithFloor = Math.log10(testNumber.longValue());
            int log10WithFloorRounded = (int) Math.floor(log10WithFloor);
            int log10WithFloorRoundedInJava = BigIntegerMath.log10(testNumber, RoundingMode.FLOOR).intValue();
            assertEquals(log10WithFloorRounded, log10WithFloorRoundedInJava);
            testNumber = testNumber.add(ten);
        }

        // Test negative number, zero, and large numbers
        assertNull(BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.ANY_VALUE));
        assertNull(BigIntegerMath.log10(BigInteger.ONE.negate(), RoundingMode.ANY_VALUE));
        MathPreconditions.checkArgument(BigIntegerMath.log10(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.ANY_VALUE) > 100);
    }

    // Test for sqrt method with different rounding modes
    @Test
    public void testSqrtWithDifferentRoundingModes() {
        BigInteger testNumber = BigInteger.valueOf(1);
        BigInteger squareNumber = BigInteger.valueOf(1);
        BigInteger squareRoot = null;
        RoundingMode[] roundingModes = RoundingMode.values();
        for (int i = 0; i < roundingModes.length; i++) {
            roundingModes[i] = roundingModes[i].name().toUpperCase();
            for (int j = 0; j < 10; j++) {
                squareNumber = squareNumber.multiply(BigInteger.TEN);
                squareRoot = BigIntegerMath.sqrt(squareNumber, roundingModes[i]);
                BigInteger squareReal = squareNumber.pow(0.5);
                assertTrue(Math.abs(squareRoot.longValue() - squareReal.longValue()) <= 1);
                squareNumber = squareNumber.add(BigInteger.ONE);
            }
        }

        // Test negative number
        assertNull(BigIntegerMath.sqrt(BigInteger.ONE.negate(), RoundingMode.ANY_VALUE));

        // Test zero
        assertEquals(BigInteger.ZERO, BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.ANY_VALUE));

        // Test large number
        MathPreconditions.checkArgument(BigIntegerMath.sqrt(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.ANY_VALUE).compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0);
    }

    // Test for divide method
    @Test
    public void testDivide() {
        BigInteger dividend = BigInteger.valueOf(10);
        BigInteger divisor = BigInteger.valueOf(3);
        BigInteger expectedResult = BigInteger.valueOf(3);
        BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR);
        assertEquals(expectedResult, result);

        // Test negative dividend
        dividend = dividend.negate();
        expectedResult = expectedResult.negate();
        result = BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR);
        assertEquals(expectedResult, result);

        // Test negative divisor
        divisor = divisor.negate();
        expectedResult = expectedResult.negate();
        result = BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR);
        assertEquals(expectedResult, result);

        // Test division by zero
        assertNull(BigIntegerMath.divide(BigInteger.ZERO, divisor, RoundingMode.ANY_VALUE));
        assertNull(BigIntegerMath.divide(dividend, BigInteger.ZERO, RoundingMode.ANY_VALUE));

        // Test large dividend and divisor
        BigInteger largeDividend = BigInteger.valueOf(Long.MAX_VALUE);
        BigInteger largeDivisor = BigInteger.valueOf(Long.MAX_VALUE / 2);
        result = BigIntegerMath.divide(largeDividend, largeDivisor, RoundingMode.CEILING);
        assertTrue(result.compareTo(BigInteger.valueOf(Long.MAX_VALUE / 2)) <= 0);
    }

    // Test for factorial method
    @Test
    public void testFactorial() {
        BigInteger testNumber = BigInteger.valueOf(0);
        BigInteger expectedResult = BigInteger.ONE;
        BigInteger result = BigIntegerMath.factorial(testNumber.intValue());
        assertEquals(expectedResult, result);

        testNumber = BigInteger.valueOf(5);
        expectedResult = BigInteger.valueOf(120);
        result = BigIntegerMath.factorial(testNumber.intValue());
        assertEquals(expectedResult, result);

        testNumber = BigInteger.valueOf(10);
        expectedResult = BigIntegerMath.listProduct(BigIntegerMath.LongMath.factorials, 0, 10).shiftLeft(testNumber.bitLength());
        result = BigIntegerMath.factorial(testNumber.intValue());
        assertEquals(expectedResult, result);

        testNumber = BigInteger.valueOf(100);
        expectedResult = BigIntegerMath.listProduct(BigIntegerMath.LongMath.factorials, 0, 100).shiftLeft(testNumber.bitLength());
        result = BigIntegerMath.factorial(testNumber.intValue());
        assertEquals(expectedResult, result);
    }
}