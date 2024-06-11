package com.google.common.math;

import com.google.common.base.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 BigInteger x = BigInteger.valueOf(16);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(27);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test
 public void testLog2() {
 BigInteger x = BigInteger.valueOf(16);
 int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(4, result);
 x = BigInteger.valueOf(28);
 result = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(5, result);
 x = BigInteger.valueOf(17);
 try {
 result = BigIntegerMath.log2(x, RoundingMode.UP);
 fail();
 } catch (AssertionError e) {
 // expected
 }
 x = BigInteger.valueOf(1024);
 int expected = 10;
 int resultUp = BigIntegerMath.log2(x, RoundingMode.UP);
 assertEquals(expected, resultUp);
 }

 @Test
 public void testLog10() {
 BigInteger x = BigInteger.valueOf(100);
 int result = BigIntegerMath.log10(x, RoundingMode.HALF_UP);
 assertEquals(2, result);
 x = BigInteger.valueOf(103);
 result = BigIntegerMath.log10(x, RoundingMode.HALF_UP);
 assertEquals(1, result);
 }

 @Test(expected = AssertionError.class)
 public void testLog10WithInvalidMode() {
 BigInteger x = BigInteger.valueOf(10);
 int result = BigIntegerMath.log10(x, RoundingMode.CEILING);
 }

 @Test
 public void testSqrt() {
 BigInteger x = BigInteger.valueOf(16);
 BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(BigInteger.valueOf(4), result);
 x = BigInteger.valueOf(2);
 result = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertEquals(BigInteger.ONE, result);
 x = BigInteger.valueOf(9);
 result = BigIntegerMath.sqrt(x, RoundingMode.CEILING);
 assertEquals(BigInteger.valueOf(3), result);
 }

 @Test(expected = AssertionError.class)
 public void testSqrtWithInvalidMode() {
 BigInteger x = BigInteger.valueOf(4);
 BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.HALF_DOWN);
 }

 @Test
 public void testDivide() {
 BigInteger dividend = BigInteger.valueOf(100);
 BigInteger divisor = BigInteger.valueOf(5);
 BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP);
 assertEquals(BigInteger.valueOf(20), result);
 }

 @Test(expected = AssertionError.class)
 public void testDivideWithInvalidMode() {
 BigInteger dividend = BigInteger.valueOf(100);
 BigInteger divisor = BigInteger.valueOf(5);
 BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.CEILING);
 }

 @Test
 public void testFactorial() {
 BigInteger result = BigIntegerMath.factorial(5);
 BigInteger expected = BigInteger.valueOf(120);
 assertEquals(expected, result);
 result = BigIntegerMath.factorial(26);
 expected = BigInteger.valueOf("15504000000000000");
 assertEquals(expected, result);
 }

 @Test(expected = AssertionError.class)
 public void testFactorialWithNegativeNumber() {
 BigInteger result = BigIntegerMath.factorial(-5);
 }

 @Test
 public void testBinomialCoefficient() {
 BigInteger result = BigIntegerMath.binomial(10, 3);
 BigInteger expected = BigInteger.valueOf("1140");
 assertEquals(expected, result);
 }

 @Test
 public void testBinomialCoefficientWithLargeInput() {
 BigInteger result = BigIntegerMath.binomial(50, 17);
 BigInteger expected = BigInteger.valueOf("417690975394482512597626830455810555111211805583635527136798002724918344494834388327");
 assertEquals(expected, result);
 }

 @Test
 public void testFitsInLong() {
 BigInteger x = BigInteger.valueOf(Long.MAX_VALUE);
 assertTrue(BigIntegerMath.fitsInLong(x));
 x = BigInteger.valueOf(Long.MAX_VALUE + 1);
 assertFalse(BigIntegerMath.fitsInLong(x));
 }
}