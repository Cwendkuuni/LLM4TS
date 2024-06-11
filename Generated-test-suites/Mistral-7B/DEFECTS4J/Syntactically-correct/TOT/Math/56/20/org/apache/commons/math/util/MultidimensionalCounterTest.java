package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link MultidimensionalCounter}.
 */
public class MultidimensionalCounterTest {

    /**
     * Test the creation of a 1-dimensional counter.
     */
    @Test
    public void test1D() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1);
        Iterator it = counter.iterator();
        assertTrue(it.hasNext());
        assertEquals(0, it.next());
        assertTrue(it.hasNext());
        assertEquals(1, it.next());
        assertFalse(it.hasNext());
    }

    /**
     * Test the creation of a 2-dimensional counter.
     */
    @Test
    public void test2D() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2);
        Iterator it = counter.iterator();
        assertTrue(it.hasNext());
        assertEquals(0, it.next());
        int[] counts = it.getCounts();
        assertArrayEquals(new int[]{0, 0}, counts);

        assertTrue(it.hasNext());
        assertEquals(1, it.next());
        counts = it.getCounts();
        assertArrayEquals(new int[]{0, 1}, counts);

        assertTrue(it.hasNext());
        assertEquals(2, it.next());
        counts = it.getCounts();
        assertArrayEquals(new int[]{0, 2}, counts);

        assertFalse(it.hasNext());
    }

    /**
     * Test the creation of a 3-dimensional counter.
     */
    @Test
    public void test3D() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        Iterator it = counter.iterator();
        assertTrue(it.hasNext());
        assertEquals(0, it.next());
        int[] counts = it.getCounts();
        assertArrayEquals(new int[]{0, 0, 0}, counts);

        assertTrue(it.hasNext());
        assertEquals(1, it.next());
        counts = it.getCounts();
        assertArrayEquals(new int[]{0, 0, 1}, counts);

        assertTrue(it.hasNext());
        assertEquals(2, it.next());
        counts = it.getCounts();
        assertArrayEquals(new int[]{0, 1, 0}, counts);

        assertTrue(it.hasNext());
        assertEquals(3, it.next());
        counts = it.getCounts();
        assertArrayEquals(new int[]{0, 1, 1}, counts);

        assertTrue(it.hasNext());
        assertEquals(4, it.next());
        counts = it.getCounts();
        assertArrayEquals(new int[]{0, 1, 2}, counts);

        assertTrue(it.hasNext());
        assertEquals(5, it.next());
        counts = it.getCounts();
        assertArrayEquals(new int[]{0, 2, 0}, counts);

        assertTrue(it.hasNext());
        assertEquals(6, it.next());
        counts = it.getCounts();
        assertArrayEquals(new int[]{0, 2, 1}, counts);

        assertTrue(it.hasNext());
        assertEquals(7, it.next());
        counts = it.getCounts();
        assertArrayEquals(new int[]{0, 2, 2}, counts);

        assertTrue(it.hasNext());
        assertEquals(8, it.next());
        counts = it.getCounts();
        assertArrayEquals(new int[]{1, 0, 0}, counts);

        assertFalse(it.hasNext());
    }

    /**
     * Test out-of-range index.
     */
    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 2);
        counter.getCount(3, 2);
    }

    /**
     * Test dimension mismatch.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2);
        counter.getCount(1, 3);
    }
}