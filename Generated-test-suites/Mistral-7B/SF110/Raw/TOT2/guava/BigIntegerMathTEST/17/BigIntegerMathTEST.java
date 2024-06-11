package com.google.common.math.test;

import static org.junit.Assert.*;

import com.google.common.math.BigIntegerMath;

import org.junit.Test;

/**
 * Test cases for BigIntegerMath class
 */
public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger testNumber = BigInteger.valueOf(1);
        assertTrue(BigIntegerMath.isPowerOfTwo(testNumber));

        testNumber = BigInteger.valueOf(16);
        assertTrue(BigIntegerMath.isPowerOfTwo(testNumber));

        testNumber = BigInteger.valueOf(256);
        assertTrue(BigIntegerMath.isPowerOfTwo(testNumber));

        testNumber = BigInteger.valueOf(257);
        assertFalse(BigIntegerMath.isPowerOfTwo(testNumber));

        testNumber = BigInteger.valueOf(-1);
        assertFalse(BigIntegerMath.isPowerOfTwo(testNumber));

        testNumber = BigInteger.valueOf(Integer.MIN_VALUE);
        assertFalse(BigIntegerMath.isPowerOfTwo(testNumber));

        testNumber = BigInteger.valueOf(Integer.MAX_VALUE);
        assertFalse(BigIntegerMath.isPowerOfTwo(testNumber));

        testNumber = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(testNumber));

        testNumber = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(testNumber));

        testNumber = BigInteger.valueOf(1 << 30);
        assertTrue(BigIntegerMath.isPowerOfTwo(testNumber));

        testNumber = BigInteger.valueOf(1 << 31);
        assertFalse(BigIntegerMath.isPowerOfTwo(testNumber));
    }

    // Add tests for other methods here
}