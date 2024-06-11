package org.apache.commons.cli.test; //or the appropriate package for your project

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertEquals(true, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(5)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(11)));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertEquals(false, BigIntegerMath.isPowerOfTwo(BigInteger.ONE.negate()));
    }

    @Test
    public void testLog2WithDifferentModes() {
        BigInteger largeNumber = BigInteger.valueOf(102345678901234567890L);
        BigInteger smallNumber = BigInteger.valueOf(12);

        assertEquals(31, BigIntegerMath.log2(smallNumber, RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(smallNumber, RoundingMode.CEILING));
        assertEquals(31, BigIntegerMath.log2(smallNumber, RoundingMode.HALF_DOWN));
        assertEquals(31, BigIntegerMath.log2(smallNumber, RoundingMode.HALF_UP));
        assertEquals(31, BigIntegerMath.log2(smallNumber, RoundingMode.HALF_EVEN));
        assertEquals(31, BigIntegerMath.log2(smallNumber, RoundingMode.UNNECESSARY));

        assertEquals(31, BigIntegerMath.log2(largeNumber, RoundingMode.DOWN));
        assertEquals(32, BigIntegerMath.log2(largeNumber, RoundingMode.CEILING));
        assertEquals(32, BigIntegerMath.log2(largeNumber, RoundingMode.HALF_DOWN));
        assertEquals(32, BigIntegerMath.log2(largeNumber, RoundingMode.HALF_UP));
        assertEquals(32, BigIntegerMath.log2(largeNumber, RoundingMode.HALF_EVEN));
        assertEquals(32, BigIntegerMath.log2(largeNumber, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testLog10WithDifferentModes() {
        BigInteger number = BigInteger.valueOf(123456789);

        assertEquals(3, BigIntegerMath.log10(number, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(number, RoundingMode.CEILING));
        assertEquals(3, BigIntegerMath.log10(number, RoundingMode.HALF_DOWN));
        assertEquals(4, BigIntegerMath.log10(number, RoundingMode.HALF_UP));
        assertEquals(4, BigIntegerMath.log10(number, RoundingMode.HALF_EVEN));
        assertEquals(4, BigIntegerMath.log10(number, RoundingMode.UNNECESSARY));
    }

    @Test
    public void testSqrtWithDifferentModes() {
        BigInteger number = BigInteger.valueOf(25);

        BigInteger expectedResult = BigInteger.valueOf(5);
        BigInteger calculatedResult = BigIntegerMath.sqrt(number, RoundingMode.DOWN);
        assertEquals(expectedResult, calculatedResult);

        expectedResult = BigInteger.valueOf(6);
        calculatedResult = BigIntegerMath.sqrt(number, RoundingMode.CEILING);
        assertEquals(expectedResult, calculatedResult);

        expectedResult = BigInteger.valueOf(5).add(BigInteger.ONE);
        calculatedResult = BigIntegerMath.sqrt(number, RoundingMode.HALF_DOWN);
        assertEquals(expectedResult, calculatedResult);

        expectedResult = BigInteger.valueOf(6);
        calculatedResult = BigIntegerMath.sqrt(number, RoundingMode.HALF_UP);
        assertEquals(expectedResult, calculatedResult);

        expectedResult = BigInteger.valueOf(5).add(BigInteger.ONE);
        calculatedResult = BigIntegerMath.sqrt(number, RoundingMode.HALF_EVEN);
        assertEquals(expectedResult, calculatedResult);

        expectedResult = BigInteger.valueOf(5);
        calculatedResult = BigIntegerMath.sqrt(number, RoundingMode.UNNECESSARY);
        assertEquals(expectedResult, calculatedResult);
    }

    @Test
    public void testFactorial() {
        int n = 5;
        BigInteger expectedResult = BigInteger.valueOf(120);
        BigInteger calculatedResult = BigIntegerMath.factorial(n);
        assertEquals(expectedResult, calculatedResult);

        n = 10;
        expectedResult = BigInteger.valueOf(3628800);
        calculatedResult = BigIntegerMath.factorial(n);
        assertEquals(expectedResult, calculatedResult);

        n = 25;
        expectedResult = BigInteger.valueOf(15504000000000000000000000000000000000000);
        calculatedResult = BigIntegerMath.factorial(n);
        assertEquals(expectedResult, calculatedResult);
    }

    @Test
    public void testListProduct() {
        List<BigInteger> numbers = new ArrayList<>();
        numbers.add(BigInteger.valueOf(1));
        numbers.add(BigInteger.valueOf(2));
        numbers.add(BigInteger.valueOf(3));
        numbers.add(BigInteger.valueOf(4));

        BigInteger expectedResult = BigInteger.valueOf(24);
        BigInteger calculatedResult = BigIntegerMath.listProduct(numbers);
        assertEquals(expectedResult, calculatedResult);

        numbers.clear();
        numbers.add(BigInteger.valueOf(1));
        numbers.add(BigInteger.valueOf(2));
        numbers.add(BigInteger.valueOf(3));
        numbers.add(BigInteger.valueOf(4));
        numbers.add(BigInteger.valueOf(5));

        expectedResult = BigInteger.valueOf(120);
        calculatedResult = BigIntegerMath.listProduct(numbers);
        assertEquals(expectedResult, calculatedResult);
    }

    @Test
    public void testBinomial() {
        int n = 5;
        int k = 3;
        BigInteger expectedResult = BigInteger.valueOf(10);
        BigInteger calculatedResult = BigIntegerMath.binomial(n, k);
        assertEquals(expectedResult, calculatedResult);

        n = 10;
        k = 3;
        expectedResult = BigInteger.valueOf(364);
        calculatedResult = BigIntegerMath.binomial(n, k);
        assertEquals(expectedResult, calculatedResult);

        n = 10;
        k = 5;
        expectedResult = BigInteger.valueOf(252);
        calculatedResult = BigIntegerMath.binomial(n, k);
        assertEquals(expectedResult, calculatedResult);

        n = 10;
        k = 10;
        expectedResult = BigInteger.valueOf(362880);
        calculatedResult = BigIntegerMath.binomial(n, k);
        assertEquals(expectedResult, calculatedResult);

        n = 10;
        k = 11;
        calculatedResult = BigIntegerMath.binomial(n, k);
        assertNull(calculatedResult);
    }

}