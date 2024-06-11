package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(new StringComparator());
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        Iterator<Long> iterator = frequency.valuesIterator();
        Long value = iterator.next();
        assertEquals(2, value.longValue());
        assertEquals(4, frequency.getCount(value));
        assertEquals(4, frequency.getCumFreq(value));
        assertEquals(0.5, frequency.getPct(value), 0.001);
        assertEquals(0.5, frequency.getCumPct(value), 0.001);

        value = iterator.next();
        assertEquals(1, value.longValue());
        assertEquals(2, frequency.getCount(value));
        assertEquals(2, frequency.getCumFreq(value));
        assertEquals(0.25, frequency.getPct(value), 0.001);
        assertEquals(0.25, frequency.getCumPct(value), 0.001);

        frequency.clear();
        assertFalse(frequency.isFull());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueInvalidComparison() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('a');
    }

    @Test
    public void testAddValueSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        Iterator<Long> iterator = frequency.valuesIterator();
        Long value = iterator.next();
        assertEquals(2, value.longValue());
        assertEquals(2, frequency.getCount(value));
        assertEquals(2, frequency.getCumFreq(value));
        assertEquals(0.5, frequency.getPct(value), 0.001);
        assertEquals(0.5, frequency.getCumPct(value), 0.001);
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(8, frequency.getSumFreq());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();

        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        Iterator<Long> iterator = frequency.valuesIterator();
        Long value = iterator.next();
        assertEquals(2, value.longValue());
        value = iterator.next();
        assertEquals(1, value.longValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(4, frequency.getCount(1L));
        assertEquals(4, frequency.getCount(2L));
        assertEquals(0, frequency.getCount('a'));
    }

    @Test
    public void testGetCountInvalidType() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        try {
            frequency.getCount(new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(0.25, frequency.getPct(1L), 0.001);
        assertEquals(0.5, frequency.getPct(2L), 0.001);
        assertEquals(Double.NaN, frequency.getPct('a'), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(0, frequency.getCumFreq(0));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
        assertEquals(4, frequency.getCumFreq(3));
        assertEquals(4, frequency.getCumFreq(4));
        assertEquals(8, frequency.getCumFreq(5));

        try {
            frequency.getCumFreq(new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(0.0, frequency.getCumPct(0), 0.001);
        assertEquals(0.25, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
        assertEquals(1.0, frequency.getCumPct(4), 0.001);
        assertEquals(1.0, frequency.getCumPct(5), 0.001);

        try {
            frequency.getCumPct(new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct.\n" +
                "1 \t 2 \t 0.25 \t 0.25\n" +
                "2 \t 2 \t 0.5 \t 0.50\n";

        assertEquals(expected, result);
    }
}

class StringComparator implements Comparator<Object>, Serializable {
    @Override
    public int compare(Object o1, Object o2) {
        return ((String)o1).compareTo((String)o2);
    }
}