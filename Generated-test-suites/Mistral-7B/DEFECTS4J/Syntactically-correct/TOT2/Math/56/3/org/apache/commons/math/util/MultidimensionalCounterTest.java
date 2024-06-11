package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for MultidimensionalCounter.
 */
public class MultidimensionalCounterTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertEquals(3 * 4 * 5, mdc.getSize());
        int[] sizesCopy = mdc.getSizes();
        assertArrayEquals(sizes, sizesCopy);
    }

    /**
     * Test getCounts method with valid index.
     */
    @Test
    public void testGetCountsWithValidIndex() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int[] counts = mdc.getCounts(0);
        assertArrayEquals(new int[] {0, 0, 0}, counts);

        counts = mdc.getCounts(mdc.getSize() - 1);
        assertArrayEquals(new int[] {2, 3, 4}, counts);
    }

    /**
     * Test getCounts method with out-of-range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithOutOfRangeIndex() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCounts(-1);
    }

    /**
     * Test getCount method with valid index.
     */
    @Test
    public void testGetCountWithValidIndex() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int count = mdc.getCount(0, 0, 0);
        assertEquals(0, count);

        count = mdc.getCount(2, 3, 4);
        assertEquals(mdc.getSize() - 1, count);
    }

    /**
     * Test getCount method with out-of-range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeIndex() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(-1, 0, 0);
    }

    /**
     * Test getCount method with out-of-range index in one dimension.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeIndexInOneDimension() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(0, 5, 0);
    }

    /**
     * Test getCount method with out-of-range index in all dimensions.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeIndexInAllDimensions() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(5, 5, 5);
    }

    /**
     * Test iterator.
     */
    @Test
    public void testIterator() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        int index = 0;
        while (it.hasNext()) {
            Integer next = it.next();
            assertEquals(index, next.intValue());
            int[] counts = it.getCounts();
            assertArrayEquals(new int[] {index / (sizes[0] * sizes[1]) % sizes[2], index / (sizes[0] * sizes[1]) / sizes[2] % sizes[1], index / (sizes[0] * sizes[1]) / (sizes[1] * sizes[2])}, counts);
            index++;
        }
    }
}