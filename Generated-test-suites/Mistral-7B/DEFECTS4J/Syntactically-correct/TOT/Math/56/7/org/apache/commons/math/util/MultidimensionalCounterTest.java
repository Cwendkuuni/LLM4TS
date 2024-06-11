package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for MultidimensionalCounter.
 */
public class MultidimensionalCounterTest {

    /**
     * Test creation with correct dimensions.
     */
    @Test
    public void testCorrectCreation() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(new int[] {1, 2, 3}, counter.getSizes());
        Assert.assertEquals(1 * 2 * 3, counter.getSize());
    }

    /**
     * Test creation with incorrect dimensions.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testIncorrectCreation() {
        new MultidimensionalCounter(-1, 2, 3);
    }

    /**
     * Test iteration with correct multidimensional counter.
     */
    @Test
    public void testCorrectIteration() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        Iterator iterator = counter.iterator();
        int expectedCount = 0;

        while (iterator.hasNext()) {
            int actualCount = iterator.next().intValue();
            Assert.assertEquals("Unexpected count: " + actualCount,
                               expectedCount + 1, actualCount);
            expectedCount++;
        }
        Assert.assertEquals("Unexpected number of elements: " + expectedCount,
                           1 * 2 * 3, expectedCount);
    }

    /**
     * Test iteration with incorrect multidimensional counter.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIncorrectIteration() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        Iterator iterator = counter.iterator();
        iterator.remove();
    }

    /**
     * Test getting multidimensional counter from unidimensional index.
     */
    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        for (int i = 0; i < 1 * 2 * 3; i++) {
            int[] actualCounts = counter.getCounts(i);
            int[] expectedCounts = new int[counter.getDimension()];

            for (int j = 0; j < counter.getDimension(); j++) {
                expectedCounts[j] = i / MathUtils.mult(counter.getSizes()[0],
                                                       counter.getSizes()[1],
                                                       counter.getSizes()[2]);
                Assert.assertEquals("Unexpected count in dimension " + j +
                                    ": " + actualCounts[j] +
                                    " vs " + expectedCounts[j],
                                   expectedCounts[j], actualCounts[j]);
            }
        }
    }

    /**
     * Test getting unidimensional counter from multidimensional indices.
     */
    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);

        for (int i = 0; i < 1 * 2 * 3; i++) {
            int[] actualCounts = counter.getCounts(i);
            int actualCount = counter.getCount(actualCounts);
            int expectedCount = i;

            Assert.assertEquals("Unexpected count: " + actualCount +
                               " vs " + expectedCount,
                               expectedCount, actualCount);
        }
    }

    /**
     * Test out of range multidimensional counter index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        counter.getCount(-1, 2, 3);
    }

    /**
     * Test out of range unidimensional counter index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeGetCountFromUnidimensional() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        counter.getCount(MathUtils.add(counter.getSize(), 1));
    }

    /**
     * Test dimension mismatch multidimensional counter indices.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        counter.getCount(new int[] {4});
    }
}