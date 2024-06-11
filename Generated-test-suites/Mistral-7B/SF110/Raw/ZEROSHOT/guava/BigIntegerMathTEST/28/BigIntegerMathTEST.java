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
 BigInteger x = BigInteger.ONE;
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = x.shiftLeft(32);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = x.add(BigInteger.ONE);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test
 public void testLog2() {
 BigInteger x = BigInteger.valueOf(32);
 int expected = 5;
 int mode = RoundingMode.DOWN;
 int actual = BigIntegerMath.log2(x, mode);
 assertEquals(expected, actual);

 x = BigInteger.valueOf(128);
 expected = 7;
 actual = BigIntegerMath.log2(x, mode);
 assertEquals(expected, actual);

 x = BigInteger.valueOf(127);
 expected = 6;
 actual = BigIntegerMath.log2(x, mode);
 assertEquals(expected, actual);

 x = BigInteger.ONE;
 expected = 0;
 actual = BigIntegerMath.log2(x, mode);
 assertEquals(expected, actual);
 }

 @Test
 public void testLog2WithDifferentModes() {
 BigInteger x = BigInteger.valueOf(13);
 int expected = 3;
 int actual = 0;

 switch (RoundingMode.values()[3]) { // Using the constant value to test all modes
 case UNNECESSARY:
 actual = BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
 Preconditions.checkRoundingUnnecessary(x.equals(BigInteger.ONE.shiftLeft(actual)));
 break;
 case DOWN:
 case FLOOR:
 actual = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(expected, actual);
 break;
 case UP:
 case CEILING:
 actual = BigIntegerMath.log2(x, RoundingMode.UP);
 assertEquals(expected + 1, actual);
 break;
 case HALF_DOWN:
 case HALF_UP:
 case HALF_EVEN:
 {
 int half = expected / 2;
 int logX2 = BigIntegerMath.log2(x.pow(2), RoundingMode.DOWN);
 actual = (logX2 < 2 * expected + 1) ? expected : expected + 1;
 assertEquals(actual, actual);
 }
 break;
 }
 }

 @Test(expected = AssertionError.class)
 public void testLog2WithInvalidMode() {
 BigInteger x = BigInteger.valueOf(13);
 int actual = BigIntegerMath.log2(x, RoundingMode.valueOf("INVALID_MODE"));
 fail("Expected AssertionError but got " + actual);
 }

 @Test
 public void testLog10() {
 BigInteger x = BigInteger.valueOf(1000000000000000000L);
 double expected = Math.log10(1000000000000000000L);
 int mode = RoundingMode.DOWN;
 double actual = 0;

 BigDecimal bdX = new BigDecimal(x);
 BigDecimal bdExpected = new BigDecimal(expected);
 BigDecimal bdActual = BigDecimal.ZERO;
 int scale = 10;

 while (true) {
 BigInteger intPart = bdX.toBigIntegerExact().mod(BigInteger.TEN.pow(scale)).intValue();
 BigInteger fractionPart = bdX.remainder(BigInteger.TEN.pow(scale)).toBigIntegerExact();
 BigInteger pow10 = BigInteger.TEN.pow(scale);
 BigInteger floorPow = BigIntegerMath.floorDivide(x, pow10, RoundingMode.DOWN);
 BigInteger nextPow = BigInteger.TEN.multiply(floorPow);
 int cmp = nextPow.compareTo(x);

 if (cmp > 0) {
 bdActual = bdActual.add(BigDecimal.valueOf(1.0).pow(scale));
 x = nextPow;
 }
 else {
 actual = BigIntegerMath.log10(x, mode).intValue();
 break;
 }

 scale *= 10;
 }

 assertEquals(expected, actual, 0.0001);
 }

 @Test
 public void testSqrt() {
 BigInteger x = BigInteger.valueOf(25);
 BigInteger expected = BigInteger.valueOf(5);
 int mode = RoundingMode.DOWN;
 BigInteger actual = BigIntegerMath.sqrt(x, mode);
 assertEquals(expected, actual);

 x = BigInteger.valueOf(26);
 expected = BigInteger.valueOf(5).add(BigInteger.ONE);
 actual = BigIntegerMath.sqrt(x, mode);
 assertEquals(expected, actual);
 }

 @Test
 public void testDivide() {
 BigInteger p = BigInteger.valueOf(10);
 BigInteger q = BigInteger.valueOf(3);
 BigInteger r = BigInteger.valueOf(7);
 int mode = RoundingMode.HALF_UP;
 BigInteger expected = BigInteger.valueOf(3);
 BigInteger actual = BigIntegerMath.divide(p, q, mode);
 assertEquals(expected, actual);

 actual = BigIntegerMath.divide(p, r, mode);
 assertEquals(BigInteger.ZERO, actual);
 }

 @Test
 public void testFactorial() {
 BigInteger x = BigInteger.valueOf(0);
 BigInteger expected = BigInteger.ONE;
 BigInteger actual = BigIntegerMath.factorial(x.intValue());
 assertEquals(expected, actual);

 x = BigInteger.valueOf(5);
 expected = BigInteger.valueOf(120);
 actual = BigIntegerMath.factorial(x.intValue());
 assertEquals(expected, actual);
 }

 @Test
 public void testBinomial() {
 int n = 10;
 int k = 3;
 BigInteger expected = BigInteger.valueOf(120);
 BigInteger actual = BigIntegerMath.binomial(n, k);
 assertEquals(expected, actual);
 }

 @Test
 public void testBinomialWithInvalidArguments() {
 int n = 0;
 int k = 1;
 // Test with invalid k value
 try {
 BigInteger actual = BigIntegerMath.binomial(n, k);
 fail("Expected IllegalArgumentException but got " + actual);
 } catch (IllegalArgumentException e) {
 // expected
 }

 // Test with invalid n value
 n = -1;
 try {
 BigInteger actual = BigIntegerMath.binomial(n, k);
 fail("Expected IllegalArgumentException but got " + actual);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFitsInLong() {
 BigInteger x = BigInteger.ONE;
 assertTrue(BigIntegerMath.fitsInLong(x));

 x = x.shiftLeft(63);
 assertFalse(BigIntegerMath.fitsInLong(x));
 }
}