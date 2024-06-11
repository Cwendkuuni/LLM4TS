package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(size, mdc.getSizes());
        assertEquals(3 * 4 * 5, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSize() {
        int[] size = {-1, 4, 5};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterZeroSize() {
        int[] size = {3, 0, 5};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int index = 0;
        int[] counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertArrayEquals(new int[]{0, 0, 0}, counts);

        index = 1;
        counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(0, counts[2]);

        index = 3 * 4;
        counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertEquals(2, counts[0]);
        assertEquals(3, counts[1]);
        assertEquals(4, counts[2]);

        index = 3 * 4 * 5 - 1;
        counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertEquals(4, counts[0]);
        assertEquals(4, counts[1]);
        assertEquals(5, counts[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRangeGetCounts() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int index = 3 * 4 * 5;
        mdc.getCounts(index);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] expected = {1, 2, 3};
        int count = mdc.getCount(expected);
        assertEquals(count, 21);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterNegativeIndexGetCount() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int index = new int[]{-1, 2, 3};
        mdc.getCount(index);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfBoundsIndexGetCount() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int index = new int[]{3, 4, 5};
        mdc.getCount(index);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterDimensionMismatchGetCount() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int index = new int[]{3, 4};
        mdc.getCount(index);
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = mdc.iterator();
        assertFalse(iterator.hasNext());

        iterator.next();
        assertFalse(iterator.hasNext());

        for (int i = 0; i < size[0]; ++i) {
            for (int j = 0; j < size[1]; ++j) {
                for (int k = 0; k < size[2]; ++k) {
                    iterator.next();
                    assertTrue(iterator.hasNext());
                }
            }
        }

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testMultidimensionalCounterIteratorGetCount() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = mdc.iterator();
        for (int i = 0; i < size[0]; ++i) {
            for (int j = 0; j < size[1]; ++j) {
                for (int k = 0; k < size[2]; ++k) {
                    iterator.next();
                    assertEquals(iterator.getCount(), i * size[1] * size[2] + j * size[2] + k + 1);
                }
            }
        }
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testMultidimensionalCounterIteratorGetCounts() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = mdc.iterator();
        for (int i = 0; i < size[0]; ++i) {
            for (int j = 0; j < size[1]; ++j) {
                for (int k = 0; k < size[2]; ++k) {
                    iterator.next();
                    int[] counts = iterator.getCounts();
                    int expected[] = {i, j, k};
                    assertArrayEquals(expected, counts);
                }
            }
        }
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        StringBuilder expected = new StringBuilder("[");
        for (int i = 0; i < size[0]; ++i) {
            expected.append("[");
            for (int j = 0; j < size[1]; ++j) {
                expected.append("[");
                for (int k = 0; k < size[2]; ++k) {
                    expected.append(mdc.getCount(i, j, k));
                    if (k < size[2] - 1) {
                        expected.append("],");
                    }
                }
                if (j < size[1] - 1) {
                    expected.append("],");
                }
            }
            if (i < size[0] - 1) {
                expected.append("],");
            }
        }
        expected.append("]");
        assertEquals(expected.toString(), mdc.toString());
    }
}