package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * Tests the {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test the default constructor and basic methods.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.freqTable);
        frequency.addValue(1);
        assertEquals(1, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1.0, frequency.getPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(1), 1.0, 0.001);
        assertArrayEquals(new Object[] { 1 }, frequency.valuesIterator().toArray());
    }

    /**
     * Test the constructor with a Comparator.
     */
    @org.junit.Test
    public void testComparatorConstructor() {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        };
        Frequency frequency = new Frequency(comparator);
        assertEquals(0, frequency.getSumFreq());
        assertNotNull(frequency.freqTable);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(3, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(2.0, frequency.getPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 1.0, 0.001);
        assertArrayEquals(new Object[] { 2, 1, 1 }, frequency.valuesIterator().toArray());
    }

    /**
     * Test the addValue method with different types.
     */
    @org.junit.Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        frequency.addValue('a');
        assertEquals(1, frequency.getCount('a'));
        frequency.addValue("test");
        assertEquals(1, frequency.getCount("test"));
        frequency.addValue(new Integer(2));
        assertEquals(1, frequency.getCount(2));
        frequency.addValue(new Long(3));
        assertEquals(1, frequency.getCount(3));
        frequency.addValue(new Double(4.5));
        assertEquals(1, frequency.getCount(4.5));
    }

    /**
     * Test the addValue method with illegal arguments.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArguments() {
        Frequency frequency = new Frequency();
        frequency.addValue(null);
    }

    /**
     * Test the getSumFreq, getCount, getPct, and getCumPct methods with different types.
     */
    @org.junit.Test
    public void testGetMethods() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue("test");
        frequency.addValue(new Integer(2));
        frequency.addValue(new Long(3));
        frequency.addValue(new Double(4.5));
        assertEquals(6, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(1), 1.0 / 6.0, 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(2), 2.0 / 6.0, 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(3), 3.0 / 6.0, 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct('a'), 4.0 / 6.0, 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct("test"), 5.0 / 6.0, 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(new Integer(2)), 5.5 / 6.0, 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(new Long(3)), 6.0 / 6.0, 0.001);
        assertEquals(1.0 / 6.0, frequency.getCumPct(new Double(4.5)), 6.5 / 6.0, 0.001);
    }

    /**
     * Test the clear method.
     */
    @org.junit.Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.freqTable);
    }

    /**
     * Test the valuesIterator method.
     */
    @org.junit.Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
    }
}