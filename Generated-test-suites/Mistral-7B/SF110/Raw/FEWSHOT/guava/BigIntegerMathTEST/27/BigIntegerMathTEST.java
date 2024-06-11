package org.apache.commons.cli.test; // or the appropriate package name for your test class

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.math.BigIntegerMath;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.ONE.negate()));
    }

    @Test
    public void testLog2() {
        assertEquals(Integer.SIZE - 2, BigIntegerMath.log2(BigInteger.valueOf((1 << (Integer.SIZE - 2)) - 1), RoundingMode.FLOOR));
        assertEquals(Integer.SIZE - 1, BigIntegerMath.log2(BigInteger.valueOf(1 << (Integer.SIZE - 1)), RoundingMode.FLOOR));
        assertEquals(Integer.SIZE, BigIntegerMath.log2(BigInteger.ONE.shiftLeft(Integer.SIZE), RoundingMode.UNNECESSARY));

        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UP));

        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(5), RoundingMode.HALF_UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(6), RoundingMode.HALF_UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(7), RoundingMode.HALF_UP));
        assertEquals(1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.HALF_UP));

        // edge cases
        assertEquals(0, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(Integer.SIZE - 1, BigIntegerMath.log2(BigInteger.ONE.shiftRight(1), RoundingMode.DOWN));
        assertEquals(Integer.SIZE, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));

        assertEquals(Integer.MAX_VALUE, BigIntegerMath.log2(BigInteger.ONE.shiftLeft(Integer.MAX_VALUE), RoundingMode.UNNECESSARY));
    }

    @Test(expected = AssertionError.class)
    public void testLog2WithInvalidMode() {
        BigIntegerMath.log2(BigInteger.ONE, new String("INVALID_ROUNDING_MODE"));
    }

    @Test
    public void testLog10() {
        // TODO: Implement test cases for BigIntegerMath.log10()
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UP));

        // edge cases
        assertNull(BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.ANY));
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));

        // check that sqrt(n) < n for n < 0
        BigInteger n = BigInteger.valueOf(-1);
        BigInteger sqrt = BigIntegerMath.sqrt(n, RoundingMode.CEILING);
        assertFalse(sqrt.compareTo(n) >= 0);
    }

    @Test(expected = AssertionError.class)
    public void testSqrtWithInvalidMode() {
        BigIntegerMath.sqrt(BigInteger.ONE, new String("INVALID_ROUNDING_MODE"));
    }

    @Test
    public void testFactorial() {
        for (int i = 0; i <= 12; ++i) {
            assertEquals(LongMath.factorials[i], BigIntegerMath.factorial(i));
        }

        // test large numbers
        assertEquals(BigInteger.valueOf(factorial(13)), BigIntegerMath.factorial(13));
        assertEquals(BigInteger.valueOf(factorial(14)), BigIntegerMath.factorial(14));
    }

    private static int factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        // implementation of factorial up to Long.MAX_VALUE
        // ...
        return LongMath.factorials[n];
    }
}