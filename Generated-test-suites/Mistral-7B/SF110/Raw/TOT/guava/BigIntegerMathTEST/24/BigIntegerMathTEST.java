package com.google.common.math;

import com.google.common.base.Preconditions;
import com.google.common.math.BigIntegerMath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(new BigInteger("1", 2)));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("3")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("-1")));
        assertFalse(BigIntegerMath.isPowerOfTwo(new BigInteger("-2")));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(new BigInteger("2").pow(31), RoundingMode.DOWN));
        assertEquals(32, BigIntegerMath.log2(new BigInteger("4").pow(2), RoundingMode.UP));
        assertEquals(32, BigIntegerMath.log2(new BigInteger("8"), RoundingMode.CEILING));
        assertEquals(31, BigIntegerMath.log2(new BigInteger("7"), RoundingMode.HALF_UP));

        try {
            BigIntegerMath.log2(new BigInteger("-1"), RoundingMode.ANY);
            fail();
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(new BigInteger("10"), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(new BigInteger("1000"), RoundingMode.CEILING));
        assertEquals(2, BigIntegerMath.log10(new BigInteger("100"), RoundingMode.HALF_UP));

        try {
            BigIntegerMath.log10(new BigInteger("-1"), RoundingMode.ANY);
            fail();
        } catch (AssertionError e) {
            // Expected
        }

        try {
            BigIntegerMath.log10(new BigInteger("1000000000000000001"), RoundingMode.ANY);
            fail();
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(new BigInteger("2"), BigIntegerMath.sqrt(new BigInteger("4"), RoundingMode.UP));
        assertEquals(new BigInteger("3"), BigIntegerMath.sqrt(new BigInteger("9"), RoundingMode.CEILING));
        assertEquals(new BigInteger("5"), BigIntegerMath.sqrt(new BigInteger("25"), RoundingMode.HALF_UP));

        try {
            BigIntegerMath.sqrt(new BigInteger("-1"), RoundingMode.ANY);
            fail();
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.factorial(6));
        assertEquals(BigInteger.valueOf(5040), BigIntegerMath.factorial(7));
        assertEquals(BigInteger.valueOf(479001600), BigIntegerMath.factorial(10));

        try {
            BigIntegerMath.factorial(-1);
            fail();
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(7, 3));
        assertEquals(BigInteger.valueOf(210), BigIntegerMath.binomial(13, 7));
        assertEquals(BigInteger.valueOf(2002144946656670905), BigIntegerMath.binomial(36, 18));

        try {
            BigIntegerMath.binomial(-1, 1);
            fail();
        } catch (AssertionError e) {
            // Expected
        }

        try {
            BigIntegerMath.binomial(1, 10);
            fail();
        } catch (AssertionError e) {
            // Expected
        }
    }
}