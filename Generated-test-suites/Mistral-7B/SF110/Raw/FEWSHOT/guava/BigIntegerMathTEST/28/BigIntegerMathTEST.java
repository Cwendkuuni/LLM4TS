package org.apache.commons.cli.test; // Assuming you want to test the package as "org.apache.commons.cli.test"

import static org.junit.Assert.*;

import com.google.common.math.BigIntegerMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.ONE.add(BigInteger.ONE)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(15)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(20)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UNNECESSARY));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(27), RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(64), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(31), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(65), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(65), RoundingMode.CEILING));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(65), RoundingMode.HALF_UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(65), RoundingMode.HALF_EVEN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.valueOf(1), RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.CEILING));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.HALF_UP));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.HALF_EVEN));
        assertEquals(Long.MAX_VALUE, BigIntegerMath.log2(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.HALF_DOWN));
    }

    @Test(expected = AssertionError.class)
    public void testLog2InvalidMode() {
        BigIntegerMath.log2(BigInteger.ONE, RoundingMode.INCOMPATIBLE);
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UNNECESSARY));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UNNECESSARY));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UNNECESSARY));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(3), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(27), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(31), RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(31), RoundingMode.HALF_UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(31), RoundingMode.HALF_EVEN));
        assertEquals(7, BigIntegerMath.log10(BigInteger.valueOf(1000000), RoundingMode.DOWN));
        assertEquals(8, BigIntegerMath.log10(BigInteger.valueOf(100000000), RoundingMode.DOWN));
        assertEquals(9, BigIntegerMath.log10(BigInteger.valueOf(1000000000), RoundingMode.DOWN));
        assertEquals(Long.MAX_VALUE, BigIntegerMath.log10(BigInteger.valueOf(Long.MAX_VALUE), RoundingMode.HALF_DOWN));
    }

    @Test(expected = AssertionError.class)
    public void testLog10InvalidMode() {
        BigIntegerMath.log10(BigInteger.ONE, RoundingMode.INCOMPATIBLE);
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(BigInteger.valueOf(16), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(BigInteger.valueOf(25), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(8), BigIntegerMath.sqrt(BigInteger.valueOf(64), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(13), BigIntegerMath.sqrt(BigInteger.valueOf(169), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(16), BigIntegerMath.sqrt(BigInteger.valueOf(256), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(17), BigIntegerMath.sqrt(BigInteger.valueOf(289), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(1000000), BigIntegerMath.sqrt(BigInteger.valueOf(1000000000000L), RoundingMode.HALF_UP));
        assertEquals(BigInteger.valueOf(999999999833L), BigIntegerMath.sqrt(BigInteger.valueOf(9999999998332656041L), RoundingMode.HALF_UP));
    }

    @Test(expected = AssertionError.class)
    public void testSqrtInvalidMode() {
        BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.INCOMPATIBLE);
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
        assertEquals(BigInteger.valueOf(362880), BigIntegerMath.factorial(9));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(10));
        assertEquals(BigInteger.valueOf(39916800), BigIntegerMath.factorial(12));
        assertEquals(BigInteger.valueOf(479001600), BigIntegerMath.factorial(14));
        assertEquals(BigInteger.valueOf(6227020800), BigIntegerMath.factorial(15));
        assertEquals(BigInteger.valueOf(87178291200L), BigIntegerMath.factorial(18));
        assertEquals(BigInteger.valueOf(1307674368000L), BigIntegerMath.factorial(20));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(2, 2));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(3, 1));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.binomial(3, 2));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.binomial(3, 3));
        assertEquals(BigInteger.valueOf(12), BigIntegerMath.binomial(4, 2));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.binomial(4, 3));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.binomial(4, 4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.binomial(6, 3));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.binomial(6, 5));
        assertEquals(BigInteger.valueOf(5040), BigIntegerMath.binomial(7, 3));
        assertEquals(BigInteger.valueOf(210), BigIntegerMath.binomial(8, 3));
        assertEquals(BigInteger.valueOf(40320), BigIntegerMath.binomial(9, 4));
        assertEquals(BigInteger.valueOf(1207274400000L), BigIntegerMath.binomial(20, 10));
        assertEquals(BigInteger.valueOf(3041409320171296000L), BigIntegerMath.binomial(30, 15));
    }

    @Test
    public void testDivide() {
        assertEquals(BigInteger.ZERO, BigIntegerMath.divide(BigInteger.ZERO, BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(BigInteger.valueOf(4), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(-1), BigIntegerMath.divide(BigInteger.valueOf(-4), BigInteger.valueOf(2), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.ONE, BigIntegerMath.divide(BigInteger.valueOf("123456789012345678901234567890"), BigInteger.valueOf("1234567890"), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(-2), BigIntegerMath.divide(BigInteger.valueOf("-123456789012345678901234567890"), BigInteger.valueOf("1234567890"), RoundingMode.UNNECESSARY));
        assertEquals(BigInteger.valueOf(100), BigIntegerMath.divide(BigInteger.valueOf("123456789012345678901234567890"), BigInteger.valueOf(12345), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(99), BigIntegerMath.divide(BigInteger.valueOf("123456789012345678901234567890"), BigInteger.valueOf(12350), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(49), BigIntegerMath.divide(BigInteger.valueOf("123456789012345678901234567890"), BigInteger.valueOf(2470), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-50), BigIntegerMath.divide(BigInteger.valueOf("-123456789012345678901234567890"), BigInteger.valueOf(2465), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.divide(BigInteger.valueOf("123456789012345678901234567890"), BigInteger.valueOf(123456789012345678901234567890), RoundingMode.UNNECESSARY));
    }

    @Test
    public void testListProduct() {
        List<BigInteger> list = Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.listProduct(list));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.listProduct(list, 1, 3));
        list = Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(4));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.listProduct(list));
        list = Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(3), BigInteger.valueOf(3));
        assertEquals(BigInteger.valueOf(27), BigIntegerMath.listProduct(list));
        list = Arrays.asList(BigInteger.valueOf(1));
        assertEquals(BigInteger.ONE, BigIntegerMath.listProduct(list));
        list = Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(1));
        assertEquals(BigInteger.ONE, BigIntegerMath.listProduct(list));
        list = Arrays.asList(BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(1), BigInteger.valueOf(1));
        assertEquals(BigInteger.ONE, BigIntegerMath.listProduct(list));
        list = Arrays.asList(BigInteger.valueOf(-1), BigInteger.valueOf(-2), BigInteger.valueOf(-3), BigInteger.valueOf(-4));
        assertEquals(BigInteger.valueOf(-24), BigIntegerMath.listProduct(list));
    }
}