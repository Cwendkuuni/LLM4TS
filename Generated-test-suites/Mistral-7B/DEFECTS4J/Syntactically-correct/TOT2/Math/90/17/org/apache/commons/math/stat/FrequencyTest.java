package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Tests for {@link Frequency} class
 */
public class FrequencyTest {

    /**
     * Test default constructor and add values
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(4, frequency.getSumFreq());
        Iterator<Long> values = frequency.valuesIterator();
        Long value = null;
        while (values.hasNext()) {
            value = values.next();
            assertEquals(2, value.longValue());
        }
    }

    /**
     * Test constructor with comparator and add values
     */
    @Test
    public void testComparatorConstructor() {
        Comparator<Object> comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals(4, frequency.getSumFreq());
        Iterator<Long> values = frequency.valuesIterator();
        Long value = null;
        while (values.hasNext()) {
            value = values.next();
            assertEquals(1L, value.longValue());
        }
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.5 \t 0.5 \n2 \t 2 \t 0.5 \t 1.0 \n";
        assertEquals(expected, frequency.toString());
    }

    /**
     * Test addValue with not comparable values
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotCompatibleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
    }

    /**
     * Test addValue with null value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(null);
    }

    /**
     * Test getCount method with compatible values
     */
    @Test
    public void testGetCountCompatibleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(0, frequency.getCount('a'));
    }

    /**
     * Test getCount method with not comparable values
     */
    @Test
    public void testGetCountNotCompatibleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('a');
        assertEquals(2, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
        assertEquals(0, frequency.getCount('a'));
    }

    /**
     * Test getCount method with null value
     */
    @Test
    public void testGetCountNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(null);
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(0, frequency.getCount(null));
    }

    /**
     * Test getPct method with compatible values
     */
    @Test
    public void testGetPctCompatibleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getPct(1), 0.0);
        assertEquals(0.5, frequency.getPct(2), 0.0);
        assertEquals(0.0, frequency.getPct('a'), 0.0);
    }

    /**
     * Test getPct method with not comparable values
     */
    @Test
    public void testGetPctNotCompatibleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('a');
        assertEquals(0.5, frequency.getPct(1), 0.0);
        assertEquals(0.0, frequency.getPct(2), 0.0);
        assertEquals(0.0, frequency.getPct('a'), 0.0);
    }

    /**
     * Test getPct method with null value
     */
    @Test
    public void testGetPctNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(null);
        assertEquals(0.5, frequency.getPct(1), 0.0);
        assertEquals(0.5, frequency.getPct(2), 0.0);
        assertEquals(0.0, frequency.getPct(null), 0.0);
    }

    /**
     * Test getCumFreq method with compatible values
     */
    @Test
    public void testGetCumFreqCompatibleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(4, frequency.getCumFreq('a'));
    }

    /**
     * Test getCumFreq method with not comparable values
     */
    @Test
    public void testGetCumFreqNotCompatibleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('a');
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq(2));
        assertEquals(4, frequency.getCumFreq('a'));
    }

    /**
     * Test getCumFreq method with null value
     */
    @Test
    public void testGetCumFreqNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(null);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getCumFreq(2));
        assertEquals(4, frequency.getCumFreq(null));
    }

    /**
     * Test getCumPct method with compatible values
     */
    @Test
    public void testGetCumPctCompatibleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertEquals(0.5, frequency.getCumPct(1), 0.0);
        assertEquals(1.0, frequency.getCumPct(2), 0.0);
        assertEquals(1.0, frequency.getCumPct('a'), 0.0);
    }

    /**
     * Test getCumPct method with not comparable values
     */
    @Test
    public void testGetCumPctNotCompatibleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('a');
        assertEquals(0.5, frequency.getCumPct(1), 0.0);
        assertEquals(0.5, frequency.getCumPct(2), 0.0);
        assertEquals(1.0, frequency.getCumPct('a'), 0.0);
    }

    /**
     * Test getCumPct method with null value
     */
    @Test
    public void testGetCumPctNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(null);
        assertEquals(0.5, frequency.getCumPct(1), 0.0);
        assertEquals(0.5, frequency.getCumPct(2), 0.0);
        assertEquals(1.0, frequency.getCumPct(null), 0.0);
    }
}