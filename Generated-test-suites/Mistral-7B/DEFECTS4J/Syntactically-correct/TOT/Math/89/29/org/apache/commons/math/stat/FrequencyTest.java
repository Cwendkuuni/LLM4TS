package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
        assertNull(freq.freqTable);
    }

    /**
     * Test constructor with comparator.
     */
    @Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
        assertNotNull(freq);
        assertNotNull(freq.freqTable);
        assertEquals(comparator, freq.freqTable.comparator());
    }

    /**
     * Test addValue method with valid inputs.
     */
    @Test
    public void testAddValidValues() {
        Frequency freq = new Frequency();

        // int
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(1.0, freq.getPct(1), 0.0);
        assertEquals(1.0, freq.getCumPct(1), 0.0);

        // long
        freq.addValue(100);
        assertEquals(1, freq.getCount(1));
        assertEquals(1, freq.getCumFreq(1));
        assertEquals(1.0 / 101.0, freq.getPct(1), 0.0);
        assertEquals(1.0 / 101.0, freq.getCumPct(1), 0.0);

        // char
        freq.clear();
        freq.addValue('A');
        assertEquals(1, freq.getCount('A'));
        assertEquals(1, freq.getCumFreq('A'));
        assertEquals(1.0 / 101.0, freq.getPct('A'), 0.0);
        assertEquals(1.0 / 101.0, freq.getCumPct('A'), 0.0);

        // add multiple values
        freq.clear();
        freq.addValue(1);
        freq.addValue(0);
        freq.addValue(1);
        assertEquals(2, freq.getCount(1));
        assertEquals(2, freq.getCumFreq(1));
        assertEquals(2.0 / 3.0, freq.getPct(1), 0.0);
        assertEquals(2.0 / 3.0, freq.getCumPct(1), 0.0);
        assertEquals(1.0 / 3.0, freq.getPct(0), 0.0);
        assertEquals(1.0 / 3.0, freq.getCumPct(0), 0.0);
    }

    /**
     * Test addValue method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidValues() {
        Frequency freq = new Frequency();

        // non-comparable value
        freq.addValue(new Object());

        // null value
        freq.addValue(null);
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.clear();
        assertNull(freq.freqTable);
    }

    /**
     * Test getSumFreq method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(3, freq.getSumFreq());
    }

    /**
     * Test valuesIterator method.
     */
    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(2);

        Iterator iterator = freq.valuesIterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().longValue());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().longValue());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test getCount method with valid inputs.
     */
    @Test
    public void testGetCountValidValues() {
        Frequency freq = new Frequency();

        // int
        freq.addValue(1);
        assertEquals(1, freq.getCount(1));

        // long
        freq.addValue(100);
        assertEquals(1, freq.getCount(1));

        // char
        freq.clear();
        freq.addValue('A');
        assertEquals(1, freq.getCount('A'));

        // multiple values
        freq.clear();
        freq.addValue(1);
        freq.addValue(0);
        freq.addValue(1);
        assertEquals(2, freq.getCount(1));
    }

    /**
     * Test getCount method with invalid inputs.
     */
    @Test
    public void testGetCountInvalidValues() {
        Frequency freq = new Frequency();

        // non-comparable value
        assertEquals(0, freq.getCount(new Object()));

        // null value
        assertEquals(0, freq.getCount(null));
    }

    /**
     * Test getPct method with valid inputs.
     */
    @Test
    public void testGetPctValidValues() {
        Frequency freq = new Frequency();

        // int
        freq.addValue(1);
        assertEquals(1.0 / 101.0, freq.getPct(1), 0.0);

        // long
        freq.addValue(100);
        assertEquals(1.0 / 101.0, freq.getPct(1), 0.0);

        // char
        freq.clear();
        freq.addValue('A');
        assertEquals(1.0 / 101.0, freq.getPct('A'), 0.0);

        // multiple values
        freq.clear();
        freq.addValue(1);
        freq.addValue(0);
        freq.addValue(1);
        assertEquals(2.0 / 3.0, freq.getPct(1), 0.0);
        assertEquals(1.0 / 3.0, freq.getPct(0), 0.0);
    }

    /**
     * Test getPct method with invalid inputs.
     */
    @Test
    public void testGetPctInvalidValues() {
        Frequency freq = new Frequency();

        // non-comparable value
        assertNull(freq.getPct(new Object()));

        // null value
        assertNull(freq.getPct(null));
    }

    /**
     * Test getCumFreq method with valid inputs.
     */
    @Test
    public void testGetCumFreqValidValues() {
        Frequency freq = new Frequency();

        // int
        freq.addValue(1);
        assertEquals(1, freq.getCumFreq(1));

        // long
        freq.addValue(100);
        assertEquals(1, freq.getCumFreq(1));

        // char
        freq.clear();
        freq.addValue('A');
        assertEquals(1, freq.getCumFreq('A'));

        // multiple values
        freq.clear();
        freq.addValue(1);
        freq.addValue(0);
        freq.addValue(1);
        assertEquals(2, freq.getCumFreq(1));
        assertEquals(1, freq.getCumFreq(0));
    }

    /**
     * Test getCumFreq method with invalid inputs.
     */
    @Test
    public void testGetCumFreqInvalidValues() {
        Frequency freq = new Frequency();

        // non-comparable value
        assertEquals(0, freq.getCumFreq(new Object()));

        // null value
        assertEquals(0, freq.getCumFreq(null));
    }

    /**
     * Test getCumPct method with valid inputs.
     */
    @Test
    public void testGetCumPctValidValues() {
        Frequency freq = new Frequency();

        // int
        freq.addValue(1);
        assertEquals(1.0 / 101.0, freq.getCumPct(1), 0.0);

        // long
        freq.addValue(100);
        assertEquals(1.0 / 101.0, freq.getCumPct(1), 0.0);

        // char
        freq.clear();
        freq.addValue('A');
        assertEquals(1.0 / 101.0, freq.getCumPct('A'), 0.0);

        // multiple values
        freq.clear();
        freq.addValue(1);
        freq.addValue(0);
        freq.addValue(1);
        assertEquals(2.0 / 3.0, freq.getCumPct(1), 0.0);
        assertEquals(1.0 / 3.0, freq.getCumPct(0), 0.0);
    }

    /**
     * Test getCumPct method with invalid inputs.
     */
    @Test
    public void testGetCumPctInvalidValues() {
        Frequency freq = new Frequency();

        // non-comparable value
        assertNull(freq.getCumPct(new Object()));

        // null value
        assertNull(freq.getCumPct(null));
    }
}