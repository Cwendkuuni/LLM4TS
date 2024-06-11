import static com.google.common.math.BigIntegerMath.*;
import static org.junit.Assert.*;

public class BigIntegerMathTest {
    @Test
    public void testIsPowerOfTwo() {
        // Test with powers of two
        for (int i = 0; i < 10; i++) {
            BigInteger test = BigInteger.ONE.shiftLeft(i);
            assertTrue(isPowerOfTwo(test));
        }

        // Test with non-powers of two
        BigInteger notPowerOfTwo = BigInteger.valueOf(3);
        assertFalse(isPowerOfTwo(notPowerOfTwo));

        BigInteger negativeNumber = BigInteger.valueOf(-5);
        assertFalse(isPowerOfTwo(negativeNumber));
    }
}