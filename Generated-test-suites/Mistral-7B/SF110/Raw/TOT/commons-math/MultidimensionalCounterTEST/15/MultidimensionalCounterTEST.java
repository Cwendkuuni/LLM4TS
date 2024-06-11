import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] dimensions = {2, 3, 4};
        MultidimensionalCounter mdc = null;
        try {
            mdc = new MultidimensionalCounter(dimensions);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertEquals(3, mdc.getDimension());
        Assert.assertArrayEquals(new int[]{2, 3, 4}, mdc.getSizes());
        Assert.assertEquals(24, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() {
        int[] dimensions = {-1, 3, 4};
        new MultidimensionalCounter(dimensions);
    }

    @Test
    public void testIterator() {
        int[] dimensions = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(dimensions);
        MultidimensionalCounter.Iterator iterator = mdc.iterator();

        int expectedCount = 0;
        while (iterator.hasNext()) {
            int actualCount = iterator.next();
            Assert.assertEquals(expectedCount++, actualCount);
        }

        Assert.assertEquals(-1, iterator.getCount());
        Assert.assertArrayEquals(new int[]{2, 3, 4}, iterator.getCounts());
    }

    @Test
    public void testGetCount() {
        int[] dimensions = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(dimensions);

        int[] coordinates = {1, 1, 1};
        int actualCount = mdc.getCount(coordinates);
        int expectedCount = 1 + mdc.uniCounterOffset[0] * coordinates[0] + mdc.uniCounterOffset[1] * coordinates[1] + mdc.uniCounterOffset[2] * coordinates[2];
        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeGetCount() {
        int[] dimensions = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(dimensions);

        int[] coordinates = {5, 5, 5};
        mdc.getCount(coordinates);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchGetCount() {
        int[] dimensions = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(dimensions);

        int[] coordinates = {1, 1, 5};
        mdc.getCount(coordinates);
    }

    @Test
    public void testToString() {
        int[] dimensions = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(dimensions);

        String expectedToString = "[1][1][1]";
        String actualToString = mdc.toString();
        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testGetCounts() {
        int[] dimensions = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(dimensions);

        int index = 11;
        int[] actualCounts = mdc.getCounts(index);
        int[] expectedCounts = new int[]{1, 2, 3};
        Assert.assertArrayEquals(expectedCounts, actualCounts);

        int expectedIndex = 11 - mdc.uniCounterOffset[0] - mdc.uniCounterOffset[1] - mdc.uniCounterOffset[2];
        Assert.assertEquals(expectedIndex, actualCounts[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeGetCounts() {
        int[] dimensions = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(dimensions);

        int index = 25;
        mdc.getCounts(index);
    }

    @Test
    public void testIterable() {
        int[] dimensions = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(dimensions);

        List<Integer> actualValues = new ArrayList<>();
        for (Integer value : mdc) {
            actualValues.add(value);
        }

        Collection<Integer> expectedValues = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
        Assert.assertEquals(expectedValues, actualValues);
    }
}