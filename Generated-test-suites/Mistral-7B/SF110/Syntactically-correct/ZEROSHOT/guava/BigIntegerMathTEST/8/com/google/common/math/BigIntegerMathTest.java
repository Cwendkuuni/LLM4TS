package com.google.common.math.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.common.math.BigIntegerMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

 @org.junit.Test
 public void testIsPowerOfTwo() {
 BigInteger x = BigInteger.valueOf(1);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(16);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(21);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(-1);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(Integer.MIN_VALUE);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @org.junit.Test
 public void testLog2() {
 BigInteger x = BigInteger.ONE;
 assertEquals(0, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
 x = BigInteger.valueOf(2);
 assertEquals(1, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
 x = BigInteger.valueOf(3);
 assertEquals(2, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
 x = BigInteger.valueOf(12);
 assertEquals(4, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));
 x = BigInteger.valueOf(13);
 assertEquals(5, BigIntegerMath.log2(x, RoundingMode.UP));
 x = BigInteger.valueOf(14);
 assertEquals(4, BigIntegerMath.log2(x, RoundingMode.DOWN));
 x = BigInteger.valueOf(15);
 assertEquals(4, BigIntegerMath.log2(x, RoundingMode.DOWN));
 x = BigInteger.valueOf(16);
 assertEquals(4, BigIntegerMath.log2(x, RoundingMode.DOWN));
 x = BigInteger.valueOf(17);
 assertEquals(5, BigIntegerMath.log2(x, RoundingMode.UP));
 x = BigInteger.valueOf(256);
 assertEquals(8, BigIntegerMath.log2(x, RoundingMode.DOWN));
 x = BigInteger.valueOf(257);
 assertEquals(9, BigIntegerMath.log2(x, RoundingMode.UP));
 x = BigInteger.valueOf(Integer.MIN_VALUE);
 assertEquals(-1, BigIntegerMath.log2(x, RoundingMode.DOWN));
 }

 @org.junit.Test(expected = AssertionError.class)
 public void testLog2WithInvalidRoundingMode() {
 BigInteger x = BigInteger.ONE;
 BigIntegerMath.log2(x, RoundingMode.MIDPOINT);
 }

 @org.junit.Test
 public void testLog10() {
 BigInteger x = BigInteger.ONE;
 assertEquals(0, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY), 0);
 x = BigInteger.valueOf(10);
 assertEquals(1, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY), 0);
 x = BigInteger.valueOf(100);
 assertEquals(2, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY), 0);
 x = BigInteger.valueOf(1000);
 assertEquals(3, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY), 0);
 x = BigInteger.valueOf(10000);
 assertEquals(4, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY), 0);
 x = BigInteger.valueOf(100000);
 assertEquals(5, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY), 0);
 x = BigInteger.valueOf(12345678901234567890L);
 assertEquals(18, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY), 0);
 x = BigInteger.valueOf(12345678901234567891L);
 assertEquals(19, BigIntegerMath.log10(x, RoundingMode.UP), 0);
 x = BigInteger.valueOf(12345678901234567892L);
 assertEquals(19, BigIntegerMath.log10(x, RoundingMode.DOWN), 0);
 x = BigInteger.valueOf(12345678901234567893L);
 assertEquals(19, BigIntegerMath.log10(x, RoundingMode.DOWN), 0);
 x = BigInteger.valueOf(12345678901234567894L);
 assertEquals(19, BigIntegerMath.log10(x, RoundingMode.UP), 0);
 x = BigInteger.valueOf(Integer.MIN_VALUE);
 assertEquals(Double.NEGATIVE_INFINITY, BigIntegerMath.log10(x, RoundingMode.DOWN), 0.0);
 }

 @org.junit.Test(expected = AssertionError.class)
 public void testLog10WithInvalidRoundingMode() {
 BigInteger x = BigInteger.ONE;
 BigIntegerMath.log10(x, RoundingMode.MIDPOINT);
 }

 @org.junit.Test
 public void testSqrt() {
 BigInteger x = BigInteger.ONE;
 assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
 x = BigInteger.valueOf(4);
 assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
 x = BigInteger.valueOf(16);
 assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY));
 x = BigInteger.valueOf(Integer.MAX_VALUE);
 assertEquals(BigInteger.valueOf(Long.MAX_VALUE), BigIntegerMath.sqrt(x, RoundingMode.UP));
 x = BigInteger.valueOf(Integer.MIN_VALUE);
 assertEquals(BigInteger.ZERO, BigIntegerMath.sqrt(x, RoundingMode.UP));
 x = BigInteger.valueOf(Long.MIN_VALUE);
 assertEquals(BigInteger.ZERO, BigIntegerMath.sqrt(x, RoundingMode.UP));
 }

 @org.junit.Test(expected = AssertionError.class)
 public void testSqrtWithInvalidRoundingMode() {
 BigInteger x = BigInteger.ONE;
 BigIntegerMath.sqrt(x, RoundingMode.MIDPOINT);
 }

 @org.junit.Test
 public void testDivide() {
 BigInteger dividend = BigInteger.valueOf(10);
 BigInteger divisor = BigInteger.valueOf(2);
 assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY));
 dividend = BigInteger.valueOf(10);
 divisor = BigInteger.valueOf(0);
 try {
 BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
 fail();
 } catch (ArithmeticException e) {
 // Expected
 }
 dividend = BigInteger.valueOf(Integer.MIN_VALUE);
 divisor = BigInteger.valueOf(-2);
 try {
 BigIntegerMath.divide(dividend, divisor, RoundingMode.UNNECESSARY);
 fail();
 } catch (ArithmeticException e) {
 // Expected
 }
 dividend = BigInteger.valueOf(Integer.MIN_VALUE);
 divisor = BigInteger.valueOf(2);
 assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 2), BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
 }

 @org.junit.Test(expected = AssertionError.class)
 public void testDivideWithInvalidRoundingMode() {
 BigInteger dividend = BigInteger.valueOf(10);
 BigInteger divisor = BigInteger.valueOf(2);
 BigIntegerMath.divide(dividend, divisor, RoundingMode.MIDPOINT);
 }

 @org.junit.Test
 public void testFactorial() {
 BigInteger n = BigInteger.ZERO;
 assertEquals(BigInteger.ONE, BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(0);
 assertEquals(BigInteger.ONE, BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(1);
 assertEquals(BigInteger.ONE, BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(2);
 assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(3);
 assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(4);
 assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(5);
 assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(6);
 assertEquals(BigInteger.valueOf(720), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(7);
 assertEquals(BigInteger.valueOf(5040), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(8);
 assertEquals(BigInteger.valueOf(40320), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(9);
 assertEquals(BigInteger.valueOf(362880), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(10);
 assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(11);
 assertEquals(BigInteger.valueOf(39916800), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(12);
 assertEquals(BigInteger.valueOf(479001600), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(13);
 assertEquals(BigInteger.valueOf(6227020800), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(14);
 assertEquals(BigInteger.valueOf(87178291200), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(15);
 assertEquals(BigInteger.valueOf(1307674368000), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(16);
 assertEquals(BigInteger.valueOf(20922789888000), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(17);
 assertEquals(BigInteger.valueOf(355687428096000), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(18);
 assertEquals(BigInteger.valueOf(6402373705323424000L), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(19);
 assertEquals(BigInteger.valueOf(121645100408832000000L), BigIntegerMath.factorial(n));
 n = BigInteger.valueOf(20);
 assertEquals(BigInteger.valueOf(2432902008176640000000L), BigIntegerMath.factorial(n));
 }

 @org.junit.Test
 public void testBinomial() {
 BigInteger n = BigInteger.valueOf(1);
 int k = 0;
 assertEquals(BigInteger.ONE, BigIntegerMath.binomial(n, k));
 k = 1;
 assertEquals(BigInteger.ONE, BigIntegerMath.binomial(n, k));
 n = BigInteger.valueOf(2);
 k = 0;
 assertEquals(BigInteger.ONE, BigIntegerMath.binomial(n, k));
 k = 1;
 assertEquals(BigInteger.ONE, BigIntegerMath.binomial(n, k));
 k = 2;
 assertEquals(BigInteger.ONE, BigIntegerMath.binomial(n, k));
 n = BigInteger.valueOf(3);
 k = 0;
 assertEquals(BigInteger.ONE, BigIntegerMath.binomial(n, k));
 k = 1;
 assertEquals(BigInteger.ONE, BigIntegerMath.binomial(n, k));
 k = 2;
 assertEquals(BigInteger.valueOf(3), BigIntegerMath.binomial(n, k));
 k = 3;
 assertEquals(BigInteger.valueOf(3), BigIntegerMath.binomial(n, k));
 n = BigInteger.valueOf(4);
 k = 0;
 assertEquals(BigInteger.ONE, BigIntegerMath.binomial(n, k));
 k = 1;
 assertEquals(BigInteger.valueOf(4), BigIntegerMath.binomial(n, k));
 k = 2;
 assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(n, k));
 k = 3;
 assertEquals(BigInteger.valueOf(4), BigIntegerMath.binomial(n, k));
 k = 4;
 assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(n, k));
 n = BigInteger.valueOf(5);
 k = 0;
 assertEquals(BigInteger.ONE, BigIntegerMath.binomial(n, k));
 k = 1;
 assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(n, k));
 k = 2;
 assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(n, k));
 k = 3;
 assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(n, k));
 k = 4;
 assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(n, k));
 k = 5;
 assertEquals(BigInteger.valueOf(5), BigIntegerMath.binomial(n, k));
 n = BigInteger.valueOf(10);
 k = 0;
 assertEquals(BigInteger.ONE, BigIntegerMath.binomial(n, k));
 k = 1;
 assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(n, k));
 k = 2;
 assertEquals(BigInteger.valueOf(45), BigIntegerMath.binomial(n, k));
 k = 3;
 assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(n, k));
 k = 4;
 assertEquals(BigInteger.valueOf(210), BigIntegerMath.binomial(n, k));
 k = 5;
 assertEquals(BigInteger.valueOf(252), BigIntegerMath.binomial(n, k));
 k = 6;
 assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(n, k));
 k = 7;
 assertEquals(BigInteger.valueOf(210), BigIntegerMath.binomial(n, k));
 k = 8;
 assertEquals(BigInteger.valueOf(270), BigIntegerMath.binomial(n, k));
 k = 9;
 assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(n, k));
 }

 @org.junit.Test
 public void testFitsInLong() {
 BigInteger x = BigInteger.ONE;
 assertTrue(BigIntegerMath.fitsInLong(x));
 x = BigInteger.valueOf(Long.MAX_VALUE);
 assertTrue(BigIntegerMath.fitsInLong(x));
 x = BigInteger.valueOf(Long.MAX_VALUE + 1);
 assertFalse(BigIntegerMath.fitsInLong(x));
 x = BigInteger.valueOf(Integer.MIN_VALUE);
 assertFalse(BigIntegerMath.fitsInLong(x));
 x = BigInteger.valueOf(Long.MIN_VALUE + 1);
 assertFalse(BigIntegerMath.fitsInLong(x));
 }
}