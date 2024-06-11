package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 BigInteger x = BigInteger.ONE;
 // positive numbers
 for (int i = 0; i < 30; i++) {
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = x.shiftLeft(1);
 }
 // negative number
 assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.negate()));
 // zero
 assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
 // odd numbers which are not powers of 2
 assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
 // non-positive numbers
 for (int i = 1; i >= -30; i--) {
 assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-(1 << i))));
 }
 }

 @Test
 public void testLog2() {
 BigInteger x = BigInteger.ONE;
 // positive powers of 2
 for (int i = 0; i < 30; i++) {
 assertEquals(Integer.bitCount(Integer.toBinaryString(i + 1).length()) - 1, BigIntegerMath.log2(x, RoundingMode.DOWN));
 x = x.shiftLeft(1);
 }
 // non-power of 2 numbers
 for (int i = 0; i < 30; i++) {
 int expected = (Integer.bitCount(Integer.toBinaryString(i + 1).length()) - 1) + 1;
 assertEquals(expected, BigIntegerMath.log2(BigInteger.valueOf(i + 1), RoundingMode.UP));
 x = BigInteger.valueOf(i + 1);
 }
 // negative number
 assertNull(BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.ANY_VALUE));
 // zero
 assertNull(BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.ANY_VALUE));
 }

 @Test
 public void testLog2WithDifferentRoundingModes() {
 BigInteger x = BigInteger.ONE;
 // positive powers of 2
 for (int i = 0; i < 30; i++) {
 // down rounding
 assertEquals(Integer.bitCount(Integer.toBinaryString(i + 1).length()) - 1, BigIntegerMath.log2(x, RoundingMode.DOWN));
 // floor rounding
 assertEquals(Integer.bitCount(Integer.toBinaryString(i + 1).length()) - 1, BigIntegerMath.log2(x, RoundingMode.FLOOR));
 // ceiling rounding
 assertEquals(Integer.bitCount(Integer.toBinaryString(i + 1).length()) - 1 + 1, BigIntegerMath.log2(x, RoundingMode.CEILING));
 // up rounding
 assertEquals(Integer.bitCount(Integer.toBinaryString(i + 1).length()), BigIntegerMath.log2(x, RoundingMode.UP));
 // half-down rounding
 int halfDownExpected = Integer.bitCount(Integer.toBinaryString(i + 1).length());
 if (halfDownExpected % 2 == 0) {
 halfDownExpected--;
 }
 assertEquals(halfDownExpected, BigIntegerMath.log2(x, RoundingMode.HALF_DOWN));
 // half-up rounding
 assertEquals(halfDownExpected + 1, BigIntegerMath.log2(x, RoundingMode.HALF_UP));
 // half-even rounding
 if (halfDownExpected % 2 == 0) {
 assertEquals(halfDownExpected, BigIntegerMath.log2(x, RoundingMode.HALF_EVEN));
 } else {
 assertEquals(halfDownExpected + 1, BigIntegerMath.log2(x, RoundingMode.HALF_EVEN));
 }
 x = x.shiftLeft(1);
 }
 }

 @Test
 public void testLog10() {
 BigInteger x = BigInteger.ONE;
 // numbers with whole log10 values
 for (int i = 0; i < 25; i++) {
 BigDecimal expected = BigDecimal.valueOf(Math.log10((double) (i + 1)));
 BigInteger result = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(expected.toBigIntegerExact(), result);
 x = x.multiply(BigInteger.TEN);
 }
 // numbers with non-whole log10 values
 for (int i = 0; i < 25; i++) {
 BigInteger xValue = BigInteger.valueOf(1 + (int) Math.pow(10, (double) (i / 3.0)));
 BigDecimal expected = BigDecimal.valueOf(Math.log10((double) (1 + (int) Math.pow(10, (double) (i / 3.0)))));
 BigInteger result = BigIntegerMath.log10(xValue, RoundingMode.HALF_UP);
 assertEquals(expected.toBigIntegerExact(), result);
 x = xValue;
 }
 // negative number
 assertNull(BigIntegerMath.log10(BigInteger.ONE.negate(), RoundingMode.ANY_VALUE));
 // zero
 assertNull(BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.ANY_VALUE));
 }

 @Test
 public void testSqrt() {
 BigInteger x = BigInteger.ONE;
 // positive numbers
 for (int i = 0; i < 30; i++) {
 BigDecimal expected = new BigDecimal(Math.sqrt((double) (i + 1)));
 BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(expected.toBigIntegerExact(), result);
 x = x.multiply(BigInteger.TEN);
 }
 // negative number
 assertNull(BigIntegerMath.sqrt(BigInteger.ONE.negate(), RoundingMode.ANY_VALUE));
 // zero
 assertNull(BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.ANY_VALUE));
 }

 @Test
 public void testFactorial() {
 BigInteger x = BigInteger.ONE;
 // numbers up to 12!
 for (int i = 0; i <= 12; i++) {
 BigInteger expected = BigInteger.valueOf(LongMath.factorial(i));
 BigInteger result = BigIntegerMath.factorial(i);
 assertEquals(expected, result);
 x = x.multiply(BigInteger.valueOf(i + 1));
 }
 // numbers greater than 12!
 for (int i = 13; i <= 30; i++) {
 BigInteger result = BigIntegerMath.factorial(i);
 BigInteger expected = listProduct(getFactorialsUpTo(i));
 assertEquals(expected, result);
 x = x.multiply(BigInteger.valueOf(i + 1));
 }
 }

 // Util method to generate a list of factorials up to a given number
 private List<BigInteger> getFactorialsUpTo(int n) {
 List<BigInteger> factorials = new ArrayList<>();
 for (int i = 0; i < n; i++) {
 factorials.add(BigInteger.valueOf(LongMath.factorial(i)));
 }
 return factorials;
 }

 @Test
 public void testBinomialCoefficient() {
 BigInteger n = BigInteger.valueOf(10);
 BigInteger k;
 // numbers up to nC5
 for (int i = 0; i <= 5; i++) {
 k = BigInteger.valueOf(i);
 BigInteger expected = BigInteger.valueOf(LongMath.binomial(10, i));
 BigInteger result = BigIntegerMath.binomial(n, k);
 assertEquals(expected, result);
 }
 // numbers greater than nC5
 for (int i = 6; i <= 10; i++) {
 k = BigInteger.valueOf(i);
 BigInteger result = BigIntegerMath.binomial(n, k);
 BigInteger expected = listProduct(getBinomialsUpTo(n, k));
 assertEquals(expected, result);
 n = n.add(BigInteger.ONE);
 }
 }

 // Util method to generate a list of binomial coefficients up to a given number
 private List<BigInteger> getBinomialsUpTo(BigInteger n, BigInteger k) {
 List<BigInteger> binomials = new ArrayList<>();
 for (int i = 0; i <= k.intValue(); i++) {
 binomials.add(BigInteger.valueOf(LongMath.binomial(n.intValue(), i)));
 }
 return binomials;
 }
}