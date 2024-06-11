package com.google.common.math;

import com.google.common.base.Preconditions;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 BigInteger x = BigInteger.valueOf(1);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = BigInteger.valueOf(1 << 31);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = BigInteger.valueOf(2);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));

 x = BigInteger.valueOf(3);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));

 x = BigInteger.valueOf(4);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = BigInteger.valueOf(5);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test
 public void testLog2WithDifferentModes() {
 BigInteger x = BigInteger.valueOf(16);

 int log2Expected = 4;

 int log2Actual = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(log2Expected, log2Actual);

 log2Actual = BigIntegerMath.log2(x, RoundingMode.UP);
 assertEquals(log2Expected + 1, log2Actual);

 log2Actual = BigIntegerMath.log2(x, RoundingMode.CEILING);
 assertEquals(log2Expected + 1, log2Actual);

 log2Actual = BigIntegerMath.log2(x, RoundingMode.HALF_DOWN);
 assertEquals(log2Expected, log2Actual);

 log2Actual = BigIntegerMath.log2(x, RoundingMode.HALF_UP);
 assertEquals(log2Expected + 1, log2Actual);

 log2Actual = BigIntegerMath.log2(x, RoundingMode.HALF_EVEN);
 assertEquals(log2Expected, log2Actual);

 log2Actual = BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
 assertFalse(log2Actual > log2Expected);
 assertFalse(log2Actual < log2Expected);
 }

 @Test
 public void testLog2WithSmallNumbers() {
 BigInteger x = BigInteger.ONE;

 int log2Expected = 0;
 int log2Actual = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(log2Expected, log2Actual);

 x = BigInteger.valueOf(2);
 log2Expected = 1;
 log2Actual = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(log2Expected, log2Actual);

 x = BigInteger.valueOf(4);
 log2Expected = 2;
 log2Actual = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(log2Expected, log2Actual);
 }

 @Test(expected = AssertionError.class)
 public void testLog2WithInvalidRoundingMode() {
 BigInteger x = BigInteger.valueOf(16);
 BigIntegerMath.log2(x, RoundingMode.CHOP);
 }

 @Test
 public void testLog10WithDifferentModes() {
 BigInteger x = BigInteger.valueOf(1024);

 int log10Expected = 10;

 int log10Actual = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(log10Expected, log10Actual);

 log10Actual = BigIntegerMath.log10(x, RoundingMode.UP);
 assertEquals(log10Expected + 1, log10Actual);

 log10Actual = BigIntegerMath.log10(x, RoundingMode.CEILING);
 assertEquals(log10Expected + 1, log10Actual);

 log10Actual = BigIntegerMath.log10(x, RoundingMode.HALF_DOWN);
 assertEquals(log10Expected, log10Actual);

 log10Actual = BigIntegerMath.log10(x, RoundingMode.HALF_UP);
 assertEquals(log10Expected + 1, log10Actual);

 log10Actual = BigIntegerMath.log10(x, RoundingMode.HALF_EVEN);
 assertEquals(log10Expected, log10Actual);

 log10Actual = BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
 assertFalse(log10Actual > log10Expected);
 assertFalse(log10Actual < log10Expected);
 }

 @Test
 public void testSqrtWithDifferentModes() {
 BigInteger x = BigInteger.valueOf(16);
 BigInteger expectedSqrt = BigInteger.valueOf(4);

 BigInteger actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(expectedSqrt, actualSqrt);

 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertTrue(expectedSqrt.compareTo(actualSqrt) <= 0);

 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.CEILING);
 assertTrue(expectedSqrt.compareTo(actualSqrt) <= 0);

 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.HALF_DOWN);
 assertEquals(expectedSqrt, actualSqrt);

 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.HALF_UP);
 assertTrue(expectedSqrt.compareTo(actualSqrt) <= 0);

 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.HALF_EVEN);
 assertEquals(expectedSqrt, actualSqrt);

 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
 assertTrue(expectedSqrt.compareTo(actualSqrt) == 0);
 }

 @Test(expected = AssertionError.class)
 public void testSqrtWithNegativeNumber() {
 BigInteger x = BigInteger.valueOf(-16);
 BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 }

 @Test
 public void testFactorial() {
 BigInteger expected = BigInteger.valueOf(120);
 BigInteger actual = BigIntegerMath.factorial(6);
 assertEquals(expected, actual);
 }

 @Test
 public void testDivide() {
 BigInteger p = BigInteger.valueOf(21);
 BigInteger q = BigInteger.valueOf(7);
 BigInteger expected = BigInteger.valueOf(3);
 BigInteger actual = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
 assertEquals(expected, actual);
 }

 @Test
 public void testDivideWithRemainder() {
 BigInteger p = BigInteger.valueOf(13);
 BigInteger q = BigInteger.valueOf(4);
 BigInteger quotientExpected = BigInteger.valueOf(3);
 BigInteger remainderExpected = BigInteger.valueOf(1);
 BigInteger actualQuotient = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
 BigInteger actualRemainder = p.remainder(q);

 assertEquals(quotientExpected, actualQuotient);
 assertEquals(remainderExpected, actualRemainder);
 }

 @Test(expected = AssertionError.class)
 public void testDivideWithInvalidRoundingMode() {
 BigInteger p = BigInteger.valueOf(13);
 BigInteger q = BigInteger.valueOf(4);
 BigIntegerMath.divide(p, q, RoundingMode.CHOP);
 }

 @Test
 public void testListProduct() {
 ArrayList<BigInteger> nums = new ArrayList<>();
 nums.add(BigInteger.valueOf(2));
 nums.add(BigInteger.valueOf(3));
 nums.add(BigInteger.valueOf(5));

 BigInteger expected = BigInteger.valueOf(30);
 BigInteger actual = BigIntegerMath.listProduct(nums);
 assertEquals(expected, actual);
 }

 @Test
 public void testBinomial() {
 int n = 5;
 int k = 3;
 BigInteger expected = BigInteger.valueOf(10);
 BigInteger actual = BigIntegerMath.binomial(n, k);
 assertEquals(expected, actual);
 }

 @Test
 public void testBinomialWithLargeInputs() {
 int n = 25;
 int k = 10;
 BigInteger expected = BigInteger.valueOf(11129021621110955904466352841);
 BigInteger actual = BigIntegerMath.binomial(n, k);
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testBinomialWithInvalidInputs() {
 int n = 5;
 int k = 6;
 BigIntegerMath.binomial(n, k);
 }
}