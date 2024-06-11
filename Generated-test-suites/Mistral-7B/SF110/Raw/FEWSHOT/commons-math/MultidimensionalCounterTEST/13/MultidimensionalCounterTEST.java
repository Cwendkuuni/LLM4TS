package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        try {
            new MultidimensionalCounter(new int[]{1, 2, 3});
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException");
        }

        try {
            new MultidimensionalCounter(new int[]{1, 2, -1});
            fail("Should throw NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testGetDimensions() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{3, 4, 5});
        assertEquals(3, mdc.getDimension());
        int[] sizes = mdc.getSizes();
        assertEquals(3, sizes.length);
        assertEquals(3, sizes[0]);
        assertEquals(4, sizes[1]);
        assertEquals(5, sizes[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetIndexOutOfRange() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 2});
        mdc.getCounts(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetDimensionMismatch() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 2});
        mdc.getCounts(new int[]{3});
    }

    @Test
    public void testGetIndexInRange() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 2});
        int[] indices = mdc.getCounts(0);
        assertEquals(0, indices[0]);
        assertEquals(0, indices[1]);
    }

    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{3, 3, 3});
        Iterator iterator = mdc.iterator();
        for (int i = 0; i < Math.pow(3, 3); ++i) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetNext() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{3, 3, 3});
        Iterator iterator = mdc.iterator();
        for (int i = 0; i < Math.pow(3, 3); ++i) {
            int next = iterator.next();
            int[] counts = iterator.getCounts();
            int total = 1;
            for (int j = 0; j < mdc.getDimension(); ++j) {
                total *= counts[j] + 1;
            }
            assertEquals(i, next);
            assertEquals(counts, mdc.getCounts(i));
            assertEquals(total, mdc.getCount(counts));
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{3, 3, 3});
        Iterator iterator = mdc.iterator();
        String expected = "[0][0][0] [0][0][1] [0][0][2] [0][1][0] [0][1][1] [0][1][2] [0][2][0] [0][2][1] [0][2][2] [1][0][0] [1][0][1] [1][0][2] [1][1][0] [1][1][1] [1][1][2] [1][2][0] [1][2][1] [1][2][2] [2][0][0] [2][0][1] [2][0][2] [2][1][0] [2][1][1] [2][1][2] [2][2][0] [2][2][1] [2][2][2]";
        for (int i = 0; i < Math.pow(3, 3); ++i) {
            String actual = String.valueOf(iterator.next());
            String trimmedActual = actual.substring(1, actual.length() - 1).replaceAll("\\[|\\]", "");
            assertEquals(String.format("%d", i), trimmedActual);
            assertEquals(expected.substring(expected.indexOf(String.format("%s", trimmedActual)), expected.indexOf(" [", i + 1)), actual);
        }
    }
}