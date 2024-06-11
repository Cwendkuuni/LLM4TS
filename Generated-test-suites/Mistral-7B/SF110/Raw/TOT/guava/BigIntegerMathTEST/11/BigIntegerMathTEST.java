package com.google.common.math;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 BigInteger x = BigInteger.ONE;
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = x.shiftLeft(10);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = BigInteger.valueOf(3);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));

 x = BigInteger.valueOf(5);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test
 public void testLog2() {
 BigInteger x = BigInteger.ONE;
 assertEquals(0, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));

 x = x.shiftLeft(1);
 assertEquals(1, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));

 x = BigInteger.valueOf(3);
 assertEquals(-1, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));

 x = x.shiftLeft(3);
 assertEquals(3, BigIntegerMath.log2(x, RoundingMode.UNNECESSARY));

 x = BigInteger.valueOf(9);
 assertEquals(3, BigIntegerMath.log2(x, RoundingMode.DOWN));

 x = BigInteger.valueOf(11);
 assertEquals(-1, BigIntegerMath.log2(x, RoundingMode.DOWN));

 x = BigInteger.valueOf(13);
 assertEquals(-1, BigIntegerMath.log2(x, RoundingMode.DOWN));

 x = BigInteger.valueOf(16);
 assertEquals(4, BigIntegerMath.log2(x, RoundingMode.UP));

 x = BigInteger.valueOf(17);
 assertEquals(5, BigIntegerMath.log2(x, RoundingMode.UP));

 x = BigInteger.valueOf(18);
 assertEquals(5, BigIntegerMath.log2(x, RoundingMode.CEILING));

 x = BigInteger.valueOf(20);
 assertEquals(5, BigIntegerMath.log2(x, RoundingMode.CEILING));

 x = BigInteger.valueOf(21);
 assertEquals(6, BigIntegerMath.log2(x, RoundingMode.CEILING));
 }

 @Test
 public void testLog10() {
 BigInteger x = BigInteger.TEN;
 assertEquals(1, BigIntegerMath.log10(x, RoundingMode.UNNECESSARY));

 x = x.shiftLeft(1);
 assertEquals(1.0d + 0.1d, BigIntegerMath.log10(x, RoundingMode.DOWN), 0.00001);

 x = x.shiftLeft(2);
 assertEquals(1.0d + 0.2d, BigIntegerMath.log10(x, RoundingMode.DOWN), 0.00001);

 x = BigInteger.valueOf(12345);
 assertEquals(3.541020408002516d, BigIntegerMath.log10(x, RoundingMode.DOWN), 0.00001);

 x = BigInteger.valueOf(12345678901234567890L);
 assertEquals(19.231353464060117d, BigIntegerMath.log10(x, RoundingMode.DOWN), 0.00001);
 }

 @Test
 public void testSqrt() {
 BigInteger x = BigInteger.TEN;
 BigInteger expected = BigInteger.valueOf(3.16227766016837952787d.doubleValue().longValue());
 assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));

 x = x.shiftLeft(1);
 expected = BigInteger.valueOf(10.61542528302222363659d.doubleValue().longValue());
 assertEquals(expected, BigIntegerMath.sqrt(x, RoundingMode.DOWN));

 x = BigInteger.valueOf(12345);
 BigInteger actual = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertTrue(x.compareTo(actual.multiply(actual)) <= 0);

 x = BigInteger.valueOf(12345678901234567890L);
 actual = BigIntegerMath.sqrt(x, RoundingMode.CEILING);
 assertTrue(x.compareTo(actual.multiply(actual)) <= 0);
 }

 @Test
 public void testDivide() {
 BigInteger p = BigInteger.valueOf(100L);
 BigInteger q = BigInteger.valueOf(15L);
 BigInteger expected = BigInteger.valueOf(6L);
 assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.DOWN));

 q = BigInteger.valueOf(0);
 try {
 BigIntegerMath.divide(p, q, RoundingMode.DOWN);
 fail();
 } catch (ArithmeticException e) {
 // expected
 }

 q = BigInteger.valueOf(1);
 expected = BigInteger.valueOf(100L);
 assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.UP));

 q = BigInteger.valueOf(1).negate();
 expected = BigInteger.valueOf(-100L);
 assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.DOWN));
 }

 @Test
 public void testFactorial() {
 BigInteger result;
 BigInteger expected = BigInteger.ONE;

 for (int i = 0; i < 10; ++i) {
 result = BigIntegerMath.factorial(i);
 assertEquals(expected, result);
 expected = expected.multiply(BigInteger.valueOf(i + 1));
 }

 result = BigIntegerMath.factorial(100);
 assertEquals(expected, result);
 }
}