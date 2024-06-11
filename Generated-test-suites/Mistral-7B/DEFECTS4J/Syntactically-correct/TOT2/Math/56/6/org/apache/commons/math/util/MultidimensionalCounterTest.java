import org.apache.commons.math.util.MultidimensionalCounter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for MultidimensionalCounter.
 */
public class MultidimensionalCounterTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Assert.assertEquals(sizes.length, counter.getDimension());
        Assert.assertArrayEquals(sizes, counter.getSizes());
        Assert.assertEquals(MathUtils.multiply(sizes[0], sizes[1], sizes[2]), counter.getSize());
    }

    /**
     * Test getCounts.
     */
    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            int[] counts = counter.getCounts(i);
            int index = counter.getCount(counts);
            Assert.assertArrayEquals(counts, counter.getCounts(index));
        }
    }

    /**
     * Test getCount.
     */
    @Test
    public void testGetCount() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < sizes[0]; i++) {
            for (int j = 0; j < sizes[1]; j++) {
                for (int k = 0; k < sizes[2]; k++) {
                    int index = counter.getCount(i, j, k);
                    int[] counts = counter.getCounts(index);
                    Assert.assertEquals(i, counts[0]);
                    Assert.assertEquals(j, counts[1]);
                    Assert.assertEquals(k, counts[2]);
                }
            }
        }
    }

    /**
     * Test iterator.
     */
    @Test
    public void testIterator() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            int index = iterator.next();
            int[] counts = iterator.getCounts();
            int unidimensionalIndex = counter.getCount(counts);
            int[] expectedCounts = counter.getCounts(unidimensionalIndex);
            Assert.assertArrayEquals(counts, expectedCounts);
            count++;
        }

        Assert.assertEquals(sizes[0] * sizes[1] * sizes[2], count);
    }

    /**
     * Test exception handling.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeException() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = sizes[0] * sizes[1] * sizes[2] + 1;
        counter.getCounts(index);
    }

    /**
     * Test exception handling.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] wrongSizes = {2, 4, 3};
        counter.getSizes(wrongSizes);
    }

    /**
     * Test null pointer exception.
     */
    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        MultidimensionalCounter counter = null;
        counter.getDimension();
    }
}