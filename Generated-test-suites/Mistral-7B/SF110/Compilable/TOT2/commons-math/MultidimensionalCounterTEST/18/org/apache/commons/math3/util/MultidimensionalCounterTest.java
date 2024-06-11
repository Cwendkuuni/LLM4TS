package org.junit.tests;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] dimensionSizes = new int[]{3, 4, 5};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(dimensionSizes);
        assertEquals(3, multidimensionalCounter.getDimension());
        assertArrayEquals(new int[]{3, 4, 5}, multidimensionalCounter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] negativeSizes = new int[]{-1, 2, 3};
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(negativeSizes));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testSizeZeroElement() {
        int[] sizeZeroElement = new int[]{3, 0, 5};
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(sizeZeroElement));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNonStrictlyPositiveSizes() {
        int[] nonStrictlyPositiveSizes = new int[]{3, -1, 5};
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(nonStrictlyPositiveSizes));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfBounds() {
        int[] dimensionSizes = new int[]{3, 4, 5};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(dimensionSizes);
        assertThrows(OutOfRangeException.class, () -> multidimensionalCounter.getCounts(100));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidDimension() {
        int[] dimensionSizes = new int[]{3, 4, 5};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(dimensionSizes);
        assertThrows(DimensionMismatchException.class, () -> multidimensionalCounter.getCount(new int[]{6, 4, 5}));
    }

    @Test
    public void testGetCount() {
        int[] dimensionSizes = new int[]{3, 4, 5};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(dimensionSizes);
        int[] expectedCounts = new int[]{1, 2, 3, 4, 5, 15};
        int[] actualCounts = multidimensionalCounter.getCounts(15);
        assertArrayEquals(expectedCounts, actualCounts);
    }

    @Test
    public void testGetSize() {
        int[] dimensionSizes = new int[]{3, 4, 5};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(dimensionSizes);
        int expectedSize = 3 * 4 * 5;
        int actualSize = multidimensionalCounter.getSize();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testToString() {
        int[] dimensionSizes = new int[]{3, 4, 5};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(dimensionSizes);
        String expectedString = "[1][2][3]";
        String actualString = multidimensionalCounter.toString();
        assertEquals(expectedString, actualString);
    }
}