
import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.common.math.BigIntegerMath.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Positive test cases
        assertEquals(true, isPowerOfTwo(BigInteger.ONE));
        assertEquals(true, isPowerOfTwo(BigInteger.valueOf(1 << 31)));
        assertEquals(true, isPowerOfTwo(BigInteger.valueOf(1 << 63)));

        // Negative test cases
        assertEquals(false, isPowerOfTwo(BigInteger.ZERO));
        assertEquals(false, isPowerOfTwo(BigInteger.valueOf(-1)));
        assertEquals(false, isPowerOfTwo(BigInteger.valueOf(2)));
        assertEquals(false, isPowerOfTwo(BigInteger.valueOf(3)));
    }

    @Test
    public void testLog2() {
        // Positive test cases
        assertEquals(0, log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(31, log2(BigInteger.valueOf(1 << 31), RoundingMode.DOWN));
        assertEquals(32, log2(BigInteger.ONE.shiftLeft(32), RoundingMode.DOWN));
        assertEquals(63, log2(BigInteger.valueOf(1 << 63), RoundingMode.UP));

        // Edge cases
        assertEquals(31, log2(BigInteger.ONE.shiftLeft(32), RoundingMode.DOWN));
        assertEquals(32, log2(BigInteger.ONE.shiftLeft(32), RoundingMode.UP));

        // Exception handling test cases
        try {
            log2(BigInteger.ZERO, RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            log2(BigInteger.ONE.negate(), RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFactorial() {
        // Positive test cases
        assertEquals(BigInteger.ONE, factorial(0));
        assertEquals(BigInteger.ONE, factorial(1));
        assertEquals(BigInteger.valueOf(2), factorial(2));
        assertEquals(BigInteger.valueOf(24), factorial(4));
        assertEquals(BigInteger.valueOf(120), factorial(6));

        // Large test cases
        assertEquals(BigInteger.valueOf(511_960_932_352_131_270_383_653_308_875_181_000_000), factorial(2031));

        // Negative test cases
        try {
            factorial(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Exception handling test cases
        try {
            factorial(Integer.MIN_VALUE);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBinomial() {
        // Positive test cases
        assertEquals(BigInteger.ONE, binomial(0, 0));
        assertEquals(BigInteger.ONE, binomial(1, 0));
        assertEquals(BigInteger.ONE, binomial(1, 1));
        assertEquals(BigInteger.valueOf(2), binomial(2, 1));
        assertEquals(BigInteger.valueOf(6), binomial(3, 2));
        assertEquals(BigInteger.valueOf(40320), binomial(14, 6));

        // Edge cases
        assertEquals(BigInteger.valueOf(1), binomial(2, 1));
        assertEquals(BigInteger.valueOf(2), binomial(2, 2));
        assertEquals(BigInteger.valueOf(120), binomial(14, 14));

        // Exception handling test cases
        try {
            binomial(-2, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            binomial(2, 15);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSqrt() {
        // Positive test cases
        assertEquals(BigInteger.ONE, sqrt(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(1 << 31), sqrt(BigInteger.valueOf(1 << 31), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(1 << 63), sqrt(BigInteger.valueOf(1 << 63), RoundingMode.UP));

        // Edge cases
        assertEquals(BigInteger.valueOf(2), sqrt(BigInteger.valueOf(4), RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(3), sqrt(BigInteger.valueOf(9), RoundingMode.UP));

        // Exception handling test cases
        try {
            sqrt(BigInteger.ZERO, RoundingMode.DOWN);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            sqrt(BigInteger.valueOf(5), RoundingMode.DOWN);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }

    @Test
    public void testDivide() {
        // Positive test cases
        assertEquals(BigInteger.ONE, divide(BigInteger.ONE, BigInteger.ONE));
        assertEquals(BigInteger.valueOf(4), divide(BigInteger.valueOf(8), BigInteger.valueOf(2)));
        assertEquals(BigInteger.valueOf(3), divide(BigInteger.valueOf(6), BigInteger.valueOf(2)));
        assertEquals(BigInteger.valueOf(7), divide(BigInteger.valueOf(14), BigInteger.valueOf(2), RoundingMode.UP));

        // Edge cases
        assertEquals(BigInteger.ONE, divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(BigInteger.valueOf(-1), divide(BigInteger.ONE, BigInteger.ONE, RoundingMode.DOWN).negate());

        // Exception handling test cases
        try {
            divide(BigInteger.ZERO, BigInteger.ONE);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }

        try {
            divide(BigInteger.valueOf(5), BigInteger.valueOf(2));
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // expected
        }
    }
}