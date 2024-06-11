package com.google.common.math; // package name should match the class

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = x.shiftLeft(10);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = x.add(BigInteger.ONE);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = x.shiftRight(1);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = new BigInteger("-1");
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = new BigInteger("-2");
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = new BigInteger("-3");
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }

    @Test
    public void testLog2WithValidInputs() {
        for (int i = 0; i < 32; i++) {
            BigInteger x = BigInteger.valueOf(1L << i);
            int expectedLog2 = i;
            int actualLog2 = BigIntegerMath.log2(x, RoundingMode.DOWN);
            assertEquals(expectedLog2, actualLog2);
        }

        BigInteger x = new BigInteger("1000000000000000000");
        int expectedLog2 = 33;
        int actualLog2 = BigIntegerMath.log2(x, RoundingMode.UP);
        assertEquals(expectedLog2, actualLog2);

        x = x.add(BigInteger.ONE);
        int actualLog2WithOne = BigIntegerMath.log2(x, RoundingMode.UP);
        assertEquals(expectedLog2 + 1, actualLog2WithOne);
    }

    @Test(expected = AssertionError.class)
    public void testLog2WithInvalidInputs() {
        BigInteger x = new BigInteger("-1");
        BigIntegerMath.log2(x, RoundingMode.DOWN);

        x = new BigInteger("-2");
        BigIntegerMath.log2(x, RoundingMode.DOWN);
    }

    @Test
    public void testLog10WithValidInputs() {
        BigInteger x = BigInteger.ONE;
        double expectedLog10 = 0.0;
        double actualLog10 = BigIntegerMath.LN_10 * BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(expectedLog10, actualLog10, 1e-15);

        x = x.shiftLeft(10);
        expectedLog10 = Math.log10(10000000000L);
        actualLog10 = BigIntegerMath.LN_10 * BigIntegerMath.log2(x, RoundingMode.DOWN);
        assertEquals(expectedLog10, actualLog10, 1e-15);

        x = x.add(BigInteger.ONE);
        expectedLog10 = Math.log10(10000000001L);
        actualLog10 = BigIntegerMath.LN_10 * BigIntegerMath.log2(x, RoundingMode.UP);
        assertEquals(expectedLog10, actualLog10, 1e-15);
    }

    @Test(expected = AssertionError.class)
    public void testLog10WithNegativeInputs() {
        BigInteger x = new BigInteger("-1");
        BigIntegerMath.log10(x, RoundingMode.DOWN);
    }

    @Test
    public void testSqrtWithValidInputs() {
        BigInteger x = BigInteger.ONE;
        BigInteger expectedSqrt = BigInteger.ONE;
        BigInteger actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(expectedSqrt, actualSqrt);

        x = BigInteger.valueOf(2 * 2 * 2 * 2 * 2 * 2 * 2 * 2);
        expectedSqrt = x.shiftRight(1);
        actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(expectedSqrt, actualSqrt);

        x = new BigInteger("1234567890123456789012345678901234567890");
        expectedSqrt = new BigInteger("111111111111111111111111111111111");
        actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.DOWN);
        assertEquals(expectedSqrt, actualSqrt);

        x = new BigInteger("11111111111111111111111111111111111111111");
        expectedSqrt = x.shiftRight(1);
        actualSqrt = BigIntegerMath.sqrt(x, RoundingMode.UP);
        assertEquals(expectedSqrt, actualSqrt);
    }

    @Test(expected = AssertionError.class)
    public void testSqrtWithNegativeInputs() {
        BigInteger x = new BigInteger("-1");
        BigIntegerMath.sqrt(x, RoundingMode.DOWN);
    }

    @Test
    public void testDivideWithValidInputs() {
        BigInteger p = BigInteger.valueOf(10);
        BigInteger q = BigInteger.valueOf(2);
        BigInteger expectedDivide = BigInteger.valueOf(5);
        BigInteger actualDivide = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(expectedDivide, actualDivide);

        p = BigInteger.valueOf(100);
        q = BigInteger.valueOf(10);
        expectedDivide = BigInteger.valueOf(10);
        actualDivide = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(expectedDivide, actualDivide);

        p = new BigInteger("1234567890123456789012345678901234567890");
        q = new BigInteger("1234567890");
        expectedDivide = new BigInteger("999999999");
        actualDivide = BigIntegerMath.divide(p, q, RoundingMode.DOWN);
        assertEquals(expectedDivide, actualDivide);

        q = BigInteger.valueOf(0);
        try {
            BigIntegerMath.divide(p, q, RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }

        q = new BigInteger("-1");
        try {
            BigIntegerMath.divide(p, q, RoundingMode.DOWN);
            fail();
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testFactorialWithValidInputs() {
        BigInteger expectedFactorial = BigInteger.ONE;
        BigInteger actualFactorial = BigIntegerMath.factorial(0);
        assertEquals(expectedFactorial, actualFactorial);

        expectedFactorial = BigInteger.valueOf(120);
        actualFactorial = BigIntegerMath.factorial(5);
        assertEquals(expectedFactorial, actualFactorial);

        expectedFactorial = BigInteger.valueOf(511200000000000L);
        actualFactorial = BigIntegerMath.factorial(15);
        assertEquals(expectedFactorial, actualFactorial);

        expectedFactorial = BigInteger.valueOf(479001600000000000000L);
        actualFactorial = BigIntegerMath.factorial(20);
        assertEquals(expectedFactorial, actualFactorial);
    }

    @Test
    public void testBinomialWithValidInputs() {
        BigInteger expectedBinomial = BigInteger.ONE;
        BigInteger actualBinomial = BigIntegerMath.binomial(0, 0);
        assertEquals(expectedBinomial, actualBinomial);

        expectedBinomial = BigInteger.valueOf(1);
        actualBinomial = BigIntegerMath.binomial(1, 0);
        assertEquals(expectedBinomial, actualBinomial);

        expectedBinomial = BigInteger.valueOf(2);
        actualBinomial = BigIntegerMath.binomial(1, 1);
        assertEquals(expectedBinomial, actualBinomial);

        expectedBinomial = BigInteger.valueOf(6);
        actualBinomial = BigIntegerMath.binomial(6, 3);
        assertEquals(expectedBinomial, actualBinomial);

        expectedBinomial = BigInteger.valueOf(210);
        actualBinomial = BigIntegerMath.binomial(15, 5);
        assertEquals(expectedBinomial, actualBinomial);

        expectedBinomial = BigInteger.valueOf(2432902008176640000L);
        actualBinomial = BigIntegerMath.binomial(31, 10);
        assertEquals(expectedBinomial, actualBinomial);

        expectedBinomial = BigInteger.valueOf(3628800L);
        actualBinomial = BigIntegerMath.binomial(10, 5);
        assertEquals(expectedBinomial, actualBinomial);
    }
}

// 
// The complete test file should start with this comment for JUnit 4
// 