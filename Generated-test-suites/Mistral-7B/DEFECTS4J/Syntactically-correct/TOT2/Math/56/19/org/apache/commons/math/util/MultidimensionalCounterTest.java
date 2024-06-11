import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math.util.MultidimensionalCounter;

public class MultidimensionalCounterTest1 {
    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(sizes.length, counter.getDimension());
        assertEquals(2 * 4 * 3, counter.getSize());
    }

    @Test
    public void testMultidimensionalCounterIteratorBasicUse() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int expectedCount = -1;
        while (iterator.hasNext()) {
            int actualCount = iterator.next();
            assertEquals(expectedCount + 1, actualCount);
            expectedCount = actualCount;
        }
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultidimensionalCounterOutOfRangeGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultidimensionalCounterOutOfRangeGetCountLastDimension() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(0, sizes[0] + 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultidimensionalCounterOutOfRangeGetCountInnerDimension() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(1, sizes[1]);
        counter.getCount(1, sizes[1] + 1);
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] sizes = {2, 4, 3};
        int[] expectedCounts = {0, 0, 0};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int actualCount = 0;
        int[] actualCounts = new int[counter.getDimension()];
        while (iterator.hasNext()) {
            actualCounts = iterator.getCounts();
            for (int i = 0; i < counter.getDimension(); i++) {
                assertEquals(expectedCounts[i], actualCounts[i]);
            }
            expectedCounts = actualCounts;
            actualCount++;
        }
    }

    @Test
    public void testMultidimensionalCounterGetCountsInvalidSize() {
        int[] sizes = {2, 4, 3};
        int[] expectedCounts = {0, 0, 0};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] actualCounts = counter.getCounts(counter.getDimension() + 1);
        assertArrayEquals(expectedCounts, actualCounts);
    }

    @Test
    public void testMultidimensionalCounterGetCountLastDimensionInvalidIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int actualCount = counter.getCount(sizes[0], sizes[0]);
        assertEquals(0, actualCount);
    }

    @Test
    public void testMultidimensionalCounterGetCountInnerDimensionInvalidIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int actualCount = counter.getCount(0, sizes[1] + 1);
        assertEquals(0, actualCount);
    }
}