package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * Tests for {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test method for {@link Frequency#Frequency()}.
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        Frequency freq = new Frequency();
        assertNotNull(freq);
    }

    /**
     * Test method for {@link Frequency#Frequency(Comparator)}.
     */
    @org.junit.Test
    public void testComparatorConstructor() {
        Comparator comparator = new NaturalComparator();
        Frequency freq = new Frequency(comparator);
        assertNotNull(freq);
    }

    /**
     * Test method for {@link Frequency#toString()}.
     */
    @org.junit.Test
    public void testToString() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        String result = freq.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 33.33% \t 33.33% \n2 \t 1 \t 16.67% \t 50.00% \n";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link Frequency#addValue(Object)}.
     */
    @org.junit.Test
    public void testAddValue() {
        Frequency freq = new Frequency();
        freq.addValue("a");
        assertNotNull(freq.getCount("a"));
        freq.addValue("a");
        assertEquals(2, freq.getCount("a"));

        freq.addValue(1);
        assertEquals(2, freq.getCount("a"));
        assertEquals(1, freq.getCount(1));

        try {
            freq.addValue(new Object() {});
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            freq.addValue('a');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Frequency freq2 = new Frequency(new NaturalComparator());
            freq2.addValue(1.5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Frequency freq3 = new Frequency(new NaturalComparator());
            freq3.addValue('z');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link Frequency#addValue(int)}.
     */
    @org.junit.Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(3, freq.getCount(1));

        Frequency freq2 = new Frequency();
        freq2.addValue(1);
        freq2.addValue(2);
        assertEquals(2, freq2.getCount(1));
        assertEquals(1, freq2.getCount(2));
    }

    /**
     * Test method for {@link Frequency#addValue(long)}.
     */
    @org.junit.Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(3, freq.getCount(1));

        Frequency freq2 = new Frequency();
        freq2.addValue(1);
        freq2.addValue(2);
        assertEquals(2, freq2.getCount(1));
        assertEquals(1, freq2.getCount(2));
    }

    /**
     * Test method for {@link Frequency#addValue(char)}.
     */
    @org.junit.Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        assertEquals(2, freq.getCount('a'));

        Frequency freq2 = new Frequency();
        freq2.addValue('a');
        freq2.addValue('b');
        assertEquals(1, freq2.getCount('a'));
        assertEquals(1, freq2.getCount('b'));
    }

    /**
     * Test method for {@link Frequency#clear()}.
     */
    @org.junit.Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.clear();
        assertNull(freq.getCount(1));
    }

    /**
     * Test method for {@link Frequency#valuesIterator()}.
     */
    @org.junit.Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        Iterator it = freq.valuesIterator();
        assertEquals(1, it.next());
        assertEquals(1, it.next());
        assertEquals(2, it.next());
        assertNull(it.hasNext());
    }

    /**
     * Test method for {@link Frequency#getSumFreq()}.
     */
    @org.junit.Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        assertEquals(4, freq.getSumFreq());
    }

    /**
     * Test method for {@link Frequency#getCount(Object)}.
     */
    @org.junit.Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(3, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertNull(freq.getCount('z'));
    }

    /**
     * Test method for {@link Frequency#getCount(int)}.
     */
    @org.junit.Test
    public void testGetCountInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(3, freq.getCount(1));
        assertEquals(1, freq.getCount(2));
        assertNull(freq.getCount(3));
    }

    /**
     * Test method for {@link Frequency#getCount(char)}.
     */
    @org.junit.Test
    public void testGetCountChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');

        assertEquals(2, freq.getCount('a'));
        assertEquals(1, freq.getCount('b'));
        assertNull(freq.getCount('z'));
    }

    /**
     * Test method for {@link Frequency#getPct(Object)}.
     */
    @org.junit.Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(50.0, freq.getPct(1), 0.01);
        assertEquals(25.0, freq.getPct(2), 0.01);
        assertNull(freq.getPct('z'));
    }

    /**
     * Test method for {@link Frequency#getPct(int)}.
     */
    @org.junit.Test
    public void testGetPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(50.0, freq.getPct(1), 0.01);
        assertEquals(25.0, freq.getPct(2), 0.01);
        assertNull(freq.getPct(3));
    }

    /**
     * Test method for {@link Frequency#getPct(char)}.
     */
    @org.junit.Test
    public void testGetPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');

        assertEquals(50.0, freq.getPct('a'), 0.01);
        assertEquals(25.0, freq.getPct('b'), 0.01);
        assertNull(freq.getPct('z'));
    }

    /**
     * Test method for {@link Frequency#getCumFreq(Object)}.
     */
    @org.junit.Test
    public void testGetCumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(1, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(2));
        assertEquals(3, freq.getCumFreq(3));
        assertEquals(4, freq.getCumFreq(4));
        assertNull(freq.getCumFreq('z'));
    }

    /**
     * Test method for {@link Frequency#getCumFreq(int)}.
     */
    @org.junit.Test
    public void testGetCumFreqInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(1, freq.getCumFreq(1));
        assertEquals(2, freq.getCumFreq(2));
        assertEquals(3, freq.getCumFreq(3));
        assertEquals(4, freq.getCumFreq(4));
        assertNull(freq.getCumFreq(5));
    }

    /**
     * Test method for {@link Frequency#getCumFreq(char)}.
     */
    @org.junit.Test
    public void testGetCumFreqChar() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(1, freq.getCumFreq('1'));
        assertEquals(2, freq.getCumFreq('1'));
        assertEquals(2, freq.getCumFreq('2'));
        assertEquals(3, freq.getCumFreq('2'));
        assertEquals(4, freq.getCumFreq('3'));
        assertNull(freq.getCumFreq('z'));
    }

    /**
     * Test method for {@link Frequency#getCumPct(Object)}.
     */
    @org.junit.Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(25.0, freq.getCumPct(1), 0.01);
        assertEquals(50.0, freq.getCumPct(2), 0.01);
        assertEquals(100.0, freq.getCumPct(3), 0.01);
        assertNull(freq.getCumPct('z'));
    }

    /**
     * Test method for {@link Frequency#getCumPct(int)}.
     */
    @org.junit.Test
    public void testGetCumPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);

        assertEquals(25.0, freq.getCumPct(1), 0.01);
        assertEquals(50.0, freq.getCumPct(2), 0.01);
        assertEquals(100.0, freq.getCumPct(3), 0.01);
        assertNull(freq.getCumPct(4));
    }

    /**
     * Test method for {@link Frequency#getCumPct(char)}.
     */
    @org.junit.Test
    public void testGetCumPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');

        assertEquals(25.0, freq.getCumPct('a'), 0.01);
        assertEquals(50.0, freq.getCumPct('a'), 0.01);
        assertEquals(100.0, freq.getCumPct('b'), 0.01);
        assertNull(freq.getCumPct('z'));
    }

}