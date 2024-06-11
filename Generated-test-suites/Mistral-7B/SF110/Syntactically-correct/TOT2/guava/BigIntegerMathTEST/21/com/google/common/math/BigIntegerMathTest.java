package com.example.bigintegermath;

import com.google.common.math.BigIntegerMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Test code from Expert 1
    }

    @Test
    public void testLog2() {
        // Test code from Expert 2
    }

    @Test
    public void testLog10() {
        // Test code from Expert 3
    }

    // Add other method tests here

    @Test
    public void testDivide() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(2);
        BigInteger expectedResult = BigInteger.valueOf(5);

        assertEquals(expectedResult, BigIntegerMath.divide(p, q, RoundingMode.DOWN));
        assertEquals(expectedResult, BigIntegerMath.divide(p, q, RoundingMode.CEILING));
        assertEquals(expectedResult, BigIntegerMath.divide(p, q, RoundingMode.HALF_DOWN));
        assertEquals(expectedResult, BigIntegerMath.divide(p, q, RoundingMode.HALF_EVEN));
        assertEquals(expectedResult, BigIntegerMath.divide(p, q, RoundingMode.HALF_UP));
        assertEquals(expectedResult, BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY));
    }

    // Test other methods and edge cases here

    @Test
    public void testFactorial() {
        BigInteger expectedResult = BigInteger.valueOf(120);
        List<BigInteger> nums = new ArrayList<>();
        for (int i = 1; i <= 5; ++i) {
            nums.add(BigInteger.valueOf(i));
        }
        assertEquals(expectedResult, BigIntegerMath.listProduct(nums));
        assertEquals(expectedResult, BigIntegerMath.factorial(5));
    }

    @Test
    public void testBinomial() {
        BigInteger expectedResult = BigInteger.valueOf(56);
        assertEquals(expectedResult, BigIntegerMath.binomial(6, 2));
        BigInteger expectedResult2 = BigInteger.valueOf(1025);
        assertEquals(expectedResult2, BigIntegerMath.binomial(10, 5));
    }

    @Test
    public void testFitsInLong() {
        BigInteger bigNumber = BigInteger.valueOf(Long.MAX_VALUE);
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.ONE));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(10)));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE - 100)));
        assertFalse(BigIntegerMath.fitsInLong(bigNumber));
    }
}