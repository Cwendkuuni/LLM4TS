package org.apache.commons.math.stat.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.Frequency.NaturalComparator;
import org.junit.Test;

/**
 * Test class for {@link Frequency Frequency} class.
 */
public class FrequencyTest {

    /**
     * Test case for default constructor and basic methods.
     */
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
        assertNull(frequency.valuesIterator().next());
    }

    /**
     * Test case for constructor with comparator.
     */
    @Test
    public void testComparatorConstructor() {
        Comparator comparator = (o1, o2) -> ((Comparable<?>) o1).compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
        assertNull(frequency.valuesIterator().next());
    }

    /**
     * Test case for addValue method with different data types.
     */
    @Test
    public void testAddValueWithDifferentDataTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a'); // should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> frequency.addValue('a'));
        frequency.addValue("test"); // should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> frequency.addValue("test"));
        assertThrows(IllegalArgumentException.class, () -> frequency.addValue(new Object()));
        assertThrows(IllegalArgumentException.class, () -> frequency.addValue(new Object[]{}));
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('b');
        frequency.addValue("test");
        frequency.addValue(new Object());
        frequency.addValue(new Object[]{});
        assertEquals(6, frequency.getSumFreq());
        assertEquals(3, frequency.getCount(1));
        assertEquals(3, frequency.getCount(2));
        assertEquals(1, frequency.getCount('b'));
        assertEquals(1, frequency.getCount("test"));
        assertEquals(1, frequency.getCount(new Object()));
        assertEquals(1, frequency.getCount(new Object[]{}));
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getPct(2), 0.001);
        assertEquals(0.16666666666666663, frequency.getPct('b'), 0.001);
        assertEquals(0.16666666666666663, frequency.getPct("test"), 0.001);
        assertEquals(0.16666666666666663, frequency.getPct(new Object()), 0.001);
        assertEquals(0.16666666666666663, frequency.getPct(new Object[]{}), 0.001);
        assertEquals(0.33333333333333335, frequency.getPct(1.0), 0.001);
        assertEquals(0.33333333333333335, frequency.getPct(2.0), 0.001);
        assertEquals(0.16666666666666663, frequency.getPct('b'), 0.001);
        assertEquals(0.16666666666666663, frequency.getPct("test"), 0.001);
        assertEquals(0.16666666666666663, frequency.getPct(new Object()), 0.001);
        assertEquals(0.16666666666666663, frequency.getPct(new Object[]{}), 0.001);
        assertEquals(0.25, frequency.getPct(1.0), 0.001);
        assertEquals(0.25, frequency.getPct(2.0), 0.001);
        assertEquals(0.06666666666666667, frequency.getPct('b'), 0.001);
        assertEquals(0.06666666666666667, frequency.getPct("test"), 0.001);
        assertEquals(0.06666666666666667, frequency.getPct(new Object()), 0.001);
        assertEquals(0.06666666666666667, frequency.getPct(new Object[]{}), 0.001);
        assertEquals(0.16666666666666663, frequency.getCumPct(1), 0.001);
        assertEquals(0.33333333333333335, frequency.getCumPct(2), 0.001);
        assertEquals(0.4999999999999999, frequency.getCumPct('b'), 0.001);
        assertEquals(1.0, frequency.getCumPct('c'), 0.001);
        assertEquals(1.0, frequency.getCumPct("test"), 0.001);
        assertEquals(1.0, frequency.getCumPct(new Object()), 0.001);
        assertEquals(1.0, frequency.getCumPct(new Object[]{}), 0.001);
    }

    /**
     * Test case for addValue method with the same data.
     */
    @Test
    public void testAddValueWithTheSameData() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(2, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getCumPct(1), 0.001);
    }

    /**
     * Test case for addValue method with different data types and the same value.
     */
    @Test
    public void testAddValueWithDifferentDataTypesAndTheSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(new Integer(1));
        frequency.addValue(1L);
        assertEquals(3, frequency.getSumFreq());
        assertEquals(3, frequency.getCount(1));
        assertEquals(1.0, frequency.getPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(1), 0.001);
    }

    /**
     * Test case for clear method.
     */
    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertNull(frequency.toString());
        assertNull(frequency.valuesIterator().next());
    }
}