package com.google.common.math;

import com.google.common.base.Preconditions;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class BigIntegerMathTest {

 @Test
 public void testIsPowerOfTwo() {
 BigInteger x = BigInteger.valueOf(1);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = BigInteger.valueOf(16);
 assertTrue(BigIntegerMath.isPowerOfTwo(x));

 x = BigInteger.valueOf(22);
 assertFalse(BigIntegerMath.isPowerOfTwo(x));
 }

 @Test
 public void testLog2() {
 BigInteger x = BigInteger.valueOf(1);
 assertEquals(0, BigIntegerMath.log2(x, RoundingMode.DOWN));

 x = BigInteger.valueOf(8);
 assertEquals(3, BigIntegerMath.log2(x, RoundingMode.DOWN));

 x = BigInteger.valueOf(13);
 assertEquals(3, BigIntegerMath.log2(x, RoundingMode.CEILING));

 x = BigInteger.valueOf(32767);
 assertEquals(15, BigIntegerMath.log2(x, RoundingMode.DOWN));
 }

 @Test(expected = AssertionError.class)
 public void testLog2InvalidMode() {
 BigInteger x = BigInteger.valueOf(1);
 BigIntegerMath.log2(x, RoundingMode.INVALID);
 }

 @Test
 public void testLog10() {
 BigInteger x = BigInteger.valueOf(1);
 assertEquals(0, BigIntegerMath.log10(x, RoundingMode.DOWN));

 x = BigInteger.valueOf(10);
 assertEquals(1, BigIntegerMath.log10(x, RoundingMode.DOWN));

 x = BigInteger.valueOf(100);
 assertEquals(2, BigIntegerMath.log10(x, RoundingMode.DOWN));

 x = BigInteger.valueOf(1000);
 assertEquals(3, BigIntegerMath.log10(x, RoundingMode.DOWN));
 }

 @Test
 public void testSqrt() {
 BigInteger x = BigInteger.valueOf(1);
 BigInteger expected = BigInteger.valueOf(1);
 BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(expected, result);

 x = BigInteger.valueOf(4);
 expected = BigInteger.valueOf(2);
 result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(expected, result);

 x = BigInteger.valueOf(25);
 expected = BigInteger.valueOf(5);
 result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(expected, result);

 x = BigInteger.valueOf(289);
 expected = BigInteger.valueOf(17);
 result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
 assertEquals(expected, result);
 }

 @Test(expected = AssertionError.class)
 public void testSqrtInvalidMode() {
 BigInteger x = BigInteger.valueOf(1);
 BigIntegerMath.sqrt(x, RoundingMode.INVALID);
 }

 @Test
 public void testFactorial() {
 BigInteger result = BigIntegerMath.factorial(0);
 assertEquals(BigInteger.ONE, result);

 result = BigIntegerMath.factorial(1);
 assertEquals(BigInteger.ONE, result);

 result = BigIntegerMath.factorial(2);
 assertEquals(BigInteger.valueOf(2), result);

 result = BigIntegerMath.factorial(5);
 assertEquals(BigInteger.valueOf(120), result);
 }

 @Test
 public void testBinomial() {
 BigInteger result = BigIntegerMath.binomial(5, 2);
 assertEquals(BigInteger.valueOf(10), result);

 result = BigIntegerMath.binomial(25, 5);
 assertEquals(BigInteger.valueOf(542432), result);
 }

 @Test
 public void testDivide() {
 BigInteger dividend = BigInteger.valueOf(100);
 BigInteger divisor = BigInteger.valueOf(5);
 RoundingMode roundingMode = RoundingMode.DOWN;

 BigInteger expectedResult = BigInteger.valueOf(20);
 BigInteger result = BigIntegerMath.divide(dividend, divisor, roundingMode);
 assertEquals(expectedResult, result);

 dividend = BigInteger.valueOf(1000000000000L);
 divisor = BigInteger.valueOf(1000);
 roundingMode = RoundingMode.CEILING;

 expectedResult = BigInteger.valueOf(10000000);
 result = BigIntegerMath.divide(dividend, divisor, roundingMode);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSqrtFloor() {
 BigInteger x = BigInteger.valueOf(1);
 BigInteger expected = BigInteger.valueOf(1);
 BigInteger result = BigIntegerMath.sqrtFloor(x);
 assertEquals(expected, result);

 x = BigInteger.valueOf(4);
 expected = BigInteger.valueOf(2);
 result = BigIntegerMath.sqrtFloor(x);
 assertEquals(expected, result);

 x = BigInteger.valueOf(25);
 expected = BigInteger.valueOf(5);
 result = BigIntegerMath.sqrtFloor(x);
 assertEquals(expected, result);
 }

 @Test
 public void testDivideWithBigDecimal() {
 BigInteger dividend = BigInteger.valueOf(1000000000000L);
 BigInteger divisor = BigInteger.valueOf(1000);
 RoundingMode roundingMode = RoundingMode.DOWN;

 BigDecimal dividendDec = new BigDecimal(dividend);
 BigDecimal divisorDec = new BigDecimal(divisor);
 BigDecimal expectedDec = divisorDec.pow(BigDecimal.ONE.negate()).multiply(dividendDec.log(BigDecimal.TEN));
 BigDecimal expected = expectedDec.round(BigDecimal.ROUND_DOWN).toBigIntegerExact();
 BigInteger result = BigIntegerMath.divide(dividend, divisor, roundingMode);
 assertEquals(expected, result);
 }

 @Test
 public void testFactorialsListProduct() {
 BigInteger[] factorials = new BigInteger[] {BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(6), BigInteger.valueOf(24)};
 ArrayList<BigInteger> list = new ArrayList<>();
 list.add(BigInteger.valueOf(1));
 list.add(BigInteger.valueOf(2));
 list.add(BigInteger.valueOf(6));
 list.add(BigInteger.valueOf(24));
 BigInteger result = BigIntegerMath.listProduct(list);
 assertEquals(BigInteger.valueOf(48), result);
 }

 @Test
 public void testBinomialListProduct() {
 BigInteger[] binomials = new BigInteger[] {BigInteger.ONE, BigInteger.valueOf(5), BigInteger.valueOf(11), BigInteger.valueOf(30), BigInteger.valueOf(120)};
 ArrayList<BigInteger> list = new ArrayList<>();
 list.add(BigInteger.ONE);
 list.add(BigInteger.valueOf(5));
 list.add(BigInteger.valueOf(11));
 list.add(BigInteger.valueOf(30));
 list.add(BigInteger.valueOf(120));
 BigInteger result = BigIntegerMath.listProduct(list);
 assertEquals(BigInteger.valueOf(362880), result);
 }
}