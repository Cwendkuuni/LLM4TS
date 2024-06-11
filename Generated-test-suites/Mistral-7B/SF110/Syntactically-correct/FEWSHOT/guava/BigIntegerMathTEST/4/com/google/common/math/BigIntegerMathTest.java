package org.apache.commons.cli.test; // Assuming you want to keep the test package under org.apache.commons.cli

import static com.google.common.math.BigIntegerMath.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Positive tests
        assertTrue(isPowerOfTwo(BigInteger.ONE));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(1L << 31)));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(1L << 63)));

        // Negative tests
        assertFalse(isPowerOfTwo(BigInteger.ZERO));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(-1L)));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(1L << 64)));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(2 * BigInteger.ONE.shiftLeft(31))));
    }

    @Test
    public void testLog2() {
        // Positive tests
        assertEquals(0, log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(31, log2(BigInteger.valueOf(1L << 31), RoundingMode.DOWN));
        assertEquals(63, log2(BigInteger.valueOf(1L << 63), RoundingMode.DOWN));

        // Rounding mode tests
        assertEquals(0, log2(BigInteger.ONE, RoundingMode.CEILING));
        assertEquals(32, log2(BigInteger.valueOf(1L << 31), RoundingMode.CEILING));
        assertEquals(64, log2(BigInteger.valueOf(1L << 63), RoundingMode.CEILING));

        // Edge cases
        assertEquals(31, log2(BigInteger.valueOf(1L << 31).add(BigInteger.ONE), RoundingMode.DOWN));
        assertEquals(32, log2(BigInteger.valueOf(1L << 31).add(BigInteger.ONE), RoundingMode.CEILING));

        // Negative tests
        assertNull(log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertNull(log2(BigInteger.ZERO, RoundingMode.CEILING));
        assertNull(log2(BigInteger.ONE.negate(), RoundingMode.DOWN));
        assertNull(log2(BigInteger.ONE.negate(), RoundingMode.CEILING));
    }

    @Test
    public void testLog10() {
        // Positive tests (fits in long)
        assertEquals(0, log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(3, log10(BigInteger.valueOf(1000), RoundingMode.DOWN));

        // Negative tests
        assertNull(log10(BigInteger.ZERO, RoundingMode.DOWN));
        assertNull(log10(BigInteger.ZERO, RoundingMode.CEILING));
        assertNull(log10(BigInteger.ONE.negate(), RoundingMode.DOWN));
        assertNull(log10(BigInteger.ONE.negate(), RoundingMode.CEILING));

        // Overflow tests
        assertEquals(1, log10(BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE), RoundingMode.DOWN));
        assertEquals(2, log10(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE), RoundingMode.DOWN));

        // Rounding mode tests
        assertEquals(1, log10(BigInteger.valueOf(Integer.MAX_VALUE), RoundingMode.CEILING));
        assertEquals(2, log10(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.CEILING));

        // Edge cases
        assertEquals(0, log10(BigInteger.valueOf(1), RoundingMode.DOWN));
        assertEquals(1, log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(1, log10(BigInteger.valueOf(10).pow(2), RoundingMode.DOWN));
        assertEquals(3, log10(BigInteger.valueOf(1000), RoundingMode.DOWN));
        assertEquals(3, log10(BigInteger.valueOf(1000000), RoundingMode.DOWN));
        assertEquals(4, log10(BigInteger.valueOf(1000000000), RoundingMode.DOWN));
    }

    @Test
    public void testSqrt() {
        // Positive tests
        assertEquals(BigInteger.valueOf(1), sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(32), sqrt(BigInteger.valueOf(1024), RoundingMode.DOWN));

        // Rounding mode tests
        assertEquals(BigInteger.valueOf(1), sqrt(BigInteger.ONE, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(33), sqrt(BigInteger.valueOf(1024), RoundingMode.CEILING));

        // Negative tests
        assertNull(sqrt(BigInteger.ZERO, RoundingMode.DOWN));
        assertNull(sqrt(BigInteger.ZERO, RoundingMode.CEILING));
        assertNull(sqrt(BigInteger.ONE.negate(), RoundingMode.DOWN));
        assertNull(sqrt(BigInteger.ONE.negate(), RoundingMode.CEILING));
    }

    @Test
    public void testFactorial() {
        // Positive tests
        assertEquals(BigInteger.ONE, factorial(0));
        assertEquals(BigInteger.valueOf(1), factorial(1));
        assertEquals(BigInteger.valueOf(2), factorial(2));
        assertEquals(BigInteger.valueOf(120), factorial(5));
        assertEquals(BigInteger.valueOf(51120), factorial(11));
        assertEquals(BigInteger.valueOf(1307674368000), factorial(20));

        // Edge cases
        assertEquals(BigInteger.valueOf(2), factorial(2));
        assertEquals(BigInteger.valueOf(24), factorial(4));
        assertEquals(BigInteger.valueOf(362880), factorial(10));
        assertEquals(BigInteger.valueOf(511760000), factorial(15));

        // Overflow tests
        assertEquals(BigInteger.valueOf(137438953471805516904338000000000000), factorial(31));
        assertEquals(BigInteger.valueOf(1157920892373161954235709850086879078532699944656679), factorial(32));
    }

    @Test
    public void testListProduct() {
        List<BigInteger> testList = new ArrayList<>();
        testList.add(BigInteger.valueOf(1));
        testList.add(BigInteger.valueOf(2));
        testList.add(BigInteger.valueOf(3));

        assertEquals(BigInteger.valueOf(2), listProduct(testList));
        assertEquals(BigInteger.valueOf(6), listProduct(testList, 1, 3));
    }

    @Test
    public void testBinomial() {
        // Positive tests
        assertEquals(BigInteger.ONE, binomial(0, 0));
        assertEquals(BigInteger.ONE, binomial(1, 0));
        assertEquals(BigInteger.ONE, binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), binomial(3, 2));
        assertEquals(BigInteger.valueOf(1307674368000), binomial(20, 19));

        // Negative tests
        assertNull(binomial(2, 3));
        assertNull(binomial(3, 2));
        assertNull(binomial(-1, 2));
        assertNull(binomial(1, 3));
    }

    @Test
    public void testFitsInLong() {
        assertTrue(fitsInLong(BigInteger.valueOf(1)));
        assertTrue(fitsInLong(BigInteger.valueOf(Integer.MAX_VALUE)));
        assertTrue(fitsInLong(BigInteger.valueOf(-Integer.MIN_VALUE)));

        assertFalse(fitsInLong(BigInteger.ONE.shiftLeft(64)));
        assertFalse(fitsInLong(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE)));
    }
}