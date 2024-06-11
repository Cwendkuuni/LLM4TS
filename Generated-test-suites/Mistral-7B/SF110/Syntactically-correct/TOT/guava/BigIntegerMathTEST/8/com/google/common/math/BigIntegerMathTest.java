package com.google.common.math;

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

 x = BigInteger.valueOf(3);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));

 x = BigInteger.valueOf(-5);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test
 public void testLog2() {
 BigInteger x = BigInteger.ONE;
 int expectedLog2 = 0;
 int actualLog2 = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(expectedLog2, actualLog2);

 x = BigInteger.valueOf(4);
 expectedLog2 = 2;
 actualLog2 = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(expectedLog2, actualLog2);

 x = BigInteger.valueOf(5);
 expectedLog2 = 3;
 actualLog2 = BigIntegerMath.log2(x, RoundingMode.DOWN);
 assertEquals(expectedLog2, actualLog2);

 x = BigInteger.valueOf(6);
 expectedLog2 = 3;
 actualLog2 = BigIntegerMath.log2(x, RoundingMode.UP);
 assertEquals(expectedLog2, actualLog2);

 x = BigInteger.valueOf(7);
 expectedLog2 = 3;
 actualLog2 = BigIntegerMath.log2(x, RoundingMode.CEILING);
 assertEquals(expectedLog2, actualLog2);

 x = BigInteger.valueOf(8);
 expectedLog2 = 3;
 actualLog2 = BigIntegerMath.log2(x, RoundingMode.HALF_DOWN);
 assertEquals(expectedLog2, actualLog2);

 x = BigInteger.valueOf(13);
 expectedLog2 = 3;
 actualLog2 = BigIntegerMath.log2(x, RoundingMode.HALF_UP);
 assertEquals(expectedLog2, actualLog2);

 x = BigInteger.valueOf(14);
 expectedLog2 = 4;
 actualLog2 = BigIntegerMath.log2(x, RoundingMode.HALF_EVEN);
 assertEquals(expectedLog2, actualLog2);
 }

 @Test
 public void testLog10() {
 BigInteger x = BigInteger.ONE;
 int expectedLog10 = 0;
 int actualLog10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(expectedLog10, actualLog10);

 x = BigInteger.valueOf(10);
 expectedLog10 = 1;
 actualLog10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(expectedLog10, actualLog10);

 x = BigInteger.valueOf(100);
 expectedLog10 = 2;
 actualLog10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(expectedLog10, actualLog10);

 x = BigInteger.valueOf(1000);
 expectedLog10 = 3;
 actualLog10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(expectedLog10, actualLog10);

 x = BigInteger.valueOf(10000);
 expectedLog10 = 4;
 actualLog10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(expectedLog10, actualLog10);

 x = BigInteger.valueOf(100000);
 expectedLog10 = 5;
 actualLog10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(expectedLog10, actualLog10);

 x = BigInteger.valueOf(1000000);
 expectedLog10 = 6;
 actualLog10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(expectedLog10, actualLog10);

 x = BigInteger.valueOf(Integer.MAX_VALUE);
 expectedLog10 = 31;
 actualLog10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertEquals(expectedLog10, actualLog10);

 x = BigInteger.valueOf(Long.parseUnsignedLong("18446744073709551617") + 1);
 expectedLog10 = 32;
 actualLog10 = BigIntegerMath.log10(x, RoundingMode.DOWN);
 assertNotEquals(expectedLog10, actualLog10); // This test should fail because the number is too large for BigInteger
 }

 @Test
 public void testSqrt() {
 BigInteger x = BigInteger.ONE;
 BigInteger expectedSqrt = x;
 BigInteger actualSqrt;

 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(expectedSqrt, actualSqrt);

 x = x.shiftLeft(15);
 expectedSqrt = expectedSqrt.shiftLeft(7);
 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(expectedSqrt, actualSqrt);

 x = BigInteger.valueOf(2825);
 expectedSqrt = new BigInteger("5325");
 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(expectedSqrt, actualSqrt);

 x = BigInteger.valueOf(1225);
 expectedSqrt = new BigInteger("35");
 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.UP);
 assertEquals(expectedSqrt, actualSqrt);

 x = BigInteger.valueOf(1225);
 expectedSqrt = new BigInteger("35");
 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.CEILING);
 assertEquals(expectedSqrt, actualSqrt);

 x = BigInteger.valueOf(1449);
 expectedSqrt = new BigInteger("38");
 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.HALF_DOWN);
 assertEquals(expectedSqrt, actualSqrt);

 x = BigInteger.valueOf(1449);
 expectedSqrt = new BigInteger("38");
 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.HALF_UP);
 assertEquals(expectedSqrt, actualSqrt);

 x = BigInteger.valueOf(1449);
 expectedSqrt = new BigInteger("38");
 actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.HALF_EVEN);
 assertEquals(expectedSqrt, actualSqrt);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidRoundingMode() {
 BigInteger x = BigInteger.ONE;
 BigIntegerMath.log10(x, RoundingMode.AVERAGE);
 }

 @Test(expected = AssertionError.class)
 public void testNegativeNumber() {
 BigInteger x = BigInteger.valueOf(-5);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test(expected = AssertionError.class)
 public void testNegativeNumberInLog10() {
 BigInteger x = BigInteger.valueOf(-5);
 try {
 BigIntegerMath.log10(x, RoundingMode.DOWN);
 } catch (AssertionError e) {
 // Expected result
 }
 }
}