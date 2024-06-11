package com.google.common.math;

import static org.junit.Assert.*;

import com.google.common.base.*;
import java.math.*;
import java.util.*;
import org.junit.*;

public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 BigInteger x = BigInteger.valueOf(1);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(2);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(3);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(5);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(8);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(16);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(32768);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(32769);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(Integer.MAX_VALUE);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(Integer.MAX_VALUE << 1);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(Integer.MAX_VALUE << 2);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 x = BigInteger.valueOf(Integer.MAX_VALUE << 31);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test
 public void testLog2() {
 int expected[] = {3, 3, 2, 1, 0, 31, 30, 29, 28, 27};
 RoundingMode mode[] = {RoundingMode.UNNECESSARY, RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN};
 BigInteger x;
 int result;
 for (int i = 0; i < mode.length; ++i) {
 x = BigInteger.valueOf(1L << expected[i]);
 result = BigIntegerMath.log2(x, mode[i]);
 assertEquals(expected[i], result);
 }

 x = BigInteger.ONE;
 result = BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
 assertEquals(0, result);

 x = BigInteger.valueOf(Integer.MIN_VALUE);
 result = BigIntegerMath.log2(x, RoundingMode.UNNECESSARY);
 assertNegative(result);
 }

 @Test
 public void testLog10() {
 double expected[] = {-1.0, 0.0, 1.0, 2.0, 3.0, Math.log10(125.0), Math.log10(1024.0), Math.log10(16384.0), Math.log10(Math.pow(10, 100.0))};
 RoundingMode mode[] = {RoundingMode.UNNECESSARY, RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN};
 BigInteger x;
 BigInteger result;
 for (int i = 0; i < mode.length; ++i) {
 x = BigInteger.valueOf((long)Math.pow(10, expected[i]));
 result = BigIntegerMath.log10(x, mode[i]);
 assertEquals(expected[i], result.doubleValue(), 0.000001);
 }

 x = BigInteger.ONE;
 result = BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
 assertEquals(0.0, result.doubleValue(), 0.000001);

 x = BigInteger.ZERO;
 result = BigIntegerMath.log10(x, RoundingMode.UNNECESSARY);
 assertNull(result);

 x = BigInteger.valueOf(1234567890123456789L);
 result = BigIntegerMath.log10(x, RoundingMode.HALF_DOWN);
 assertGreaterThan(result.intValue(), Integer.MAX_VALUE);
 }

 @Test
 public void testSqrt() {
 double expected[] = {1.0, 2.0, 3.0, 4.0, 5.0, Math.sqrt(25.0), Math.sqrt(121.0), Math.sqrt(169.0), Math.sqrt(Math.pow(10, 24.0))};
 BigInteger x;
 BigInteger result;
 for (int i = 0; i < expected.length; ++i) {
 x = BigInteger.valueOf((long)Math.pow(10, Math.log10(expected[i])))
.shiftRight(BigIntegerMath.LN_10 * Math.floor(Math.log10(expected[i])));x = x.shiftLeft(1);
 result = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
 assertEquals(expected[i], result.doubleValue(), 0.000001);
 }

 x = BigInteger.ONE;
 result = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
 assertEquals(1.0, result.doubleValue(), 0.000001);

 x = BigInteger.TEN.pow(2);
 result = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
 assertEquals(Math.sqrt(2.0), result.doubleValue(), 0.000001);

 x = BigInteger.ONE.negate();
 result = BigIntegerMath.sqrt(x, RoundingMode.UNNECESSARY);
 assertNull(result);
 }

 @Test
 public void testDivide() {
 BigInteger p = BigInteger.valueOf(1234567890123456789L);
 BigInteger q = BigInteger.valueOf(1111111111111111111L);
 BigInteger r;
 RoundingMode mode[] = {RoundingMode.UNNECESSARY, RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN};
 for (RoundingMode m : mode) {
 r = BigIntegerMath.divide(p, q, m);
 System.out.println("p / q = " + p + " / " + q + " = " + r);
 }
 }

 @Test
 public void testFactorial() {
 BigInteger expected[] = {BigInteger.ONE, BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(6), BigInteger.valueOf(24), BigInteger.valueOf(120), BigInteger.valueOf(511_200), BigInteger.valueOf(1307674368000), BigInteger.valueOf(Math.pow(10, 2000))};
 BigInteger x = BigInteger.ZERO;
 BigInteger result;
 for (int i = 0; i < expected.length; ++i) {
 x = BigIntegerMath.factorial(i);
 assertEquals(expected[i], x);
 }

 x = BigInteger.valueOf(12345);
 result = BigIntegerMath.factorial(x.intValue());
 assertEquals(BigInteger.valueOf(fact(12345)), result);
 }

 private static long fact(int n) {
 long result = 1;
 for (int i = 2; i <= n; ++i) {
 result *= i;
 }
 return result;
 }
}