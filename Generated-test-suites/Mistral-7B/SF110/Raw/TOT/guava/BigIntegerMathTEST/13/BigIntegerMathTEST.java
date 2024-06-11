package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.math.BigIntegerMath;
import com.google.common.math.MathPreconditions;

public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 // Test positive cases
 BigInteger x1 = BigInteger.ONE;
 assertTrue(BigIntegerMath.isPowerOfTwo(x1));
 BigInteger x2 = BigInteger.valueOf(16);
 assertTrue(BigIntegerMath.isPowerOfTwo(x2));

 // Test negative cases
 BigInteger x3 = BigInteger.ZERO;
 assertFalse(BigIntegerMath.isPowerOfTwo(x3));
 BigInteger x4 = BigInteger.valueOf(-1);
 assertFalse(BigIntegerMath.isPowerOfTwo(x4));
 BigInteger x5 = BigInteger.valueOf(3);
 assertFalse(BigIntegerMath.isPowerOfTwo(x5));
 }

 @Test
 public void testLog2() {
 // Test positive cases
 BigInteger x1 = BigInteger.ONE;
 int expectedResult1 = 0;
 int roundingMode1 = RoundingMode.DOWN;
 int result1 = BigIntegerMath.log2(x1, roundingMode1);
 assertEquals(expectedResult1, result1);
 BigInteger x2 = BigInteger.valueOf(8);
 int expectedResult2 = 3;
 int roundingMode2 = RoundingMode.CEILING;
 int result2 = BigIntegerMath.log2(x2, roundingMode2);
 assertEquals(expectedResult2, result2);

 // Test negative cases
 BigInteger x3 = BigInteger.valueOf(-1);
 try {
 BigIntegerMath.log2(x3, RoundingMode.DOWN);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 BigInteger x4 = BigInteger.valueOf(0);
 int expectedResult4 = 0;
 int roundingMode4 = RoundingMode.DOWN;
 int result4 = BigIntegerMath.log2(x4, roundingMode4);
 assertEquals(expectedResult4, result4);
 BigInteger x5 = BigInteger.valueOf(5);
 int expectedResult5 = -1;
 int roundingMode5 = RoundingMode.FLOOR;
 int result5 = BigIntegerMath.log2(x5, roundingMode5);
 assertEquals(expectedResult5, result5);
 }

 @Test
 public void testLog10() {
 // Test positive cases
 BigInteger x1 = BigInteger.valueOf(10);
 int expectedResult1 = 1;
 int roundingMode1 = RoundingMode.DOWN;
 int result1 = BigIntegerMath.log10(x1, roundingMode1);
 assertEquals(expectedResult1, result1);
 BigInteger x2 = BigInteger.valueOf(100);
 int expectedResult2 = 2;
 int roundingMode2 = RoundingMode.CEILING;
 int result2 = BigIntegerMath.log10(x2, roundingMode2);
 assertEquals(expectedResult2, result2);

 // Test negative cases
 BigInteger x3 = BigInteger.ZERO;
 try {
 BigIntegerMath.log10(x3, RoundingMode.DOWN);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 BigInteger x4 = BigInteger.valueOf(5);
 int expectedResult4 = -1;
 int roundingMode4 = RoundingMode.FLOOR;
 int result4 = BigIntegerMath.log10(x4, roundingMode4);
 assertEquals(expectedResult4, result4);
 }

 @Test
 public void testSqrt() {
 // Test positive cases
 BigInteger x1 = BigInteger.ONE;
 BigInteger expectedResult1 = BigInteger.ONE;
 RoundingMode roundingMode1 = RoundingMode.DOWN;
 BigInteger result1 = BigIntegerMath.sqrt(x1, roundingMode1);
 assertEquals(expectedResult1, result1);
 BigInteger x2 = BigInteger.valueOf(16);
 BigInteger expectedResult2 = BigInteger.valueOf(4);
 RoundingMode roundingMode2 = RoundingMode.CEILING;
 BigInteger result2 = BigIntegerMath.sqrt(x2, roundingMode2);
 assertEquals(expectedResult2, result2);

 // Test negative cases
 BigInteger x3 = BigInteger.valueOf(-1);
 try {
 BigIntegerMath.sqrt(x3, RoundingMode.DOWN);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 BigInteger x4 = BigInteger.valueOf(121);
 BigInteger expectedResult4 = BigInteger.valueOf(11);
 RoundingMode roundingMode4 = RoundingMode.HALF_UP;
 BigInteger result4 = BigIntegerMath.sqrt(x4, roundingMode4);
 assertEquals(expectedResult4, result4);
 }

 @Test
 public void testFactorial() {
 // Test positive cases
 int n1 = 0;
 BigInteger expectedResult1 = BigInteger.ONE;
 BigInteger result1 = BigIntegerMath.factorial(n1);
 assertEquals(expectedResult1, result1);
 int n2 = 5;
 BigInteger expectedResult2 = BigInteger.valueOf(120);
 BigInteger result2 = BigIntegerMath.factorial(n2);
 assertEquals(expectedResult2, result2);

 // Test negative cases
 int n3 = -1;
 try {
 BigIntegerMath.factorial(n3);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 int n4 = 13;
 BigInteger expectedResult4 = BigInteger.valueOf(6402800);
 BigInteger result4 = BigIntegerMath.factorial(n4);
 assertEquals(expectedResult4, result4);
 }
}