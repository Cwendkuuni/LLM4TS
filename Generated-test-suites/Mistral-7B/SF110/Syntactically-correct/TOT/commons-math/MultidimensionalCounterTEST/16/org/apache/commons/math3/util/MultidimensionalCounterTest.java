package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorWithValidParameters() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Assert.assertEquals(6, counter.getDimension());
        Assert.assertArrayEquals(new int[]{3, 2, 1}, counter.getSizes());
        Assert.assertEquals(18, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidParameters() {
        int[] size = new int[]{-1, 2, 1};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetCounts() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int index = 0;
        int[] counts = counter.getCounts(index);
        Assert.assertEquals(3, counts[0]);
        Assert.assertEquals(2, counts[1]);
        Assert.assertEquals(1, counts[2]);

        index = counter.getTotalSize() - 1;
        counts = counter.getCounts(index);
        Assert.assertEquals(0, counts[0]);
        Assert.assertEquals(2 - 1, counts[1]);
        Assert.assertEquals(1 - 1, counts[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int index = -1;
        counter.getCounts(index);
    }

    @Test
    public void testGetCount() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] c = {0, 0, 0};
        int count = counter.getCount(c);
        Assert.assertEquals(0, count);

        c = new int[]{2, 1, 0};
        count = counter.getCount(c);
        Assert.assertEquals(2, count);

        c = new int[]{1, 1, 1};
        count = counter.getCount(c);
        Assert.assertEquals(1, count);

        c = new int[]{2, 2, 1};
        Assert.assertThrows(DimensionMismatchException.class, () -> counter.getCount(c));
    }

    @Test
    public void testIterator() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
        List<Integer> actual = new ArrayList<>();
        Iterator iterator = counter.iterator();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        String expected = "[0][1][0] [1][1][0] [2][1][0] [0][2][0] [1][2][0] [2][2][0] [0][3][0] [1][3][0] [2][3][0]";
        Assert.assertEquals(expected, counter.toString());
    }
}