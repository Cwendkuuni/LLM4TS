package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * Test class for Frequency class
 */
public class FrequencyTest {

    private Frequency frequency;
    private Frequency frequencyWithComparator;

    @Before
    public void setUp() {
        frequency = new Frequency();
        frequencyWithComparator = new Frequency(new NaturalComparator());
    }

    /**
     * Test adding values to an empty Frequency object
     */
    @Test
    public void testAddingValuesToEmptyFrequency() {
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Iterator values = frequency.valuesIterator();
        Assert.assertEquals(1, values.next());
        Assert.assertEquals(2, values.next());
        Assert.assertEquals(1, values.next());
        Assert.assertEquals(0, values.hasNext());
        Assert.assertEquals(3, frequency.getSumFreq());
    }

    /**
     * Test adding values to a Frequency object with existing values
     */
    @Test
    public void testAddingValuesToFrequencyWithExistingValues() {
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(1);
        Iterator values = frequency.valuesIterator();
        Assert.assertEquals(1, values.next());
        Assert.assertEquals(3, values.next());
        Assert.assertEquals(2, values.next());
        Assert.assertEquals(1, values.next());
        Assert.assertEquals(0, values.hasNext());
        Assert.assertEquals(7, frequency.getSumFreq());
    }

    /**
     * Test adding char value to Frequency object
     */
    @Test
    public void testAddingCharValueToFrequency() {
        frequency.addValue('A');
        frequency.addValue('B');
        frequency.addValue('A');
        Iterator values = frequency.valuesIterator();
        Assert.assertEquals('A', values.next());
        Assert.assertEquals('B', values.next());
        Assert.assertEquals('A', values.next());
        Assert.assertEquals(0, values.hasNext());
        Assert.assertEquals(3, frequency.getSumFreq());
    }

    /**
     * Test clearing Frequency object
     */
    @Test
    public void testClearingFrequency() {
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        Iterator values = frequency.valuesIterator();
        Assert.assertEquals(0, values.hasNext());
        Assert.assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test getting frequency of a value from Frequency object
     */
    @Test
    public void testGettingFrequencyOfValue() {
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        Assert.assertEquals(2, count);
        count = frequency.getCount(2);
        Assert.assertEquals(2, count);
        count = frequency.getCount('A');
        Assert.assertEquals(0, count);
    }

    /**
     * Test getting percentage of a value from Frequency object
     */
    @Test
    public void testGettingPercentageOfValue() {
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(1);
        double pct = frequency.getPct(1);
        Assert.assertEquals(0.4, pct, 0.001);
        pct = frequency.getPct(2);
        Assert.assertEquals(0.4, pct, 0.001);
        pct = frequency.getPct('A');
        Assert.assertEquals(0.0, pct, 0.001);
    }

    /**
     * Test getting cumulative frequency
     */
    @Test
    public void testGettingCumulativeFrequency() {
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(1);
        long cumulativeFreq = frequency.getCumFreq(1);
        Assert.assertEquals(3, cumulativeFreq);
        cumulativeFreq = frequency.getCumFreq(2);
        Assert.assertEquals(6, cumulativeFreq);
        cumulativeFreq = frequency.getCumFreq(3);
        Assert.assertEquals(7, cumulativeFreq);
        cumulativeFreq = frequency.getCumFreq('A');
        Assert.assertEquals(0, cumulativeFreq);
    }

    /**
     * Test getting cumulative percentage of a value
     */
    @Test
    public void testGettingCumulativePercentageOfValue() {
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(1);
        double cumulativePct = frequency.getCumPct(1);
        Assert.assertEquals(0.3, cumulativePct, 0.001);
        cumulativePct = frequency.getCumPct(2);
        Assert.assertEquals(0.6, cumulativePct, 0.001);
        cumulativePct = frequency.getCumPct(3);
        Assert.assertEquals(1.0, cumulativePct, 0.001);
        cumulativePct = frequency.getCumPct('A');
        Assert.assertEquals(0.0, cumulativePct, 0.001);
    }

    /**
     * Test adding values with comparator to Frequency object
     */
    @Test
    public void testAddingValuesWithComparatorToFrequency() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };
        frequencyWithComparator.addValue(2);
        frequencyWithComparator.addValue(1);
        frequencyWithComparator.addValue(1);
        Iterator values = frequencyWithComparator.valuesIterator();
        Assert.assertEquals(1, values.next());
        Assert.assertEquals(1, values.next());
        Assert.assertEquals(2, values.next());
        Assert.assertEquals(0, values.hasNext());
        Assert.assertEquals(3, frequencyWithComparator.getSumFreq());
    }

    /**
     * Test getting frequency, percentage, and cumulative frequency and percentage with comparator
     */
    @Test
    public void testGettingFrequencyPercentageCumulativeFrequencyAndPercentageWithComparator() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };
        frequencyWithComparator.addValue(2);
        frequencyWithComparator.addValue(1);
        frequencyWithComparator.addValue(1);
        long frequency = frequencyWithComparator.getCount(1);
        Assert.assertEquals(1, frequency);
        double percentage = frequencyWithComparator.getPct(1);
        Assert.assertEquals(0.33, percentage, 0.001);
        long cumulativeFrequency = frequencyWithComparator.getCumFreq(1);
        Assert.assertEquals(2, cumulativeFrequency);
        double cumulativePercentage = frequencyWithComparator.getCumPct(1);
        Assert.assertEquals(0.67, cumulativePercentage, 0.001);
    }

    /**
     * Test exception when adding uncomparable values to Frequency object
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenAddingUncomparableValuesToFrequency() {
        frequency.addValue("A");
        frequency.addValue(1);
    }

    /**
     * Test exception when adding the same uncomparable value twice to Frequency object
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenAddingTheSameUncomparableValueTwiceToFrequency() {
        frequency.addValue("A");
        frequency.addValue("A");
    }

    /**
     * Test exception when getting frequency, percentage, and cumulative frequency and percentage of uncomparable value from Frequency object
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenGettingFrequencyPercentageCumulativeFrequencyAndPercentageOfUncomparableValueFromFrequency() {
        frequency.addValue("A");
        Assert.assertEquals(0, frequency.getCount("A"));
        Assert.assertEquals(Double.NaN, frequency.getPct("A"), 0.001);
        Assert.assertEquals(0, frequency.getCumFreq("A"));
        Assert.assertEquals(Double.NaN, frequency.getCumPct("A"), 0.001);
    }

    /**
     * Test exception when adding null value to Frequency object
     */
    @Test(expected = NullPointerException.class)
    public void testExceptionWhenAddingNullValueToFrequency() {
        frequency.addValue(null);
    }

    /**
     * Test exception when getting frequency, percentage, and cumulative frequency and percentage of null value from Frequency object
     */
    @Test(expected = NullPointerException.class)
    public void testExceptionWhenGettingFrequencyPercentageCumulativeFrequencyAndPercentageOfNullValueFromFrequency() {
        frequency.addValue(null);
        Assert.assertEquals(0, frequency.getCount(null));
        Assert.assertEquals(Double.NaN, frequency.getPct(null), 0.001);
        Assert.assertEquals(0, frequency.getCumFreq(null));
        Assert.assertEquals(Double.NaN, frequency.getCumPct(null), 0.001);
    }
}