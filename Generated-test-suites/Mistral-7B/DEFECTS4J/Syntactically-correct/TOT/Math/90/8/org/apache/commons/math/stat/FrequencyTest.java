package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Tests for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test default constructor and basic functionality.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        // Check size and values
        Assert.assertEquals(4, frequency.values().size());
        Assert.assertEquals(1, frequency.getCount(1L));
        Assert.assertEquals(2, frequency.getCount(1L));
        Assert.assertEquals(1, frequency.getCount(2L));
        Assert.assertEquals(1, frequency.getCount(3L));

        // Check sum frequency, percentage, and cumulative percentage
        Assert.assertEquals(4, frequency.getSumFreq());
        Assert.assertEquals(0.5, frequency.getPct(1L), 0.01);
        Assert.assertEquals(0.375, frequency.getPct(1.5), 0.01); // edge case
        Assert.assertEquals(1.0, frequency.getCumPct(3L));
        Assert.assertEquals(0.25, frequency.getCumPct(1L), 0.01);

        // Check toString
        Assert.assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "1 \t 2 \t 0.5 \t 0.25\n" +
                "2 \t 1 \t 0.25 \t 0.5\n" +
                "3 \t 1 \t 0.25 \t 1.0\n", frequency.toString());

        // Test clearing
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertEquals(0, frequency.values().size());
    }

    /**
     * Test Comparator constructor and functionality.
     */
    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };

        Frequency frequency = new Frequency(comparator);
        frequency.addValue(3);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        // Check size and values
        Assert.assertEquals(4, frequency.values().size());
        Assert.assertEquals(1, frequency.getCount(1L));
        Assert.assertEquals(1, frequency.getCount(1L));
        Assert.assertEquals(1, frequency.getCount(2L));
        Assert.assertEquals(1, frequency.getCount(3L));

        // Check sum frequency, percentage, and cumulative percentage
        Assert.assertEquals(4, frequency.getSumFreq());
        Assert.assertEquals(0.375, frequency.getPct(1L), 0.01);
        Assert.assertEquals(0.375, frequency.getPct(1L), 0.01); // edge case
        Assert.assertEquals(1.0, frequency.getCumPct(3L));
        Assert.assertEquals(0.375, frequency.getCumPct(1L), 0.01);

        // Check toString
        Assert.assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "1 \t 2 \t 0.375 \t 0.375\n" +
                "2 \t 1 \t 0.25 \t 0.625\n" +
                "3 \t 1 \t 0.25 \t 1.0\n", frequency.toString());

        // Test clearing
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertEquals(0, frequency.values().size());
    }

    /**
     * Test addValue with IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        // Test adding non-comparable value (char)
        char c = 'a';
        frequency.addValue(c);
    }

    /**
     * Test addValue with ClassCastException.
     */
    @Test(expected = ClassCastException.class)
    public void testAddValueWithClassCastException() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        // Test adding non-Comparable value (TreeMap)
        TreeMap map = new TreeMap();
        frequency.addValue(map);
    }
}