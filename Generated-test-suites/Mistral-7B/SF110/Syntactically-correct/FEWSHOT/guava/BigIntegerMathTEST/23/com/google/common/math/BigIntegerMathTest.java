package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.BigIntegerMath;
import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1 << 32)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(4).add(BigInteger.ONE)));
    }

    @Test
    public void testLog2() {
        assertEquals(1, BigIntegerMath.log2(BigInteger.ONE));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(1 << 32)));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1 << 31)));
        assertEquals(33, BigIntegerMath.log2(BigInteger.valueOf(1 << 33)));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate()));

        // Test different rounding modes
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1 << 31), RoundingMode.DOWN));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(1 << 32), RoundingMode.UP));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(1 << 32), RoundingMode.CEILING));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1 << 31), RoundingMode.FLOOR));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1 << 31), RoundingMode.HALF_DOWN));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(1 << 32), RoundingMode.HALF_UP));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(1 << 32), RoundingMode.HALF_EVEN));

        // Test out of range value
        assertNull(BigIntegerMath.log2(null));
    }

    @Test(expected = AssertionError.class)
    public void testLog2InvalidRoundingMode() {
        BigIntegerMath.log2(BigInteger.ONE, RoundingMode.CHOP);
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ZERO));
        assertEquals(1, BigIntegerMath.log10(BigInteger.ONE));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(10)));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100)));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(1000)));

        // Test different rounding modes
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.CEILING));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.FLOOR));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.HALF_DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.HALF_UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.HALF_EVEN));

        // Test out of range value
        assertNull(BigIntegerMath.log10(null));
    }

    @Test(expected = AssertionError.class)
    public void testLog10InvalidRoundingMode() {
        BigIntegerMath.log10(BigInteger.ONE, RoundingMode.CHOP);
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4)));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9)));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25)));
        assertEquals(BigInteger.valueOf(17), BigIntegerMath.sqrt(BigInteger.valueOf(289)));
        assertEquals(BigInteger.valueOf(29), BigIntegerMath.sqrt(BigInteger.valueOf(841)));

        // Test different rounding modes
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.HALF_EVEN));

        // Test negative number
        assertNull(BigIntegerMath.sqrt(BigInteger.ONE.negate()));

        // Test out of range value
        assertNull(BigIntegerMath.sqrt(BigInteger.valueOf(-1)));
    }

    @Test(expected = AssertionError.class)
    public void testSqrtInvalidRoundingMode() {
        BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.CHOP);
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
        assertEquals(BigInteger.valueOf(40320), BigIntegerMath.factorial(8));
        assertEquals(BigInteger.valueOf(362880), BigIntegerMath.factorial(9));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(10));

        // Test large number
        assertEquals(BigInteger.valueOf(1307674368000L), BigIntegerMath.factorial(20));
    }

    @Test
    public void testListProduct() {
        List<BigInteger> nums = new ArrayList<>();
        nums.add(BigInteger.ONE);
        nums.add(BigInteger.valueOf(2));
        nums.add(BigInteger.valueOf(3));
        assertEquals(BigInteger.valueOf(2 * 3 * 1), BigIntegerMath.listProduct(nums));

        nums.add(BigInteger.valueOf(4));
        assertEquals(BigInteger.valueOf(2 * 3 * 1 * 4), BigIntegerMath.listProduct(nums));

        nums.add(BigInteger.valueOf(5));
        assertEquals(BigInteger.valueOf(2 * 3 * 1 * 4 * 5), BigIntegerMath.listProduct(nums));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.ZERO, BigIntegerMath.binomial(1, 2));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 2));
        assertEquals(BigInteger.TWO, BigIntegerMath.binomial(3, 2));
        assertEquals(BigInteger.valueOf(11), BigIntegerMath.binomial(4, 3));
        assertEquals(BigInteger.valueOf(210), BigIntegerMath.binomial(10, 5));
    }

    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.ONE));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE + 1)));
    }
}