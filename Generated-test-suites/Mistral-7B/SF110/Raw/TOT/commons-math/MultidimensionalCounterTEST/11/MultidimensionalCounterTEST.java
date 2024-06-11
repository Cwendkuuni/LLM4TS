package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter multidimensionalCounter;

    @Before
    public void setUp() {
        try {
            multidimensionalCounter = new MultidimensionalCounter(new int[]{3, 2, 1});
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Should not throw NotStrictlyPositiveException with dimensions 3, 2, 1");
        }
    }

    @Test
    public void testGetDimension() {
        Assert.assertEquals(3, multidimensionalCounter.getDimension());
    }

    @Test
    public void testGetSizes() {
        List<Integer> expectedSizes = Arrays.asList(3, 2, 1);
        List<Integer> actualSizes = Arrays.asList(multidimensionalCounter.getSizes());
        Assert.assertEquals(expectedSizes, actualSizes);
    }

    @Test
    public void testGetTotalSize() {
        Assert.assertEquals(18, multidimensionalCounter.getSize());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegativeIndex() {
        multidimensionalCounter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRangeIndex() {
        multidimensionalCounter.getCounts(17);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongDimension() {
        try {
            multidimensionalCounter.getCounts(new int[]{3, 4});
        } catch (DimensionMismatchException e) {
            Assert.assertEquals(3, e.getExpectedDimension());
            Assert.assertEquals(2, e.getActualDimension());
        }
    }

    @Test
    public void testGetCounts() {
        int[] expectedIndices = {0, 0, 0};
        int[] actualIndices = multidimensionalCounter.getCounts(0);
        Assert.assertArrayEquals(expectedIndices, actualIndices);

        int[] expectedIndices2 = {2, 1, 0};
        int[] actualIndices2 = multidimensionalCounter.getCounts(11);
        Assert.assertArrayEquals(expectedIndices2, actualIndices2);

        int[] expectedIndices3 = {2, 1, 2};
        int[] actualIndices3 = multidimensionalCounter.getCounts(16);
        Assert.assertArrayEquals(expectedIndices3, actualIndices3);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        multidimensionalCounter.getCount(-1, new int[]{0, 0, 0});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongDimension() {
        try {
            multidimensionalCounter.getCount(new int[]{3, 3}, new int[]{0, 0, 0});
        } catch (DimensionMismatchException e) {
            Assert.assertEquals(3, e.getExpectedDimension());
            Assert.assertEquals(3, e.getActualDimension());
        }
    }

    @Test
    public void testGetCount() {
        int expectedCount = 0;
        int actualCount = multidimensionalCounter.getCount(new int[]{0, 0, 0});
        Assert.assertEquals(expectedCount, actualCount);

        expectedCount = 11;
        actualCount = multidimensionalCounter.getCount(new int[]{2, 0, 0});
        Assert.assertEquals(expectedCount, actualCount);

        expectedCount = 16;
        actualCount = multidimensionalCounter.getCount(new int[]{2, 1, 2});
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex() {
        multidimensionalCounter.getCount(new int[]{3, 2, 1}, 18);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex2() {
        multidimensionalCounter.getCount(new int[]{-1, 0, 0}, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex3() {
        multidimensionalCounter.getCount(new int[]{0, -1, 0}, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex4() {
        multidimensionalCounter.getCount(new int[]{0, 0, -1}, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeCount() {
        multidimensionalCounter.getCount(new int[]{0, 0, 0}, -1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWrongDimensionCount() {
        try {
            multidimensionalCounter.getCount(new int[]{2, 3}, new int[]{0, 0, 0});
        } catch (OutOfRangeException e) {
            Assert.assertEquals(3, e.getSize()[0]);
            Assert.assertEquals(3, e.getSize()[1]);
            Assert.assertEquals(3, e.getSize()[2]);
        }
    }

    @Test
    public void testIteratorHasNext() {
        Iterator iterator = multidimensionalCounter.iterator();
        for (int i = 0; i < 18; ++i) {
            Assert.assertTrue(iterator.hasNext());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCount() {
        Iterator iterator = multidimensionalCounter.iterator();
        for (int i = 0; i < 18; ++i) {
            int count = iterator.getCount();
            int expectedCount = i;
            Assert.assertEquals(expectedCount, count);
        }
        Assert.assertEquals(-1, multidimensionalCounter.getCount());
    }

    @Test
    public void testIteratorGetCounts() {
        Iterator iterator = multidimensionalCounter.iterator();
        for (int i = 0; i < 18; ++i) {
            int[] counts = iterator.getCounts();
            int[] expectedCounts = new int[3];
            int index = 0;
            for (int j = 0; j < 3; ++j) {
                expectedCounts[j] = Precision.floorDivide(i, MathArrays.product(multidimensionalCounter.getSizes()));
                index = j + 1;
            }
            Assert.assertArrayEquals(Arrays.asList(expectedCounts).toArray(), counts);
        }
        Assert.assertArrayEquals(new int[0], multidimensionalCounter.getCounts());
    }
}