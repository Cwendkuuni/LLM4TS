import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.util.MultidimensionalCounter;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertEquals(2 * 3 * 4, mdc.getSize());
    }

    @Test
    public void testMultidimensionalCounterEmptyArray() {
        try {
            int[] emptyArray = {};
            MultidimensionalCounter mdc = new MultidimensionalCounter(emptyArray);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testMultidimensionalCounterNullArray() {
        try {
            MultidimensionalCounter mdc = new MultidimensionalCounter(null);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testGetCountsOutOfBounds() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        try {
            mdc.getCounts(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            mdc.getCounts(Integer.MAX_VALUE);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testGetCountWrongDimensions() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        try {
            mdc.getCount(new int[]{1, 2});
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        try {
            mdc.getCount(new int[]{2, 3, 5});
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testToString() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        assertEquals("[0][0][0]", mdc.toString());
    }
}