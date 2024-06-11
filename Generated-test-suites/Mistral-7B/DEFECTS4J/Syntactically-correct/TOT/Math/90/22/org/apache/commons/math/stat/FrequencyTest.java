package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * Tests for {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test of the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        assertTrue(freq.isEmpty());
        // Test addValue method
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(3, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1.0 / 3.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 3.0, freq.getCumPct(1), 0.001);
    }

    /**
     * Test of the constructor with a custom Comparator.
     */
    @Test
    public void testCustomComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2) * -1;
            }
        };
        Frequency freq = new Frequency(comparator);
        assertEquals(0, freq.getSumFreq());
        assertTrue(freq.isEmpty());
        // Test addValue method
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(3, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1.0 / 3.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 3.0, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(3), 0.001);
    }

    /**
     * Test of the addValue method with different types.
     */
    @Test
    public void testAddValueWithDifferentTypes() {
        Frequency freq = new Frequency();
        // Test int value
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertEquals(0.5, freq.getPct(1), 0.001);
        assertEquals(0.5, freq.getPct(2), 0.001);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
        // Test Integer value
        freq.clear();
        freq.addValue(new Integer(1));
        freq.addValue(new Integer(2));
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertEquals(0.5, freq.getPct(1), 0.001);
        assertEquals(0.5, freq.getPct(2), 0.001);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
        // Test long value
        freq.clear();
        freq.addValue(1L);
        freq.addValue(2L);
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertEquals(0.5, freq.getPct(1), 0.001);
        assertEquals(0.5, freq.getPct(2), 0.001);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
        // Test Long value
        freq.clear();
        freq.addValue(Long.valueOf(1));
        freq.addValue(Long.valueOf(2));
        assertEquals(2, freq.getSumFreq());
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertEquals(0.5, freq.getPct(1), 0.001);
        assertEquals(0.5, freq.getPct(2), 0.001);
        assertEquals(0.5, freq.getCumPct(1), 0.001);
        assertEquals(1.0, freq.getCumPct(2), 0.001);
    }

    /**
     * Test of the addValue method with non-comparable values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValues() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue('a');
    }

    /**
     * Test of the clear method.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertTrue(freq.isEmpty());
    }

    /**
     * Test of the valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        Iterator iterator = freq.valuesIterator();
        assertEquals(1, iterator.next());
        assertEquals(1, iterator.next());
        assertEquals(1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test of the getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(3, freq.getSumFreq());
    }

    /**
     * Test of the getCount method.
     */
    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
        assertEquals(0, freq.getCount('a'));
    }

    /**
     * Test of the getPct method.
     */
    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(0.333, freq.getPct(1), 0.001);
        assertEquals(0.0, freq.getPct('a'), 0.001);
    }

    /**
     * Test of the getCumPct method.
     */
    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);
        assertEquals(0.333, freq.getCumPct(1), 0.001);
        assertEquals(0.667, freq.getCumPct(2), 0.001);
        assertEquals(1.0, freq.getCumPct(3), 0.001);
    }
}