package org.apache.commons.cli.test; // Assuming you want to create a package named org.apache.commons.cli.test for your test class

import static com.google.common.math.BigIntegerMath.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(16)));
        assertFalse(isPowerOfTwo(BigInteger.ONE.shiftLeft(32)));
        assertFalse(isPowerOfTwo(BigInteger.ONE.shiftLeft(64).add(BigInteger.ONE)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, log2(BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(2, log2(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(3, log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(31, log2(BigInteger.ONE.shiftLeft(31), RoundingMode.DOWN));
        assertEquals(32, log2(BigInteger.ONE.shiftLeft(32), RoundingMode.UP));
        assertEquals(33, log2(BigInteger.valueOf(8).multiply(BigInteger.valueOf(2)), RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        // Add test cases for various BigInteger values and RoundingMode
    }

    @Test
    public void testSqrt() {
        // Add test cases for various BigInteger values and RoundingMode
    }

    @Test
    public void testDivide() {
        // Add test cases for various BigInteger values as dividend and divisor, and RoundingMode
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, factorial(0));
        assertEquals(BigInteger.ONE, factorial(1));
        assertEquals(BigInteger.valueOf(2), factorial(2));
        assertEquals(BigInteger.valueOf(6), factorial(3));
        assertEquals(BigInteger.valueOf(24), factorial(4));
        assertEquals(BigInteger.valueOf(120), factorial(5));
        // Add test cases for larger factorials
    }

    @Test
    public void testBinomial() {
        // Add test cases for various n and k values
    }
}