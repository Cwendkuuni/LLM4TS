package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
            assertEquals(3, counter.getDimension());
            assertArrayEquals(size, counter.getSizes());
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructionNegativeSize() {
        int[] size = {-2, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testMultidimensionalCounterGetDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testMultidimensionalCounterGetSizes() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertArrayEquals(size, counter.getSizes());
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        String expected = "[0][0][0]";
        String actual = counter.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMultidimensionalCounterIteratorHasNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[0] * size[1] * size[2]; ++i) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultidimensionalCounterIteratorNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int expectedCount = -1;
        int actualCount = 0;
        for (int i = 0; i < size[0] * size[1] * size[2]; ++i) {
            Integer actual = iterator.next();
            assertEquals(i, actual.intValue());
            expectedCount++;
        }
        assertEquals(expectedCount, iterator.getCount());
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountsOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterGetCountsDimensionMismatch() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] wrongDimension = {1, 2, 4};
        counter.getCounts(wrongDimension[0] * wrongDimension[1]);
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expected = {1, 2, 3};
        int[] actual = counter.getCounts(0);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int expected = 0;
        int actual = counter.getCount(new int[]{1, 1, 1});
        assertEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] outOfRange = {1, 10, 1};
        counter.getCount(outOfRange);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterGetCountDimensionMismatch() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] dimensionMismatch = {3, 2, 1};
        counter.getCount(dimensionMismatch);
    }

    @Test
    public void testMultidimensionalCounterGetCountDimensionMatch() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int expected = 1;
        int[] actual = {1, 1, 1};
        int actualCount = counter.getCount(actual);
        assertEquals(expected, actualCount);
    }

    @Test
    public void testMultidimensionalCounterGetCountWithMultipleIterators() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator1 = counter.iterator();
        Iterator iterator2 = counter.iterator();
        int expectedCount1 = 0;
        int expectedCount2 = 1;
        for (int i = 0; i < size[0] * size[1] * size[2] / 2; ++i) {
            Integer num1 = iterator1.next();
            Integer num2 = iterator2.next();
            assertEquals(num1.intValue(), num2.intValue());
            expectedCount1++;
            expectedCount2++;
        }
        assertEquals(expectedCount1, iterator1.getCount());
        assertEquals(expectedCount2, iterator2.getCount());
    }
}