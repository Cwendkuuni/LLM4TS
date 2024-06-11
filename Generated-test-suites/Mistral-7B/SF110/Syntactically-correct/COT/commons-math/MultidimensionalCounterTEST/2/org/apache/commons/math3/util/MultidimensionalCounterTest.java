package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

    private int[] size;
    private MultidimensionalCounter counter;

    public MultidimensionalCounterTest(int... size) {
        try {
            this.counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            fail("Expected positive sizes, but received: " + Arrays.toString(size));
        }
        this.size = size;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new int[]{1},
                new int[]{2, 2},
                new int[]{3, 3, 3},
                new int[]{1, 2, 3},
                new int[]{4, 4, 4, 4},
                new int[]{5, 5, 5, 5, 5}
        });
    }

    @Test
    public void testConstructor() {
        assertEquals(size.length, counter.getDimension());
    }

    @Test
    public void testGetDimension() {
        assertEquals(size.length, counter.getDimension());
    }

    @Test
    public void testGetCountsValidIndex() {
        int[] counts = counter.getCounts(0);
        assertArrayEquals(size, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() {
        counter.getCounts(size.length);
    }

    @Test
    public void testGetCountValidIndices() {
        int[] indices = new int[size.length];
        int total = 0;
        for (int i = 0; i < size.length; ++i) {
            indices[i] = size[i] - 1;
            total += counter.uniCounterOffset[i] * indices[i] + counter.uniCounterOffset[i];
        }
        int expected = total + indices[size.length - 1];
        int actual = counter.getCount(indices);
        assertEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidIndices() {
        int[] invalidIndices = new int[size.length];
        for (int i = 0; i < size.length; ++i) {
            invalidIndices[i] = size[i] + 1;
        }
        counter.getCount(invalidIndices);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidCountDimensions() {
        int[] invalidDimensionCounts = new int[]{3, 2};
        counter.getCount(invalidDimensionCounts);
    }

    @Test
    public void testGetSize() {
        assertEquals(MathArrays.product(size), counter.getSize());
    }

    @Test
    public void testGetSizes() {
        assertArrayEquals(size, counter.getSizes());
    }

    @Test
    public void testToString() {
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < size.length; ++i) {
            expected.append("[");
            expected.append(size[i] - 1);
            expected.append("]");
        }
        assertEquals(expected.toString(), counter.toString());
    }

    @Test
    public void testIteratorHasNext() {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size.length; ++i) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size.length; ++i) {
            assertEquals(iterator.next(), i + 1);
        }
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorGetCount() {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size.length; ++i) {
            assertEquals(iterator.getCount(), i);
        }
        assertNull(iterator.getCount());
    }

    @Test
    public void testIteratorGetCounts() {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size.length; ++i) {
            int[] counts = iterator.getCounts();
            assertArrayEquals(new int[]{i, size[i] - 1}, counts);
        }
        assertNull(iterator.getCounts());
    }

    @Test
    public void testIteratorGetCountLastDimension() {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size.length - 1; ++i) {
            iterator.next();
        }
        int[] counts = iterator.getCounts();
        assertEquals(new int[]{size.length - 1, size[size.length - 1] - 1}, counts);
    }
}