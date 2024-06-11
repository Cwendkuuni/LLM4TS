package org.apache.commons.math.stat.test;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the Frequency class.
 */
public class FrequencyTest {

    /**
     * Test for default constructor and basic functionality of the Frequency class.
     */
    @Test
    public void testDefaultConstructorAndBasicFunctionality() {
        Frequency frequency = new Frequency();
        // Add some values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        // Test size of the frequency distribution
        assertEquals("Size of the frequency distribution is wrong", 6, frequency.valuesIterator().size());

        // Test getCount method
        long count1 = frequency.getCount(1);
        long count2 = frequency.getCount(2);
        long count3 = frequency.getCount(3);
        assertEquals("Count for value 1 is wrong", 2, count1);
        assertEquals("Count for value 2 is wrong", 2, count2);
        assertEquals("Count for value 3 is wrong", 2, count3);

        // Test getPct method
        double pct1 = frequency.getPct(1);
        double pct2 = frequency.getPct(2);
        double pct3 = frequency.getPct(3);
        assertEquals("Percentage for value 1 is wrong", 0.3333333333333333, pct1, 0.0001);
        assertEquals("Percentage for value 2 is wrong", 0.3333333333333333, pct2, 0.0001);
        assertEquals("Percentage for value 3 is wrong", 0.3333333333333333, pct3, 0.0001);

        // Test getCumFreq method
        long cumFreq1 = frequency.getCumFreq(1);
        long cumFreq2 = frequency.getCumFreq(2);
        long cumFreq3 = frequency.getCumFreq(3);
        assertEquals("Cumulative frequency for value 1 is wrong", 2, cumFreq1);
        assertEquals("Cumulative frequency for value 2 is wrong", 4, cumFreq2);
        assertEquals("Cumulative frequency for value 3 is wrong", 6, cumFreq3);

        // Test getCumPct method
        double cumPct1 = frequency.getCumPct(1);
        double cumPct2 = frequency.getCumPct(2);
        double cumPct3 = frequency.getCumPct(3);
        assertEquals("Cumulative percentage for value 1 is wrong", 0.3333333333333333, cumPct1, 0.0001);
        assertEquals("Cumulative percentage for value 2 is wrong", 0.6666666666666666, cumPct2, 0.0001);
        assertEquals("Cumulative percentage for value 3 is wrong", 1, cumPct3, 0.0001);

        // Test clear method
        frequency.clear();
        assertEquals("Size of the frequency distribution after clearing it is not 0", 0, frequency.valuesIterator().size());
    }

    /**
     * Test for constructor with Comparator and basic functionality of the Frequency class.
     */
    @Test
    public void testConstructorWithComparatorAndBasicFunctionality() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);

        Comparator<Comparable<Object>> comparator = (o1, o2) -> numberFormat.parse(o1.toString()).compareTo(numberFormat.parse(o2.toString()));

        Frequency frequency = new Frequency(comparator);
        // Add some values
        frequency.addValue(new Integer(3));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));
        frequency.addValue(new Integer(1));
        frequency.addValue(new Integer(2));

        // Test size of the frequency distribution
        assertEquals("Size of the frequency distribution is wrong", 5, frequency.valuesIterator().size());

        // Test getCount method
        long count3 = frequency.getCount(new Integer(3));
        long count1 = frequency.getCount(new Integer(1));
        long count2 = frequency.getCount(new Integer(2));
        assertEquals("Count for value 3 is wrong", 1, count3);
        assertEquals("Count for value 1 is wrong", 2, count1);
        assertEquals("Count for value 2 is wrong", 2, count2);

        // Test getPct method
        double pct3 = frequency.getPct(new Integer(3));
        double pct1 = frequency.getPct(new Integer(1));
        double pct2 = frequency.getPct(new Integer(2));
        assertEquals("Percentage for value 3 is wrong", 0.2, pct3, 0.0001);
        assertEquals("Percentage for value 1 is wrong", 0.4, pct1, 0.0001);
        assertEquals("Percentage for value 2 is wrong", 0.4, pct2, 0.0001);

        // Test getCumFreq method
        long cumFreq3 = frequency.getCumFreq(new Integer(3));
        long cumFreq1 = frequency.getCumFreq(new Integer(1));
        long cumFreq2 = frequency.getCumFreq(new Integer(2));
        assertEquals("Cumulative frequency for value 3 is wrong", 1, cumFreq3);
        assertEquals("Cumulative frequency for value 1 is wrong", 3, cumFreq1);
        assertEquals("Cumulative frequency for value 2 is wrong", 5, cumFreq2);

        // Test getCumPct method
        double cumPct3 = frequency.getCumPct(new Integer(3));
        double cumPct1 = frequency.getCumPct(new Integer(1));
        double cumPct2 = frequency.getCumPct(new Integer(2));
        assertEquals("Cumulative percentage for value 3 is wrong", 0.2, cumPct3, 0.0001);
        assertEquals("Cumulative percentage for value 1 is wrong", 0.4, cumPct1, 0.0001);
        assertEquals("Cumulative percentage for value 2 is wrong", 0.6, cumPct2, 0.0001);

        // Test clear method
        frequency.clear();
        assertEquals("Size of the frequency distribution after clearing it is not 0", 0, frequency.valuesIterator().size());
    }

    /**
     * Test for addValue method with IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithIllegalArgumentException() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue('A'); // Should throw an IllegalArgumentException
    }

    /**
     * Test for addValue method with ClassCastException.
     */
    @Test(expected = ClassCastException.class)
    public void testAddValueWithClassCastException() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue("3"); // Should throw a ClassCastException
    }
}