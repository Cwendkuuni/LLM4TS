package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(Math.pow(2, 16));
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(Math.pow(2, 32) - BigInteger.ONE);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(Math.pow(3, 2));
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.ONE.negate();
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.TEN;
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2() {
        BigInteger x = BigInteger.ONE;
        int expected = 0;
        for (int i = 0; i < 31; ++i) {
            int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
            assertEquals(expected, result);
            x = x.shiftLeft(1);
            expected++;
        }

        x = BigInteger.ONE.add(BigInteger.ONE);
        expected = 32;
        for (int i = 0; i < 10; ++i) {
            int result = BigIntegerMath.log2(x, RoundingMode.UP);
            assertEquals(expected, result);
            x = x.shiftLeft(1);
            expected++;
        }

        x = BigInteger.valueOf(Math.pow(2, 63) - BigInteger.ONE);
        int maxResult = Integer.MAX_VALUE;
        int result = BigIntegerMath.log2(x, RoundingMode.HALF_UP);
        assertEquals(maxResult, result);

        x = BigInteger.ONE.negate();
        try {
            BigIntegerMath.log2(x, RoundingMode.DOWN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        x = BigInteger.valueOf(Long.MIN_VALUE);
        try {
            BigIntegerMath.log2(x, RoundingMode.UP);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLog10() {
        BigInteger x = BigInteger.ONE;
        RoundingMode mode = RoundingMode.DOWN;

        double expected = 0.0;
        for (int i = 0; i < 23; ++i) {
            int result = BigIntegerMath.log10(x, mode).intValueExact();
            assertEquals((int) Math.pow(10, expected), result);
            x = x.multiply(BigInteger.TEN);
            expected += Math.log10(10.0);
        }

        mode = RoundingMode.UP;
        x = BigInteger.ONE.add(BigInteger.ONE);
        for (int i = 0; i < 10; ++i) {
            int result = BigIntegerMath.log10(x, mode).intValueExact();
            assertEquals((int) Math.ceil(Math.log10(11.0)), result);
            x = x.multiply(BigInteger.TEN);
        }

        x = BigInteger.valueOf(Math.pow(10, 304));
        int maxResult = Integer.MAX_VALUE;
        int result = BigIntegerMath.log10(x, RoundingMode.HALF_UP).intValueExact();
        assertEquals(maxResult, result);

        x = BigInteger.ONE.negate();
        try {
            BigIntegerMath.log10(x, RoundingMode.DOWN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        x = BigInteger.valueOf(Long.MIN_VALUE);
        try {
            BigIntegerMath.log10(x, RoundingMode.UP);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSqrt() {
        BigInteger x = BigInteger.ONE;

        BigInteger expected = BigInteger.ONE;
        for (int i = 0; i < 15; ++i) {
            BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
            assertEquals(expected, result);
            x = x.multiply(BigInteger.TEN);
            expected = expected.multiply(expected).add(BigInteger.ONE).shiftRight(1);
        }

        x = BigInteger.valueOf(Long.MAX_VALUE);
        expected = new BigInteger("9223372036854775807");
        BigInteger result = BigIntegerMath.sqrt(x, RoundingMode.UP);
        assertEquals(expected, result);

        x = new BigInteger("1000000000000000000000000000000000000000000000000000000000000003");
        expected = new BigInteger("316227766016837755206392165063533269991323681428225120192586624196850101112937211220812822481283115233");
        result = BigIntegerMath.sqrt(x, RoundingMode.HALF_UP);
        assertEquals(expected, result);

        x = BigInteger.ONE.negate();
        try {
            BigIntegerMath.sqrt(x, RoundingMode.DOWN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFactorial() {
        for (int i = 0; i < 15; ++i) {
            BigInteger result = BigIntegerMath.factorial(i);
            BigInteger expected = BigInteger.valueOf(LongMath.factorials[i]);
            assertEquals(expected, result);
        }

        int n = 15;
        BigInteger result = BigIntegerMath.factorial(n);
        BigInteger expected = BigInteger.valueOf(LongMath.factorials[n]);
        for (int i = 0; i < n; ++i) {
            BigInteger x = BigIntegerMath.factorial(i);
            BigInteger y = BigIntegerMath.factorial(n - i - 1);
            result = result.multiply(x).multiply(y);
        }
        assertEquals(expected, result);

        n = 64;
        result = BigIntegerMath.factorial(n);
        expected = BigInteger.valueOf(LongMath.factorials[n]);
        for (int i = 0; i < 64; ++i) {
            BigInteger x = BigIntegerMath.factorial(i);
            result = result.multiply(x);
        }
        assertEquals(expected, result);

        try {
            BigIntegerMath.factorial(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            BigIntegerMath.factorial(Integer.MAX_VALUE + 1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBinomial() {
        BigInteger n = BigInteger.valueOf(5);
        int k = 3;
        BigInteger result = BigIntegerMath.binomial(n, k);
        BigInteger expected = BigInteger.valueOf(10);
        assertEquals(expected, result);

        n = BigInteger.valueOf(21);
        k = 11;
        result = BigIntegerMath.binomial(n, k);
        expected = new BigInteger("3221225400000000000000000000000000000000000000000000000000000000000000000000000121393225953725110000000000000000");
        assertEquals(expected, result);

        n = BigInteger.valueOf(123456);
        k = 23;
        result = BigIntegerMath.binomial(n, k);
        expected = BigInteger.ZERO;
        assertEquals(expected, result);

        n = BigInteger.valueOf(123456);
        k = 123454;
        result = BigIntegerMath.binomial(n, k);
        expected = BigInteger.ZERO;
        assertEquals(expected, result);

        n = BigInteger.valueOf(123456);
        k = 123455;
        result = BigIntegerMath.binomial(n, k);
        expected = BigInteger.valueOf(1140065272870511202237116481063956218532886282720266936400822431632152415266010313262626187023576123646323368016218526347115428711223130623119748345433388354747526395662363254882021634365718760600000000000L);
        assertEquals(expected, result);
    }

    @Test
    public void testDivide() {
        BigInteger p = BigInteger.valueOf(12345);
        BigInteger q = BigInteger.valueOf(7);
        BigInteger result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        BigInteger expected = new BigInteger("1763");
        assertEquals(expected, result);

        p = BigInteger.valueOf(Integer.MIN_VALUE);
        q = BigInteger.ONE;
        result = BigIntegerMath.divide(p, q, RoundingMode.UP);
        expected = new BigInteger("-2147483648");
        assertEquals(expected, result);

        p = BigInteger.valueOf(Integer.MIN_VALUE);
        q = BigInteger.valueOf(2);
        try {
            result = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }

        p = BigInteger.valueOf(Long.MAX_VALUE);
        q = BigInteger.VALUE_OF(12345);
        try {
            result = BigIntegerMath.divide(p, q, RoundingMode.UP);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }
}