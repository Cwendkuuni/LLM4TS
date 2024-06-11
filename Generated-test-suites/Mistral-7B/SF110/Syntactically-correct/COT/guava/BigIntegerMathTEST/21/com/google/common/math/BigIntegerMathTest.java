package com.google.common.math;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(Math.pow(2, 10));
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(Math.pow(2, 32));
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(Math.pow(3, 10));
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(Math.pow(10, 3));
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.ONE.negate();
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        Preconditions.checkNull(BigIntegerMath.isPowerOfTwo(null));
    }

    @Test
    public void testLog2() {
        BigInteger x = BigInteger.ONE;

        for (RoundingMode mode : RoundingMode.values()) {
            int expected = 0;
            int actual = BigIntegerMath.log2(x, mode);
            assertEquals(expected, actual);
            x = x.multiply(BigInteger.TEN);
            expected += 1;
        }

        x = BigInteger.valueOf(Long.decode("0xffffffffffffffff"));
        for (RoundingMode mode : RoundingMode.values()) {
            int expected = 64;
            int actual = BigIntegerMath.log2(x, mode);
            assertEquals(expected, actual);
        }

        x = BigInteger.ONE.shiftLeft(256);
        RoundingMode[] roundingModes = new RoundingMode[] { RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN };
        for (RoundingMode mode : roundingModes) {
            int expectedLogFloor = 256;
            int expectedLogUp = 257;
            int actualLogFloor = BigIntegerMath.log2(x, RoundingMode.FLOOR);
            int actualLogUp = BigIntegerMath.log2(x, mode);
            assertEquals(expectedLogFloor, actualLogFloor);
            assertEquals(expectedLogUp, actualLogUp);
        }

        Preconditions.checkNegative("x", BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.UNNECESSARY));
        Preconditions.checkNull(BigIntegerMath.log2(null, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10() {
        BigInteger x = BigInteger.ONE;

        for (RoundingMode mode : RoundingMode.values()) {
            long expected = 0;
            long actual = BigIntegerMath.log10(x, mode).longValue();
            assertEquals(expected, actual);
            x = x.multiply(BigInteger.TEN);
            expected++;
        }

        x = BigInteger.valueOf(Long.decode("0xffffffffffffffff"));
        for (RoundingMode mode : RoundingMode.values()) {
            long expected = 64;
            long actual = BigIntegerMath.log10(x, mode).longValue();
            assertEquals(expected, actual);
        }

        RoundingMode[] roundingModes = new RoundingMode[] { RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN };
        for (RoundingMode mode : roundingModes) {
            long expectedLogFloor = 64;
            long expectedLogUp = 65;
            long actualLogFloor = BigIntegerMath.log10(BigInteger.valueOf(Long.decode("0xffffffffffffffff")), RoundingMode.FLOOR).longValue();
            long actualLogUp = BigIntegerMath.log10(BigInteger.valueOf(Long.decode("0xffffffffffffffff")), mode).longValue();
            assertEquals(expectedLogFloor, actualLogFloor);
            assertEquals(expectedLogUp, actualLogUp);
        }

        Preconditions.checkNegative("x", BigIntegerMath.log10(BigInteger.ONE.negate(), RoundingMode.UNNECESSARY).longValue());
        Preconditions.checkNull(BigIntegerMath.log10(null, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrt() {
        BigInteger x = BigInteger.ONE;

        for (RoundingMode mode : RoundingMode.values()) {
            BigInteger expected = x;
            BigInteger actual = BigIntegerMath.sqrt(x, mode);
            assertEquals(expected, actual);
            x = x.multiply(BigInteger.TEN);
        }

        x = BigInteger.valueOf(Long.decode("0xffffffffffffffff"));
        RoundingMode[] roundingModes = new RoundingMode[] { RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN };
        for (RoundingMode mode : roundingModes) {
            BigInteger expected = sqrtApprox(x, mode);
            BigInteger actual = BigIntegerMath.sqrt(x, mode);
            assertEquals(expected, actual);
        }

        Preconditions.checkNegative("x", BigIntegerMath.sqrt(BigInteger.ONE.negate(), RoundingMode.UNNECESSARY));
        Preconditions.checkNull(BigIntegerMath.sqrt(null, RoundingMode.UNNECESSARY));
    }

    private static BigInteger sqrtApprox(BigInteger x, RoundingMode mode) {
        BigInteger sqrtFloor = sqrtFloorApprox(x);
        BigInteger sqrt2 = sqrtFloor.add(x.divide(sqrtFloor)).shiftRight(1);
        while (!sqrtFloor.equals(sqrt2)) {
            sqrtFloor = sqrt2;
            sqrt2 = sqrtFloor.add(x.divide(sqrtFloor)).shiftRight(1);
        }
        return sqrtFloor;
    }

    private static BigInteger sqrtFloorApprox(BigInteger x) {
        int log2 = BigIntegerMath.log2(x, RoundingMode.FLOOR);
        if (log2 < 1023) {
            return sqrtApproxWithDoubles(x);
        }

        int shift = log2 - 52 & 0xFFFFFFFE;
        return sqrtApproxWithDoubles(x.shiftRight(shift)).shiftLeft(shift >> 1);
    }

    @Test
    public void testSqrtApproxWithDoubles() {
        for (RoundingMode mode : RoundingMode.values()) {
            double expected = Math.sqrt(1.0);
            double actual = DoubleUtils.bigToDouble(BigIntegerMath.sqrtApproxWithDoubles(BigInteger.ONE).toByteArray());
            assertEquals(expected, actual, 0.0);
        }

        for (RoundingMode mode : RoundingMode.values()) {
            double expected = Math.sqrt(Math.pow(2, 64));
            double actual = DoubleUtils.bigToDouble(BigIntegerMath.sqrtApproxWithDoubles(BigInteger.valueOf(Long.decode("0xffffffffffffffff"))).toByteArray());
            assertEquals(expected, actual, 0.0);
        }

        RoundingMode[] roundingModes = new RoundingMode[] { RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN };
        for (RoundingMode mode : roundingModes) {
            double expected = Math.sqrt(1.0) + 0.5;
            double actual = DoubleUtils.bigToDouble(BigIntegerMath.sqrtApproxWithDoubles(BigInteger.ONE).shiftRight(1).toByteArray());
            assertEquals(expected, actual, 0.0);

            expected = Math.sqrt(Math.pow(2, 64)) + 0.5;
            actual = DoubleUtils.bigToDouble(BigIntegerMath.sqrtApproxWithDoubles(BigInteger.valueOf(Long.decode("0xffffffffffffffff"))).shiftRight(63).toByteArray());
            assertEquals(expected, actual, 0.0);
        }

        Preconditions.checkNull(BigIntegerMath.sqrtApproxWithDoubles(null));
    }

    @Test
    public void testDivide() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(3);
        BigInteger expected = BigInteger.valueOf(3);
        BigInteger actual = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(expected, actual);

        expected = BigInteger.valueOf(4);
        actual = BigIntegerMath.divide(p, q, RoundingMode.CEILING);
        assertEquals(expected, actual);

        expected = BigInteger.valueOf(3);
        actual = BigIntegerMath.divide(p, q, RoundingMode.HALF_DOWN);
        assertEquals(expected, actual);

        expected = BigInteger.valueOf(3);
        actual = BigIntegerMath.divide(p, q, RoundingMode.HALF_UP);
        assertEquals(expected, actual);

        expected = BigInteger.valueOf(3);
        actual = BigIntegerMath.divide(p, q, RoundingMode.HALF_EVEN);
        assertEquals(expected, actual);

        Preconditions.checkArgument(p.compareTo(q) >= 0, "p (%s) < q (%s)", p, q);
        expected = BigInteger.ZERO;
        actual = BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY);
        assertEquals(expected, actual);

        Preconditions.checkArgument(p.compareTo(q) < 0, "p (%s) > q (%s)", p, q);
        expected = p;
        actual = BigIntegerMath.divide(p, q, RoundingMode.UNNECESSARY);
        assertEquals(expected, actual);

        Preconditions.checkNull(BigIntegerMath.divide(null, q, RoundingMode.UNNECESSARY));
        Preconditions.checkNull(BigIntegerMath.divide(p, null, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testFactorial() {
        for (int n = 1; n <= 10; n++) {
            BigInteger expected = BigInteger.valueOf(LongMath.factorials[n]);
            BigInteger actual = BigIntegerMath.factorial(n);
            assertEquals(expected, actual);
        }

        BigInteger x = BigInteger.valueOf(Long.decode("0xffffffffffffffff"));
        int expectedSize = IntMath.divide(x.bitLength(), 64) + 1;
        ArrayList<BigInteger> bignums = new ArrayList<BigInteger>(expectedSize);
        for (int i = 0; i < expectedSize; i++) {
            int num = i + 1;
            BigInteger actual = BigIntegerMath.factorial(num);
            assertEquals(num, actual.bitLength());
            bignums.add(actual);
        }

        BigInteger result = BigIntegerMath.listProduct(bignums);
        BigInteger expectedResult = result.shiftLeft(x.bitLength());
        assertEquals(expectedResult, result);

        Preconditions.checkArgument(n >= 0, "n (%s) < 0", n);
        Preconditions.checkNegative(n, "n (%s) > 0", n);
        Preconditions.checkNull(BigIntegerMath.factorial(n));
    }

    @Test
    public void testBinomial() {
        int n = 5;
        int k = 3;
        BigInteger expected = BigInteger.valueOf(10);
        BigInteger actual = BigIntegerMath.binomial(n, k);
        assertEquals(expected, actual);

        n = 15;
        k = 5;
        expected = BigInteger.valueOf(3003);
        actual = BigIntegerMath.binomial(n, k);
        assertEquals(expected, actual);

        n = 20;
        k = 10;
        expected = BigInteger.valueOf(1771000);
        actual = BigIntegerMath.binomial(n, k);
        assertEquals(expected, actual);

        Preconditions.checkArgument(n >= 0, "n (%s) < 0", n);
        Preconditions.checkArgument(k >= 0, "k (%s) < 0", k);
        Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", k, n);
        Preconditions.checkNull(BigIntegerMath.binomial(n, k));
    }

    @Test
    public void testFitsInLong() {
        for (BigInteger x : new BigInteger[] { BigInteger.ONE, BigInteger.valueOf(Math.pow(2, 64)), BigInteger.valueOf(Math.pow(10, 23)), BigInteger.valueOf(Math.pow(10, 3)) }) {
            boolean expected = x.bitLength() <= 63;
            boolean actual = BigIntegerMath.fitsInLong(x);
            assertEquals(expected, actual);
        }

        Preconditions.checkNull(BigIntegerMath.fitsInLong(null));
    }
}