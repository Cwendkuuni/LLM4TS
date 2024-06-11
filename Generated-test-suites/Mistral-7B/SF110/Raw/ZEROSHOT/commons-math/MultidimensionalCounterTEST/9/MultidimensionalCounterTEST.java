package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(new int[]{2, 3, 4}, mdc.getSizes());
        assertEquals(24, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        int[] size = {-1, 3, 4};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterZeroTotalSize() {
        int[] size = {2, 3, 0};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int index = 0;
        int[] counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertArrayEquals(new int[]{0, 0, index}, counts);

        index = mdc.getSize() - 1;
        counts = mdc.getCounts(index);
        assertEquals(3, counts.length);
        assertArrayEquals(new int[]{2 - 1, 3 - 1, 4 - 1}, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRangeGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRangeGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] c = {1, 1, 10};
        mdc.getCount(c);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterDimensionMismatchGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] c = {1, 1, 5};
        mdc.getCount(c);
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = mdc.iterator();
        List<Integer> expectedList = Arrays.asList(0, 3, 6, 7, 11, 12, 15, 18, 21, 23, 24);
        List<Integer> actualList = new ArrayList<>();
        while (iterator.hasNext()) {
            actualList.add(iterator.next());
        }
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals("[0][0][0]", mdc.toString());
    }
}