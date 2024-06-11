package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Test class for {@link MultidimensionalCounter}.
 */
public class MultidimensionalCounterTest {

    /**
     * Test a 1-dimensional counter.
     */
    @Test
    public void test1DimensionalCounter() {
        int[] size = new int[] {1};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < counter.getSize(); i++) {
            assertEquals(i, iterator.next().intValue());
        }
        assertFalse(iterator.hasNext());
    }

    /**
     * Test a 2-dimensional counter.
     */
    @Test
    public void test2DimensionalCounter() {
        int[] size = new int[] {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < size[0] * size[1]; i++) {
            assertEquals(i, iterator.next().intValue());
        }
        assertFalse(iterator.hasNext());

        int[] counts = counter.getCounts(0);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);

        for (int j = 0; j < size[1]; j++) {
            counts = counter.getCounts(j * size[0] + i);
            assertEquals(i, counts[0]);
            assertEquals(j, counts[1]);
        }
    }

    /**
     * Test a 3-dimensional counter.
     */
    @Test
    public void test3DimensionalCounter() {
        int[] size = new int[] {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            assertEquals(i, iterator.next().intValue());
        }
        assertFalse(iterator.hasNext());

        int[] counts = counter.getCounts(0);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        for (int j = 0; j < size[1]; j++) {
            for (int k = 0; k < size[2]; k++) {
                counts = counter.getCounts(j * size[2] + k);
                assertEquals(k, counts[2]);
                assertEquals(j, counts[1]);
                assertEquals(i, counts[0]);
                i += size[0];
            }
        }

        try {
            counter.getCounts(size[0] * size[1] * size[2]);
            fail("Exception expected");
        } catch (OutOfRangeException e) {
            // expected
        }

        try {
            counter.getCounts(-1);
            fail("Exception expected");
        } catch (OutOfRangeException e) {
            // expected
        }

        try {
            counter.getCounts(null);
            fail("Exception expected");
        } catch (DimensionMismatchException e) {
            // expected
        }
    }
}