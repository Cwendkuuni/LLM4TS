import static org.junit.Assert.*;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Test positive cases
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));

        // Test negative cases
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.negate()));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-16)));

        // Test edge cases
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(Integer.MIN_VALUE)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(Long.MAX_VALUE).shiftRight(1)));
    }
}