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
 BigInteger testCases[] = {BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(4), BigInteger.valueOf(16), BigInteger.valueOf(256), BigInteger.valueOf(512), BigInteger.valueOf(1024), BigInteger.valueOf(16777216), BigInteger.valueOf(67108864), BigInteger.valueOf(1L << 64), BigInteger.TEN.pow(2), BigInteger.TEN.pow(3), BigInteger.TEN.pow(4), BigInteger.TEN.pow(5), BigInteger.TEN.pow(6), BigInteger.TEN.pow(7), BigInteger.TEN.pow(8), BigInteger.TEN.pow(9), BigInteger.TEN.pow(10)};
 for (BigInteger testCase : testCases) {
 assertTrue(BigIntegerMath.isPowerOfTwo(testCase));
 }
 BigInteger negativeTestCases[] = {BigInteger.valueOf(-1), BigInteger.valueOf(-2), BigInteger.valueOf(-4), BigInteger.valueOf(-16), BigInteger.valueOf(-256), BigInteger.valueOf(-512), BigInteger.valueOf(-1024), BigInteger.valueOf(-16777216), BigInteger.valueOf(-67108864), BigInteger.valueOf(-1L << 64), BigInteger.valueOf(0), BigInteger.valueOf(1), BigInteger.valueOf(3), BigInteger.valueOf(5), BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9), BigInteger.valueOf(11), BigInteger.valueOf(13)};
 for (BigInteger negativeTestCase : negativeTestCases) {
 assertFalse(BigIntegerMath.isPowerOfTwo(negativeTestCase));
 }
 }

 @Test
 public void testLog2() {
 BigInteger testCases[] = {BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(4), BigInteger.valueOf(16), BigInteger.valueOf(256), BigInteger.valueOf(1024), BigInteger.valueOf(16777216), BigInteger.valueOf(67108864), BigInteger.valueOf(1L << 64), BigInteger.TEN.pow(2), BigInteger.TEN.pow(3), BigInteger.TEN.pow(4), BigInteger.TEN.pow(5), BigInteger.TEN.pow(6), BigInteger.TEN.pow(7), BigInteger.TEN.pow(8), BigInteger.TEN.pow(9), BigInteger.TEN.pow(10), BigInteger.valueOf(1000000000), BigInteger.valueOf(1000000001)};
 for (BigInteger testCase : testCases) {
 for (RoundingMode roundingMode : RoundingMode.values()) {
 int expectedLog2 = (int)Math.log(testCase.doubleValue()) / Math.log(2.0);
 int actualLog2 = BigIntegerMath.log2(testCase, roundingMode);
 assertEquals(expectedLog2, actualLog2);
 }
 }
 BigInteger negativeTestCases[] = {BigInteger.valueOf(-1), BigInteger.valueOf(-2), BigInteger.valueOf(-4), BigInteger.valueOf(-16), BigInteger.valueOf(-256), BigInteger.valueOf(-512), BigInteger.valueOf(-1024), BigInteger.valueOf(-16777216), BigInteger.valueOf(-67108864), BigInteger.valueOf(-1L << 64), BigInteger.valueOf(0), BigInteger.valueOf(1), BigInteger.valueOf(3), BigInteger.valueOf(5), BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9), BigInteger.valueOf(11), BigInteger.valueOf(13)};
 for (BigInteger negativeTestCase : negativeTestCases) {
 for (RoundingMode roundingMode : RoundingMode.values()) {
 try {
 BigIntegerMath.log2(negativeTestCase, roundingMode);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }
 }
 }

 @Test
 public void testLog10() {
 BigInteger testCases[] = {BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(4), BigInteger.valueOf(16), BigInteger.valueOf(256), BigInteger.valueOf(1024), BigInteger.valueOf(16777216), BigInteger.valueOf(67108864), BigInteger.valueOf(1L << 64), BigInteger.TEN.pow(2), BigInteger.TEN.pow(3), BigInteger.TEN.pow(4), BigInteger.TEN.pow(5), BigInteger.TEN.pow(6), BigInteger.TEN.pow(7), BigInteger.TEN.pow(8), BigInteger.TEN.pow(9), BigInteger.TEN.pow(10), BigInteger.valueOf(1000000000), BigInteger.valueOf(1000000001)};
 for (BigInteger testCase : testCases) {
 for (RoundingMode roundingMode : RoundingMode.values()) {
 double expectedLog10 = Math.log10(testCase.doubleValue());
 BigInteger actualLog10 = BigIntegerMath.log10(testCase, roundingMode);
 assertEquals(Long.toString(Math.round(expectedLog10 * Math.pow(10, 3))).substring(1), actualLog10.toString());
 }
 }
 BigInteger negativeTestCases[] = {BigInteger.valueOf(-1), BigInteger.valueOf(-2), BigInteger.valueOf(-4), BigInteger.valueOf(-16), BigInteger.valueOf(-256), BigInteger.valueOf(-512), BigInteger.valueOf(-1024), BigInteger.valueOf(-16777216), BigInteger.valueOf(-67108864), BigInteger.valueOf(-1L << 64), BigInteger.valueOf(0), BigInteger.valueOf(1), BigInteger.valueOf(3), BigInteger.valueOf(5), BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9), BigInteger.valueOf(11), BigInteger.valueOf(13)};
 for (BigInteger negativeTestCase : negativeTestCases) {
 for (RoundingMode roundingMode : RoundingMode.values()) {
 try {
 BigIntegerMath.log10(negativeTestCase, roundingMode);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }
 }
 }

 @Test
 public void testSqrt() {
 BigInteger testCases[] = {BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(4), BigInteger.valueOf(16), BigInteger.valueOf(256), BigInteger.valueOf(1024), BigInteger.valueOf(16777216), BigInteger.valueOf(67108864), BigInteger.valueOf(1L << 64), BigInteger.TEN.pow(2), BigInteger.TEN.pow(3), BigInteger.TEN.pow(4), BigInteger.TEN.pow(5), BigInteger.TEN.pow(6), BigInteger.TEN.pow(7), BigInteger.TEN.pow(8), BigInteger.TEN.pow(9), BigInteger.TEN.pow(10), BigInteger.valueOf(1000000000), BigInteger.valueOf(1000000001)};
 for (BigInteger testCase : testCases) {
 for (RoundingMode roundingMode : RoundingMode.values()) {
 BigInteger expectedSqrt = BigInteger.valueOf((int)Math.sqrt(testCase.doubleValue()));
 BigInteger actualSqrt = BigIntegerMath.sqrt(testCase, roundingMode);
 assertEquals(expectedSqrt, actualSqrt);
 }
 }
 BigInteger negativeTestCases[] = {BigInteger.valueOf(-1), BigInteger.valueOf(-2), BigInteger.valueOf(-4), BigInteger.valueOf(-16), BigInteger.valueOf(-256), BigInteger.valueOf(-512), BigInteger.valueOf(-1024), BigInteger.valueOf(-16777216), BigInteger.valueOf(-67108864), BigInteger.valueOf(-1L << 64), BigInteger.valueOf(0), BigInteger.valueOf(1), BigInteger.valueOf(3), BigInteger.valueOf(5), BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9), BigInteger.valueOf(11), BigInteger.valueOf(13)};
 for (BigInteger negativeTestCase : negativeTestCases) {
 for (RoundingMode roundingMode : RoundingMode.values()) {
 try {
 BigIntegerMath.sqrt(negativeTestCase, roundingMode);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }
 }
 }

 @Test
 public void testDivide() {
 BigInteger testCases[] = {BigInteger.valueOf(10), BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(5), BigInteger.valueOf(6), BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9)};
 BigInteger dividend[] = {BigInteger.ONE, BigInteger.valueOf(10), BigInteger.valueOf(20), BigInteger.valueOf(30), BigInteger.valueOf(40), BigInteger.valueOf(50), BigInteger.valueOf(60), BigInteger.valueOf(70), BigInteger.valueOf(80), BigInteger.valueOf(90), BigInteger.valueOf(100), BigInteger.valueOf(1000), BigInteger.valueOf(10000), BigInteger.valueOf(100000), BigInteger.valueOf(1000000), BigInteger.valueOf(10000000), BigInteger.valueOf(100000000), BigInteger.valueOf(1000000000)};
 BigInteger divisor[] = {BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(5), BigInteger.valueOf(6), BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9), BigInteger.valueOf(10), BigInteger.valueOf(11), BigInteger.valueOf(11), BigInteger.valueOf(11), BigInteger.valueOf(11), BigInteger.valueOf(11), BigInteger.valueOf(11), BigInteger.valueOf(11), BigInteger.valueOf(11), BigInteger.valueOf(11), BigInteger.valueOf(11)};
 for (int i = 0; i < testCases.length; i++) {
 for (int j = 0; j < dividend.length; j++) {
 for (int k = 0; k < divisor.length; k++) {
 BigInteger dividendTest = dividend[j];
 BigInteger divisorTest = divisor[k];
 RoundingMode roundingMode = RoundingMode.HALF_UP;
 BigInteger expectedQuotient = BigInteger.valueOf(dividendTest.divide(divisorTest).intValueExact());
 BigInteger actualQuotient = BigIntegerMath.divide(testCases[i], divisorTest, roundingMode);
 assertEquals(expectedQuotient, actualQuotient);
 }
 }
 }
 }

 @Test
 public void testFactorial() {
 BigInteger testCases[] = {BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN, BigInteger.valueOf(2), BigInteger.valueOf(5), BigInteger.valueOf(10), BigInteger.valueOf(20), BigInteger.valueOf(25), BigInteger.valueOf(50), BigInteger.valueOf(100), BigInteger.valueOf(250), BigInteger.valueOf(500), BigInteger.valueOf(1000), BigInteger.valueOf(5000), BigInteger.valueOf(10000), BigInteger.valueOf(50000), BigInteger.valueOf(100000), BigInteger.valueOf(500000), BigInteger.valueOf(1000000), BigInteger.valueOf(5000000), BigInteger.valueOf(10000000), BigInteger.valueOf(50000000), BigInteger.valueOf(100000000), BigInteger.valueOf(500000000)};
 for (BigInteger testCase : testCases) {
 BigInteger expectedFactorial = BigIntegerMath.factorial(testCase.intValue());
 BigInteger actualFactorial = BigIntegerMath.factorial(testCase);
 assertEquals(expectedFactorial.toString(), actualFactorial.toString());
 }
 }

 @Test
 public void testBinomial() {
 BigInteger testCases[] = {BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(5), BigInteger.valueOf(6), BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9), BigInteger.valueOf(10), BigInteger.valueOf(15), BigInteger.valueOf(20), BigInteger.valueOf(25), BigInteger.valueOf(30), BigInteger.valueOf(35), BigInteger.valueOf(40), BigInteger.valueOf(45), BigInteger.valueOf(50), BigInteger.valueOf(55), BigInteger.valueOf(60), BigInteger.valueOf(65), BigInteger.valueOf(70), BigInteger.valueOf(75), BigInteger.valueOf(80), BigInteger.valueOf(85), BigInteger.valueOf(90), BigInteger.valueOf(95), BigInteger.valueOf(100)};
 for (BigInteger n : testCases) {
 for (int k = 0; k <= n.intValue(); k++) {
 BigInteger actual = BigIntegerMath.binomial(n.intValue(), k);
 assertEquals(Long.toString(LongMath.binomial(n.intValue(), k)), actual.toString());
 }
 }
 }

 @Test
 public void testFitsInLong() {
 BigInteger testCases[] = {BigInteger.ONE, BigInteger.valueOf(2), BigInteger.valueOf(4), BigInteger.valueOf(16), BigInteger.valueOf(256), BigInteger.valueOf(1024), BigInteger.valueOf(16777216), BigInteger.valueOf(67108864), BigInteger.valueOf(1L << 64), BigInteger.TEN.pow(2), BigInteger.TEN.pow(3), BigInteger.TEN.pow(4), BigInteger.TEN.pow(5), BigInteger.TEN.pow(6), BigInteger.TEN.pow(7), BigInteger.TEN.pow(8), BigInteger.TEN.pow(9), BigInteger.TEN.pow(10), BigInteger.valueOf(1000000000), BigInteger.valueOf(1000000001)};
 for (BigInteger testCase : testCases) {
 assertTrue(BigIntegerMath.fitsInLong(testCase));
 }
 BigInteger negativeTestCases[] = {BigInteger.valueOf(-1), BigInteger.valueOf(-2), BigInteger.valueOf(-4), BigInteger.valueOf(-16), BigInteger.valueOf(-256), BigInteger.valueOf(-512), BigInteger.valueOf(-1024), BigInteger.valueOf(-16777216), BigInteger.valueOf(-67108864), BigInteger.valueOf(-1L << 64), BigInteger.valueOf(0), BigInteger.valueOf(1), BigInteger.valueOf(3), BigInteger.valueOf(5), BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9), BigInteger.valueOf(11), BigInteger.valueOf(13)};
 for (BigInteger negativeTestCase : negativeTestCases) {
 assertFalse(BigIntegerMath.fitsInLong(negativeTestCase));
 }
 }

}