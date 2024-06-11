package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests the Frequency class.
 */
public class FrequencyTest {

    /**
     * Test empty frequency distribution.
     */
    @Test
    public void testEmptyFrequencyDistribution() {
        Frequency freq = new Frequency();
        assertEquals("Empty frequency distribution should have size zero.", 0, freq.size());
        assertTrue("Empty frequency distribution should be empty.", freq.isEmpty());
        assertEquals("Empty frequency distribution should have sum frequency 0.", 0, freq.getSumFreq());
    }

    /**
     * Test frequency distribution with single value.
     */
    @Test
    public void testFrequencyDistributionWithSingleValue() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        assertEquals("Frequency distribution with single value should have size one.", 1, freq.size());
        assertFalse("Frequency distribution with single value should not be empty.", freq.isEmpty());
        assertEquals("Frequency distribution with single value should have sum frequency 1.", 1, freq.getSumFreq());
        assertEquals("Frequency distribution with single value should have count 1.", 1, freq.getCount(5));
        assertEquals("Frequency distribution with single value should have percentage 100%.", 1.0, freq.getPct(5), 0.001);
        assertEquals("Frequency distribution with single value should have cumulative frequency 1.", 1, freq.getCumFreq(5));
        assertEquals("Frequency distribution with single value should have cumulative percentage 100%.", 1.0, freq.getCumPct(5), 0.001);
    }

    /**
     * Test frequency distribution with multiple values.
     */
    @Test
    public void testFrequencyDistributionWithMultipleValues() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(2);
        freq.addValue(5);
        freq.addValue(7);
        freq.addValue(2);
        freq.addValue(5);
        assertEquals("Frequency distribution with multiple values should have size six.", 6, freq.size());
        assertFalse("Frequency distribution with multiple values should not be empty.", freq.isEmpty());
        assertEquals("Frequency distribution with multiple values should have sum frequency 15.", 15, freq.getSumFreq());
        assertEquals("Frequency distribution with multiple values should have count 3 for value 2.", 3, freq.getCount(2));
        assertEquals("Frequency distribution with multiple values should have count 4 for value 5.", 4, freq.getCount(5));
        assertEquals("Frequency distribution with multiple values should have percentage 20% for value 2.", 0.2, freq.getPct(2), 0.01);
        assertEquals("Frequency distribution with multiple values should have percentage 40% for value 5.", 0.4, freq.getPct(5), 0.01);
        assertEquals("Frequency distribution with multiple values should have cumulative frequency 5 for value 2.", 5, freq.getCumFreq(2));
        assertEquals("Frequency distribution with multiple values should have cumulative frequency 11 for value 5.", 11, freq.getCumFreq(5));
        assertEquals("Frequency distribution with multiple values should have cumulative percentage 20% for value 2.", 0.2, freq.getCumPct(2), 0.01);
        assertEquals("Frequency distribution with multiple values should have cumulative percentage 60% for value 5.", 0.6, freq.getCumPct(5), 0.01);
    }

    /**
     * Test frequency distribution with duplicate values.
     */
    @Test
    public void testFrequencyDistributionWithDuplicateValues() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(5);
        freq.addValue(5);
        assertEquals("Frequency distribution with duplicate values should have size three.", 3, freq.size());
        assertFalse("Frequency distribution with duplicate values should not be empty.", freq.isEmpty());
        assertEquals("Frequency distribution with duplicate values should have sum frequency 15.", 15, freq.getSumFreq());
        assertEquals("Frequency distribution with duplicate values should have count 3 for value 5.", 3, freq.getCount(5));
        assertEquals("Frequency distribution with duplicate values should have percentage 60% for value 5.", 0.6, freq.getPct(5), 0.01);
        assertEquals("Frequency distribution with duplicate values should have cumulative frequency 3 for value 5.", 3, freq.getCumFreq(5));
        assertEquals("Frequency distribution with duplicate values should have cumulative percentage 60% for value 5.", 0.6, freq.getCumPct(5), 0.01);
    }

    /**
     * Test frequency distribution with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyDistributionWithNullValue() {
        Frequency freq = new Frequency();
        freq.addValue(null);
    }

    /**
     * Test frequency distribution with non-comparable values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyDistributionWithNonComparableValues() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue(new Object());
    }

    /**
     * Test frequency distribution with values of different types.
     */
    @Test
    public void testFrequencyDistributionWithValuesOfDifferentTypes() {
        Frequency freq = new Frequency();
        freq.addValue(5);
        freq.addValue('A');
        assertEquals("Frequency distribution with values of different types should have size two.", 2, freq.size());
        assertFalse("Frequency distribution with values of different types should not be empty.", freq.isEmpty());
        assertEquals("Frequency distribution with values of different types should have sum frequency 1.", 1, freq.getSumFreq());
        assertEquals("Frequency distribution with values of different types should have count 1 for value 5.", 1, freq.getCount(5));
        assertEquals("Frequency distribution with values of different types should have count 1 for value A.", 1, freq.getCount('A'));
        assertEquals("Frequency distribution with values of different types should have percentage 50% for value 5.", 0.5, freq.getPct(5), 0.01);
        assertEquals("Frequency distribution with values of different types should have percentage 50% for value A.", 0.5, freq.getPct('A'), 0.01);
        assertEquals("Frequency distribution with values of different types should have cumulative frequency 1 for value 5.", 1, freq.getCumFreq(5));
        assertEquals("Frequency distribution with values of different types should have cumulative frequency 1 for value A.", 1, freq.getCumFreq('A'));
        assertEquals("Frequency distribution with values of different types should have cumulative percentage 50% for value 5.", 0.5, freq.getCumPct(5), 0.01);
        assertEquals("Frequency distribution with values of different types should have cumulative percentage 50% for value A.", 0.5, freq.getCumPct('A'), 0.01);
    }

    /**
     * Test frequency distribution with values of different types, but comparable.
     */
    @Test
    public void testFrequencyDistributionWithValuesOfDifferentTypesButComparable() {
        Frequency freq = new Frequency(new NaturalComparator());
        freq.addValue(5);
        freq.addValue('A');
        assertEquals("Frequency distribution with values of different types, but comparable should have size two.", 2, freq.size());
        assertFalse("Frequency distribution with values of different types, but comparable should not be empty.", freq.isEmpty());
        assertEquals("Frequency distribution with values of different types, but comparable should have sum frequency 1.", 1, freq.getSumFreq());
        assertEquals("Frequency distribution with values of different types, but comparable should have count 1 for value 5.", 1, freq.getCount(5));
        assertEquals("Frequency distribution with values of different types, but comparable should have count 1 for value A.", 1, freq.getCount('A'));
        assertEquals("Frequency distribution with values of different types, but comparable should have percentage 50% for value 5.", 0.5, freq.getPct(5), 0.01);
        assertEquals("Frequency distribution with values of different types, but comparable should have percentage 50% for value A.", 0.5, freq.getPct('A'), 0.01);
        assertEquals("Frequency distribution with values of different types, but comparable should have cumulative frequency 1 for value 5.", 1, freq.getCumFreq(5));
        assertEquals("Frequency distribution with values of different types, but comparable should have cumulative frequency 1 for value A.", 1, freq.getCumFreq('A'));
        assertEquals("Frequency distribution with values of different types, but comparable should have cumulative percentage 50% for value 5.", 0.5, freq.getCumPct(5), 0.01);
        assertEquals("Frequency distribution with values of different types, but comparable should have cumulative percentage 50% for value A.", 0.5, freq.getCumPct('A'), 0.01);
    }

    /**
     * Test frequency distribution with empty comparator.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyDistributionWithEmptyComparator() {
        Frequency freq = new Frequency((Comparator)null);
    }

    /**
     * Test frequency distribution with comparator that throws exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyDistributionWithComparatorThatThrowsException() {
        Frequency freq = new Frequency(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                throw new RuntimeException();
            }
        });
    }

    /**
     * Test frequency distribution with custom comparator.
     */
    @Test
    public void testFrequencyDistributionWithCustomComparator() {
        Comparator<Comparable<?>> comparator = new Comparator<Comparable<?>>() {
            @Override
            public int compare(Comparable<?> o1, Comparable<?> o2) {
                return o1.compareTo(o2);
            }
        };
        Frequency freq = new Frequency(comparator);
        freq.addValue(new Integer(5));
        freq.addValue(new Integer(2));
        freq.addValue(new Integer(5));
        freq.addValue(new Integer(7));
        freq.addValue(new Integer(2));
        freq.addValue(new Integer(5));
        List<Comparable<?>> keys = Collections.list(freq.keySet());
        Comparable<?> key1 = keys.get(0);
        Comparable<?> key2 = keys.get(1);
        Comparable<?> key3 = keys.get(2);
        Comparable<?> key4 = keys.get(3);
        Comparable<?> key5 = keys.get(4);
        Comparable<?> key6 = keys.get(5);
        assertEquals("Frequency distribution with custom comparator should have size six.", 6, freq.size());
        assertFalse("Frequency distribution with custom comparator should not be empty.", freq.isEmpty());
        assertEquals("Frequency distribution with custom comparator should have sum frequency 15.", 15, freq.getSumFreq());
        assertEquals("Frequency distribution with custom comparator should have count 3 for value 2.", 3, freq.getCount(new Integer(2)));
        assertEquals("Frequency distribution with custom comparator should have count 4 for value 5.", 4, freq.getCount(new Integer(5)));
        assertEquals("Frequency distribution with custom comparator should have percentage 20% for value 2.", 0.2, freq.getPct(new Integer(2)), 0.01);
        assertEquals("Frequency distribution with custom comparator should have percentage 40% for value 5.", 0.4, freq.getPct(new Integer(5)), 0.01);
        assertEquals("Frequency distribution with custom comparator should have cumulative frequency 5 for value 2.", 5, freq.getCumFreq(new Integer(2)));
        assertEquals("Frequency distribution with custom comparator should have cumulative frequency 11 for value 5.", 11, freq.getCumFreq(new Integer(5)));
        assertEquals("Frequency distribution with custom comparator should have cumulative percentage 20% for value 2.", 0.2, freq.getCumPct(new Integer(2)), 0.01);
        assertEquals("Frequency distribution with custom comparator should have cumulative percentage 60% for value 5.", 0.6, freq.getCumPct(new Integer(5)), 0.01);
        assertEquals("Values in frequency distribution with custom comparator should be in the correct order.", key1, key2);
        assertEquals("Values in frequency distribution with custom comparator should be in the correct order.", key2, key3);
        assertEquals("Values in frequency distribution with custom comparator should be in the correct order.", key3, key4);
        assertEquals("Values in frequency distribution with custom comparator should be in the correct order.", key4, key5);
        assertEquals("Values in frequency distribution with custom comparator should be in the correct order.", key5, key6);
    }
}