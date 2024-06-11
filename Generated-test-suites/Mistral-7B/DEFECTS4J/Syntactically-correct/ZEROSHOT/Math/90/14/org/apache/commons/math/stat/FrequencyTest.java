package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * Test class for {@link Frequency} class
 */
public class FrequencyTest {

    /**
     * Test construction without comparator
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
        assertEquals(0, freq.getSumFreq());
        assertEquals(0, freq.getCount(null));
        assertEquals(0, freq.getCumFreq(null));
        assertEquals(0, freq.getCumPct(null));
    }

    /**
     * Test construction with comparator
     */
    @org.junit.Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
        assertNotNull(freq);
        assertEquals(0, freq.getSumFreq());
        assertEquals(0, freq.getCount(null));
        assertEquals(0, freq.getCumFreq(null));
        assertEquals(0, freq.getCumPct(null));
    }

    /**
     * Test addValue method for int value
     */
    @org.junit.Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(1.0, freq.getPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
    }

    /**
     * Test addValue method for long value
     */
    @org.junit.Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(2L);
        assertEquals(1, freq.getCount(2));
        assertEquals(1, freq.getCumFreq(2));
        assertEquals(1.0, freq.getPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
    }

    /**
     * Test addValue method for char value
     */
    @org.junit.Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        assertEquals(1, freq.getCount('A'));
        assertEquals(1, freq.getCumFreq('A'));
        assertEquals(1.0, freq.getPct('A'), 0.001);
        assertEquals(1.0, freq.getCumPct('A'), 0.001);
    }

    /**
     * Test addValue method for Object value - null
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueNull() {
        Frequency freq = new Frequency();
        freq.addValue(null);
    }

    /**
     * Test addValue method for Object value - not comparable
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency freq = new Frequency();
        freq.addValue("test");
    }

    /**
     * Test addValue method for different types - int and long
     */
    @org.junit.Test
    public void testAddValueIntAndLong() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2L);
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(1.0, freq.getPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals(1, freq.getCount(2));
        assertEquals(2, freq.getCumFreq(2));
        assertEquals(2.0, freq.getPct(2), 0.001);
        assertEquals(2.0, freq.getCumPct(2), 0.001);
    }

    /**
     * Test addValue method for different types - int and char
     */
    @org.junit.Test
    public void testAddValueIntAndChar() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('A');
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(1.0, freq.getPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals(1, freq.getCount('A'));
        assertEquals(1, freq.getCumFreq('A'));
        assertEquals(1.0, freq.getPct('A'), 0.001);
        assertEquals(1.0, freq.getCumPct('A'), 0.001);
    }

    /**
     * Test addValue method for different types - long and char
     */
    @org.junit.Test
    public void testAddValueLongAndChar() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue('A');
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(1.0, freq.getPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals(1, freq.getCount('A'));
        assertEquals(1, freq.getCumFreq('A'));
        assertEquals(1.0, freq.getPct('A'), 0.001);
        assertEquals(1.0, freq.getCumPct('A'), 0.001);
    }

    /**
     * Test addValue method for different types - long and long
     */
    @org.junit.Test
    public void testAddValueLongAndLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(1.0, freq.getPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals(1, freq.getCount(2));
        assertEquals(2, freq.getCumFreq(2));
        assertEquals(2.0, freq.getPct(2), 0.001);
        assertEquals(2.0, freq.getCumPct(2), 0.001);
    }

    /**
     * Test getSumFreq method
     */
    @org.junit.Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2L);
        assertEquals(2, freq.getSumFreq());
    }

    /**
     * Test getCount method for int value
     */
    @org.junit.Test
    public void testGetCountInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
    }

    /**
     * Test getCount method for long value
     */
    @org.junit.Test
    public void testGetCountLong() {
        Frequency freq = new Frequency();
        freq.addValue(2L);
        assertEquals(1, freq.getCount(2));
    }

    /**
     * Test getCount method for char value
     */
    @org.junit.Test
    public void testGetCountChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        assertEquals(1, freq.getCount('A'));
    }

    /**
     * Test getCount method for null value
     */
    @org.junit.Test
    public void testGetCountNull() {
        Frequency freq = new Frequency();
        assertNull(freq.getCount(null));
    }

    /**
     * Test getCount method for not comparable value
     */
    @org.junit.Test
    public void testGetCountNotComparable() {
        Frequency freq = new Frequency();
        assertNull(freq.getCount("test"));
    }

    /**
     * Test getCumFreq method for int value
     */
    @org.junit.Test
    public void testGetCumFreqInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1, freq.getCumFreq(1));
    }

    /**
     * Test getCumFreq method for long value
     */
    @org.junit.Test
    public void testGetCumFreqLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(2));
    }

    /**
     * Test getCumFreq method for char value
     */
    @org.junit.Test
    public void testGetCumFreqChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('B');
        assertEquals(1, freq.getCumFreq('A'));
        assertEquals(2, freq.getCumFreq('B'));
    }

    /**
     * Test getCumFreq method for null value
     */
    @org.junit.Test
    public void testGetCumFreqNull() {
        Frequency freq = new Frequency();
        assertNull(freq.getCumFreq(null));
    }

    /**
     * Test getCumFreq method for not comparable value
     */
    @org.junit.Test
    public void testGetCumFreqNotComparable() {
        Frequency freq = new Frequency();
        assertNull(freq.getCumFreq("test"));
    }

    /**
     * Test getPct method for int value
     */
    @org.junit.Test
    public void testGetPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1.0, freq.getPct(1), 0.001);
    }

    /**
     * Test getPct method for long value
     */
    @org.junit.Test
    public void testGetPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(2L);
        assertEquals(1.0, freq.getPct(2), 0.001);
    }

    /**
     * Test getPct method for char value
     */
    @org.junit.Test
    public void testGetPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        assertEquals(1.0, freq.getPct('A'), 0.001);
    }

    /**
     * Test getPct method for null value
     */
    @org.junit.Test
    public void testGetPctNull() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getPct(null));
    }

    /**
     * Test getPct method for not comparable value
     */
    @org.junit.Test
    public void testGetPctNotComparable() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getPct("test"));
    }

    /**
     * Test getCumPct method for int value
     */
    @org.junit.Test
    public void testGetCumPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
    }

    /**
     * Test getCumPct method for long value
     */
    @org.junit.Test
    public void testGetCumPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        assertEquals(1.0, freq.getCumPct(1), 0.001);
        assertEquals(2.0, freq.getCumPct(2), 0.001);
    }

    /**
     * Test getCumPct method for char value
     */
    @org.junit.Test
    public void testGetCumPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('A');
        freq.addValue('B');
        assertEquals(1.0, freq.getCumPct('A'), 0.001);
        assertEquals(2.0, freq.getCumPct('B'), 0.001);
    }

    /**
     * Test getCumPct method for null value
     */
    @org.junit.Test
    public void testGetCumPctNull() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getCumPct(null));
    }

    /**
     * Test getCumPct method for not comparable value
     */
    @org.junit.Test
    public void testGetCumPctNotComparable() {
        Frequency freq = new Frequency();
        assertEquals(Double.NaN, freq.getCumPct("test"));
    }

    /**
     * Test clear method
     */
    @org.junit.Test
     public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertNull(freq.getCount(1));
        assertNull(freq.getCumFreq(1));
        assertEquals(0.0, freq.getPct(1), 0.001);
        assertEquals(0.0, freq.getCumPct(1), 0.001);
    }
}