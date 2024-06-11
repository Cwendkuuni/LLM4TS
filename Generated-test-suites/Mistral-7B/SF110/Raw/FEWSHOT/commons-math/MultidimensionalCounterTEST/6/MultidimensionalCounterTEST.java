package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(2 * 3 * 4, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructionWithNegativeSize() {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructionWithZeroSize() {
        int[] size = {0, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructionWithOneDimensionZeroSize() {
        int[] size = {0};
        new MultidimensionalCounter(size);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithInvalidDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] invalidDimension = {5};
        counter.getCounts(5);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithOutOfRangeIndex() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] outOfRangeIndex = {2, 5};
        counter.getCounts(counter.getTotalSize() + 1);
    }

    @Test
    public void testGetCountsWithValidIndex() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] validIndex = {0, 1, 2};
        int[] expected = {0, 1, 2};
        assertArrayEquals(expected, counter.getCounts(counter.getTotalSize() - 3));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithInvalidDimensionLength() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] invalidDimensionLength = {2, 5};
        counter.getCount(invalidDimensionLength);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithOutOfRangeIndexInOneDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] outOfRangeIndex = {2, 5, 0};
        counter.getCount(outOfRangeIndex);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithOutOfRangeIndexInSecondDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] outOfRangeIndex = {2, 5, 5};
        counter.getCount(outOfRangeIndex);
    }

    @Test
    public void testGetCountsWithValidIndexInOneDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] validIndex = {0, 1, 2};
        int expected = 2 * 3 * 3 + 2 * 3 * 2 + 2 * 3;
        assertEquals(expected, counter.getCount(validIndex));
    }

    @Test
    public void testGetCountsWithValidIndexInSecondDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] validIndex = {0, 2, 2};
        int expected = 2 * 3 * 4 + 2 * 3 * 3 + 2 * 3 * 2 + 2 * 3;
        assertEquals(expected, counter.getCount(validIndex));
    }

    @Test
    public void testMultidimensionalCounterIteratorHasNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        // Last element
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultidimensionalCounterIteratorNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        Integer next = iterator.next();
        assertEquals(0, next.intValue());
        next = iterator.next();
        assertEquals(1, next.intValue());
        next = iterator.next();
        assertEquals(2, next.intValue());
        // Last element
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultidimensionalCounterIteratorGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int count = iterator.getCount();
        assertEquals(0, count);
        iterator.next();
        count = iterator.getCount();
        assertEquals(1, count);
        iterator.next();
        count = iterator.getCount();
        assertEquals(2, count);
        // Last element
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        String expected = "[0][0][0] [0][0][1] [0][0][2] [0][1][0] [0][1][1] [0][1][2] [0][2][0] [0][2][1] [0][2][2] [1][0][0] [1][0][1] [1][0][2] [1][1][0] [1][1][1] [1][1][2] [1][2][0] [1][2][1] [1][2][2]";
        assertEquals(expected, counter.toString());
    }

}