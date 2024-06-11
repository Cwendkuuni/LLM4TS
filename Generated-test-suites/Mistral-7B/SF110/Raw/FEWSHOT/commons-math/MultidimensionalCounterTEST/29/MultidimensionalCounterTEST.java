package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            fail("Should not throw NotStrictlyPositiveException");
        }
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[]{3, 2, 1}, counter.getSizes());
        assertEquals(18, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizes() {
        int[] size = new int[]{-1, 2, 1};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testMultidimensionalCounterGetCounts() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] indices;

        indices = counter.getCounts(0);
        assertEquals(0, indices[0]);
        assertEquals(0, indices[1]);
        assertEquals(0, indices[2]);

        indices = counter.getCounts(1);
        assertEquals(0, indices[0]);
        assertEquals(0, indices[1]);
        assertEquals(1, indices[2]);

        indices = counter.getCounts(2);
        assertEquals(0, indices[0]);
        assertEquals(0, indices[1]);
        assertEquals(2, indices[2]);

        indices = counter.getCounts(3);
        assertEquals(0, indices[0]);
        assertEquals(0, indices[1]);
        assertEquals(2, indices[2]);

        indices = counter.getCounts(4);
        assertEquals(0, indices[0]);
        assertEquals(1, indices[1]);
        assertEquals(2, indices[2]);

        indices = counter.getCounts(5);
        assertEquals(0, indices[0]);
        assertEquals(1, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(6);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(7);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(8);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(9);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(10);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(11);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(12);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(13);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(14);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(15);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(16);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);

        indices = counter.getCounts(17);
        assertEquals(0, indices[0]);
        assertEquals(2, indices[1]);
        assertEquals(3, indices[2]);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int count = counter.getCount(new int[]{0, 0, 0});
        assertEquals(0, count);

        count = counter.getCount(new int[]{0, 0, 1});
        assertEquals(0, count);

        count = counter.getCount(new int[]{0, 1, 0});
        assertEquals(0, count);

        count = counter.getCount(new int[]{0, 1, 1});
        assertEquals(0, count);

        count = counter.getCount(new int[]{1, 0, 0});
        assertEquals(0, count);

        count = counter.getCount(new int[]{1, 0, 1});
        assertEquals(0, count);

        count = counter.getCount(new int[]{1, 1, 0});
        assertEquals(0, count);

        count = counter.getCount(new int[]{1, 1, 1});
        assertEquals(0, count);

        count = counter.getCount(new int[]{2, 0, 0});
        assertEquals(0, count);

        count = counter.getCount(new int[]{2, 0, 1});
        assertEquals(0, count);

        count = counter.getCount(new int[]{2, 1, 0});
        assertEquals(0, count);

        count = counter.getCount(new int[]{2, 1, 1});
        assertEquals(0, count);

        count = counter.getCount(new int[]{2, 2, 0});
        assertEquals(0, count);

        count = counter.getCount(new int[]{2, 2, 1});
        assertEquals(0, count);

        count = counter.getCount(new int[]{2, 2, 2});
        assertEquals(0, count);

        try {
            count = counter.getCount(new int[]{3, 0, 0});
            fail("Should throw OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }

        try {
            count = counter.getCount(new int[]{3, 2, 3});
            fail("Should throw OutOfRangeException");
        } catch (OutOfRangeException e) {
            // expected
        }

        try {
            count = counter.getCount(new int[]{3, 3, 3});
            fail("Should throw DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = new int[]{3, 2, 1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator it = counter.iterator();

        assertTrue(it.hasNext());
        assertEquals(0, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(1, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(2, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(3, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(4, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(5, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(6, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(7, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(8, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(9, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(10, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(11, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(12, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(13, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(14, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(15, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(16, it.next().intValue());

        assertTrue(it.hasNext());
        assertEquals(17, it.next().intValue());
    }
}