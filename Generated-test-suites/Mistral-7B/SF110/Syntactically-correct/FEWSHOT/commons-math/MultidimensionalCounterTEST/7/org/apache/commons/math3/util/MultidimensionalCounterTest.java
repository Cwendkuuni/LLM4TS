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

public class MultidimensionalCounterTest {
    @Test
    public void testConstructor() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[]{3, 2, 1}, counter.getSizes());
        assertEquals(18, counter.getSize());
    }

    @Test
    public void testIterator() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        assertEquals(-1, iterator.getCount());
        assertEquals(Arrays.stream(new int[]{3, 2, 1}).mapToInt(i -> i - 1).toArray(), iterator.getCounts());
        assertFalse(iterator.hasNext());

        iterator = counter.iterator();
        assertEquals(0, iterator.next());
        assertEquals(new int[]{0, 0, 0}, iterator.getCounts());
        assertEquals(1, iterator.getCount());
        assertTrue(iterator.hasNext());

        for (int i = 0; i < 18; ++i) {
            iterator.next();
        }

        assertEquals(18, iterator.getCount());
        assertEquals(new int[]{-1, -1, -1}, iterator.getCounts());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCount() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] coordinates = new int[]{2, 1, 0};
        int expected = 2 * 3 + 1 * 2 + 0 * 1;
        int actual = counter.getCount(coordinates);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCountOutOfRange() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] coordinates = new int[]{-1, 2, 1};
        assertThrows(OutOfRangeException.class, () -> counter.getCount(coordinates));

        coordinates = new int[]{3, 2, 1};
        assertThrows(OutOfRangeException.class, () -> counter.getCount(coordinates));
    }

    @Test
    public void testGetCountsOutOfRange() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int[] coordinates = new int[]{2, 1, 0};
        int[] expected = new int[]{2, 1, 0};
        int[] actual = counter.getCounts(counter.getCount(coordinates));
        assertArrayEquals(expected, actual);

        coordinates = new int[]{-1, 2, 1};
        assertNull(counter.getCounts((int) -1));

        coordinates = new int[]{3, 2, 1};
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(counter.getTotalSize()));
    }

    @Test
    public void testGetDimension() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSizes() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertArrayEquals(new int[]{3, 2, 1}, counter.getSizes());
    }

    @Test
    public void testGetTotalSize() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(18, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        int[] size = new int[]{-1, 2, 1};
        new MultidimensionalCounter(size);
    }
}