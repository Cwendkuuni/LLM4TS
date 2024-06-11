package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeSizes() {
        int[] sizes = {-1, 2, 3};
        try {
            new MultidimensionalCounter(sizes);
            fail();
        } catch (NotStrictlyPositiveException e) {
        }
    }

    @Test
    public void testConstructor_ValidSizes() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(sizes, counter.getSizes());
        assertEquals(144, counter.getSize());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRangeIndex() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 150;
        try {
            counter.getCounts(index);
            fail();
        } catch (OutOfRangeException e) {
        }
    }

    @Test
    public void testGetCounts_ValidIndex() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 10;
        int[] expected = {0, 2, 3, 1};
        assertArrayEquals(expected, counter.getCounts(index));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeIndex() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = {1, 2, 3};
        try {
            counter.getCount(c);
            fail();
        } catch (OutOfRangeException e) {
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_InvalidDimension() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = {1, 2};
        try {
            counter.getCount(c);
            fail();
        } catch (DimensionMismatchException e) {
        }
    }

    @Test
    public void testGetCount_ValidIndex() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = {1, 2, 3};
        int expected = 11 * 3 + 2 * 3 + 1 * 4 + 1;
        assertEquals(expected, counter.getCount(c));
    }

    @Test
    public void testIterator_EmptyCounter() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator_NonEmptyCounter() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        // ... continue testing next() until all elements are exhausted
    }

    @Test
    public void testIterator_HasNextreturnsFalse() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        // Set all counters to their maximum value
        for (int i = 0; i < counter.getDimension(); ++i) {
            for (int j = 0; j < counter.getSizes()[i]; ++j) {
                iterator.next();
            }
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expected = "[0][0][0] [1][0][0] [1][1][0] [1][1][1] [2][0][0] [2][1][0] [2][1][1] [2][2][0] [2][2][1] [2][2][2] [3][0][0] [3][0][1] [3][0][2] [3][1][0] [3][1][1] [3][1][2] [3][2][0] [3][2][1] [3][2][2]";
        assertEquals(expected, counter.toString());
    }
}