package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the MultidimensionalCounter class.
 */
public class MultidimensionalCounterTest {

    /**
     * Test creation of a counter with correct sizes.
     * @throws Exception If initialization fails.
     */
    @Test
    public void testCorrectSizes() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(new int[]{1, 2, 3}, counter.getSizes());
    }

    /**
     * Test creation of a counter with incorrect sizes.
     * @throws Exception If initialization fails.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testIncorrectSizes() throws Exception {
        new MultidimensionalCounter(-1, 2, 3);
    }

    /**
     * Test iteration over the counter.
     * @throws Exception If iteration fails.
     */
    @Test
    public void testIteration() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        MultidimensionalCounter.Iterator it = counter.iterator();
        int count = 0;
        while (it.hasNext()) {
            int index = it.next();
            int[] counts = it.getCounts();
            int size = it.getCount(0);
            int dim = it.getCount(1);
            int totalSize = it.getSize();
            Assert.assertEquals(count, index);
            Assert.assertArrayEquals(new int[]{size, dim}, counts);
            Assert.assertEquals(totalSize, size * dim);
            count++;
        }
        Assert.assertEquals(totalSize, count);
    }

    /**
     * Test getting multidimensional counter slots.
     * @throws Exception If getting the multidimensional counter slots fails.
     */
    @Test
    public void testGetCounts() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        int index = 0;
        int[] counts = counter.getCounts(index);
        Assert.assertEquals(0, counts[0]);
        Assert.assertEquals(0, counts[1]);
        Assert.assertEquals(0, counts[2]);

        index = 1;
        counts = counter.getCounts(index);
        Assert.assertEquals(1, counts[0]);
        Assert.assertEquals(0, counts[1]);
        Assert.assertEquals(2, counts[2]);

        index = 2;
        counts = counter.getCounts(index);
        Assert.assertEquals(1, counts[0]);
        Assert.assertEquals(2, counts[1]);
        Assert.assertEquals(3, counts[2]);

        index = 3;
        counts = counter.getCounts(index);
        Assert.assertEquals(1, counts[0]);
        Assert.assertEquals(2, counts[1]);
        Assert.assertEquals(1, counts[2]);

        index = 4;
        counts = counter.getCounts(index);
        Assert.assertEquals(1, counts[0]);
        Assert.assertEquals(2, counts[1]);
        Assert.assertEquals(0, counts[2]);

        index = 5;
        counts = counter.getCounts(index);
        Assert.assertEquals(1, counts[0]);
        Assert.assertEquals(2, counts[1]);
        Assert.assertEquals(3, counts[2]);
    }

    /**
     * Test getting unidimensional counter slot.
     * @throws Exception If getting the unidimensional counter slot fails.
     */
    @Test
    public void testGetCount() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        int index = 0;
        int count = counter.getCount(index);
        Assert.assertEquals(0, count);

        index = 1;
        count = counter.getCount(index);
        Assert.assertEquals(1, count);

        index = 2;
        count = counter.getCount(index);
        Assert.assertEquals(2, count);

        index = 3;
        count = counter.getCount(index);
        Assert.assertEquals(3, count);

        index = 4;
        count = counter.getCount(index);
        Assert.assertEquals(3, count);

        index = 5;
        count = counter.getCount(index);
        Assert.assertEquals(3, count);

        index = 6;
        count = counter.getCount(index);
        Assert.assertEquals(2, count);

        index = 7;
        count = counter.getCount(index);
        Assert.assertEquals(1, count);

        index = 8;
        count = counter.getCount(index);
        Assert.assertEquals(1, count);

        index = 9;
        count = counter.getCount(index);
        Assert.assertEquals(1, count);
    }

    /**
     * Test out-of-range index.
     * @throws Exception If out-of-range index fails.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeIndex() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        int index = 10;
        int count = counter.getCount(index);
    }

    /**
     * Test dimension mismatch index.
     * @throws Exception If dimension mismatch index fails.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchIndex() throws Exception {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        int[] index = new int[4];
        int count = counter.getCount(index);
    }
}