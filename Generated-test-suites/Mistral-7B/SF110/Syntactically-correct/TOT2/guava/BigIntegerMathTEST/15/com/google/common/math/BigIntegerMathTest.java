import static org.junit.Assert.*;
import com.google.common.math.BigIntegerMath;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(1L << 31);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(3L).pow(2);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(5L).pow(2);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));

        x = BigInteger.valueOf(-1L);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }
}