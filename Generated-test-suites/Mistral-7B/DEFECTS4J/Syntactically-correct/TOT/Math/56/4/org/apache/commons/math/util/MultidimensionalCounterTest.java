package org.apache.commons.math.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterZeroDimensions() {
        int[] size = new int[0];
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(size);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testMultidimensionalCounterOneDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{5});
        assertEquals(0, counter.getCount(0));
        assertIterateCorrectly(counter);
    }

    @Test
    public void testMultidimensionalCounterTwoDimensions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4});
        assertEquals(0, counter.getCount(0, 0));
        assertIterateCorrectly(counter);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterWrongDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4});
        counter.getCount(5, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4});
        counter.getCount(3, 4);
    }

    @Test
    public void testMultidimensionalCounterReset() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4});
        int index = counter.getCount(2, 3);
        counter.reset();
        assertFalse(counter.hasNext());
    }

    @Test
    public void testMultidimensionalCounterResetSameIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4});
        int index = counter.getCount(2, 3);
        counter.reset();
        int newIndex = counter.getCount(2, 3);
        assertEquals(index, newIndex);
    }

    @Test
    public void testMultidimensionalCounterResetNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4});
        int index = counter.getCount(2, 3);
        counter.reset();
        int nextIndex = counter.next().getCount();
        assertNotEquals(index, nextIndex);
    }

    @Test
    public void testMultidimensionalCounterParallel() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 4});
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                counter.next();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                counter.next();
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Iterator<Integer> iterator = counter.iterator();
        int totalCount = 0;
        while (iterator.hasNext()) {
            totalCount++;
            iterator.next();
        }
        assertEquals(200, totalCount);
    }

    private void assertIterateCorrectly(MultidimensionalCounter counter) {
        Iterator<Integer> iterator = counter.iterator();
        int count = -1;
        int[] counts = new int[counter.getDimension()];
        while (iterator.hasNext()) {
            int next = iterator.next();
            count++;
            counts = iterator.getCounts();
            for (int i = 0; i < counter.getDimension(); i++) {
                assertEquals(count % counter.getSize(i), counts[i]);
                count++;
            }
        }
        assertFalse(iterator.hasNext());
    }

}