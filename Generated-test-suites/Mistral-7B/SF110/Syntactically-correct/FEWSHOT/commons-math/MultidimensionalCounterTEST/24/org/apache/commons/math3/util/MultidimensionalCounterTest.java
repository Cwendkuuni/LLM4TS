package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstructor() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(new int[]{3, 4, 5}, mdc.getSizes());
        assertEquals(1382, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructorNegativeSize() {
        int[] size = {-1, 4, 5};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructorZeroSize() {
        int[] size = {0, 4, 5};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetCounts() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] index = mdc.getCounts(0);
        assertEquals(0, index[0]);
        assertEquals(0, index[1]);
        assertEquals(0, index[2]);
        index = mdc.getCounts(1381);
        assertEquals(2, index[0]);
        assertEquals(3, index[1]);
        assertEquals(4, index[2]);
        assertEquals(4, mdc.getCounts(1382).length);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfTotalSize() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCounts(1383);
    }

    @Test
    public void testGetCount() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(0, mdc.getCount(0, 0, 0));
        assertEquals(0, mdc.getCount(2, 3, 4));
        assertEquals(15, mdc.getCount(1, 1, 1));
        assertEquals(1382, mdc.getCount(2, 3, 4));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(-1, 0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBounds() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(4, 0, 0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] dim = {3, 5};
        mdc.getCount(dim);
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator it = mdc.iterator();
        for (int i = 0; i < 1382; ++i) {
            assertTrue(it.hasNext());
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator it = mdc.iterator();
        for (int i = 0; i < 1382; ++i) {
            assertEquals(i + 1, it.next());
        }
        assertNull(it.next());
    }

    @Test
    public void testIteratorGetCount() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator it = mdc.iterator();
        for (int i = 0; i < 1382; ++i) {
            assertEquals(i + 1, it.getCount());
            it.next();
        }
        assertNull(it.getCount());
    }

    @Test
    public void testIteratorGetCounts() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator it = mdc.iterator();
        for (int i = 0; i < 1382; ++i) {
            int[] counts = it.getCounts();
            assertEquals(3, counts.length);
            assertEquals(i / (Math.pow(5, 2) * 4 + i % 5), counts[0]);
            assertEquals(i % 4, counts[1]);
            assertEquals(i % 5, counts[2]);
            it.next();
        }
        assertNull(it.getCounts());
    }
}