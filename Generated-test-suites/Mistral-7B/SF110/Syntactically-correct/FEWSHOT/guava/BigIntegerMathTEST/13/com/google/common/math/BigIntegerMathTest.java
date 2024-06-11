package org.apache.commons.cli.test; // replace with your test package name if necessary

import static org.junit.Assert.*;
import com.google.common.math.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.DOWN));
        assertEquals(Integer.MAX_VALUE, BigIntegerMath.log2(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.NEGATE, RoundingMode.DOWN));
        assertNull(BigIntegerMath.log2(null, RoundingMode.DOWN));
    }

    @Test
    public void testLog2WithDifferentRoundingModes() {
        for (RoundingMode roundingMode : RoundingMode.values()) {
            testLog2WithRoundingMode(roundingMode);
        }
    }

    private void testLog2WithRoundingMode(RoundingMode roundingMode) {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, roundingMode));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), roundingMode));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), roundingMode));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), roundingMode));
        assertEquals(Integer.MAX_VALUE, BigIntegerMath.log2(BigInteger.valueOf(Long.MAX_VALUE), roundingMode));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, roundingMode));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.NEGATE, roundingMode));
        assertNull(BigIntegerMath.log2(null, roundingMode));
        // edge cases
        assertEquals(-1, BigIntegerMath.log2(BigInteger.valueOf(-1), roundingMode));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate(), roundingMode));
    }

    @Test
    public void testLog10() {
        // TODO: Implement test cases for log10 method
    }

    @Test
    public void testSqrt() {
        // TODO: Implement test cases for sqrt method
    }

    @Test
    public void testSqrtFloor() {
        // TODO: Implement test cases for sqrtFloor method
    }

    @Test
    public void testSqrtApproxWithDoubles() {
        // TODO: Implement test cases for sqrtApproxWithDoubles method
    }

    @Test
    public void testDivide() {
        // TODO: Implement test cases for divide method
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.factorial(6));
        assertEquals(BigInteger.valueOf(5040), BigIntegerMath.factorial(7));
        assertEquals(BigInteger.valueOf(40320), BigIntegerMath.factorial(8));
        assertEquals(BigInteger.valueOf(362880), BigIntegerMath.factorial(9));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(10));
        assertEquals(BigInteger.valueOf(39916800), BigIntegerMath.factorial(11));
        assertEquals(BigInteger.valueOf(479001600), BigIntegerMath.factorial(12));
        assertEquals(BigInteger.valueOf(6227020800), BigIntegerMath.factorial(13));
        assertEquals(BigInteger.valueOf(87178291200), BigIntegerMath.factorial(14));
        assertEquals(BigInteger.valueOf(1307674368000), BigIntegerMath.factorial(15));
        assertEquals(BigInteger.valueOf(20358209700160000), BigIntegerMath.factorial(16));
        assertEquals(BigInteger.valueOf(35568742809600000000L), BigIntegerMath.factorial(17));
        assertEquals(BigInteger.valueOf(622702080000000000000L), BigIntegerMath.factorial(18));
        assertEquals(BigInteger.valueOf(1216451004088320000000000L), BigIntegerMath.factorial(19));
        // large values
        assertEquals(BigInteger.valueOf(2311289092341722400000000000000L), BigIntegerMath.factorial(20));
        assertEquals(BigInteger.valueOf(51121609635720368560000000000000000L), BigIntegerMath.factorial(21));
        assertEquals(BigInteger.valueOf(112400072743171514000000000000000000000L), BigIntegerMath.factorial(22));
    }

    @Test
    public void testListProduct() {
        List<BigInteger> list = Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(4));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.listProduct(list));
        assertEquals(BigInteger.ONE, BigIntegerMath.listProduct(Collections.emptyList()));
        assertEquals(BigInteger.ONE, BigIntegerMath.listProduct(Arrays.asList(BigInteger.ONE)));
        assertEquals(BigInteger.valueOf(15), BigIntegerMath.listProduct(Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(1))));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.ZERO, BigIntegerMath.binomial(1, 2));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 0));
        assertEquals(BigInteger.TWO, BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.TWO, BigIntegerMath.binomial(2, 2));
        assertEquals(BigInteger.THREE, BigIntegerMath.binomial(3, 1));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.binomial(5, 2));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(7, 3));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.binomial(6, 5));
        assertEquals(BigInteger.valueOf(5110), BigIntegerMath.binomial(11, 6));
        assertEquals(BigInteger.valueOf(45360), BigIntegerMath.binomial(12, 7));
        assertEquals(BigInteger.valueOf(142506), BigIntegerMath.binomial(14, 8));
        assertEquals(BigInteger.valueOf(112278720), BigIntegerMath.binomial(21, 10));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.binomial(10, 5));
    }

    @Test
    public void testFitsInLong() {
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.ONE));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(123)));
        assertTrue(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(1234567890123456789L)));
        assertFalse(BigIntegerMath.fitsInLong(BigInteger.valueOf(Long.MAX_VALUE.longValue() + 1)));
    }
}