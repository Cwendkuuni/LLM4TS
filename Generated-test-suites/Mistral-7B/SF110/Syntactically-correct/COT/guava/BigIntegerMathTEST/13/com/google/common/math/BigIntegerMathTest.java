package com.google.common.math;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        for (int i = 1; i <= 10; i++) {
            x = x.shiftLeft(1);
            assertTrue(BigIntegerMath.isPowerOfTwo(x));
        }

        BigInteger negativeNumber = BigInteger.valueOf(-1);
        assertFalse(BigIntegerMath.isPowerOfTwo(negativeNumber));

        BigInteger notPowerOfTwo = BigInteger.valueOf(3);
        assertFalse(BigIntegerMath.isPowerOfTwo(notPowerOfTwo));

        BigInteger largeNumber = BigInteger.valueOf(12345678901234567890L);
        assertFalse(BigIntegerMath.isPowerOfTwo(largeNumber));

        BigInteger numberWithMixedBits = BigInteger.valueOf(0x55555555555555555555577777777777L);
        assertFalse(BigIntegerMath.isPowerOfTwo(numberWithMixedBits));
    }

    @Test
    public void testLog2() {
        BigInteger x = BigInteger.ONE;
        int expected = 0;
        for (int i = 0; i < 31; i++) {
            int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
            assertEquals(expected, result);
            x = x.shiftLeft(1);
            expected++;
        }

        x = BigInteger.valueOf(128);
        expected = 7;
        int result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(expected, result);

        x = BigInteger.valueOf(129);
        expected = 8;
        result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(expected, result);

        x = BigInteger.valueOf(130);
        expected = 8;
        result = BigIntegerMath.log2(x, RoundingMode.UP);
        assertEquals(expected, result + 1);

        x = BigInteger.valueOf(131);
        expected = 8;
        result = BigIntegerMath.log2(x, RoundingMode.UP);
        assertEquals(expected, result + 1);

        x = BigInteger.valueOf(Long.MAX_VALUE);
        expected = 63;
        result = BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(expected, result);

        x = BigInteger.valueOf(Long.MAX_VALUE.longValue() + 1);
        expected = 64;
        result = BigIntegerMath.log2(x, RoundingMode.UP);
        assertEquals(expected, result);

        x = BigInteger.ZERO;
        expectThrows(IllegalArgumentException.class, () -> BigIntegerMath.log2(x, RoundingMode.ANY));
    }

    @Test
    public void testLog10() {
        BigInteger x = BigInteger.ONE;
        int expected = 0;
        RoundingMode roundingMode = RoundingMode.HALF_UP;
        for (int i = 0; i < 10; i++) {
            BigInteger result = BigIntegerMath.log10(x, roundingMode);
            assertEquals(expected, result.intValue());
            x = x.multiply(BigInteger.TEN);
            expected++;
        }

        x = BigInteger.valueOf(1023);
        expected = 3;
        roundingMode = RoundingMode.DOWN;
        BigInteger result = BigIntegerMath.log10(x, roundingMode);
        assertEquals(expected, result.intValue());

        x = BigInteger.valueOf(1024);
        expected = 4;
        roundingMode = RoundingMode.DOWN;
        result = BigIntegerMath.log10(x, roundingMode);
        assertEquals(expected, result.intValue());

        x = BigInteger.valueOf(1025);
        expected = 4;
        roundingMode = RoundingMode.UP;
        result = BigIntegerMath.log10(x, roundingMode);
        assertEquals(expected, result.intValue());

        x = BigInteger.valueOf(1026);
        expected = 4;
        roundingMode = RoundingMode.UP;
        result = BigIntegerMath.log10(x, roundingMode);
        assertEquals(expected, result.intValue());

        x = BigInteger.valueOf(Long.MAX_VALUE);
        expected = 33;
        roundingMode = RoundingMode.DOWN;
        result = BigIntegerMath.log10(x, roundingMode);
        assertEquals(expected, result.intValue());

        x = BigInteger.valueOf(Long.MAX_VALUE.longValue() + 1);
        expectThrows(IllegalArgumentException.class, () -> BigIntegerMath.log10(x, roundingMode));
    }

    @Test
    public void testSqrt() {
        BigInteger x = BigInteger.ONE;
        BigInteger expected = BigInteger.ONE;
        RoundingMode roundingMode = RoundingMode.HALF_UP;
        for (int i = 1; i <= 10; i++) {
            BigInteger result = BigIntegerMath.sqrt(x, roundingMode);
            assertEquals(expected, result);
            x = x.multiply(BigInteger.TEN);
        }

        x = BigInteger.valueOf(128);
        expected = BigInteger.valueOf(11);
        result = BigIntegerMath.sqrt(x, roundingMode);
        assertEquals(expected, result);

        x = BigInteger.valueOf(129);
        expected = BigInteger.valueOf(11);
        result = BigIntegerMath.sqrt(x, roundingMode);
        assertEquals(expected, result);

        x = BigInteger.valueOf(130);
        expected = BigInteger.valueOf(11);
        result = BigIntegerMath.sqrt(x, roundingMode);
        assertEquals(expected, result);

        x = BigInteger.valueOf(131);
        expected = BigInteger.valueOf(11).add(BigInteger.ONE);
        result = BigIntegerMath.sqrt(x, roundingMode);
        assertEquals(expected, result);

        x = BigInteger.valueOf(Long.MAX_VALUE);
        expected = BigInteger.valueOf(Long.MAX_VALUE).divide(BigInteger.TEN).shiftRight(1);
        result = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(expected, result);

        x = BigInteger.valueOf(Long.MAX_VALUE.longValue() + 1);
        expectThrows(IllegalArgumentException.class, () -> BigIntegerMath.sqrt(x, roundingMode));
    }

    @Test
    public void testDivide() {
        BigInteger dividend = BigInteger.ONE;
        BigInteger divisor = BigInteger.valueOf(2);
        BigInteger expected = BigInteger.valueOf(1);

        for (int i = 1; i <= 10; i++) {
            BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.HALF_UP);
            assertEquals(expected, result);
            dividend = dividend.multiply(BigInteger.TEN);
            divisor = divisor.multiply(BigInteger.TEN);
            expected = expected.multiply(BigInteger.TEN);
        }

        dividend = BigInteger.valueOf(12345678901234567890L);
        divisor = BigInteger.valueOf(1234567890L);
        expected = BigInteger.valueOf(10000000000000000001L);
        BigInteger result = BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
        assertEquals(expected, result);

        divisor = BigInteger.valueOf(1234567890L).add(BigInteger.ONE);
        result = BigIntegerMath.divide(dividend, divisor, RoundingMode.UP);
        assertEquals(expected.subtract(BigInteger.ONE), result);

        divisor = BigInteger.valueOf(1234567890L).add(BigInteger.TEN);
        result = BigIntegerMath.divide(dividend, divisor, RoundingMode.DOWN);
        assertEquals(expected, result);

        divisor = BigInteger.valueOf(1234567890L).add(BigInteger.TEN).add(BigInteger.ONE);
        result = BigIntegerMath.divide(dividend, divisor, RoundingMode.UP);
        assertEquals(expected.subtract(BigInteger.ONE), result);

        dividend = BigInteger.ZERO;
        divisor = BigInteger.ONE;
        expectThrows(ArithmeticException.class, () -> BigIntegerMath.divide(dividend, divisor, RoundingMode.ANY));

        dividend = BigInteger.ONE;
        divisor = BigInteger.ZERO;
        expectThrows(ArithmeticException.class, () -> BigIntegerMath.divide(dividend, divisor, RoundingMode.ANY));

        dividend = BigInteger.valueOf(12345678901234567890L);
        divisor = BigInteger.valueOf(12345678901234567891L);
        expectThrows(ArithmeticException.class, () -> BigIntegerMath.divide(dividend, divisor, RoundingMode.ANY));
    }

    @Test
    public void testFactorial() {
        int n = 0;
        BigInteger expected = BigInteger.ONE;
        for (int i = 0; i <= 10; i++) {
            BigInteger result = BigIntegerMath.factorial(n);
            assertEquals(expected, result);
            n++;
            expected = expected.multiply(BigInteger.TEN);
        }

        n = 11;
        expected = BigInteger.valueOf(BigDecimal.valueOf(39916800L).pow(2).longValue());
        BigInteger result = BigIntegerMath.factorial(n);
        assertEquals(expected, result);

        n = 12;
        expected = result.multiply(BigInteger.valueOf(11));
        result = BigIntegerMath.factorial(n);
        assertEquals(expected, result);

        n = 20;
        expected = result.multiply(BigInteger.valueOf(2520));
        result = BigIntegerMath.factorial(n);
        assertEquals(expected, result);

        n = 100;
        expected = result.multiply(BigInteger.valueOf(2432902008176640000));
        result = BigIntegerMath.factorial(n);
        assertEquals(expected, result);

        n = 1000;
        expected = result.multiply(BigInteger.valueOf(3041409320171337804361260816606476884437764156896051200000000000000));
        result = BigIntegerMath.factorial(n);
        assertEquals(expected, result);

        expectThrows(ArithmeticException.class, () -> BigIntegerMath.factorial(-1));
    }

    @Test
    public void testBinomial() {
        int n = 0;
        int k = 0;
        BigInteger expected = BigInteger.ONE;
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= i; j++) {
                BigInteger result = BigIntegerMath.binomial(n, j);
                assertEquals(expected, result);
                expected = expected.multiply(BigInteger.valueOf(n - j));
                expected = expected.divide(BigInteger.valueOf(j + 1));
            }
            n++;
        }

        n = 6;
        k = 0;
        expected = BigInteger.ONE;
        for (int j = 0; j <= n; j++) {
            BigInteger result = BigIntegerMath.binomial(n, j);
            assertEquals(expected, result);
            expected = expected.multiply(BigInteger.valueOf(n - j));
            expected = expected.divide(BigInteger.valueOf(j + 1));
        }

        n = 7;
        k = 3;
        expected = BigInteger.valueOf(21);
        BigInteger result = BigIntegerMath.binomial(n, k);
        assertEquals(expected, result);

        n = 100;
        k = 50;
        expected = BigInteger.ONE;
        for (int i = 50; i <= 100; i++) {
            expected = expected.multiply(BigInteger.valueOf(i - 50));
        }
        for (int i = 0; i <= 50; i++) {
            expected = expected.divide(BigInteger.valueOf(i + 1));
        }
        result = BigIntegerMath.binomial(n, k);
        assertEquals(expected, result);

        expectThrows(IllegalArgumentException.class, () -> BigIntegerMath.binomial(-1, 0));
        expectThrows(IllegalArgumentException.class, () -> BigIntegerMath.binomial(0, 1));
        expectThrows(IllegalArgumentException.class, () -> BigIntegerMath.binomial(0, 0));
        expectThrows(IllegalArgumentException.class, () -> BigIntegerMath.binomial(0, -1));
    }
}