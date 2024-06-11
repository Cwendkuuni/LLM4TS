package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
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
    public void testMultidimensionalCounterNegativeSize() {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterZeroSize() {
        int[] size = {0, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int index = 0;
        int[] counts = counter.getCounts(index);
        assertEquals(2, counts[0]);
        assertEquals(3, counts[1]);
        assertEquals(4, counts[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int index = 25;
        counter.getCounts(index);
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] coordinates = {0, 1, 2};
        int count = counter.getCount(coordinates);
        assertEquals(count, 2 * 3 * 4 - 2 * 3 - 2 * 4 - 3 * 4 + 2 * 3 * 4 - 2 * 3 - 2 * 4 + 3 * 4 - 2 * 3 - 2 * 4 + 3 * 4);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] coordinates = {-1, 1, 2};
        counter.getCount(coordinates);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] coordinates = {0, 1, 5};
        counter.getCount(coordinates);
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int currentIndex = -1;
        while (iterator.hasNext()) {
            int nextIndex = iterator.next();
            if (currentIndex == -1) {
                currentIndex = nextIndex;
            } else {
                assertArrayEquals(new int[]{currentIndex + 1, 0, 0}, iterator.getCounts());
                currentIndex = nextIndex;
            }
        }
        assertEquals(-1, currentIndex);
    }

    @Test
    public void testToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        String counterString = counter.toString();
        String expectedString = "[0][0][0] [1][0][0] [1][1][0] [1][1][1] [2][0][0] [2][1][0] [2][1][1] [2][2][0] [2][2][1] [2][2][2]";
        assertEquals(expectedString, counterString);
    }
}