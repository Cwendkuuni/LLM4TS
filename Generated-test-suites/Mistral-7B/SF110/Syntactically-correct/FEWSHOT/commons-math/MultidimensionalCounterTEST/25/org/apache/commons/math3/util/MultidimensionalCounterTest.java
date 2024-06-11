package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[]{3, 4, 5}, counter.getSizes());
        assertEquals(13823, counter.getSize());
    }

    @Test
    public void testOutOfRangeException() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(-1));
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(13823));
    }

    @Test
    public void testDimensionMismatchException() {
        int[] size1 = {3, 4};
        int[] size2 = {3, 4, 5};
        assertThrows(DimensionMismatchException.class, () -> new MultidimensionalCounter(size1));
    }

    @Test
    public void testNotStrictlyPositiveException() {
        int[] size = {-3, 4, 5};
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(size));
    }

    @ParameterizedTest(name = "{index} = {0}")
    @MethodSource("getCountsTestCaseData")
    public void testGetCounts(int index, int[] expected) {
        int[] size = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] actual = counter.getCounts(index);
        assertArrayEquals(expected, actual);
    }

    @ParameterizedTest(name = "{0} = {1}")
    @MethodSource("getCountTestCaseData")
    public void testGetCount(int[] coordinates, int expected) {
        int[] size = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int actual = counter.getCount(coordinates);
        assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        List<Integer> list = new ArrayList<>();
        for (Integer i : counter) {
            list.add(i);
        }
        assertEquals(13 * 2 * 3, list.size());
        assertArrayEquals(counter.getCounts(list.get(0)), new int[]{2, 3, 4});
    }

    @Test
    public void testToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals("[2][3][4]", counter.toString());
    }

    private static int[][] getCountsTestCaseData() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        return new int[][]{{0, 0, 0}, {1, 2, 3}, {2, 2, 2}, {2, 3, 2}, {5, 1, 2}, {2, 2, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}};
    }

    private static int[][] getCountTestCaseData() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        return new int[][]{{0, 0, 0}, {1, 1, 1}, {2, 2, 2}, {2, 3, 2}, {5, 1, 2}, {1, 2, 3}, {2, 2, 2}, {2, 3, 2}, {2, 3, 3}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}, {2, 3, 4}};
    }
}