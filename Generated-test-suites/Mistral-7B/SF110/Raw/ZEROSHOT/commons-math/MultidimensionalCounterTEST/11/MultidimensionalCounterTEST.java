package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterCreation() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            assertEquals(3, mdc.getDimension());
            assertArrayEquals(size, mdc.getSizes());
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterCreationNegativeSize() {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterCreationZeroSize() {
        int[] size = {0, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            int index = 5;
            int[] expected = {1, 1, 1};
            int[] actual = mdc.getCounts(index);
            assertArrayEquals(expected, actual);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | OutOfRangeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountsOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            int index = -1;
            mdc.getCounts(index);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            int[] coord = {1, 1, 1};
            int expected = 1;
            int actual = mdc.getCount(coord);
            assertEquals(expected, actual);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | OutOfRangeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterGetCountOutOfRange() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            int[] coord = {2, 2, 2};
            mdc.getCount(coord);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultidimensionalCounterGetCountDifferentDimension() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            int[] coord1 = {1, 1, 1};
            int coord2 = 2;
            int expected1 = 1;
            int expected2 = 2;
            int actual1 = mdc.getCount(coord1);
            int actual2 = mdc.getCount(coord2);
            assertEquals(expected1, actual1);
            assertEquals(expected2, actual2);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | OutOfRangeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultidimensionalCounterSize() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            int expected = 2 * 3 * 4;
            int actual = mdc.getSize();
            assertEquals(expected, actual);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            Iterator iterator = mdc.iterator();
            List expectedList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
            List actualList = new ArrayList<>();
            while (iterator.hasNext()) {
                actualList.add(iterator.next());
            }
            assertEquals(expectedList, actualList);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultidimensionalCounterIteratorGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            Iterator iterator = mdc.iterator();
            iterator.next();
            int expected = 0;
            int actual = iterator.getCount();
            assertEquals(expected, actual);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultidimensionalCounterIteratorGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            Iterator iterator = mdc.iterator();
            iterator.next();
            int[] expected = {1, 0, 0};
            int[] actual = iterator.getCounts();
            assertArrayEquals(expected, actual);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
            String expected = "[1][1][1]";
            String actual = mdc.toString();
            assertEquals(expected, actual);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}