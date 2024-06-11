package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor_ValidSizes() {
        int[] sizes = {2, 3, 4};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
            assertEquals(3, counter.getDimension());
            int[] sizesCopy = MathArrays.copyOf(counter.getSizes());
            assertArrayEquals(sizes, sizesCopy);
        } catch (NotStrictlyPositiveException e) {
            fail("Invalid exception thrown during constructor call.");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeSizes() {
        int[] sizes = {-1, 3, 4};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetDimension() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRangeIndex() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(-1);
    }

    @Test
    public void testGetCounts_ValidIndex() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] indices = counter.getCounts(10);
        assertEquals(3, indices.length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCounts_InvalidDimension() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(3); // valid index but wrong dimension
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeIndex() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeIndex2() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1, 2);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_InvalidDimension() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(3, 0);
    }

    @Test
    public void testGetCount_ValidIndex() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int count = counter.getCount(1, 1);
        assertEquals(6, count);
    }

    @Test
    public void testGetSize() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(12 * 3 * 4, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] sizesCopy = counter.getSizes();
        assertArrayEquals(sizes, sizesCopy);
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expected = "[0][0][0]";
        String actual = counter.toString();
        assertEquals(expected, actual);
    }

    // Test the Iterator class

    @Test
    public void testIterator_HasNext_FirstElement() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testIterator_HasNext_SubsequentElements() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.next();
        iterator.next();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testIterator_Next_FirstElement() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int expected = 0;
        int actual = iterator.next();
        assertEquals(expected, actual);
    }

    @Test
    public void testIterator_Next_SubsequentElements() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.next();
        iterator.next();
        int expected = 2;
        int actual = iterator.next();
        assertEquals(expected, actual);
    }

    @Test
    public void testIterator_GetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int expected = 0;
        int actual = iterator.getCount();
        assertEquals(expected, actual);
    }

    @Test
    public void testIterator_GetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int[] expected = new int[counter.getDimension()];
        int[] actual = iterator.getCounts();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIterator_GetCount_LastDimension() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int expected = 3;
        int actual = iterator.getCount(counter.getDimension() - 1);
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }
}