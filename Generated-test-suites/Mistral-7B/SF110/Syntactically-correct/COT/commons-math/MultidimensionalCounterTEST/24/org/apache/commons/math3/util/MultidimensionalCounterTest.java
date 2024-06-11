package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstructor() {
        int[] size = new int[]{2, 3, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
            assertEquals(3, counter.getDimension());
            assertArrayEquals(size, counter.getSizes());
            assertEquals(72, counter.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructorNegativeDimension() {
        int[] size = new int[]{-1, 3, 1};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructorZeroSizeDimension() {
        int[] size = new int[]{0};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        assertEquals(3, counter.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegativeIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        counter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRangeIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        counter.getCounts(72);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        int[] result = counter.getCounts(3);
        assertEquals(new int[]{1, 2, 1}, result);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        counter.getCounts(new int[]{2, 3, 2});
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        int result = counter.getCount(new int[]{1, 1, 1});
        assertEquals(1, result);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        counter.getCount(new int[]{-1, 1, 1});
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        counter.getCount(new int[]{2, 3, 1});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        counter.getCount(new int[]{2, 3, 2});
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        assertEquals(72, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        int[] result = counter.getSizes();
        assertArrayEquals(new int[]{2, 3, 1}, result);
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        String result = counter.toString();
        assertEquals("[0][0][0]", result);
    }

    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        Iterator<Integer> iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        // Last element
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        Iterator<Integer> iterator = counter.iterator();
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        // Last element
        assertEquals(71, iterator.next().intValue());
    }

    @Test
    public void testIteratorGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        Iterator<Integer> iterator = counter.iterator();
        assertEquals(0, iterator.getCount());
        assertEquals(1, iterator.getCount());
        assertEquals(2, iterator.getCount());
        // Last element
        assertEquals(71, iterator.getCount());
    }

    @Test
    public void testIteratorGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        Iterator<Integer> iterator = counter.iterator();
        int[] expected = new int[]{0, 1, 0};
        int[] result = iterator.getCounts();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testIteratorGetCountAtDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 1});
        Iterator<Integer> iterator = counter.iterator();
        assertEquals(0, iterator.getCount(0));
        assertEquals(1, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
    }
}