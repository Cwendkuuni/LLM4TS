package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test cases for {@link Frequency}.
 */
public class FrequencyTest {

    /**
     * Test of empty constructor.
     */
    @Test
    public void testEmptyConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(null));
        assertEquals(0, frequency.getCount('a'));
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCumFreq(null));
        assertEquals(0, frequency.getCumFreq('a'));
        assertEquals(0, frequency.getCumFreq(1));
        assertEquals(0, frequency.getCumPct(null));
        assertEquals(0, frequency.getCumPct('a'), 0.0);
        assertEquals(0, frequency.getCumPct(1), 0.0);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n", frequency.toString());
    }

    /**
     * Test of constructor with comparator.
     */
    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(new NaturalComparator());
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('b');
        frequency.addValue('b');
        frequency.addValue(2);
        frequency.addValue(2);

        assertEquals(8, frequency.getSumFreq());
        assertEquals(4, frequency.getCount('a'));
        assertEquals(2, frequency.getCount('b'));
        assertEquals(4, frequency.getCount(1));
        assertEquals(4, frequency.getCount(2));
        assertEquals(4, frequency.getCumFreq('a'));
        assertEquals(5, frequency.getCumFreq('b'));
        assertEquals(6, frequency.getCumFreq(1));
        assertEquals(8, frequency.getCumFreq(2));
        assertEquals(0.5, frequency.getPct('a'), 0.001);
        assertEquals(0.25, frequency.getPct('b'), 0.001);
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(0.5, frequency.getCumPct('a'), 0.001);
        assertEquals(0.625, frequency.getCumPct('b'), 0.001);
        assertEquals(0.625, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, festival.getCumPct(2), 0.001);
        assertEquals("Value \t Freq. \t Pct. \t Cum Pct.\n" +
                     "a       \t 4       \t 0.5      \t 0.50\n" +
                     "1        \t 4       \t 0.5      \t 1.00\n" +
                     "b       \t 2       \t 0.25     \t 1.13\n" +
                     "2        \t 2       \t 0.25     \t 1.38\n", frequency.toString());
    }

    /**
     * Test of addValue method with int.
     */
    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(3, frequency.getSumFreq());
        assertEquals(3, frequency.getCount(1));
        assertEquals(0, frequency.getCumFreq(1));
        assertEquals(1.0, frequency.getPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.01);
    }

    /**
     * Test of addValue method with long.
     */
    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        assertEquals(3, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1L));
        assertEquals(1, frequency.getCount(2L));
        assertEquals(2.0 / 3.0, frequency.getPct(1L), 0.01);
        assertEquals(2.0 / 3.0, frequency.getCumPct(1L), 0.01);
        assertEquals(1.0 / 3.0, frequency.getCumPct(2L), 0.01);
    }

    /**
     * Test of addValue method with char.
     */
    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        assertEquals(3, frequency.getSumFreq());
        assertEquals(2, frequency.getCount('a'));
        assertEquals(1, frequency.getCount('b'));
        assertEquals(2.0 / 3.0, frequency.getPct('a'), 0.01);
        assertEquals(2.0 / 3.0, frequency.getCumPct('a'), 0.01);
        assertEquals(1.0 / 3.0, frequency.getCumPct('b'), 0.01);
    }

    /**
     * Test of addValue method with Object.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    /**
     * Test of addValue method with non-comparable value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new String("foo"));
    }

    /**
     * Test of addValue method with non-comparable value of wrong type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNonComparableWrongType() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Integer(1));
        frequency.addValue(1.0);
    }

    /**
     * Test of addValue method with duplicate values.
     */
    @Test
    public void testAddValueDuplicate() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getCount(1));
    }

    /**
     * Test of clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCumFreq(1));
        assertEquals(0.0, frequency.getPct(1), 0.01);
        assertEquals(0.0, frequency.getCumPct(1), 0.01);
    }

}