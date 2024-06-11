
import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Positive test case
        BigInteger number = BigInteger.valueOf(1);
        assertTrue(BigIntegerMath.isPowerOfTwo(number));

        // Test with power of two
        number = BigInteger.valueOf(8);
        assertTrue(BigIntegerMath.isPowerOfTwo(number));

        // Test with negative number
        number = BigInteger.valueOf(-1);
        assertFalse(BigIntegerMath.isPowerOfTwo(number));

        // Test with non-power of two
        number = BigInteger.valueOf(15);
        assertFalse(BigIntegerMath.isPowerOfTwo(number));
    }

    @Test
    public void testLog2Positive() {
        BigInteger number = BigInteger.valueOf(8);
        int expectedResult = 3;
        int actualResult = BigIntegerMath.log2(number, RoundingMode.FLOOR);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLog2Zero() {
        BigInteger number = BigInteger.ZERO;
        int expectedResult = 0;
        int actualResult = BigIntegerMath.log2(number, RoundingMode.FLOOR);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLog2Negative() {
        BigInteger number = BigInteger.valueOf(-5);
        int expectedResult = Integer.MIN_VALUE;
        try {
            int actualResult = BigIntegerMath.log2(number, RoundingMode.FLOOR);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLog2RoundingModes() {
        BigInteger number = BigInteger.valueOf(16);
        int expectedResultDown = 4;
        int actualResultDown = BigIntegerMath.log2(number, RoundingMode.DOWN);
        int expectedResultUp = 5;
        int actualResultUp = BigIntegerMath.log2(number, RoundingMode.UP);
        int expectedResultHalfDown = 4;
        int actualResultHalfDown = BigIntegerMath.log2(number, RoundingMode.HALF_DOWN);
        int expectedResultHalfUp = 5;
        int actualResultHalfUp = BigIntegerMath.log2(number, RoundingMode.HALF_UP);
        int expectedResultHalfEven = 5;
        int actualResultHalfEven = BigIntegerMath.log2(number, RoundingMode.HALF_EVEN);

        assertEquals(expectedResultDown, actualResultDown);
        assertEquals(expectedResultUp, actualResultUp);
        assertEquals(expectedResultHalfDown, actualResultHalfDown);
        assertEquals(expectedResultHalfUp, actualResultHalfUp);
        assertEquals(expectedResultHalfEven, actualResultHalfEven);
    }

    @Test
    public void testFactorialSmallValues() {
        BigInteger result = BigIntegerMath.factorial(0);
        assertEquals(BigInteger.ONE, result);

        result = BigIntegerMath.factorial(1);
        assertEquals(BigInteger.ONE, result);

        result = BigIntegerMath.factorial(2);
        assertEquals(BigInteger.valueOf(2), result);

        result = BigIntegerMath.factorial(3);
        assertEquals(BigInteger.valueOf(6), result);
    }

    @Test
    public void testFactorialLargeValues() {
        BigInteger result = BigIntegerMath.factorial(50);
        BigInteger expectedResult = BigInteger.valueOf(30414093201713378043612608166064768844377641568960512000000000000000000000000);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFactorialNegativeNumber() {
        try {
            BigInteger result = BigIntegerMath.factorial(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}
