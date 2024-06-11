package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * Test class for {@link Frequency}.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);

        Frequency frequencyComparator = new Frequency(new NaturalComparator());
        assertNotNull(frequencyComparator);
    }

    /**
     * Test method <code>toString()</code>.
     */
    @org.junit.Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        String expected = "Value       Freq.       Pct.       Cum Pct.\n" +
                "1         2        0.3333      0.3333\n" +
                "2         2        0.3333      0.6667\n";
        String actual = frequency.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test method <code>addValue(Object)</code> with comparable values.
     */
    @org.junit.Test
    public void testAddValueWithComparableValues() {
        Frequency frequency = new Frequency();

        // Add values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        // Test getCount
        assertEquals(3, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));

        // Test getPct
        assertEquals(0.3333, frequency.getPct(1), 0.0001);
        assertEquals(0.3333, frequency.getPct(2), 0.0001);

        // Test getCumFreq
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(5, frequency.getCumFreq(2));

        // Test getCumPct
        assertEquals(0.3333, frequency.getCumPct(1), 0.0001);
        assertEquals(0.6667, frequency.getCumPct(2), 0.0001);
    }

    /**
     * Test method <code>addValue(Object)</code> with non-comparable values.
     * <p>
     * Should throw {@link IllegalArgumentException}.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue("A");
        frequency.addValue(1);
    }

    /**
     * Test method <code>clear()</code>.
     */
    @org.junit.Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test method <code>valuesIterator()</code>.
     */
    @org.junit.Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        Iterator<Object> values = frequency.valuesIterator();
        Object value = values.next();
        assertEquals(1, value.hashCode());
        assertEquals(1, frequency.getCount(value));

        value = values.next();
        assertEquals(2, value.hashCode());
        assertEquals(2, frequency.getCount(value));

        assertFalse(values.hasNext());
    }

    /**
     * Test method <code>getSumFreq()</code>.
     */
    @org.junit.Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(6, frequency
                .getSumFreq());
    }
}