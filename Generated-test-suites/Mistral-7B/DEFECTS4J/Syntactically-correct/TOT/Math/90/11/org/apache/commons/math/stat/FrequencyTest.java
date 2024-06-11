package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class FrequencyTest {

    private Frequency frequency;

    @Before
    public void setUp() {
        frequency = new Frequency();
    }

    @Test
    public void testDefaultConstructor() {
        // Test adding values and getting counts
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('a');

        long count1 = frequency.getCount(1);
        long count2 = frequency.getCount(2);
        long countA = frequency.getCount('a');

        assertEquals(4, frequency.getSumFreq());
        assertEquals(2, count1);
        assertEquals(2, count2);
        assertEquals(1, countA);

        // Test getting percentages
        double pct1 = frequency.getPct(1);
        double pct2 = frequency.getPct(2);
        double pctA = frequency.getPct('a');

        assertEquals(0.5, pct1, Precision.SAFE_EPSILON);
        assertEquals(0.5, pct2, Precision.SAFE_EPSILON);
        assertNull(pctA);

        // Test getting cumulative frequencies and percentages
        long cumFreq1 = frequency.getCumFreq(1);
        long cumFreq2 = frequency.getCumFreq(2);
        long cumFreqA = frequency.getCumFreq('a');

        double cumPct1 = frequency.getCumPct(1);
        double cumPct2 = frequency.getCumPct(2);
        double cumPctA = frequency.getCumPct('a');

        assertEquals(1, cumFreq1);
        assertEquals(3, cumFreq2);
        assertNull(cumFreqA);
        assertEquals(0.25, cumPct1, Precision.SAFE_EPSILON);
        assertEquals(0.75, cumPct2, Precision.SAFE_EPSILON);
        assertNull(cumPctA);

        // Test toString method
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n"
                + "1 \t 2 \t 0.5 \t 0.25 \n"
                + "2 \t 2 \t 0.5 \t 0.75 \n"
                + "a \t 1 \t NaN \t NaN\n";
        String result = frequency.toString();
        assertEquals(expected, result);

        // Test adding values with different types
        frequency.clear();
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.addValue(2L);

        long count1_1 = frequency.getCount(1);
        long countA_1 = frequency.getCount('a');
        long count2_1 = frequency.getCount(2);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(1, count1_1);
        assertEquals(1, countA_1);
        assertEquals(1, count2_1);
    }

    @Test
    public void testComparatorConstructor() {
        Comparator<Object> comparator = new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable<?>) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('a');

        long count1 = frequency.getCount(1);
        long count2 = frequency.getCount(2);
        long countA = frequency.getCount('a');

        assertEquals(4, frequency.getSumFreq());
        assertEquals(2, count1);
        assertEquals(2, count2);
        assertEquals(1, countA);

        // Test getting percentages
        double pct1 = frequency.getPct(1);
        double pct2 = frequency.getPct(2);
        double pctA = frequency.getPct('a');

        assertEquals(0.5, pct1, Precision.SAFE_EPSILON);
        assertEquals(0.5, pct2, Precision.SAFE_EPSILON);
        assertNull(pctA);

        // Test getting cumulative frequencies and percentages
        long cumFreq1 = frequency.getCumFreq(1);
        long cumFreq2 = frequency.getCumFreq(2);
        long cumFreqA = frequency.getCumFreq('a');

        double cumPct1 = frequency.getCumPct(1);
        double cumPct2 = frequency.getCumPct(2);
        double cumPctA = frequency.getCumPct('a');

        assertEquals(1, cumFreq1);
        assertEquals(3, cumFreq2);
        assertNull(cumFreqA);
        assertEquals(0.25, cumPct1, Precision.SAFE_EPSILON);
        assertEquals(0.75, cumPct2, Precision.SAFE_EPSILON);
        assertNull(cumPctA);

        // Test toString method
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n"
                + "1 \t 2 \t 0.5 \t 0.25 \n"
                + "2 \t 2 \t 0.5 \t 0.75 \n"
                + "a \t 1 \t NaN \t NaN\n";
        String result = frequency.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testAddingValuesWithSameKey() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);

        long count1 = frequency.getCount(1);

        assertEquals(2, frequency.getSumFreq());
        assertEquals(2, count1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingInvalidValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');

        frequency.addValue(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingInvalidValue2() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        frequency.addValue('a');
    }

    @Test
    public void testGettingNonExistentValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        long countNonExistent = frequency.getCount(3);

        assertEquals(0, countNonExistent);
    }

    @Test
    public void testGettingNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        long countNull = frequency.getCount(null);

        assertEquals(0, countNull);
    }

    @Test
    public void testClearingValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        long sumFreqBeforeClear = frequency.getSumFreq();
        frequency.clear();
        long sumFreqAfterClear = frequency.getSumFreq();

        assertEquals(2, sumFreqBeforeClear);
        assertEquals(0, sumFreqAfterClear);
    }
}