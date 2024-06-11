import static org.junit.Assert.*;
import com.google.common.math.BigIntegerMath;
import org.junit.Test;

public class isPowerOfTwoTest {
    @Test
    public void testIsPowerOfTwo() {
        BigInteger x = BigInteger.valueOf(1);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        x = BigInteger.valueOf(2);
        assertTrue(BigIntegerMath.isPowerOfTwo(x));
        x = BigInteger.valueOf(3);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
        x = BigInteger.valueOf(5);
        assertFalse(BigIntegerMath.isPowerOfTwo(x));
    }
}