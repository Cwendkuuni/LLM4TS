import static com.google.common.base.Preconditions.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Test positive cases
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1 << 31)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1 << 63)));

        // Test negative cases
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.NEGATIVE_ONE));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1 << 31)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1 << 63)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
    }
}