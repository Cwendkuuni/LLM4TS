import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator comparator = (o1, o2) -> o1.compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        assertNotNull(frequency);
        assertEquals(0, frequency.getSumFreq());
        assertTrue(frequency.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableType() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals(1, frequency.getCount(1));
        assertEquals(1.0, frequency.getPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(1), 0.001);
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(1, frequency.getSumFreq());
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        assertEquals(1, frequency.getCount(2));
        assertEquals(1.0, frequency.getPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);
        assertEquals(1, frequency.getCumFreq(2));
        assertEquals(2, frequency.getSumFreq());
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        assertEquals(1, frequency.getCount('A'));
        assertEquals(1.0, frequency.getPct('A'), 0.001);
        assertEquals(1.0, frequency.getCumPct('A'), 0.001);
        assertEquals(1, frequency.getCumFreq('A'));
        assertEquals(2, frequency.getSumFreq());
    }

    @Test
    public void testAddValueComparable() {
        Frequency frequency = new Frequency();
        Comparable value = 1;
        frequency.addValue(value);
        assertEquals(1, frequency.getCount(value));
        assertEquals(1.0, frequency.getPct(value), 0.001);
        assertEquals(1.0, frequency.getCumPct(value), 0.001);
        assertEquals(1, frequency.getCumFreq(value));
        assertEquals(1, frequency.getSumFreq());
    }

    @Test
    public void testAddValueMultipleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
        assertEquals(2.0, frequency.getPct(1), 0.001);
        assertEquals(2.0, frequency.getPct(2), 0.001);
        assertEquals(4.0, frequency.getCumPct(2), 0.001);
        assertEquals(4.0, frequency.getCumPct(1), 0.001);
        assertEquals(4, frequency.getCumFreq(1));
        assertEquals(4, frequency.getCumFreq(2));
        assertEquals(6, frequency.getSumFreq());
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        assertEquals(0, frequency.getSumFreq());
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);

        Iterator iterator = frequency.valuesIterator();
        assertEquals(1, iterator.next());
        assertEquals(2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        assertEquals(6, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        assertEquals(2, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        assertEquals(0.33, frequency.getPct(1), 0.001);
        assertEquals(0.67, frequency.getPct(2), 0.001);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        assertEquals(0.33, frequency.getPct(1), 0.001);
        assertEquals(0.67, frequency.getCumPct(2), 0.001);
        assertEquals(1.0, frequency.getCumPct(3), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(2));
    }
}