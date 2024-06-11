package com.google.common.math; // make sure the package name is correct

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.junit.Test;

public class BigIntegerMathTest {

  

  @Test
  public void testIsPowerOfTwo() {
    assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
    assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("10")));
    assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("-10")));
    assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("20")));
    assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("100").negate()));
  }

  @Test
  public void testLog2() {
    int log2Expected = 0;
    for (int i = 0; i <= 31; i++) {
      log2Expected += 1;
      assertEquals(log2Expected, BigIntegerMath.log2(BigInteger.valueOf(1L << i), RoundingMode.HALF_UP));
    }

    assertEquals(33, BigIntegerMath.log2(new BigInteger("8192"), RoundingMode.HALF_UP));
    assertEquals(32, BigIntegerMath.log2(new BigInteger("16777216"), RoundingMode.HALF_UP));

    try {
      BigIntegerMath.log2(new BigInteger("-1"), RoundingMode.HALF_UP);
      fail("Expected IllegalArgumentException with negative BigInteger.");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      BigIntegerMath.log2(new BigInteger("0"), RoundingMode.HALF_UP);
      fail("Expected IllegalArgumentException with zero BigInteger.");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testLog10() {
    // TODO: Implement testLog10 method with edge cases and exceptions
  }

  @Test
  public void testSqrt() {
    // TODO: Implement testSqrt method with edge cases and exceptions
  }

  @Test
  public void testSqrtFloor() {
    assertEquals(new BigInteger("3"), BigIntegerMath.sqrtFloor(new BigInteger("9")));
    assertEquals(new BigInteger("4"), BigIntegerMath.sqrtFloor(new BigInteger("16")));

    assertEquals(new BigInteger("1024"), BigIntegerMath.sqrtFloor(new BigInteger("1048576")));
    assertEquals(new BigInteger("65536"), BigIntegerMath.sqrtFloor(new BigInteger("4294967296")));

    try {
      BigIntegerMath.sqrtFloor(new BigInteger("-1"));
      fail("Expected IllegalArgumentException with negative BigInteger.");
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      BigIntegerMath.sqrtFloor(new BigInteger("0"));
      fail("Expected IllegalArgumentException with zero BigInteger.");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDivide() {
    // TODO: Implement testDivide method with edge cases and exceptions
  }

  @Test
  public void testFactorial() {
    assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
    assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
    assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
    assertEquals(new BigInteger("6"), BigIntegerMath.factorial(3));
    assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
    assertEquals(new BigInteger("120"), BigIntegerMath.factorial(5));

    assertEquals(new BigInteger("511520"), BigIntegerMath.factorial(10));
    assertEquals(new BigInteger("3628800"), BigIntegerMath.factorial(15));

    try {
      BigIntegerMath.factorial(-1);
      fail("Expected IllegalArgumentException with negative BigInteger.");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testBinomial() {
    // TODO: Implement testBinomial method with edge cases and exceptions
  }

  
}