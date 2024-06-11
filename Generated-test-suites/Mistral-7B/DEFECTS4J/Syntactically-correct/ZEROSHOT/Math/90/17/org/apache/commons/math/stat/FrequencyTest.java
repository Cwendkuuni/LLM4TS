package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

/**
 * Test class for the Frequency distribution class.
 */
public class FrequencyTest {

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertTrue(frequency instanceof TreeMap);
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    /**
     * Test the constructor with a Comparator.
     */
    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
        assertTrue(frequency instanceof TreeMap);
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    /**
     * Test the addValue method with an Integer.
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getSumFreq());
        Iterator iterator = frequency.valuesIterator();
        assertNotNull(iterator.next());
        assertEquals(1, iterator.next().longValue());
    }

    /**
     * Test the addValue method with a Long.
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getSumFreq());
        Iterator iterator = frequency.valuesIterator();
        assertNotNull(iterator.next());
        assertEquals(1, iterator.next().longValue());
    }

    /**
     * Test the addValue method with an Integer and an IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
    }

    /**
     * Test the addValue method with a Character.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        assertEquals(1, frequency.getCount('A'));
        assertEquals(1, frequency.getSumFreq());
        Iterator iterator = frequency.valuesIterator();
        assertNotNull(iterator.next());
        assertEquals(1, iterator.next().longValue());
    }

    /**
     * Test the addValue method with an Integer and a duplicate value.
     */
    @Test
    public void testAddValueDuplicate() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getSumFreq());
        Iterator iterator = frequency.valuesIterator();
        assertNotNull(iterator.next());
        assertEquals(1, iterator.next().longValue());
        assertNotNull(iterator.next());
        assertEquals(1, iterator.next().longValue());
    }

    /**
     * Test the clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getSumFreq());
        Iterator iterator = frequency.valuesIterator();
        assertNotNull(iterator.next());
        assertEquals(1, iterator.next().longValue());
        frequency.clear();
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getSumFreq());
        assertEmpty(frequency.valuesIterator());
    }

    /**
     * Test the valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Iterator iterator = frequency.valuesIterator();
        assertNotNull(iterator.next());
        assertEquals(1, iterator.next().longValue());
        assertNotNull(iterator.next());
        assertEquals(2, iterator.next().longValue());
        assertNotNull(iterator.next());
        assertEquals(1, iterator.next().longValue());
    }

    /**
     * Test the getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals(4, frequency.getSumFreq());
    }

    /**
     * Test the getCount method with an Integer.
     */
    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
    }

    /**
     * Test the getCount method with a Long.
     */
    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(Long.valueOf(1)));
        assertEquals(1, frequency.getCount(Long.valueOf(2)));
    }

    /**
     * Test the getCount method with a Character.
     */
    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        assertEquals(2, frequency.getCount('A'));
        assertEquals(1, frequency.getCount('B'));
    }

    /**
     * Test the getPct method with an Integer.
     */
    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double pct = frequency.getPct(1);
        assertTrue(pct > 0 && pct < 1);
        assertEquals(0.4, pct, 0.001);
        pct = frequency.getPct(2);
        assertTrue(pct > 0 && pct < 1);
        assertEquals(0.3, pct, 0.001);
        pct = frequency.getPct(3);
        assertEquals(Double.NaN, pct, 0.0);
    }

    /**
     * Test the getPct method with a Long.
     */
    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double pct = frequency.getPct(Long.valueOf(1));
        assertTrue(pct > 0 && pct < 1);
        assertEquals(0.4, pct, 0.001);
        pct = frequency.getPct(Long.valueOf(2));
        assertTrue(pct > 0 && pct < 1);
        assertEquals(0.3, pct, 0.001);
        pct = frequency.getPct(Long.valueOf(3));
        assertEquals(Double.NaN, pct, 0.0);
    }

    /**
     * Test the getPct method with a Character.
     */
    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        double pct = frequency.getPct('A');
        assertTrue(pct > 0 && pct < 1);
        assertEquals(0.6, pct, 0.001);
        pct = frequency.getPct('B');
        assertTrue(pct > 0 && pct < 1);
        assertEquals(0.2, pct, 0.001);
        pct = frequency.getPct('C');
        assertEquals(Double.NaN, pct, 0.0);
    }

    /**
     * Test the getCumFreq method with an Integer.
     */
    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(1, cumFreq);
        cumFreq = frequency.getCumFreq(2);
        assertEquals(2, cumFreq);
        cumFreq = frequency.getCumFreq(3);
        assertEquals(3, cumFreq);
        cumFreq = frequency.getCumFreq(4);
        assertEquals(3, cumFreq);
        cumFreq = frequency.getCumFreq(5);
        assertEquals(3, cumFreq);
        cumFreq = frequency.getCumFreq(-1);
        assertEquals(3, cumFreq);
        cumFreq = frequency.getCumFreq(Long.MAX_VALUE);
        assertEquals(3, cumFreq);
        cumFreq = frequency.getCumFreq(Long.MIN_VALUE);
        assertEquals(0, cumFreq);
    }

    /**
     * Test the getCumFreq method with a Long.
     */
    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(Long.valueOf(1));
        assertEquals(1, cumFreq);
        cumFreq = frequency.getCumFreq(Long.valueOf(2));
        assertEquals(2, cumFreq);
        cumFreq = frequency.getCumFreq(Long.valueOf(3));
        assertEquals(3, cumFreq);
        cumFreq = frequency.getCumFreq(Long.valueOf(4));
        assertEquals(3, cumFreq);
        cumFreq = frequency.getCumFreq(Long.valueOf(5));
        assertEquals(3, cumFreq);
        cumFreq = frequency.getCumFreq(Long.MAX_VALUE);
        assertEquals(3, cumFreq);
        cumFreq = frequency.getCumFreq(Long.MIN_VALUE);
        assertEquals(0, cumFreq);
    }

    /**
     * Test the getCumFreq method with a Character.
     */
    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        long cumFreq = frequency.getCumFreq('A');
        assertEquals(1, cumFreq);
        cumFreq = frequency.getCumFreq('B');
        assertEquals(2, cumFreq);
        cumFreq = frequency.getCumFreq('C');
        assertEquals(2, cumFreq);
        cumFreq = frequency.getCumFreq('D');
        assertEquals(2, cumFreq);
        cumFreq = frequency.getCumFreq('E');
        assertEquals(2, cumFreq);
        cumFreq = frequency.getCumFreq('F');
        assertEquals(2, cumFreq);
        cumFreq = frequency.getCumFreq('G');
        assertEquals(0, cumFreq);
    }

    /**
     * Test the getCumPct method with an Integer.
     */
    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.3, cumPct, 0.001);
        cumPct = frequency.getCumPct(2);
        assertEquals(0.6, cumPct, 0.001);
        cumPct = frequency.getCumPct(3);
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct(4);
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct(5);
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct(-1);
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct(Long.MAX_VALUE);
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct(Long.MIN_VALUE);
        assertEquals(0.0, cumPct, 0.0);
    }

    /**
     * Test the getCumPct method with a Long.
     */
    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        double cumPct = frequency.getCumPct(Long.valueOf(1));
        assertEquals(0.3, cumPct, 0.001);
        cumPct = frequency.getCumPct(Long.valueOf(2));
        assertEquals(0.6, cumPct, 0.001);
        cumPct = frequency.getCumPct(Long.valueOf(3));
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct(Long.valueOf(4));
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct(Long.valueOf(5));
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct(Long.MAX_VALUE);
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct(Long.MIN_VALUE);
        assertEquals(0.0, cumPct, 0.0);
    }

    /**
     * Test the getCumPct method with a Character.
     */
    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        frequency.addValue('A');
        frequency.addValue('B');
        double cumPct = frequency.getCumPct('A');
        assertEquals(0.6, cumPct, 0.001);
        cumPct = frequency.getCumPct('B');
        assertEquals(0.7, cumPct, 0.001);
        cumPct = frequency.getCumPct('C');
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct('D');
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct('E');
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct('F');
        assertEquals(1.0, cumPct, 0.001);
        cumPct = frequency.getCumPct('G');
        assertEquals(1.0, cumPct, 0.001);
    }

    /**
     * Empty iterator test.
     */
    @Test
    public void testEmptyIterator() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.valuesIterator().hasNext());
        assertNull(frequency.valuesIterator().next());
        assertFalse(frequency.valuesIterator().hasNext());
    }

}