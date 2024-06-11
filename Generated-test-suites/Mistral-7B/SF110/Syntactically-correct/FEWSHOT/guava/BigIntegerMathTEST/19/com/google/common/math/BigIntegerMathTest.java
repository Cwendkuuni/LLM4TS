package org.apache.commons.cli.test; // or the correct package name for your test class

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(15)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(BigInteger.valueOf(2), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(256), RoundingMode.HALF_UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.HALF_DOWN));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(65536), RoundingMode.HALF_DOWN));
        assertEquals(32, BigIntegerMath.log2(BigInteger.valueOf(4194304), RoundingMode.HALF_DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(BigInteger.ONE.negate(), RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(BigInteger.valueOf(-1L), RoundingMode.UP));
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(10000), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log10(BigInteger.valueOf(100000), RoundingMode.UP));
        assertEquals(6, BigIntegerMath.log10(BigInteger.valueOf(1000000), RoundingMode.UP));
        assertEquals(7, BigIntegerMath.log10(BigInteger.valueOf(10000000), RoundingMode.UP));
        assertEquals(-1, BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log10(BigInteger.ONE.negate(), RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log10(BigInteger.valueOf(-1L), RoundingMode.UP));
    }

    @Test
    public void testSqrt() {
        assertEquals(BigInteger.ONE, BigIntegerMath.sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(BigInteger.valueOf(9), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(16), BigIntegerMath.sqrt(BigInteger.valueOf(256), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(64), BigIntegerMath.sqrt(BigInteger.valueOf(4096), RoundingMode.UP));
        assertEquals(BigInteger.valueOf(-1), BigIntegerMath.sqrt(BigInteger.ONE.negate(), RoundingMode.UP));
        assertEquals(null, BigIntegerMath.sqrt(BigInteger.ZERO, RoundingMode.DOWN));
    }

    @Test
    public void testFactorial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.factorial(0));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.factorial(1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.factorial(2));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.factorial(3));
        assertEquals(BigInteger.valueOf(24), BigIntegerMath.factorial(4));
        assertEquals(BigInteger.valueOf(120), BigIntegerMath.factorial(5));
        assertEquals(BigInteger.valueOf(720), BigIntegerMath.factorial(6));
        assertEquals(BigInteger.valueOf(5040), BigIntegerMath.factorial(7));
        assertEquals(BigInteger.valueOf(40320), BigIntegerMath.factorial(8));
        assertEquals(BigInteger.valueOf(362880), BigIntegerMath.factorial(9));
        assertEquals(BigInteger.valueOf(3628800), BigIntegerMath.factorial(10));
        assertEquals(BigInteger.valueOf(479001600), BigIntegerMath.factorial(12));
        assertEquals(BigInteger.valueOf(6227020800), BigIntegerMath.factorial(15));
        assertEquals(BigInteger.valueOf(1307674368000), BigIntegerMath.factorial(17));
        assertEquals(BigInteger.valueOf(2835816708884757270000000000L), BigIntegerMath.factorial(20));
    }

    @Test
    public void testBinomial() {
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(6, 3));
        assertEquals(BigInteger.valueOf(20), BigIntegerMath.binomial(10, 5));
        assertEquals(BigInteger.valueOf(511), BigIntegerMath.binomial(20, 10));
        assertEquals(BigInteger.valueOf(19683), BigIntegerMath.binomial(21, 11));
        assertEquals(BigInteger.valueOf(65578708450), BigIntegerMath.binomial(25, 10));
        assertEquals(BigInteger.valueOf(3792222223330985984000000), BigIntegerMath.binomial(50, 25));
    }

    @Test
    public void testListProduct() {
        ArrayList<BigInteger> list = new ArrayList<>();
        list.add(BigInteger.valueOf(1));
        list.add(BigInteger.valueOf(2));
        list.add(BigInteger.valueOf(3));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.listProduct(list));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.listProduct(list, 1, 3));
        assertEquals(BigInteger.valueOf(108), BigIntegerMath.listProduct(list, 0, 3));

        list.clear();
        list.add(BigInteger.valueOf(5));
        list.add(BigInteger.valueOf(7));
        list.add(BigInteger.valueOf(11));
        list.add(BigInteger.valueOf(13));
        assertEquals(BigInteger.valueOf(19683), BigIntegerMath.listProduct(list));
    }
}