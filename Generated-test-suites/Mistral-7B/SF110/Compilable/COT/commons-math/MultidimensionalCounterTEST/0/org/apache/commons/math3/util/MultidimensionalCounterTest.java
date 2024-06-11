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
import java.util.Collections;
import java.util.List;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor_positiveDimensions() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(sizes, counter.getSizes());
        assertEquals(Precision.round(Math.pow(2, sizes.length) * (sizes.length == 1 ? 1 : 1 + sizes[0]), 0), counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_negativeSize() throws Exception {
        int[] sizes = {1, -1};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_zeroSize() throws Exception {
        int[] sizes = {1, 0};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetDimension() {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_outOfRange() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(-1);
    }

    @Test
    public void testGetCounts() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 5;
        int[] expectedIndices = {1, 1, 1};
        int[] expectedCounts = {1, 1, 1};
        int[] actualIndices = counter.getCounts(index);
        assertArrayEquals(expectedIndices, actualIndices);
        assertArrayEquals(expectedCounts, counter.getSizes());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_outOfRange() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = {0, -1, 1};
        counter.getCount(c);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_wrongDimension() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = {0, 1, 2};
        counter.getCount(c);
    }

    @Test
    public void testGetCount() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] c = {1, 1, 1};
        int expectedCount = 5;
        int actualCount = counter.getCount(c);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetSize() {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int expectedSize = (int) Math.pow(2, sizes.length) * (sizes.length == 1 ? 1 : 1 + sizes[0]);
        int actualSize = counter.getSize();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetSizes() {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] expectedSizes = sizes;
        int[] actualSizes = counter.getSizes();
        assertArrayEquals(expectedSizes, actualSizes);
    }

    @Test
    public void testToString() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expectedString = "[1][1][1]";
        String actualString = counter.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testIterator_hasNext() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator_next() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int expectedNext = 0;
        int actualNext = iterator.next();
        assertEquals(expectedNext, actualNext);
        expectedNext = 1;
        actualNext = iterator.next();
        assertEquals(expectedNext, actualNext);
        expectedNext = -1;
        actualNext = iterator.next();
        assertEquals(expectedNext, actualNext);
    }

    @Test
    public void testIterator_getCount() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int expectedCount = 0;
        int actualCount = iterator.getCount();
        assertEquals(expectedCount, actualCount);
        expectedCount = 2;
        iterator.next();
        actualCount = iterator.getCount();
        assertEquals(expectedCount, actualCount);
        expectedCount = 3;
        iterator.next();
        actualCount = iterator.getCount();
        assertEquals(expectedCount, actualCount);
        expectedCount = -1;
        iterator = counter.iterator();
        actualCount = iterator.getCount();
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testIterator_getCounts() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] expectedCounts = {0, 0, 0};
        int[] actualCounts = iterator.getCounts();
        assertArrayEquals(expectedCounts, actualCounts);
        expectedCounts = new int[] {2, 2, 1};
        iterator.next();
        actualCounts = iterator.getCounts();
        assertArrayEquals(expectedCounts, actualCounts);
        expectedCounts = new int[] {2, 2, 2};
        iterator.next();
        actualCounts = iterator.getCounts();
        assertArrayEquals(expectedCounts, actualCounts);
        expectedCounts = new int[] {1, 2, 2};
        iterator.next();
        actualCounts = iterator.getCounts();
        assertArrayEquals(expectedCounts, actualCounts);
        expectedCounts = new int[] {1, 1, 2};
        iterator = counter.iterator();
        actualCounts = iterator.getCounts();
        assertArrayEquals(expectedCounts, actualCounts);
    }

    @Test
    public void testIterator_getCount_wrongDimension() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int expectedDim = 3;
        int actualDim = iterator.getCount(expectedDim);
        assertEquals(expectedDim, actualDim);
        iterator.next();
        expectedDim = 4;
        actualDim = iterator.getCount(expectedDim);
        assertEquals(expectedDim, actualDim);
        assertNotEquals(1, actualDim);
    }

    @Test
    public void testIterator_remove() throws Exception {
        int[] sizes = {2, 3, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        try {
            iterator.remove();
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}