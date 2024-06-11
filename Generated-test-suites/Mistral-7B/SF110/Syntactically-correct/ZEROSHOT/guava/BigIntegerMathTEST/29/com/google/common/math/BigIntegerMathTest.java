package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.*;
import com.google.common.annotations.*;
import java.math.*;
import java.util.*;

@GuavaResources(files = "BigIntegerMathTest.properties")
public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 BigInteger x = BigInteger.ONE;
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = x.shiftLeft(3);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = x.add(BigInteger.ONE);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test
 public void testLog2() {
 BigInteger x = BigInteger.ONE;
 assertEquals(0, BigIntegerMath.log2(x, RoundingMode.DOWN));

 x = x.shiftLeft(1);
 assertEquals(1, BigIntegerMath.log2(x, RoundingMode.DOWN));

 x = x.shiftLeft(2);
 assertEquals(2, BigIntegerMath.log2(x, RoundingMode.DOWN));

 x = x.add(BigInteger.ONE);
 assertEquals(3, BigIntegerMath.log2(x, RoundingMode.UP));

 x = new BigInteger("-128", 16);
 assertEquals(-7, BigIntegerMath.log2(x, RoundingMode.DOWN));

 x = new BigInteger("1024", 10);
 assertEquals(10, BigIntegerMath.log2(x, RoundingMode.UP));
 }

 @Test
 public void testLog2WithDifferentRoundingModes() {
 BigInteger x = BigInteger.valueOf(12345);

 int floorLog2 = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(11, floorLog2);

 int upLog2 = BigIntegerMath.log2(x, RoundingMode.UP);
 assertEquals(12, upLog2);

 int halfDownLog2 = BigIntegerMath.log2(x, RoundingMode.HALF_DOWN);
 assertEquals(11, halfDownLog2);

 int halfUpLog2 = BigIntegerMath.log2(x, RoundingMode.HALF_UP);
 assertEquals(12, halfUpLog2);

 int halfEvenLog2 = BigIntegerMath.log2(x, RoundingMode.HALF_EVEN);
 assertEquals(12, halfEvenLog2);
 }

 @Test(expected = AssertionError.class)
 public void testLog2WithInvalidRoundingMode() {
 BigInteger x = BigInteger.ONE;
 BigIntegerMath.log2(x, RoundingMode.INVALID);
 }

 @Test
 public void testLog10() {
 BigInteger x = BigInteger.ONE;
 assertEquals(0, BigIntegerMath.log10(x, RoundingMode.DOWN));

 x = x.shiftLeft(1);
 assertEquals(1, BigIntegerMath.log10(x, RoundingMode.DOWN));

 x = x.shiftLeft(2);
 assertEquals(2, BigIntegerMath.log10(x, RoundingMode.UP));

 x = new BigInteger("-128", 16);
 assertEquals(-6, BigIntegerMath.log10(x, RoundingMode.DOWN));

 x = new BigInteger("1024", 10);
 assertEquals(3, BigIntegerMath.log10(x, RoundingMode.UP));
 }

 @Test
 public void testSqrt() {
 BigInteger x = BigInteger.ONE;
 BigInteger sqrt = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(x, sqrt.pow(2));

 x = x.shiftLeft(3);
 sqrt = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertEquals(sqrt.pow(2), x);

 x = new BigInteger("-15", 16);
 sqrt = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(sqrt.negate().multiply(sqrt), x);
 }

 @Test(expected = AssertionError.class)
 public void testSqrtWithInvalidRoundingMode() {
 BigInteger x = BigInteger.ONE;
 BigIntegerMath.sqrt(x, RoundingMode.INVALID);
 }

 @Test
 public void testFactorial() {
 BigInteger x = BigInteger.valueOf(5);
 BigInteger expected = BigInteger.valueOf(120);
 assertEquals(expected, BigIntegerMath.factorial(x.intValue()));

 x = BigInteger.valueOf(10);
 expected = BigInteger.valueOf(3628800);
 assertEquals(expected, BigIntegerMath.factorial(x.intValue()));
 }

 @Test
 public void testBinomial() {
 BigInteger n = BigInteger.valueOf(12);
 BigInteger k = BigInteger.valueOf(3);
 BigInteger expected = BigInteger.valueOf(2211);
 assertEquals(expected, BigIntegerMath.binomial(n, k));

 n = new BigInteger("21", 16);
 k = BigInteger.valueOf(7);
 expected = new BigInteger("196390", 16);
 assertEquals(expected, BigIntegerMath.binomial(n, k));
 }

 @Test
 public void testDivide() {
 BigInteger p = new BigInteger("1234567890123456789012345678901234567890");
 BigInteger q = new BigInteger("100000000000000000000000000000001");
 BigInteger expected = new BigInteger("123456789", 10);
 assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.DOWN));

 p = p.add(BigInteger.ONE);
 expected = new BigInteger("1234567890123456789012345678901234567891");
 assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.UP));
 }

 @Test
 public void testDivideWithNegativeQuotient() {
 BigInteger p = new BigInteger("-1234567890123456789012345678901234567891");
 BigInteger q = new BigInteger("100000000000000000000000000000001");
 BigInteger expected = new BigInteger("-123456789", 10);
 assertEquals(expected, BigIntegerMath.divide(p, q, RoundingMode.DOWN));
 }
}