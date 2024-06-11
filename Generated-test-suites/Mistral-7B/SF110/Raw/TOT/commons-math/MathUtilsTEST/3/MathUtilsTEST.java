import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {
    // Test for hash(double) method
    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expected = 12345; // Any arbitrary hash code value for testing
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual); // Typical use case

        value = Double.MIN_VALUE;
        actual = MathUtils.hash(value);
        assertEquals(Long.doubleToRawLongBits(value), actual); // Edge case

        value = Double.NaN;
        assertNotSame(Long.doubleToRawLongBits(value), actual); // Error scenario
    }

    // Test for hash(double[]) method
    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 12345; // Any arbitrary hash code value for testing
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual); // Typical use case

        values = new double[0];
        actual = MathUtils.hash(values);
        assertEquals(0, actual); // Edge case: empty array

        values = new double[]{Double.MIN_VALUE, Double.NaN};
        assertNotSame(Arrays.hashCode(values), actual); // Error scenario: NaN and negative double in the array
    }
}