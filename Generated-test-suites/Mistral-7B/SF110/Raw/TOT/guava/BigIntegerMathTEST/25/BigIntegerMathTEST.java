package com.google.common.math;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.Test;

/**
 * Unit tests for the BigIntegerMath class.
 */
public final class BigIntegerMathTest {

  @Test
  public void testIsPowerOfTwo() {
    // Positive cases
    assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
    assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1 << 31)));

    // Negative cases
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1)));
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
  }

  @Test
  public void testLog2() {
    // Positive cases
    assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
    assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1 << 31), RoundingMode.DOWN));
    assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1 << 31), RoundingMode.UP));
    assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(1 << 32), RoundingMode.UP));

    // Negative cases
    try {
      BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.DOWN);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testLog10() {
    // Positive cases
    assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
    assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.DOWN));
    assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.DOWN));

    // Negative cases
    try {
      BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.DOWN);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      BigIntegerMath.log10(BigInteger.ONE.negate(), RoundingMode.DOWN);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testSqrt() {
    // Positive cases
    assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
    assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UP));

    // Negative cases
    try {
      BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.DOWN);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      BigIntegerMath.sqrt(BigInteger.ONE.negate(), RoundingMode.DOWN);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDivide() {
    // Positive cases
    assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(9), BigInteger.valueOf(3), RoundingMode.DOWN));
    assertEquals(BigInteger.valueOf(15), BigIntegerMath.divide(BigInteger.valueOf(30), BigInteger.valueOf(2), RoundingMode.UP));

    // Edge cases
    assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.HALF_UP));

    // Negative cases
    try {
      BigIntegerMath.divide(BigInteger.ZERO, BigInteger.ONE, RoundingMode.DOWN);
      fail("Expected ArithmeticException");
    } catch (ArithmeticException e) {
      // Expected
    }

    try {
      BigIntegerMath.divide(BigInteger.ONE, BigInteger.ZERO, RoundingMode.DOWN);
      fail("Expected ArithmeticException");
    } catch (ArithmeticException e) {
      // Expected
    }
  }

  @Test
  public void testFactorial() {
    // Positive cases
    assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
    assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
    assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
    assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
    assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));

    // Negative cases
    try {
      BigIntegerMath.factorial(-1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      BigIntegerMath.factorial(2147483647);
      fail("Expected ArithmeticException");
    } catch (ArithmeticException e) {
      // Expected
    }
  }

  @Test
  public void testBinomial() {
    // Positive cases
    assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
    assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
    assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
    assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
    assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(6, 3));

    // Negative cases
    try {
      BigIntegerMath.binomial(-2, 3);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      BigIntegerMath.binomial(6, 7);
      fail("Expected ArithmeticException");
    } catch (ArithmeticException e) {
      // Expected
    }

    // Edge cases
    assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(1, 1).shiftRight(63));
    assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(63, 63).shiftRight(63));
  }

  @Test
  public void testListProduct() {
    // Positive cases
    List<BigInteger> nums = new ArrayList<BigInteger>();
    nums.add(BigInteger.valueOf(2));
    nums.add(BigInteger.valueOf(3));
    nums.add(BigInteger.valueOf(5));
    assertEquals(BigInteger.valueOf(30), BigIntegerMath.listProduct(nums));

    // Negative cases
    nums = new ArrayList<BigInteger>();
    nums.add(BigInteger.ZERO);
    try {
      BigIntegerMath.listProduct(nums);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testFactorialsAndBinomials() {
    // Test that factorial method correctly calculates binomial coefficients
    for (int n = 0; n <= 10; ++n) {
      for (int k = 0; k <= n; ++k) {
        assertEquals(BigIntegerMath.binomial(n, k), BigIntegerMath.factorial(n).divide(BigIntegerMath.factorial(k).multiply(BigIntegerMath.factorial(n - k))));
      }
    }
  }

  @Test
  public void testDivideWithBigDecimals() {
    // Test that divide method correctly handles division of large BigIntegers
    BigDecimal dividendDec = new BigDecimal("12345678901234567890123456789012345678901234567890123456789012345678901234567890", 10);
    BigDecimal divisorDec = new BigDecimal("123456789", 10);
    BigDecimal expectedQuotientDec = dividendDec.divide(divisorDec, new MathContext(150, RoundingMode.HALF_UP));
    BigInteger dividend = new BigInteger(dividendDec.toBigInteger());
    BigInteger divisor = new BigInteger(divisorDec.toBigInteger());
    BigInteger expectedQuotient = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP);
    assertEquals(expectedQuotient, expectedQuotientDec.toBigInteger());
  }
}