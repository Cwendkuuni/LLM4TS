package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
            assertEquals(3, counter.getDimension());
            int[] expectedSizes = { 3, 4, 5 };
            assertArrayEquals(expectedSizes, counter.getSizes());
            assertEquals(13825, counter.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructionNegativeSize() {
        int[] size = { -3, 4, 5 };
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructionZeroDimension() {
        int[] size = {};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegativeIndex() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRangeIndex() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(Integer.MAX_VALUE);
    }

    @Test
    public void testGetCountsIndexInRange() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expectedIndices = { 2, 3, 1 };
        int[] actualIndices = counter.getCounts(5);
        assertArrayEquals(expectedIndices, actualIndices);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(Integer.MAX_VALUE);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] wrongSize = { 3, 4, 6 };
        counter.getCount(wrongSize);
    }

    @Test
    public void testGetCountCorrectIndex() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] correctIndex = { 1, 2, 3 };
        int actualCount = counter.getCount(correctIndex);
        int expectedCount = 11 * 4 * 3 + 2 * 4 + 3;
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetSize() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(13825, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expectedSizes = { 3, 4, 5 };
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testToString() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        String expectedString = "[3][4][5]";
        String actualString = counter.toString();
        assertEquals(expectedString, actualString);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] size = { 3, 4, 5 };
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        iterator.next();
        iterator.remove();
    }
}