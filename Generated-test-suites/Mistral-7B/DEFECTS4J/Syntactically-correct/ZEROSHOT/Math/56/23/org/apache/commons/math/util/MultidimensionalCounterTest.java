import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertEquals(size, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterWithNegativeSize() {
        int[] size = new int[]{-1, 4, 3};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterWithZeroSize() {
        int[] size = new int[]{2, 4, 0};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testIterator() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Integer index = iterator.next();
            assertEquals(index, count);
            int[] counts = iterator.getCounts();
            assertArrayEquals(new int[]{count % 2, count % 4, count % 3}, counts);
            count++;
        }
        assertFalse(iterator.hasNext());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(-1, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeLastDimension() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(1, 5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongNumberOfArguments() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount();
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongNumberOfArgumentsTwoDimensions() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(1);
    }

    @Test
    public void testGetCount() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int index = counter.getCount(0, 0);
        assertEquals(index, 0);
        int[] counts = counter.getCounts(0, 0);
        assertArrayEquals(new int[]{0, 0, 0}, counts);

        index = counter.getCount(0, 1);
        assertEquals(index, 1);
        counts = counter.getCounts(0, 1);
        assertArrayEquals(new int[]{0, 0, 1}, counts);

        index = counter.getCount(1, 0);
        assertEquals(index, 3);
        counts = counter.getCounts(1, 0);
        assertArrayEquals(new int[]{0, 3, 0}, counts);

        index = counter.getCount(1, 3);
        assertEquals(index, 23);
        counts = counter.getCounts(1, 3);
        assertArrayEquals(new int[]{0, 3, 2}, counts);
    }
}