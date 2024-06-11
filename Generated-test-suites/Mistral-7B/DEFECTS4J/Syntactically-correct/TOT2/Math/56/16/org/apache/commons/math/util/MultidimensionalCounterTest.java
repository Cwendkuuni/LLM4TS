import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(sizes, counter.getSizes());
        assertEquals(24 * 4 * 3, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        int[] sizes = {-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int[] expected = {0, 0, 0};
        int[] actual = iterator.getCounts();
        assertArrayEquals(expected, actual);
        iterator.next();
        expected = new int[]{0, 0, 1};
        assertArrayEquals(expected, iterator.getCounts());
        // test all possible multidimensional index values
        for (int i = 0; i < counter.getSize(); i++) {
            expected = counter.getCounts(i);
            assertArrayEquals(expected, iterator.getCounts());
            iterator.next();
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetCountNegativeIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetCountOutOfBoundsIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(counter.getSize());
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        for (int i = 0; i < counter.getSize(); i++) {
            int index = counter.getCount();
            int[] expected = counter.getCounts(index);
            int[] actual = counter.getCounts();
            assertArrayEquals(expected, actual);
            counter.next();
        }
        Assert.assertFalse(counter.hasNext());
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        int expected = 0;
        while (iterator.hasNext()) {
            int count = iterator.next();
            String actual = counter.toString();
            String expectedString = "[";
            for (int i = 0; i < counter.getDimension(); i++) {
                expectedString += "[" + expected++ + "]";
                if (i < counter.getDimension() - 1) {
                    expectedString += ", ";
                }
            }
            expectedString += "]";
            assertEquals(expectedString, actual);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithInvalidCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(2, 1, 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithOutOfBoundsCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(0, 5, 3);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWithOutOfBoundsCountLastDimension() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(0, 0, 5);
    }
}