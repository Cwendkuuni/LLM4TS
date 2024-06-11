package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test of addValue method, of class Frequency.
     */
    @Test
    public void testAddValue() {
        Frequency instance = new Frequency();
        instance.addValue(1);
        instance.addValue(1);
        instance.addValue(2);
        instance.addValue(2);
        assertEquals("Total frequency count should be 5", 5, instance.getSumFreq());
        assertEquals("Count for value 1 should be 2", 2, instance.getCount(1));
        assertEquals("Count for value 2 should be 2", 2, instance.getCount(2));
        assertEquals("Count for value 3 should be 0", 0, instance.getCount(3));

        try {
            instance.addValue("not a number");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        instance.clear();
        instance.addValue(1.5);
        assertEquals("Count for value 1.5 should be 1", 1, instance.getCount(1.5));
        try {
            instance.addValue(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test of getCount method, of class Frequency.
     */
    @Test
    public void testGetCount() {
        Frequency instance = new Frequency();
        instance.addValue(1);
        instance.addValue(1);
        instance.addValue(2);
        instance.addValue(2);
        assertEquals("Count for value 1 should be 2", 2, instance.getCount(1));
        assertEquals("Count for value 2 should be 2", 2, instance.getCount(2));
        assertEquals("Count for value 3 should be 0", 0, instance.getCount(3));
    }

    /**
     * Test of getCumFreq method, of class Frequency.
     */
    @Test
    public void testGetCumFreq() {
        Frequency instance = new Frequency();
        instance.addValue(1);
        instance.addValue(1);
        instance.addValue(2);
        instance.addValue(2);
        assertEquals("Cumulative frequency for value 1 should be 2", 2, instance.getCumFreq(1));
        assertEquals("Cumulative frequency for value 2 should be 4", 4, instance.getCumFreq(2));
        assertEquals("Cumulative frequency for value 3 should be 0", 0, instance.getCumFreq(3));
    }

    /**
     * Test of getCumPct method, of class Frequency.
     */
    @Test
    public void testGetCumPct() {
        Frequency instance = new Frequency();
        instance.addValue(1);
        instance.addValue(1);
        instance.addValue(2);
        instance.addValue(2);
        assertEquals("Cumulative percentage for value 1 should be 0.4", 0.4, instance.getCumPct(1), 0.01);
        assertEquals("Cumulative percentage for value 2 should be 0.8", 0.8, instance.getCumPct(2), 0.01);
        assertEquals("Cumulative percentage for value 3 should be 0", 0, instance.getCumPct(3), 0.01);
    }

    /**
     * Test of valuesIterator method, of class Frequency.
     */
    @Test
    public void testValuesIterator() {
        Frequency instance = new Frequency();
        instance.addValue(1);
        instance.addValue(1);
        instance.addValue(2);
        instance.addValue(2);
        Frequency frequencies = new Frequency();
        frequencies.addValues(StatUtils.RAND.nextInt(10, 20));
        Iterator it = instance.valuesIterator();
        int i = 0;
        while (it.hasNext()) {
            Object value = it.next();
            assertEquals("Value " + i + " should be " + (i + 1), i + 1, value);
            i++;
        }
        assertFalse("Iterator should be empty", it.hasNext());

        it = frequencies.valuesIterator();
        i = 0;
        while (it.hasNext()) {
            Object value = it.next();
            assertTrue("Value " + i + " should be a number", value instanceof Number);
            i++;
        }
        assertFalse("Iterator should be empty", it.hasNext());
    }
}