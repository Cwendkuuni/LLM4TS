package org.apache.commons.cli; // Replace with the actual package name of BigIntegerMath if necessary

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.common.math.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1L << 31)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-5)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.TEN));
    }

    @Test
    public void testLog2() {
        assertEquals(1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1L << 31), RoundingMode.DOWN));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(1L << 32), RoundingMode.UP));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(-1L << 31), RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        // TODO: Implement test cases for log10 method
    }

    @Test
    public void testSqrt() {
        // TODO: Implement test cases for sqrt method
    }

    @Test
    public void testSqrtFloor() {
        // TODO: Implement test cases for sqrtFloor method
    }

    @Test
    public void testSqrtApproxWithDoubles() {
        // TODO: Implement test cases for sqrtApproxWithDoubles method
    }

    @Test
    public void testDivide() {
        // TODO: Implement test cases for divide method
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(51120), BigIntegerMath.factorial(6));

        // Test larger factorials
        final int maxTest = 50;
        for (int i = 7; i <= maxTest; ++i) {
            final BigInteger factorial = BigIntegerMath.factorial(i);
            System.out.printf("Factorial of %d is %s%n", i, factorial);
        }
    }

    @Test
    public void testListProduct() {
        // TODO: Implement test cases for listProduct method
    }

    @Test
    public void testBinomial() {
        // TODO: Implement test cases for binomial method
    }

    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.ONE));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.TEN.pow(16)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(-1L << 63)));
    }
}