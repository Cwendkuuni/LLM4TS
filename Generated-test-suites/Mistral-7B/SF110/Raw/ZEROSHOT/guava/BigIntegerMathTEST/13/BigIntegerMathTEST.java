package com.google.common.math;

import com.google.common.base.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 BigInteger x = BigInteger.ONE;
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = x.shiftLeft(32);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = x.add(BigInteger.ONE);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));

 x = x.shiftRight(1);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test
 public void testLog2() {
 BigInteger x = BigInteger.ONE;
 int log2 = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(0, log2);

 x = x.shiftLeft(3);
 log2 = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(3, log2);

 x = x.add(BigInteger.ONE);
 log2 = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(3, log2);

 x = BigInteger.TEN.pow(3);
 log2 = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(3, log2);

 x = BigInteger.valueOf(128);
 log2 = BigIntegerMath.log2(x, RoundingMode.UP);
 assertEquals(7, log2.intValue());

 x = BigInteger.valueOf(129);
 log2 = BigIntegerMath.log2(x, RoundingMode.UP);
 assertEquals(8, log2.intValue());

 x = BigInteger.valueOf(-128);
 log2 = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(-7, log2.intValue());

 x = BigInteger.valueOf(-129);
 log2 = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(-8, log2.intValue());
 }

 @Test(expected = AssertionError.class)
 public void testLog2InvalidMode() {
 BigInteger x = BigInteger.ONE;
 int log2 = BigIntegerMath.log2(x, RoundingMode.INCOMPATIBLE);
 }

 @Test
 public void testLog10() {
 BigInteger x = BigInteger.ONE;
 int log10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(0, log10);

 x = x.shiftLeft(3);
 log10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(3, log10);

 x = x.add(BigInteger.ONE);
 log10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(3, log10);

 x = BigInteger.TEN.pow(3);
 log10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(3, log10);

 x = BigInteger.valueOf(1000);
 log10 = BigIntegerMath.log10(x, RoundingMode.UP);
 assertEquals(3, log10.intValue());

 x = BigInteger.valueOf(1001);
 log10 = BigIntegerMath.log10(x, RoundingMode.UP);
 assertEquals(3, log10.intValue());

 x = BigInteger.valueOf(-1000);
 log10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(-3, log10.intValue());

 x = BigInteger.valueOf(-1001);
 log10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(-3, log10.intValue());
 }

 @Test(expected = AssertionError.class)
 public void testLog10InvalidMode() {
 BigInteger x = BigInteger.ONE;
 int log10 = BigIntegerMath.log10(x, RoundingMode.INCOMPATIBLE);
 }

 @Test
 public void testSqrt() {
 BigInteger x = BigInteger.valueOf(1);
 BigInteger y = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(BigInteger.valueOf(1), y);

 x = BigInteger.valueOf(2);
 y = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(BigInteger.valueOf(1), y.shiftRight(1));

 x = BigInteger.valueOf(3);
 y = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(BigInteger.valueOf(1).add(BigInteger.valueOf(1).shiftRight(1)), y);

 x = BigInteger.valueOf(9);
 y = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertEquals(BigInteger.valueOf(3), y);

 x = BigInteger.valueOf(16);
 y = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertEquals(BigInteger.valueOf(4), y);

 x = BigInteger.valueOf(25);
 y = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertEquals(BigInteger.valueOf(5), y);

 x = BigInteger.valueOf(26);
 y = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertEquals(BigInteger.valueOf(5), y);

 x = BigInteger.valueOf(27);
 y = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertEquals(BigInteger.valueOf(5).add(BigInteger.ONE), y);
 }

 @Test(expected = AssertionError.class)
 public void testSqrtInvalidMode() {
 BigInteger x = BigInteger.ONE;
 BigInteger y = BigIntegerMath.sqrt(x, RoundingMode.INCOMPATIBLE);
 }

 @Test
 public void testFactorial() {
 BigInteger x = BigInteger.ONE;
 for (int i = 0; i < 15; ++i) {
 x = BigIntegerMath.factorial(i + 1);
 System.out.println(i + "! = " + x);
 }
 }
}