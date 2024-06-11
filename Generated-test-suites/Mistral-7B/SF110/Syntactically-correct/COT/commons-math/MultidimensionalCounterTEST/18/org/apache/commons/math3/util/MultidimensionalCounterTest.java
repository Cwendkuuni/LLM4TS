package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MultidimensionalCounterTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSizes() {
        int[] sizes = {-1, 2, 3};
        try {
            new MultidimensionalCounter(sizes);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testGetDimension() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int dimension = counter.getDimension();
        assertEquals(3, dimension);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(-1);
        fail("Expected OutOfRangeException");
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 10;
        int[] counts = counter.getCounts(index);
        int[] expectedCounts = new int[sizes.length];
        expectedCounts[0] = index / (2 * 3);
        expectedCounts[1] = (index % (2 * 3)) / 3;
        expectedCounts[2] = index % 3;
        assertArrayEquals(expectedCounts, counts);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongDimension() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] counts = counter.getCounts(new int[]{1, 4, 3});
        fail("Expected DimensionMismatchException");
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] count = counter.getCount(new int[]{-1, 2, 3});
        fail("Expected OutOfRangeException");
    }

    @Test
    public void testGetCount() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] count = counter.getCount(new int[]{1, 2, 3});
        assertEquals(1 * 2 * 3 + 1, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongDimension2() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int count = counter.getCount(new int[]{1, 4});
        fail("Expected DimensionMismatchException");
    }

    @Test
    public void testGetSizes() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] expectedSizes = new int[sizes.length];
        System.arraycopy(sizes, 0, expectedSizes, 0, sizes.length);
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testToString() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expectedToString = "[1][2][3]";
        String toString = counter.toString();
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        List<Integer> expectedList = Arrays.asList(0, 1, 2, 3);
        List<Integer> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        assertEquals(expectedList, list);
    }

    @Test
    public void testIteratorGetCount() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        int count = iterator.getCount();
        assertEquals(0, count);
    }

    @Test
    public void testIteratorGetCounts() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        int[] counts = iterator.getCounts();
        int[] expectedCounts = new int[sizes.length];
        Arrays.fill(expectedCounts, 0);
        assertArrayEquals(expectedCounts, counts);
    }

    @Test
    public void testIteratorGetCountLast() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        int countLast = iterator.getCount(sizes.length - 1);
        assertEquals(0, countLast);
    }

    @Test
    public void testIteratorNextWhileNotLast() {
        int[] sizes = {1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            ++count;
        }
        assertEquals(Precision.round(Math.pow(sizes[0], sizes.length), 0), (long) count);
    }

}