package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test method for constructor Frequency().
     */
    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        assertEquals(0, freq.getCount(1));
        assertEquals(0.0, freq.getPct(1), 0.001);
        assertEquals(0.0, freq.getCumPct(1), 0.001);
    }

    /**
     * Test method for constructor Frequency(Comparator).
     */
    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
        Frequency expected = new Frequency();
        assertEquals(expected, freq);
    }

    /**
     * Test method for method addValue(Object).
     */
    @Test
    public void testAddValueObject() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
        assertEquals(1.0 / 2.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 2.0, freq.getCumPct(1), 0.001);

        try {
            freq.addValue("hello");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for method addValue(int).
     */
    @Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
        assertEquals(1.0 / 2.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 2.0, freq.getCumPct(1), 0.001);

        freq.addValue(1);
        assertEquals(2, freq.getCount(1));
        assertEquals(1.0, freq.getPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(1), 0.001);

        freq.addValue(2);
        assertEquals(2, freq.getCount(1));
        assertEquals(1.0 / 2.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 2.0, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
    }

    /**
     * Test method for method addValue(long).
     */
    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
        assertEquals(1.0 / 2.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 2.0, freq.getCumPct(1), 0.001);

        freq.addValue(1);
        assertEquals(2, freq.getCount(1));
        assertEquals(1.0, freq.getPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);

        freq.addValue(2);
        assertEquals(2, freq.getCount(1));
        assertEquals(1.0 / 2.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 2.0, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(3), 0.001);
    }

    /**
     * Test method for method addValue(char).
     */
    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        assertEquals(1, freq.getCount('a'));
        assertEquals(1.0 / 2.0, freq.getPct('a'), 0.001);
        assertEquals(1.0 / 2.0, freq.getCumPct('a'), 0.001);

        freq.addValue('a');
        assertEquals(2, freq.getCount('a'));
        assertEquals(1.0, freq.getPct('a'), 0.001);
        assertEquals(1.0, freq.getCumPct('a'), 0.001);
        assertEquals(1.0, freq.getCumPct('a'), 0.001);

        freq.addValue('b');
        assertEquals(2, freq.getCount('a'));
        assertEquals(1.0 / 2.0, freq.getPct('a'), 0.001);
        assertEquals(1.0 / 2.0, freq.getCumPct('a'), 0.001);
        assertEquals(1.0, freq.getCumPct('a'), 0.001);
        assertEquals(1.0, freq.getCumPct('b'), 0.001);
    }

    /**
     * Test method for method addValue(int[]).
     */
    @Test
    public void testAddValueIntArray() {
        Frequency freq = new Frequency();
        int[] values = {1, 1, 2, 2, 3, 3};
        for (int value : values) {
            freq.addValue(value);
        }
        Iterator valuesIterator = freq.valuesIterator();
        int i = 0;
        while (valuesIterator.hasNext()) {
            Object nextValue = valuesIterator.next();
            assertEquals(values[i++], nextValue);
        }
        assertEquals(6, freq.getSumFreq());
        assertEquals(3, freq.getCount(1));
        assertEquals(1.0 / 2.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 2.0, freq.getCumPct(1), 0.001);
        assertEquals(1.0 / 3.0, freq.getCumPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(3), 0.001);
    }

    /**
     * Test method for method addValue(long[]).
     */
    @Test
    public void testAddValueLongArray() {
        Frequency freq = new Frequency();
        long[] values = {1, 1, 2, 2, 3, 3};
        for (long value : values) {
            freq.addValue(value);
        }
        Iterator valuesIterator = freq.valuesIterator();
        int i = 0;
        while (valuesIterator.hasNext()) {
            Object nextValue = valuesIterator.next();
            assertEquals(values[i++], nextValue);
        }
        assertEquals(6, freq.getSumFreq());
        assertEquals(3, freq.getCount(1));
        assertEquals(1.0 / 2.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 2.0, freq.getCumPct(1), 0.001);
        assertEquals(1.0 / 3.0, freq.getCumPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(3), 0.001);
    }

    /**
     * Test method for method addValue(char[]).
     */
    @Test
    public void testAddValueCharArray() {
        Frequency freq = new Frequency();
        char[] values = {'a', 'a', 'b', 'b', 'c', 'c'};
        for (char value : values) {
            freq.addValue(value);
        }
        Iterator valuesIterator = freq.valuesIterator();
        int i = 0;
        while (valuesIterator.hasNext()) {
            Object nextValue = valuesIterator.next();
            assertEquals(values[i++], nextValue);
        }
        assertEquals(6, freq.getSumFreq());
        assertEquals(3, freq.getCount('a'));
        assertEquals(1.0 / 2.0, freq.getPct('a'), 0.001);
        assertEquals(1.0 / 2.0, freq.getCumPct('a'), 0.001);
        assertEquals(1.0 / 2.0, freq.getCumPct('b'), 0.001);
        assertEquals(1.0, freq.getCumPct('c'), 0.001);
    }

    /**
     * Test method for method clear().
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertEquals(0, freq.getCount(1));
        assertEquals(0.0, freq.getPct(1), 0.001);
        assertEquals(0.0, freq.getCumPct(1), 0.001);
    }
}