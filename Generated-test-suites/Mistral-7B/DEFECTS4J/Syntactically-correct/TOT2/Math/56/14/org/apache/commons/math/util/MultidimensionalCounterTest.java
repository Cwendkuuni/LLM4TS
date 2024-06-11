package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Tests for the MultidimensionalCounter class.
 */
public class MultidimensionalCounterTest {
    /**
     * Test constructor with valid sizes.
     */
    @Test
    public void testConstructorValid() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        assertEquals(3, mc.getDimension());
        assertEquals(2 * 3 * 4, mc.getSize());
    }

    /**
     * Test constructor with empty sizes.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorEmptySize() {
        int[] sizes = {};
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test constructor with negative sizes.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        int[] sizes = {-1, 2, 3};
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test getCounts method with valid index.
     */
    @Test
    public void testGetCountsValid() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        int[] counts = mc.getCounts(15);
        assertEquals(3, counts.length);
        assertEquals(1, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(1, counts[2]);
    }

    /**
     * Test getCounts method with out of range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        mc.getCounts(16);
    }

    /**
     * Test getCount method with valid index.
     */
    @Test
    public void testGetCountValid() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        int index = 15;
        int count = mc.getCount(index);
        assertEquals(15, index);
        int[] counts = mc.getCounts(index);
        assertEquals(1, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(1, counts[2]);
        assertEquals(count, mc.getCount(counts[0], counts[1], counts[2]));
    }

    /**
     * Test getCount method with out of range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        int index = 16;
        mc.getCount(index);
    }

    /**
     * Test iterator hasNext method with valid state.
     */
    @Test
    public void testIteratorHasNextValid() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        Iterator it = mc.iterator();
        assertTrue(it.hasNext());
        int count = it.next();
        assertEquals(0, count);
        assertEquals(new int[] {0, 0, 0}, it.getCounts());
        assertTrue(it.hasNext());
        count = it.next();
        assertEquals(1, count);
        assertEquals(new int[] {1, 0, 0}, it.getCounts());
        // ... continue testing the iterator
    }

    /**
     * Test iterator hasNext method with exhausted state.
     */
    @Test
    public void testIteratorHasNextExhausted() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        Iterator it = mc.iterator();
        // Advance the iterator to the end
        for (int i = 0; i < mc.getSize(); i++) {
            it.next();
        }
        assertFalse(it.hasNext());
    }

    /**
     * Test constructor with invalid sizes.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorInvalidSizes() {
        int[] sizes = {2, 3, 3};
        new MultidimensionalCounter(sizes);
    }

    /**
     * Test iterator getCount method with valid dimensions and index.
     */
    @Test
    public void testIteratorGetCountValid() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        Iterator it = mc.iterator();
        int[] counts = it.getCounts();
        int dim = 1;
        int index = counts[dim];
        int count = it.getCount(dim);
        assertEquals(index, count);
    }

    /**
     * Test iterator getCount method with invalid dimension.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIteratorGetCountInvalidDimension() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        Iterator it = mc.iterator();
        int[] counts = it.getCounts();
        int dim = counts.length + 1;
        it.getCount(dim);
    }

    /**
     * Test iterator getCounts method with invalid index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIteratorGetCountsInvalidIndex() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        Iterator it = mc.iterator();
        it.getCounts(16);
    }

    /**
     * Test iterator remove method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemoveUnsupported() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        Iterator it = mc.iterator();
        it.remove();
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mc = new MultidimensionalCounter(sizes);
        String expected = "[1, 2, 3]";
        String actual = mc.toString();
        assertEquals(expected, actual);
    }
}