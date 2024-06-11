package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            assertEquals(3, counter.getDimension());
            assertArrayEquals(size, counter.getSizes());
            assertEquals(2 * 3 * 4, counter.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSize() {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int index = 0;
        int[] expected = {0, 1, 2};
        int[] actual = counter.getCounts(index);
        assertArrayEquals(expected, actual);

        index = 1;
        expected = new int[]{1, 1, 2};
        actual = counter.getCounts(index);
        assertArrayEquals(expected, actual);

        index = 2;
        expected = new int[]{1, 1, 3};
        actual = counter.getCounts(index);
        assertArrayEquals(expected, actual);

        index = 3;
        expected = new int[]{1, 2, 3};
        actual = counter.getCounts(index);
        assertArrayEquals(expected, actual);

        index = 4;
        expected = new int[]{1, 2, 4};
        actual = counter.getCounts(index);
        assertArrayEquals(expected, actual);

        index = 5;
        assertNull(counter.getCounts(index));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int index = 6;
        counter.getCounts(index);
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] coordinates = {0, 1, 2};
        int expected = 0;
        int actual = counter.getCount(coordinates);
        assertEquals(expected, actual);

        coordinates = new int[]{0, 1, 3};
        expected = 3;
        actual = counter.getCount(coordinates);
        assertEquals(expected, actual);

        coordinates = new int[]{1, 1, 2};
        expected = 2;
        actual = counter.getCount(coordinates);
        assertEquals(expected, actual);

        coordinates = new int[]{1, 2, 2};
        expected = 6;
        actual = counter.getCount(coordinates);
        assertEquals(expected, actual);

        coordinates = new int[]{2, 2, 2};
        expected = 4 * 6;
        actual = counter.getCount(coordinates);
        assertEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] coordinates = {0, 1, 5};
        counter.getCount(coordinates);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] coordinates = {0, 4, 2};
        counter.getCount(coordinates);
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int currentCount = -1;
        MultidimensionalCounter.MultidimensionalCounterIterator iterator = counter.iterator();
        while (iterator.hasNext()) {
            int nextCount = iterator.next();
            assertTrue(nextCount > currentCount);
            currentCount = nextCount;
        }

        assertEquals(counter.getSize(), currentCount + 1);
    }
}