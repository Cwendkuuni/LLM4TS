package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Frequency class.
 */
public class FrequencyTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency freq = new Frequency();
        assertEquals(0, freq.getSumFreq());
        assertNull(freq.valuesIterator().next());
    }

    /**
     * Test constructor with null comparator.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullComparator() {
        new Frequency(null);
    }

    /**
     * Test constructor with custom comparator.
     */
    @Test
    public void testCustomComparator() {
        Comparator<Integer> comparator = (o1, o2) -> o1.compareTo(o2) * -1;
        Frequency freq = new Frequency(comparator);
        Frequency freqExpected = new Frequency();
        freqExpected.addValue(5);
        freqExpected.addValue(3);
        freqExpected.addValue(5);
        freqExpected.addValue(3);

        freq.addValue(5);
        freq.addValue(3);
        freq.addValue(5);
        freq.addValue(3);

        Iterator<Object> values = freq.valuesIterator();
        Iterator<Object> valuesExpected = freqExpected.valuesIterator();
        while (values.hasNext() && valuesExpected.hasNext()) {
            Object value = values.next();
            Object valueExpected = valuesExpected.next();
            assertEquals(value, valueExpected);
        }

        assertEquals(freqExpected.getSumFreq(), freq.getSumFreq());
    }

    /**
     * Test addValue int.
     */
    @Test
    public void testAddValueInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);

        assertEquals(3, freq.getCount(1));
        assertEquals(1.0, freq.getPct(1), 0.001);
        assertEquals(3.0, freq.getCumPct(1), 0.001);
    }

    /**
     * Test addValue long.
     */
    @Test
    public void testAddValueLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);

        assertEquals(2, freq.getCount(1L));
        assertEquals(0.5, freq.getPct(1L), 0.001);
        assertEquals(0.5, freq.getCumPct(1L), 0.001);
    }

    /**
     * Test addValue char.
     */
    @Test
    public void testAddValueChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');

        assertEquals(2, freq.getCount('a'));
        assertEquals(1.0, freq.getPct('a'), 0.001);
        assertEquals(2.0, freq.getCumPct('a'), 0.001);
    }

    /**
     * Test addValue null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNull() {
        Frequency freq = new Frequency();
        freq.addValue(null);
    }

    /**
     * Test addValue non-comparable.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue("nonComparable");
    }

    /**
     * Test clear.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.clear();

        assertEquals(0, freq.getSumFreq());
        assertFalse(freq.valuesIterator().hasNext());
    }

    /**
     * Test getSumFreq.
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);

        assertEquals(3, freq.getSumFreq());
    }

    /**
     * Test getCount int.
     */
    @Test
    public void testGetCountInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);

        assertEquals(3, freq.getCount(1));
    }

    /**
     * Test getCount long.
     */
    @Test
    public void testGetCountLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);

        assertEquals(2, freq.getCount(1L));
    }

    /**
     * Test getCount char.
     */
    @Test
    public void testGetCountChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');

        assertEquals(2, freq.getCount('a'));
    }

    /**
     * Test getCount null.
     */
    @Test
    public void testGetCountNull() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);

        assertEquals(0, freq.getCount(null));
    }

    /**
     * Test getPct int.
     */
    @Test
    public void testGetPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);

        assertEquals(1.0 / 3.0, freq.getPct(1), 0.001);
    }

    /**
     * Test getPct long.
     */
    @Test
    public void testGetPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);

        assertEquals(1.0 / 4.0, freq.getPct(1L), 0.001);
    }

    /**
     * Test getPct char.
     */
    @Test
    public void testGetPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');

        assertEquals(1.0 / 3.0, freq.getPct('a'), 0.001);
    }

    /**
     * Test getPct null.
     */
    @Test
    public void testGetPctNull() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);

        assertEquals(0.0, freq.getPct(null), 0.001);
    }

    /**
     * Test getCumFreq int.
     */
    @Test
    public void testGetCumFreqInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);

        assertEquals(1, freq.getCumFreq(1));
        assertEquals(1, freq.getCumFreq(2));
        assertEquals(3, freq.getCumFreq(3));
    }

    /**
     * Test getCumFreq long.
     */
    @Test
    public void testGetCumFreqLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);

        assertEquals(1, freq.getCumFreq(1L));
        assertEquals(2, freq.getCumFreq(1L + 1));
        assertEquals(3, freq.getCumFreq(2L));
    }

    /**
     * Test getCumFreq char.
     */
    @Test
    public void testGetCumFreqChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');

        assertEquals(1, freq.getCumFreq('a'));
        assertEquals(1, freq.getCumFreq('a' + 1));
        assertEquals(2, freq.getCumFreq('b'));
    }

    /**
     * Test getCumFreq null.
     */
    @Test
    public void testGetCumFreqNull() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);

        assertEquals(0, freq.getCumFreq(null));
    }

    /**
     * Test getCumPct int.
     */
    @Test
    public void testGetCumPctInt() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(1);

        assertEquals(1.0 / 3.0, freq.getPct(1), 0.001);
        assertEquals(1.0 / 3.0, freq.getCumPct(1), 0.001);
        assertEquals(2.0 / 3.0, freq.getCumPct(2), 0.001);
    }

    /**
     * Test getCumPct long.
     */
    @Test
    public void testGetCumPctLong() {
        Frequency freq = new Frequency();
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(1L);

        assertEquals(1.0 / 4.0, freq.getPct(1L), 0.001);
        assertEquals(1.0 / 4.0, freq.getCumPct(1L), 0.001);
        assertEquals(2.0 / 4.0, freq.getCumPct(2L), 0.001);
    }

    /**
     * Test getCumPct char.
     */
    @Test
    public void testGetCumPctChar() {
        Frequency freq = new Frequency();
        freq.addValue('a');
        freq.addValue('a');
        freq.addValue('b');

        assertEquals(1.0 / 3.0, freq.getPct('a'), 0.001);
        assertEquals(1.0 / 3.0, freq.getCumPct('a'), 0.001);
        assertEquals(2.0 / 3.0, freq.getCumPct('b'), 0.001);
    }

    /**
     * Test getCumPct null.
     */
    @Test
    public void testGetCumPctNull() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);

        assertEquals(0.0, freq.getCumPct(null), 0.001);
    }
}