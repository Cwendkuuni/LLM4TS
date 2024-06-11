package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.MathPreconditions;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.BigIntegerMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
    }

    @Test
    public void testLog2WithPositiveArgument() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483647L), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog2WithEdgeCases() {
        // Test negative number
        try {
            BigIntegerMath.log2(BigInteger.valueOf(-1), RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test zero
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.UNNECESSARY));

        // Test BigInteger that is not power of 2
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(5), RoundingMode.UNNECESSARY));

        // Test BigInteger that is power of 2 but with bits set other than the highest bit
        assertEquals(5, BigIntegerMath.log2(BigInteger.valueOf(32768), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog2WithRoundingModes() {
        // Test UP rounding mode, which always rounds up
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(32768), RoundingMode.UP));

        // Test DOWN rounding mode, which always rounds down
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483647L), RoundingMode.DOWN));

        // Test FLOOR rounding mode, which rounds down if the result is exactly halfway between two integers
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(9), RoundingMode.FLOOR));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483648L), RoundingMode.FLOOR));

        // Test CEILING rounding mode, which rounds up if the result is exactly halfway between two integers
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(9), RoundingMode.CEILING));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(2147483648L), RoundingMode.CEILING));

        // Test HALF_DOWN rounding mode, which rounds down if the result is exactly halfway between an even number and an odd number
        // and up otherwise
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(10), RoundingMode.HALF_DOWN));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(2147483649L), RoundingMode.HALF_DOWN));

        // Test HALF_UP rounding mode, which rounds up if the result is exactly halfway between an even number and an odd number
        // and down otherwise
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(10), RoundingMode.HALF_UP));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483648L), RoundingMode.HALF_UP));
    }

    @Test
    public void testLog10() {
        // Test log10 of small positive integers
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));

        // Test log10 of large positive integers
        assertEquals(31, BigIntegerMath.log10(BigInteger.valueOf(1000000000000000001L), RoundingMode.UNNECESSARY));

        // Test log10 of negative number
        try {
            BigIntegerMath.log10(BigInteger.valueOf(-1), RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test log10 of zero
        try {
            BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test log10 of BigInteger that doesn't fit into long
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(1234567890L), RoundingMode.UNNECESSARY));
        assertEquals(10, BigIntegerMath.log10(BigInteger.valueOf(1234567890123456789L), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt() {
        // Test positive numbers
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(163), BigIntegerMath.sqrt(BigInteger.valueOf(26241), RoundingMode.UNNECESSARY));

        // Test negative number
        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(-1), RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test zero
        try {
            BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.UNNECESSARY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test BigInteger that is not a perfect square
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testDivide() {
        // Test positive numbers
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(9), BigInteger.valueOf(3), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(163), BigIntegerMath.divide(BigInteger.valueOf(26241), BigInteger.valueOf(163), RoundingMode.UNNECESSARY));

        // Test zero dividend
        try {
            BigIntegerMath.divide(BigInteger.ZERO, BigInteger.ONE, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Test zero divisor
        try {
            BigIntegerMath.divide(BigInteger.ONE, BigInteger.ZERO, RoundingMode.UNNECESSARY);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Test very large numbers
        BigInteger divisor = BigInteger.valueOf(Long.MAX_VALUE);
        BigInteger dividend = BigInteger.valueOf(Long.MAX_VALUE + 1);
        BigDecimal dividendDec = new BigDecimal(dividend);
        BigDecimal divisorDec = new BigDecimal(divisor);
        BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
        BigDecimal expectedResultDec = dividendDec.divide(divisorDec, BigDecimal.ROUND_UNNECESSARY);
        assertEquals(expectedResultDec.toBigIntegerExact(), result);
    }

    @Test
    public void testFactorial() {
        // Test small integers
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));

        // Test very large integers
        List<BigInteger> factorials = new ArrayList<>();
        factorials.add(BigInteger.ONE);
        for (int i = 1; i <= 10; ++i) {
            factorials.add(factorials.get(factorials.size() - 1).multiply(BigInteger.valueOf(i)));
        }
        BigInteger expected = BigInteger.valueOf(3628800);
        assertEquals(expected, BigIntegerMath.factorial(10));

        // Test negative integers
        try {
            BigIntegerMath.factorial(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test very large integers that don't fit into long
        assertEquals(factorial(50), BigIntegerMath.factorial(50));
    }

    @Test
    public void testListProduct() {
        // Test product of empty list
        assertEquals(BigInteger.ONE, BigIntegerMath.listProduct(Lists.newArrayList(), 0, 0));

        // Test product of one-element list
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.listProduct(Lists.newArrayList(BigInteger.valueOf(2)), 0, 1));

        // Test product of two-element list
        assertEquals(BigInteger.valueOf(2 * 3), BigIntegerMath.listProduct(Lists.newArrayList(BigInteger.valueOf(2), BigInteger.valueOf(3)), 0, 2));

        // Test product of larger list
        List<BigInteger> nums = new ArrayList<>();
        nums.add(BigInteger.valueOf(2));
        nums.add(BigInteger.valueOf(3));
        nums.add(BigInteger.valueOf(5));
        assertEquals(BigInteger.valueOf(2 * 3 * 5), BigIntegerMath.listProduct(nums, 0, nums.size()));
    }

    @Test
    public void testBinomial() {
        // Test binomial coefficient of small integers
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 2));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 0));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));

        // Test binomial coefficient of larger integers that fit into long
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(4, 3));

        // Test very large integers that don't fit into long
        assertEquals(BigInteger.binomial(100, 50), BigIntegerMath.binomial(100, 50));

        // Test negative integers
        try {
            BigIntegerMath.binomial(-1, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test k > n
        try {
            BigIntegerMath.binomial(5, 6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}