import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(new java.util.Collections.reverseOrder());
        assertTrue(frequency.isEmpty());
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        Iterator iterator = frequency.valuesIterator();
        Long value = null;
        while (iterator.hasNext()) {
            value = (Long) iterator.next();
            assertEquals(3, frequency.getCount(value));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArgument() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        Iterator iterator = frequency.valuesIterator();
        Character value = null;
        while (iterator.hasNext()) {
            value = (Character) iterator.next();
            assertEquals(2, frequency.getCount(value));
        }
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(6, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        assertEquals(3, frequency.getCount(1L));
        assertEquals(3, frequency.getCount(1));
        assertEquals(3, frequency.getCount(1L));
        assertEquals(2, frequency.getCount(2L));
        assertEquals(2, frequency.getCount(2));
        assertEquals(2, frequency.getCount(2L));
        assertEquals(1, frequency.getCount(3L));
        assertEquals(1, frequency.getCount(3));
        assertEquals(1, frequency.getCount(3L));
        assertEquals(0, frequency.getCount('a'));
        assertEquals(0, frequency.getCount('a'));
        assertEquals(0, frequency.getCount('a'));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        double total = 0.0;
        Iterator iterator = frequency.valuesIterator();
        Long value = null;
        while (iterator.hasNext()) {
            value = (Long) iterator.next();
            total += getPct(frequency, value);
        }
        assertEquals(1.0, total, 0.001);
    }

    @Test
    public void testGetPctInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        double total = 0.0;
        Iterator iterator = frequency.valuesIterator();
        int value = 0;
        while (iterator.hasNext()) {
            value = ((Long) iterator.next()).intValue();
            total += getPct(frequency, value);
        }
        assertEquals(1.0, total, 0.001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        double total = 0.0;
        Iterator iterator = frequency.valuesIterator();
        Long value = null;
        while (iterator.hasNext()) {
            value = (Long) iterator.next();
            total += getPct(frequency, value.longValue());
        }
        assertEquals(1.0, total, 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        Long value = null;
        Long expected = 0L;
        Iterator iterator = frequency.valuesIterator();
        while (iterator.hasNext()) {
            value = (Long) iterator.next();
            assertEquals(expected, frequency.getCumFreq(value));
            expected += frequency.getCount(value);
        }
    }

    @Test
    public void testGetCumFreqInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        int value = 0;
        int expected = 0;
        Iterator iterator = frequency.valuesIterator();
        while (iterator.hasNext()) {
            value = ((Long) iterator.next()).intValue();
            assertEquals(expected, frequency.getCumFreq(value));
            expected += frequency.getCount(value);
        }
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        long value = 0;
        long expected = 0;
        Iterator iterator = frequency.valuesIterator();
        while (iterator.hasNext()) {
            value = ((Long) iterator.next()).longValue();
            assertEquals(expected, frequency.getCumFreq(value));
            expected += frequency.getCount(value);
        }
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        Long value = null;
        Double expected = 0.0;
        Iterator iterator = frequency.valuesIterator();
        while (iterator.hasNext()) {
            value = (Long) iterator.next();
            assertEquals(expected, frequency.getCumPct(value), 0.001);
            expected += getPct(frequency, value);
        }
    }

    @Test
    public void testGetCumPctInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        int value = 0;
        double expected = 0.0;
        Iterator iterator = frequency.valuesIterator();
        while (iterator.hasNext()) {
            value = ((Long) iterator.next()).intValue();
            assertEquals(expected, frequency.getCumPct(value), 0.001);
            expected += getPct(frequency, value);
        }
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        long value = 0;
        double expected = 0.0;
        Iterator iterator = frequency.valuesIterator();
        while (iterator.hasNext()) {
            value = ((Long) iterator.next()).longValue();
            assertEquals(expected, frequency.getCumPct(value), 0.001);
            expected += getPct(frequency, value);
        }
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        assertTrue(frequency.isEmpty());
    }

    private double getPct(Frequency frequency, long value) {
        long count = frequency.getCount(value);
        return (double) count / frequency.getSumFreq();
    }

    private double getPct(Frequency frequency, int value) {
        long count = frequency.getCount(value);
        return (double) count / frequency.getSumFreq();
    }
}