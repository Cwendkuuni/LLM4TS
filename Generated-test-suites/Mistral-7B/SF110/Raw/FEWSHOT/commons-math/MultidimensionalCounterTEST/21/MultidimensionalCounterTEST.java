package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size.clone());
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
        assertEquals(72, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSize() {
        int[] size = new int[]{-1, 3, 4};
        new MultidimensionalCounter(size.clone());
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size.clone());
        int[] index = new int[]{0, 1, 2};
        int expected[] = new int[]{0, 0, 0};
        assertEquals(expected, counter.getCounts(counter.getSize() - 1));
        expected[0] = 1;
        assertEquals(expected, counter.getCounts(counter.getSize() - 2));
        expected[1] = 1;
        assertEquals(expected, counter.getCounts(counter.getSize() - 3));
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRangeGetCounts() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size.clone());
        int index = counter.getSize() + 1;
        counter.getCounts(index);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size.clone());
        int index[] = new int[]{1, 1, 1};
        int expected = 2 * 3 * 1 + 1 * 3 + 1;
        assertEquals(expected, counter.getCount(index));
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRangeGetCount() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size.clone());
        int index[] = new int[]{-1, 1, 1};
        counter.getCount(index);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterDimensionMismatchGetCount() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size.clone());
        int index[] = new int[]{1, 1, 5};
        counter.getCount(index);
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size.clone());
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Iterator expected = new Iterator() {
            private int count = -1;

            public boolean hasNext() {
                return count < counter.getSize() - 1;
            }

            public int next() {
                count++;
                return count;
            }

            public int[] getCounts() {
                int counts[] = new int[counter.getDimension()];
                for (int i = 0; i < counter.getDimension(); i++) {
                    counts[i] = Math.min(counter.getSize()[i] - 1, count / counter.getSize()[counter.getDimension() - 1]);
                }
                return counts;
            }
        };

        assertEquals(expected.getClass(), iterator.getClass());
        assertEquals(true, iterator.hasNext());
        assertEquals(0, iterator.next());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        assertEquals(true, iterator.hasNext());
        assertEquals(1, iterator.next());
        assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());
        assertEquals(true, iterator.hasNext());
        assertEquals(2, iterator.next());
        assertArrayEquals(new int[]{0, 0, 2}, iterator.getCounts());
        assertEquals(true, iterator.hasNext());
        assertEquals(3, iterator.next());
        assertArrayEquals(new int[]{0, 1, 0}, iterator.getCounts());
        assertEquals(true, iterator.hasNext());
        assertEquals(4, iterator.next());
        assertArrayEquals(new int[]{0, 1, 1}, iterator.getCounts());
        assertEquals(true, iterator.hasNext());
        assertEquals(5, iterator.next());
        assertArrayEquals(new int[]{0, 1, 2}, iterator.getCounts());
        assertEquals(true, iterator.hasNext());
        assertEquals(6, iterator.next());
        assertArrayEquals(new int[]{1, 0, 0}, iterator.getCounts());
        assertEquals(true, iterator.hasNext());
        assertEquals(7, iterator.next());
        assertArrayEquals(new int[]{1, 0, 1}, iterator.getCounts());
        assertEquals(true, iterator.hasNext());
        assertEquals(8, iterator.next());
        assertArrayEquals(new int[]{1, 0, 2}, iterator.getCounts());
        assertEquals(true, iterator.hasNext());
        assertEquals(9, iterator.next());
        assertArrayEquals(new int[]{1, 1, 0}, iterator.getCounts());
        assertEquals(true, iterator.hasNext());
        assertEquals(10, iterator.next());
        assertArrayEquals(new int[]{1, 1, 1}, iterator.getCounts());
        assertEquals(false, iterator.hasNext());
    }
}