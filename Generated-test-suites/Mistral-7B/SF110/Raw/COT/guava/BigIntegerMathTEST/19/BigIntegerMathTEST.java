package com.google.common.math; // update this package name if necessary

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Test positive cases
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        
        // Test negative cases
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.negate()));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-16)));
        
        // Test edge cases
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.TEN));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2).pow(1024)));
    }

    @Test
    public void testLog2WithValidInputsAndDownRounding() {
        // Test positive cases
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        
        // Test negative cases
        try {
            BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test edge cases
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483647), RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.DOWN));
    }

    @Test
    public void testLog2WithValidInputsAndUpRounding() {
        // Test positive cases
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
        
        // Test negative cases
        try {
            BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.UP);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test edge cases
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(2147483648), RoundingMode.UP));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(2).pow(1024), RoundingMode.UP));
    }

    @Test
    public void testLog2WithValidInputsAndHalfDownRounding() {
        // Test positive cases
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.HALF_DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.HALF_DOWN));
        
        // Test negative cases
        try {
            BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.HALF_DOWN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test edge cases
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483647), RoundingMode.HALF_DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.HALF_DOWN));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(2147483648), RoundingMode.HALF_DOWN));
    }

    @Test
    public void testLog2WithValidInputsAndHalfUpRounding() {
        // Test positive cases
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.HALF_UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.HALF_UP));
        
        // Test negative cases
        try {
            BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.HALF_UP);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test edge cases
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(2147483648), RoundingMode.HALF_UP));
        assertEquals(33, BigIntegerMath.log2(BigInteger.valueOf(2).pow(1024), RoundingMode.HALF_UP));
    }

    @Test
    public void testLog2WithValidInputsAndHalfEvenRounding() {
        // Test positive cases
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.HALF_EVEN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.HALF_EVEN));
        
        // Test negative cases
        try {
            BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.HALF_EVEN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test edge cases
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(2147483647), RoundingMode.HALF_EVEN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.One.negate(), RoundingMode.HALF_EVEN));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(2147483648), RoundingMode.HALF_EVEN));
    }

    @Test(expected = AssertionError.class)
    public void testLog2WithInvalidRoundingMode() {
        BigIntegerMath.log2(BigInteger.ONE, RoundingMode.CHOP); // Unsupported rounding mode
    }

    @Test
    public void testLog10WithValidInputsAndDownRounding() {
        // Test positive cases
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        
        // Test edge cases
        try {
            BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.DOWN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLog10WithValidInputsAndUpRounding() {
        // Test positive cases
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UP));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UP));
        
        // Test negative cases
        try {
            BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.UP);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test edge cases
        try {
            BigIntegerMath.log10(BigInteger.valueOf(Integer.MIN_VALUE), RoundingMode.UP);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testLog10WithValidInputsAndHalfDownRounding() {
        // Test positive cases
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.HALF_DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.HALF_DOWN));
        
        // Test negative cases
        try {
            BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.HALF_DOWN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test edge cases
        try {
            BigIntegerMath.log10(BigInteger.valueOf(Integer.MIN_VALUE), RoundingMode.HALF_DOWN);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testLog10WithValidInputsAndHalfUpRounding() {
        // Test positive cases
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.HALF_UP));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.HALF_UP));
        
        // Test negative cases
        try {
            BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.HALF_UP);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test edge cases
        try {
            BigIntegerMath.log10(BigInteger.valueOf(Integer.MIN_VALUE), RoundingMode.HALF_UP);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testLog10WithValidInputsAndHalfEvenRounding() {
        // Test positive cases
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.HALF_EVEN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.HALF_EVEN));
        
        // Test negative cases
        try {
            BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.HALF_EVEN);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test edge cases
        try {
            BigIntegerMath.log10(BigInteger.valueOf(Integer.MIN_VALUE), RoundingMode.HALF_EVEN);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test(expected = AssertionError.class)
    public void testLog10WithInvalidRoundingMode() {
        BigIntegerMath.log10(BigInteger.ONE, RoundingMode.CHOP); // Unsupported rounding mode
    }

    @Test
    public void testSqrtWithValidInputsAndDownRounding() {
        // Test positive cases
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.DOWN));
        
        // Test negative cases
        try {
            BigIntegerMath.sqrt(BigInteger.NEGATE_ONE, RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
        
        // Test edge cases
        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSqrtWithValidInputsAndUpRounding() {
        // Test positive cases
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UP));
        
        // Test negative cases
        try {
            BigIntegerMath.sqrt(BigInteger.NEGATE_ONE, RoundingMode.UP);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
        
        // Test edge cases
        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.UP);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSqrtWithValidInputsAndHalfDownRounding() {
        // Test positive cases
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.HALF_DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.HALF_DOWN));
        
        // Test negative cases
        try {
            BigIntegerMath.sqrt(BigInteger.NEGATE_ONE, RoundingMode.HALF_DOWN);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
        
        // Test edge cases
        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.HALF_DOWN);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSqrtWithValidInputsAndHalfUpRounding() {
        // Test positive cases
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.HALF_UP));
        
        // Test negative cases
        try {
            BigIntegerMath.sqrt(BigInteger.NEGATE_ONE, RoundingMode.HALF_UP);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
        
        // Test edge cases
        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.HALF_UP);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void testSqrtWithValidInputsAndHalfEvenRounding() {
        // Test positive cases
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.HALF_EVEN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.HALF_EVEN));
        
        // Test negative cases
        try {
            BigIntegerMath.sqrt(BigInteger.NEGATE_ONE, RoundingMode.HALF_EVEN);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
        
        // Test edge cases
        try {
            BigIntegerMath.sqrt(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.HALF_EVEN);
            fail();
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test(expected = ArithmeticException.class)
    public void testSqrtWithInvalidInput() {
        BigIntegerMath.sqrt(BigInteger.valueOf(-1), RoundingMode.HALF_UP); // Negative number
    }

    @Test
    public void testFactorial() {
        // Test positive cases
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1 * 2 * 3), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(1 * 2 * 3 * 4 * 5), BigIntegerMath.factorial(5));
        
        // Test negative cases
        try {
            BigIntegerMath.factorial(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test edge cases
        assertEquals(BigInteger.valueOf(243290200817664), BigIntegerMath.factorial(13));
        assertEquals(BigInteger.valueOf("1307674368000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), BigIntegerMath.factorial(20));
    }
}