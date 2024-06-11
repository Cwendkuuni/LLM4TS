package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertEquals(3, counter.getSize());
        assertArrayEquals(new int[]{3, 2, 1}, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSize() {
        int[] size = {-1, 2, 1};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] counts = counter.getCounts(0);
        assertEquals(3, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(1, counts[2]);

        counts = counter.getCounts(counter.getTotalSize() - 1);
        assertEquals(2, counts[1]);
        assertEquals(1, counts[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRange() {
        int[] size = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(-1);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] size = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(0, counter.getCount(new int[]{0, 0, 0}));
        assertEquals(3 * 2 * 1, counter.getCount(new int[]{2, 1, 0}));
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRangeCount() {
        int[] size = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(new int[]{-1, 0, 0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterDimensionMismatch() {
        int[] size = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(new int[]{3, 2, 1});
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        assertFalse(iterator.hasNext());

        iterator.next();
        int[] counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(0, counts[2]);

        iterator.next();
        counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(1, counts[2]);

        iterator.next();
        counts = iterator.getCounts();
        assertEquals(2, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(1, counts[2]);

        iterator.next();
        counts = iterator.getCounts();
        assertEquals(2, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(2, counts[2]);

        assertFalse(iterator.hasNext());
    }
}