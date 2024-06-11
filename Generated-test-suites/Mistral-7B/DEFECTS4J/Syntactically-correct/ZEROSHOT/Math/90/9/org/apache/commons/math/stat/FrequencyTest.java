package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Tests the Frequency class.
 */
public class FrequencyTest {

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
    }

    /**
     * Test the constructor with a comparator.
     */
    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(2);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 3 \t 0.6666666666666666 \t 1.0\n";
        assertEquals(expected, result);
    }

    /**
     * Test the addValue method with a null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(null);
    }

    /**
     * Test the addValue method with an uncomparable value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue('x');
    }

    /**
     * Test the addValue method with different types.
     */
    @Test
    public void testAddValueDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('a');
        long count1 = frequency.getCount(1);
        long count2 = frequency.getCount(2);
        long countA = frequency.getCount('a');
        assertEquals(3, count1);
        assertEquals(2, count2);
        assertEquals(2, countA);
    }

    /**
     * Test the addValue method with the same value multiple times.
     */
    @Test
    public void testAddValueSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    /**
     * Test the addValue method with different values.
     */
    @Test
    public void testAddValueDifferentValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        long count1 = frequency.getCount(1);
        long count2 = frequency.getCount(2);
        assertEquals(2, count1);
        assertEquals(1, count2);
    }

    /**
     * Test the clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test the getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        long sumFreq = frequency.getSumFreq();
        assertEquals(4, sumFreq);
    }

    /**
     * Test the getCount method with a null value.
     */
    @Test
    public void testGetCountNullValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCount(null));
    }

    /**
     * Test the getCount method with an uncomparable value.
     */
    @Test
    public void testGetCountUncomparableValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCount('x'));
    }

    /**
     * Test the getCount method with a value that is not in the frequency distribution.
     */
    @Test
    public void testGetCountNotInDistribution() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(2);
        assertEquals(0, count);
    }

    /**
     * Test the getCount method with a value that is in the frequency distribution.
     */
    @Test
    public void testGetCountInDistribution() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(2, count);
    }

    /**
     * Test the getPct method with a null value.
     */
    @Test
    public void testGetPctNullValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getPct(null));
    }

    /**
     * Test the getPct method with an uncomparable value.
     */
    @Test
    public void testGetPctUncomparableValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getPct('x'));
    }

    /**
     * Test the getPct method with a value that is not in the frequency distribution.
     */
    @Test
    public void testGetPctNotInDistribution() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        double pct = frequency.getPct(2);
        assertTrue(Double.isNaN(pct));
    }

    /**
     * Test the getPct method with a value that is in the frequency distribution.
     */
    @Test
    public void testGetPctInDistribution() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        double pct = frequency.getPct(1);
        assertEquals(0.5, pct, 0.0001);
    }

    /**
     * Test the getCumFreq method with a null value.
     */
    @Test
    public void testGetCumFreqNullValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCumFreq(null));
    }

    /**
     * Test the getCumFreq method with an uncomparable value.
     */
    @Test
    public void testGetCumFreqUncomparableValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCumFreq('x'));
    }

    /**
     * Test the getCumFreq method with a value that is less than the first value in the frequency distribution.
     */
    @Test
    public void testGetCumFreqLessThanFirstValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long cumFreq = frequency.getCumFreq(0);
        assertEquals(0, cumFreq);
    }

    /**
     * Test the getCumFreq method with a value that is greater than the last value in the frequency distribution.
     */
    @Test
    public void testGetCumFreqGreaterThanLastValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long cumFreq = frequency.getCumFreq(3);
        assertEquals(2, cumFreq);
    }

    /**
     * Test the getCumFreq method with a value that is in the frequency distribution.
     */
    @Test
    public void testGetCumFreqInDistribution() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(1, cumFreq);
    }

    /**
     * Test the getCumPct method with a null value.
     */
    @Test
    public void testGetCumPctNullValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCumPct(null));
    }

    /**
     * Test the getCumPct method with an uncomparable value.
     */
    @Test
    public void testGetCumPctUncomparableValue() {
        Frequency frequency = new Frequency();
        assertNull(frequency.getCumPct('x'));
    }

    /**
     * Test the getCumPct method with a value that is less than the first value in the frequency distribution.
     */
    @Test
    public void testGetCumPctLessThanFirstValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        double cumPct = frequency.getCumPct(0);
        assertTrue(Double.isNaN(cumPct));
    }

    /**
     * Test the getCumPct method with a value that is greater than the last value in the frequency distribution.
     */
    @Test
    public void testGetCumPctGreaterThanLastValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        double cumPct = frequency.getCumPct(3);
        assertEquals(1.0, cumPct, 0.0001);
    }

    /**
     * Test the getCumPct method with a value that is in the frequency distribution.
     */
    @Test
    public void testGetCumPctInDistribution() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.5, cumPct, 0.0001);
    }
}