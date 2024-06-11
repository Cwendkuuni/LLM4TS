package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterCreation() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterCreationWithNegativeSizes() {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterCreationWithZeroSize() {
        int[] size = {2, 0, 4};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(-1, iterator.next());
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            assertTrue(iterator.hasNext());
            assertEquals(i + 1, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            assertEquals(i, iterator.getCount());
            assertEquals(i + 1, iterator.next());
        }
        assertNull(iterator.getCount());
    }

    @Test
    public void testIteratorGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            int[] counts = iterator.getCounts();
            assertEquals(counter.getDimension(), counts.length);
            for (int j = 0; j < counter.getDimension(); ++j) {
                assertEquals(i % counter.getSize(j), counts[j]);
            }
            assertEquals(i, iterator.getCount());
            iterator.next();
        }
        assertNull(iterator.getCounts());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithDimensionMismatch() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(new int[]{5, 3, 4});
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            int[] indices = counter.getCounts(i);
            int count = counter.getCount(indices);
            assertEquals(i, count);
        }
        assertNull(counter.getCount(null));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(new int[]{-1});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithDimensionMismatch() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(new int[]{5});
    }

}