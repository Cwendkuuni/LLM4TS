package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = new int[]{-1, 2, 3};
        try {
            new MultidimensionalCounter(sizes);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorSimple() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(sizes, counter.getSizes());
        assertEquals(729, counter.getSize());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsInvalidIndex() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 729;
        try {
            counter.getCounts(index);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testGetCountsValidIndex() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 0;
        int[] expected = new int[]{0, 0, 0};
        int[] actual = counter.getCounts(index);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 730;
        try {
            counter.getCount(index);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testGetCountValidIndex() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] index = new int[]{0, 0, 0};
        int expected = 0;
        int actual = counter.getCount(index);
        assertEquals(expected, actual);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimension() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] index = new int[]{1, 0, 0};
        try {
            counter.getCount(index);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeDimension() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] index = new int[]{2, 3, 4};
        try {
            counter.getCount(index);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        assertFalse(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int expected = 0;
        int actual = iterator.next();
        assertEquals(expected, actual);
        expected = 1;
        actual = iterator.next();
        assertEquals(expected, actual);
        expected = 2;
        actual = iterator.next();
        assertEquals(expected, actual);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCount() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int expected = 0;
        int actual = iterator.getCount();
        assertEquals(expected, actual);
        iterator.next();
        expected = 1;
        actual = iterator.getCount();
        assertEquals(expected, actual);
        iterator.next();
        expected = 2;
        actual = iterator.getCount();
        assertEquals(expected, actual);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToString() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expected = "[0][0][0]";
        String actual = counter.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSize() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int expected = 729;
        int actual = counter.getSize();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSizes() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] expected = new int[]{1, 2, 3};
        int[] actual = counter.getSizes();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCountsEdgeCase() {
        int[] sizes = new int[]{1, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] index = new int[]{0, 1, 1};
        int[] expected = new int[]{0, 1, 1};
        int[] actual = counter.getCounts(index);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCountEdgeCase() {
        int[] sizes = new int[]{1, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] index = new int[]{0, 1, 1};
        int expected = 2;
        int actual = counter.getCount(index);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCountNegativeIndex() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] index = new int[]{-1, 0, 0};
        try {
            counter.getCount(index);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testGetCountsOutOfRange() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] index = new int[]{3, 0, 0};
        try {
            counter.getCounts(index);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testIteratorHasNextEdgeCase() {
        int[] sizes = new int[]{1, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        iterator.next();
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNextEdgeCase() {
        int[] sizes = new int[]{1, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int expected = 2;
        int actual = iterator.next();
        assertEquals(expected, actual);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCountEdgeCase() {
        int[] sizes = new int[]{1, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        iterator.next();
        iterator.next();
        int expected = 2;
        int actual = iterator.getCount();
        assertEquals(expected, actual);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCountsEdgeCase() {
        int[] sizes = new int[]{1, 2, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        iterator.next();
        iterator.next();
        int[] expected = new int[]{1, 1, 0};
        int[] actual = iterator.getCounts();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIteratorRemove() {
        int[] sizes = new int[]{1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetCountWithPrecision() {
        int[] sizes = new int[]{1000, 1000, 1000};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] index = new int[]{500, 500, 500};
        long expected = Precision.round(Math.pow(sizes[0], 2) * Math.pow(sizes[1], 2) * Math.pow(sizes[2], 2) / 3 * (index[0] + 1) * (index[1] + 1) * (index[2] + 1), 0);
        long actual = counter.getCount(index);
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithPrecision() {
        int[] sizes = new int[]{1000, 1000, 1000};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < sizes.length; ++i) {
            long value = counter.getCount(new int[]{i, 0, 0});
            expected.append("[").append(Precision.round(value, 0)).append("]");
            if (i != sizes.length - 1) {
                expected.append(", ");
            }
        }
        String actual = counter.toString();
        assertEquals(expected.toString(), actual);
    }
}