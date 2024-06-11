import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(sizes, mdc.getSizes());
        assertEquals(24, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] sizes = new int[]{-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSizes() {
        int[] sizes = new int[]{0, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        int[] counts;

        counts = mdc.getCounts(0);
        assertArrayEquals(new int[]{0, 0, 0}, counts);
        assertEquals(0, mdc.getCount(counts));

        counts = mdc.getCounts(23);
        assertArrayEquals(new int[]{1, 3, 2}, counts);
        assertEquals(23, mdc.getCount(counts));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCounts(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsDimensionMismatch() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        mdc.getCounts(new int[]{1, 3, 4});
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator it = mdc.iterator();

        for (int i = 0; i < 24; i++) {
            assertTrue(it.hasNext());
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator it = mdc.iterator();

        int index = 0;
        while (it.hasNext()) {
            int count = it.next();
            assertEquals(index, count);
            index++;
        }
    }

    @Test
    public void testIteratorGetCount() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator it = mdc.iterator();

        int index = 0;
        while (it.hasNext()) {
            int[] counts = it.getCounts();
            int count = it.getCount();
            assertEquals(index, count);
            assertArrayEquals(new int[]{index / (sizes[1] * sizes[2]), index % (sizes[1] * sizes[2]) / sizes[1], index % sizes[1]}, counts);
            index++;
        }
    }

    @Test
    public void testIteratorGetCounts() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator it = mdc.iterator();

        int index = 0;
        while (it.hasNext()) {
            int[] counts = it.getCounts();
            assertArrayEquals(new int[]{index / (sizes[1] * sizes[2]), index % (sizes[1] * sizes[2]) / sizes[1], index % sizes[1]}, counts);
            index++;
        }
    }

    @Test
    public void testToString() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator it = mdc.iterator();

        int index = 0;
        while (it.hasNext()) {
            int[] counts = it.getCounts();
            String str = mdc.toString();
            String expected = "[";
            for (int i = 0; i < counts.length; i++) {
                expected += String.valueOf(counts[i]) + "]";
                if (i < counts.length - 1) {
                    expected += ", ";
                }
            }
            assertEquals(expected, str);
            index++;
        }
    }
}