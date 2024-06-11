package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest
{
    private final int[] size;
    private MultidimensionalCounter counter;

    public MultidimensionalCounterTest(int[] size)
    {
        this.size = size;
    }

    @Parameters
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][] { { new int[] { 2, 3, 1 } } });
    }

    @Before
    public void setUp() throws NotStrictlyPositiveException
    {
        counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension()
    {
        assertEquals(size.length, counter.getDimension());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException
    {
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            int[] counts = counter.getCounts(i);
            int sum = 0;
            for (int j = 0; j < size.length; ++j) {
                sum += counts[j] * size[j];
            }
            assertEquals(i, sum);
        }
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException
    {
        int[] c = new int[size.length];
        for (int i = 0; i < size.length; ++i) {
            c[i] = size[i] - 1;
        }
        int count = counter.getCount(c);
        assertEquals(counter.next().intValue(), count + 1);
    }

    @Test
    public void testGetSize()
    {
        assertEquals(MathArrays.multiply(size), counter.getSize());
    }

    @Test
    public void testGetSizes()
    {
        assertArrayEquals(size, counter.getSizes());
    }

    @Test
    public void testToString()
    {
        assertNotNull(counter.toString());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() throws NotStrictlyPositiveException
    {
        new MultidimensionalCounter(new int[] { -1 });
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSize() throws NotStrictlyPositiveException
    {
        new MultidimensionalCounter(new int[] { 0 });
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeIndex() throws OutOfRangeException
    {
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(-1));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatch() throws DimensionMismatchException
    {
        assertThrows(DimensionMismatchException.class, () -> counter.getCount(new int[] { 3 }));
    }

    @Test
    public void testIteratorHasNext()
    {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            assertTrue(iterator.hasNext());
        }
    }

    @Test
    public void testIteratorNext()
    {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            assertEquals(counter.next().intValue(), iterator.next().intValue());
        }
    }

    @Test
    public void testIteratorGetCount()
    {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            assertEquals(iterator.getCount(), i);
        }
    }

    @Test
    public void testIteratorGetCounts()
    {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            int[] counts = iterator.getCounts();
            int sum = 0;
            for (int j = 0; j < size.length; ++j) {
                sum += counts[j] * size[j];
            }
            assertEquals(i, sum);
        }
    }

    @Test
    public void testIteratorRemove()
    {
        assertThrows(UnsupportedOperationException.class, () -> counter.iterator().remove());
    }
}