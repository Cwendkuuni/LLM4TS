package com.google.common.math;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BigIntegerMathTest {

    // Test for isPowerOfTwo method
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(isPowerOfTwo(BigInteger.ONE));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(16)));
        assertFalse(isPowerOfTwo(BigInteger.ZERO));
        assertFalse(isPowerOfTwo(BigInteger.TEN));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(-64)));
    }

    // Test for log2 method with various input and RoundingModes
    @Test
    public void testLog2() {
        assertEquals(0, log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, log2(BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(3, log2(BigInteger.valueOf(8), RoundingMode.UP));
        assertEquals(3, log2(BigInteger.valueOf(8), RoundingMode.CEILING));
        assertEquals(3, log2(BigInteger.valueOf(8), RoundingMode.HALF_UP));
        assertEquals(3, log2(BigInteger.valueOf(8), RoundingMode.HALF_EVEN));
        assertEquals(-1, log2(BigInteger.ONE, RoundingMode.UP));
        assertEquals(-1, log2(BigInteger.ONE, RoundingMode.CEILING));
        assertEquals(-1, log2(BigInteger.ONE, RoundingMode.HALF_UP));
        assertEquals(-1, log2(BigInteger.ONE, RoundingMode.HALF_EVEN));
        assertEquals(32, log2(BigInteger.valueOf((long) Math.pow(2, 32)), RoundingMode.DOWN));
        assertEquals(33, log2(BigInteger.valueOf((long) Math.pow(2, 32)), RoundingMode.UP));
        assertEquals(33, log2(BigInteger.valueOf((long) Math.pow(2, 32)), RoundingMode.CEILING));
    }

    // Test for log10 method with various input and RoundingModes
    @Test
    public void testLog10() {
        // TODO: Implement test cases for this method
    }

    // Test for sqrt method with various input and RoundingModes
    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), sqrt(BigInteger.valueOf(9), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(3), sqrt(BigInteger.valueOf(9), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(4), sqrt(BigInteger.valueOf(16), RoundingMode.HALF_UP));
        try {
            sqrt(BigInteger.valueOf(-1), RoundingMode.DOWN);
            fail();
        } catch (AssertionError e) {
            // expected exception
        }
        try {
            sqrt(BigInteger.TEN, RoundingMode.DOWN);
            fail();
        } catch (AssertionError e) {
            // expected exception
        }
    }

    // Test for divide method with various inputs
    @Test
    public void testDivide() {
        assertEquals(BigInteger.ONE, divide(BigInteger.valueOf(10), BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), divide(BigInteger.valueOf(12), BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(10), divide(BigInteger.valueOf(20), BigInteger.valueOf(2), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(5), divide(BigInteger.valueOf(25), BigInteger.valueOf(5), RoundingMode.UP));
        try {
            divide(BigInteger.valueOf(-1), BigInteger.ONE, RoundingMode.DOWN);
            fail();
        } catch (AssertionError e) {
            // expected exception
        }
        try {
            divide(BigInteger.valueOf(1), BigInteger.ZERO, RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException e) {
            // expected exception
        }
    }

    // Test for factorial method with various inputs
    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, factorial(0));
        assertEquals(BigInteger.valueOf(1), factorial(1));
        assertEquals(BigInteger.valueOf(2), factorial(2));
        assertEquals(BigInteger.valueOf(24), factorial(4));
        assertEquals(BigInteger.valueOf(120), factorial(6));
        assertEquals(BigInteger.valueOf(720), factorial(6));
        try {
            factorial(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        try {
            factorial(13);
            fail();
        } catch (StackOverflowError e) {
            // expected exception
        }
    }

    // Test for binomial method with various inputs
    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, binomial(0, 0));
        assertEquals(BigInteger.ONE, binomial(0, 1));
        assertEquals(BigInteger.ONE, binomial(1, 0));
        assertEquals(BigInteger.ONE, binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), binomial(3, 2));
        assertEquals(BigInteger.valueOf(21), binomial(4, 3));
        assertEquals(BigInteger.valueOf(1140), binomial(7, 6));
        try {
            binomial(-1, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        try {
            binomial(5, 7);
            fail();
        } catch (StackOverflowError e) {
            // expected exception
        }
    }

    // Test for fitsInLong method
    @Test
    public void testFitsInLong() {
        assertFalse(fitsInLong(BigInteger.valueOf(2147483648L)));
        assertFalse(fitsInLong(BigInteger.valueOf(2147483649L)));
        assertTrue(fitsInLong(BigInteger.ONE));
        assertTrue(fitsInLong(BigInteger.valueOf(1234567890L)));
        assertTrue(fitsInLong(BigInteger.valueOf(-1234567890L)));
    }

    private boolean isPowerOfTwo(BigInteger x) {
        // Test implementation from BigIntegerMath class
    }

    private int log2(BigInteger x, RoundingMode mode) {
        // Test implementation from BigIntegerMath class
    }

    // Test helper methods
    private BigInteger sqrt(BigInteger x, RoundingMode mode) {
        // Test implementation from BigIntegerMath class
    }

    private BigInteger sqrtFloor(BigInteger x) {
        // Test implementation from BigIntegerMath class, private
    }

    private BigInteger sqrtApproxWithDoubles(BigInteger x) {
        // Test implementation from BigIntegerMath class, private
    }

    private BigInteger divide(BigInteger p, BigInteger q, RoundingMode mode) {
        // Test implementation from BigIntegerMath class
    }

    private BigInteger factorial(int n) {
        // Test implementation from BigIntegerMath class
    }

    private BigInteger listProduct(List<BigInteger> nums) {
        // Test implementation from BigIntegerMath class, static
    }

    private BigInteger listProduct(List<BigInteger> nums, int start, int end) {
        // Test implementation from BigIntegerMath class, static
    }

    private boolean fitsInLong(BigInteger x) {
        // Test implementation from BigIntegerMath class
    }
}