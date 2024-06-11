import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.util.MathUtils;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedResult = 123;
        int result = MathUtils.hash(value);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expectedResult = 12345;
        int result = MathUtils.hash(values);
        assertEquals(expectedResult, result);
    }

    // Test for other methods in the same way
}