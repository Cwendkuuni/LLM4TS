
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import java.util.*;

public class MultidimensionalCounterTest {
    // Test codes from all experts combined.

    // Expert 1 tests.
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        int[] sizes = {-1, 2, 3};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(sizes);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertArrayEquals(new int[]{sizes[0]}, e.getArgs());
        }
    }

    @Test
    public void testConstructor() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Assert.assertEquals(6 * 3 * 3, counter.getSize());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = Integer.MIN_VALUE;
        try {
            counter.getCounts(index);
        } catch (OutOfRangeException e) {
            Assert.assertEquals(index, e.getIndex());
            Assert.assertEquals(0, e.getTotalSize());
        }
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 2 * 3 * 2 + 1;
        int[] counts = counter.getCounts(index);
        Assert.assertArrayEquals(new int[]{2, 2, index}, counts);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] coordinates = {1, 1, 1, 1};
        try {
            counter.getCount(coordinates);
        } catch (DimensionMismatchException e) {
            Assert.assertEquals(coordinates.length, e.getDimensions().length);
            Assert.assertEquals(counter.getDimension(), e.getDimensions().length);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] coordinates = {2, 2, 2};
        try {
            counter.getCount(coordinates);
        } catch (OutOfRangeException e) {
            Assert.assertEquals(coordinates[counter.getDimension() - 1], e.getIndex());
            Assert.assertEquals(coordinates[counter.getDimension() - 1], sizes[counter.getDimension() - 1] - 1);
        }
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] coordinates = {1, 1, 1};
        int expectedCount = 1 + sizes[0] * sizes[1] * sizes[2];
        Assert.assertEquals(expectedCount, counter.getCount(coordinates));
    }

    @Test
    public void testGetSize() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Assert.assertEquals(6 * 3 * 3, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] actualSizes = counter.getSizes();
        Assert.assertArrayEquals(sizes, actualSizes);
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expectedString = "[3][3][3]";
        String actualString = counter.toString();
        Assert.assertEquals(expectedString, actualString);
    }

    // Expert 2 tests.
    @Test
    public void testIteratorHasNext() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int expectedCount = iterator.next();
        Assert.assertEquals(0, expectedCount);
    }

    @Test
    public void testIteratorGetCount() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int expectedCount = iterator.getCount();
        Assert.assertEquals(-1, expectedCount);
    }

    @Test
    public void testIteratorGetCounts() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int[] expectedCounts = new int[counter.getDimension()];
        int[] actualCounts = iterator.getCounts();
        Assert.assertArrayEquals(expectedCounts, actualCounts);
    }

    // Expert 3 tests.
    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        iterator.remove();
    }

    // Combined Test.
    @Test
    public void testCombined() {
        int[] sizes = {2, 3, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int expectedCount = iterator.next();
        Assert.assertEquals(0, expectedCount);
        int[] expectedCounts = new int[counter.getDimension()];
        int[] actualCounts = iterator.getCounts();
        Assert.assertArrayEquals(expectedCounts, actualCounts);
        expectedCount = iterator.next();
        Assert.assertEquals(1, expectedCount);
        expectedCounts = new int[]{1, 0, 0};
        actualCounts = iterator.getCounts();
        Assert.assertArrayEquals(expectedCounts, actualCounts);
        expectedCount = iterator.next();
        Assert.assertEquals(2, expectedCount);
        expectedCounts = new int[]{2, 0, 0};
        actualCounts = iterator.getCounts();
        Assert.assertArrayEquals(expectedCounts, actualCounts);
        expectedCount = iterator.next();
        Assert.assertEquals(3, expectedCount);
        expectedCounts = new int[]{2, 0, 1};
        actualCounts = iterator.getCounts();
        Assert.assertArrayEquals(expectedCounts, actualCounts);
        expectedCount = iterator.next();
        Assert.assertEquals(4, expectedCount);
        expectedCounts = new int[]{2, 0, 2};
        actualCounts = iterator.getCounts();
        Assert.assertArrayEquals(expectedCounts, actualCounts);
        expectedCount = iterator.next();
        Assert.assertEquals(5, expectedCount);
        expectedCounts = new int[]{2, 1, 0};
        actualCounts = iterator.getCounts();
        Assert.assertArrayEquals(expectedCounts, actualCounts);
        expectedCount = iterator.next();
        Assert.assertEquals(6, expectedCount);
        expectedCounts = new int[]{2, 1, 1};
        actualCounts = iterator.getCounts();
        Assert.assertArrayEquals(expectedCounts, actualCounts);
        Assert.assertFalse(iterator.hasNext());
    }
}