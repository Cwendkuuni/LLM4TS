package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

    private final int[] size;
    private MultidimensionalCounter counter;

    public MultidimensionalCounterTest(int... size) {
        this.size = size;
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][] {
                new int[] { 1 },
                new int[] { 1, 1 },
                new int[] { 2, 3 },
                new int[] { 3, 2, 1 }
        });
    }

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(size);
        assertEquals("Incorrect dimension.", size.length, counter.getDimension());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidSize() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(new int[] { -1 });
    }

    @Test
    public void testGetDimension() {
        assertEquals("Incorrect dimension.", size.length, counter.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        int index = counter.totalSize;
        counter.getCounts(index);
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] indices = counter.getCounts(0);
        int[] expected = new int[size.length];
        Arrays.fill(expected, 0);
        Arrays.equals("Unexpected counts.", expected, indices);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int dimension = size.length;
        int[] coordinates = new int[dimension];
        int count = 0;
        for (int i = 0; i < dimension; ++i) {
            coordinates[i] = i;
            count += counter.uniCounterOffset[i] * size[i];
        }
        int expected = count + size[dimension - 1];
        assertEquals("Unexpected count.", expected, counter.getCount(coordinates));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws OutOfRangeException, DimensionMismatchException {
        int[] coordinates = new int[size.length];
        for (int i = 0; i < size.length; ++i) {
            coordinates[i] = size[i] + 1;
        }
        counter.getCount(coordinates);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidCoordinates() throws OutOfRangeException, DimensionMismatchException {
        int[] coordinates = new int[size.length - 1];
        counter.getCount(coordinates);
    }

    @Test
    public void testGetSize() {
        int totalSize = 1;
        for (int i = 0; i < size.length; ++i) {
            totalSize *= size[i];
        }
        assertEquals("Unexpected total size.", totalSize, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] expected = MathArrays.copyOf(size);
        assertArrayEquals("Unexpected sizes.", expected, counter.getSizes());
    }

    @Test
    public void testToString() {
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < size.length; ++i) {
            expected.append("[").append(size[i]).append("]");
        }
        assertEquals("Unexpected string representation.", expected.toString(), counter.toString());
    }

    @Test
    public void testIteratorHasNext() {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[size.length - 1]; ++i) {
            assertTrue("Iterator should have next value.", iterator.hasNext());
        }
        assertFalse("Iterator should not have next value.", iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[size.length - 1]; ++i) {
            int next = iterator.next();
            assertEquals("Unexpected next value.", (long) i + 1, (long) next);
        }
        assertNull("Next should return null.", iterator.next());
    }

    @Test
    public void testIteratorGetCount() {
        Iterator iterator = counter.iterator();
        assertEquals("Unexpected initial count.", -1, iterator.getCount());
        for (int i = 0; i < size[size.length - 1]; ++i) {
            int next = iterator.next();
            assertEquals("Unexpected next count.", (long) i + 1, (long) next);
            int count = iterator.getCount();
            assertEquals("Unexpected count.", (long) i + 2, (long) count);
        }
        assertNull("GetCount should return null.", iterator.getCount());
    }

    @Test
    public void testIteratorGetCounts() {
        Iterator iterator = counter.iterator();
        int[] counts = iterator.getCounts();
        int[] expected = new int[size.length];
        Arrays.fill(expected, 0);
        Arrays.equals("Unexpected initial counts.", expected, counts);
        for (int i = 0; i < size[size.length - 1]; ++i) {
            int next = iterator.next();
            int[] counts2 = iterator.getCounts();
            Arrays.equals("Unexpected updated counts.", Arrays.copyOfRange(new int[] { i + 1 }, 0, size), counts2);
        }
        int[] counts3 = iterator.getCounts();
        assertArrayEquals("Unexpected final counts.", new int[] { size[size.length - 1] }, counts3);
    }

    @Test
    public void testIteratorGetCountAtLastDimension() {
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[size.length - 1]; ++i) {
            int next = iterator.next();
            int count = iterator.getCount(size.length - 1);
            int expected = (long) i + 2;
            assertEquals("Unexpected count at last dimension.", expected, count);
        }
        int count = iterator.getCount(size.length - 1);
        assertEquals("Unexpected count at last dimension.", size[size.length - 1], count);
    }
}