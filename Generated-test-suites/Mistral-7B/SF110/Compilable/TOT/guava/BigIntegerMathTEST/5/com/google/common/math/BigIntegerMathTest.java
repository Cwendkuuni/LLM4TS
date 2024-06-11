package com.google.common.math;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;
import java.math.RoundingMode;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-16)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.FLOOR));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.HALF_UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(27), RoundingMode.HALF_DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.UNNECESSARY));
        try {
            BigIntegerMath.log2(BigInteger.valueOf(-1), RoundingMode.UNNECESSARY);
            fail("Should have thrown an AssertionError");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testLog10() {
        // TODO: Implement test cases for this method
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(16), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.HALF_DOWN));
        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(-1), RoundingMode.UNNECESSARY);
            fail("Should have thrown an AssertionError");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.divide(BigInteger.valueOf(100), BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.divide(BigInteger.valueOf(100), BigInteger.valueOf(32), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(BigInteger.valueOf(9), BigInteger.valueOf(3), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.divide(BigInteger.valueOf(12), BigInteger.valueOf(3), RoundingMode.HALF_DOWN));
        try {
            BigIntegerMath.divide(BigInteger.valueOf(-1), BigInteger.ONE, RoundingMode.UNNECESSARY);
            fail("Should have thrown an ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.factorial(6));
        assertEquals(BigInteger.valueOf(5040), BigIntegerMath.factorial(7));
        assertEquals(BigInteger.valueOf(40320), BigIntegerMath.factorial(8));
        assertEquals(BigInteger.valueOf(362880), BigIntegerMath.factorial(9));
        try {
            BigIntegerMath.factorial(-1);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBinomial() {
        // TODO: Implement test cases for this method
    }
}