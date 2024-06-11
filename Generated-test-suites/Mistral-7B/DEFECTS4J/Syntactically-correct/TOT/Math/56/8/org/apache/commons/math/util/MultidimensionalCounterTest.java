package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

public class MultidimensionalCounterTest {

    @Test
    public void testEmptyMultiDimensionalCounter() {
        int[] counterSizes = {};
        MultidimensionalCounter counter = new MultidimensionalCounter(counterSizes);
        Iterator iterator = counter.iterator();
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testValidMultiDimensionalCounter() {
        int[] counterSizes = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(counterSizes);
        Iterator iterator = counter.iterator();
        int[] expectedCounts;

        // Test initial state
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        // Test iteration
        expectedCounts = new int[]{0, 0, 0};
        iterator.next();
        assertArrayEquals(expectedCounts, iterator.getCounts());
        assertEquals(0, iterator.getCount());
        expectedCounts = new int[]{0, 0, 1};
        iterator.next();
        assertArrayEquals(expectedCounts, iterator.getCounts());
        assertEquals(1, iterator.getCount());

        // Test error handling
        assertThrows(OutOfRangeException.class, () -> {
            iterator.getCount(-1);
        });
        assertThrows(OutOfRangeException.class, () -> {
            iterator.getCount(counter.getDimension());
        });
        assertThrows(DimensionMismatchException.class, () -> {
            MultidimensionalCounter counter2 = new MultidimensionalCounter(new int[]{3, 4, 6});
            Iterator iterator2 = counter2.iterator();
            iterator2.next();
            iterator2.next();
            iterator2.next();
            iterator2.next();
            iterator2.next();
            iterator2.next();
            iterator2.next();
            int index = 7;
            iterator2.getCount(index);
        });
    }

    @Test
    public void testValidMultiDimensionalCounterBackwards() {
        int[] counterSizes = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(counterSizes);
        Iterator iterator = counter.iterator();
        int[] expectedCounts;

        // Test iteration backwards
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        expectedCounts = new int[]{2, 4, 4};
        assertArrayEquals(expectedCounts, iterator.getCounts());
        assertEquals(counter.getSize() - 1, iterator.getCount());

        iterator.next();
        expectedCounts = new int[]{2, 4, 5};
        assertArrayEquals(expectedCounts, iterator.getCounts());
        assertEquals(counter.getSize() - 2, iterator.getCount());

        iterator.next();
        expectedCounts = new int[]{2, 4, 5};
        assertArrayEquals(expectedCounts, iterator.getCounts());
        assertEquals(counter.getSize() - 3, iterator.getCount());

        iterator.next();
        expectedCounts = new int[]{2, 4, 5};
        assertArrayEquals(expectedCounts, iterator.getCounts());
        assertEquals(counter.getSize() - 4, iterator.getCount());

        iterator.next();
        expectedCounts = new int[]{2, 4, 5};
        assertArrayEquals(expectedCounts, iterator.getCounts());
        assertEquals(counter.getSize() - 5, iterator.getCount());

        iterator.next();
        expectedCounts = new int[]{3, 4, 5};
        assertArrayEquals(expectedCounts, iterator.getCounts());
        assertEquals(counter.getSize() - 6, iterator.getCount());

        // Test error handling
        assertThrows(OutOfRangeException.class, () -> {
            iterator.next();
            iterator.getCount();
        });
    }
}