package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterWithNegativeSizes() {
        int[] sizes = {-1, 2, 3};
        try {
            new MultidimensionalCounter(sizes);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals("The size in dimension 0 must be positive: -1", e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterWithTotalSizeLessThanZero() {
        int[] sizes = {1, 2, -1};
        try {
            new MultidimensionalCounter(sizes);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals("The total size must be non-negative: -1", e.getMessage());
        }
    }

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Assert.assertEquals(3, mdc.getDimension());
        Assert.assertArrayEquals(new int[]{1, 2, 3}, mdc.getSizes());
        Assert.assertEquals(6, mdc.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int[] expected = {0, 0, 0};
        Assert.assertArrayEquals(expected, mdc.getCounts(0));
        expected = new int[]{2, 0, 0};
        Assert.assertArrayEquals(expected, mdc.getCounts(2));
        expected = new int[]{1, 1, 1};
        Assert.assertArrayEquals(expected, mdc.getCounts(3));
        try {
            mdc.getCounts(6);
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongDimension() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCounts(4);
    }

    @Test
    public void testGetCount() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Assert.assertEquals(0, mdc.getCount(new int[]{0, 0, 0}));
        Assert.assertEquals(0, mdc.getCount(new int[]{2, 0, 0}));
        Assert.assertEquals(0, mdc.getCount(new int[]{1, 1, 1}));
        Assert.assertEquals(2, mdc.getCount(new int[]{1, 0, 0}));
        Assert.assertEquals(1, mdc.getCount(new int[]{1, 1, 0}));
        Assert.assertEquals(3, mdc.getCount(new int[]{1, 1, 1}));
        try {
            mdc.getCount(new int[]{4, 4, 4});
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        try {
            mdc.getCount(new int[]{1, 2, 4});
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            mdc.getCount(new int[]{1, 2, -1});
            Assert.fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testIterator() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();
        int expected = 0;
        int counter[] = new int[mdc.getDimension()];

        while (it.hasNext()) {
            int next = it.next();
            Assert.assertTrue(next >= 0);
            if (next < expected) {
                Assert.fail("Expected next to be greater than or equal to previous value");
            }
            counter[mdc.getLastIndexOf(next)]++;
            expected = next + 1;
        }

        Assert.assertArrayEquals(new int[]{6, 3, 1}, counter);
    }

    @Test
    public void testToString() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        String expected = "[0] [0] [0] [1] [0] [1] [1] [2] [0] [1] [1] [2] [0] [1] [1] [2] [0] [1] [1] [2] [0] [1] [1] [2] [0] [1] [1] [2] [0] [1]";
        Assert.assertEquals(expected, mdc.toString());
    }

    @Test
    public void testIteratorGetCounts() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();
        int expected[] = new int[mdc.getDimension()];

        while (it.hasNext()) {
            int next = it.next();
            expected = mdc.getCounts(next);
            Assert.assertArrayEquals(Arrays.copyOf(new int[]{next}, mdc.getDimension()), expected);
        }
    }

    @Test
    public void testIteratorGetCount() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();
        int expected;

        while (it.hasNext()) {
            int next = it.next();
            expected = mdc.getCount(new int[]{mdc.getLastIndexOf(next)});
            Assert.assertEquals(next, expected);
        }
    }
}