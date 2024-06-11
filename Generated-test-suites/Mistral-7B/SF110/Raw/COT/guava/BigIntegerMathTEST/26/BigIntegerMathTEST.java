import static org.junit.Assert.*;
import com.google.common.math.BigIntegerMath;

public class isPowerOfTwoTest {
    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.valueOf(1);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        BigInteger x2 = BigInteger.valueOf(2);
        assertTrue(BigIntegerMath.isPowerOfTwo(x2));

        BigInteger x3 = BigInteger.valueOf(3);
        assertFalse(BigIntegerMath.isPowerOfTwo(x3));

        BigInteger x4 = BigInteger.valueOf(4);
        assertTrue(BigIntegerMath.isPowerOfTwo(x4));

        BigInteger x5 = BigInteger.valueOf(5);
        assertFalse(BigIntegerMath.isPowerOfTwo(x5));

        BigInteger x6 = BigInteger.valueOf(6);
        assertTrue(BigIntegerMath.isPowerOfTwo(x6));

        BigInteger xNegative = BigInteger.valueOf(-1);
        assertFalse(BigIntegerMath.isPowerOfTwo(xNegative));

        BigInteger xZero = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(xZero));
    }
}