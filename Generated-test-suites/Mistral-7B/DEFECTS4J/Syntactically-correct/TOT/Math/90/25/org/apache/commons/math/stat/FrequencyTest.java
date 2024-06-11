package org.apache.commons.math.stat;

import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.TreeMap;

import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyDefaultConstructor() {
        Frequency frequency = new Frequency();
        // Test empty frequency distribution
        assertEquals("Empty distribution has no values", 0, frequency.getSumFreq());
        assertEquals("Empty distribution has no values", 0, frequency.getCount(null));
        assertEquals("Empty distribution has no values", 0, frequency.getCount(0));
        assertEquals("Empty distribution has no values", 0.0, frequency.getPct(null), 0.001);
        assertEquals("Empty distribution has no values", 0.0, frequency.getPct(0), 0.001);
        assertEquals("Empty distribution has no values", 0.0, frequency.getCumPct(null), 0.001);
        assertEquals("Empty distribution has no values", 0.0, frequency.getCumPct(0), 0.001);

        // Test adding values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(3);

        // Test magnitude of distribution
        assertEquals("Distribution has 5 values", 5, frequency.getSumFreq());

        // Test single value frequency
        assertEquals("Frequency of value 1 is 3", 3, frequency.getCount(1));
        assertEquals("Frequency of value 1 is 3", 3, frequency.getCount(Long.valueOf(1)));
        assertEquals("Frequency of value 1 is 3", 0.6, frequency.getPct(1), 0.001);
        assertEquals("Frequency of value 1 is 3", 0.06, frequency.getCumPct(1), 0.001);

        // Test value not in distribution
        assertEquals("Frequency of value 4 is 0", 0, frequency.getCount(4));
        assertEquals("Frequency of value 4 is 0", 0.0, frequency.getPct(4), 0.001);
        assertEquals("Frequency of value 4 is 0", 0.0, frequency.getCumPct(4), 0.001);

        // Test adding duplicate values
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals("Frequency of value 1 is 4", 4, frequency.getCount(1));
        assertEquals("Frequency of value 1 is 4", 0.8, frequency.getPct(1), 0.001);
        assertEquals("Frequency of value 1 is 4", 0.16, frequency.getCumPct(1), 0.001);

        // Test adding different types of values
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('a');

        // Test magnitudes of different types of values
        assertEquals("Distribution has 8 values", 8, frequency.getSumFreq());

        // Test single character frequency
        assertEquals("Frequency of character 'a' is 4", 4, frequency.getCount('a'));
        assertEquals("Frequency of character 'a' is 4", 0.5, frequency.getPct('a'), 0.001);
        assertEquals("Frequency of character 'a' is 4", 0.25, frequency.getCumPct('a'), 0.001);

        // Test single character frequency with comparator
        Comparator<Character> comparator = (o1, o2) -> Character.compare(o1, o2);
        Frequency frequencyComparator = new Frequency(comparator);
        frequencyComparator.addValue('z');
        frequencyComparator.addValue('a');
        frequencyComparator.addValue('z');
        frequencyComparator.addValue('a');

        // Test magnitudes of different types of values with comparator
        assertEquals("Distribution has 6 values", 6, frequencyComparator.getSumFreq());

        // Test single character frequency with comparator
        assertEquals("Frequency of character 'a' is 2", 2, frequencyComparator.getCount('a'));
        assertEquals("Frequency of character 'a' is 2", 0.33, frequencyComparator.getPct('a'), 0.001);
        assertEquals("Frequency of character 'a' is 2", 0.17, frequencyComparator.getCumPct('a'), 0.001);

        // Test single character frequency with comparator, value out of order
        assertEquals("Frequency of character 'z' is 2", 2, frequencyComparator.getCount('z'));
        assertEquals("Frequency of character 'z' is 2", 0.33, frequencyComparator.getPct('z'), 0.001);
        assertEquals("Frequency of character 'z' is 2", 0.5, frequencyComparator.getCumPct('z'), 0.001);
    }

    @Test
    public void testFrequencyComparatorConstructor() {
        Comparator<String> comparator = (o1, o2) -> o1.compareTo(o2);
        Frequency frequencyComparator = new Frequency(comparator);

        // Test adding values
        frequencyComparator.addValue("one");
        frequencyComparator.addValue("one");
        frequencyComparator.addValue("two");
        frequencyComparator.addValue("one");
        frequencyComparator.addValue("three");

        // Test magnitudes of strings
        assertEquals("Distribution has 5 values", 5, frequencyComparator.getSumFreq());

        // Test single string frequency
        assertEquals("Frequency of value 'one' is 3", 3, frequencyComparator.getCount("one"));
        assertEquals("Frequency of value 'one' is 3", 0.6, frequencyComparator.getPct("one"), 0.001);
        assertEquals("Frequency of value 'one' is 3", 0.12, frequencyComparator.getCumPct("one"), 0.001);

        // Test value not in distribution
        assertEquals("Frequency of value 'four' is 0", 0, frequencyComparator.getCount("four"));
        assertEquals("Frequency of value 'four' is 0", 0.0, frequencyComparator.getPct("four"), 0.001);
        assertEquals("Frequency of value 'four' is 0", 0.0, frequencyComparator.getCumPct("four"), 0.001);

        // Test adding duplicate values
        frequencyComparator.addValue("one");
        frequencyComparator.addValue("one");
        assertEquals("Frequency of value 'one' is 4", 4, frequencyComparator.getCount("one"));
        assertEquals("Frequency of value 'one' is 0.8", 0.8, frequencyComparator.getPct("one"), 0.001);
        assertEquals("Frequency of value 'one' is 0.4", 0.4, frequencyComparator.getCumPct("one"), 0.001);

        // Test adding different strings
        frequencyComparator.addValue("three");
        assertEquals("Frequency of value 'three' is 1", 1, frequencyComparator.getCount("three"));
        assertEquals("Frequency of value 'three' is 0.2", 0.2, frequencyComparator.getPct("three"), 0.001);
        assertEquals("Frequency of value 'three' is 0.1", 0.1, frequencyComparator.getCumPct("three"), 0.001);

        // Test adding strings out of order
        frequencyComparator.addValue("two");
        assertEquals("Frequency of value 'two' is 1", 1, frequencyComparator.getCount("two"));
        assertEquals("Frequency of value 'two' is 0.2", 0.2, frequencyComparator.getPct("two"), 0.001);
        assertEquals("Frequency of value 'two' is 0.2", 0.2, frequencyComparator.getCumPct("two"), 0.001);
    }
}