package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import java.util.Iterator;

/**
 * Test class for MultidimensionalCounter.
 */
public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = {-1, 2, 3};
        try {
            new MultidimensionalCounter(sizes);
            fail("NotStrictlyPositiveException expected.");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorTotalSizeNegative() {
        int[] sizes = {1, 2, 3};
        try {
            new MultidimensionalCounter(sizes);
            int[] uniCounterOffset = new int[sizes.length];
            int totalSize = 1;
            for (int i = 0; i < sizes.length; ++i) {
                int count = 1;
                for (int j = 0; j < sizes.length && j < i; ++j) {
                    count *= sizes[j];
                }
                uniCounterOffset[i] = count;
                totalSize *= sizes[i];
            }
            if (totalSize <= 0) {
                throw new NotStrictlyPositiveException(totalSize);
            }
            fail("NotStrictlyPositiveException expected.");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testConstructor() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(sizes, counter.getSizes());
        assertEquals(6, counter.getSize());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegativeIndex() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(-1);
        fail("OutOfRangeException expected.");
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(6);
        fail("OutOfRangeException expected.");
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] indices = counter.getCounts(0);
        assertEquals(sizes.length, indices.length);
        for (int i = 0; i < sizes.length; ++i) {
            assertEquals(0, indices[i]);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1);
        fail("OutOfRangeException expected.");
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int c[] = {3, 1, 2};
        counter.getCount(c);
        int count = 1;
        for (int i = 0; i < sizes.length; ++i) {
            count *= sizes[i];
            if (count < c[i]) {
                fail("OutOfRangeException expected.");
            }
        }
        fail("OutOfRangeException expected.");
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int c[] = {3, 2, 4};
        counter.getCount(c);
        fail("DimensionMismatchException expected.");
    }

    @Test
    public void testGetCount() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int c[] = {1, 1, 1};
        int count = counter.getCount(c);
        assertEquals(1, count);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = counter.iterator();
        assertTrue(it.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = counter.iterator();
        int next = it.next();
        assertEquals(0, next);
    }

    @Test
    public void testIteratorGetCount() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = counter.iterator();
        int next = it.next();
        int count = it.getCount();
        assertEquals(next, count);
    }

    @Test
    public void testIteratorGetCounts() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = counter.iterator();
        int next = it.next();
        int[] counts = it.getCounts();
        int expectedSize = sizes.length;
        int expectedCount = next;
        for (int i = 0; i < expectedSize; ++i) {
            assertEquals(expectedCount % sizes[i], counts[i]);
            expectedCount /= sizes[i];
        }
    }

    @Test
    public void testToString() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String str = counter.toString();
        String expected = "[0][0][0]";
        for (int i = 0; i < sizes.length; ++i) {
            expected += "[";
            for (int j = 0; j < sizes[i]; ++j) {
                expected += j;
                if (j < sizes[i] - 1) {
                    expected += ", ";
                }
            }
            expected += "]";
            if (i < sizes.length - 1) {
                expected += ", ";
            }
        }
        assertEquals(expected, str);
    }
}