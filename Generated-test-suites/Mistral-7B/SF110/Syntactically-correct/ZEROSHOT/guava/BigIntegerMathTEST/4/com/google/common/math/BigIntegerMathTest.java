package com.google.common.math;

import static org.junit.Assert.*;

import com.google.common.base.*;
import org.junit.*;

import java.math.*;
import java.util.*;

public class BigIntegerMathTest {

    @BeforeClass
    public static void setup() {
        BigIntegerMath.SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
        BigIntegerMath.LN_10 = Math.log(10.0);
        BigIntegerMath.LN_2 = Math.log(2.0);
    }

    @Test
    public void testIsPowerOfTwo() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger four = two.multiply(two);
        BigInteger sixteen = four.multiply(four);
        BigInteger thirtyTwo = sixteen.multiply(two);
        BigInteger oneHundred = BigInteger.valueOf(100);
        BigInteger negativeTwo = BigInteger.valueOf(-2);
        BigInteger negativeOne = BigInteger.ONE.negate();
        BigInteger negativeFifteen = BigInteger.valueOf(-15);

        assertTrue(BigIntegerMath.isPowerOfTwo(two));
        assertTrue(BigIntegerMath.isPowerOfTwo(four));
        assertTrue(BigIntegerMath.isPowerOfTwo(sixteen));
        assertTrue(BigIntegerMath.isPowerOfTwo(thirtyTwo));
        assertFalse(BigIntegerMath.isPowerOfTwo(oneHundred));
        assertFalse(BigIntegerMath.isPowerOfTwo(negativeTwo));
        assertFalse(BigIntegerMath.isPowerOfTwo(negativeOne));
        assertFalse(BigIntegerMath.isPowerOfTwo(negativeFifteen));
    }

    @Test
    public void testLog2() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger four = two.multiply(two);
        BigInteger sixteen = four.multiply(four);
        BigInteger thirtyTwo = sixteen.multiply(two);
        BigInteger oneHundred = BigInteger.valueOf(100);
        BigInteger negativeTwo = BigInteger.valueOf(-2);
        BigInteger negativeOne = BigInteger.ONE.negate();
        BigInteger negativeFifteen = BigInteger.valueOf(-15);

        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log2(two, RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log2(four, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(sixteen, RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log2(thirtyTwo, RoundingMode.DOWN));
        assertEquals(6, BigIntegerMath.log2(oneHundred, RoundingMode.DOWN));
        assertEquals(31, BigIntegerMath.log2(oneHundred, RoundingMode.UP));
        assertEquals(32, BigIntegerMath.log2(thirtyTwo, RoundingMode.UP));
        assertEquals(33, BigIntegerMath.log2(oneHundred.add(BigInteger.ONE), RoundingMode.UP));
        assertEquals(-1, BigIntegerMath.log2(negativeTwo, RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log2(negativeOne, RoundingMode.UNNECESSARY));
        assertEquals(-4, BigIntegerMath.log2(negativeFifteen, RoundingMode.DOWN));
    }

    @Test(expected = AssertionError.class)
    public void testLog2InvalidMode() {
        BigInteger x = BigInteger.valueOf(10);
        BigIntegerMath.log2(x, new RoundingMode() {});
    }

    @Test
    public void testLog10() {
        BigInteger ten = BigInteger.TEN;
        BigInteger hundred = ten.multiply(ten);
        BigInteger thousand = hundred.multiply(ten);
        BigInteger tenThousand = thousand.multiply(ten);
        BigInteger oneMillion = tenThousand.multiply(ten);
        BigInteger negativeFive = BigInteger.valueOf(-5);
        BigInteger negativeSeven = BigInteger.valueOf(-7);
        BigInteger negativeTenThousand = negativeTenThousand.negate();

        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(ten, RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(hundred, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log10(thousand, RoundingMode.DOWN));
        assertEquals(4, BigIntegerMath.log10(tenThousand, RoundingMode.DOWN));
        assertEquals(6, BigIntegerMath.log10(oneMillion, RoundingMode.DOWN));
        assertEquals(7, BigIntegerMath.log10(oneMillion.add(BigInteger.ONE), RoundingMode.UP));
        assertEquals(-3, BigIntegerMath.log10(negativeTenThousand, RoundingMode.DOWN));
        assertEquals(-2, BigIntegerMath.log10(negativeFive, RoundingMode.DOWN));
        assertEquals(-1, BigIntegerMath.log10(negativeSeven, RoundingMode.UP));
    }

    @Test(expected = AssertionError.class)
    public void testLog10InvalidMode() {
        BigInteger x = BigInteger.valueOf(10);
        BigIntegerMath.log10(x, new RoundingMode() {});
    }

    @Test
    public void testSqrt() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger three = BigInteger.valueOf(3);
        BigInteger four = two.multiply(two);
        BigInteger nine = three.multiply(three);
        BigInteger sixteen = four.multiply(two);
        BigInteger twentyFive = sixteen.multiply(two).add(BigInteger.ONE);
        BigInteger negativeSixteen = sixteen.negate();
        BigInteger negativeFortyNine = negativeSixteen.multiply(three).negate();

        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(two, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), BigIntegerMath.sqrt(three, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.sqrt(four, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(5), BigIntegerMath.sqrt(twentyFive, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(4), BigIntegerMath.sqrt(sixteen, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(7), BigIntegerMath.sqrt(negativeFortyNine, RoundingMode.UP));
    }

    @Test(expected = AssertionError.class)
    public void testSqrtInvalidMode() {
        BigInteger x = BigInteger.valueOf(10);
        BigIntegerMath.sqrt(x, new RoundingMode() {});
    }

    @Test
    public void testDivide() {
        BigInteger twenty = BigInteger.valueOf(20);
        BigInteger three = BigInteger.valueOf(3);
        BigInteger six = three.multiply(two);
        BigInteger ten = BigInteger.TEN;
        BigInteger negativeTen = ten.negate();
        BigInteger negativeThree = three.negate();

        assertEquals(BigInteger.valueOf(6), BigIntegerMath.divide(twenty, three, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.divide(twenty, three, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(10), BigIntegerMath.divide(twenty, ten, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.divide(ten, five, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(1), BigIntegerMath.divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.UP));
        assertEquals(BigInteger.valueOf(-2), BigIntegerMath.divide(negativeTen, three, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-3), BigIntegerMath.divide(twenty, negativeThree, RoundingMode.UP));
    }

    @Test(expected = AssertionError.class)
    public void testDivideInvalidMode() {
        BigInteger dividend = BigInteger.valueOf(10);
        BigInteger divisor = BigInteger.ONE;
        BigIntegerMath.divide(dividend, divisor, new RoundingMode() {});
    }

    @Test
    public void testFactorial() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger three = BigInteger.valueOf(3);
        BigInteger four = BigInteger.valueOf(4);
        BigInteger five = BigInteger.valueOf(5);
        BigInteger six = BigInteger.valueOf(6);
        BigInteger seven = BigInteger.valueOf(7);
        BigInteger eight = BigInteger.valueOf(8);
        BigInteger nine = BigInteger.valueOf(9);
        BigInteger ten = BigInteger.TEN;
        BigInteger twenty = BigInteger.valueOf(20);
        BigInteger thirty = BigInteger.valueOf(30);
        BigInteger forty = BigInteger.valueOf(40);
        BigInteger fortyNine = BigInteger.valueOf(49);
        BigInteger fifty = BigInteger.valueOf(50);
        BigInteger fiftyFive = BigInteger.valueOf(55);
        BigInteger sixty = BigInteger.valueOf(60);

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
        assertEquals(BigInteger.valueOf(2432902008176640000), BigIntegerMath.factorial(twenty));
        assertEquals(BigInteger.valueOf(2317816708410104368801092481617136220225859308000000000000000000), BigIntegerMath.factorial(thirty));
        assertEquals(BigInteger.valueOf(20278256636065485760000000000000000000000000000000000000000000000), BigIntegerMath.factorial(forty));
        assertEquals(BigInteger.valueOf(8717829120000000000000000000000000000000000000000000000000000000), BigIntegerMath.factorial(fortyNine));
        assertEquals(BigInteger.valueOf(1216451004088327782400000000000000000000000000000000000000000000), BigIntegerMath.factorial(fifty));
        assertEquals(BigInteger.valueOf(3041409320171337804361260816606476884437764156896051200000000000), BigIntegerMath.factorial(fiftyFive));
        assertEquals(BigInteger.valueOf(13228806661563055220886613303595625337322470938907037038747804925001600000000000), BigIntegerMath.factorial(sixty));
    }

    @Test
    public void testBinomial() {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger five = BigInteger.valueOf(5);
        BigInteger ten = BigInteger.TEN;
        BigInteger fifteen = BigInteger.valueOf(15);
        BigInteger sixteen = two.multiply(two).multiply(two).multiply(two);
        BigInteger twenty = ten.multiply(ten);
        BigInteger twentyFive = twenty.multiply(five);
        BigInteger thirty = fifteen.multiply(two);

        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(0, 1));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 0));
        assertEquals(BigInteger.ONE, BigIntegerMath.binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), BigIntegerMath.binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), BigIntegerMath.binomial(6, 3));
        assertEquals(BigInteger.valueOf(20), BigIntegerMath.binomial(20, 19));
        assertEquals(BigInteger.valueOf(15120), BigIntegerMath.binomial(thirty, 12));
        assertEquals(BigInteger.valueOf(1207274489692323121516061454136969589742088321220834835347688405084), BigIntegerMath.binomial(twentyFive, fifteen));
        assertEquals(BigInteger.valueOf(479001600), BigIntegerMath.binomial(twenty, ten));
    }

    @Test
    public void testFitsInLong() {
        BigInteger zero = BigInteger.ZERO;
        BigInteger one = BigInteger.ONE;
        BigInteger two = BigInteger.valueOf(2);
        BigInteger three = BigInteger.valueOf(3);
        BigInteger ten = BigInteger.TEN;
        BigInteger hundred = ten.multiply(ten);
        BigInteger thousand = hundred.multiply(ten);
        BigInteger tenThousand = thousand.multiply(ten);
        BigInteger negativeTenThousand = tenThousand.negate();
        BigInteger negativeTwenty = negativeTenThousand.negate();

        assertTrue(BigIntegerMath.fitsInLong(zero));
        assertTrue(BigIntegerMath.fitsInLong(one));
        assertTrue(BigIntegerMath.fitsInLong(two));
        assertTrue(BigIntegerMath.fitsInLong(three));
        assertTrue(BigIntegerMath.fitsInLong(ten));
        assertTrue(BigIntegerMath.fitsInLong(hundred));
        assertTrue(BigIntegerMath.fitsInLong(thousand));
        assertTrue(BigIntegerMath.fitsInLong(tenThousand));
        assertFalse(BigIntegerMath.fitsInLong(negativeTenThousand));
        assertFalse(BigIntegerMath.fitsInLong(negativeTwenty));
    }
}