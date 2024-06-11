package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FrequencyTest {

    @Test
    public void testDefaultFrequency() {
        Frequency freq = new Frequency();
        // Add some values
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        // Test getSumFreq
        long sumFreq = freq.getSumFreq();
        assertEquals(6, sumFreq);
        // Test getCount
        long count1 = freq.getCount(1);
        assertEquals(2, count1);
        long count2 = freq.getCount(2);
        assertEquals(2, count2);
        long count3 = freq.getCount(3);
        assertEquals(1, count3);
        // Test getPct
        double pct1 = freq.getPct(1);
        assertEquals(1 / 3.0, pct1, 0.001);
        double pct2 = freq.getPct(2);
        assertEquals(1 / 3.0, pct2, 0.001);
        double pct3 = freq.getPct(3);
        assertEquals(1 / 6.0, pct3, 0.001);
        // Test getCumFreq
        long cumFreq1 = freq.getCumFreq(1);
        assertEquals(2, cumFreq1);
        long cumFreq2 = freq.getCumFreq(2);
        assertEquals(4, cumFreq2);
        long cumFreq3 = freq.getCumFreq(3);
        assertEquals(5, cumFreq3);
        // Test getCumPct
        double cumPct1 = freq.getCumPct(1);
        assertEquals(1 / 3.0, cumPct1, 0.001);
        double cumPct2 = freq.getCumPct(2);
        assertEquals(2 / 3.0, cumPct2, 0.001);
        double cumPct3 = freq.getCumPct(3);
        assertEquals(1.0, cumPct3, 0.001);
    }

    @Test
    public void testFrequencyWithCustomComparator() {
        Comparator customComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o2).compareTo(o1);
            }
        };
        Frequency freq = new Frequency(customComparator);
        // Add some values
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        // Test getSumFreq
        long sumFreq = freq.getSumFreq();
        assertEquals(6, sumFreq);
        // Test getCount
        long count1 = freq.getCount(1);
        assertEquals(2, count1);
        long count2 = freq.getCount(2);
        assertEquals(2, count2);
        long count3 = freq.getCount(3);
        assertEquals(1, count3);
        // Test getPct
        double pct1 = freq.getPct(1);
        assertEquals(1 / 3.0, pct1, 0.001);
        double pct2 = freq.getPct(2);
        assertEquals(1 / 3.0, pct2, 0.001);
        double pct3 = freq.getPct(3);
        assertEquals(1 / 6.0, pct3, 0.001);
        // Test getCumFreq
        long cumFreq1 = freq.getCumFreq(1);
        assertEquals(2, cumFreq1);
        long cumFreq2 = freq.getCumFreq(2);
        assertEquals(4, cumFreq2);
        long cumFreq3 = freq.getCumFreq(3);
        assertEquals(5, cumFreq3);
        // Test getCumPct
        double cumPct1 = freq.getCumPct(1);
        assertEquals(1 / 3.0, cumPct1, 0.001);
        double cumPct2 = freq.getCumPct(2);
        assertEquals(2 / 3.0, cumPct2, 0.001);
        double cumPct3 = freq.getCumPct(3);
        assertEquals(1.0, cumPct3, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenAddingNonComparableValue() {
        Frequency freq = new Frequency();
        freq.addValue("not a number");
    }

    @Test
    public void testValuesIterator() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        List values = List.of(1, 1, 2, 2, 3);
        Iterator iterator = freq.valuesIterator();
        for (int i = 0; i < values.size(); i++) {
            assertEquals(values.get(i), iterator.next());
        }
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testClear() {
        Frequency freq = new Frequency();
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.clear();
        long sumFreq = freq.getSumFreq();
        assertEquals(0, sumFreq);
    }
}