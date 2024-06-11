package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Comparator;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for {@link Frequency}.
 */
public class FrequencyTest {

    /**
     * Test method for {@link Frequency#Frequency()} and {@link Frequency#addValue(Object)}.
     */
    @Test
    public void testDefaultFrequency() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertNotNull(frequency.toString());
        assertEquals(4, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(0.25, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
    }

    /**
     * Test method for {@link Frequency#Frequency(Comparator)} and {@link Frequency#addValue(Object)}.
     */
    @Test
    public void testCustomComparatorFrequency() {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        assertNotNull(frequency.toString());
        assertEquals(4, frequency.getSumFreq());
        assertEquals(1, frequency.getCount(2));
        assertEquals(2, frequency.getCount(1));
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.25, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
    }

    /**
     * Test method for {@link Frequency#addValue(Object)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueType() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    /**
     * Test method for {@link Frequency#addValue(Object)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueComparability() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
    }

    /**
     * Test method for {@link Frequency#clear()}.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertNull(frequency.toString());
    }

    /**
     * Test method for {@link Frequency#valuesIterator()}, {@link Frequency#getSumFreq()},
     * {@link Frequency#getCount(Object)}, {@link Frequency#getPct(Object)}, and {@link Frequency#getCumFreq(Object)}.
     */
    @Test
    public void testGetters() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        Object next;
        long sumFreq, count, cumFreq;
        double pct, cumPct;
        while (iterator.hasNext()) {
            next = iterator.next();
            sumFreq = frequency.getSumFreq();
            count = frequency.getCount(next);
            pct = frequency.getPct(next);
            cumFreq = frequency.getCumFreq(next);
            cumPct = frequency.getCumPct(next);
            System.out.println("Value: " + next + "\tFrequency: " + count + "\tCumulative Frequency: " + cumFreq
                    + "\tPercentage: " + pct + "\tCumulative Percentage: " + cumPct);
            assertEquals(1, count);
            assertEquals(1, cumFreq);
            assertEquals(0.25, pct, 0.001);
            assertEquals(0.25, cumPct, 0.001);
        }
        assertEquals(4, sumFreq);
        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(1, frequency.getCount('1'));
        assertEquals(1, frequency.getCount('2'));
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(0.25, frequency.getPct('1'), 0.001);
        assertEquals(0.25, frequency.getPct('2'), 0.001);
        assertEquals(0.25, frequency.getCumPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(2), 0.001);
        assertEquals(0.25, frequency.getCumPct('1'), 0.001);
        assertEquals(0.5, frequency.getCumPct('2'), 0.001);
    }
}