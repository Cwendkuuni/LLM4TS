package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertEquals(Arrays.asList(2, 3, 4), Arrays.asList(mdc.getSizes()));
        assertEquals(2 * 3 * 4, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        int[] sizes = {-2, 3, 4};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int index = 0;
        int[] counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        index = 1;
        counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(1, counts[2]);

        index = 2;
        counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(2, counts[2]);

        index = 3;
        counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(2, counts[2]);
        assertEquals(1, counts[2]);

        index = 4;
        counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(2, counts[2]);
        assertEquals(1, counts[2]);
        assertEquals(1, counts[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int index = 7;
        mdc.getCounts(index);
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int[] count = {0, 1, 2};
        int result = mdc.getCount(count);
        assertEquals(4, result);

        count = new int[]{3, 0, 2};
        result = mdc.getCount(count);
        assertEquals(11, result);

        count = new int[]{-1, 1, 2};
        try {
            mdc.getCount(count);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            //expected
        }

        count = new int[]{3, 3, 4};
        try {
            mdc.getCount(count);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            //expected
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int[] count = {3};
        mdc.getCount(count);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int[] count = {2, 3, 5};
        mdc.getCount(count);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator it = mdc.iterator();
        for (int i = 0; i < mdc.getSize(); ++i) {
            assertTrue(it.hasNext());
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator it = mdc.iterator();
        for (int i = 0; i < mdc.getSize(); ++i) {
            int result = it.next();
            assertEquals(i + 1, result);
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testIteratorGetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator it = mdc.iterator();
        for (int i = 0; i < mdc.getSize(); ++i) {
            int result = it.getCount();
            assertEquals(i + 1, result);
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testIteratorGetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator it = mdc.iterator();
        for (int i = 0; i < mdc.getSize(); ++i) {
            int[] result = it.getCounts();
            int[] expected = new int[sizes.length];
            for (int j = 0; j < sizes.length; ++j) {
                expected[j] = i / Math.pow(sizes[j], sizes.length - 1) % sizes[j];
            }
            assertArrayEquals(expected, result);
        }
        assertFalse(it.hasNext());
    }
}