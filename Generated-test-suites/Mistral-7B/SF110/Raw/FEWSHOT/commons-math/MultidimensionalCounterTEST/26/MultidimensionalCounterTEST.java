package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        int[] sizes = {-1, 2, 3};
        try {
            new MultidimensionalCounter(sizes);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
        }
    }

    @Test
    public void testMultidimensionalCounterGetDimension() {
        int[] sizes = {2, 3, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testMultidimensionalCounterIteratorHasNext() {
        int[] sizes = {2, 3, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();

        // First call should return false since iterator is empty
        assertFalse(iterator.hasNext());

        // Iterate through all possible indices
        for (int i = 0; i < Math.pow(sizes[0], sizes.length); ++i) {
            assertTrue(iterator.hasNext());
        }

        // Last call should return false since all indices have been visited
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultidimensionalCounterIteratorGetNext() {
        int[] sizes = {2, 3, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();

        // First call should return null since iterator is empty
        assertNull(iterator.next());

        // Iterate through all possible indices and check if next returns the correct value
        for (int i = 0, index = 0; i < Math.pow(sizes[0], sizes.length); ++i, ++index) {
            Integer next = iterator.next();
            assertEquals(index, next.intValue());
        }

        // Last call should return null since all indices have been visited
        assertNull(iterator.next());
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] sizes = {2, 3, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 0;

        // Get the counts for each index and check if they are correct
        while (counter.iterator().hasNext()) {
            int[] counts = counter.iterator().getCounts();
            assertEquals(sizes, counts);
            index++;
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRangeException() {
        int[] sizes = {2, 3, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        // Test getCounts method with an out of range index
        int index = Math.pow(sizes[0], sizes.length) + 1;
        counter.getCounts(index);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterDimensionMismatchException() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        // Test getCount method with a mismatched dimension
        int[] wrongDimension = {2, 3, 4};
        counter.getCount(wrongDimension);
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] sizes = {2, 3, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        StringBuilder expected = new StringBuilder();
        expected.append("[").append(Math.pow(sizes[0], sizes.length - 1)).append("][")
                .append(Math.pow(sizes[0], sizes.length - 2)).append("]...");

        assertEquals(expected.toString(), counter.toString());
    }
}