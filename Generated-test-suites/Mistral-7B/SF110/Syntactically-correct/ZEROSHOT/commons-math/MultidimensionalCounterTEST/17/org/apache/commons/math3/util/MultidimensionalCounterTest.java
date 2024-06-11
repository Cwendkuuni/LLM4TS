package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter multidimensionalCounter;

    @Before
    public void setUp() {
        int[] size = {3, 4, 5};
        try {
            multidimensionalCounter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMultidimensionalCounterConstruction() {
        assertEquals(3, multidimensionalCounter.getDimension());
        assertArrayEquals(new int[]{3, 4, 5}, multidimensionalCounter.getSizes());
        assertEquals(540, multidimensionalCounter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        int[] size = {-1, 2, 3};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testIteratorHasNext() {
        Iterator iterator = multidimensionalCounter.iterator();
        for (int i = 0; i < Math.pow(multidimensionalCounter.getSizes()[0], multidimensionalCounter.getDimension()); ++i) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        Iterator iterator = multidimensionalCounter.iterator();
        assertEquals(-1, iterator.next());

        int[] expected = new int[]{0, 0, 0};
        assertArrayEquals(expected, iterator.getCounts());
        assertEquals(0, iterator.getCount());

        for (int i = 0; i < multidimensionalCounter.getSize(); ++i) {
            int next = iterator.next();
            int[] counts = iterator.getCounts();
            int count = iterator.getCount();

            List<Integer> indexList = Arrays.asList(counts);
            int index = indexList.indexOf(Math.max(counts));

            int[] expectedCounts = new int[multidimensionalCounter.getDimension()];
            for (int j = 0; j < multidimensionalCounter.getDimension(); ++j) {
                expectedCounts[j] = (index / MathArrays.prod(multidimensionalCounter.getSizes(), 0, j)) % multidimensionalCounter.getSizes()[j];
            }
            assertArrayEquals(expectedCounts, counts);
            assertEquals(next, count + Arrays.stream(multidimensionalCounter.getSizes()).mapToInt(i -> multidimensionalCounter.uniCounterOffset[0] * i).sum());
        }

        assertNull(iterator.next());
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeExceptionGetCounts() {
        int index = multidimensionalCounter.getTotalSize() + 1;
        multidimensionalCounter.getCounts(index);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchExceptionGetCount() {
        int[] wrongDimension = new int[]{3, 4};
        multidimensionalCounter.getCount(wrongDimension);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeExceptionGetCount() {
        int[] wrongIndex = new int[]{3, 3, 5};
        multidimensionalCounter.getCount(wrongIndex);
    }
}