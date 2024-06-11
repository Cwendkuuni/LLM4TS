package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterDimension() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
        assertEquals(3, mdc.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(-1, 2, 2);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterInvalidTotalSize() {
        int[] sizes = {1, 2, -1};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
    }

    @Test
    public void testMultidimensionalCounterIteratorHasNext() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
        Iterator iterator = mdc.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultidimensionalCounterIteratorNext() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
        Iterator iterator = mdc.iterator();
        assertEquals(-1, iterator.next());
        assertEquals(0, iterator.next());
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
        assertEquals(3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
        int[] expected = {0, 0, 0};
        int[] counts = mdc.getCounts(0);
        assertArrayEquals(expected, counts);
        expected = new int[]{0, 1, 0};
        counts = mdc.getCounts(1);
        assertArrayEquals(expected, counts);
        expected = new int[]{0, 1, 1};
        counts = mdc.getCounts(2);
        assertArrayEquals(expected, counts);
        expected = new int[]{0, 2, 0};
        counts = mdc.getCounts(3);
        assertArrayEquals(expected, counts);
        expected = new int[]{0, 2, 1};
        counts = mdc.getCounts(4);
        assertArrayEquals(expected, counts);
        expected = new int[]{0, 2, 2};
        counts = mdc.getCounts(5);
        assertArrayEquals(expected, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountsOutOfRange() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
        mdc.getCounts(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterGetCountsDimensionMismatch() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
        mdc.getCounts(6);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
        assertEquals(0, mdc.getCount(0, 0, 0));
        assertEquals(0, mdc.getCount(1, 0, 0));
        assertEquals(0, mdc.getCount(0, 1, 0));
        assertEquals(0, mdc.getCount(0, 0, 1));
        assertEquals(0, mdc.getCount(1, 1, 0));
        assertEquals(0, mdc.getCount(0, 1, 1));
        assertEquals(0, mdc.getCount(1, 0, 1));
        assertEquals(2, mdc.getCount(1, 1, 1));
        assertEquals(3, mdc.getCount(2, 2, 2));
        assertEquals(6, mdc.getCount(2, 2, 1));
        assertEquals(6, mdc.getCount(2, 1, 2));
        assertEquals(6, mdc.getCount(1, 2, 2));
        assertEquals(8, mdc.getCount(2, 2, 2));
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountOutOfRange() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
        mdc.getCount(-1, 0, 0);
        mdc.getCount(3, 0, 0);
        mdc.getCount(0, 3, 0);
        mdc.getCount(0, 0, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterGetCountDimensionMismatch() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
        mdc.getCount(3, 2, 1);
    }

    @Test
    public void testMultidimensionalCounterToString() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(3, 2, 2);
        String expected = "[0][0][0]" +
                "[0][0][1]" +
                "[0][1][0]" +
                "[0][1][1]" +
                "[0][2][0]" +
                "[0][2][1]" +
                "[0][2][2]" +
                "[1][0][0]" +
                "[1][0][1]" +
                "[1][0][2]" +
                "[1][1][0]" +
                "[1][1][1]" +
                "[1][1][2]" +
                "[1][2][0]" +
                "[1][2][1]" +
                "[1][2][2]" +
                "[2][0][0]" +
                "[2][0][1]" +
                "[2][0][2]" +
                "[2][1][0]" +
                "[2][1][1]" +
                "[2][1][2]" +
                "[2][2][0]" +
                "[2][2][1]" +
                "[2][2][2]";
        assertEquals(expected, mdc.toString());
    }
}