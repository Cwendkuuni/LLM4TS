import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedHash = 123; // replace with a real expected hash value
        int actualHash = MathUtils.hash(value);
        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expectedHash = 456; // replace with a real expected hash value
        int actualHash = MathUtils.hash(values);
        assertEquals(expectedHash, actualHash);
    }

    // add test cases for the other methods
}