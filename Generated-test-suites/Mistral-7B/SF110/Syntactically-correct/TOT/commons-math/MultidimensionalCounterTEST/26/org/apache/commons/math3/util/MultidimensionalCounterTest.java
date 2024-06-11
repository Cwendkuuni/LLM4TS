package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = new int[]{3, 4, 5};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(new int[]{3, 4, 5}, counter.getSizes());
        Assert.assertEquals(18275, counter.getSize());
    }

    @Test
    public void testGetCountForValidIndices() {
        int[] size = new int[]{3, 4, 5};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        int[] validIndices = new int[]{1, 2, 2};
        int expected = 2 * size[1] + 2 * size[2];
        int actual = counter.getCount(validIndices);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetCountForInvalidDimension() {
        int[] size = new int[]{3, 4, 5};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        int[] invalidDimensions = new int[]{3, 5};
        try {
            counter.getCount(invalidDimensions);
            Assert.fail("Expected OutOfRangeException, but no exception was thrown.");
        } catch (DimensionMismatchException e) {
            // Expected
        } catch (OutOfRangeException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetCountForInvalidIndices() {
        int[] size = new int[]{3, 4, 5};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        int[] invalidIndices = new int[]{-1, 9, 2};
        try {
            counter.getCount(invalidIndices);
            Assert.fail("Expected OutOfRangeException, but no exception was thrown.");
        } catch (OutOfRangeException e) {
            // Expected
        } catch (DimensionMismatchException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetCountsForValidIndex() {
        int[] size = new int[]{3, 4, 5};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        int index = 12;
        int[] expectedIndices = new int[]{1, 1, 2};
        int[] expectedCounts = new int[]{2, 3, 3};
        int[] actualCounts = counter.getCounts(index);
        Assert.assertArrayEquals(expectedIndices, actualCounts);
        Assert.assertArrayEquals(expectedCounts, counter.getSizes());
    }

    @Test
    public void testGetCountsForInvalidIndex() {
        int[] size = new int[]{3, 4, 5};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        int index = -1;
        try {
            counter.getCounts(index);
            Assert.fail("Expected OutOfRangeException, but no exception was thrown.");
        } catch (OutOfRangeException e) {
            // Expected
        } catch (DimensionMismatchException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = new int[]{3, 4, 5};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Iterator<Integer> iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        for (int i = 0; i < 18274; ++i) {
            iterator.next();
            Assert.assertTrue(iterator.hasNext());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] size = new int[]{3, 4, 5};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 18274; ++i) {
            int next = iterator.next();
            Assert.assertEquals(i + 1, next);
        }
        try {
            iterator.next();
            Assert.fail("Expected NoSuchElementException, but no exception was thrown.");
        } catch (NoSuchElementException e) {
            // Expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] size = new int[]{3, 4, 5};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Iterator<Integer> iterator = counter.iterator();
        iterator.remove();
    }
}