package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(new int[]{2, 3, 1}, counter.getSizes());
        Assert.assertEquals(2 * 3 * 1, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructionWithNegativeSize() {
        int[] size = {-1, 2, 3};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        int index = 0;
        int[] indices = null;
        try {
            indices = counter.getCounts(index);
        } catch (OutOfRangeException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertEquals(3, indices.length);
        Assert.assertEquals(0, indices[0]);
        Assert.assertEquals(0, indices[1]);
        Assert.assertEquals(0, indices[2]);

        index = 1;
        try {
            indices = counter.getCounts(index);
        } catch (OutOfRangeException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertEquals(3, indices.length);
        Assert.assertEquals(0, indices[0]);
        Assert.assertEquals(1, indices[1]);
        Assert.assertEquals(0, indices[2]);

        index = 2;
        try {
            indices = counter.getCounts(index);
        } catch (OutOfRangeException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertEquals(3, indices.length);
        Assert.assertEquals(0, indices[0]);
        Assert.assertEquals(0);
        Assert.assertEquals(1, indices[2]);

        index = 3;
        try {
            indices = counter.getCounts(index);
        } catch (OutOfRangeException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertEquals(3, indices.length);
        Assert.assertEquals(0, indices[0]);
        Assert.assertEquals(0);
        Assert.assertEquals(1);
        Assert.assertEquals(1, indices[2]);

        index = 4;
        try {
            indices = counter.getCounts(index);
        } catch (OutOfRangeException e) {
            Assert.assertEquals(index, e.getActualIndex());
            Assert.assertEquals(0, e.getExpectedMin());
            Assert.assertEquals(5, e.getExpectedMax());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongDimension() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        int[] wrongDimension = {2};
        int index = 0;
        try {
            counter.getCounts(wrongDimension);
        } catch (OutOfRangeException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        int[] count = new int[]{1, 1, 1};
        int result = counter.getCount(count);
        Assert.assertEquals(2 * 3 * 1 * (1 + 1) + 1, result);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBounds() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        int[] wrongCount = new int[]{1, 3};
        int index = 0;
        try {
            counter.getCount(wrongCount);
        } catch (DimensionMismatchException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            index = counter.getCount(wrongCount);
        } catch (OutOfRangeException e) {
            Assert.assertEquals(1, e.getActualIndex());
            Assert.assertEquals(2, e.getExpectedMin());
            Assert.assertEquals(3, e.getExpectedMax());
        }
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.next().intValue());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next().intValue());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next().intValue());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(3, iterator.next().intValue());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(4, iterator.next().intValue());

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCounts() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());

        int[] counts = iterator.getCounts();
        Assert.assertEquals(3, counts.length);
        Assert.assertEquals(0, counts[0]);
        Assert.assertEquals(0, counts[1]);
        Assert.assertEquals(0, counts[2]);

        Assert.assertTrue(iterator.hasNext());

        counts = iterator.getCounts();
        Assert.assertEquals(3, counts.length);
        Assert.assertEquals(0, counts[0]);
        Assert.assertEquals(1, counts[1]);
        Assert.assertEquals(0, counts[2]);

        Assert.assertTrue(iterator.hasNext());

        counts = iterator.getCounts();
        Assert.assertEquals(3, counts.length);
        Assert.assertEquals(0, counts[0]);
        Assert.assertEquals(1, counts[1]);
        Assert.assertEquals(1, counts[2]);

        Assert.assertTrue(iterator.hasNext());

        counts = iterator.getCounts();
        Assert.assertEquals(3, counts.length);
        Assert.assertEquals(0, counts[0]);
        Assert.assertEquals(1, counts[1]);
        Assert.assertEquals(1, counts[2]);

        Assert.assertTrue(iterator.hasNext());

        counts = iterator.getCounts();
        Assert.assertEquals(3, counts.length);
        Assert.assertEquals(1, counts[0]);
        Assert.assertEquals(1, counts[1]);
        Assert.assertEquals(1, counts[2]);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCount() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());

        int count = iterator.getCount();
        Assert.assertEquals(0, count);

        Assert.assertTrue(iterator.hasNext());

        count = iterator.getCount();
        Assert.assertEquals(1, count);

        Assert.assertTrue(iterator.hasNext());

        count = iterator.getCount();
        Assert.assertEquals(2, count);

        Assert.assertTrue(iterator.hasNext());

        count = iterator.getCount();
        Assert.assertEquals(3, count);

        Assert.assertTrue(iterator.hasNext());

        count = iterator.getCount();
        Assert.assertEquals(4, count);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testToString() {
        int[] size = {2, 3, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertEquals("[0][0][0]", counter.toString());
    }
}