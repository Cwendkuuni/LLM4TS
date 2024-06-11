package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.StatFunctions;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Tests for {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();

        // Add values and check frequencies
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);

        Assert.assertEquals(3, frequency.getCount(1));
        Assert.assertEquals(3, frequency.getCount(1L));
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(2, frequency.getCount(2L));
        Assert.assertEquals(1, frequency.getCount(3));
        Assert.assertEquals(1, frequency.getCount(3L));

        // Test getSumFreq
        Assert.assertEquals(9L, frequency.getSumFreq());

        // Test getCumFreq and getCumPct
        Assert.assertEquals(3, frequency.getCumFreq(1));
        Assert.assertEquals(6, frequency.getCumFreq(2));
        Assert.assertEquals(9, frequency.getCumFreq(3));
        Assert.assertEquals(0.33333333333333333, frequency.getPct(1), 0.000001);
        Assert.assertEquals(0.66666666666666667, frequency.getPct(2), 0.000001);
        Assert.assertEquals(1, frequency.getPct(3), 0.000001);
        Assert.assertEquals(0.33333333333333333, frequency.getCumPct(1), 0.000001);
        Assert.assertEquals(0.66666666666666667, frequency.getCumPct(2), 0.000001);
        Assert.assertEquals(1, frequency.getCumPct(3), 0.000001);

        // Test toString
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "1        \t 3       \t 0.333\t\t 0.333\n" +
                "2        \t 2       \t 0.222\t\t 0.556\n" +
                "3        \t 1       \t 0.111\t\t 0.667\n";
        Assert.assertEquals(expected, frequency.toString());

        // Test addValue with non-Comparable object, IllegalArgumentException should be thrown
        try {
            frequency.addValue("test");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test addValue with non-comparable value types (char and int), IllegalArgumentException should be thrown
        try {
            frequency.addValue(1);
            frequency.addValue('a');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test clear
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertTrue(frequency.isEmpty());
    }

    /**
     * Test constructor with Comparator.
     */
    @Test
    public void testConstructorWithComparator() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };

        Frequency frequency = new Frequency(comparator);

        // Add values and check frequencies
        frequency.addValue(3);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        Assert.assertEquals(3, frequency.getCount(1));
        Assert.assertEquals(3, frequency.getCount(1L));
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(2, frequency.getCount(2L));
        Assert.assertEquals(1, frequency.getCount(3));
        Assert.assertEquals(1, frequency.getCount(3L));

        // Test getSumFreq
        Assert.assertEquals(9L, frequency.getSumFreq());

        // Test getCumFreq and getCumPct
        Assert.assertEquals(3, frequency.getCumFreq(1));
        Assert.assertEquals(6, frequency.getCumFreq(2));
        Assert.assertEquals(9, frequency.getCumFreq(3));
        Assert.assertEquals(0.33333333333333333, frequency.getPct(1), 0.000001);
        Assert.assertEquals(0.66666666666666667, frequency.getPct(2), 0.000001);
        Assert.assertEquals(1, frequency.getPct(3), 0.000001);
        Assert.assertEquals(0.33333333333333333, frequency.getCumPct(1), 0.000001);
        Assert.assertEquals(0.66666666666666667, frequency.getCumPct(2), 0.000001);
        Assert.assertEquals(1, frequency.getCumPct(3), 0.000001);

        // Test toString
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "1        \t 3       \t 0.333\t\t 0.333\n" +
                "2        \t 2       \t 0.222\t\t 0.556\n" +
                "3        \t 1       \t 0.111\t\t 0.667\n";
        Assert.assertEquals(expected, frequency.toString());

        // Test addValue with non-Comparable object, IllegalArgumentException should be thrown
        try {
            frequency.addValue("test");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test addValue with non-comparable value types (char and int), IllegalArgumentException should be thrown
        try {
            frequency.addValue(1);
            frequency.addValue('a');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test clear
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertTrue(frequency.isEmpty());
    }
}