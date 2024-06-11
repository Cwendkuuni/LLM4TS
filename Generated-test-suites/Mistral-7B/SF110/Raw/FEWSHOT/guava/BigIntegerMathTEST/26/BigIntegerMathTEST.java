package com.google.common.math;

import com.google.common.base.*;
import com.google.common.annotations.*;
import java.math.*;
import java.util.*;
import static org.junit.Assert.*;

@GwtCompatible(emulated = true)
public final class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(5)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(11)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(27), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.HALF_EVEN));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(65536), RoundingMode.HALF_EVEN));
        assertEquals(2, BigIntegerMath.log2(BigInteger.valueOf(128), RoundingMode.HALF_DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(512), RoundingMode.HALF_UP));
        assertEquals(5, BigIntegerMath.log2(BigInteger.valueOf(32768), RoundingMode.HALF_EVEN));
    }

    @Test (expected = AssertionError.class)
    public void testLog2InvalidMode() {
        BigIntegerMath.log2(BigInteger.ONE, RoundingMode.valueOf("INVALID_MODE"));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.HALF_EVEN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.HALF_DOWN));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(10000), RoundingMode.HALF_UP));
    }

    @Test (expected = AssertionError.class)
    public void testLog10InvalidMode() {
        BigIntegerMath.log10(BigInteger.ONE, RoundingMode.valueOf("INVALID_MODE"));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.valueOf(1), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(11), BigIntegerMath.sqrt(BigInteger.valueOf(121), RoundingMode.HALF_EVEN));
    }

    @Test (expected = AssertionError.class)
    public void testSqrtInvalidMode() {
        BigIntegerMath.sqrt(BigInteger.valueOf(1), RoundingMode.valueOf("INVALID_MODE"));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(6), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(6), BigInteger.valueOf(2), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.divide(BigInteger.valueOf(16), BigInteger.valueOf(4), RoundingMode.HALF_EVEN));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.divide(BigInteger.valueOf(25), BigInteger.valueOf(5), RoundingMode.HALF_UP));
    }

    @Test (expected = AssertionError.class)
    public void testDivideByZero() {
        BigIntegerMath.divide(BigInteger.ONE, BigInteger.ZERO, RoundingMode.DOWN);
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(2432902008176640000), BigIntegerMath.factorial(13));
        assertEquals(BigInteger.valueOf(7152709438785716606368564037568977249230407582097561), BigIntegerMath.factorial(20));
    }

    @Test (expected = AssertionError.class)
    public void testFactorialNegative() {
        BigIntegerMath.factorial(-1);
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(3, 2));
        assertEquals(BigInteger.valueOf(210), BigIntegerMath.binomial(11, 5));
        assertEquals(BigInteger.valueOf(13776541820417661232951559169638328977113129639444), BigIntegerMath.binomial(21, 10));
        assertEquals(BigInteger.valueOf(10946), BigIntegerMath.binomial(20, 10));
    }

    @Test (expected = AssertionError.class)
    public void testBinomialInvalidParams() {
        BigIntegerMath.binomial(-1, 0);
        BigIntegerMath.binomial(1, 21);
    }

    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.ONE));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE + 1)));
    }
}