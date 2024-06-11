import static org.junit.Assert.*;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {
    @Test
    public void testIsPowerOfTwo() {
        // Positive test cases
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));

        // Negative test cases with negative numbers
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-2)));

        // Negative test cases with non-power-of-two numbers
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(15)));
    }
}