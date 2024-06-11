package org.apache.commons.cli.test;  // Assuming you want to test the package com.google.common.math under org.apache.commons.cli for the sake of the example

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.math.*;
import java.util.*;

import org.junit.Test;
import com.google.common.base.*;
import com.google.common.annotations.*;
import com.google.common.math.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assumeTrue(BigInteger.VALUE.isPowerOfTwo());
        assertTrue(BigInteger.ONE.isPowerOfTwo());
        assertFalse(new BigInteger("3")); // non power of two
        assertFalse(new BigInteger("-1")); // negative number
        assertFalse(new BigInteger("0")); // zero
    }

    @Test
    public void testLog2() {
        assumePositive(new BigInteger("1"));

        // Test with positive integers
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(new BigInteger("2147483647"), RoundingMode.DOWN));
        assertEquals(63, BigIntegerMath.log2(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.DOWN));

        // Test with negative integers
        assertEquals(-1, BigIntegerMath.log2(new BigInteger("-1"), RoundingMode.DOWN));
        assertEquals(-32, BigIntegerMath.log2(new BigInteger("-1").negate(), RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(new BigInteger("-1").shiftLeft(31).negate(), RoundingMode.DOWN));

        // Test rounding modes
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UP));
        assertEquals(32, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.UP));
        assertEquals(32, BigIntegerMath.log2(new BigInteger("2"), RoundingMode.CEILING));
        assertEquals(33, BigIntegerMath.log2(new BigInteger("3"), RoundingMode.CEILING));
        assertEquals(32, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.HALF_UP));
        assertEquals(32, BigIntegerMath.log2(new BigInteger("4"), RoundingMode.HALF_EVEN));
        assertEquals(32, BigIntegerMath.log2(new BigInteger("5"), RoundingMode.HALF_DOWN));
        assertEquals(33, BigIntegerMath.log2(new BigInteger("5"), RoundingMode.HALF_UP));
        assertEquals(33, BigIntegerMath.log2(new BigInteger("5"), RoundingMode.HALF_EVEN));
        assertEquals(34, BigIntegerMath.log2(new BigInteger("7"), RoundingMode.HALF_DOWN));
        assertEquals(33, BigIntegerMath.log2(new BigInteger("7"), RoundingMode.HALF_UP));
        assertEquals(33, BigIntegerMath.log2(new BigInteger("7"), RoundingMode.HALF_EVEN));
    }

    // Add tests for log10, sqrt, sqrtFloor, sqrtApproxWithDoubles, divide, factorial, listProduct, binomial, and fitsInLong methods.

}