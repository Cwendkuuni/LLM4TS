package com.google.common.math;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 BigInteger x = BigInteger.valueOf(1);
 BigInteger powerOfTwo = BigInteger.ONE.shiftLeft(1);
 for (int i = 0; i < 10; i++) {
 x = powerOfTwo;
 powerOfTwo = powerOfTwo.shiftLeft(1);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 }

 powerOfTwo = powerOfTwo.shiftLeft(1);
 assertFalse(BigIntegerMath.isPowerOfTwo(powerOfTwo));
 }

 @Test
 public void testIsPowerOfTwoWithNegativeNumber() {
 BigInteger x = BigInteger.valueOf(-1);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test
 public void testLog2RoundingModes() {
 BigInteger x = new BigInteger("1234567890123456789012345678901234567890");
 int log2 = 21;
 for (RoundingMode roundingMode : RoundingMode.values()) {
 int result = BigIntegerMath.log2(x, roundingMode);
 assertEquals(log2, result);
 }
 }

 @Test
 public void testLog2WithZero() {
 BigInteger x = BigInteger.ZERO;
 int log2 = 0;
 for (RoundingMode roundingMode : RoundingMode.values()) {
 int result = BigIntegerMath.log2(x, roundingMode);
 assertEquals(log2, result);
 }
 }

 @Test
 public void testLog2WithNegativeNumber() {
 BigInteger x = BigInteger.valueOf(-1);
 try {
 BigIntegerMath.log2(x, RoundingMode.HALF_UP);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLog10WithFitsInLong() {
 BigInteger x = BigInteger.valueOf(12345);
 for (RoundingMode roundingMode : RoundingMode.values()) {
 int result = BigIntegerMath.log10(x, roundingMode).intValue();
 assertEquals(4, result);
 }
 }

 @Test
 public void testLog10WithLargeNumber() {
 BigInteger x = new BigInteger("12345678901234567890123456789012345678901234567890");
 for (RoundingMode roundingMode : RoundingMode.values()) {
 BigInteger result = BigIntegerMath.log10(x, roundingMode);
 // for large numbers, the result can be verified only by comparing it with another large number
 BigInteger expected = new BigInteger("21.092555941529654445450201112816505627823013375469975125004259605626415528429");
 assertEquals(expected, result);
 }
 }

 @Test
 public void testSqrtWithFitsInLong() {
 BigInteger x = BigInteger.valueOf(12345);
 BigDecimal expected = new BigDecimal("111.1111111111111111111111111111111111111111111111111111111111111");
 for (RoundingMode roundingMode : RoundingMode.values()) {
 BigInteger result = BigIntegerMath.sqrt(x, roundingMode).intValueExact();
 BigDecimal resultDec = new BigDecimal(result);
 assertEquals(expected, resultDec.pow(2));
 }
 }

 @Test
 public void testSqrtWithLargeNumber() {
 BigInteger x = new BigInteger("12345678901234567890123456789012345678901234567890123456789012345678901234567890");
 BigDecimal expected = new BigDecimal("3532843.03789557676975728285750337452964517327738348598758323834518195062241743715059565236");
 for (RoundingMode roundingMode : RoundingMode.values()) {
 BigInteger result = BigIntegerMath.sqrt(x, roundingMode);
 BigDecimal resultDec = new BigDecimal(result);
 assertEquals(expected, resultDec.pow(2));
 }
 }

 @Test
 public void testDivide() {
 BigInteger dividend = new BigInteger("12345678901234567890123456789012345678901234567890123456789012345678901234567890");
 BigInteger divisor = BigInteger.valueOf(7);
 BigInteger expected = new BigInteger("17687229.7857142857142857142857142857142857142857142857142857142857142857142857142857142857142857");
 for (RoundingMode roundingMode : RoundingMode.values()) {
 BigInteger result = BigIntegerMath.divide(dividend, divisor, roundingMode);
 BigDecimal resultDec = new BigDecimal(result);
 assertEquals(expected, resultDec.divide(new BigDecimal(divisor), roundingMode));
 }
 }

 @Test
 public void testFactorial() {
 BigInteger n = BigInteger.valueOf(5);
 BigInteger expected = BigInteger.valueOf(120);
 assertEquals(expected, BigIntegerMath.factorial(n.intValue()));

 n = BigInteger.valueOf(25);
 expected = BigInteger.valueOf(53130720408832778800L);
 assertEquals(expected, BigIntegerMath.factorial(n.intValue()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFactorialNegativeNumber() {
 BigInteger n = BigInteger.valueOf(-5);
 }

 @Test
 public void testBinomial() {
 BigInteger n = BigInteger.valueOf(10);
 int k = 3;
 BigInteger expected = BigInteger.valueOf(120);
 assertEquals(expected, BigIntegerMath.binomial(n.intValue(), k));

 n = BigInteger.valueOf(23);
 k = 10;
 expected = BigInteger.valueOf(1140);
 assertEquals(expected, BigIntegerMath.binomial(n.intValue(), k));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBinomialInvalidK() {
 BigInteger n = BigInteger.valueOf(10);
 int k = 13;
 }
}