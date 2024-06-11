package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);

        Frequency comparatorFrequency = new Frequency(new NaturalComparator());
        assertNotNull(comparatorFrequency);
    }

    /**
     * Test toString method when empty Frequency.
     */
    @Test
    public void testToStringEmpty() {
        Frequency frequency = new Frequency();
        assertEquals("", frequency.toString());
    }

    /**
     * Test addValue method with int value.
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1.0 / 3.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.001);
    }

    /**
     * Test addValue method with long value.
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1.0 / 3.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct(2), 0.001);
    }

    /**
     * Test addValue method with char value.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        assertEquals(3, frequency.getSumFreq());
        assertNull(frequency.getCount('b'));
        assertEquals(2, frequency.getCount('a'));
        assertEquals(2.0 / 3.0, frequency.getPct('a'), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct('a'), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct('a'), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct('b'), 0.001);
    }

    /**
     * Test addValue method with Object value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueObjectInvalid() {
        Frequency frequency = new Frequency();
        frequency.addValue("invalid");
    }

    /**
     * Test addValue method with Comparable value.
     */
    @Test
    public void testAddValueComparable() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(1.0 / 3.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct(2), 0.001);

        Frequency frequency2 = new Frequency();
        frequency2.addValue(2);
        frequency2.addValue(1);
        frequency2.addValue(1);

        Comparator comparator2 = new NaturalComparator();
        Frequency frequency3 = new Frequency(comparator2);
        frequency3.addAll(frequency2.values());

        assertEquals(3, frequency3.getSumFreq());
        assertEquals(1, frequency3.getCount(1));
        assertEquals(1.0 / 3.0, frequency3.getPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency3.getCumPct(1), 0.001);
        assertEquals(2.0 / 3.0, frequency3.getCumPct(2), 0.001);
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.getCount(1));
    }
}