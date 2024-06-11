package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testEmptyCounter() {
        int[] size = new int[0];
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> it = mdc.iterator();
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testOneDimensionCounter() {
        int[] size = {5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> it = mdc.iterator();
        int count = 0;
        while (it.hasNext()) {
            int index = it.next();
            int[] counts = mdc.getCounts(index);
            Assert.assertEquals(1, counts.length);
            Assert.assertEquals(index, counts[0]);
            count++;
        }
        Assert.assertEquals(5, count);
    }

    @Test
    public void testTwoDimensionsCounter() {
        int[] size = {3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> it = mdc.iterator();
        int count = 0;
        while (it.hasNext()) {
            int index = it.next();
            int[] counts = mdc.getCounts(index);
            Assert.assertEquals(2, counts.length);
            Assert.assertArrayEquals(new int[]{0, 0}, counts);
            if (count == 0) {
                continue;
            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    if (count == i * 4 + j + 1) {
                        int[] expected = new int[]{i, j};
                        Assert.assertArrayEquals(expected, counts);
                        break;
                    }
                }
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testWrongNumberOfDimensions() {
        int[] size = {3, 4, 5};
        new MultidimensionalCounter(size);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRange() {
        int[] size = {3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(-1);
    }

    @Test
    public void testGetSize() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Assert.assertEquals(3 * 4 * 5, mdc.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Assert.assertArrayEquals(size, mdc.getSizes());
    }
}