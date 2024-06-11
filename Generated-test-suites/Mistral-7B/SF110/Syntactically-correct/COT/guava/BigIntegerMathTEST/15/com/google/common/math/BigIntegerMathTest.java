package com.google.common.math.test;

import static org.junit.Assert.*;

import com.google.common.base.*;
import com.google.common.math.*;
import java.math.*;
import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(5)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(13)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(17)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(15), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(27), RoundingMode.HALF_UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(28), RoundingMode.HALF_UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(32), RoundingMode.HALF_UP));

        try {
            BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.ANY_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            BigIntegerMath.log2(BigInteger.NEGATIVE_ONE, RoundingMode.ANY_VALUE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // Add more test cases for log10, sqrt, and other methods following the same pattern
}