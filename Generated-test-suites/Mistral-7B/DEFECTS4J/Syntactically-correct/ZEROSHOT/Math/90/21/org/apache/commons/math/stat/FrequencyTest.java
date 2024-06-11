package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * Test class for {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
        Frequency comparatorFreq = new Frequency(new NaturalComparator());
        assertNotNull(comparatorFreq);
    }

    /**
     * Test addValue method with Long values.
     */
    @org.junit.Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);
        freq.addValue(2L);
        assertEquals(4, freq.getSumFreq());
        assertEquals(2, freq.getCount(1L));
        assertEquals(2, freq.getCount(2L));
        assertEquals(1.0, freq.getPct(1L), 0.001);
        assertEquals(1.0, freq.getPct(2L), 0.001);
        assertEquals(0.5, freq.getCumPct(1L), 0.001);
        assertEquals(1.0, freq.getCumPct(2L), 0.001);
    }

    /**
     * Test addValue method with Integer values.
     */
    @org.junit.Test
    public void testAddValueInteger() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(4, freq.getSumFreq());
        assertEquals(2, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
        assertEquals(1.0, freq.getPct(1), 0.001);
        assertEquals(1.0, freq.getPct(2), 0.001);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
    }

    /**
     * Test addValue method with char values.
     */
    @org.junit.Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('A');
        freq.addValue('B');
        freq.addValue('B');
        assertEquals(4, freq.getSumFreq());
        assertEquals(2, freq.getCount('A'));
        assertEquals(2, freq.getCount('B'));
        assertEquals(1.0, freq.getPct('A'), 0.001);
        assertEquals(1.0, freq.getPct('B'), 0.001);
        assertEquals(0.5, freq.getCumPct('A'), 0.001);
        assertEquals(1.0, freq.getCumPct('B'), 0.001);
    }

    /**
     * Test addValue method with non-comparable values.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue('A');
    }

    /**
     * Test clear method.
     */
    @org.junit.Test
     public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertNull(freq.getCount(1L));
        assertNull(freq.getCount(2L));
        assertNull(freq.getCount('A'));
    }

    /**
     * Test getCumFreq method with Long values.
     */
    @org.junit.Test
    public void testGetCumFreqLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);
        freq.addValue(2L);
        assertEquals(0, freq.getCumFreq(null));
        assertEquals(1, freq.getCumFreq(0L));
        assertEquals(2, freq.getCumFreq(1L));
        assertEquals(2, freq.getCumFreq(1L)); // same value, should return the same result
        assertEquals(3, freq.getCumFreq(2L));
        assertEquals(4, freq.getCumFreq(3L));
    }

    /**
     * Test getCumFreq method with Integer values.
     */
    @org.junit.Test
    public void testGetCumFreqInteger() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0, freq.getCumFreq(null));
        assertEquals(1, freq.getCumFreq(0));
        assertEquals(2, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(1)); // same value, should return the same result
        assertEquals(3, freq.getCumFreq(2));
        assertEquals(4, freq.getCumFreq(3));
    }

    /**
     * Test getCumFreq method with char values.
     */
    @org.junit.Test
    public void testGetCumFreqChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('A');
        freq.addValue('B');
        freq.addValue('B');
        assertEquals(0, freq.getCumFreq(null));
        assertEquals(1, freq.getCumFreq('A'.toString().charAt(0)));
        assertEquals(2, freq.getCumFreq('A'.toString().charAt(0))); // same value, should return the same result
        assertEquals(3, freq.getCumFreq('B'.toString().charAt(0)));
        assertEquals(4, freq.getCumFreq('C'.toString().charAt(0))); // different value
    }

    /**
     * Test getCumPct method with Long values.
     */
    @org.junit.Test
    public void testGetCumPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);
        freq.addValue(2L);
        assertEquals(Double.NaN, freq.getCumPct(null));
        assertEquals(0.25, freq.getCumPct(0L), 0.001);
        assertEquals(0.5, freq.getCumPct(1L), 0.001);
        assertEquals(0.75, freq.getCumPct(2L), 0.001);
        assertEquals(1.0, freq.getCumPct(3L), 0.001);
    }

    /**
     * Test getCumPct method with Integer values.
     */
    @org.junit.Test
    public void testGetCumPctInteger() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(Double.NaN, freq.getCumPct(null));
        assertEquals(0.25, freq.getCumPct(0), 0.001);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
        assertEquals(0.75, freq.getCumPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(3), 0.001);
    }

    /**
     * Test getCumPct method with char values.
     */
    @org.junit.Test
    public void testGetCumPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('A');
        freq.addValue('B');
        freq.addValue('B');
        assertEquals(Double.NaN, freq.getCumPct(null));
        assertEquals(0.25, freq.getCumPct('A'.toString().charAt(0)), 0.001);
        assertEquals(0.5, freq.getCumPct('A'.toString().charAt(0)), 0.001); // same value, should return the same result
        assertEquals(0.75, freq.getCumPct('B'.toString().charAt(0)), 0.001);
        assertEquals(1.0, freq.getCumPct('C'.toString().charAt(0)), 0.001); // different value
    }

    /**
     * Test getCount method with Long values.
     */
    @org.junit.Test
    public void testGetCountLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);
        freq.addValue(2L);
        assertEquals(0, freq.getCount(null));
        assertEquals(2, freq.getCount(1L));
        assertEquals(2, freq.getCount(2L));
        assertEquals(0, freq.getCount(3L));
    }

    /**
     * Test getCount method with Integer values.
     */
    @org.junit.Test
    public void testGetCountInteger() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0, freq.getCount(null));
        assertEquals(2, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
        assertEquals(0, freq.getCount(3));
    }

    /**
     * Test getCount method with char values.
     */
    @org.junit.Test
    public void testGetCountChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('A');
        freq.addValue('B');
        freq.addValue('B');
        assertEquals(0, freq.getCount(null));
        assertEquals(2, freq.getCount('A'.toString().charAt(0)));
        assertEquals(2, freq.getCount('B'.toString().charAt(0)));
        assertEquals(0, freq.getCount('C'.toString().charAt(0)));
    }

    /**
     * Test getPct method with Long values.
     */
    @org.junit.Test
    public void testGetPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);
        freq.addValue(2L);
        assertEquals(Double.NaN, freq.getPct(null));
        assertEquals(0.25, freq.getPct(1L), 0.001);
        assertEquals(0.25, freq.getPct(1L), 0.001); // same value, should return the same result
        assertEquals(0.25, freq.getPct(1L), 0.001); // same value, should return the same result
        assertEquals(0.5, freq.getPct(2L), 0.001);
        assertEquals(1.0, freq.getPct(3L), 0.001); // different value
    }

    /**
     * Test getPct method with Integer values.
     */
    @org.junit.Test
    public void testGetPctInteger() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(Double.NaN, freq.getPct(null));
        assertEquals(0.25, freq.getPct(1), 0.001);
        assertEquals(0.25, freq.getPct(1), 0.001); // same value, should return the same result
        assertEquals(0.25, freq.getPct(1), 0.001); // same value, should return the same result
        assertEquals(0.5, freq.getPct(2), 0.001);
        assertEquals(1.0, freq.getPct(3), 0.001); // different value
    }

    /**
     * Test getPct method with char values.
     */
    @org.junit.Test
    public void testGetPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('A');
        freq.addValue('B');
        freq.addValue('B');
        assertEquals(Double.NaN, freq.getPct(null));
        assertEquals(0.25, freq.getPct('A'.toString().charAt(0)), 0.001);
        assertEquals(0.25, freq.getPct('A'.toString().charAt(0)), 0.001); // same value, should return the same result
        assertEquals(0.25, freq.getPct('A'.toString().charAt(0)), 0.001); // same value, should return the same result
        assertEquals(0.5, freq.getPct('B'.toString().charAt(0)), 0.001);
        assertEquals(0.0, freq.getPct('C'.toString().charAt(0)), 0.001); // different value
    }

    /**
     * Test iterator method.
     */
    @org.junit.Test
    public void testIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);
        freq.addValue(2L);
        Iterator<Comparable> iterator = freq.valuesIterator();
        int index = 0;
        while (iterator.hasNext()) {
            Comparable next = iterator.next();
            assertEquals(Long.valueOf(index % 2 + 1), next);
            index++;
        }
    }

    /**
     * Test toString method.
     */
    @org.junit.Test
    public void testToString() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);
        freq.addValue(2L);
        String expected = "Value      Freq.      Pct.      Cum Pct.\n" +
                "1         2         0.25        0.25\n" +
                "2         2         0.25        0.50\n" +
                "--------------------\n" +
                "Total      4         1.00        1.00";
        String actual = freq.toString();
        assertEquals(expected, actual);
    }
}