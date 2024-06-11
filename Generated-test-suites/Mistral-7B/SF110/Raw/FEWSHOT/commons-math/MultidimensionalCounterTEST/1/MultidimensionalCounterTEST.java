package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected NotStrictlyPositiveException, size is positive: " + Arrays.toString(size));
        }
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(new int[]{2, 3, 4}, mdc.getSizes());
        assertEquals(24, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] index = {1, 1, 1};
        int count = mdc.getCount(index);
        assertEquals(1, count);

        index = new int[]{2, 2, 2};
        count = mdc.getCount(index);
        assertEquals(8, count);

        index = new int[]{2, 2, 3};
        try {
            mdc.getCount(index);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        index = new int[]{3, 3, 3};
        try {
            mdc.getCount(index);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        index = new int[]{1, 3, 2};
        try {
            mdc.getCount(index);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        index = new int[]{2, 3, 3};
        try {
            mdc.getCount(index);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int index = 0;
        Iterator iterator = mdc.iterator();
        while (iterator.hasNext()) {
            int count = iterator.next();
            assertEquals(index, count);
            index++;
        }
        assertFalse(iterator.hasNext());

        iterator = mdc.iterator();
        int[] counts = iterator.getCounts();
        assertArrayEquals(new int[]{0, 0, 0}, counts);
        assertEquals(0, iterator.getCount());
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] index = {1, 1, 1};
        int[] counts = mdc.getCounts(index);
        assertArrayEquals(index, counts);

        index = new int[]{2, 2, 2};
        counts = mdc.getCounts(index);
        assertArrayEquals(new int[]{2, 2, 2}, counts);

        index = new int[]{2, 2, 3};
        try {
            mdc.getCounts(index);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        index = new int[]{3, 3, 3};
        try {
            mdc.getCounts(index);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        index = new int[]{1, 3, 2};
        try {
            mdc.getCounts(index);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        index = new int[]{2, 3, 3};
        try {
            mdc.getCounts(index);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }
}