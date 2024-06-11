package com.google.common.math; // replace with the actual package name if different

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class BigIntegerMathTest {

    private BigInteger number;
    private BigInteger powerOfTwoExpected;

    @Parameters
    public static Iterable<Object[]> data() {
        return new Object[][] {
                {BigInteger.ONE, true},
                {BigInteger.valueOf(3), true},
                {BigInteger.valueOf(4), true},
                {BigInteger.valueOf(5), false},
                {BigInteger.valueOf(16), true},
                {BigInteger.valueOf(33), false},
                {BigInteger.valueOf(-64), false},
                {BigInteger.valueOf(-1), false}
        };
    }

    public BigIntegerMathTest(BigInteger number, boolean powerOfTwoExpected) {
        this.number = number;
        this.powerOfTwoExpected = powerOfTwoExpected ? number : number.shiftRight(number.bitLength() - 1);
    }

    @Test
    public void testIsPowerOfTwo() {
        assertEquals(powerOfTwoExpected, BigIntegerMath.isPowerOfTwo(number));
    }

    @Test
    public void testLog2() {
        for (RoundingMode mode : RoundingMode.values()) {
            int expectedLog = number.bitLength() - 1;
            if (powerOfTwoExpected) {
                expectedLog -= 1;
            }
            assertEquals(expectedLog, BigIntegerMath.log2(number, mode));
        }
    }

    @Test(expected = AssertionError.class)
    public void testLog2WithInvalidMode() {
        BigIntegerMath.log2(number, RoundingMode.valueOf("INVALID"));
    }

    @Test
    public void testLog10() {
        RoundingMode[] modes = {RoundingMode.DOWN, RoundingMode.UP, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_EVEN};
        for (RoundingMode mode : modes) {
            BigInteger log10 = BigIntegerMath.log10(number, mode);
            BigInteger expectedLog10;
            if (number.compareTo(BigInteger.ONE) <= 0) {
                expectedLog10 = BigInteger.ZERO;
            } else {
                expectedLog10 = BigInteger.valueOf(Math.log10(number.longValue()));
            }
            assertEquals(expectedLog10, log10);
        }
    }

    @Test(expected = AssertionError.class)
    public void testLog10WithInvalidNumber() {
        BigIntegerMath.log10(null, RoundingMode.FLOOR);
    }

    @Test
    public void testSqrt() {
        RoundingMode[] modes = {RoundingMode.DOWN, RoundingMode.UP, RoundingMode.CEILING, RoundingMode.HALF_DOWN, RoundingMode.HALF_EVEN};
        for (RoundingMode mode : modes) {
            BigInteger expectedSqrt = null;
            if (number.compareTo(BigInteger.ZERO) > 0) {
                BigInteger sqrt = number.pow(0.5);
                if (mode == RoundingMode.HALF_DOWN || mode == RoundingMode.HALF_EVEN) {
                    if (sqrt.bitLength() % 2 == 1) {
                        expectedSqrt = sqrt.subtract(BigInteger.ONE).shiftRight(1);
                    } else {
                        expectedSqrt = sqrt;
                    }
                } else {
                    expectedSqrt = sqrt;
                }
            }
            BigInteger actualSqrt = BigIntegerMath.sqrt(number, mode);
            assertEquals(expectedSqrt, actualSqrt);
        }
    }

    @Test(expected = AssertionError.class)
    public void testSqrtWithInvalidNumber() {
        BigIntegerMath.sqrt(null, RoundingMode.FLOOR);
    }

    @Test
    public void testFactorial() {
        int[] numbers = {0, 1, 2, 3, 5, 7, 10, 20};
        for (int number : numbers) {
            BigInteger expectedFactorial = factorial(number);
            BigInteger actualFactorial = BigIntegerMath.factorial(number);
            assertEquals(expectedFactorial, actualFactorial);
        }
    }

    private static BigInteger factorial(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Factorial of negative numbers is undefined.");
        }
        if (number == 0) {
            return BigInteger.ONE;
        }
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= number; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}

