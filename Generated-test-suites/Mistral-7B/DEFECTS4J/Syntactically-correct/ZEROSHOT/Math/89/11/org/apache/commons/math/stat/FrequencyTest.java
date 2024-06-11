package org.apache.commons.math.stat.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

import org.junit.Test;

/**
 * JUnit test file for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test constructor without comparator.
     */
    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
    }

    /**
     * Test constructor with comparator.
     */
    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
    }

    /**
     * Test addValue methods.
     */
    @Test
    public void testAddValueMethods() {
        Frequency frequency = new Frequency();

        // Add int value
        frequency.addValue(1);
        assertEquals(Long.valueOf(1), frequency.getCount(1));
        assertEquals(Long.valueOf(1), frequency.getCount(1L));
        assertEquals(Long.valueOf(1), frequency.getCount(Integer.valueOf(1)));
        assertEquals(Long.valueOf(1), frequency.getCount(Character.valueOf((char) 1)));

        // Add long value
        frequency.addValue(2L);
        assertEquals(Long.valueOf(2), frequency.getCount(2));
        assertEquals(Long.valueOf(2), frequency.getCount(2L));
        assertEquals(Long.valueOf(2), frequency.getCount(Long.valueOf(2)));
        assertEquals(Long.valueOf(2), frequency.getCount(Character.valueOf((char) 2)));

        // Add char value
        frequency.addValue('A');
        assertEquals(Long.valueOf(1), frequency.getCount('A'));
        assertEquals(Long.valueOf(1), frequency.getCount(Character.valueOf('A')));

        // Add same value multiple times
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(Long.valueOf(3), frequency.getCount(1));
        assertEquals(Long.valueOf(3), frequency.getCount(1L));
        assertEquals(Long.valueOf(3), frequency.getCount(Integer.valueOf(1)));
        assertEquals(Long.valueOf(3), frequency.getCount(Character.valueOf((char) 1)));

        // Add different types of values
        frequency.addValue(1);
        frequency.addValue('A');
        frequency.addValue(2L);
        assertEquals(Long.valueOf(1), frequency.getCount(1));
        assertEquals(Long.valueOf(1), frequency.getCount(1L));
        assertEquals(Long.valueOf(1), frequency.getCount(Integer.valueOf(1)));
        assertEquals(Long.valueOf(1), frequency.getCount(Character.valueOf((char) 1)));
        assertEquals(Long.valueOf(1), frequency.getCount('A'));
        assertEquals(Long.valueOf(1), frequency.getCount(Character.valueOf((char) 'A')));
        assertEquals(Long.valueOf(1), frequency.getCount(2L));
        assertEquals(Long.valueOf(1), frequency.getCount(Long.valueOf(2)));
        assertEquals(Long.valueOf(1), frequency.getCount(Long.valueOf(2L)));
        assertEquals(Long.valueOf(1), frequency.getCount(2));
        assertEquals(Long.valueOf(1), frequency.getCount(Integer.valueOf(2)));
        assertEquals(Long.valueOf(1), frequency.getCount(Integer.valueOf(2L)));
        assertEquals(Long.valueOf(1), frequency.getCount(Character.valueOf((char) 2)));
        assertEquals(Long.valueOf(1), frequency.getCount(Character.valueOf((char) '2')));

        // Test addValue with non-comparable values
        try {
            frequency.addValue("notComparable");
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test getCount methods.
     */
    @Test
    public void testGetCountMethods() {
        Frequency frequency = new Frequency();

        // Test getCount with int value
        frequency.addValue(1);
        assertEquals(Long.valueOf(1), frequency.getCount(1));
        assertEquals(Long.valueOf(1), frequency.getCount(1L));
        assertEquals(Long.valueOf(1), frequency.getCount(Integer.valueOf(1)));
        assertEquals(Long.valueOf(1), frequency.getCount(Character.valueOf((char) 1)));

        // Test getCount with long value
        frequency.addValue(2L);
        assertEquals(Long.valueOf(2), frequency.getCount(2));
        assertEquals(Long.valueOf(2), frequency.getCount(2L));
        assertEquals(Long.valueOf(2), frequency.getCount(Long.valueOf(2)));
        assertEquals(Long.valueOf(2), frequency.getCount(Character.valueOf((char) 2)));

        // Test getCount with char value
        frequency.addValue('A');
        assertEquals(Long.valueOf(1), frequency.getCount('A'));
        assertEquals(Long.valueOf(1), frequency.getCount(Character.valueOf('A')));

        // Test getCount with non-existent value
        assertEquals(Long.valueOf(0), frequency.getCount('B'));
        assertEquals(Long.valueOf(0), frequency.getCount(Character.valueOf((char) 'B')));

        // Test getCount with null
        assertNull(frequency.getCount(null));

        // Test getCount with Integer and Long values
        frequency.addValue(Integer.valueOf(1));
        frequency.addValue(1L);
        assertEquals(Long.valueOf(2), frequency.getCount(1));
        assertEquals(Long.valueOf(2), frequency.getCount(1L));
        assertEquals(Long.valueOf(2), frequency.getCount(Integer.valueOf(1)));
        assertEquals(Long.valueOf(2), frequency.getCount(Character.valueOf((char) 1)));
        assertEquals(Long.valueOf(2), frequency.getCount(Character.valueOf((char) '1')));

        // Test getCount with Long and Integer values
        frequency.addValue(1L);
        frequency.addValue(Integer.valueOf(1));
        assertEquals(Long.valueOf(2), frequency.getCount(1));
        assertEquals(Long.valueOf(2), frequency.getCount(1L));
        assertEquals(Long.valueOf(2), frequency.getCount(Long.valueOf(1)));
        assertEquals(Long.valueOf(2), frequency.getCount(Long.valueOf(1L)));
        assertEquals(Long.valueOf(2), frequency.getCount(Integer.valueOf(1)));
        assertEquals(Long.valueOf(2), frequency.getCount(Integer.valueOf(1L)));
        assertEquals(Long.valueOf(2), frequency.getCount(Character.valueOf((char) 1)));
        assertEquals(Long.valueOf(2), frequency.getCount(Character.valueOf((char) '1')));
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreqMethod() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(Long.valueOf(6), frequency.getSumFreq());
    }

    /**
     * Test getPct methods.
     */
    @Test
    public void testGetPctMethods() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        // Test getPct with int value
        assertEquals(Double.valueOf(0.3333), frequency.getPct(1));
        assertEquals(Double.valueOf(0.6667), frequency.getPct(2));

        // Test getPct with long value
        assertEquals(Double.valueOf(0.3333), frequency.getPct(1L));
        assertEquals(Double.valueOf(0.6667), frequency.getPct(2L));

        // Test getPct with Integer value
        assertEquals(Double.valueOf(0.3333), frequency.getPct(Integer.valueOf(1)));
        assertEquals(Double.valueOf(0.6667), frequency.getPct(Integer.valueOf(2)));

        // Test getPct with char value
        assertEquals(Double.valueOf(0.3333), frequency.getPct((char) 1));
        assertEquals(Double.valueOf(0.6667), frequency.getPct((char) 2));

        // Test getPct with non-existent value
        assertEquals(Double.valueOf(0.0), frequency.getPct('B'));
        assertEquals(Double.valueOf(0.0), frequency.getPct(Character.valueOf((char) 'B')));

        // Test getPct with null
        assertNull(frequency.getPct(null));
    }

    /**
     * Test getCumFreq methods.
     */
    @Test
    public void testGetCumFreqMethods() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        // Test getCumFreq with int value
        assertEquals(Long.valueOf(1), frequency.getCumFreq(1));
        assertEquals(Long.valueOf(1), frequency.getCumFreq(1L));
        assertEquals(Long.valueOf(1), frequency.getCumFreq(Integer.valueOf(1)));
        assertEquals(Long.valueOf(1), frequency.getCumFreq(Character.valueOf((char) 1)));

        // Test getCumFreq with long value
        assertEquals(Long.valueOf(1), frequency.getCumFreq(1L));
        assertEquals(Long.valueOf(2), frequency.getCumFreq(2));
        assertEquals(Long.valueOf(2), frequency.getCumFreq(2L));
        assertEquals(Long.valueOf(2), frequency.getCumFreq(Long.valueOf(2)));
        assertEquals(Long.valueOf(2), frequency.getCumFreq(Long.valueOf(2L)));
        assertEquals(Long.valueOf(2), frequency.getCumFreq(2));
        assertEquals(Long.valueOf(2), frequency.getCumFreq(Integer.valueOf(2)));
        assertEquals(Long.valueOf(2), frequency.getCumFreq(Integer.valueOf(2L)));
        assertEquals(Long.valueOf(2), frequency.getCumFreq(Character.valueOf((char) 2)));
        assertEquals(Long.valueOf(2), frequency.getCumFreq(Character.valueOf((char) '2')));

        // Test getCumFreq with non-existent value
        assertEquals(Long.valueOf(0), frequency.getCumFreq(3));
        assertEquals(Long.valueOf(0), frequency.getCumFreq(Character.valueOf((char) 3)));

        // Test getCumFreq with null
        assertNull(frequency.getCumFreq(null));
    }

    /**
     * Test getCumPct methods.
     */
    @Test
    public void testGetCumPctMethods() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        // Test getCumPct with int value
        assertEquals(Double.valueOf(0.3333), frequency.getCumPct(1));
        assertEquals(Double.valueOf(0.6667), frequency.getCumPct(2));

        // Test getCumPct with long value
        assertEquals(Double.valueOf(0.3333), frequency.getCumPct(1L));
        assertEquals(Double.valueOf(0.6667), frequency.getCumPct(2L));

        // Test getCumPct with Integer value
        assertEquals(Double.valueOf(0.3333), frequency.getCumPct(Integer.valueOf(1)));
        assertEquals(Double.valueOf(0.6667), frequency.getCumPct(Integer.valueOf(2)));

        // Test getCumPct with char value
        assertEquals(Double.valueOf(0.3333), frequency.getCumPct((char) 1));
        assertEquals(Double.valueOf(0.6667), frequency.getCumPct((char) 2));

        // Test getCumPct with non-existent value
        assertEquals(Double.valueOf(0.0), frequency.getCumPct('B'));
        assertEquals(Double.valueOf(0.0), frequency.getCumPct(Character.valueOf((char) 'B')));

        // Test getCumPct with null
        assertNull(frequency.getCumPct(null));
    }
}