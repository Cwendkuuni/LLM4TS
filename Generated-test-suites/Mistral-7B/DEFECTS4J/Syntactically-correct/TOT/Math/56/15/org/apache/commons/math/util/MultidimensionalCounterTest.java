package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link MultidimensionalCounter}.
 */
public class MultidimensionalCounterTest {

    /**
     * Test counter creation.
     */
    @Test
    public void testMultidimensionalCounterCreation() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertEquals(sizes, mdc.getSizes());
        assertEquals(72, mdc.getSize());
    }

    /**
     * Test iterator.
     */
    @Test
    public void testIterator() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        int index = 0;
        while (it.hasNext()) {
            int next = it.next();
            assertEquals(index, next);
            index++;
        }
        assertFalse(it.hasNext());
    }

    /**
     * Test getCounts.
     */
    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < sizes[i]; j++) {
                int[] counts = mdc.getCounts(mdc.getCount(i, j));
                for (int k = 0; k < sizes.length; k++) {
                    assertEquals(i, counts[k]);
                }
            }
        }
    }

    /**
     * Test getCount.
     */
    @Test
    public void testGetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < sizes[i]; j++) {
                int count = mdc.getCount(i, j);
                int[] counts = mdc.getCounts(count);
                assertEquals(i, counts[count.length - 1]);
                assertEquals(j, counts[count.length - 2]);
                assertEquals(i, counts[count.length - 3]);
                assertEquals(j, counts[count.length - 4]);
            }
        }
    }

    /**
     * Test exception when trying to get count out of bounds.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBounds() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(-1, -1);
    }

    /**
     * Test exception when trying to create counter with negative sizes.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterCreationNegativeSizes() {
        int[] sizes = {-1, 3, 4};
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test exception when trying to create counter with non-integer sizes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultidimensionalCounterCreationNonIntegerSizes() {
        int[] sizes = {2, 3.5, 4};
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test exception when trying to get count with negative index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(-1, -1);
    }

    /**
     * Test exception when trying to get count with index out of bounds in one dimension.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountIndexOutOfBoundsOneDimension() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int[] indices = {2, 3};
        mdc.getCount(indices);
    }

    /**
     * Test exception when trying to get count with index out of bounds in multiple dimensions.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountIndexOutOfBoundsMultipleDimensions() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int[] indices = {2, 3, 5};
        mdc.getCount(indices);
    }
}