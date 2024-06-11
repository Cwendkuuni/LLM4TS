package org.apache.commons.cli.test; // make sure to use the correct package name for your test case

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.math.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.TEN));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-64)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(13)));
    }

    @Test
    public void testLog2() {
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(27), RoundingMode.HALF_UP));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertNull(BigIntegerMath.log2(null, RoundingMode.DOWN));
    }

    @Test(expected = AssertionError.class)
    public void testLog2InvalidMode() {
        BigIntegerMath.log2(BigInteger.ONE, RoundingMode.INVALID);
    }

    @Test
    public void testLog10() {
        assertEquals(1, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.HALF_UP));
        assertEquals(1, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.HALF_UP));
        assertEquals(-1, BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.DOWN));
        assertNull(BigIntegerMath.log10(null, RoundingMode.DOWN));
    }

    @Test(expected = AssertionError.class)
    public void testLog10InvalidMode() {
        BigIntegerMath.log10(BigInteger.ONE, RoundingMode.INVALID);
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(16), RoundingMode.HALF_UP));
        assertNull(BigIntegerMath.sqrt(null, RoundingMode.DOWN));
    }

    @Test(expected = AssertionError.class)
    public void testSqrtInvalidMode() {
        BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.INVALID);
    }

    @Test
    public void testDivide() {
        BigInteger dividend = BigInteger.valueOf(21);
        BigInteger divisor = BigInteger.valueOf(7);

        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(dividend, divisor, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN));

        BigInteger largerDividend = BigInteger.valueOf(1000000000000000001);
        BigInteger smallerDivisor = BigInteger.valueOf(1000);

        assertEquals(BigInteger.valueOf(999999999999999), BigIntegerMath.divide(largerDividend, smallerDivisor, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(999999999999999), BigIntegerMath.divide(largerDividend, smallerDivisor, RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(999999999999999), BigIntegerMath.divide(largerDividend, smallerDivisor, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(999999999999999), BigIntegerMath.divide(largerDividend, smallerDivisor, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(999999999999999), BigIntegerMath.divide(largerDividend, smallerDivisor, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(999999999999999), BigIntegerMath.divide(largerDividend, smallerDivisor, RoundingMode.HALF_EVEN));

        assertNull(BigIntegerMath.divide(null, null, RoundingMode.DOWN));
        assertNull(BigIntegerMath.divide(null, divisor, RoundingMode.DOWN));
        assertNull(BigIntegerMath.divide(dividend, null, RoundingMode.DOWN));
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

        BigInteger bigFactorial = BigIntegerMath.factorial(10);
        assertEquals(BigInteger.valueOf(3628800), bigFactorial);

        BigInteger bigFactorialNegative = BigIntegerMath.factorial(-1);
        assertNull(bigFactorialNegative);
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.ZERO, BigIntegerMath.binomial(0, 2));
        assertEquals(BigInteger.TEN, BigIntegerMath.binomial(3, 2));
        assertEquals(BigInteger.valueOf(23), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(496), BigIntegerMath.binomial(8, 3));
        assertEquals(BigInteger.valueOf(2211840), BigIntegerMath.binomial(14, 6));
        assertEquals(BigInteger.valueOf(511840), BigIntegerMath.binomial(15, 5));

        BigInteger largeBinomial = BigIntegerMath.binomial(25, 20);
        assertEquals(BigInteger.valueOf(17710560022525142643374607280000), largeBinomial);

        BigInteger largeNegativeBinomial = BigIntegerMath.binomial(-7, 3);
        assertNull(largeNegativeBinomial);
    }
}