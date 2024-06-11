package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.FrequencyComparator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Frequency.
 */
public class FrequencyTest {

    /**
     * Test default constructor and basic functionality.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        // Test size and values
        assertEquals(4, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1L));
        assertEquals(2, frequency.getCount(2L));
        assertEquals(0, frequency.getCount(3L));

        // Test percentages
        assertEquals(0.5, frequency.getPct(1L), 0.001);
        assertEquals(0.5, frequency.getPct(2L), 0.001);
        assertEquals(0.0, frequency.getPct(3L), 0.001);

        // Test cumulative frequencies
        assertEquals(2, frequency.getCumFreq(1L));
        assertEquals(4, frequency.getCumFreq(2L));
        assertEquals(4, frequency.getCumFreq(3L));

        // Test cumulative percentages
        assertEquals(0.5, frequency.getCumPct(1L), 0.001);
        assertEquals(1.0, frequency.getCumPct(2L), 0.001);
        assertEquals(1.0, frequency.getCumPct(3L), 0.001);
    }

    /**
     * Test constructor with custom Comparator.
     */
    @Test
    public void testCustomComparatorConstructor() {
        FrequencyComparator comparator = new FrequencyComparator();
        Frequency frequency = new Frequency(comparator);
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');
        frequency.addValue('a');

        // Test size and values
        assertEquals(4, frequency.getSumFreq());
        assertEquals(2, frequency.getCount('a'));
        assertEquals(2, frequency.getCount('b'));

        // Test percentages
        assertEquals(0.5, frequency.getPct('a'), 0.001);
        assertEquals(0.5, frequency.getPct('b'), 0.001);

        // Test cumulative frequencies
        assertEquals(2, frequency.getCumFreq('a'));
        assertEquals(4, frequency.getCumFreq('b'));

        // Test cumulative percentages
        assertEquals(0.5, frequency.getCumPct('a'), 0.001);
        assertEquals(1.0, frequency.getCumPct('b'), 0.001);
    }

    /**
     * Test addValue with incorrect types.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithIncorrectTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('a');
    }

    /**
     * Test addValue with values that are not comparable.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('a');
        frequency.addValue(3);
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }
}