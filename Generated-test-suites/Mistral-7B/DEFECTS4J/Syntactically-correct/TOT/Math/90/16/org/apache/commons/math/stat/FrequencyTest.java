package org.apache.commons.math.stat.test;

import static org.junit.Assert.*;
import org.apache.commons.math.stat.Frequency;

import java.util.Comparator;

/**
 * Tests for the {@link Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test default constructor and basic functionality.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        // Add some values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        // Test basic functionality
        assertEquals(3, frequency.getCount(1));
        assertEquals(3, frequency.getCount(1L));
        assertEquals(3, frequency.getCount(1L, new NaturalComparator()));
        assertEquals(3, frequency.getCount('1'));
        assertEquals(3, frequency.getCount('1', new NaturalComparator()));
        assertEquals(3, frequency.getCount(1, null));

        assertEquals(6, frequency.getCount(1), 0.0);
        assertEquals(6, frequency.getCount(1), 0.01);
        assertEquals(6.0 / 11.0, frequency.getPct(1), 0.01);
        assertEquals(6.0 / 11.0, frequency.getPct(1L), 0.01);
        assertEquals(6.0 / 11.0, frequency.getPct(1L, new NaturalComparator()), 0.01);
        assertEquals(6.0 / 11.0, frequency.getPct('1'), 0.01);
        assertEquals(6.0 / 11.0, frequency.getPct('1', new NaturalComparator()), 0.01);
        assertEquals(6.0 / 11.0, frequency.getPct(1, null), 0.01);

        assertEquals(2, frequency.getCount(2));
        assertEquals(2, frequency.getCount(2L));
        assertEquals(2, frequency.getCount(2L, new NaturalComparator()));
        assertEquals(2, frequency.getCount('2'));
        assertEquals(2, frequency.getCount('2', new NaturalComparator()));
        assertEquals(2, frequency.getCount(2, null));

        assertEquals(4, frequency.getCount(2), 0.0);
        assertEquals(4, frequency.getCount(2), 0.01);
        assertEquals(4.0 / 11.0, frequency.getPct(2), 0.01);
        assertEquals(4.0 / 11.0, frequency.getPct(2L), 0.01);
        assertEquals(4.0 / 11.0, frequency.getPct(2L, new NaturalComparator()), 0.01);
        assertEquals(4.0 / 11.0, frequency.getPct('2'), 0.01);
        assertEquals(4.0 / 11.0, frequency.getPct('2', new NaturalComparator()), 0.01);
        assertEquals(4.0 / 11.0, frequency.getPct(2, null), 0.01);

        assertEquals(3, frequency.getCount(3));
        assertEquals(3, frequency.getCount(3L));
        assertEquals(3, frequency.getCount(3L, new NaturalComparator()));
        assertEquals(3, frequency.getCount('3'));
        assertEquals(3, frequency.getCount('3', new NaturalComparator()));
        assertEquals(3, frequency.getCount(3, null));

        assertEquals(3, frequency.getCount(3), 0.0);
        assertEquals(3, frequency.getCount(3), 0.01);
        assertEquals(3.0 / 11.0, frequency.getPct(3), 0.01);
        assertEquals(3.0 / 11.0, frequency.getPct(3L), 0.01);
        assertEquals(3.0 / 11.0, frequency.getPct(3L, new NaturalComparator()), 0.01);
        assertEquals(3.0 / 11.0, frequency.getPct('3'), 0.01);
        assertEquals(3.0 / 11.0, frequency.getPct('3', new NaturalComparator()), 0.01);
        assertEquals(3.0 / 11.0, frequency.getPct(3, null), 0.01);

        assertEquals(9, frequency.getSumFreq());
        assertEquals(9, frequency.getSumFreq(), 0.0);

        // Test getCumFreq and getCumPct
        assertEquals(3, frequency.getCumFreq(1));
        assertEquals(6, frequency.getCumFreq(2));
        assertEquals(9, frequency.getCumFreq(3));

        assertEquals(0.27272727272727273, frequency.getCumPct(1), 0.0000001);
        assertEquals(0.5454545454545455, frequency.getCumPct(2), 0.0000001);
        assertEquals(1, frequency.getCumPct(3), 0.0000001);
    }

    /**
     * Test constructor with Comparator.
     */
    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable)o1).compareTo(o2);
            }
        });

        // Add some values
        frequency.addValue(3);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        // Test basic functionality
        assertEquals(3, frequency.getCount(3));
        assertEquals(3, frequency.getCount(3L));
        assertEquals(3, frequency.getCount(3L, new NaturalComparator()));
        assertEquals(3, frequency.getCount('3'));
        assertEquals(3, frequency.getCount('3', new NaturalComparator()));
        assertEquals(3, frequency.getCount(3, null));

        assertEquals(3, frequency.getCount(3), 0.0);
        assertEquals(3, frequency.getCount(3), 0.01);
        assertEquals(3.0 / 9.0, frequency.getPct(3), 0.01);
        assertEquals(3.0 / 9.0, frequency.getPct(3L), 0.01);
        assertEquals(3.0 / 9.0, frequency.getPct(3L, new NaturalComparator()), 0.01);
        assertEquals(3.0 / 9.0, frequency.getPct('3'), 0.01);
        assertEquals(3.0 / 9.0, frequency.getPct('3', new NaturalComparator()), 0.01);
        assertEquals(3.0 / 9.0, frequency.getPct(3, null), 0.01);

        assertEquals(1, frequency.getCount(1));
        assertEquals(1, frequency.getCount(1L));
        assertEquals(1, frequency.getCount(1L, new NaturalComparator()));
        assertEquals(1, frequency.getCount('1'));
        assertEquals(1, frequency.getCount('1', new NaturalComparator()));
        assertEquals(1, frequency.getCount(1, null));

        assertEquals(1, frequency.getCount(1), 0.0);
        assertEquals(1, frequency.getCount(1), 0.01);
        assertEquals(1.0 / 9.0, frequency.getPct(1), 0.01);
        assertEquals(1.0 / 9.0, frequency.getPct(1L), 0.01);
        assertEquals(1.0 / 9.0, frequency.getPct(1L, new NaturalComparator()), 0.01);
        assertEquals(1.0 / 9.0, frequency.getPct('1'), 0.01);
        assertEquals(1.0 / 9.0, frequency.getPct('1', new NaturalComparator()), 0.01);
        assertEquals(1.0 / 9.0, frequency.getPct(1, null), 0.01);

        assertEquals(2, frequency.getCount(2));
        assertEquals(2, frequency.getCount(2L));
        assertEquals(2, frequency.getCount(2L, new NaturalComparator()));
        assertEquals(2, frequency.getCount('2'));
        assertEquals(2, frequency.getCount('2', new NaturalComparator()));
        assertEquals(2, frequency.getCount(2, null));

        assertEquals(2, frequency.getCount(2), 0.0);
        assertEquals(2, frequency.getCount(2), 0.01);
        assertEquals(2.0 / 9.0, frequency.getPct(2), 0.01);
        assertEquals(2.0 / 9.0, frequency.getPct(2L), 0.01);
        assertEquals(2.0 / 9.0, frequency.getPct(2L, new NaturalComparator()), 0.01);
        assertEquals(2.0 / 9.0, frequency.getPct('2'), 0.01);
        assertEquals(2.0 / 9.0, frequency.getPct('2', new NaturalComparator()), 0.01);
        assertEquals(2.0 / 9.0, frequency.getPct(2, null), 0.01);

        assertEquals(4, frequency.getCount(2), 0.0);
        assertEquals(4, frequency.getCount(2), 0.01);
        assertEquals(2.0 / 3.0, frequency.getPct(2), 0.01);
        assertEquals(2.0 / 3.0, frequency.getPct(2L), 0.01);
        assertEquals(2.0 / 3.0, frequency.getPct(2L, new NaturalComparator()), 0.01);
        assertEquals(2.0 / 3.0, frequency.getPct('2'), 0.01);
        assertEquals(2.0 / 3.0, frequency.getPct('2', new NaturalComparator()), 0.01);
        assertEquals(2.0 / 3.0, frequency.getPct(2, null), 0.01);

        assertEquals(5, frequency.getSumFreq());
        assertEquals(5, frequency.getSumFreq(), 0.0);

        // Test getCumFreq and getCumPct
        assertEquals(3, frequency.getCumFreq(3));
        assertEquals(6, frequency.getCumFreq(2));
        assertEquals(9, frequency.getCumFreq(1));

        assertEquals(0.6, frequency.getCumPct(3), 0.00001);
        assertEquals(1.0, frequency.getCumPct(2), 0.00001);
        assertEquals(1.0, frequency.getCumPct(1), 0.00001);
    }

    /**
     * Test exception handling when adding uncomparable values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueException() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
    }

    /**
     * Test exception handling when adding non-Comparable values.
     */
    @Test(expected = ClassCastException.class)
    public void testAddNonComparableValueException() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object() {
            // Non-Comparable value
        });
    }

    /**
     * Test exception handling when comparing non-Comparable values.
     */
    @Test(expected = ClassCastException.class)
    public void testCompareNonComparableValuesException() {
        Frequency frequency = new Frequency();
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                throw new ClassCastException();
            }
        };
        try {
            frequency.addValue(1);
            frequency.addValue(1);
            frequency.addValue(new Object() {
                // Non-Comparable value
            });
            frequency.valuesIterator();
            frequency.values();
            frequency.clear();
            frequency.addValue(1, comparator);
            frequency.addValue(1, comparator);
            frequency.addValue(1, comparator);
            frequency.valuesIterator();
            frequency.values();
            frequency.clear();
        } catch (NoSuchElementException e) {
            // expected
        }
    }
}