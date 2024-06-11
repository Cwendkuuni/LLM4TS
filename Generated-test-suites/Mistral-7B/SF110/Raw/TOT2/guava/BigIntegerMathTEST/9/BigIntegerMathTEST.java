import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo_PositiveNumber() {
        BigInteger number = BigInteger.valueOf(16);
        assertTrue(BigIntegerMath.isPowerOfTwo(number));
    }

    @Test
    public void testIsPowerOfTwo_NegativeNumber() {
        BigInteger number = BigInteger.valueOf(-16);
        assertFalse(BigIntegerMath.isPowerOfTwo(number));
    }

    @Test
    public void testIsPowerOfTwo_Zero() {
        BigInteger number = BigInteger.ZERO;
        assertFalse(BigIntegerMath.isPowerOfTwo(number));
    }

    @Test
    public void testIsPowerOfTwo_NonPowerOfTwo() {
        BigInteger number = BigInteger.valueOf(13);
        assertFalse(BigIntegerMath.isPowerOfTwo(number));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPowerOfTwo_NullNumber() {
        BigInteger number = null;
        BigIntegerMath.isPowerOfTwo(number);
    }
}