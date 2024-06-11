package org.apache.commons.cli.test; // assuming you want to create a test package under org.apache.commons.cli.test

import static org.junit.Assert.*;

import com.google.common.math.*;
import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(256)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(13)));
    }

    @Test
    public void testLog2() {
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.CEILING));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.DOWN));
        assertEquals(5, BigIntegerMath.log2(BigInteger.valueOf(32), RoundingMode.CEILING));
        assertEquals(16, BigIntegerMath.log2(BigInteger.valueOf(65536), RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        // You need to implement the log10 method for testing
        // For now, this test case is incomplete
    }

    @Test
    public void testSqrt() {
        // You need to implement the sqrt method for testing
        // For now, this test case is incomplete
    }

    @Test
    public void testDivide() {
        // You need to implement the divide method for testing
        // For now, this test case is incomplete
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(51120), BigIntegerMath.factorial(6));
        assertEquals(BigInteger.valueOf(798336078840), BigIntegerMath.factorial(12));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(6, 3));
        assertEquals(BigInteger.valueOf(20), BigIntegerMath.binomial(10, 5));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(12, 11));
    }
}