package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Test cases for Frequency class.
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
        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(0.67, frequency.getPct(1), 0.01);
        Assert.assertEquals(0.33, frequency.getPct(2), 0.01);
        Assert.assertEquals(0.33, frequency.getCumPct(1), 0.01);
        Assert.assertEquals(0.67, frequency.getCumPct(2), 0.01);
    }

    /**
     * Test constructor with custom Comparator.
     */
    @Test
    public void testCustomComparatorConstructor() {
        Comparator<Long> comparator = new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o2.compareTo(o1);
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(1L));
        Assert.assertEquals(1, frequency.getCount(2L));
        Assert.assertEquals(0.67, frequency.getPct(1L), 0.01);
        Assert.assertEquals(0.33, frequency.getPct(2L), 0.01);
        Assert.assertEquals(0.67, frequency.getCumPct(1L), 0.01);
        Assert.assertEquals(0.33, frequency.getCumPct(2L), 0.01);
    }

    /**
     * Test addValue with long values.
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(1L));
        Assert.assertEquals(1, frequency.getCount(2L));
        Assert.assertEquals(0.67, frequency.getPct(1L), 0.01);
        Assert.assertEquals(0.33, frequency.getPct(2L), 0.01);
        Assert.assertEquals(0.33, frequency.getCumPct(1L), 0.01);
        Assert.assertEquals(0.67, frequency.getCumPct(2L), 0.01);
    }

    /**
     * Test addValue with int values.
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(0.67, frequency.getPct(1), 0.01);
        Assert.assertEquals(0.33, frequency.getPct(2), 0.01);
        Assert.assertEquals(0.33, frequency.getCumPct(1), 0.01);
        Assert.assertEquals(0.67, frequency.getCumPct(2), 0.01);
    }

    /**
     * Test addValue with char values.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount('a'));
        Assert.assertEquals(1, frequency.getCount('b'));
        Assert.assertEquals(0.67, frequency.getPct('a'), 0.01);
        Assert.assertEquals(0.33, frequency.getPct('b'), 0.01);
        Assert.assertEquals(0.33, frequency.getCumPct('a'), 0.01);
        Assert.assertEquals(0.67, frequency.getCumPct('b'), 0.01);
    }

    /**
     * Test addValue with invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue(1);
    }

    /**
     * Test getCount with invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCountIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(0, frequency.getCount('a'));
    }

    /**
     * Test getPct with invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPctIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(Double.NaN, frequency.getPct('a'), 0.01);
    }

    /**
     * Test getCumPct with invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCumPctIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(Double.NaN, frequency.getCumPct('a'), 0.01);
    }

    /**
     * Test clear.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertEquals(0, frequency.getCount(1));
        Assert.assertEquals(0, frequency.getCount(2));
        Assert.assertEquals(0, frequency.getCount('a'));
    }
}