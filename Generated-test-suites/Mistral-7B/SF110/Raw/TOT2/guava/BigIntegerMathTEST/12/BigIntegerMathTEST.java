import static com.google.common.math.BigIntegerMath.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Test positive cases
        assertTrue(isPowerOfTwo(BigInteger.ONE));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(1 << 32)));
        assertTrue(isPowerOfTwo(BigInteger.valueOf(1L << 64)));

        // Test negative cases
        assertFalse(isPowerOfTwo(BigInteger.ZERO));
        assertFalse(isPowerOfTwo(BigInteger.NEGATIVE_ONE));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(-1L << 32)));
        assertFalse(isPowerOfTwo(BigInteger.valueOf(-1L << 64)));
    }
}