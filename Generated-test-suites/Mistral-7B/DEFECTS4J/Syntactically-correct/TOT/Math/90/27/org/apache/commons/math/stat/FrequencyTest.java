package org.apache.commons.math.stat;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testFrequencyAddValues() {
        Frequency freq = new Frequency();

        // Test adding integer values
        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);

        long expectedCount1 = 3;
        long expectedCount2 = 3;
        long expectedCount3 = 2;

        long actualCount1 = freq.getCount(1);
        long actualCount2 = freq.getCount(2);
        long actualCount3 = freq.getCount(3);

        assertEquals(expectedCount1, actualCount1);
        assertEquals(expectedCount2, actualCount2);
        assertEquals(expectedCount3, actualCount3);

        // Test adding long values
        freq.clear();
        freq.addValue(1L);
        freq.addValue(1L);
        freq.addValue(2L);
        freq.addValue(2L);
        freq.addValue(3L);
        freq.addValue(3L);

        long actualCount1L = freq.getCount(1L);
        long actualCount2L = freq.getCount(2L);
        long actualCount3L = freq.getCount(3L);

        assertEquals(expectedCount1, actualCount1L);
        assertEquals(expectedCount2, actualCount2L);
        assertEquals(expectedCount3, actualCount3L);

        // Test adding char values
        freq.clear();
        freq.addValue('A');
        freq.addValue('A');
        freq.addValue('B');
        freq.addValue('B');
        freq.addValue('C');
        freq.addValue('C');

        char expectedChar = 'A';
        long expectedCountA = 3;
        long expectedCountB = 2;
        long expectedCountC = 1;

        char actualChar = (char) freq.getCount(expectedChar).intValue();
        long actualCountA = freq.getCount(expectedChar);
        long actualCountB = freq.getCount('B');
        long actualCountC = freq.getCount('C');

        assertEquals(expectedChar, actualChar);
        assertEquals(expectedCountA, actualCountA);
        assertEquals(expectedCountB, actualCountB);
        assertEquals(expectedCountC, actualCountC);

        // Test adding null value
        freq.clear();
        try {
            freq.addValue(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test adding non-comparable value
        freq.clear();
        try {
            freq.addValue(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFrequencyClear() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);

        long expectedSumFreq = 6;
        long expectedCount1 = 3;
        long expectedCount2 = 3;
        long expectedCount3 = 2;

        long actualSumFreq = freq.getSumFreq();
        long actualCount1 = freq.getCount(1);
        long actualCount2 = freq.getCount(2);
        long actualCount3 = freq.getCount(3);

        assertEquals(expectedSumFreq, actualSumFreq);
        assertEquals(expectedCount1, actualCount1);
        assertEquals(expectedCount2, actualCount2);
        assertEquals(expectedCount3, actualCount3);

        freq.clear();

        long actualSumFreqAfterClear = freq.getSumFreq();
        long actualCount1AfterClear = freq.getCount(1);
        long actualCount2AfterClear = freq.getCount(2);
        long actualCount3AfterClear = freq.getCount(3);

        assertEquals(0, actualSumFreqAfterClear);
        assertEquals(0, actualCount1AfterClear);
        assertEquals(0, actualCount2AfterClear);
        assertEquals(0, actualCount3AfterClear);
    }

    @Test
    public void testFrequencyValuesIterator() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);

        Iterator<Comparable> valuesIterator = freq.valuesIterator();

        Comparable expectedValue1 = 1;
        Comparable expectedValue2 = 2;
        Comparable expectedValue3 = 3;

        Comparable actualValue;

        while (valuesIterator.hasNext()) {
            actualValue = valuesIterator.next();
            if (actualValue.equals(expectedValue1)) {
                assertEquals(expectedValue1, actualValue);
                expectedValue1 = (Comparable) 4; // next value
            } else if (actualValue.equals(expectedValue2)) {
                assertEquals(expectedValue2, actualValue);
                expectedValue2 = (Comparable) 4; // next value
            } else if (actualValue.equals(expectedValue3)) {
                assertEquals(expectedValue3, actualValue);
                expectedValue3 = null; // last value
            } else {
                fail("Unexpected value: " + actualValue);
            }
        }

        if (expectedValue3 != null) {
            fail("Expected iterator to return all values");
        }
    }

    @Test
    public void testFrequencyGetSumFreq() {
        Frequency freq = new Frequency();

        freq.addValue(1);
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue(3);

        long expectedSumFreq = 6;
        long actualSumFreq = freq.getSumFreq();

        assertEquals(expectedSumFreq, actualSumFreq);
    }

    // Add more test cases for getCount, getPct, getCumFreq, and getCumPct
}