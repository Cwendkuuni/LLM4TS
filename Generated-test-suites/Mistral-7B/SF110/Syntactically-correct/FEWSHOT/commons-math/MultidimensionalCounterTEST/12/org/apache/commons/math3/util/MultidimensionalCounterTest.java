package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;

import java.util.Arrays;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(144, counter.getSize());
    }

    @Test
    public void testMultidimensionalCounterConstructionNegativeSize() {
        int[] size = new int[]{-2, 3, 4};
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(size));
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[0] * size[1] * size[2]; ++i) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[0] * size[1] * size[2]; ++i) {
            int next = iterator.next();
            assertEquals(i + 1, next);
        }
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    public void testIteratorGetCount() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[0] * size[1] * size[2]; ++i) {
            assertEquals(i, iterator.getCount());
            iterator.next();
        }
        assertNull(iterator.getCount());
    }

    @Test
    public void testIteratorGetCounts() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[0] * size[1] * size[2]; ++i) {
            int[] counts = iterator.getCounts();
            assertArrayEquals(new int[]{i / size[0] % size[1], i / size[1] % size[2], i / size[2]}, counts);
            iterator.next();
        }
        assertNull(iterator.getCounts());
    }

    @Test
    public void testGetCountsOutOfRange() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int index = size[0] * size[1] * size[2] + 1;
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(index));
    }

    @Test
    public void testGetCountOutOfRange() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] index = new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, -1};
        for (int i = 0; i < 3; ++i) {
            assertThrows(OutOfRangeException.class, () -> counter.getCount(index[i]));
        }
    }

    @Test
    public void testGetCountsDimensionMismatch() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertThrows(DimensionMismatchException.class, () -> counter.getCounts(new int[]{2, 2, 3}));
    }

    @Test
    public void testGetCountsZeroDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new MultidimensionalCounter(new int[]{0}));
        assertThrows(IllegalArgumentException.class, () -> new MultidimensionalCounter(new int[]{0, 0}));
        assertThrows(IllegalArgumentException.class, () -> new MultidimensionalCounter(new int[]{0, 0, 0}));
    }

    @Test
    public void testToString() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        StringBuilder expected = new StringBuilder();
        expected.append("[");
        expected.append(counter.getCount(0));
        expected.append("] [");
        expected.append(counter.getCount(1));
        expected.append("] [");
        expected.append(counter.getCount(2));
        expected.append("]");
        assertEquals(expected.toString(), counter.toString());
    }
}