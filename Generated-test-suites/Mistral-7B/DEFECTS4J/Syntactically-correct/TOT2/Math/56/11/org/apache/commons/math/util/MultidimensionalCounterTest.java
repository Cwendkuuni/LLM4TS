import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collection;

@RunWith(BlockJUnit4ClassRunner.class)
public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Assert.assertEquals(sizes.length, mdc.getDimension());
        Assert.assertArrayEquals(sizes, mdc.getSizes());
        Assert.assertEquals(Precision.round(Math.pow(2, sizes.length) * (sizes[sizes.length - 1] + 1), 0), mdc.getSize());
    }

    @Test
    public void testInvalidSize() {
        try {
            int[] sizes = {-1, 4, 3};
            new MultidimensionalCounter(sizes);
            Assert.fail();
        } catch (NotStrictlyPositiveException ex) {
            // expected
        }
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            int[] counts = mdc.getCounts(i);
            int unidimCount = mdc.getCount(counts);
            Assert.assertEquals(i, unidimCount);
        }
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        for (int i = 0; i < sizes[0]; i++) {
            for (int j = 0; j < sizes[1]; j++) {
                for (int k = 0; k < sizes[2]; k++) {
                    int count = mdc.getCount(i, j, k);
                    int[] counts = mdc.getCounts(count);
                    Assert.assertArrayEquals(new int[]{i, j, k}, counts);
                }
            }
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCount(-1, 0, 0);
    }

    @Test
    public void testIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator iterator = mdc.iterator();
        int expectedCount = 0;
        while (iterator.hasNext()) {
            int count = iterator.next();
            Assert.assertEquals(expectedCount++, count);
            int[] counts = iterator.getCounts();
            Assert.assertArrayEquals(counts, mdc.getCounts(count));
        }
        Assert.assertEquals(sizes[0] * sizes[1] * sizes[2], expectedCount);
    }

    @Test
    public void testReset() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator iterator = mdc.iterator();
        int expectedCount = 0;
        while (iterator.hasNext()) {
            int count = iterator.next();
            iterator.remove();
            expectedCount++;
        }
        Assert.assertEquals(sizes[0] * sizes[1] * sizes[2], mdc.getSize());
        iterator = mdc.iterator();
        int resetCount = iterator.next();
        iterator.remove();
        Assert.assertEquals(expectedCount, mdc.getSize());
        Assert.assertEquals(resetCount, mdc.getCount(mdc.getCounts(resetCount)));
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        String expectedString = "[0] [0][0] [0][0][0] [0][0][0][0] [0][0][0][1] ...";
        Assert.assertEquals(expectedString, mdc.toString());
    }

    @Test
    public void testInvalidDimensionCount() {
        int[] sizes = {2, 4};
        try {
            new MultidimensionalCounter(sizes);
            Assert.fail();
        } catch (DimensionMismatchException ex) {
            // expected
        }
    }

    @Test
    public void testGetSize() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Assert.assertEquals(sizes[0] * sizes[1] * sizes[2], mdc.getSize());
    }

    @Test
    public void testIterable() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Collection<Integer> collection = new java.util.ArrayList<>(mdc);
        Assert.assertEquals(sizes[0] * sizes[1] * sizes[2], collection.size());
        Iterator iterator = mdc.iterator();
        int expectedCount = 0;
        while (iterator.hasNext()) {
            int count = iterator.next();
            Assert.assertEquals(expectedCount++, count);
            Assert.assertTrue(collection.contains(count));
        }
    }
}