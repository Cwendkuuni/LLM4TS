package com.google.common.math; // replace with the actual package name if different

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

        x = x.shiftLeft(10);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = x.add(BigInteger.ONE);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(-1L);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2() {
        BigInteger x = BigInteger.ONE;

        // Test log2 with positive numbers
        assertEquals(0, BigIntegerMath.log2(x, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(x.shiftLeft(1), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(x.shiftLeft(3), RoundingMode.DOWN));

        // Test log2 with negative numbers
        assertNull(BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.ANY_VALUE));
        x = BigInteger.valueOf(-1L);
        assertNull(BigIntegerMath.log2(x, RoundingMode.ANY_VALUE));

        // Test log2 with large numbers
        x = BigInteger.valueOf(Long.MAX_VALUE);
        assertEquals(63, BigIntegerMath.log2(x, RoundingMode.DOWN));

        // Test log2 with edge cases and exceptions
        x = BigInteger.valueOf(-2L);
        assertNull(BigIntegerMath.log2(x, RoundingMode.ANY_VALUE));

        x = BigInteger.TEN.pow(1024);
        assertEquals(1024, BigIntegerMath.log2(x, RoundingMode.DOWN));
    }

    @Test(expected = AssertionError.class)
    public void testLog10WithInvalidRoundingMode() {
        BigInteger x = BigInteger.TEN;
        BigIntegerMath.log10(x, RoundingMode.FIRST_UP); // Should throw an AssertionError
    }

    @Test
    public void testLog10() {
        BigInteger x = BigInteger.ONE;

        // Test log10 with small positive numbers
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(3), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.DOWN));

        // Test log10 with large numbers
        x = BigInteger.valueOf(Long.MAX_VALUE);
        assertEquals(63, BigIntegerMath.log10(x, RoundingMode.DOWN).intValue());

        // Test log10 with negative numbers
        x = BigInteger.valueOf(-1L);
        assertNull(BigIntegerMath.log10(x, RoundingMode.ANY_VALUE));

        // Test log10 with edge cases and exceptions
        x = BigInteger.ZERO;
        assertNull(BigIntegerMath.log10(x, RoundingMode.ANY_VALUE));

        x = BigInteger.TEN.pow(1024);
        assertEquals(1023, BigIntegerMath.log10(x, RoundingMode.DOWN).intValue());
    }

    @Test(expected = AssertionError.class)
    public void testSqrtWithInvalidRoundingMode() {
        BigInteger x = BigInteger.valueOf(2);
        BigIntegerMath.sqrt(x, RoundingMode.LAST_IN_EXCEEDS); // Should throw an AssertionError
    }

    @Test
    public void testSqrt() {
        BigInteger x = BigInteger.ONE;

        // Test sqrt with small positive numbers
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));

        // Test sqrt with negative numbers
        assertNull(BigIntegerMath.sqrt(BigInteger.valueOf(-1L), RoundingMode.ANY_VALUE));

        // Test sqrt with large numbers
        x = BigInteger.valueOf(Long.MAX_VALUE);
        assertEquals(x, BigIntegerMath.sqrt(x, RoundingMode.UP));

        // Test sqrt with edge cases
        x = BigInteger.ZERO;
        assertNull(BigIntegerMath.sqrt(x, RoundingMode.ANY_VALUE));

        x = BigInteger.TEN.pow(1024);
        assertEquals(x.shiftRight(52), BigIntegerMath.sqrt(x, RoundingMode.DOWN));
    }

    @Test
    public void testDivide() {
        BigInteger p = BigInteger.valueOf(1234567890123456789L);
        BigInteger q = BigInteger.valueOf(123456789);

        // Test divide with positive numbers
        assertEquals(new BigDecimal("100000000001234.56789"), new BigDecimal(BigIntegerMath.divide(p, q, RoundingMode.HALF_UP)));

        // Test divide with large numbers
        p = BigInteger.valueOf(Long.MAX_VALUE);
        q = BigInteger.ONE;
        assertEquals(p, BigIntegerMath.divide(p, q, RoundingMode.UP));

        // Test divide with negative numbers
        p = BigInteger.valueOf(-1234567890123456789L);
        q = BigInteger.valueOf(-123456789);
        assertEquals(new BigDecimal("-999999999991234.56789"), new BigDecimal(BigIntegerMath.divide(p, q, RoundingMode.HALF_UP)));

        // Test divide with edge cases and exceptions
        p = BigInteger.ZERO;
        assertNull(BigIntegerMath.divide(p, q, RoundingMode.ANY_VALUE));

        p = BigInteger.valueOf(Long.MIN_VALUE);
        assertNull(BigIntegerMath.divide(p, q, RoundingMode.ANY_VALUE));
    }

    @Test
    public void testFactorial() {
        for (int n = 0; n <= 10; ++n) {
            assertEquals(LongMath.factorials[n], BigIntegerMath.factorial(n).longValue());
        }

        int n = 11;
        BigInteger result = BigIntegerMath.factorial(n);
        long expected = LongMath.factorials[n - 1];
        assertEquals(expected, result.longValue());

        n = 12;
        result = BigIntegerMath.factorial(n);
        expected *= n;
        assertNotEquals(expected, result.longValue());

        // Test factorial with edge cases
        assertNull(BigIntegerMath.factorial(-1));

        n = 1024;
        result = BigIntegerMath.factorial(n);
        expected *= Long.MAX_VALUE;
        assertNotEquals(expected, result.longValue());
    }
}