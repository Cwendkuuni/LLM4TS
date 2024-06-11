import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

    private final int[] size;
    private MultidimensionalCounter counter;

    public MultidimensionalCounterTest(int... size) {
        this.size = size;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return List.of(new Object[]{new int[]{1}}, new Object[]{new int[]{1, 1}}, new Object[]{new int[]{2, 2, 1}});
    }

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(size);
        assertEquals(size.length, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(MathArrays.product(size), counter.getSize());
    }

    @Test
    public void testGetDimension() {
        assertEquals(size.length, counter.getDimension());
    }

    @Test
    public void testGetSizes() {
        assertArrayEquals(size, counter.getSizes());
    }

    @Test
    public void testGetSize() {
        assertEquals(MathArrays.product(size), counter.getSize());
    }

    @Test
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        assertThrows(OutOfRangeException.class, () -> counter.getCounts(-1));
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] counts = counter.getCounts(0);
        assertEquals(size.length, counts.length);
        for (int i = 0; i < size.length; ++i) {
            assertEquals(size[i] - 1, counts[i]);
        }
    }

    @Test
    public void testGetCountOutOfRange() throws OutOfRangeException {
        assertThrows(OutOfRangeException.class, () -> counter.getCount(-1, new int[0]));
        assertThrows(OutOfRangeException.class, () -> counter.getCount(size[0], new int[1]));
        assertThrows(DimensionMismatchException.class, () -> counter.getCount(new int[1], new int[2]));
    }

    @Test
    public void testGetCount() throws OutOfRangeException {
        int count = counter.getCount(new int[0]);
        assertEquals(-1, count);
        count = counter.getCount(new int[1]);
        assertEquals(size[0], count);
        count = counter.getCount(new int[]{size[0] - 1, 0});
        assertEquals(counter.uniCounterOffset[0] * (size[0] - 1) + counter.uniCounterOffset[1] * 0, count);
        count = counter.getCount(new int[]{size[0] - 1, size[1] - 1});
        assertEquals(counter.uniCounterOffset[0] * (size[0] - 1) + counter.uniCounterOffset[1] * (size[1] - 1) + counter.uniCounterOffset[2], count);
    }

    @Test
    public void testIteratorHasNext() {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < MathArrays.product(size); ++i) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        Iterator iterator = counter.iterator();
        int index = -1;
        while (iterator.hasNext()) {
            index = iterator.next();
            assertFalse(iterator.hasPrevious());
        }
        assertEquals(-1, index);
        iterator = counter.iterator();
        int expectedIndex = 0;
        while (iterator.hasNext()) {
            int actualIndex = iterator.next();
            assertEquals(expectedIndex++, actualIndex);
        }
        assertEquals(MathArrays.product(size), expectedIndex);
    }

    @Test
    public void testIteratorGetCount() {
        Iterator iterator = counter.iterator();
        int index = -1;
        while (iterator.hasNext()) {
            index = iterator.next();
            assertEquals(index, iterator.getCount());
        }
        assertEquals(-1, index);
    }

    @Test
    public void testIteratorGetCounts() {
        Iterator iterator = counter.iterator();
        int[] counts = new int[size.length];
        int index = -1;
        while (iterator.hasNext()) {
            index = iterator.next();
            iterator.getCounts(counts);
            assertArrayEquals(new int[]{index}, counts);
        }
        assertEquals(-1, index);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        Iterator iterator = counter.iterator();
        iterator.remove();
    }
}