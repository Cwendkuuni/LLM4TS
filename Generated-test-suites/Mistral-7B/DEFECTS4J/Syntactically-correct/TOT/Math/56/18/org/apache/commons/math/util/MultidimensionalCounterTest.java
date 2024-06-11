import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounter() {
        // Test valid construction
        MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2, 2);
        assertEquals(24, mdc.getSize()); // total size = 3 * 2 * 2

        // Test valid iteration
        Iterator iterator = mdc.iterator();
        int[] counts = new int[3];
        int index = 0;
        while (iterator.hasNext()) {
            counts[index++] = iterator.next();
        }
        assertArrayEquals(new int[]{0, 0, 0}, counts);

        iterator = mdc.iterator();
        while (iterator.hasNext()) {
            assertEquals(3, iterator.getCounts()[0]); // 3 slots in 1st dimension
            assertEquals(2, iterator.getCounts()[1]); // 2 slots in 2nd dimension
            assertEquals(2, iterator.getCounts()[2]); // 2 slots in 3rd dimension

            int[] multidimensionalCounts = iterator.getCounts();
            int unidimensionalCount = iterator.getCount();

            // Check conversion from multidimensional to unidimensional count
            int[] expected = new int[]{multidimensionalCounts[0], multidimensionalCounts[1], multidimensionalCounts[2]};
            assertArrayEquals(expected, new int[]{iterator.getCount(multidimensionalCounts)});

            // Check conversion from unidimensional to multidimensional count
            int[] unidimensionalCounts = new int[]{iterator.getCount()};
            int[] expectedMultidimensionalCounts = mdc.getCounts(iterator.getCount());
            assertArrayEquals(multidimensionalCounts, expectedMultidimensionalCounts);

            iterator.next();
        }

        // Test invalid construction with negative or zero size
        try {
            MultidimensionalCounter mdcNegative = new MultidimensionalCounter(-1, 2, 2, 2);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            MultidimensionalCounter mdcZero = new MultidimensionalCounter(0, 2, 2, 2);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            MultidimensionalCounter mdcSizeMismatch = new MultidimensionalCounter(1, 2, 3, 2);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        // Test out of range index in unidimensional counter
        try {
            MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2, 2);
            mdc.getCount(-1);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2, 2);
            mdc.getCount(25);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        // Test out of range multidimensional indices
        try {
            MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2, 2);
            mdc.getCount(new int[]{-1, 0, 0});
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2, 2);
            mdc.getCount(new int[]{3, 2, 2});
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }
}