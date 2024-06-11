package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest
{
    private final int[] size;
    private final MultidimensionalCounter multidimensionalCounter;

    public MultidimensionalCounterTest(int... size)
    {
        this.size = size;
        try {
            multidimensionalCounter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException: " + e.getMessage());
        }
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data()
    {
        return Arrays.asList(new Object[][] {
                new Object[] {new int[] {}},
                new Object[] {new int[] {1}},
                new Object[] {new int[] {1, 1}},
                new Object[] {new int[] {1, 2, 1}},
                new Object[] {new int[] {1, 1, 1, 1}}
        });
    }

    @Test
    public void testGetDimension()
    {
        assertEquals(multidimensionalCounter.getDimension(), size.length);
    }

    @Test
    public void testGetTotalSize()
    {
        int totalSize = 1;
        for (int i = 0; i < size.length; ++i) {
            totalSize *= size[i];
        }
        assertEquals(multidimensionalCounter.getSize(), totalSize);
    }

    @Test
    public void testGetSizes()
    {
        int[] sizes = multidimensionalCounter.getSizes();
        assertArrayEquals(size, sizes);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegativeIndex()
    {
        multidimensionalCounter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRangeIndex()
    {
        int index = multidimensionalCounter.getTotalSize();
        multidimensionalCounter.getCounts(index);
    }

    @Test
    public void testGetCountsValidIndex()
    {
        int index = 0;
        int[] counts = multidimensionalCounter.getCounts(index);
        assertEquals(counts.length, size.length);
        for (int i = 0; i < size.length; ++i) {
            assertEquals(counts[i], 0);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex()
    {
        multidimensionalCounter.getCount(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex()
    {
        int index = multidimensionalCounter.getTotalSize();
        multidimensionalCounter.getCount(index);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimension()
    {
        int[] c = new int[size.length + 1];
        multidimensionalCounter.getCount(c);
    }

    @Test
    public void testGetCountValidIndex()
    {
        int index = 0;
        int expectedCount = 0;
        for (int i = 0; i < size.length; ++i) {
            expectedCount += size[i];
        }
        assertEquals(multidimensionalCounter.getCount(new int[size.length]), expectedCount);
    }

    @Test
    public void testIteratorHasNext()
    {
        Iterator iterator = multidimensionalCounter.iterator();
        assertTrue(iterator.hasNext());
        for (int i = 0; i < multidimensionalCounter.getTotalSize(); ++i) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetNext()
    {
        Iterator iterator = multidimensionalCounter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            int next = iterator.next();
            assertEquals(next, count);
            ++count;
        }
    }

    @Test
    public void testIteratorGetCount()
    {
        Iterator iterator = multidimensionalCounter.iterator();
        int count = -1;
        while (iterator.hasNext()) {
            iterator.next();
            ++count;
        }
        assertEquals(count, multidimensionalCounter.getTotalSize() - 1);
    }

    @Test
    public void testIteratorGetCounts()
    {
        Iterator iterator = multidimensionalCounter.iterator();
        int[] counts = new int[size.length];
        while (iterator.hasNext()) {
            iterator.next();
            int index = iterator.getCount();
            counts[index]++;
        }
        Arrays.sort(counts);
        for (int i = 0; i < size.length; ++i) {
            assertEquals(counts[i], size[i]);
        }
    }

    @Test
    public void testToString()
    {
        assertEquals("[0]", multidimensionalCounter.toString());
        for (int i = 0; i < multidimensionalCounter.getTotalSize(); ++i) {
            multidimensionalCounter.next();
            assertEquals("[1]", multidimensionalCounter.toString());
        }
    }
}