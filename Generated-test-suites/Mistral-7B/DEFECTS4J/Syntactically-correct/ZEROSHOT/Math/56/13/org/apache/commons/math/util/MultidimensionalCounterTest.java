import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertEquals(2 * 4 * 3, mdc.getSize());
        assertArrayEquals(new int[]{2, 4, 3}, mdc.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSize() {
        int[] size = {-1, 2, 3};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterZeroSize() {
        int[] size = {0, 2, 3};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        for (int i = 0; i < 2 * 4 * 3; i++) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        int expectedCount = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            assertEquals(expectedCount++, next);
        }
    }

    @Test
    public void testIteratorGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        int expectedCount = 0;
        while (iterator.hasNext()) {
            int count = iterator.getCount();
            assertEquals(expectedCount++, count);
        }
    }

    @Test
    public void testIteratorGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();
        int[] expectedCounts = new int[3];
        while (iterator.hasNext()) {
            int[] counts = iterator.getCounts();
            assertArrayEquals(expectedCounts, counts);
            expectedCounts[0]++;
            expectedCounts[1]++;
            expectedCounts[2]++;
        }
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        for (int i = 0; i < 2 * 4 * 3; i++) {
            int[] counts = mdc.getCounts(i);
            assertArrayEquals(new int[]{i / (2 * 4) % 2, i / (2 * 4) / 2 % 4, i % 3}, counts);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(2 * 4 * 3);
    }
}