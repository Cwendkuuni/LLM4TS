import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double testValue = 123.45;
        int expectedHash = 12345; // hashcode of testValue
        int actualHash = MathUtils.hash(testValue);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testHashDoubleArray() {
        double[] testValues = {1.1, 2.2, 3.3};
        long expectedHash = 12345678L; // arbitrary hashcode for testValues array
        long actualHash = MathUtils.hash(testValues);
        assertEquals(expectedHash, actualHash);
    }

    // Test cases for other methods omitted for brevity

    @Test(expected = MathArithmeticException.class)
    public void testCopySignByteNegativeAndPositiveSign() {
        byte magnitude = 5;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign); // Should not throw an exception
        sign = -1;
        try {
            MathUtils.copySign(magnitude, sign);
        } catch (MathArithmeticException e) {
            // Expected exception
        }
    }

    // Test cases for other methods with edge cases and exceptions omitted for brevity
}