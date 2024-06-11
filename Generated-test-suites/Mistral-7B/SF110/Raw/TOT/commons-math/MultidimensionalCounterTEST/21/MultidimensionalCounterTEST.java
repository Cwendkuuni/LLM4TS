package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class MultidimensionalCounterTest
{
    private MultidimensionalCounter counter;

    @Before
    public void setUp()
    {
        int[] size = {2, 3, 4};
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            System.out.println("Error during setup: " + e.getMessage());
        }
    }

    @Test
    public void testGetDimension()
    {
        Assert.assertEquals("Dimension should be 3", 3, counter.getDimension());
    }

    @Test
    public void testGetSizes()
    {
        int[] expected = {2, 3, 4};
        Assert.assertArrayEquals("Sizes should match", expected, counter.getSizes());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegativeIndex()
    {
        int index = -1;
        counter.getCounts(index);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange()
    {
        int index = counter.getTotalSize() + 1;
        counter.getCounts(index);
    }

    @Test
    public void testGetCountsValidIndex()
    {
        int index = 12;
        int[] expected = {0, 2, 3, index};
        int[] actual = counter.getCounts(index);
        Assert.assertArrayEquals("Counts should match", expected, actual);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimension()
    {
        int[] invalidDimension = {2, 3, 5};
        try {
            new MultidimensionalCounter(invalidDimension);
            // This line should never be reached
        } catch (Exception e) {
            Assert.assertEquals("Exception message should be 'Dimension mismatch: size.length != counter.getDimension()'", "Dimension mismatch: size.length != counter.getDimension()", e.getMessage());
        }
    }

    @Test
    public void testGetCountValidIndex()
    {
        int[] index = {1, 1, 1};
        int expected = 1 + counter.uniCounterOffset[0] + counter.uniCounterOffset[0] * counter.size[0] + counter.uniCounterOffset[0] * counter.size[0] * counter.size[1] + counter.uniCounterOffset[0] * counter.size[0] * counter.size[1] * counter.size[2];
        int actual = counter.getCount(index);
        Assert.assertEquals("Count should match", expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidIndex1()
    {
        int[] invalidIndex = {1, 1, 10};
        counter.getCount(invalidIndex);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidIndex2()
    {
        int[] invalidIndex = {1, 10, 1};
        counter.getCount(invalidIndex);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidIndex3()
    {
        int[] invalidIndex = {10, 1, 1};
        counter.getCount(invalidIndex);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimension2()
    {
        int[] invalidIndex = {1, 2, 3, 4};
        try {
            counter.getCount(invalidIndex);
            // This line should never be reached
        } catch (Exception e) {
            Assert.assertEquals("Exception message should be 'Dimension mismatch: size.length != counter.getDimension()'", "Dimension mismatch: size.length != counter.getDimension()", e.getMessage());
        }
    }

    @Test
    public void testIteratorHasNext()
    {
        Iterator it = counter.iterator();
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            Assert.assertTrue("Iterator should have next element", it.hasNext());
            it.next();
        }
        Assert.assertFalse("Iterator should not have next element", it.hasNext());
    }

    @Test
    public void testIteratorGetCount()
    {
        Iterator it = counter.iterator();
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            int count = it.getCount();
            int size = counter.getSizes()[counter.getLast()];
            int expected = i + 1;
            Assert.assertEquals("Count should match", expected, count);
            Assert.assertEquals("Size should match", size, counter.getSizes()[counter.getLast()]);
            it.next();
        }
        int count = it.getCount();
        Assert.assertEquals("Count should be -1", -1, count);
    }

    @Test
    public void testIteratorGetCounts()
    {
        Iterator it = counter.iterator();
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            int[] counts = it.getCounts();
            int[] sizes = counter.getSizes();
            int size = sizes[counter.getLast()];
            int expectedLast = i % size;
            int expectedOther = i / size;
            for (int j = 0; j < counter.getDimension(); ++j) {
                int expected = (j == counter.getLast()) ? expectedLast : expectedOther;
                int actual = counts[j];
                Assert.assertEquals("Counts should match", expected, actual);
            }
            it.next();
        }
        int[] counts = it.getCounts();
        Assert.assertArrayEquals("Counts should be all zero", new int[]{0, 0, 0}, counts);
    }
}