package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

import java.util.Comparator;
import java.util.TreeMap;

import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testDefaultFrequency() {
        Frequency frequency = new Frequency();

        // Test adding values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        // Test getCount
        assertEquals(3, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertNull(frequency.getCount('a'));

        // Test getPct
        assertEquals(0.6, frequency.getPct(1), 0.001);
        assertEquals(0.2, frequency.getPct(2), 0.001);
        assertNull(frequency.getPct('a'));

        // Test getCumFreq
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(4, frequency.getCumFreq(2));
        assertNull(frequency.getCumFreq('a'));

        // Test getCumPct
        assertEquals(0.6, frequency.getCumPct(1), 0.001);
        assertEquals(0.8, frequency.getCumPct(2), 0.001);
        assertNull(frequency.getCumPct('a'));

        // Test toString
        assertEquals("Value   Freq.   Pct.   Cum Pct.\n1        3       0.6        0.6\n2        1       0.2        0.8\n", frequency.toString());

        // Test clear
        frequency.clear();
        assertNull(frequency.getCount(1));
        assertNull(frequency.getCount(2));
    }

    @Test
    public void testFrequencyWithComparator() {
        Comparator<Comparable<?>> comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);

        // Test adding values
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(1);

        // Test getCount
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertNull(frequency.getCount('a'));

        // Test getPct
        assertEquals(0.4, frequency.getPct(1), 0.001);
        assertEquals(0.2, frequency.getPct(2), 0.001);
        assertNull(frequency.getPct('a'));

        // Test getCumFreq
        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertNull(frequency.getCumFreq('a'));

        // Test getCumPct
        assertEquals(0.4, frequency.getCumPct(1), 0.001);
        assertEquals(0.6, frequency.getCumPct(2), 0.001);
        assertNull(frequency.getCumPct('a'));

        // Test toString
        assertEquals("Value   Freq.   Pct.   Cum Pct.\n1        2       0.4        0.4\n2        1       0.2        0.6\n", frequency.toString());

        // Test addValue with uncomparable object
        assertThrows(IllegalArgumentException.class, () -> frequency.addValue("test"));
    }
}