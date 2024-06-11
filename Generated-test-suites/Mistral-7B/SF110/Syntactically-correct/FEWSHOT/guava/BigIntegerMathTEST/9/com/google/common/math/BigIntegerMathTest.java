package org.apache.commons.cli.test; // Assuming you want to create a package named org.apache.commons.cli.test for your test cases

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.math.BigIntegerMath;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1L << 31)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.add(BigInteger.ONE)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.negate()));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1L)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(12345L)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(12345L << 1)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(12345L << 2)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1L << 31), RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(Integer.MAX_VALUE), RoundingMode.DOWN));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(Integer.MAX_VALUE + 1), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.HALF_UP));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1L << 31), RoundingMode.HALF_UP));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(Integer.MAX_VALUE), RoundingMode.HALF_UP));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(Integer.MAX_VALUE + 1), RoundingMode.HALF_UP));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.HALF_DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.HALF_UP));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.HALF_EVEN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.valueOf(-1L), RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.valueOf(-1L), RoundingMode.HALF_DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.valueOf(-1L), RoundingMode.HALF_UP));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.valueOf(-1L), RoundingMode.HALF_EVEN));
        assertEquals(1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.CEILING));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(Integer.MAX_VALUE + 1), RoundingMode.CEILING));
        assertEquals(33, BigIntegerMath.log2(BigInteger.valueOf(Integer.MAX_VALUE + 1), RoundingMode.CEILING));
        assertEquals(1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1L << 31), RoundingMode.UNNECESSARY));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(Integer.MAX_VALUE), RoundingMode.UNNECESSARY));
        assertEquals(33, BigIntegerMath.log2(BigInteger.valueOf(Integer.MAX_VALUE + 1), RoundingMode.UNNECESSARY));
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
        assertEquals(BigInteger.TEN, BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(2432902008176640000L), BigIntegerMath.factorial(10));
        assertEquals(BigInteger.valueOf(1277752000000000000000000000000000L), BigIntegerMath.factorial(20));

        assertNull(BigIntegerMath.factorial(-1));
        assertNull(BigIntegerMath.factorial(Integer.MIN_VALUE));
    }

    @Test
    public void testListProduct() {
        List<BigInteger> nums = new ArrayList<>();
        nums.add(BigInteger.valueOf(1L));
        nums.add(BigInteger.valueOf(2L));
        nums.add(BigInteger.valueOf(3L));

        BigInteger result = BigIntegerMath.listProduct(nums);
        assertEquals(BigInteger.valueOf(2 * 3 * 1L), result);

        nums.clear();
        nums.add(BigInteger.ONE);

        result = BigIntegerMath.listProduct(nums);
        assertEquals(BigInteger.ONE, result);

        nums = new ArrayList<>();
        nums.add(BigInteger.valueOf(1L));
        nums.add(BigInteger.valueOf(2L));
        nums.add(BigInteger.valueOf(3L));
        nums.add(BigInteger.valueOf(4L));

        result = BigIntegerMath.listProduct(nums, 1, 3);
        assertEquals(BigInteger.valueOf(2 * 3L), result);

        nums = new ArrayList<>();
        nums.add(BigInteger.valueOf(1L));
        nums.add(BigInteger.valueOf(2L));
        nums.add(BigInteger.valueOf(3L));
        nums.add(BigInteger.valueOf(4L));
        nums.add(BigInteger.valueOf(5L));

        result = BigIntegerMath.listProduct(nums);
        assertEquals(BigInteger.valueOf(1 * 2 * 3 * 4 * 5L), result);

        nums = new ArrayList<>();
        nums.add(BigInteger.valueOf(1L));
        nums.add(BigInteger.valueOf(2L));
        nums.add(BigInteger.valueOf(3L));
        nums.add(BigInteger.valueOf(4L));
        nums.add(BigInteger.valueOf(5L));
        nums.add(BigInteger.valueOf(6L));

        result = BigIntegerMath.listProduct(nums, 1, 4);
        assertEquals(BigInteger.valueOf(2 * 3 * 4 * 1L), result);

        result = BigIntegerMath.listProduct(nums, 1, 5);
        assertEquals(BigInteger.valueOf(2 * 3 * 4 * 5L), result);
    }

    @Test
    public void testBinomial() {
        // TODO: Implement test cases for binomial method
    }

    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.ONE));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(123L)));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(12345678901234567890L)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.ONE.add(BigInteger.ONE)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(12345678901234567890L).add(BigInteger.ONE)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(-12345678901234567890L)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(-12345678901234567890L).negate()));
    }
}