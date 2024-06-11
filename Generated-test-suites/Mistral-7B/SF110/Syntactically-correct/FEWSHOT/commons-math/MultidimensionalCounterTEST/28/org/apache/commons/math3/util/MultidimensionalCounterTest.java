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
import java.util.Collections;
import java.util.Iterator;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructor() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
        assertEquals(24, counter.getSize());

        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(new int[]{-1, 3, 4}));
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(new int[]{1}));
    }

    @Test
    public void testGetCounts() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] index = new int[]{-1, 2, 3};
        assertNull(counter.getCounts(index[0]));

        index = new int[]{0, 1, 2};
        assertArrayEquals(new int[]{0, 1, 2}, counter.getCounts(counter.getTotalSize() - 1));

        index = new int[]{0, 0, 0};
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));

        index = new int[]{1, 1, 1};
        assertArrayEquals(new int[]{1, 1, 1}, counter.getCounts(counter.getSize() - 1));

        index = new int[]{2, 2, 2};
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(counter.getTotalSize() + 1));
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(-1));
        assertThrows(DimensionMismatchException.class, () -> counter.getCounts(new int[]{3}));
    }

    @Test
    public void testGetCount() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] index = new int[]{0, 0, 0};
        assertEquals(0, counter.getCount(index));

        index = new int[]{1, 1, 1};
        assertEquals(8, counter.getCount(index));

        index = new int[]{1, 1, 2};
        assertEquals(12, counter.getCount(index));

        index = new int[]{2, 2, 2};
        assertEquals(16, counter.getCount(index));

        index = new int[]{0, 1, 2};
        assertEquals(7, counter.getCount(index));

        index = new int[]{1, 2, 2};
        assertEquals(12, counter.getCount(index));

        index = new int[]{2, 0, 2};
        assertEquals(4, counter.getCount(index));

        index = new int[]{2, 1, 2};
        assertEquals(12, counter.getCount(index));

        index = new int[]{2, 2, 0};
        assertEquals(4, counter.getCount(index));

        index = new int[]{2, 2, 3};
        assertEquals(4, counter.getCount(index));

        index = new int[]{1, 2, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{0, 1, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{0, 2, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{1, 0, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{1, 2, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{2, 0, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{2, 1, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{2, 2, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{0, 0, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{0, 1, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{0, 2, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{1, 0, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{1, 1, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{1, 2, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{2, 0, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{2, 1, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{2, 2, 3};
        assertEquals(12, counter.getCount(index));

        index = new int[]{0, 0, 4};
        assertEquals(0, counter.getCount(index));

        index = new int[]{0, 1, 4};
        assertEquals(0, counter.getCount(index));

        index = new int[]{0, 2, 4};
        assertEquals(0, counter.getCount(index));

        index = new int[]{1, 0, 4};
        assertEquals(0, counter.getCount(index));

        index = new int[]{1, 1, 4};
        assertEquals(0, counter.getCount(index));

        index = new int[]{1, 2, 4};
        assertEquals(0, counter.getCount(index));

        index = new int[]{2, 0, 4};
        assertEquals(0, counter.getCount(index));

        index = new int[]{2, 1, 4};
        assertEquals(0, counter.getCount(index));

        index = new int[]{2, 2, 4};
        assertEquals(0, counter.getCount(index));

        index = new int[]{0, 0, 5};
        assertEquals(-1, counter.getCount(index));

        index = new int[]{0, 1, 5};
        assertEquals(-1, counter.getCount(index));

        index = new int[]{0, 2, 5};
        assertEquals(-1, counter.getCount(index));

        index = new int[]{1, 0, 5};
        assertEquals(-1, counter.getCount(index));

        index = new int[]{1, 1, 5};
        assertEquals(-1, counter.getCount(index));

        index = new int[]{1, 2, 5};
        assertEquals(-1, counter.getCount(index));

        index = new int[]{2, 0, 5};
        assertEquals(-1, counter.getCount(index));

        index = new int[]{2, 1, 5};
        assertEquals(-1, counter.getCount(index));
    }

    @Test
    public void testIterator() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        assertFalse(iterator.hasNext());

        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertTrue(iterator.hasNext());

        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertFalse(iterator.hasNext());

        assertEquals(counter.getSize(), iterator.getCount());

        int[] counts = iterator.getCounts();
        assertArrayEquals(new int[]{2, 3, 3}, counts);

        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(2, counts[2]);

        iterator = counter.iterator();
        iterator.next();
        iterator.next();
        iterator.next();

        counts = iterator.getCounts();
        assertArrayEquals(new int[]{2, 3, 2}, counts);

        assertEquals(3, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(2, counts[2]);
    }
}