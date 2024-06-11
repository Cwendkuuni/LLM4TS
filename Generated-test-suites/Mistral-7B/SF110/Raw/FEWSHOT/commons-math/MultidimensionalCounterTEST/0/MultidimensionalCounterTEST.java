package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertEquals(Arrays.toString(size), Arrays.toString(mdc.getSizes()));
        assertEquals(3 * 4 * 5, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSize() {
        int[] size = {-1, 4, 5};
        try {
            new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            assertEquals(-1, e.getValue());
        }
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator iterator = mdc.iterator();

        assertFalse(iterator.hasNext());

        iterator.next();
        int[] counts = iterator.getCounts();
        int count = iterator.getCount();
        assertEquals(1, count);
        assertEquals(new int[]{0, 0, 0}, counts);

        iterator.next();
        counts = iterator.getCounts();
        count = iterator.getCount();
        assertEquals(2, count);
        assertEquals(new int[]{0, 0, 1}, counts);

        for (int i = 0; i < 5 * 4 * 3; ++i) {
            iterator.next();
        }

        counts = iterator.getCounts();
        count = iterator.getCount();
        assertEquals(5 * 4 * 3 + 1, count);
        int lastIndex = sizes(size).length - 1;
        assertEquals(new int[]{5, 4, lastIndex}, counts);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        int[] c = new int[3];

        for (int i = 0; i < 5 * 4 * 3; ++i) {
            c = mdc.getCounts(i);
            int count = mdc.getCount(c);
            assertEquals(i + 1, count);
        }

        try {
            mdc.getCount(new int[]{3, 4, 5});
            fail();
        } catch (DimensionMismatchException e) {
            // expected
        }

        try {
            mdc.getCount(new int[]{3, 4, 6});
            fail();
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testMultidimensionalCounterToString() {
        int[] size = {3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size.length; ++i) {
            sb.append("[");
            sb.append(mdc.getCount(i));
            sb.append("]");
        }

        assertEquals(sb.toString(), mdc.toString());
    }

    private int[] sizes(int[] size) {
        int length = size.length;
        return new int[length];
    }
}