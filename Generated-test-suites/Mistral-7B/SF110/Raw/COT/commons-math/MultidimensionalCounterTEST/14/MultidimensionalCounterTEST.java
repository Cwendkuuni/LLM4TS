package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest
{
    @Test
    public void testMultidimensionalCounterConstruction() throws Exception
    {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3 * 2 * 4, counter.getSize());
        assertEquals(3, counter.getDimension());
        assertEquals(Arrays.asList(new int[] {2, 3, 4}), Arrays.asList(counter.getSizes()));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructionWithNegativeSize()
    {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructionWithZeroSize() throws Exception
    {
        int[] size = {2, 0, 4};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        assertEquals(3, counter.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        counter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfTotalSize() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        counter.getCounts(counter.getSize());
    }

    @Test
    public void testGetCounts() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        int[] counts = counter.getCounts(12);
        assertEquals(3, counts.length);
        assertEquals(2, counts[0]);
        assertEquals(3, counts[1]);
        assertEquals(4, counts[2]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithDimensionMismatch() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        counter.getCounts(new int[] {2, 3, 5});
    }

    @Test
    public void testGetCount() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        int count = counter.getCount(1, 1, 1);
        assertEquals(1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1, count);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeFirstDimension() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        counter.getCount(-1, 1, 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeSecondDimension() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        counter.getCount(1, -1, 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeThirdDimension() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        counter.getCount(1, 1, -1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithDimensionMismatch() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        counter.getCount(new int[] {2, 3, 5});
    }

    @Test
    public void testToString() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        assertEquals("[0][0][0]", counter.toString());
    }

    @Test
    public void testIteratorHasNext() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        Iterator iterator = counter.iterator();
        assertEquals(0, iterator.next());
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
        assertEquals(3, iterator.next());
        assertEquals(4, iterator.next());
    }

    @Test
    public void testIteratorGetCount() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        Iterator iterator = counter.iterator();
        iterator.next();
        iterator.next();
        assertEquals(2, iterator.getCount());
    }

    @Test
    public void testIteratorGetCounts() throws Exception
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
        Iterator iterator = counter.iterator();
        iterator.next();
        iterator.next();
        int[] counts = iterator.getCounts();
        assertEquals(3, counts.length);
        assertEquals(2, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(1, counts[2]);
    }
}