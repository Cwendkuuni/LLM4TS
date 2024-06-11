import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {
    @Test
    public void testIsPowerOfTwo() {
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(1)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(8)));
        assertTrue(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(16)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(3)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(5)));
        assertFalse(BigIntegerMath.isPowerOfTwo(BigInteger.valueOf(10)));
    }

    @Test
    public void testLog2() {
        assertEquals(0, BigIntegerMath.log2(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(8), RoundingMode.UP));
        assertEquals(4, BigIntegerMath.log2(BigInteger.valueOf(16), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(9), RoundingMode.HALF_DOWN));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(9), RoundingMode.HALF_UP));
        assertEquals(3, BigIntegerMath.log2(BigInteger.valueOf(9), RoundingMode.HALF_EVEN));
        try {
            BigIntegerMath.log2(BigInteger.ZERO, RoundingMode.ANY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLog10() {
        assertEquals(0, BigIntegerMath.log10(BigInteger.ONE, RoundingMode.DOWN));
        assertEquals(1, BigIntegerMath.log10(BigInteger.valueOf(10), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.DOWN));
        assertEquals(2, BigIntegerMath.log10(BigInteger.valueOf(100), RoundingMode.UP));
        assertEquals(3, BigIntegerMath.log10(BigInteger.valueOf(1000), RoundingMode.UP));
        try {
            BigIntegerMath.log10(BigInteger.ZERO, RoundingMode.ANY);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}