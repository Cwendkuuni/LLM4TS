package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.math.BigIntegerMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 BigInteger x = BigInteger.ONE;
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = x.shiftLeft(10);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = x.add(BigInteger.ONE);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test
 public void testLog2WithPositiveNumbers() {
 BigInteger x = BigInteger.ONE;
 int expected = 0;
 for (int i = 0; i < 31; i++) {
 int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(expected, result);
 x = x.shiftLeft(1);
 expected++;
 }
 x = BigInteger.valueOf(Long.MAX_VALUE);
 int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(63, result);
 }

 @Test
 public void testLog2WithNegativeNumbers() {
 BigInteger x = BigInteger.valueOf(-1);
 int expected = -1;
 for (int i = 0; i < 31; i++) {
 int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(expected, result);
 x = x.shiftLeft(1);
 expected++;
 }
 x = BigInteger.valueOf(-2);
 int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(-1, result);
 }

 @Test
 public void testLog2WithRoundingModes() {
 BigInteger x = BigInteger.ONE;
 int expected = 0;
 for (int i = 0; i < 31; i++) {
 int result = BigIntegerMath.log2(x, RoundingMode.UP);
 assertEquals(expected, result);
 x = x.shiftLeft(1);
 expected++;
 }
 x = BigInteger.valueOf(Long.MAX_VALUE);
 int result = BigIntegerMath.log2(x, RoundingMode.UP);
 assertEquals(63, result);
 x = BigInteger.valueOf(-1);
 int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(-1, result);
 int resultRoundedUp = BigIntegerMath.log2(x, RoundingMode.UP);
 assertEquals(0, resultRoundedUp);
 }

 @Test
 public void testLog10WithPositiveNumbers() {
 BigInteger x = BigInteger.ONE;
 int expected = 0;
 for (int i = 0; i < 31; i++) {
 double ln10 = Math.log10(1L << i);
 BigInteger result = BigIntegerMath.log10(BigInteger.valueOf(1L << i), RoundingMode.DOWN);
 double calculated = result.doubleValue();
 assertEquals(expected, (int) (ln10 * 1000000.0), 0.0001);
 x = x.multiply(BigInteger.TEN);
 expected++;
 }
 }

 @Test
 public void testLog10WithNegativeNumbers() {
 BigInteger x = BigInteger.ONE.negate();
 int expected = -1;
 for (int i = 0; i < 31; i++) {
 double ln10 = Math.log10(1L << i);
 BigInteger result = BigIntegerMath.log10(x, RoundingMode.DOWN);
 double calculated = result.doubleValue();
 assertEquals(expected, (int) (ln10 * 1000000.0), 0.0001);
 x = x.multiply(BigInteger.TEN);
 expected++;
 }
 x = x.negate();
 int result = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(-1, result);
 }

 @Test
 public void testLog10WithRoundingModes() {
 BigInteger x = BigInteger.ONE;
 int expected = 0;
 for (int i = 0; i < 31; i++) {
 double ln10 = Math.log10(1L << i);
 BigInteger result = BigIntegerMath.log10(x, RoundingMode.UP);
 double calculated = result.doubleValue();
 assertEquals(expected, (int) (ln10 * 1000000.0), 0.0001);
 x = x.multiply(BigInteger.TEN);
 expected++;
 }
 x = BigInteger.valueOf(Long.MIN_VALUE).negate();
 int result = BigIntegerMath.log10(x, RoundingMode.UP);
 assertEquals(-1, result);
 int resultRoundedDown = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(0, resultRoundedDown);
 }

 @Test
 public void testSqrtWithPositiveNumbers() {
 BigInteger x = BigInteger.ZERO;
 BigInteger expected = BigInteger.ZERO;
 for (int i = 1; i <= 16; i++) {
 BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(expected, result);
 x = x.add(BigInteger.valueOf(i * i));
 expected = expected.add(BigInteger.valueOf(i));
 }
 x = BigInteger.valueOf(Long.MAX_VALUE);
 BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(x, result);
 }

 @Test
 public void testSqrtWithNegativeNumbers() {
 BigInteger x = BigInteger.ONE.negate();
 BigInteger expected = BigInteger.ONE.negate();
 for (int i = 1; i <= 16; i++) {
 BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(expected, result);
 x = x.add(BigInteger.valueOf(i * i));
 expected = expected.add(BigInteger.valueOf(i));
 }
 x = BigInteger.ONE;
 BigInteger result = BigIntegerMath.sqrt(x.negate(), RoundingMode.DOWN);
 assertEquals(BigInteger.ONE.negate(), result);
 }

 @Test
 public void testSqrtWithRoundingModes() {
 BigInteger x = BigInteger.ZERO;
 BigInteger expected = BigInteger.ZERO;
 for (int i = 1; i <= 16; i++) {
 BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertEquals(expected, result);
 x = x.add(BigInteger.valueOf(i * i));
 expected = expected.add(BigInteger.valueOf(i));
 }
 x = BigInteger.valueOf(Long.MIN_VALUE).negate();
 BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertEquals(BigInteger.ONE.negate(), result);
 BigInteger resultRoundedDown = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(BigInteger.ZERO, resultRoundedDown);
 }

 @Test
 public void testDivide() {
 BigInteger dividend = BigInteger.valueOf(12345678901234567890L);
 BigInteger divisor = BigInteger.valueOf(7);
 BigInteger expected = BigInteger.valueOf(1763665293084202890L);
 BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
 assertEquals(expected, result);
 }

 @Test(expected = AssertionError.class)
 public void testDivideWithInvalidDividend() {
 BigInteger dividend = null;
 BigInteger divisor = BigInteger.valueOf(7);
 BigInteger expected = BigInteger.valueOf(12345678901234567890L);
 BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
 }

 @Test(expected = AssertionError.class)
 public void testDivideWithInvalidDivisor() {
 BigInteger dividend = BigInteger.valueOf(12345678901234567890L);
 BigInteger divisor = null;
 BigInteger expected = BigInteger.valueOf(12345678901234567890L);
 BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
 }

 @Test(expected = AssertionError.class)
 public void testDivideWithUnexpectedRoundingMode() {
 BigInteger dividend = BigInteger.valueOf(12345678901234567890L);
 BigInteger divisor = BigInteger.valueOf(7);
 BigInteger expected = BigInteger.valueOf(1763665293084202890L);
 BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.UPPER_QUARTILE);
 }

 @Test
 public void testFactorial() {
 BigInteger expected = BigInteger.ONE;
 for (int i = 0; i <= 10; i++) {
 BigInteger result = BigIntegerMath.factorial(i);
 assertEquals(expected, result);
 expected = expected.multiply(BigInteger.valueOf(i + 1));
 }
 }

 @Test
 public void testFactorialWithBigNumbers() {
 BigInteger result = BigIntegerMath.factorial(100);
 assertEquals(fact(100), result.longValueExact());
 }

 @Test(expected = AssertionError.class)
 public void testFactorialWithNegativeNumber() {
 BigInteger result = BigIntegerMath.factorial(-1);
 }
}