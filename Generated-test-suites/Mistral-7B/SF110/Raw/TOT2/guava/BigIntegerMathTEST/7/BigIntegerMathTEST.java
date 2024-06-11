import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {

    @Test
    public void testIsPowerOfTwo() {
        // Positive test case
        BigInteger x = BigInteger.ONE;
        assertTrue(BigIntegerMath.isPowerOfTwo(x));

        // Edge cases
        BigInteger xEdge1 = new BigInteger("1", 1); // single bit set
        BigInteger xEdge2 = new BigInteger("10", 3); // not the highest bit set
        BigInteger xEdge3 = new BigInteger("11111111111111111111111111111111111111111111111111111111", 32); // max int size
        BigInteger xEdge4 = new BigInteger("1000000000000000000000000000000000000000000000000000000000000", 64); // max long size
        BigInteger xEdge5 = new BigInteger("11111111111111111111111111111111111111111111111111111111111111111111111111111", 64); // max long + 1

        // Negative test case
        BigInteger xNeg = new BigInteger("-1");
        assertFalse(BigIntegerMath.isPowerOfTwo(xNeg));

        // Exception test case
        BigInteger xNull = null;
        try {
            BigIntegerMath.isPowerOfTwo(xNull);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }
}