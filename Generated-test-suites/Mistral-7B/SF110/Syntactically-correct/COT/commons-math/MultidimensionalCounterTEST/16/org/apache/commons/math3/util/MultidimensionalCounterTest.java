package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertNotNull(mdc);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(size, mdc.getSizes());
        assertEquals(144, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSize() {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithTotalSizeLessThanZero() {
        int[] size = {2, 3, 4};
        int totalSize = -1;
        new MultidimensionalCounter(size, totalSize);
    }

    @Test
    public void testGetDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCounts(-1);
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int index = 6;
        int[] counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertArrayEquals(new int[] {1, 1, 2}, counts);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCounts(new int[] {5, 5});
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] c = {1, 2, 3};
        int count = mdc.getCount(c);
        assertEquals(13, count);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithNegativeIndex() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(new int[] {-1, 2, 3});
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeIndex() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(new int[] {10, 2, 3});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongDimensionSize() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(new int[] {1, 5, 3});
    }

    @Test
    public void testToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        String expected = "[1][1][2]";
        assertEquals(expected, mdc.toString());
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator it = mdc.iterator();
        assertFalse(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator it = mdc.iterator();
        it.next();
        int actual = it.next();
        assertEquals(1, actual);
    }

    @Test
    public void testIteratorGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator it = mdc.iterator();
        it.next();
        int count = it.getCount();
        assertEquals(1, count);
    }

    @Test
    public void testIteratorGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator it = mdc.iterator();
        it.next();
        int[] counts = it.getCounts();
        assertArrayEquals(new int[] {0, 0, 0}, counts);
    }

}