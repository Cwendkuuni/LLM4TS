package org.apache.commons.math.stat.test;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Test class for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test for default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        Assert.assertEquals("Incorrect frequency count for value 1", 2, frequency.getCount(1));
        Assert.assertEquals("Incorrect frequency count for value 2", 1, frequency.getCount(2));
        Assert.assertEquals("Incorrect total frequency count", 3, frequency.getSumFreq());

        Assert.assertEquals("Incorrect percentage for value 1", 0.6666666666666667, frequency.getPct(1), 0.0001);
        Assert.assertEquals("Incorrect percentage for value 2", 0.3333333333333333, frequency.getPct(2), 0.0001);

        Assert.assertEquals("Incorrect cumulative frequency for value 1", 2, frequency.getCumFreq(1));
        Assert.assertEquals("Incorrect cumulative frequency for value 2", 3, frequency.getCumFreq(2));

        Assert.assertEquals("Incorrect cumulative percentage for value 1", 0.6666666666666667, frequency.getCumPct(1), 0.0001);
        Assert.assertEquals("Incorrect cumulative percentage for value 2", 1.0, frequency.getCumPct(2), 0.0001);
    }

    /**
     * Test for constructor with comparator.
     */
    @Test
    public void testComparatorConstructor() {
        Comparator comparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(2);
        frequency.addValue(1);

        Assert.assertEquals("Incorrect frequency count for value 1", 1, frequency.getCount(1));
        Assert.assertEquals("Incorrect frequency count for value 2", 2, frequency.getCount(2));
        Assert.assertEquals("Incorrect total frequency count", 3, frequency.getSumFreq());

        Assert.assertEquals("Incorrect percentage for value 1", 0.3333333333333333, frequency.getPct(1), 0.0001);
        Assert.assertEquals("Incorrect percentage for value 2", 0.6666666666666667, frequency.getPct(2), 0.0001);

        Assert.assertEquals("Incorrect cumulative frequency for value 1", 1, frequency.getCumFreq(1));
        Assert.assertEquals("Incorrect cumulative frequency for value 2", 3, frequency.getCumFreq(2));

        Assert.assertEquals("Incorrect cumulative percentage for value 1", 0.3333333333333333, frequency.getCumPct(1), 0.0001);
        Assert.assertEquals("Incorrect cumulative percentage for value 2", 1.0, frequency.getCumPct(2), 0.0001);
    }

    /**
     * Test for addValue method with different data types.
     */
    @Test
    public void testAddValueWithDifferentDataTypes() {
        Frequency frequency = new Frequency();

        // Test with int
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        Assert.assertEquals("Incorrect frequency count for value 1", 2, frequency.getCount(1));
        Assert.assertEquals("Incorrect frequency count for value 2", 1, frequency.getCount(2));
        Assert.assertEquals("Incorrect total frequency count", 3, frequency.getSumFreq());

        // Test with long
        frequency.addValue(3L);
        frequency.addValue(3L);

        Assert.assertEquals("Incorrect frequency count for value 1", 4, frequency.getCount(1));
        Assert.assertEquals("Incorrect frequency count for value 2", 2, frequency.getCount(2));
        Assert.assertEquals("Incorrect total frequency count", 6, frequency.getSumFreq());

        // Test with char
        frequency.addValue('a');
        frequency.addValue('a');

        Assert.assertEquals("Incorrect frequency count for value 'a'", 2, frequency.getCount('a'));
        Assert.assertEquals("Incorrect total frequency count", 4, frequency.getSumFreq());

        // Test with String
        try {
            frequency.addValue("test");
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for addValue method with duplicate values.
     */
    @Test
    public void testAddValueWithDuplicateValues() {
        Frequency frequency = new Frequency();

        // Test with int
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        Assert.assertEquals("Incorrect frequency count for value 1", 2, frequency.getCount(1));
        Assert.assertEquals("Incorrect frequency count for value 2", 1, frequency.getCount(2));
        Assert.assertEquals("Incorrect total frequency count", 3, frequency.getSumFreq());

        // Test with long
        frequency.addValue(3L);
        frequency.addValue(3L);

        Assert.assertEquals("Incorrect frequency count for value 1", 2, frequency.getCount(3L));
        Assert.assertEquals("Incorrect frequency count for value 2", 2, frequency.getCount(3L));
        Assert.assertEquals("Incorrect total frequency count", 4, frequency.getSumFreq());

        // Test with char
        frequency.addValue('a');
        frequency.addValue('a');

        Assert.assertEquals("Incorrect frequency count for value 'a'", 2, frequency.getCount('a'));
        Assert.assertEquals("Incorrect total frequency count", 2, frequency.getSumFreq());
    }

    /**
     * Test for addValue method with out-of-range values.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddValueWithOutOfRangeValues() {
        Frequency frequency = new Frequency();
        long maxLongValue = Long.MAX_VALUE;
        frequency.addValue(maxLongValue);
    }
}