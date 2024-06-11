import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(sizes, counter.getSizes());
        Assert.assertEquals(1440, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        int[] sizes = {3, -1, 5};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSize() {
        int[] sizes = {3, 4, 0};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIteratorBasic() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int count = -1;
        while (iterator.hasNext()) {
            int nextInt = iterator.next();
            int[] counts = iterator.getCounts();
            count++;
            Assert.assertEquals(count, nextInt);
            Assert.assertArrayEquals(counts, new int[]{count / sizes[2], count % sizes[2], count % sizes[1], count % sizes[0]});
        }
        Assert.assertEquals(1440, count);
    }

    @Test
    public void testGetCount() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] indices = {1, 2, 3};
        int expected = 15;
        int actual = counter.getCount(indices);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongDimension() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] wrongIndices = {5, 2, 3};
        counter.getCount(wrongIndices);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] outOfRangeIndices = {10, 2, 3};
        counter.getCount(outOfRangeIndices);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] indices = {1, 2, 3};
        int[] expected = {1, 2, 3};
        int[] actual = counter.getCounts(counter.getCount(indices));
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] outOfRangeIndices = {10, 2, 3};
        counter.getCounts(counter.getCount(outOfRangeIndices));
    }

    @Test
    public void testToString() {
        int[] sizes = {3, 4, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] indices = {1, 2, 3};
        String expected = "[1][2][3]";
        String actual = counter.toString();
        Assert.assertEquals(expected, actual);
    }
}