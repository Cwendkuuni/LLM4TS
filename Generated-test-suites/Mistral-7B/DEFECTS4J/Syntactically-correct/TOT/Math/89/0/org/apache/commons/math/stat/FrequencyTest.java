package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for {@link Frequency}.
 */
public class FrequencyTest {

    /**
     * Test method for {@link Frequency#Frequency()}.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test method for {@link Frequency#Frequency(Comparator)}.
     */
    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(NaturalComparator.INSTANCE);
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test method for {@link Frequency#addValue(Object)}.
     */
    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        // test getCount
        assertEquals(3, frequency.getCount('a'));
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(0, frequency.getCount('b'));
        assertEquals(0, frequency.getCount(3));

        // test getPct
        assertEquals(0.3, frequency.getPct('a'), 0.001);
        assertEquals(0.4, frequency.getPct(1), 0.001);
        assertEquals(0.3, frequency.getPct(2), 0.001);

        // test getCumFreq
        assertEquals(3, frequency.getCumFreq('a'));
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(5, frequency.getCumFreq(2));

        // test getCumPct
        assertEquals(0.3, frequency.getCumPct('a'), 0.001);
        assertEquals(0.4, frequency.getCumPct(1), 0.001);
        assertEquals(0.7, frequency.getCumPct(2), 0.001);
    }

    /**
     * Test method for {@link Frequency#clear()}.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.clear();
        assertTrue(frequency.isEmpty());
        assertEquals(0, frequency.getSumFreq());
    }

    /**
     * Test method for {@link Frequency#valuesIterator()}.
     */
    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        Iterator<Object> iterator = frequency.valuesIterator();
        assertTrue(iterator.hasNext());
        assertEquals('a', iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link Frequency#toString()}.
     */
    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n"
                + "a\t3\t0.3\t0.3\n"
                + "1\t2\t0.4\t0.7\n"
                + "2\t2\t0.3\t1.0\n";
        String actual = frequency.toString();
        assertEquals(expected, actual);

        frequency = new Frequency(NaturalComparator.INSTANCE);
        frequency.addValue('z');
        frequency.addValue('y');
        frequency.addValue('x');
        frequency.addValue('w');

        expected = "Value \t Freq. \t Pct. \t Cum Pct.\n"
                + "w\t1\t0.2\t0.2\n"
                + "x\t1\t0.2\t0.4\n"
                + "y\t1\t0.2\t0.6\n"
                + "z\t1\t0.2\t1.0\n";
        actual = frequency.toString();
        assertEquals(expected, actual);
    }

}