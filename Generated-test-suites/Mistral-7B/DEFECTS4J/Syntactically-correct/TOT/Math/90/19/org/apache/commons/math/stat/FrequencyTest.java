package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Tests for {@link Frequency}.
 */
public class FrequencyTest {

    /**
     * Test constructor with default comparator.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        Assert.assertEquals("Frequency should be equal to 4", 4, frequency.getSumFreq());
        Assert.assertEquals("Count of value 1 should be equal to 2", 2, frequency.getCount(1));
        Assert.assertEquals("Count of value 2 should be equal to 2", 2, frequency.getCount(2));
        Assert.assertEquals("Percentage of value 1 should be equal to 0.5", 0.5, frequency.getPct(1), 0.01);
        Assert.assertEquals("Percentage of value 2 should be equal to 0.5", 0.5, frequency.getPct(2), 0.01);
        Assert.assertEquals("Cumulative percentage of value 1 should be equal to 0.5", 0.5, frequency.getCumPct(1), 0.01);
        Assert.assertEquals("Cumulative percentage of value 2 should be equal to 1.0", 1.0, frequency.getCumPct(2), 0.01);
    }

    /**
     * Test constructor with custom comparator.
     */
    @Test
    public void testCustomComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable)o1).compareTo(o2) * -1;
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        Assert.assertEquals("Frequency should be equal to 4", 4, frequency.getSumFreq());
        Assert.assertEquals("Count of value 1 should be equal to 2", 2, frequency.getCount(1));
        Assert.assertEquals("Count of value 2 should be equal to 2", 2, frequency.getCount(2));
        Assert.assertEquals("Percentage of value 1 should be equal to 0.5", 0.5, frequency.getPct(1), 0.01);
        Assert.assertEquals("Percentage of value 2 should be equal to 0.5", 0.5, frequency.getPct(2), 0.01);
        Assert.assertEquals("Cumulative percentage of value 1 should be equal to 0.5", 0.5, frequency.getCumPct(1), 0.01);
        Assert.assertEquals("Cumulative percentage of value 2 should be equal to 1.0", 1.0, frequency.getCumPct(2), 0.01);

        Assert.assertEquals("ToString should print correct values",
                "Value \t Freq. \t Pct. \t Cum Pct.\n" +
                        "2 \t 2 \t 0.5 \t 0.5\n" +
                        "1 \t 2 \t 0.5 \t 1.0\n", frequency.toString());
    }

    /**
     * Test addition of invalid values (non-comparable).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue("Invalid value");
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        Assert.assertEquals("Frequency should be equal to 4", 4, frequency.getSumFreq());
        frequency.clear();

        Assert.assertEquals("Frequency should be empty after clear", 0, frequency.getSumFreq());
    }
}