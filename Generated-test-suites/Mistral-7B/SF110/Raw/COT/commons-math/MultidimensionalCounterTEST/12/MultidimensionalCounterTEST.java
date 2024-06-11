package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

public class MultidimensionalCounterTest
{
    @Test
    public void testConstructor() throws Exception
    {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() throws Exception
    {
        int[] size = {-1, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        assertEquals(3, counter.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegativeIndex()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        counter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRangeIndex()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        counter.getCounts(25);
    }

    @Test
    public void testGetCounts()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        int[] actual = counter.getCounts(5);
        int[] expected = {1, 2, 3};
        assertArrayEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        counter.getCount(-1, 0);
        counter.getCount(-1, 1);
        counter.getCount(-1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex1D()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        counter.getCount(3, 0);
        counter.getCount(3, 1);
        counter.getCount(3, 2);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountOutOfRangeIndex2D()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        counter.getCount(new int[]{3, 3}, 0);
    }

    @Test
    public void testGetCount()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        int actual = counter.getCount(new int[]{1, 1, 1});
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCount1D()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        int actual = counter.getCount(new int[]{1, 1});
        int expected = 4;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCount2D()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        int actual = counter.getCount(new int[]{1, 1, 1});
        int expected = 1;
        assertEquals(expected, actual);

        actual = counter.getCount(new int[]{1, 2, 1});
        expected = 3;
        assertEquals(expected, actual);

        actual = counter.getCount(new int[]{1, 2, 2});
        expected = 6;
        assertEquals(expected, actual);
    }

    @Test
    public void testIteratorHasNext()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 24; ++i) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 24; ++i) {
            assertEquals(i + 1, iterator.next());
        }
    }

    @Test
    public void testIteratorGetCount()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 24; ++i) {
            assertEquals(i + 1, iterator.getCount());
        }
    }

    @Test
    public void testIteratorGetCounts()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 24; ++i) {
            int[] counts = iterator.getCounts();
            int[] expected = new int[]{i / 4, i / 3 % 4, i % 4};
            assertArrayEquals(expected, counts);
        }
    }

    @Test
    public void testIteratorGetCount1D()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 24; ++i) {
            int count = iterator.getCount(0);
            int expected = i / 4 + 1;
            assertEquals(expected, count);
        }
    }

    @Test
    public void testIteratorGetCount2D()
    {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3, 4});
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < 24; ++i) {
            int[] counts = iterator.getCounts();
            int countX = counts[0];
            int countY = counts[1];
            int countZ = counts[2];
            int expectedX = i / (4 * 3) + 1;
            int expectedY = (i / (4 * 3)) % 3 + 1;
            int expectedZ = (i / 4) % 4 + 1;
            assertEquals(expectedX, countX);
            assertEquals(expectedY, countY);
            assertEquals(expectedZ, countZ);
        }
    }
}