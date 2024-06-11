package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link MultidimensionalCounter}.
 */
public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        int[] size = {2, 3, 5};
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetTotalSize() {
        assertEquals(30, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        assertArrayEquals(new int[] {2, 3, 5}, counter.getSizes());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        counter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndex() {
        counter.getCounts(32);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongDimension() {
        counter.getCount(new int[] {4, 3, 5});
    }

    @Test
    public void testGetCount() {
        int[] c = new int[] {0, 1, 2};
        int count = counter.getCount(c);
        assertEquals(7, count);
    }

    @Test
    public void testGetCountAtLastDimension() {
        int count = counter.getCount(new int[] {0, 1, 5});
        assertEquals(5, count);
    }

    @Test
    public void testGetCountAtFirstDimension() {
        int count = counter.getCount(new int[] {2, 1, 5});
        assertEquals(3, count);
    }

    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter.Iterator it = counter.iterator();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        // ...
        for (int i = 0; i < counter.getSize(); ++i) {
            it.next();
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testIteratorNext() {
        MultidimensionalCounter.Iterator it = counter.iterator();
        int expected = 0;
        for (int i = 0; i < counter.getSize(); ++i) {
            int actual = it.next();
            assertEquals(expected++, actual);
        }
    }

    @Test
    public void testIteratorGetCount() {
        MultidimensionalCounter.Iterator it = counter.iterator();
        for (int i = 0; i < counter.getSize(); ++i) {
            it.next();
        }
        assertEquals(-1, it.getCount());
        it = counter.iterator();
        int count = it.getCount();
        assertEquals(0, count);
        it.next();
        count = it.getCount();
        assertEquals(1, count);
        // ...
    }

    @Test
    public void testIteratorGetCounts() {
        MultidimensionalCounter.Iterator it = counter.iterator();
        int[] expected = new int[counter.getDimension()];
        int[] actual;
        for (int i = 0; i < counter.getSize(); ++i) {
            it.next();
            actual = it.getCounts();
            Arrays.sort(expected);
            Arrays.sort(actual);
            assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void testToString() {
        List<Object> expected = new ArrayList<>();
        for (int i = 0; i < counter.getSize(); ++i) {
            MultidimensionalCounter.Iterator it = counter.iterator();
            int count = it.next();
            String str = it.toString();
            expected.add(String.format("[%d]", count));
            it.remove();
        }
        String actual = counter.toString();
        List<Object> actualList = Arrays.asList(actual.split("]["));
        Collections.sort(expected);
        Collections.sort(actualList, (o1, o2) -> o1.toString().compareTo(o2.toString()));
        assertEquals(expected, actualList);
    }
}