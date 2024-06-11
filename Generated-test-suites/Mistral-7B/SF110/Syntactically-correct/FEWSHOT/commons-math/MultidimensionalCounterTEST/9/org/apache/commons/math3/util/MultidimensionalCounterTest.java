package org.apache.commons.math3.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorPositive() throws Exception {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        assertEquals(3, multidimensionalCounter.getDimension());
        assertArrayEquals(new int[]{2, 3, 4}, multidimensionalCounter.getSizes());
        assertEquals(24, multidimensionalCounter.getSize());
    }

    @Test
    public void testConstructorNegativeDimensions() {
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(new int[]{-1, 3, 4}));
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(new int[]{2, -1, 4}));
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(new int[]{2, 3, -1}));
    }

    @Test
    public void testConstructorZeroDimension() {
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(new int[0]);
        assertEquals(0, multidimensionalCounter.getDimension());
        assertEquals(Collections.emptyList(), multidimensionalCounter.getSizes());
        assertEquals(0, multidimensionalCounter.getSize());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        assertArrayEquals(new int[]{0, 0, 2}, multidimensionalCounter.getCounts(0));
        assertArrayEquals(new int[]{0, 1, 2}, multidimensionalCounter.getCounts(1));
        assertArrayEquals(new int[]{1, 1, 1}, multidimensionalCounter.getCounts(2));
        assertArrayEquals(new int[]{2, 2, 1}, multidimensionalCounter.getCounts(3));
        assertArrayEquals(new int[]{2, 2, 2}, multidimensionalCounter.getCounts(4));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(5));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(6));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(7));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(8));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(9));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(10));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(11));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(12));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(13));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(14));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(15));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(16));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(17));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(18));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(19));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(20));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(21));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(22));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(23));
        assertArrayEquals(new int[]{2, 3, 3}, multidimensionalCounter.getCounts(24));
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        assertEquals(0, multidimensionalCounter.getCount(new int[0]));
        assertEquals(0, multidimensionalCounter.getCount(new int[]{0}));
        assertEquals(0, multidimensionalCounter.getCount(new int[]{0, 0}));
        assertEquals(0, multidimensionalCounter.getCount(new int[]{0, 0, 0}));
        assertEquals(0, multidimensionalCounter.getCount(new int[3]));
        assertEquals(0, multidimensionalCounter.getCount(new int[]{2}));
        assertEquals(0, multidimensionalCounter.getCount(new int[]{2, 0}));
        assertEquals(0, multidimensionalCounter.getCount(new int[]{2, 0, 0}));
        assertEquals(0, multidimensionalCounter.getCount(new int[]{2, 2}));
        assertEquals(2, multidimensionalCounter.getCount(new int[]{1, 1, 1}));
        assertEquals(4, multidimensionalCounter.getCount(new int[]{1, 1, 2}));
        assertEquals(6, multidimensionalCounter.getCount(new int[]{1, 2, 1}));
        assertEquals(8, multidimensionalCounter.getCount(new int[]{1, 2, 2}));
        assertEquals(12, multidimensionalCounter.getCount(new int[]{2, 1, 1}));
        assertEquals(12, multidimensionalCounter.getCount(new int[]{2, 1, 2}));
        assertEquals(16, multidimensionalCounter.getCount(new int[]{2, 2, 1}));
        assertEquals(24, multidimensionalCounter.getCount(new int[]{2, 2, 2}));
    }

    @Test
    public void testIterator() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        Iterator iterator = multidimensionalCounter.iterator();
        assertEquals(-1, iterator.getCount());
        assertEquals(new int[]{0, 0, 0}, iterator.getCounts());
        assertEquals(0, iterator.next());
        assertEquals(new int[]{1, 0, 0}, iterator.getCounts());
        assertEquals(1, iterator.next());
        assertEquals(new int[]{1, 1, 0}, iterator.getCounts());
        assertEquals(2, iterator.next());
        assertEquals(new int[]{1, 1, 1}, iterator.getCounts());
        assertEquals(3, iterator.next());
        assertEquals(new int[]{1, 2, 0}, iterator.getCounts());
        assertEquals(4, iterator.next());
        assertEquals(new int[]{1, 2, 1}, iterator.getCounts());
        assertEquals(5, iterator.next());
        assertEquals(new int[]{1, 3, 0}, iterator.getCounts());
        assertEquals(6, iterator.next());
        assertEquals(new int[]{1, 3, 1}, iterator.getCounts());
        assertEquals(7, iterator.next());
        assertEquals(new int[]{2, 0, 0}, iterator.getCounts());
        assertEquals(8, iterator.next());
        assertEquals(new int[]{2, 1, 0}, iterator.getCounts());
        assertEquals(9, iterator.next());
        assertEquals(new int[]{2, 1, 1}, iterator.getCounts());
        assertEquals(10, iterator.next());
        assertEquals(new int[]{2, 2, 0}, iterator.getCounts());
        assertEquals(11, iterator.next());
        assertEquals(new int[]{2, 2, 1}, iterator.getCounts());
        assertEquals(12, iterator.next());
        assertEquals(new int[]{2, 3, 0}, iterator.getCounts());
        assertEquals(13, iterator.next());
        assertEquals(new int[]{2, 3, 1}, iterator.getCounts());
        assertEquals(14, iterator.next());
        assertEquals(new int[]{2, 3, 2}, iterator.getCounts());
        assertEquals(15, iterator.next());
        assertEquals(new int[]{1, 0, 1}, iterator.getCounts());
        assertEquals(16, iterator.next());
        assertEquals(new int[]{1, 1, 2}, iterator.getCounts());
        assertEquals(17, iterator.next());
        assertEquals(new int[]{1, 2, 1}, iterator.getCounts());
        assertEquals(18, iterator.next());
        assertEquals(new int[]{2, 0, 1}, iterator.getCounts());
        assertEquals(19, iterator.next());
        assertEquals(new int[]{2, 1, 2}, iterator.getCounts());
        assertEquals(20, iterator.next());
        assertEquals(new int[]{2, 2, 2}, iterator.getCounts());
        assertEquals(21, iterator.next());
        assertEquals(new int[]{1, 2, 2}, iterator.getCounts());
        assertEquals(22, iterator.next());
        assertEquals(new int[]{2, 2, 3}, iterator.getCounts());
        assertEquals(23, iterator.next());
        assertEquals(new int[]{1, 2, 3}, iterator.getCounts());
        assertEquals(24, iterator.next());
        assertNull(iterator.next());
    }

    @Test
    public void testToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        String toString = multidimensionalCounter.toString();
        assertEquals("[0][0][0]", toString.substring(0, 15));
        assertEquals("[1][0][0]", toString.substring(15, 30));
        assertEquals("[1][1][0]", toString.substring(30, 45));
        assertEquals("[1][1][1]", toString.substring(45, 60));
        assertEquals("[1][2][0]", toString.substring(60, 75));
        assertEquals("[1][2][1]", toString.substring(75, 90));
        assertEquals("[1][3][0]", toString.substring(90, 105));
        assertEquals("[1][3][1]", toString.substring(105, 120));
        assertEquals("[1][3][2]", toString.substring(120, 135));
        assertEquals("[2][0][0]", toString.substring(135, 150));
        assertEquals("[2][1][0]", toString.substring(150, 165));
        assertEquals("[2][1][1]", toString.substring(165, 180));
        assertEquals("[2][2][0]", toString.substring(180, 195));
        assertEquals("[2][2][1]", toString.substring(195, 210));
        assertEquals("[2][3][0]", toString.substring(210, 225));
        assertEquals("[2][3][1]", toString.substring(225, 240));
        assertEquals("[2][3][2]", toString.substring(240, 255));
        assertEquals("[1][0][1]", toString.substring(255, 270));
        assertEquals("[1][1][2]", toString.substring(270, 285));
        assertEquals("[1][2][1]", toString.substring(285, 300));
        assertEquals("[2][0][1]", toString.substring(300, 315));
        assertEquals("[2][1][2]", toString.substring(315, 330));
        assertEquals("[2][2][1]", toString.substring(330, 345));
        assertEquals("[2][2][2]", toString.substring(345, 360));
        assertEquals("[1][2][2]", toString.substring(360, 375));
        assertEquals("[2][2][3]", toString.substring(375, 400));
        assertEquals("[1][2][3]", toString.substring(400, 415));
        assertEquals("[2][3][1]", toString.substring(415, 430));
        assertEquals("[2][3][2]", toString.substring(430, 445));
        assertEquals("[2][3][3]", toString.substring(445, 460));
    }

    @Test
    public void testOutOfRangeException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        assertThrows(OutOfRangeException.class, () -> multidimensionalCounter.getCounts(-1));
        assertThrows(OutOfRangeException.class, () -> multidimensionalCounter.getCounts(25));
    }

    @Test
    public void testDimensionMismatchException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        assertThrows(DimensionMismatchException.class, () -> multidimensionalCounter.getCount(new int[1]));
        assertThrows(DimensionMismatchException.class, () -> multidimensionalCounter.getCount(new int[4]));
    }

    @Test
    public void testIteratorNextWithNegativeCounter() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        Iterator iterator = multidimensionalCounter.iterator();
        iterator.counter = new int[]{3, 2, 1};
        assertThrows(IndexOutOfBoundsException.class, () -> iterator.next());
    }

    @Test
    public void testIteratorRemove() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        Iterator iterator = multidimensionalCounter.iterator();
        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }

}