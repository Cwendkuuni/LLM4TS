package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.apache.commons.math3.stat.Frequency.Value;
import org.junit.Test;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test method for {@link Frequency#Frequency()}.
     */
    @Test
    public void testFrequencyConstructor() {
        Frequency frequency = new Frequency();
        // Test empty frequency distribution
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.getCount(Integer.valueOf(1)));
        assertTrue(frequency.valuesIterator().isEmpty());
    }

    /**
     * Test method for {@link Frequency#Frequency(Comparator)}.
     */
    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator comparator = new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        // Test empty frequency distribution
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.getCount(Integer.valueOf(1)));
        assertTrue(frequency.valuesIterator().isEmpty());
    }

    /**
     * Test method for {@link Frequency#addValue(Object)}.
     */
    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        // Test adding values to frequency distribution
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(2));
        frequency.addValue(Integer.valueOf(2));
        frequency.addValue(Integer.valueOf(3));
        // Test frequency distribution properties
        assertEquals(6, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(Integer.valueOf(1)));
        assertEquals(2, frequency.getCount(Integer.valueOf(2)));
        assertEquals(1, frequency.getCount(Integer.valueOf(3)));
        assertEquals(0, frequency.getCount(Integer.valueOf(4)));
        assertEquals(1.0 / 6.0, frequency.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0 / 3.0, frequency.getPct(Integer.valueOf(2)), 0.001);
        assertEquals(1.0 / 6.0, frequency.getPct(Integer.valueOf(3)), 0.001);
        assertEquals(0.0, frequency.getPct(Integer.valueOf(4)), 0.001);
        // Test cumulative frequency distribution
        assertEquals(2, frequency.getCumFreq(Integer.valueOf(1)));
        assertEquals(4, frequency.getCumFreq(Integer.valueOf(2)));
        assertEquals(6, frequency.getCumFreq(Integer.valueOf(3)));
        assertEquals(6, frequency.getCumFreq(Integer.valueOf(4)));
        // Test cumulative percentage distribution
        assertEquals(2.0 / 6.0, frequency.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(4.0 / 6.0, frequency.getCumPct(Integer.valueOf(2)), 0.001);
        assertEquals(6.0 / 6.0, frequency.getCumPct(Integer.valueOf(3)), 0.001);
        assertEquals(1.0, frequency.getCumPct(Integer.valueOf(4)), 0.001);
    }

    /**
     * Test method for {@link Frequency#addValue(Object)}.
     * Test IllegalArgumentException when adding non-comparable objects.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    /**
     * Test method for {@link Frequency#addValue(Object)}.
     * Test ClassCastException when adding non-Long objects to TreeMap.
     */
    @Test(expected = ClassCastException.class)
    public void testAddValueClassCastException() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Value(1, "A"));
    }

    /**
     * Test method for {@link Frequency#clear()}.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        // Test adding values to frequency distribution
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(Integer.valueOf(2));
        frequency.addValue(Integer.valueOf(2));
        frequency.addValue(Integer.valueOf(3));
        // Test clearing frequency distribution
        frequency.clear();
        // Test empty frequency distribution
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.getCount(Integer.valueOf(1)));
        assertTrue(frequency.valuesIterator().isEmpty());
    }
}