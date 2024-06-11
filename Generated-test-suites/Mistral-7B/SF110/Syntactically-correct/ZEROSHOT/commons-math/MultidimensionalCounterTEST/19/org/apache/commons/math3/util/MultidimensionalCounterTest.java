package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import it.unimi.dsi.fastutil.ints.IntArrays;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

    private final int[] size;
    private MultidimensionalCounter counter;

    public MultidimensionalCounterTest(int... size) {
        this.size = size;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            new int[] { 3, 3, 1 },
            new int[] { 2, 2, 2 },
            new int[] { 1, 1, 1, 1 },
            new int[] { 3, 1, 1 }
        });
    }

    @Test
    public void testConstruction() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(size);
        assertEquals(size.length, counter.getDimension());
        assertEquals(Arrays.toString(size), Arrays.toString(counter.getSizes()));
        assertEquals(counter.getSize(), Arrays.stream(size).parallel().mapToInt(i -> 1).sum());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizeException() {
        new MultidimensionalCounter(new int[] { -1, 1, 1 });
    }

    @Test
    public void testIterator() {
        counter = new MultidimensionalCounter(size);
        final MultidimensionalCounter.Iterator iter = counter.iterator();
        assertTrue(iter.hasNext());
        assertEquals(0, iter.next().intValue());
        assertTrue(iter.hasNext());
        final int last = iter.next().intValue();
        assertTrue(iter.hasNext());
        assertEquals(last + 1, iter.next().intValue());
        assertFalse(iter.hasNext());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetIndexOutOfRange() throws OutOfRangeException {
        counter = new MultidimensionalCounter(size);
        counter.getCounts(-1);
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        counter = new MultidimensionalCounter(size);
        final int[] actual = counter.getCounts(0);
        assertArrayEquals(IntArrays.zeros(size.length), actual);
        final int[] expected = new int[size.length];
        Arrays.fill(expected, size[0] - 1);
        counter.next();
        assertArrayEquals(expected, counter.getCounts());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsDimensionMismatch() throws DimensionMismatchException, OutOfRangeException {
        counter = new MultidimensionalCounter(new int[] { 3, 3, 1 });
        counter.getCounts(new int[] { 3, 3, 2 });
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetIndexOutOfRange2() throws OutOfRangeException {
        counter = new MultidimensionalCounter(size);
        counter.next();
        counter.next();
        counter.getCounts(counter.getSize());
    }

    @Test
    public void testGetCount() throws OutOfRangeException {
        counter = new MultidimensionalCounter(size);
        final int count = counter.getCount(new int[] { 0, 0 });
        assertEquals(0, count);
        counter.next();
        final int count2 = counter.getCount(new int[] { 0, 0 });
        assertEquals(1, count2);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() throws DimensionMismatchException {
        counter = new MultidimensionalCounter(new int[] { 3, 3, 1 });
        counter.getCount(new int[] { 3, 2, 1 });
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws OutOfRangeException {
        counter = new MultidimensionalCounter(new int[] { 3, 3, 1 });
        counter.next();
        counter.next();
        counter.next();
        counter.getCount(new int[] { 4, 4, 1 });
    }

    @Test
    public void testToString() {
        counter = new MultidimensionalCounter(new int[] { 3, 3, 1 });
        final String actual = counter.toString();
        final String expected = "[" + (size[0] - 1) + "][" + (size[1] - 1) + "][" + (size[2] - 1) + "]";
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSize() {
        counter = new MultidimensionalCounter(size);
        assertEquals(Arrays.stream(size).parallel().mapToInt(i -> 1).sum(), counter.getSize());
    }
}