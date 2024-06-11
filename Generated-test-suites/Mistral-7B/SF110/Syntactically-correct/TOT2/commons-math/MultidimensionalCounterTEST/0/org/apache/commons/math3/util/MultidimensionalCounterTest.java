import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class MultidimensionalCounterTest {
    // 

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultiDimensionalCounterWithNegativeSize() {
        int[] negativeSize = {-1, 2, 3};
        try {
            new MultidimensionalCounter(negativeSize);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testMultiDimensionalCounterWithValidSize() {
        int[] validSize = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(validSize);
        // Add more assertions to check the dimensions and total size
    }

    // 
}