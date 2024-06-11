import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Positive cases
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.ONE));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(2)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(4)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));

        // Negative cases
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ZERO));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(-1)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.ONE.add(BigInteger.ONE)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(5)));
    }
}