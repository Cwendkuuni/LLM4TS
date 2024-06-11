package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

    private int[] size;
    private MultidimensionalCounter counter;

    public MultidimensionalCounterTest(int... size) {
        this.size = size;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new int[]{1, 1},
                new int[]{1, 2},
                new int[]{2, 2},
                new int[]{3, 3},
                new int[]{5, 5},
                new int[]{10, 10}
        });
    }

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(size);
        assertEquals(size.length, counter.getDimension());
        assertEquals(Arrays.toString(size), Arrays.toString(counter.getSizes()));
        assertEquals(Arrays.stream(size).mapToInt(i -> (int) Math.pow((double) i, size.length)).sum(), counter.getSize());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        counter = new MultidimensionalCounter(size);
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            int[] counts = counter.getCounts(i);
            for (int j = 0; j < size.length; ++j) {
                assertEquals(size[j], counts[j]);
            }
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        counter = new MultidimensionalCounter(size);
        counter.getCounts(-1);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        counter = new MultidimensionalCounter(size);
        for (int[] counts : getTestCases()) {
            int count = counter.getCount(counts);
            assertArrayEquals(counts, new int[]{counts[size.length - 1]});
            assertEquals(count, count + counts[size.length - 1]);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() throws DimensionMismatchException {
        counter = new MultidimensionalCounter(size);
        int[] wrongSize = Arrays.copyOf(size, size.length + 1);
        counter.getCount(wrongSize);
    }

    @Test
    public void testToString() {
        counter = new MultidimensionalCounter(size);
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < size.length; ++i) {
            expected.append("[").append(size[i]).append("]");
        }
        assertEquals(expected.toString(), counter.toString());
    }

    @Test
    public void testIteratorHasNext() {
        counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            assertTrue(iterator.hasNext());
        }
    }

    @Test
    public void testIteratorNext() {
        counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = -1;
        int[] counts = new int[size.length];
        while (iterator.hasNext()) {
            int next = iterator.next();
            counts[size.length - 1] = next;
            ++count;
        }
        assertArrayEquals(counts, counter.getCounts(count));
        assertEquals(count + 1, counter.getTotalSize());
    }

    @Test
    public void testIteratorGetCount() {
        counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        for (int i = 0; i < size.length; ++i) {
            assertEquals(0, iterator.getCount(i));
        }
        for (int i = 0; i < counter.getTotalSize(); ++i) {
            int next = iterator.next();
            assertEquals(next, iterator.getCount());
            assertEquals(next + 1, counter.getCount(iterator.getCounts()));
        }
    }

    @Test
    public void testIteratorRemove() {
        counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        for (int i = 0; i < size.length; ++i) {
            assertTrue(iterator.hasNext());
        }
        iterator.remove();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    private int[][] getTestCases() {
        return new int[][]{
                new int[]{0, 0},
                new int[]{0, size[0] - 1},
                new int[]{size[0] - 1, 0},
                new int[]{size[0] - 1, size[1] - 1},
                new int[]{size[0] - 1, size[1]},
                new int[]{size[0], size[1] - 1},
                new int[]{size[0], size[1]},
                new int[]{size[0], size[1] + 1},
                new int[]{size[0] + 1, size[1] - 1},
                new int[]{size[0] + 1, size[1]},
                new int[]{size[0] + 1, size[1] + 1}
        };
    }
}