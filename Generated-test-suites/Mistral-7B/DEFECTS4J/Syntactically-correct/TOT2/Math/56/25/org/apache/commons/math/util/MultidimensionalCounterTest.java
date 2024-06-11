import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.util.MathUtils;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorWithValidSizes() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(sizes.length, counter.getDimension());
        assertEquals(MathUtils.multiply(MathUtils.copyOf(sizes)), counter.getSizes());
        assertEquals(MathUtils.multiply(MathUtils.copyOf(sizes)), counter.getSizes());
        assertEquals(23, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSizes() {
        int[] sizes = {-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSize() {
        int[] sizes = {2, 0, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        for (int i = 0; i < 23; i++) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        for (int i = 0; i < 23; i++) {
            Integer next = iterator.next();
            assertEquals(i, next.intValue());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        for (int i = 0; i < 23; i++) {
            int count = counter.getCount(i / sizes[1], i % sizes[1], i % sizes[2]);
            assertEquals(i, count);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBounds() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1, 4, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionalMismatch() {
        int[] sizes = {2, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4});
        counter.getCount(new int[]{2, 4});
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        for (int i = 0; i < 23; i++) {
            String expected = "[";
            for (int j = 0; j < sizes.length; j++) {
                expected += i / MathUtils.multiply(MathUtils.copyOf(sizes.slice(0, j))) + "]";
                if (j < sizes.length - 1) {
                    expected += ", ";
                }
            }
            expected += "]";
            assertEquals(expected, counter.toString());
            counter.next();
        }
    }
}