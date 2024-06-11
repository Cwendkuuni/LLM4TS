package org.apache.commons.math.util;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for the MultidimensionalCounter class.
 */
public class MultidimensionalCounterTest {

    /**
     * Test the constructor with valid sizes.
     */
    @Test
    public void testValidConstruction() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Assert.assertEquals(2 * 4 * 3, mdc.getSize());
    }

    /**
     * Test the iterator.
     */
    @Test
    public void testIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator iterator = mdc.iterator();

        // Test hasNext
        for (int i = 0; i < 2 * 4 * 3; i++) {
            Assert.assertTrue(iterator.hasNext());
        }
        Assert.assertFalse(iterator.hasNext());

        // Test next
        int index = -1;
        while (iterator.hasNext()) {
            index = iterator.next();
            Assert.assertEquals(index, mdc.getCount());
            mdc.next();
        }
        Assert.assertEquals(-1, index);

        // Test getCounts
        int[] counts = null;
        int[] expectedCounts = {0, 0, 0};
        while (iterator.hasNext()) {
            counts = iterator.getCounts();
            Assert.assertArrayEquals(expectedCounts, counts);
            mdc.next();
            expectedCounts = MathUtils.increment(expectedCounts);
        }

        // Test getCount
        int count = -1;
        while (iterator.hasNext()) {
            count = mdc.getCount();
            Assert.assertEquals(count, iterator.getCount());
            mdc.next();
        }
    }

    /**
     * Test the constructor with zero size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSizeConstruction() {
        int[] sizes = {0, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test the constructor with negative size.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizeConstruction() {
        int[] sizes = {2, -4, 3};
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test the getCounts method with index out of bounds.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfBoundsGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCounts(2 * 4 * 3);
    }

    /**
     * Test the getCount method with index out of bounds.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfBoundsGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(-1);
    }

    /**
     * Test the getCount method with multidimensional index out of bounds.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfBoundsGetCountMultidimensional() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(2, 4, 3);
    }

    /**
     * Test the getCounts method with multidimensional index out of bounds.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfBoundsGetCountsMultidimensional() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCounts(2, 4, 3);
    }

    /**
     * Test the getCounts method with dimensionality mismatch.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCounts(2, 5, 3);
    }

    /**
     * Test the getCount method with multidimensional index.
     */
    @Test
    public void testGetCountMultidimensional() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int count = mdc.getCount(1, 2, 1);
        Assert.assertEquals(count, mdc.getCounts(1, 2, 1)[0] + mdc.getCounts(1, 2, 1)[1] + mdc.getCounts(1, 2, 1)[2]);
    }
}