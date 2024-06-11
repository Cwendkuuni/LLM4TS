package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstructor() {
        int[] size = new int[]{2, 3, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException should not be thrown");
        }
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[]{2, 3, 1}, counter.getSizes());
        assertEquals(2 * 3 * 1, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructorNegativeSize() {
        int[] size = new int[]{-1, 3, 1};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructorZeroSize() {
        int[] size = new int[]{0, 3, 1};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = new int[]{2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] indices = new int[]{1, 1, 0};
        int[] expectedIndices = new int[]{1, 1, 0};
        int[] actualIndices = counter.getCounts(0);
        assertArrayEquals(expectedIndices, actualIndices);

        indices = new int[]{1, 1, 1};
        expectedIndices = new int[]{1, 1, 1};
        actualIndices = counter.getCounts(counter.getSize() - 1);
        assertArrayEquals(expectedIndices, actualIndices);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountsOutOfRange() {
        int[] size = new int[]{2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] indices = new int[]{-1, 1, 0};
        counter.getCounts(indices[0]);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] size = new int[]{2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] indices = new int[]{1, 1, 0};
        int expectedCount = 1 * 1 * 0 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 * 1 + counter.uniCounterOffset[2] * 1 * 1 * 1;
        int actualCount = counter.getCount(indices);
        assertEquals(expectedCount, actualCount);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountOutOfRange() {
        int[] size = new int[]{2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] indices = new int[]{1, 1, 2};
        counter.getCount(indices);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterGetCountDimensionMismatch() {
        int[] size = new int[]{2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] indices = new int[]{1, 2, 0};
        counter.getCount(indices);
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = new int[]{2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator it = counter.iterator();
        assertEquals(-1, it.getCount());
        assertNull(it.next());

        it = counter.iterator();
        it.next();
        int[] expectedIndices = new int[]{0, 0, 0};
        int[] actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        int expectedCount = 0;
        int actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        it.next();
        expectedIndices = new int[]{0, 0, 1};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        it.next();
        expectedIndices = new int[]{0, 1, 0};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        it.next();
        expectedIndices = new int[]{0, 1, 1};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        it.next();
        expectedIndices = new int[]{1, 0, 0};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        it.next();
        expectedIndices = new int[]{1, 0, 1};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        it.next();
        expectedIndices = new int[]{1, 1, 0};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        expectedIndices = new int[]{1, 1, 1};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        expectedIndices = new int[]{1, 2, 0};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        expectedIndices = new int[]{1, 2, 1};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        expectedIndices = new int[]{2, 0, 0};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[0] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        expectedIndices = new int[]{2, 0, 1};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        expectedIndices = new int[]{2, 1, 0};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[1] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);

        expectedIndices = new int[]{2, 1, 1};
        actualIndices = it.getCounts();
        assertArrayEquals(expectedIndices, actualIndices);

        expectedCount = 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[2] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[0] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[1] * 1 + counter.uniCounterOffset[1] * 1;
        actualCount = it.getCount();
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] size = new int[]{2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        StringBuilder expectedString = new StringBuilder();
        expectedString.append("[");
        expectedString.append(counter.getCount(0));
        expectedString.append("] [");
        expectedString.append(counter.getCount(1));
        expectedString.append("] [");
        expectedString.append(counter.getCount(2));
        expectedString.append("]");
        String actualString = counter.toString();
        assertEquals(expectedString.toString(), actualString);
    }
}