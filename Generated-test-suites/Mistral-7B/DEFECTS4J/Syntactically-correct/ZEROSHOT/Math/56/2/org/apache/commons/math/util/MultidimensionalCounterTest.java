import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertEquals(2 * 4 * 3, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() {
        int[] size = {-1, 4, 3};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSize() {
        int[] size = {0, 4, 3};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetCount() throws OutOfRangeException {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(0, mdc.getCount());
        for (int i = 0; i < mdc.getSize(); i++) {
            int count = mdc.getCount();
            int[] counts = mdc.getCounts(count);
            assertArrayEquals(new int[]{i / (mdc.getSize() / mdc.getDimension()),
                    i % (mdc.getSize() / mdc.getDimension())}, counts);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithWrongNumberOfArguments() throws OutOfRangeException {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(5);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws OutOfRangeException {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(mdc.getSize() + 1);
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        for (int i = 0; i < mdc.getSize(); i++) {
            int[] counts = mdc.getCounts(i);
            assertArrayEquals(new int[]{i / (mdc.getSize() / mdc.getDimension()),
                    i % (mdc.getSize() / mdc.getDimension())}, counts);
        }
    }

    @Test
    public void testIterator() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator it = mdc.iterator();
        int count = 0;
        while (it.hasNext()) {
            int multidimensionalCount = it.next();
            int unidimensionalCount = it.getCount();
            assertEquals(multidimensionalCount, unidimensionalCount);
            int[] counts = it.getCounts();
            assertArrayEquals(new int[]{count / (mdc.getSize() / mdc.getDimension()),
                    count % (mdc.getSize() / mdc.getDimension())}, counts);
            count++;
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testSize() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(2 * 4 * 3, mdc.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertArrayEquals(new int[]{2, 4, 3}, mdc.getSizes());
    }

    @Test
    public void testToString() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        String toString = mdc.toString();
        String expected = "[" + 0 + "][" + 0 + "][" + 0 + "]";
        for (int i = 1; i < mdc.getSize(); i++) {
            toString = toString.substring(0, toString.lastIndexOf("]")) + "[" + i / (mdc.getSize() / mdc.getDimension()) + "][" + i % (mdc.getSize() / mdc.getDimension()) + "]";
            expected += "[" + i / (mdc.getSize() / mdc.getDimension()) + "][" + i % (mdc.getSize() / mdc.getDimension()) + "]";
        }
        assertEquals(expected, toString);
    }
}