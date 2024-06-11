import static org.junit.Assert.*;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Positive test case
        BigInteger x = BigInteger.valueOf(16);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        // Negative test case
        BigInteger y = BigInteger.valueOf(-16);
        assertFalse(BigIntegerMath.isPowerOfTwo(y));

        // Edge case: zero
        BigInteger z = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(z));
    }
}