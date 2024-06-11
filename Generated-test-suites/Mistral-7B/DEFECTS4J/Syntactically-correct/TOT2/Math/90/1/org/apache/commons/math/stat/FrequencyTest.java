package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;

/**
 * Test for {@link Frequency} class.
 *
 * @version $Revision$ $Date$
 */
public class FrequencyTest {

    /**
     * Test constructor with default comparator.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency freq = new Frequency();
        // test empty frequency table
        assertEquals(0, freq.getSumFreq());
        assertEquals(0, freq.getCount(Integer.valueOf(1)));
        assertEquals(0.0, freq.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(0.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
        // test adding values
        freq.addValue(Integer.valueOf(1));
        freq.addValue(Integer.valueOf(1));
        assertEquals(2, freq.getSumFreq());
        assertEquals(2, freq.getCount(Integer.valueOf(1)));
        assertEquals(1.0, freq.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
    }

    /**
     * Test constructor with custom comparator.
     */
    @Test
    public void testCustomComparatorConstructor() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o2).compareTo(o1);
            }
        };
        Frequency freq = new Frequency(comparator);
        // test empty frequency table
        assertEquals(0, freq.getSumFreq());
        assertEquals(0, freq.getCount(Integer.valueOf(1)));
        assertEquals(0.0, freq.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(0.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
        // test adding values
        freq.addValue(Integer.valueOf(1));
        freq.addValue(Integer.valueOf(1));
        assertEquals(2, freq.getSumFreq());
        assertEquals(2, freq.getCount(Integer.valueOf(1)));
        assertEquals(1.0, freq.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
        // test values in reverse order
        freq.addValue(Integer.valueOf(2));
        freq.addValue(Integer.valueOf(2));
        assertEquals(4, freq.getSumFreq());
        assertEquals(2, freq.getCount(Integer.valueOf(2)));
        assertEquals(0.5, freq.getPct(Integer.valueOf(2)), 0.001);
        assertEquals(0.5, freq.getCumPct(Integer.valueOf(2)), 0.001);
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
    }

    /**
     * Test {@link Frequency#addValue(Object)} method with valid arguments.
     */
    @Test
    public void testAddValueValid() {
        Frequency freq = new Frequency();
        // test adding valid integers
        for (int i = 0; i < 10; i++) {
            freq.addValue(Integer.valueOf(i));
        }
        // test adding valid longs
        for (long l = 0; l < 10; l++) {
            freq.addValue(Long.valueOf(l));
        }
        // test adding valid chars
        for (char c = '0'; c <= '9'; c++) {
            freq.addValue(Character.valueOf(c));
        }
        // test adding valid strings
        freq.addValue("one");
        freq.addValue("one");
        // test getting values
        assertEquals(10, freq.getSumFreq());
        assertEquals(1, freq.getCount(Integer.valueOf(0)));
        assertEquals(0.1, freq.getPct(Integer.valueOf(0)), 0.001);
        assertEquals(0.1, freq.getCumPct(Integer.valueOf(0)), 0.001);
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(9)), 0.001);
        assertEquals(1, freq.getCount("one"));
        assertEquals(0.1, freq.getPct("one"), 0.001);
        assertEquals(0.1, freq.getCumPct("one"), 0.001);
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(9)), 0.001);
    }

    /**
     * Test {@link Frequency#addValue(Object)} method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalid() {
        Frequency freq = new Frequency();
        // test adding invalid integers
        freq.addValue(new Object());
        freq.addValue(1.0);
        // test adding invalid longs
        freq.addValue(new Object());
        freq.addValue("10L");
        // test adding invalid chars
        freq.addValue(new Object());
        freq.addValue((char) 0);
        // test adding invalid strings
        freq.addValue(new Object());
        freq.addValue(new String());
    }

    /**
     * Test {@link Frequency#clear()} method.
     */
    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        // test empty frequency table
        assertEquals(0, freq.getSumFreq());
        assertEquals(0, freq.getCount(Integer.valueOf(1)));
        assertEquals(0.0, freq.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(0.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
        // test adding values
        freq.addValue(Integer.valueOf(1));
        freq.addValue(Integer.valueOf(1));
        assertEquals(2, freq.getSumFreq());
        assertEquals(2, freq.getCount(Integer.valueOf(1)));
        assertEquals(1.0, freq.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
        // test clearing
        freq.clear();
        assertEquals(0, freq.getSumFreq());
        assertEquals(0, freq.getCount(Integer.valueOf(1)));
        assertEquals(0.0, freq.getPct(Integer.valueOf(1)), 0.001);
        assertEquals(0.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
    }

    /**
     * Test {@link Frequency#valuesIterator()} method.
     */
    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        // test empty frequency table
        assertEquals(0, freq.getSumFreq());
        Iterator iterator = freq.valuesIterator();
        assertFalse(iterator.hasNext());
        // test adding values
        freq.addValue(Integer.valueOf(1));
        freq.addValue(Integer.valueOf(1));
        assertEquals(2, freq.getSumFreq());
        iterator = freq.valuesIterator();
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test {@link Frequency#getSumFreq()} method.
     */
    @Test
    public void testGetSumFreq() {
        Frequency freq = new Frequency();
        // test empty frequency table
        assertEquals(0, freq.getSumFreq());
        // test adding values
        freq.addValue(Integer.valueOf(1));
        freq.addValue(Integer.valueOf(1));
        assertEquals(2, freq.getSumFreq());
    }

    /**
     * Test {@link Frequency#getCount(Object)} method.
     */
    @Test
    public void testGetCount() {
        Frequency freq = new Frequency();
        // test empty frequency table
        assertEquals(0, freq.getCount(Integer.valueOf(1)));
        // test adding values
        freq.addValue(Integer.valueOf(1));
        freq.addValue(Integer.valueOf(1));
        assertEquals(2, freq.getCount(Integer.valueOf(1)));
    }

    /**
     * Test {@link Frequency#getPct(Object)} method.
     */
    @Test
    public void testGetPct() {
        Frequency freq = new Frequency();
        // test empty frequency table
        assertEquals(0.0, freq.getPct(Integer.valueOf(1)), 0.001);
        // test adding values
        freq.addValue(Integer.valueOf(1));
        freq.addValue(Integer.valueOf(1));
        assertEquals(1.0, freq.getPct(Integer.valueOf(1)), 0.001);
    }

    /**
     * Test {@link Frequency#getCumPct(Object)} method.
     */
    @Test
    public void testGetCumPct() {
        Frequency freq = new Frequency();
        // test empty frequency table
        assertEquals(0.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
        // test adding values in order
        freq.addValue(Integer.valueOf(1));
        freq.addValue(Integer.valueOf(1));
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(2)), 0.001);
        // test adding values out of order
        freq.addValue(Integer.valueOf(2));
        freq.addValue(Integer.valueOf(1));
        assertEquals(1.0, freq.getCumPct(Integer.valueOf(1)), 0.001);
        assertEquals(2.0, freq.getCumPct(Integer.valueOf(2)), 0.001);
    }
}