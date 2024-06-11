package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 BigInteger bigInteger = BigInteger.ONE;
 assertTrue(BigIntegerMath.isPowerOfTwo(bigInteger));
 bigInteger = bigInteger.shiftLeft(3);
 assertTrue(BigIntegerMath.isPowerOfTwo(bigInteger));
 bigInteger = bigInteger.add(BigInteger.ONE);
 assertFalse(BigIntegerMath.isPowerOfTwo(bigInteger));
 }

 @Test
 public void testLog2() {
 BigInteger bigInteger = BigInteger.ONE;
 assertEquals(0, BigIntegerMath.log2(bigInteger, RoundingMode.FLOOR));
 bigInteger = bigInteger.shiftLeft(1);
 assertEquals(1, BigIntegerMath.log2(bigInteger, RoundingMode.FLOOR));
 bigInteger = bigInteger.shiftLeft(3);
 assertEquals(3, BigIntegerMath.log2(bigInteger, RoundingMode.FLOOR));
 bigInteger = BigInteger.valueOf(Integer.MAX_VALUE);
 assertEquals(31, BigIntegerMath.log2(bigInteger, RoundingMode.CEILING));
 }

 @Test
 public void testLogWithRounding() {
 BigInteger bigInteger = BigInteger.valueOf(128);
 assertEquals(7, BigIntegerMath.log2(bigInteger, RoundingMode.DOWN));
 assertEquals(8, BigIntegerMath.log2(bigInteger, RoundingMode.CEILING));
 bigInteger = BigInteger.ONE;
 assertEquals(0, BigIntegerMath.log2(bigInteger, RoundingMode.DOWN));
 assertEquals(0, BigIntegerMath.log2(bigInteger, RoundingMode.CEILING));
 bigInteger = BigInteger.valueOf(Integer.MIN_VALUE);
 assertEquals(-1, BigIntegerMath.log2(bigInteger, RoundingMode.DOWN));
 assertEquals(0, BigIntegerMath.log2(bigInteger, RoundingMode.CEILING));
 }

 @Test
 public void testLog10() {
 BigInteger bigInteger = BigInteger.valueOf(12345);
 BigDecimal expected = new BigDecimal("3.1111111111111114159265358979311599736768842262255781");
 BigInteger actual = BigIntegerMath.log10(bigInteger, RoundingMode.HALF_UP);
 assertEquals(expected, actual);
 bigInteger = BigInteger.ONE;
 BigDecimal zero = BigDecimal.ZERO;
 BigInteger actualZero = BigIntegerMath.log10(bigInteger, RoundingMode.HALF_UP);
 assertEquals(zero, actualZero);
 }

 @Test(expected = AssertionError.class)
 public void testUnsupportedLog10() {
 BigInteger bigInteger = BigInteger.valueOf(Long.MAX_VALUE);
 BigIntegerMath.log10(bigInteger, RoundingMode.UNNECESSARY);
 }

 @Test
 public void testSqrt() {
 BigInteger bigInteger = BigInteger.valueOf(16);
 BigInteger expected = BigInteger.valueOf(4);
 BigInteger actual = BigIntegerMath.sqrt(bigInteger, RoundingMode.FLOOR);
 assertEquals(expected, actual);
 bigInteger = BigInteger.valueOf(25);
 expected = BigInteger.valueOf(5);
 actual = BigIntegerMath.sqrt(bigInteger, RoundingMode.CEILING);
 assertEquals(expected, actual);
 bigInteger = BigInteger.ONE;
 assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(bigInteger, RoundingMode.UNNECESSARY));
 bigInteger = BigInteger.valueOf(-1);
 assertNull(BigIntegerMath.sqrt(bigInteger, RoundingMode.UNNECESSARY));
 }

 @Test
 public void testFactorial() {
 BigInteger expected = BigInteger.valueOf(120);
 BigInteger actual = BigIntegerMath.factorial(5);
 assertEquals(expected, actual);
 expected = BigInteger.valueOf(1307674368000L);
 actual = BigIntegerMath.factorial(12);
 assertEquals(expected, actual);
 }

 @Test
 public void testBinomial() {
 BigInteger expected = BigInteger.valueOf(2432);
 BigInteger actual = BigIntegerMath.binomial(13, 6);
 assertEquals(expected, actual);
 expected = BigInteger.valueOf(11293234527676240000);
 actual = BigIntegerMath.binomial(100, 50);
 assertEquals(expected, actual);
 }
}