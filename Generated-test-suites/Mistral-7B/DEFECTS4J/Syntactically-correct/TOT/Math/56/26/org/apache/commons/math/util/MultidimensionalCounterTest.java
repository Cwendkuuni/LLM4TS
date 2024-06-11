package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        final int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Assert.assertEquals(3, mdc.getDimension());
        Assert.assertArrayEquals(sizes, mdc.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        new MultidimensionalCounter(new int[]{-1, 5, 3});
    }

    @Test
    public void testGetCounts() {
        final int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        for (int i = 0; i < mdc.getSize(); i++) {
            int[] counts = mdc.getCounts(i);
            for (int j = 0; j < mdc.getDimension(); j++) {
                Assert.assertEquals(sizes[j], counts[j]);
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        final int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(new int[]{6, 5, 4});
    }

    @Test
    public void testGetCount() {
        final int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        for (int i = 0; i < mdc.getSize(); i++) {
            int index = mdc.getCount(new int[]{i / 15, i % 15 / 5, i % 15 % 5});
            Assert.assertEquals(i, index);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeException() {
        final int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(new int[]{3, 4, 5});
    }

    @Test
    public void testIterator() {
        final int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        Iterator it = mdc.iterator();
        int count = 0;

        while (it.hasNext()) {
            int currIndex = it.next();
            int[] currCounts = it.getCounts();
            Assert.assertEquals(sizes.length, currCounts.length);
            for (int i = 0; i < sizes.length; i++) {
                Assert.assertEquals(sizes[i], currCounts[i]);
            }
            Assert.assertEquals(count, currIndex);
            count++;
        }

        it = mdc.iterator();
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testSize() {
        final int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int size = mdc.getSize();

        for (int i = 0; i < size; i++) {
            mdc.getCount(new int[]{i / 15, i % 15 / 5, i % 15 % 5});
        }

        it = mdc.iterator();
        int count = 0;

        while (it.hasNext()) {
            int currIndex = it.next();
            Assert.assertEquals(size, currIndex);
            count++;
        }

        Assert.assertEquals(size, count);
    }
}