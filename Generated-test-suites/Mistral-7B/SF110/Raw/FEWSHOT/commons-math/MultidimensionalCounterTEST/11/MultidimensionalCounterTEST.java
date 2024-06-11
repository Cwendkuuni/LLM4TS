package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(size, mdc.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        int[] size = {-1, 2, 3};
        try {
            new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            assertEquals(-1, size[0]);
            throw e;
        }
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        int[] expected = {0, 0, 0};
        int index = mdc.getTotalSize() - 1;
        assertArrayEquals(expected, mdc.getCounts(index));

        // Test out of bounds
        index = -1;
        try {
            mdc.getCounts(index);
            fail("OutOfRangeException expected");
        } catch (OutOfRangeException e) {
            // Expected
        }

        index = mdc.getTotalSize();
        try {
            mdc.getCounts(index);
            fail("OutOfRangeException expected");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        int[] expected = {0, 1, 2};
        int index = 1;
        int[] counts = mdc.getCounts(index);
        int count = mdc.getCount(expected);
        assertEquals(count, index);

        try {
            mdc.getCount(new int[]{1, 2, 3});
            fail("DimensionMismatchException expected");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        Iterator<Integer> iterator = mdc.iterator();

        // Test hasNext
        for (int i = 0; i < mdc.getTotalSize(); ++i) {
            assertTrue(iterator.hasNext());
        }

        // Test next
        int expected = 0;
        for (int i = 0; i < mdc.getTotalSize(); ++i) {
            int actual = iterator.next();
            assertEquals(expected++, actual);
        }

        // Test getCount
        assertEquals(-1, iterator.getCount());

        // Test getCounts
        int[] expectedCounts = new int[mdc.getDimension()];
        for (int i = 0; i < mdc.getTotalSize(); ++i) {
            iterator.next();
            mdc.iterator().next();
            iterator.next();
            int dimension = mdc.getDimension();
            for (int j = 0; j < dimension; ++j) {
                expectedCounts[j] += mdc.iterator().getCount(j);
            }
        }
        assertArrayEquals(expectedCounts, iterator.getCounts());
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        String expected = "[0][0][0]";
        String actual = mdc.toString();
        assertEquals(expected, actual);
    }
}