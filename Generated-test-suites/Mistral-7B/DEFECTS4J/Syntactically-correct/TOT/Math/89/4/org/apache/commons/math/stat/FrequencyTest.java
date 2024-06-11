package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Frequency} class.
 */
public class FrequencyTest {

    private Frequency<Integer> frequency;
    private Frequency<String> frequencyString;

    /**
     * Initialize test objects.
     */
    @Before
    public void setUp() {
        frequency = new Frequency<>();
        frequencyString = new Frequency<>(String::compareTo);
    }

    /**
     * Test addValue method for Integer values.
     */
    @Test
    public void testAddValueInteger() {
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        // test getCount method
        assertEquals(3, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertNull(frequency.getCount(3));

        // test getPct method
        assertEquals(0.5, frequency.getPct(1), 0.01);
        assertEquals(0.25, frequency.getPct(2), 0.01);
        assertNull(frequency.getPct(3));

        // test getCumFreq method
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(3, frequency.getCumFreq(3));

        // test getCumPct method
        assertEquals(0.33, frequency.getCumPct(1), 0.01);
        assertEquals(0.67, frequency.getCumPct(2), 0.01);
        assertEquals(1, frequency.getCumPct(3), 0.01);
    }

    /**
     * Test addValue method for String values.
     */
    @Test
    public void testAddValueString() {
        frequencyString.addValue("A");
        frequencyString.addValue("B");
        frequencyString.addValue("A");

        // test getCount method
        assertEquals(2, frequencyString.getCount("A"));
        assertEquals(1, frequencyString.getCount("B"));
        assertNull(frequencyString.getCount("C"));

        // test getPct method
        assertEquals(0.67, frequencyString.getPct("A"), 0.01);
        assertEquals(0.33, frequencyString.getPct("B"), 0.01);
        assertNull(frequencyString.getPct("C"));

        // test getCumFreq method
        assertEquals(1, frequencyString.getCumFreq("A"));
        assertEquals(2, frequencyString.getCumFreq("B"));
        assertEquals(2, frequencyString.getCumFreq("A"));

        // test getCumPct method
        assertEquals(0.5, frequencyString.getCumPct("A"), 0.01);
        assertEquals(0.5, frequencyString.getCumPct("B"), 0.01);
        assertEquals(1, frequencyString.getCumPct("A"), 0.01); // cumulative percentage should be the same for "A"
    }

    /**
     * Test addValue method with non-Comparable object.
     */
    @Test
    public void testAddValueNonComparable() {
        assertThrows(IllegalArgumentException.class, () -> frequency.addValue(new Object()));
    }

    /**
     * Test clear method.
     */
    @Test
    public void testClear() {
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertNull(frequency.getCount(1));
    }
}