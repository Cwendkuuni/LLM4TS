import static org.junit.Assert.*;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {
    // Test case for hash(double value) method
    @Test
    public void testHashWithDouble() {
        double value = 123.45;
        int expectedHash = 12345; // Calculate expected hash based on Java's Double.hashCode()
        int actualHash = MathUtils.hash(value);
        assertEquals(expectedHash, actualHash);
    }
}