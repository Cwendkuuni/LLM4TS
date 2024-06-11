package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor_ValidArguments() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertArrayEquals(new int[] {2, 3, 4}, mdc.getSizes());
        assertEquals(24, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_InvalidArguments() {
        int[] size = {2, -1, 4};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        assertEquals(3, mdc.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRangeIndex() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        mdc.getCounts(5);
    }

    @Test
    public void testGetCounts_ValidIndex() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        int[] expected = {0, 1, 2};
        int[] actual = mdc.getCounts(0);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_InvalidDimension() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        mdc.getCount(-1, new int[] {1, 2});
    }

    @Test
    public void testGetCount_ValidDimension() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        int expected = 12;
        int actual = mdc.getCount(new int[] {1, 1, 1});
        assertEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeDimensionValue() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        mdc.getCount(new int[] {1, 1, 5});
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeDimensionIndex() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        mdc.getCount(new int[] {2, 2});
    }

    @Test
    public void testIterator_HasNext_False() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        Iterator it = mdc.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    public void testIterator_HasNext_True() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        Iterator it = mdc.iterator();
        assertTrue(it.hasNext());
    }

    @Test
    public void testIterator_Next() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        Iterator it = mdc.iterator();
        int expectedNext = 0;
        int actualNext = it.next();
        assertEquals(expectedNext, actualNext);

        int[] expectedCounter = {0, 0, 0};
        int[] actualCounter = it.getCounts();
        assertArrayEquals(expectedCounter, actualCounter);

        expectedNext = 1;
        expectedCounter = new int[] {1, 0, 0};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 2;
        expectedCounter = new int[] {1, 0, 1};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 3;
        expectedCounter = new int[] {1, 1, 0};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 4;
        expectedCounter = new int[] {1, 1, 1};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 5;
        expectedCounter = new int[] {1, 1, 2};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 6;
        expectedCounter = new int[] {1, 2, 0};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 7;
        expectedCounter = new int[] {1, 2, 1};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 8;
        expectedCounter = new int[] {1, 2, 2};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 9;
        expectedCounter = new int[] {1, 3, 0};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 10;
        expectedCounter = new int[] {1, 3, 1};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 11;
        expectedCounter = new int[] {1, 3, 2};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 12;
        expectedCounter = new int[] {1, 3, 3};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 13;
        expectedCounter = new int[] {1, 4, 0};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 14;
        expectedCounter = new int[] {1, 4, 1};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 15;
        expectedCounter = new int[] {1, 4, 2};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 16;
        expectedCounter = new int[] {1, 4, 3};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 17;
        expectedCounter = new int[] {2, 0, 0};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 18;
        expectedCounter = new int[] {2, 0, 1};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 19;
        expectedCounter = new int[] {2, 0, 2};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 20;
        expectedCounter = new int[] {2, 0, 3};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 21;
        expectedCounter = new int[] {2, 1, 0};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 22;
        expectedCounter = new int[] {2, 1, 1};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 23;
        expectedCounter = new int[] {2, 1, 2};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = 24;
        expectedCounter = new int[] {2, 1, 3};
        it.next();
        actualNext = it.next();
        assertEquals(expectedNext, actualNext);
        assertArrayEquals(expectedCounter, it.getCounts());

        expectedNext = -1;
        assertNull(it.next());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        String expected = "[0][1][2]";
        String actual = mdc.toString();
        assertEquals(expected, actual);

        mdc = new MultidimensionalCounter(new int[] {5, 5, 5});
        expected = "[0][0][0][0][0][0][0][0][0][0]";
        actual = mdc.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {2, 3, 4});
        Iterator it = mdc.iterator();
        it.remove();
    }
}