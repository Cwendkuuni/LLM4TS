package org.apache.commons.cli.test; // Assuming you want to place the test cases in a separate package

import static org.junit.Assert.*;
import com.google.common.math.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2L)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16L)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(128L)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3L)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(4L)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(5L)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10L)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2L), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8L), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16L), RoundingMode.DOWN));
        assertEquals(6, BigIntegerMath.log2(BigInteger.valueOf(64L), RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483647L), RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483648L), RoundingMode.UP));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(4294967296L), RoundingMode.UP));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        for (int i = 0; i < 10; ++i) {
            assertEquals(i, BigIntegerMath.log10(BigInteger.valueOf(Math.pow(10, i)), RoundingMode.DOWN));
            assertEquals(i + 1, BigIntegerMath.log10(BigInteger.valueOf(Math.pow(10, i + 1)), RoundingMode.UP));
        }
        assertEquals(-1, BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(Long.MAX_VALUE, BigIntegerMath.log10(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.UP));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(17), BigIntegerMath.sqrt(BigInteger.valueOf(289), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(29), BigIntegerMath.sqrt(BigInteger.valueOf(841), RoundingMode.UP));
        assertEquals(null, BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(null, BigIntegerMath.sqrt(BigInteger.valueOf(121), RoundingMode.DOWN));
    }

    @Test
    public void testDivide() {
        BigInteger dividend = BigInteger.valueOf(100);
        BigInteger divisor = BigInteger.valueOf(5);

        assertEquals(BigInteger.valueOf(20), BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(21), BigIntegerMath.divide(dividend, divisor, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(19), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(19), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(19), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN));

        dividend = BigInteger.valueOf(100);
        divisor = BigInteger.valueOf(1);

        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.UP));
        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP));
        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN));

        dividend = BigInteger.valueOf(100);
        divisor = BigInteger.valueOf(-5);

        assertEquals(BigInteger.valueOf(-20), BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-21), BigIntegerMath.divide(dividend, divisor, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(-19), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(-19), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(-19), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN));

        dividend = BigInteger.valueOf(100);
        divisor = BigInteger.valueOf(0);

        assertNull(BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
        assertNull(BigIntegerMath.divide(dividend, divisor, RoundingMode.UP));
        assertNull(BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertNull(BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP));
        assertNull(BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN));
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
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(6, 3));
        assertEquals(BigInteger.valueOf(56), BigIntegerMath.binomial(10, 5));
        assertEquals(BigInteger.valueOf(252), BigIntegerMath.binomial(10, 8));
        assertEquals(BigInteger.valueOf(6227020800), BigIntegerMath.binomial(20, 10));
    }

    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.ONE));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MIN_VALUE)));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE + 1)));
    }
}