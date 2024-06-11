import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Assert.assertEquals(3, mdc.getDimension());
        Assert.assertArrayEquals(size, mdc.getSizes());
        Assert.assertEquals(144, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        int[] size = {-1, 3, 4};
        try {
            new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals(-1, size[0]);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterZeroStaticSize() {
        int[] size = {2, 3, 0};
        try {
            new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals(0, size[2]);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRangeIndex() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int index = mdc.getTotalSize() + 1;
        try {
            mdc.getCounts(index);
        } catch (OutOfRangeException e) {
            Assert.assertEquals(index, Precision.round(index.doubleValue() + 0.5));
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithDimensionMismatch() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] invalidCount = {5, 3};
        try {
            mdc.getCounts(MathArrays.sum(invalidCount));
        } catch (DimensionMismatchException e) {
            // expected exception
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithOutOfRangeIndices() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] invalidCount = {5, 3, 2};
        try {
            mdc.getCounts(MathArrays.sum(invalidCount));
        } catch (OutOfRangeException e) {
            // expected exception
        }
    }

    @Test
    public void testGetCountsValidIndex() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int validIndex = MathArrays.sum(new int[]{1, 2, 3});
        int[] expected = new int[]{1, 2, 3};
        int[] result = mdc.getCounts(validIndex);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGetCountValidIndices() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] validIndices = new int[]{1, 2, 1};
        int expectedTotal = MathArrays.sum(new int[]{2, 3, 1});
        int resultTotal = mdc.getCount(validIndices);
        Assert.assertEquals(expectedTotal, resultTotal);
        int[] expectedIndices = validIndices;
        int[] resultIndices = mdc.getCounts(resultTotal);
        Assert.assertArrayEquals(expectedIndices, resultIndices);
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        for (int i = 0; i < mdc.getTotalSize(); ++i) {
            Assert.assertTrue(iterator.hasNext());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetNext() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        for (int i = 0; i < mdc.getTotalSize(); ++i) {
            int next = iterator.next();
            Assert.assertTrue((next >= 0) && (next < mdc.getTotalSize()));
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCount() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        for (int i = 0; i < mdc.getTotalSize(); ++i) {
            int next = iterator.next();
            int count = iterator.getCount();
            Assert.assertEquals(next, count);
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        for (int i = 0; i < mdc.getTotalSize(); ++i) {
            int next = iterator.next();
            int[] counts = iterator.getCounts();
            Assert.assertEquals(next, MathArrays.sum(counts));
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        String expected = "[1][1][1]";
        Iterator iterator = mdc.iterator();
        int index = 0;
        String result = "";
        while (iterator.hasNext()) {
            int next = iterator.next();
            String nextString = String.valueOf(next);
            result += "[";
            for (int i = 0; i < mdc.getDimension(); ++i) {
                if (i > 0) {
                    result += ", ";
                }
                result += nextString;
            }
            result += "]";
            if (iterator.hasNext()) {
                result += ", ";
            }
            index++;
        }
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetCountsForAllIndices() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int totalSize = Math.pow(size[0], size[1]) * size[2];
        List<Integer> allIndices = new ArrayList<>();
        for (int i = 0; i < totalSize; ++i) {
            allIndices.add(i);
        }
        for (int index : allIndices) {
            int[] indices = mdc.getCounts(index);
            int[] expected = new int[]{index / mdc.getSize(0), index % mdc.getSize(0), index % mdc.getSize(1)};
            Assert.assertArrayEquals(expected, indices);
        }
    }

    @Test
    public void testGetCountsForSpecificIndices() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] specificIndices = new int[]{1, 1, 1};
        int[] expected = new int[]{1, 1, 1};
        int[] result = mdc.getCounts(MathArrays.sum(specificIndices));
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testIteratorGetCountsForFirstItem() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        int[] expected = new int[]{0, 0, 0};
        int[] result = iterator.getCounts();
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testIteratorGetCountsForLastItem() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        int index = mdc.getTotalSize() - 1;
        int[] expected = new int[]{size[0] - 1, size[1] - 1, size[2] - 1};
        int[] result = iterator.getCounts();
        Assert.assertArrayEquals(expected, result);
    }
}