import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        assertEquals(3, mdCounter.getDimension());
        assertEquals(sizes, mdCounter.getSizes());
        assertEquals(24, mdCounter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        int[] sizes = new int[]{-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterZeroSizes() {
        int[] sizes = new int[]{0, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        assertEquals(0, mdCounter.getCount(0, 0, 0));
        assertEquals(1, mdCounter.getCount(0, 0, 1));
        assertEquals(2, mdCounter.getCount(0, 0, 2));
        assertEquals(3, mdCounter.getCount(0, 1, 0));
        // ...
        assertEquals(23, mdCounter.getCount(1, 3, 2));

        try {
            mdCounter.getCount(3, 0, 0);
            fail();
        } catch (DimensionMismatchException e) {
            // Expected
        }

        try {
            mdCounter.getCount(-1, 0, 0);
            fail();
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            mdCounter.getCount(1, 5, 0);
            fail();
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);

        int[] expected = new int[]{0, 0, 0};
        assertArrayEquals(expected, mdCounter.getCounts(0));

        expected = new int[]{0, 1, 0};
        assertArrayEquals(expected, mdCounter.getCounts(1));

        expected = new int[]{1, 0, 0};
        assertArrayEquals(expected, mdCounter.getCounts(12));

        expected = new int[]{1, 3, 2};
        assertArrayEquals(expected, mdCounter.getCounts(23));

        try {
            mdCounter.getCounts(24);
            fail();
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            mdCounter.getCounts(-1);
            fail();
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        Iterator it = mdCounter.iterator();

        int expectedCount = 0;
        for (int i = 0; i < 24; i++) {
            assertNotNull(it.next());
            assertEquals(i, it.getCount());
            assertEquals(it.getCounts(), mdCounter.getCounts(i));
            expectedCount++;
        }
        assertFalse(it.hasNext());
    }
}