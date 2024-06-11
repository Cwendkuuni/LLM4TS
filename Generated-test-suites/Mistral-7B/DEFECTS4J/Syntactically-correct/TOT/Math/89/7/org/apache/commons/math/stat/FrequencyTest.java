package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat. statistical.DescriptiveStatistics;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Frequency.
 */
public class FrequencyTest {

    /**
     * Test adding values to empty frequency distribution.
     */
    @Test
    public void testAddingValuesToEmptyFrequencyDistribution() {
        Frequency frequency = new Frequency();
        // Adding int values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals("Incorrect frequency count for value 1.", 2, frequency.getCount(1));
        assertEquals("Incorrect frequency count for value 2.", 1, frequency.getCount(2));
        // Adding long values
        frequency.clear();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        assertEquals("Incorrect frequency count for value 1 (long).", 2, frequency.getCount(1L));
        assertEquals("Incorrect frequency count for value 2 (long).", 1, frequency.getCount(2L));
        // Adding char values
        frequency.clear();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        assertEquals("Incorrect frequency count for value a (char).", 2, frequency.getCount('a'));
        assertEquals("Incorrect frequency count for value b (char).", 1, frequency.getCount('b'));
        // Adding Object values
        frequency.clear();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        assertEquals("Incorrect frequency count for value 1 (Object).", 2, frequency.getCount(1));
        assertEquals("Incorrect frequency count for value 2 (Object).", 1, frequency.getCount(2));
        // Adding invalid values
        try {
            frequency.addValue(new Object());
            fail("Expected IllegalArgumentException when adding a non-comparable value.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            frequency.addValue(new Double(1.5));
            fail("Expected IllegalArgumentException when adding a non-comparable value.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test adding values to frequency distribution with comparator.
     */
    @Test
    public void testAddingValuesToFrequencyDistributionWithComparator() {
        Comparator<Comparable<?>> comparator = (o1, o2) -> o1.compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        // Adding int values
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals("Incorrect frequency count for value 1.", 2, frequency.getCount(1));
        assertEquals("Incorrect frequency count for value 2.", 2, frequency.getCount(2));
        // Adding long values
        frequency.clear();
        frequency.addValue(2L);
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        assertEquals("Incorrect frequency count for value 1 (long).", 2, frequency.getCount(1L));
        assertEquals("Incorrect frequency count for value 2 (long).", 2, frequency.getCount(2L));
        // Adding char values
        frequency.clear();
        frequency.addValue('z');
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('z');
        frequency.addValue('a');
        assertEquals("Incorrect frequency count for value a (char).", 2, frequency.getCount('a'));
        assertEquals("Incorrect frequency count for value z (char).", 2, frequency.getCount('z'));
        // Adding Object values
        frequency.clear();
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        assertEquals("Incorrect frequency count for value 1 (Object).", 2, frequency.getCount(1));
        assertEquals("Incorrect frequency count for value 2 (Object).", 2, frequency.getCount(2));
    }

    /**
     * Test getting sum frequency.
     */
    @Test
    public void testGettingSumFrequency() {
        Frequency frequency = new Frequency();
        // Adding int values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals("Incorrect sum frequency.", 4, frequency.getSumFreq());
        // Adding long values
        frequency.clear();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        assertEquals("Incorrect sum frequency (long).", 4, frequency.getSumFreq());
        // Adding char values
        frequency.clear();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        assertEquals("Incorrect sum frequency (char).", 4, frequency.getSumFreq());
        // Adding Object values
        frequency.clear();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        assertEquals("Incorrect sum frequency (Object).", 4, frequency.getSumFreq());
    }

    /**
     * Test getting frequency of a specific value.
     */
    @Test
    public void testGettingFrequencyOfASpecificValue() {
        Frequency frequency = new Frequency();
        // Adding int values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals("Incorrect frequency of value 1.", 2, frequency.getCount(1));
        assertEquals("Incorrect frequency of value 2.", 1, frequency.getCount(2));
        // Adding long values
        frequency.clear();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        assertEquals("Incorrect frequency of value 1 (long).", 2, frequency.getCount(1L));
        assertEquals("Incorrect frequency of value 2 (long).", 1, frequency.getCount(2L));
        // Adding char values
        frequency.clear();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        assertEquals("Incorrect frequency of value a (char).", 2, frequency.getCount('a'));
        assertEquals("Incorrect frequency of value b (char).", 1, frequency.getCount('b'));
        // Adding Object values
        frequency.clear();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        assertEquals("Incorrect frequency of value 1 (Object).", 2, frequency.getCount(1));
        assertEquals("Incorrect frequency of value 2 (Object).", 1, frequency.getCount(2));
        // Testing getting frequency of non-existent value
        assertEquals("Incorrect frequency of non-existent value.", 0, frequency.getCount(5));
    }

    /**
     * Test getting percentage of a specific value.
     */
    @Test
    public void testGettingPercentageOfASpecificValue() {
        Frequency frequency = new Frequency();
        // Adding int values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double percentage = frequency.getPct(1);
        assertEquals("Incorrect percentage of value 1.", 0.5, percentage, 0.001);
        percentage = frequency.getPct(2);
        assertEquals("Incorrect percentage of value 2.", 0.25, percentage, 0.001);
        // Adding long values
        frequency.clear();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        percentage = frequency.getPct(1L);
        assertEquals("Incorrect percentage of value 1 (long).", 0.5, percentage, 0.001);
        percentage = frequency.getPct(2L);
        assertEquals("Incorrect percentage of value 2 (long).", 0.25, percentage, 0.001);
        // Adding char values
        frequency.clear();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        percentage = frequency.getPct('a');
        assertEquals("Incorrect percentage of value a (char).", 0.5, percentage, 0.001);
        percentage = frequency.getPct('b');
        assertEquals("Incorrect percentage of value b (char).", 0.25, percentage, 0.001);
        // Adding Object values
        frequency.clear();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        percentage = frequency.getPct(1);
        assertEquals("Incorrect percentage of value 1 (Object).", 0.5, percentage, 0.001);
        percentage = frequency.getPct(2);
        assertEquals("Incorrect percentage of value 2 (Object).", 0.25, percentage, 0.001);
        // Testing getting percentage of non-existent value
        assertEquals("Incorrect percentage of non-existent value.", 0.0, frequency.getPct(5), 0.001);
    }

    /**
     * Test getting cumulative frequency of a specific value.
     */
    @Test
    public void testGettingCumulativeFrequencyOfASpecificValue() {
        Frequency frequency = new Frequency();
        // Adding int values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        long cumulativeFrequency = frequency.getCumFreq(1);
        assertEquals("Incorrect cumulative frequency of value 1.", 1, cumulativeFrequency);
        cumulativeFrequency = frequency.getCumFreq(2);
        assertEquals("Incorrect cumulative frequency of value 2.", 2, cumulativeFrequency);
        // Adding long values
        frequency.clear();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        cumulativeFrequency = frequency.getCumFreq(1L);
        assertEquals("Incorrect cumulative frequency of value 1 (long).", 1, cumulativeFrequency);
        cumulativeFrequency = frequency.getCumFreq(2L);
        assertEquals("Incorrect cumulative frequency of value 2 (long).", 3, cumulativeFrequency);
        // Adding char values
        frequency.clear();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        cumulativeFrequency = frequency.getCumFreq('a');
        assertEquals("Incorrect cumulative frequency of value a (char).", 1, cumulativeFrequency);
        cumulativeFrequency = frequency.getCumFreq('b');
        assertEquals("Incorrect cumulative frequency of value b (char).", 2, cumulativeFrequency);
        // Adding Object values
        frequency.clear();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        cumulativeFrequency = frequency.getCumFreq(1);
        assertEquals("Incorrect cumulative frequency of value 1 (Object).", 1, cumulativeFrequency);
        cumulativeFrequency = frequency.getCumFreq(2);
        assertEquals("Incorrect cumulative frequency of value 2 (Object).", 3, cumulativeFrequency);
        // Testing getting cumulative frequency of non-existent value
        assertEquals("Incorrect cumulative frequency of non-existent value.", 0, frequency.getCumFreq(5));
    }

    /**
     * Test getting cumulative percentage of a specific value.
     */
    @Test
    public void testGettingCumulativePercentageOfASpecificValue() {
        Frequency frequency = new Frequency();
        // Adding int values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        double cumulativePercentage = frequency.getCumPct(1);
        assertEquals("Incorrect cumulative percentage of value 1.", 0.25, cumulativePercentage, 0.001);
        cumulativePercentage = frequency.getCumPct(2);
        assertEquals("Incorrect cumulative percentage of value 2.", 0.5, cumulativePercentage, 0.001);
        // Adding long values
        frequency.clear();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        cumulativePercentage = frequency.getCumPct(1L);
        assertEquals("Incorrect cumulative percentage of value 1 (long).", 0.25, cumulativePercentage, 0.001);
        cumulativePercentage = frequency.getCumPct(2L);
        assertEquals("Incorrect cumulative percentage of value 2 (long).", 0.5, cumulativePercentage, 0.001);
        // Adding char values
        frequency.clear();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');
        cumulativePercentage = frequency.getCumPct('a');
        assertEquals("Incorrect cumulative percentage of value a (char).", 0.25, cumulativePercentage, 0.001);
        cumulativePercentage = frequency.getCumPct('b');
        assertEquals("Incorrect cumulative percentage of value b (char).", 0.5, cumulativePercentage, 0.001);
        // Adding Object values
        frequency.clear();
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        cumulativePercentage = frequency.getCumPct(1);
        assertEquals("Incorrect cumulative percentage of value 1 (Object).", 0.25, cumulativePercentage, 0.001);
        cumulativePercentage = frequency.getCumPct(2);
        assertEquals("Incorrect cumulative percentage of value 2 (Object).", 0.5, cumulativePercentage, 0.001);
        // Testing getting cumulative percentage of non-existent value
        assertEquals("Incorrect cumulative percentage of non-existent value.", 0.0, frequency.getCumPct(5), 0.001);
    }
}