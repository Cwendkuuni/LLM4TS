import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Assert.assertEquals(2 * 4 * 3, counter.getSize());
        Assert.assertArrayEquals(sizes, counter.getSizes());
    }

    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        int[] sizes = {-2, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            int index = iterator.next();
            int[] multidimensionalCounts = iterator.getCounts();
            int unidimensionalCount = counter.getCount(multidimensionalCounts);
            Assert.assertEquals(index, unidimensionalCount);
            count++;
        }
        Assert.assertEquals(Precision.round(Math.pow(2, 3) * Math.pow(4, 2) * Math.pow(3, 1), 0), count);
    }

    @Test(expected = org.apache.commons.math3.exception.OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 100;
        counter.getCount(index);
    }

    @Test
    public void testMultidimensionalCounterDimension() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Assert.assertEquals(3, counter.getDimension());
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] multidimensionalCounts = counter.getCounts(0);
        int[] expected = {0, 0, 0};
        Assert.assertArrayEquals(expected, multidimensionalCounts);
        int unidimensionalCount = counter.getCount(multidimensionalCounts);
        Assert.assertEquals(0, unidimensionalCount);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] multidimensionalCounts = {1, 1, 1};
        int unidimensionalCount = counter.getCount(multidimensionalCounts);
        int[] expected = {1, 1, 1};
        Assert.assertArrayEquals(expected, counter.getCounts(unidimensionalCount));
        Assert.assertEquals(unidimensionalCount, counter.getCount(expected));
    }

    @Test
    public void testMultidimensionalCounterSize() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Assert.assertEquals(Precision.round(Math.pow(2, 3) * Math.pow(4, 2) * Math.pow(3, 1), 0), counter.getSize());
    }
}