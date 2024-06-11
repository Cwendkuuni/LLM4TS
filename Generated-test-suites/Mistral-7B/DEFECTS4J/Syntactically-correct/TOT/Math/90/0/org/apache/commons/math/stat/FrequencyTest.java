package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor with default comparator.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(1);
        Iterator keys = freq.valuesIterator();
        assertEquals(3, keys.next());
        assertEquals(2, keys.next());
        assertEquals(1, keys.next());
        assertEquals(1, freq.getCount(1));
        assertEquals(2, freq.getCount(2));
        assertEquals(3, freq.getSumFreq());
        assertEquals(0.3333, freq.getPct(1), 0.0001);
        assertEquals(0.6667, freq.getPct(2), 0.0001);
        assertEquals(0.3333, freq.getCumPct(1), 0.0001);
        assertEquals(0.6667, freq.getCumPct(2), 0.0001);
        assertEquals(0.6667, freq.getCumPct(3), 0.0001);
        assertEquals(1.0, freq.getCumPct(Integer.MAX_VALUE), 0.0001);
    }

    /**
     * Test constructor with specified comparator.
     */
    @Test
    public void testConstructorWithComparator() {
        Comparator comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return -((Comparable) o1).compareTo(o2);
            }
        };
        Frequency freq = new Frequency(comparator);
        freq.addValue(2);
        freq.addValue(1);
        Iterator keys = freq.valuesIterator();
        assertEquals(1, keys.next());
        assertEquals(2, keys.next());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertEquals(2, freq.getSumFreq());
        assertEquals(0.5, freq.getPct(1), 0.0001);
        assertEquals(0.5, freq.getPct(2), 0.0001);
        assertEquals(0.5, freq.getCumPct(1), 0.0001);
        assertEquals(1.0, freq.getCumPct(2), 0.0001);
    }

    /**
     * Test adding values of different types.
     */
    @Test
    public void testAddingValuesDifferentTypes() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
        freq.addValue(2);
        Iterator keys = freq.valuesIterator();
        assertEquals(3, keys.next());
        assertEquals(2, keys.next());
        assertEquals(1, keys.next());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount('a'));
        assertEquals(1, freq.getCount(2));
        assertEquals(3, freq.getSumFreq());
        assertEquals(0.3333, freq.getPct(1), 0.0001);
        assertEquals(0.3333, freq.getPct('a'), 0.0001);
        assertEquals(0.3333, freq.getPct(2), 0.0001);
        assertEquals(0.3333, freq.getCumPct(1), 0.0001);
        assertEquals(0.3333, freq.getCumPct('a'), 0.0001);
        assertEquals(0.3333, freq.getCumPct(2), 0.0001);
        assertEquals(1.0, freq.getCumPct(Integer.MAX_VALUE), 0.0001);
    }

    /**
     * Test adding values of the same type.
     */
    @Test
    public void testAddingValuesSameType() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        Iterator keys = freq.valuesIterator();
        assertEquals(1, keys.next());
        assertEquals(3, keys.next());
        assertEquals(1, freq.getCount(1));
        assertEquals(3, freq.getCount(1));
        assertEquals(3, freq.getSumFreq());
        assertEquals(1.0, freq.getPct(1), 0.0001);
        assertEquals(1.0, freq.getCumPct(1), 0.0001);
        assertEquals(1.0, freq.getCumPct(Integer.MAX_VALUE), 0.0001);
    }

    /**
     * Test adding non-comparable values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddingNonComparableValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
        freq.addValue(2);
        freq.addValue('b');
    }

    /**
     * Test adding null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(null);
    }

    /**
     * Test clearing the frequency table.
     */
    @Test
    public void testClearingFrequencyTable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        freq.clear();
        Iterator keys = freq.valuesIterator();
        assertFalse(keys.hasNext());
    }

    /**
     * Test getting frequencies for non-existent value.
     */
    @Test
    public void testGettingFrequencyForNonExistentValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0, freq.getCount(3));
    }

    /**
     * Test getting cumulative frequencies for non-existent value.
     */
    @Test
    public void testGettingCumulativeFrequenciesForNonExistentValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(0, freq.getCumFreq(3));
    }

    /**
     * Test getting percentages for non-existent value.
     */
    @Test
    public void testGettingPercentagesForNonExistentValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(Double.NaN, freq.getPct(3), 0.0);
    }

    /**
     * Test getting cumulative percentages for non-existent value.
     */
    @Test
    public void testGettingCumulativePercentagesForNonExistentValue() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(Double.NaN, freq.getCumPct(3), 0.0);
    }
}