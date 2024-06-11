package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException was not thrown");
        }
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(new int[]{2, 3, 4}, mdc.getSizes());
        assertEquals(24, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSize() {
        int[] size = {-1, 2, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = {1, 2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        MultidimensionalCounter.Iterator it = mdc.iterator();
        int count = -1;
        while (it.hasNext()) {
            int num = it.next();
            if (count == -1) {
                count = num;
            } else {
                assertEquals(count + 1, num);
            }
            assertEquals(it.getCount(), count + 1);
        }
        assertEquals(-1, it.getCount());
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterInvalidGetCount() {
        int[] size = {2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterInvalidGetCounts() {
        int[] size = {2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCounts(4);
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = {2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] counts = mdc.getCounts(5);
        assertArrayEquals(new int[]{1, 2, 3}, counts);
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] size = {2, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        String str = mdc.toString();
        String expected = "[0][0][0] [0][0][1] [0][1][0] [0][1][1] [0][2][0] [0][2][1] [0][2][2] [1][0][0] [1][0][1] [1][1][0] [1][1][1] [1][2][0] [1][2][1] [1][2][2]";
        assertEquals(expected, str);
    }
}