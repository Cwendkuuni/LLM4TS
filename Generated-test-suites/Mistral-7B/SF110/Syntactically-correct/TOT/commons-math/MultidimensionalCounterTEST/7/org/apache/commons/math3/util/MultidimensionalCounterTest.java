import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeSizes() {
        int[] sizes = {-1, 2, 3};
        try {
            new MultidimensionalCounter(sizes);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructor_ValidSizes() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Assert.assertEquals(2 * 3 * 4, counter.getSize());
        Assert.assertArrayEquals(sizes, counter.getSizes());
    }

    // Add more test cases here for other methods
}