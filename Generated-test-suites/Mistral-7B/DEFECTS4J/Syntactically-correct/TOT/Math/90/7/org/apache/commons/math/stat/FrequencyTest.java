package org.apache.commons.math.stat.test;

import java.util.Comparator;
import org.apache.commons.math.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Frequency class.
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
        Iterator<Object> iterator = frequency.valuesIterator();
        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals("Sum frequency is not correct", sumFreq, 6L);
        long value = 1;
        long count = frequency.getCount(value);
        Assert.assertEquals("Count for value 1 is not correct", count, 2L);
        double pct = frequency.getPct(value);
        Assert.assertEquals("Percentage for value 1 is not correct", pct, 1.0 / 3.0, 0.001);
        long cumFreq = frequency.getCumFreq(value);
        Assert.assertEquals("Cumulative frequency for value 1 is not correct", cumFreq, 2L);
        double cumPct = frequency.getCumPct(value);
        Assert.assertEquals("Cumulative percentage for value 1 is not correct", cumPct, 1.0 / 3.0, 0.001);
        value = 2;
        count = frequency.getCount(value);
        Assert.assertEquals("Count for value 2 is not correct", count, 2L);
        pct = frequency.getPct(value);
        Assert.assertEquals("Percentage for value 2 is not correct", pct, 1.0 / 3.0, 0.001);
        cumFreq = frequency.getCumFreq(value);
        Assert.assertEquals("Cumulative frequency for value 2 is not correct", cumFreq, 4L);
        cumPct = frequency.getCumPct(value);
        Assert.assertEquals("Cumulative percentage for value 2 is not correct", cumPct, 2.0 / 3.0, 0.001);
    }

    /**
     * Test constructor with custom comparator.
     */
    @Test
    public void testCustomComparatorConstructor() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<Long>) Long.valueOf(0L)).compareTo(Long.valueOf(1L));
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);
        Iterator<Object> iterator = frequency.valuesIterator();
        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals("Sum frequency is not correct", sumFreq, 6L);
        long value = 1L;
        long count = frequency.getCount(value);
        Assert.assertEquals("Count for value 1 is not correct", count, 2L);
        double pct = frequency.getPct(value);
        Assert.assertEquals("Percentage for value 1 is not correct", pct, 1.0 / 3.0, 0.001);
        long cumFreq = frequency.getCumFreq(value);
        Assert.assertEquals("Cumulative frequency for value 1 is not correct", cumFreq, 2L);
        double cumPct = frequency.getCumPct(value);
        Assert.assertEquals("Cumulative percentage for value 1 is not correct", cumPct, 1.0 / 3.0, 0.001);
        value = 2L;
        count = frequency.getCount(value);
        Assert.assertEquals("Count for value 2 is not correct", count, 2L);
        pct = frequency.getPct(value);
        Assert.assertEquals("Percentage for value 2 is not correct", pct, 1.0 / 3.0, 0.001);
        cumFreq = frequency.getCumFreq(value);
        Assert.assertEquals("Cumulative frequency for value 2 is not correct", cumFreq, 4L);
        cumPct = frequency.getCumPct(value);
        Assert.assertEquals("Cumulative percentage for value 2 is not correct", cumPct, 2.0 / 3.0, 0.001);
    }

    /**
     * Test addValue method with valid values.
     */
    @Test
    public void testAddValueValid() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals("Sum frequency is not correct", sumFreq, 6L);
    }

    /**
     * Test addValue method with invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalid() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
    }

    /**
     * Test addValue method with invalid value of different type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('a');
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
        frequency.clear();
        Iterator<Object> iterator = frequency.valuesIterator();
        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals("Sum frequency is not correct", sumFreq, 0L);
    }
}