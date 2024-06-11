package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link MultidimensionalCounter}.
 */
public class MultidimensionalCounterTest {

    /**
     * Test constructor with valid sizes.
     */
    @Test
    public void testValidSizes() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(6 * 4 * 3, counter.getSize());
    }

    /**
     * Test constructor with invalid sizes.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSizes() {
        int[] sizes = {0, 2, 4};
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test getting multidimensional counts from unidimensional index.
     */
    @Test
    public void testGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] counts = counter.getCounts(0);
        assertArrayEquals(new int[]{0, 0, 0}, counts);

        counts = counter.getCounts(12);
        assertArrayEquals(new int[]{1, 0, 0}, counts);

        counts = counter.getCounts(23);
        assertArrayEquals(new int[]{1, 3, 2}, counts);
    }

    /**
     * Test getting unidimensional count from multidimensional counts.
     */
    @Test
    public void testGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int count = counter.getCount(0, 0, 0);
        assertEquals(0, count);

        count = counter.getCount(1, 0, 0);
        assertEquals(12, count);

        count = counter.getCount(1, 3, 2);
        assertEquals(23, count);

        count = counter.getCount(1, 1, 1);
        assertEquals(14, count);
    }

    /**
     * Test out of range index in multidimensional counter.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1, 0, 0);
    }

    /**
     * Test out of range index in unidimensional counter.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeUnidimensionalIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(100, 0, 0);
    }

    /**
     * Test invalid dimension index in multidimensional counter.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidDimensionIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(0, 5, 0);
    }

    /**
     * Test invalid dimension index in unidimensional counter.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testInvalidUnidimensionalIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(0, 0, 5);
    }
}