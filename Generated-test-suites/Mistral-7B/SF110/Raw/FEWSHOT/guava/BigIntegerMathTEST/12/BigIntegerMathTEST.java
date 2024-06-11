package org.apache.commons.cli.test; // Assume we create a new package named org.apache.commons.cli.test for the test cases

import static org.junit.Assert.*;

import com.google.common.math.*;
import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(4)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.ONE.add(BigInteger.ONE)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(5)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(11)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(32), RoundingMode.CEILING));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(128), RoundingMode.HALF_UP));
        assertEquals(Integer.MAX_VALUE - 3, BigIntegerMath.log2(BigInteger.valueOf(Integer.MAX_VALUE), RoundingMode.HALF_DOWN));
        assertEquals(Integer.MAX_VALUE - 3, BigIntegerMath.log2(BigInteger.valueOf(Integer.MAX_VALUE), RoundingMode.HALF_EVEN));
        assertEquals(Integer.MAX_VALUE - 2, BigIntegerMath.log2(BigInteger.valueOf(Integer.MAX_VALUE), RoundingMode.UP));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.DOWN));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000000), RoundingMode.HALF_UP));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(1000000000), RoundingMode.HALF_UP));
        assertEquals(Integer.MAX_VALUE - 3, BigIntegerMath.log10(BigInteger.valueOf(Integer.MAX_VALUE), RoundingMode.HALF_DOWN));
        assertEquals(Integer.MAX_VALUE - 3, BigIntegerMath.log10(BigInteger.valueOf(Integer.MAX_VALUE), RoundingMode.HALF_EVEN));
        assertEquals(Integer.MAX_VALUE - 2, BigIntegerMath.log10(BigInteger.valueOf(Integer.MAX_VALUE), RoundingMode.UP));
        assertEquals(-1, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log10(BigInteger.ONE.negate(), RoundingMode.DOWN));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(16), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(8), BigIntegerMath.sqrt(BigInteger.valueOf(64), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(17), BigIntegerMath.sqrt(BigInteger.valueOf(289), RoundingMode.HALF_EVEN));
        assertEquals(BigInteger.valueOf(16384), BigIntegerMath.sqrt(BigInteger.valueOf(262144), RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(16384), BigIntegerMath.sqrt(BigInteger.valueOf(262144), RoundingMode.HALF_EVEN));
        assertEquals(null, BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(null, BigIntegerMath.sqrt(BigInteger.ONE.negate(), RoundingMode.DOWN));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(24*49*120), BigIntegerMath.factorial(10));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(14));
        assertEquals(BigInteger.valueOf(BigInteger.ONE.shiftLeft(63)), BigIntegerMath.factorial(64)); // Maximum value representable by a long
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(7, 3));
        assertEquals(BigInteger.valueOf(252), BigIntegerMath.binomial(10, 5));
        assertEquals(BigInteger.valueOf(167960), BigIntegerMath.binomial(20, 10));
        assertEquals(BigInteger.valueOf(76543200), BigIntegerMath.binomial(25, 20));
    }
}