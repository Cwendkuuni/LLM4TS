package com.google.common.math.test;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import com.google.common.math.BigIntegerMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import org.junit.Test;

public class BigIntegerMathTest {

  @Test
  public void testIsPowerOfTwo() {
    assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.TEN));
    assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.shiftLeft(32)));
  }

  @Test
  public void testLog2WithRoundingModes() {
    BigInteger x = BigInteger.valueOf(16);
    assertEquals(4, BigIntegerMath.log2(x, RoundingMode.DOWN));
    assertEquals(5, BigIntegerMath.log2(x, RoundingMode.UP));
    assertEquals(4, BigIntegerMath.log2(x, RoundingMode.FLOOR));
    assertEquals(5, BigIntegerMath.log2(x, RoundingMode.CEILING));
    IllegalStateException exception = null;
    try {
      BigIntegerMath.log2(x, RoundingMode.HALF_DOWN);
    } catch (IllegalArgumentException e) {
      exception = e;
    }
    assertNotNull(exception);
  }

  @Test
  public void testLog10WithRoundingModes() {
    BigInteger x = BigInteger.valueOf(12345);
    assertEquals(4, BigIntegerMath.log10(x, RoundingMode.DOWN));
    assertEquals(5, BigIntegerMath.log10(x, RoundingMode.UP));
    assertEquals(4, BigIntegerMath.log10(x, RoundingMode.FLOOR));
    assertEquals(5, BigIntegerMath.log10(x, RoundingMode.CEILING));
    assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(10000), RoundingMode.HALF_DOWN));
    assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.HALF_UP));
    assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.HALF_EVEN));
  }

  @Test
  public void testSqrtWithRoundingModes() {
    BigInteger x = BigInteger.valueOf(16);
    BigInteger sqrt = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
    assertEquals(BigInteger.FOUR, sqrt);
    sqrt = BigIntegerMath.sqrt(x, RoundingMode.UP);
    assertTrue(x.compareTo(sqrt.multiply(sqrt)) <= 0);
    sqrt = BigIntegerMath.sqrt(x, RoundingMode.CEILING);
    assertTrue(x.compareTo(sqrt.multiply(sqrt)) <= 0);
    sqrt = BigIntegerMath.sqrt(x, RoundingMode.HALF_DOWN);
    assertTrue(x.compareTo(sqrt.multiply(sqrt)) <= 0);
    sqrt = BigIntegerMath.sqrt(x, RoundingMode.HALF_UP);
    assertTrue(x.compareTo(sqrt.multiply(sqrt).add(BigInteger.ONE)) <= 0);
    sqrt = BigIntegerMath.sqrt(x, RoundingMode.HALF_EVEN);
    assertTrue(x.compareTo(sqrt.multiply(sqrt)) <= 0);
  }

  @Test
  public void testDivide() {
    BigInteger dividend = BigInteger.valueOf(12345);
    BigInteger divisor = BigInteger.valueOf(7);
    BigInteger expected = BigInteger.valueOf(1763);
    BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
    assertEquals(expected, result);
    result = BigIntegerMath.divide(dividend, divisor, RoundingMode.UP);
    assertTrue(expected.compareTo(result) <= 0);
    result = BigIntegerMath.divide(dividend, divisor, RoundingMode.CEILING);
    assertTrue(expected.compareTo(result) <= 0);
    result = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN);
    assertTrue(expected.compareTo(result) <= 0);
    result = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP);
    assertTrue(expected.compareTo(result) < 0);
    result = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN);
    assertTrue(expected.compareTo(result) <= 0);
  }

  @Test
  public void testFactorial() {
    assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
    assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
    assertEquals(BigInteger.TEN, BigIntegerMath.factorial(2));
    assertEquals(BigInteger.valueOf(243290200817664), BigIntegerMath.factorial(13));
  }

  @Test
  public void testBinomial() {
    assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
    assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
    assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
    assertEquals(BigInteger.TEN, BigIntegerMath.binomial(2, 1));
    assertEquals(BigInteger.valueOf(35), BigIntegerMath.binomial(5, 2));
    assertEquals(BigInteger.valueOf(425), BigIntegerMath.binomial(7, 3));
    assertEquals(BigInteger.valueOf(3527), BigIntegerMath.binomial(10, 4));
    assertEquals(BigInteger.valueOf(2002511007151), BigIntegerMath.binomial(21, 10));
    assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.binomial(10, 5));
  }

  @Test
  public void testLog2FitsInLong() {
    assertTrue(BigIntegerMath.fitsInLong(BigInteger.ONE));
    assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(123456789)));
    assertFalse(BigIntegerMath.fitsInLong(BigInteger.ONE.shiftLeft(64)));
  }
}