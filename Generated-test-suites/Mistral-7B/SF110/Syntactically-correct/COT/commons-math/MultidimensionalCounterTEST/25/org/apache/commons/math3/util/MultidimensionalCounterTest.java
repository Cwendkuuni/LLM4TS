package org.apache.commons.math3.util;

import static org.junit.Assert.ArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

    private final int[] size;
    private MultidimensionalCounter counter;

    public MultidimensionalCounterTest(int... size) {
        this.size = size;
    }

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][] {
                new int[] { 3, 4, 5 },
                new int[] { 1, 2, 2 },
                new int[] { 5, 5, 5 },
                new int[] { 0 }
        });
    }

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(size);
        assertEquals(size.length, counter.getDimension());
        ArrayEquals(size, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizeThrowsException() {
        new MultidimensionalCounter(new int[] { -1, 2, 3 });
    }

    @Test
    public void testGetDimension() {
        assertEquals(size.length, counter.getDimension());
    }

    @Test
    public void testGetSizes() {
        ArrayEquals(size, counter.getSizes());
    }

    @Test
    public void testGetTotalSize() {
        int totalSize = 1;
        for (int i = 0; i < size.length; ++i) {
            totalSize *= size[i];
        }
        assertEquals(totalSize, counter.getSize());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetIndexOutOfRangeThrowsException() {
        int index = size[0] * size[1];
        counter.getCounts(index);
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] index = counter.getCounts(0);
        for (int i = 0; i < size.length; ++i) {
            assertEquals(0, index[i]);
        }
        for (int i = size.length - 1; i >= 0; --i) {
            assertEquals(size[i] - 1, index[i]);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongDimensionThrowsException() {
        int[] wrongSize = new int[3];
        for (int i = 0; i < wrongSize.length; ++i) {
            wrongSize[i] = size[i] + 1;
        }
        counter.getCounts(counter.getSize() + 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithNegativeIndexThrowsException() {
        int[] index = new int[size.length];
        index[size.length - 1] = -1;
        counter.getCount(index);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongDimensionThrowsException2() {
        int[] wrongIndex = new int[size.length + 1];
        counter.getCount(wrongIndex);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int[] index = new int[size.length];
        for (int i = 0; i < size.length; ++i) {
            index[i] = Precision.floor(Math.random() * (size[i] + 1));
        }
        int expected = 0;
        for (int i = 0; i < size.length; ++i) {
            expected += index[i] * MathArrays.prod(Arrays.copyOfRange(size, 0, i));
        }
        int actual = counter.getCount(index);
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < size.length; ++i) {
            expected.append("[");
            expected.append(Arrays.toString(counter.getSizes()));
            expected.append("]");
        }
        StringBuilder actual = new StringBuilder();
        actual.append(counter.toString());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testIteratorHasNext() {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[size.length - 1] * MathArrays.prod(size); ++i) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        Iterator iterator = counter.iterator();
        int count = -1;
        while (iterator.hasNext()) {
            int next = iterator.next();
            assertEquals(next, ++count);
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCount() {
        Iterator iterator = counter.iterator();
        int count = -1;
        while (iterator.hasNext()) {
            int next = iterator.next();
            assertEquals(next, ++count);
            assertEquals(count, iterator.getCount());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCounts() {
        Iterator iterator = counter.iterator();
        int[] counts = new int[size.length];
        int index = 0;
        while (iterator.hasNext()) {
            iterator.next();
            counts = iterator.getCounts();
            ArrayEquals(new int[] { index++ }, counts);
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCountWithWrongDimensionThrowsException() {
        Iterator iterator = counter.iterator();
        int[] wrongCount = new int[size.length + 1];
        iterator.getCount(wrongCount);
    }
}