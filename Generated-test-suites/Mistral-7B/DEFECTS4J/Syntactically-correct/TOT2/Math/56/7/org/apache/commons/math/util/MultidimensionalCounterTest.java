import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        int[] size = {2, 3, 4};
        counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension() {
        int expected = 3;
        int actual = counter.getDimension();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSize() {
        int expected = 2 * 3 * 4;
        int actual = counter.getSize();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCounts_ValidIndex() {
        int[] expected = {0, 0, 0};
        int index = 0;
        int[] actual = counter.getCounts(index);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_InvalidIndex() {
        int index = counter.getSize();
        counter.getCounts(index);
    }

    @Test
    public void testGetCount_ValidIndex() {
        int expected = 0;
        int[] index = {0, 0, 0};
        int actual = counter.getCount(index);
        assertEquals(expected, actual);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_InvalidDimensionIndex() {
        int[] index = {2, 2, 2};
        counter.getCount(index);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_InvalidIndexInDimension() {
        int[] index = {0, 3, 0};
        counter.getCount(index);
    }

    @Test
    public void testIterator_HasNext_ReturnsFalseWhenAllSlotsUsed() {
        Iterator iterator = counter.iterator();

        while (iterator.hasNext()) {
            iterator.next();
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator_HasNext_ReturnsTrueWhenNotAllSlotsUsed() {
        Iterator iterator = counter.iterator();
        int slot = iterator.next().intValue();
        counter.reset();

        assertTrue(iterator.hasNext());
    }

    @Test
    public void testIterator_Next_ReturnsValuesInCorrectOrder() {
        Iterator iterator = counter.iterator();
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        List<Integer> actual = new ArrayList<>();

        while (iterator.hasNext()) {
            actual.add(iterator.next().intValue());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testIterator_Reset_ResetsCounterToInitialState() {
        Iterator iterator = counter.iterator();
        int slot = iterator.next().intValue();

        iterator.reset();

        int expected = 0;
        int actual = iterator.next().intValue();

        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testMultidimensionalCounterWithNegativeSizes() {
        try {
            new MultidimensionalCounter(new int[]{-1, 3, 4});
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}