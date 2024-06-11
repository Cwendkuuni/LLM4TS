package org.apache.commons.cli.math; // assuming you have imported the package name correctly

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.math.BigIntegerMath;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1 << 31)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1 << 64))); // assuming BigInteger supports 128-bit integers
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1 << 31), RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(1 << 31), RoundingMode.UP));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(1 << 32), RoundingMode.DOWN));
        assertEquals(33, BigIntegerMath.log2(BigInteger.valueOf(1 << 33), RoundingMode.UP));
        assertEquals(63, BigIntegerMath.log2(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.DOWN));
        assertEquals(64, BigIntegerMath.log2(BigInteger.ONE.longValue() << 63, RoundingMode.DOWN));
        assertNull(BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertNull(BigIntegerMath.log2(BigInteger.valueOf(-1), RoundingMode.DOWN));
        try {
            BigIntegerMath.log2(null, RoundingMode.DOWN);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UP));
        assertEquals(-31, BigIntegerMath.log2(BigInteger.valueOf(1 << 31).negate(), RoundingMode.DOWN));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.TEN, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1024), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1024), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(10240), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(10240), RoundingMode.UP));
        assertEquals(13, BigIntegerMath.log10(BigInteger.valueOf((long) Math.pow(10, 13))));

        long maxLong = Long.MAX_VALUE;
        assertEquals((int) Math.log10(maxLong), BigIntegerMath.log10(BigInteger.valueOf(maxLong), RoundingMode.DOWN).intValue());
        assertEquals((int) Math.ceil(Math.log10(maxLong)), BigIntegerMath.log10(BigInteger.valueOf(maxLong), RoundingMode.CEILING).intValue());
        assertEquals((int) Math.floor(Math.log10(maxLong)), BigIntegerMath.log10(BigInteger.valueOf(maxLong), RoundingMode.FLOOR).intValue());

        BigInteger bigNumber = BigInteger.valueOf(Long.MAX_VALUE)
                .multiply(BigInteger.valueOf(Math.pow(10, 62)));
        assertEquals((int) Math.log10(bigNumber.longValueExact()), BigIntegerMath.log10(bigNumber, RoundingMode.DOWN).intValue());
        assertEquals((int) Math.ceil(Math.log10(bigNumber.longValueExact())), BigIntegerMath.log10(bigNumber, RoundingMode.CEILING).intValue());
        assertEquals((int) Math.floor(Math.log10(bigNumber.longValueExact())), BigIntegerMath.log10(bigNumber, RoundingMode.FLOOR).intValue());

        try {
            BigIntegerMath.log10(null, RoundingMode.DOWN);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5L), BigIntegerMath.sqrt(BigInteger.valueOf(25L), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(17L), BigIntegerMath.sqrt(BigInteger.valueOf(289L), RoundingMode.DOWN));

        BigInteger bigNumber = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(2));
        assertEquals(BigIntegerMath.sqrt(bigNumber, RoundingMode.DOWN), BigIntegerMath.sqrt(bigNumber, RoundingMode.CEILING));

        BigIntegerMath.sqrt(BigInteger.valueOf(Long.MAX_VALUE).longValueExact(), RoundingMode.UP); // not supported

        try {
            BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(Long.MIN_VALUE).negate(), RoundingMode.UP);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            BigIntegerMath.sqrt(null, RoundingMode.DOWN);
            fail();
        } catch (NullPointerException e) {
            // expected
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
        assertEquals(BigInteger.valueOf(40320), BigIntegerMath.factorial(8));
        assertEquals(BigInteger.valueOf(362880), BigIntegerMath.factorial(10));

        long maxLong = Long.MAX_VALUE;
        assertEquals(BigInteger.valueOf(maxLong), BigIntegerMath.factorial(Long.SIZE / java.lang.Long.NUMBER_OF_BYTES * 8));

        BigInteger bigNumber = BigInteger.ONE.shiftLeft(60);
        assertEquals(bigNumber, BigIntegerMath.factorial(60));

        try {
            BigIntegerMath.factorial(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BigIntegerMath.factorial(Long.MIN_VALUE / 2);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            BigIntegerMath.factorial(Long.MAX_VALUE / 2 + 1);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            BigIntegerMath.factorial(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testDivide() {
        BigInteger dividend = BigInteger.valueOf(12345);
        BigInteger divisor = BigInteger.valueOf(7);

        assertEquals(BigInteger.valueOf(1763), BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(1764), BigIntegerMath.divide(dividend, divisor, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(1763), BigIntegerMath.divide(dividend, divisor, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(1762), BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(1763), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(1764), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(1763), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN));

        divisor = BigInteger.ONE;
        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.UP));
        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.CEILING));
        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR));
        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP));
        assertEquals(dividend, BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN));

        divisor = BigInteger.valueOf(-7);
        assertEquals(BigInteger.valueOf(-1763), BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-1764), BigIntegerMath.divide(dividend, divisor, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(-1763), BigIntegerMath.divide(dividend, divisor, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(-1762), BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(-1763), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(-1764), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(-1763), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN));

        dividend = BigInteger.valueOf(Long.MIN_VALUE);
        divisor = BigInteger.valueOf(10);
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN));

        divisor = BigInteger.valueOf(-10);
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.CEILING));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.FLOOR));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(Long.MIN_VALUE / 10), BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_EVEN));

        try {
            BigIntegerMath.divide(BigInteger.ZERO, BigInteger.ONE, RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            BigIntegerMath.divide(null, BigInteger.ONE, RoundingMode.DOWN);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            BigIntegerMath.divide(BigInteger.ONE, null, RoundingMode.DOWN);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 1));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(3, 2));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(10, 5));
        assertEquals(BigInteger.valueOf(1297), BigIntegerMath.binomial(13, 4));
        assertEquals(BigInteger.valueOf(2111485077978050451), BigIntegerMath.binomial(25, 10));

        try {
            BigIntegerMath.binomial(-1, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BigIntegerMath.binomial(2, 13);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BigIntegerMath.binomial(2, 26);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            BigIntegerMath.binomial(null, 1);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }
}