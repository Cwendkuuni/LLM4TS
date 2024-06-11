import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Assert.assertEquals(sizes.length, counter.getDimension());
        Assert.assertArrayEquals(sizes, counter.getSizes());
        Assert.assertEquals(Precision.round(Math.pow(2, sizes.length) * (sizes[sizes.length - 1] + 1), 0), counter.getSize());
    }

    @Test
    public void testIteration() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            Assert.assertEquals(index, next);
            index++;
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        iterator.next();
        iterator.remove();
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1, -1, -1);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 0;
        for (int i = 0; i < sizes.length; i++) {
            int[] counts = counter.getCounts(index);
            Assert.assertEquals(i, counts[i]);
            index++;
        }
        index = sizes.length * sizes.length;
        Assert.assertArrayEquals(new int[sizes.length], counter.getCounts(index));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongDimension() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] wrongDimension = {2, 3};
        counter.getCount(wrongDimension);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithOutOfRangeIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(new int[]{sizes[0], sizes[1] + 1, sizes[2]});
    }

    @Test
    public void testGetCountWithValidIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 0;
        Assert.assertEquals(index, counter.getCount(new int[]{0, 0, 0}));
        index++;
        Assert.assertEquals(index, counter.getCount(new int[]{0, 0, 1}));
        index++;
        Assert.assertEquals(index, counter.getCount(new int[]{0, 1, 0}));
        index++;
        Assert.assertEquals(index, counter.getCount(new int[]{1, 0, 0}));
        index += sizes[0] * sizes[1];
        Assert.assertEquals(index, counter.getCount(new int[]{1, 1, 0}));
        index++;
        Assert.assertEquals(index, counter.getCount(new int[]{1, 1, 1}));
    }
}