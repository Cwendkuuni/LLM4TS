package com.google.common.math;

import com.google.common.base.Preconditions;
import com.google.common.annotations.VisibleForTesting;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @VisibleForTesting
    static final int TEST_DATA_SIZE = 10;

    @org.junit.BeforeClass
    public static void setUp() {
        BigIntegerMath.SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
        BigIntegerMath.LN_10 = Math.log(10.0);
        BigIntegerMath.LN_2 = Math.log(2.0);
    }

    @org.junit.Test
    public void testIsPowerOfTwo() {
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            BigInteger testValue = BigInteger.valueOf(1 << i);
            assertTrue(BigIntegerMath.isPowerOfTwo(testValue));

            testValue = BigInteger.ONE.shiftLeft(i).add(BigInteger.ONE);
            assertFalse(BigIntegerMath.isPowerOfTwo(testValue));
        }
    }

    @org.junit.Test
    public void testLog2() {
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            BigInteger testValue = BigInteger.valueOf(1 << i);
            int expected = i;
            int actual = BigIntegerMath.log2(testValue, RoundingMode.DOWN);
            assertEquals(expected, actual);
        }

        BigInteger bigNumber = new BigInteger("1234567890123456789012345678901234567890");
        for (RoundingMode roundingMode : RoundingMode.values()) {
            int actual = BigIntegerMath.log2(bigNumber, roundingMode);
            int expected = Math.log(bigNumber.longValue() & 0xFFFFFFFFFFFFL) / Math.log(2.0) + 1;
            switch (roundingMode) {
                case DOWN:
                case FLOOR:
                    assertEquals(expected - 1, actual);
                    break;
                case UP:
                case CEILING:
                    assertEquals(expected, actual);
                    break;
                case HALF_DOWN:
                    // TODO: Implement test for HALF_DOWN
                    break;
                case HALF_UP:
                    // TODO: Implement test for HALF_UP
                    break;
                case HALF_EVEN:
                    // TODO: Implement test for HALF_EVEN
                    break;
            }
        }
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testLog2InvalidRoundingMode() {
        BigInteger bigNumber = new BigInteger("1234567890123456789012345678901234567890");
        BigIntegerMath.log2(bigNumber, new RoundingMode("INVALID"));
    }

    @org.junit.Test
    public void testLog10() {
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            BigInteger testValue = BigInteger.valueOf(10L << i);
            double expected = Math.log10(testValue.longValue());
            BigInteger actual = BigIntegerMath.log10(testValue, RoundingMode.DOWN);
            assertEquals(expected, actual.doubleValue(), 0.0000001);
        }

        BigInteger bigNumber = new BigInteger("1234567890123456789012345678901234567890");
        for (RoundingMode roundingMode : RoundingMode.values()) {
            BigInteger actual = BigIntegerMath.log10(bigNumber, roundingMode);
            double expected = Math.log10(bigNumber.longValue() & 0xFFFFFFFFFFFFL);
            switch (roundingMode) {
                case DOWN:
                case FLOOR:
                    assertEquals(expected, actual.doubleValue(), 0.0000001);
                    break;
                case UP:
                case CEILING:
                    BigInteger maxValue = new BigInteger("9999999999999999999999999999999999999999");
                    BigInteger expectedCeiling = BigIntegerMath.log10(maxValue, RoundingMode.DOWN).add(BigInteger.ONE);
                    assertTrue(expectedCeiling.compareTo(actual) >= 0);
                    break;
                case HALF_DOWN:
                    // TODO: Implement test for HALF_DOWN
                    break;
                case HALF_UP:
                    // TODO: Implement test for HALF_UP
                    break;
                case HALF_EVEN:
                    // TODO: Implement test for HALF_EVEN
                    break;
            }
        }
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testLog10InvalidRoundingMode() {
        BigInteger bigNumber = new BigInteger("1234567890123456789012345678901234567890");
        BigIntegerMath.log10(bigNumber, new RoundingMode("INVALID"));
    }

    @org.junit.Test
    public void testSqrt() {
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            BigInteger testValue = BigInteger.valueOf(Math.pow(2, i));
            BigInteger expected = new BigInteger(Math.sqrt(testValue.longValue()));
            BigInteger actual = BigIntegerMath.sqrt(testValue, RoundingMode.DOWN);
            assertEquals(expected, actual);
        }

        BigInteger bigNumber = new BigInteger("1234567890123456789012345678901234567890");
        for (RoundingMode roundingMode : RoundingMode.values()) {
            BigInteger actual = BigIntegerMath.sqrt(bigNumber, roundingMode);
            BigInteger expectedSquared = bigNumber.multiply(bigNumber);
            BigInteger expectedSquareRoot = new BigInteger(Math.sqrt(expectedSquared.longValue()));
            switch (roundingMode) {
                case DOWN:
                case FLOOR:
                    assertEquals(expectedSquareRoot, actual);
                    break;
                case UP:
                case CEILING:
                    BigInteger maxValue = new BigInteger("9999999999999999999999999999999999999999");
                    BigInteger expectedCeilingSquareRoot = BigIntegerMath.sqrt(maxValue, RoundingMode.DOWN).add(BigInteger.ONE);
                    assertTrue(expectedCeilingSquareRoot.compareTo(actual) >= 0);
                    break;
                case HALF_DOWN:
                    // TODO: Implement test for HALF_DOWN
                    break;
                case HALF_UP:
                    // TODO: Implement test for HALF_UP
                    break;
                case HALF_EVEN:
                    // TODO: Implement test for HALF_EVEN
                    break;
            }
        }
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testSqrtInvalidRoundingMode() {
        BigInteger bigNumber = new BigInteger("1234567890123456789012345678901234567890");
        BigIntegerMath.sqrt(bigNumber, new RoundingMode("INVALID"));
    }

    @org.junit.Test
    public void testDivide() {
        BigInteger dividend = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger divisor = new BigInteger("11111111111111111111111111111111111111111");

        for (RoundingMode roundingMode : RoundingMode.values()) {
            BigInteger expected = dividend.divide(divisor, 0, roundingMode);
            BigInteger actual = BigIntegerMath.divide(dividend, divisor, roundingMode);
            assertEquals(expected, actual);
        }
    }

    @org.junit.Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        BigInteger dividend = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger divisor = BigInteger.ZERO;
        BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
    }

    @org.junit.Test
    public void testFactorial() {
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            int n = i;
            BigInteger expected = BigInteger.valueOf(LongMath.factorial(n));
            BigInteger actual = BigIntegerMath.factorial(n);
            assertEquals(expected, actual);
        }

        BigInteger bigNumber = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger actual = BigIntegerMath.factorial(bigNumber.intValue());
        BigInteger expected = listProduct(LongMath.factorials, 0, bigNumber.intValue());
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testBinomial() {
        for (int n = 0; n < TEST_DATA_SIZE; n++) {
            for (int k = 0; k <= n; k++) {
                BigInteger expected = BigInteger.valueOf(LongMath.binomial(n, k));
                BigInteger actual = BigIntegerMath.binomial(n, k);
                assertEquals(expected, actual);
            }
        }
    }

    @org.junit.Test
    public void testFitsInLong() {
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            BigInteger testValue = BigInteger.valueOf(1 << i);
            assertTrue(BigIntegerMath.fitsInLong(testValue) == (i < 64));
        }
    }

    private static List<BigInteger> listProduct(List<BigInteger> nums) {
        return BigIntegerMath.listProduct(nums);
    }

    private static List<BigInteger> listProduct(List<BigInteger> nums, int start, int end) {
        return BigIntegerMath.listProduct(nums, start, end);
    }

    private static List<BigInteger> listProduct(List<BigInteger> nums) {
        return BigIntegerMath.listProduct(nums, 0, nums.size());
    }

    private static List<BigInteger> listProduct(List<BigInteger> nums, int start, int end) {
        return BigIntegerMath.listProduct(nums, start, end);
    }

    private static List<BigInteger> listProduct(List<BigInteger> nums) {
        ArrayList<BigInteger> result = new ArrayList<>();
        for (int i = 0; i < nums.size(); ++i) {
            if (i == 0) {
                result.add(BigInteger.ONE);
            }
            result.add(result.get(i - 1).multiply(nums.get(i)));
        }
        return result;
    }

    private static List<BigInteger> listProduct(List<BigInteger> nums, int start, int end) {
        ArrayList<BigInteger> result = new ArrayList<>();
        int index = start;
        BigInteger accumulator = BigInteger.ONE;
        while (index < end) {
            result.add(accumulator);
            accumulator = accumulator.multiply(nums.get(index++));
        }
        return result;
    }

    private static List<BigInteger> listProduct(List<BigInteger> nums) {
        ArrayList<BigInteger> result = new ArrayList<>();
        int index = 0;
        BigInteger accumulator = BigInteger.ONE;
        while (index < nums.size()) {
            result.add(accumulator);
            accumulator = accumulator.multiply(nums.get(index++));
        }
        return result;
    }

    private static List<BigInteger> listProduct(List<BigInteger> nums, int start, int end) {
        ArrayList<BigInteger> result = new ArrayList<>();
        int index = start;
        BigInteger accumulator = BigInteger.ONE;
        while (index < end) {
            result.add(accumulator);
            accumulator = accumulator.multiply(nums.get(index++));
        }
        return result;
    }
}