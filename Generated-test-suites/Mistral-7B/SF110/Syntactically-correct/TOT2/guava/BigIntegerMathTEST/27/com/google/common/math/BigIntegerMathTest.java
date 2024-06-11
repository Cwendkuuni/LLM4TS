import static com.google.common.math.BigIntegerMath.*;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        assertTrue(isPowerOfTwo(BigInteger.ONE));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(1 << 31)));
        assertFalse(isPowerOfTwo(BigInteger.ZERO));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(5)));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(13)));
    }
}