package com.example.test;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.math.BigIntegerMath;
import com.google.common.math.MathPreconditions;

public class BigIntegerMathTest {
  @Test
  public void testIsPowerOfTwo() {
    // Test with positive BigIntegers
    assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1")));
    assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("8")));
    assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("16")));

    // Test with negative BigIntegers
    assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("-1")));
    assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("-512")));

    // Test with large BigIntegers
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(Long.MAX_VALUE)));
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(Long.MIN_VALUE)));

    // Test with zero
    assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
  }

  @Test
  public void testLog2() {
    // Test with positive BigIntegers and all rounding modes
    assertEquals(0, BigIntegerMath.log2(new BigInteger("1"), RoundingMode.UNNECESSARY));
    assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.DOWN));
    assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.FLOOR));
    assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.UP));
    assertEquals(3, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.CEILING));
    assertEquals(8, BigIntegerMath.log2(new BigInteger("256"), RoundingMode.HALF_DOWN));
    assertEquals(9, BigIntegerMath.log2(new BigInteger("256"), RoundingMode.HALF_UP));
    assertEquals(9, BigIntegerMath.log2(new BigInteger("256"), RoundingMode.HALF_EVEN));

    // Test with negative BigIntegers
    assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.log2(new BigInteger("-1"), RoundingMode.UNNECESSARY));
    assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.log2(new BigInteger("-512"), RoundingMode.UNNECESSARY));

    // Test with large BigIntegers
    assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.log2(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.UNNECESSARY));
    assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.log2(BigInteger.valueOf(Long.MIN_VALUE), RoundingMode.UNNECESSARY));

    // Test rounding exceptions
    MathPreconditions.checkRoundingUnnecessary(true, () -> BigIntegerMath.log2(new BigInteger("1"), RoundingMode.UNNECESSARY));
    assertThrows(IllegalArgumentException.class, () -> BigIntegerMath.log2(new BigInteger("1"), RoundingMode.DOWN));
  }

  @Test
  public void testFactorial() {
    assertEquals(new BigInteger("1"), BigIntegerMath.factorial(0));
    assertEquals(new BigInteger("1"), BigIntegerMath.factorial(1));
    assertEquals(new BigInteger("2"), BigIntegerMath.factorial(2));
    assertEquals(new BigInteger("24"), BigIntegerMath.factorial(4));
    assertEquals(new BigInteger("511200"), BigIntegerMath.factorial(7));
    assertEquals(new BigInteger("479001600"), BigIntegerMath.factorial(10));
    assertEquals(new BigInteger("115796093536873420489"), BigIntegerMath.factorial(13));
  }
}