package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing MultidimensionalCounter class
 */
public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            Assert.assertEquals(3, mdc.getDimension());
            Assert.assertArrayEquals(new int[]{2, 3, 4}, mdc.getSizes());
            Assert.assertEquals(32, mdc.getSize());
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Expected no exception, but got " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int count = mdc.getCount(0, 0, 0);
        Assert.assertEquals(0, count);
        count = mdc.getCount(0, 0, 1);
        Assert.assertEquals(1, count);
        count = mdc.getCount(1, 1, 1);
        Assert.assertEquals(3, count);
        count = mdc.getCount(2, 2, 2);
        Assert.assertEquals(4, count);
        count = mdc.getCount(2, 2, 3);
        Assert.assertEquals(4, count);
        count = mdc.getCount(2, 2, 4);
        Assert.assertEquals(4, count);
        try {
            mdc.getCount(-1, 1, 1);
            Assert.fail();
        } catch (OutOfRangeException e) {
            // expected
        }
        try {
            mdc.getCount(2, -1, 1);
            Assert.fail();
        } catch (OutOfRangeException e) {
            // expected
        }
        try {
            mdc.getCount(2, 1, -1);
            Assert.fail();
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithWrongDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(5, 1, 1);
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] index = mdc.getCounts(0);
        Assert.assertEquals(3, index.length);
        Assert.assertEquals(0, index[0]);
        Assert.assertEquals(0, index[1]);
        Assert.assertEquals(0, index[2]);
        index = mdc.getCounts(32);
        Assert.assertEquals(3, index.length);
        Assert.assertEquals(2, index[0]);
        Assert.assertEquals(3, index[1]);
        Assert.assertEquals(4, index[2]);
        try {
            mdc.getCounts(-1);
            Assert.fail();
        } catch (OutOfRangeException e) {
            // expected
        }
        try {
            mdc.getCounts(33);
            Assert.fail();
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator it = mdc.iterator();
        int count = 0;
        while (it.hasNext()) {
            int nextInt = it.next();
            Assert.assertTrue(nextInt >= 0);
            count++;
        }
        Assert.assertEquals(32, count);
        it = mdc.iterator();
        it.remove(); // should throw UnsupportedOperationException
    }

    @Test
    public void testToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        String str = mdc.toString();
        StringBuilder expected = new StringBuilder();
        expected.append("[").append(0).append("][").append(0).append("][").append(0).append("]");
        Assert.assertEquals(expected.toString(), str);
    }
}