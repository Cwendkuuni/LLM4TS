package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();
        // Test adding values of different types
        frequency.addValue(1);
        frequency.addValue("one");
        frequency.addValue('a');
        frequency.addValue(1.0);
        // Test adding the same value multiple times
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        // Test adding values out of order
        frequency.addValue(3);
        frequency.addValue(2);
        frequency.addValue(1);
        // Test adding a non-comparable value
        Assert.assertThrows(IllegalArgumentException.class, () -> frequency.addValue(new Object()));
        // Test getting the frequency of a value
        Assert.assertEquals(4, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount("one"));
        Assert.assertEquals(1, frequency.getCount('a'));
        Assert.assertEquals(1, frequency.getCount(1.0));
        // Test getting the cumulative frequency of a value
        Assert.assertEquals(4, frequency.getCumFreq(1));
        Assert.assertEquals(5, frequency.getCumFreq("one"));
        Assert.assertEquals(5, frequency.getCumFreq('a'));
        Assert.assertEquals(6, frequency.getCumFreq(1.0));
        // Test getting the cumulative percentage of a value
        Assert.assertNotNaN(frequency.getPct(1), "Pct of 1 is NaN");
        Assert.assertEquals(0.2, frequency.getPct(1.0), 0.001);
        Assert.assertNotNaN(frequency.getCumPct(1), "CumPct of 1 is NaN");
        Assert.assertEquals(0.2, frequency.getCumPct(1), 0.001);
        Assert.assertEquals(0.4, frequency.getCumPct(3), 0.001);
        Assert.assertEquals(1.0, frequency.getCumPct(Integer.MAX_VALUE), 0.001);
        // Test clearing the frequency distribution
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
    }

    @Test
    public void testFrequencyWithComparator() {
        Comparator<Object> comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        // Test adding values of different types with a custom comparator
        frequency.addValue(1);
        frequency.addValue("one");
        frequency.addValue('a');
        frequency.addValue(1.0);
        frequency.addValue(3);
        frequency.addValue(2);
        frequency.addValue(1);
        // Test getting the frequency of a value with a custom comparator
        Assert.assertEquals(4, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount("one"));
        Assert.assertEquals(1, frequency.getCount('a'));
        Assert.assertEquals(1, frequency.getCount(1.0));
        Assert.assertEquals(1, frequency.getCount(3));
        Assert.assertEquals(1, frequency.getCount(2));
        // Test getting the cumulative frequency of a value with a custom comparator
        Assert.assertEquals(4, frequency.getCumFreq(1));
        Assert.assertEquals(5, frequency.getCumFreq("one"));
        Assert.assertEquals(6, frequency.getCumFreq('a'));
        Assert.assertEquals(7, frequency.getCumFreq(1.0));
        Assert.assertEquals(8, frequency.getCumFreq(3));
        Assert.assertEquals(9, frequency.getCumFreq(2));
        // Test getting the cumulative percentage of a value with a custom comparator
        Assert.assertNotNaN(frequency.getPct(1), "Pct of 1 is NaN");
        Assert.assertEquals(0.2, frequency.getPct(1), 0.001);
        Assert.assertEquals(0.2, frequency.getCumPct(1), 0.001);
        Assert.assertEquals(0.4, frequency.getCumPct(3), 0.001);
        Assert.assertEquals(1.0, frequency.getCumPct(Integer.MAX_VALUE), 0.001);
    }
}