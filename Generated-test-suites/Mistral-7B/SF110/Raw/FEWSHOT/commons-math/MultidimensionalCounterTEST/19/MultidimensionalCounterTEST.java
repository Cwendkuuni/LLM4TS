package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] sizes = {3, 2, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(sizes);
            assertEquals(3, counter.getDimension());
            assertArrayEquals(sizes, counter.getSizes());
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception during MultidimensionalCounter construction.");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        int[] sizes = {-3, 2, 1};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testMultidimensionalCounterGetDimension() {
        int[] sizes = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testMultidimensionalCounterGetSizes() {
        int[] sizes = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertArrayEquals(sizes, counter.getSizes());
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] sizes = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        int expectedCount = 0;
        int currentCount = 0;

        while (iterator.hasNext()) {
            int next = iterator.next();
            currentCount++;
            assertEquals(expectedCount, next);
            expectedCount++;
        }

        assertEquals(counter.getSize(), expectedCount);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountOutOfRange() {
        int[] sizes = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountOutOfRangeDifferentDimension() {
        int[] sizes = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(1, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterGetCountDimensionMismatch() {
        int[] sizes = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(3, 1);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] sizes = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < counter.getDimension(); i++) {
            int j = 0;
            int expectedCount = 0;
            int currentCount = 0;

            while (counter.hasNext()) {
                int[] next = counter.next();
                if (Arrays.equals(new int[]{i, j}, next)) {
                    currentCount++;
                }
                j++;
            }

            assertEquals(expectedCount, currentCount);
            expectedCount += sizes[i];
        }
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] sizes = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expectedToString = "[0][0][0] [0][0][1] [0][0][2] [0][1][0] [0][1][1] [0][1][2] [1][0][0] [1][0][1] [1][0][2] [1][1][0] [1][1][1] [1][1][2] [2][0][0] [2][0][1] [2][0][2] [2][1][0] [2][1][1] [2][1][2]";
        assertEquals(expectedToString, counter.toString());
    }

    @Test
    public void testMultidimensionalCounterIteratorGetCounts() {
        int[] sizes = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        int expectedCount = 0;
        Integer currentCount = null;

        while (iterator.hasNext()) {
            currentCount = iterator.next();
            int[] nextCounts = iterator.getCounts();
            assertArrayEquals(new int[]{expectedCount / sizes[counter.getDimension() - 1], expectedCount % sizes[counter.getDimension() - 1]}, nextCounts);
            expectedCount++;
        }

        assertEquals(counter.getSize(), expectedCount);
    }

    @Test
    public void testMultidimensionalCounterIteratorGetCount() {
        int[] sizes = {3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        int expectedCount = 0;
        int currentDimension = -1;
        int currentCount = 0;

        while (iterator.hasNext()) {
            currentCount = iterator.next();
            int currentDimensionTemp = counter.getDimension() - 1;

            if (currentDimension != currentDimensionTemp) {
                assertEquals(expectedCount / Math.pow(sizes[currentDimensionTemp], counter.getDimension() - currentDimension - 1), currentCount);
                expectedCount += sizes[currentDimensionTemp] * currentCount;
                currentDimension = currentDimensionTemp;
            }
        }

        assertEquals(counter.getSize(), expectedCount);
    }
}