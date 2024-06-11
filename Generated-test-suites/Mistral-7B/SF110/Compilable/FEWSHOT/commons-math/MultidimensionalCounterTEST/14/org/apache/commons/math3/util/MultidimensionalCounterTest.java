package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(new int[]{2, 3, 4}, mdc.getSizes());
        assertEquals(32, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSize() {
        int[] size = {0, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        Iterator iterator = mdc.iterator();
        assertEquals(-1, iterator.next());
        assertEquals(0, iterator.next());
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
        assertEquals(3, iterator.next());
        assertEquals(4, iterator.next());
        assertEquals(5, iterator.next());
        assertEquals(6, iterator.next());
        assertEquals(7, iterator.next());
        assertEquals(8, iterator.next());
        assertEquals(9, iterator.next());
        assertEquals(10, iterator.next());
        assertEquals(11, iterator.next());
        assertEquals(12, iterator.next());
        assertEquals(13, iterator.next());
        assertEquals(14, iterator.next());
        assertEquals(15, iterator.next());
        assertEquals(16, iterator.next());
        assertEquals(17, iterator.next());
        assertEquals(18, iterator.next());
        assertEquals(19, iterator.next());
        assertEquals(20, iterator.next());
        assertEquals(21, iterator.next());
        assertEquals(22, iterator.next());
        assertEquals(23, iterator.next());
        assertEquals(24, iterator.next());
        assertEquals(25, iterator.next());
        assertEquals(26, iterator.next());
        assertEquals(27, iterator.next());
        assertEquals(28, iterator.next());
        assertEquals(29, iterator.next());
        assertEquals(30, iterator.next());
        assertEquals(31, iterator.next());
        assertEquals(31, iterator.next());

        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }

    @Test
    public void testGetCountsOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        assertThrows(OutOfRangeException.class, () -> mdc.getCounts(-1));
        assertThrows(OutOfRangeException.class, () -> mdc.getCounts(32));
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        int index = 0;
        int[] counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        index = 1;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(1, counts[2]);

        index = 2;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(0, counts[2]);

        index = 3;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 4;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 5;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 6;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 7;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 8;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 9;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 10;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 11;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 12;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 13;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 14;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 15;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 16;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 17;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 18;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 19;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 20;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 21;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 22;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 23;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 24;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 25;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 26;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 27;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 28;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 29;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 30;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        index = 31;
        counts = mdc.getCounts(index);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);
    }

    @Test
    public void testGetCountOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        assertThrows(OutOfRangeException.class, () -> mdc.getCount(-1, 0));
        assertThrows(OutOfRangeException.class, () -> mdc.getCount(32, 0));
        assertThrows(OutOfRangeException.class, () -> mdc.getCount(0, 3));
        assertThrows(OutOfRangeException.class, () -> mdc.getCount(0, 32));
        assertThrows(DimensionMismatchException.class, () -> mdc.getCount(3, 3));
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        int[] c = {0, 1, 2};
        int count = mdc.getCount(c);
        assertEquals(1, count);

        c = new int[]{1, 1, 2};
        count = mdc.getCount(c);
        assertEquals(3, count);

        c = new int[]{1, 1, 3};
        count = mdc.getCount(c);
        assertEquals(4, count);

        c = new int[]{1, 2, 2};
        count = mdc.getCount(c);
        assertEquals(6, count);

        c = new int[]{1, 2, 3};
        count = mdc.getCount(c);
        assertEquals(8, count);

        c = new int[]{2, 2, 2};
        count = mdc.getCount(c);
        assertEquals(9, count);

        c = new int[]{2, 2, 3};
        count = mdc.getCount(c);
        assertEquals(12, count);

        c = new int[]{2, 3, 3};
        count = mdc.getCount(c);
        assertEquals(14, count);

        c = new int[]{3, 3, 3};
        count = mdc.getCount(c);
        assertEquals(27, count);

        c = new int[]{3, 3, 4};
        count = mdc.getCount(c);
        assertEquals(32, count);
    }
}