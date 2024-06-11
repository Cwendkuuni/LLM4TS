package com.google.common.math;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BigIntegerMathTest {

 private BigInteger x;
 private RoundingMode mode;

 public BigIntegerMathTest(BigInteger x, RoundingMode mode) {
 this.x = x;
 this.mode = mode;
 }

 @Parameterized.Parameters(name = "{index}: test log2({0}, {1})")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // positive numbers
 new Object[]{BigInteger.ONE, RoundingMode.DOWN, 0},
 new Object[]{BigInteger.valueOf(12345L), RoundingMode.UP, 17},
 new Object[]{BigInteger.TEN.pow(100), RoundingMode.HALF_UP, 101},
 new Object[]{BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.HALF_DOWN, 63},
 // negative numbers
 new Object[]{BigInteger.NEGATE_ONE, RoundingMode.DOWN, 0},
 new Object[]{BigInteger.valueOf(Long.MIN_VALUE), RoundingMode.UP, 63},
 // zero
 new Object[]{BigInteger.ZERO, RoundingMode.DOWN, 0},
 new Object[]{BigInteger.ZERO, RoundingMode.UP, 0},
 // special cases
 new Object[]{BigInteger.ONE.shiftLeft(63), RoundingMode.UP, 63},
 new Object[]{BigInteger.ONE.shiftLeft(63), RoundingMode.DOWN, 63},
 new Object[]{BigInteger.ONE.shiftLeft(63), RoundingMode.HALF_UP, 64},
 new Object[]{BigInteger.ONE.shiftLeft(63), RoundingMode.HALF_DOWN, 64}
 });
 }

 @Test
 public void testLog2() {
 int expectedLog = (int) params[0];
 int actualLog = BigIntegerMath.log2(x, mode);
 assertThat(actualLog).isEqualTo(expectedLog);
 }

 @Test(expected = AssertionError.class)
 public void testLog2WithInvalidRoundingMode() {
 BigIntegerMath.log2(x, RoundingMode.valueOf("INVALID_MODE"));
 }

 // Add more test cases for isPowerOfTwo, log10, sqrt, divide, factorial, binomial and other methods

 @Test
 public void testFactorial() {
 int n = 10;
 BigInteger actualFactorial = BigIntegerMath.factorial(n);
 BigInteger expectedFactorial = BigInteger.valueOf(3628800L); // 10!
 assertThat(actualFactorial).isEqualTo(expectedFactorial);
 }

 @Test
 public void testBinomial() {
 int n = 5;
 int k = 3;
 BigInteger actualBinomial = BigIntegerMath.binomial(n, k);
 BigInteger expectedBinomial = BigInteger.valueOf(10L); // 5C3 = 10
 assertThat(actualBinomial).isEqualTo(expectedBinomial);
 }

 @Test
 public void testFitsInLong() {
 // true cases
 assertThat(BigIntegerMath.fitsInLong(BigInteger.ONE)).isTrue();
 assertThat(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE))).isTrue();
 // false cases
 assertThat(BigIntegerMath.fitsInLong(BigInteger.ONE.shiftLeft(64))).isFalse();
 }

 // Add more test cases for other methods

}