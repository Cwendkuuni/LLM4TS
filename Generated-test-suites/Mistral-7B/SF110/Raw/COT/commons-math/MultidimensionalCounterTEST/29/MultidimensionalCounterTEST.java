package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter counter;

 @Test
 public void testMultidimensionalCounterConstruction() {
        int[] sizes = {2, 3, 4};
        try {
            counter = new MultidimensionalCounter(sizes);
            assertEquals(3, counter.getDimension());
            assertArrayEquals(sizes, counter.getSizes());
            assertEquals(144, counter.getSize());
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw an exception with valid input.");
        }
    }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterNegativeSizes() {
        int[] sizes = {-2, 3, 4};
        try {
            new MultidimensionalCounter(sizes);
            fail("Should have thrown an exception.");
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
    }

 @Test
 public void testMultidimensionalCounterGetIterator() {
        int[] sizes = {2, 3, 4};
        counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        // check thatgetCount method returns the correct value
        assertEquals(2, iterator.getCount());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().intValue());
        assertEquals(3, iterator.getCount());
        assertTrue(iterator.hasNext());
        assertEquals(4, iterator.next().intValue());
        assertEquals(4, iterator.getCount());
        assertTrue(iterator.hasNext());
        assertEquals(5, iterator.next().intValue());
        assertEquals(5, iterator.getCount());
        // check thatgetCounts method returns the correct value
        Integer[] counts = iterator.getCounts();
        assertArrayEquals(new Integer[]{2, 1, 1}, counts);
        assertTrue(iterator.hasNext());
        counts = iterator.getCounts();
        assertArrayEquals(new Integer[]{2, 2, 1}, counts);
        // check thatgetCount method returns the correct value for multi-dimensional indices
        assertEquals(6, counter.getCount(1, 1, 1));
        assertEquals(7, counter.getCount(1, 1, 2));
        assertEquals(8, counter.getCount(1, 2, 1));
        assertEquals(9, counter.getCount(1, 2, 2));
        assertEquals(10, counter.getCount(2, 1, 1));
        assertEquals(11, counter.getCount(2, 1, 2));
        assertEquals(12, counter.getCount(2, 2, 1));
        assertEquals(13, counter.getCount(2, 2, 2));
        assertEquals(14, counter.getCount(3, 1, 1));
        assertEquals(15, counter.getCount(3, 1, 2));
        assertEquals(16, counter.getCount(3, 2, 1));
        assertEquals(17, counter.getCount(3, 2, 2));
    }

 @Test(expected = OutOfRangeException.class)
 public void testMultidimensionalCounterOutOfRangeGetCount() {
        int[] sizes = {2, 3, 4};
        counter = new MultidimensionalCounter(sizes);
        try {
            counter.getCount(-1, 1, 1);
            fail("Should have thrown an exception.");
        } catch (OutOfRangeException e) {
            // expected exception
        }
    }

 @Test(expected = OutOfRangeException.class)
 public void testMultidimensionalCounterOutOfRangeGetCount2() {
        int[] sizes = {2, 3, 4};
        counter = new MultidimensionalCounter(sizes);
        try {
            counter.getCount(1, -1, 1);
            fail("Should have thrown an exception.");
        } catch (OutOfRangeException e) {
            // expected exception
        }
    }

 @Test(expected = OutOfRangeException.class)
 public void testMultidimensionalCounterOutOfRangeGetCount3() {
        int[] sizes = {2, 3, 4};
        counter = new MultidimensionalCounter(sizes);
        try {
            counter.getCount(1, 1, -1);
            fail("Should have thrown an exception.");
        } catch (OutOfRangeException e) {
            // expected exception
        }
    }

 @Test(expected = DimensionMismatchException.class)
 public void testMultidimensionalCounterDimensionMismatchGetCount() {
        int[] sizes = {2, 3, 4};
        counter = new MultidimensionalCounter(sizes);
        try {
            counter.getCount(1, 4, 1);
            fail("Should have thrown an exception.");
        } catch (DimensionMismatchException e) {
            // expected exception
        }
    }

 @Test
 public void testMultidimensionalCounterToString() {
        int[] sizes = {2, 3, 4};
        counter = new MultidimensionalCounter(sizes);
        assertEquals("[0][0][0]", counter.toString());
        counter.next();
        counter.next();
        counter.next();
        assertEquals("[0][0][1]", counter.toString());
        Integer[] counts = counter.iterator().getCounts();
        for (int i = 0; i < counter.getDimension(); i++) {
            assertEquals(counts[i].intValue() + 1, counter.getCount(i));
        }
    }

}