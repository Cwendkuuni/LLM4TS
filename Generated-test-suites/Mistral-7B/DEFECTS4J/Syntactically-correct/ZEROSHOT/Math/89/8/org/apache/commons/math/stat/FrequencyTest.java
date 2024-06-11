import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testEmptyFrequency() {
        Frequency frequency = new Frequency();
        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount('a'));
        assertEquals(0, frequency.getCumFreq(1));
        assertEquals(0, frequency.getCumFreq('a'));
        assertEquals(0, frequency.getCumPct(1), 0.0);
        assertEquals(0, frequency.getCumPct('a'), 0.0);
    }

    @Test
    public void testFrequencyWithIntegerValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(1.0 / 3.0, frequency.getPct(1), 0.001);
        assertEquals(2.0 / 3.0, frequency.getPct(2), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(2), 0.001);

        assertArrayEquals(new Long[]{1L, 2L}, frequency.valuesIterator().toArray());
    }

    @Test
    public void testFrequencyWithLongValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(1.0 / 3.0, frequency.getPct(1), 0.001);
        assertEquals(2.0 / 3.0, frequency.getPct(2), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(2), 0.001);

        assertArrayEquals(new Object[]{1L, 2L}, frequency.valuesIterator().toArray());
    }

    @Test
    public void testFrequencyWithCharacterValues() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        assertEquals(3, frequency.getSumFreq());
        assertEquals(2, frequency.getCount('a'));
        assertEquals(1, frequency.getCount('b'));
        assertEquals(1, frequency.getCumFreq('a'));
        assertEquals(2, frequency.getCumFreq('a'));
        assertEquals(1.0 / 3.0, frequency.getPct('a'), 0.001);
        assertEquals(2.0 / 3.0, frequency.getPct('b'), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct('a'), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct('a'), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct('b'), 0.001);

        assertArrayEquals(new Object[]{'a', 'b'}, frequency.valuesIterator().toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidValueInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(new Object(){});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('1');
    }

    @Test
    public void testClearFrequency() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        assertEquals(3, frequency.getSumFreq());
        assertEquals(2, frequency.getCount(1));
        assertEquals(1, frequency.getCount(2));
        assertEquals(1, frequency.getCumFreq(1));
        assertEquals(2, frequency.getCumFreq(1));
        assertEquals(1.0 / 3.0, frequency.getPct(1), 0.001);
        assertEquals(2.0 / 3.0, frequency.getPct(2), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(2.0 / 3.0, frequency.getCumPct(1), 0.001);
        assertEquals(1.0 / 3.0, frequency.getCumPct(2), 0.001);

        frequency.clear();

        assertEquals(0, frequency.getSumFreq());
        assertEquals(0, frequency.getCount(1));
        assertEquals(0, frequency.getCount(2));
        assertEquals(0, frequency.getCumFreq(1));
        assertEquals(0, frequency.getCumFreq(1));
        assertNull(frequency.getCumPct(1));
        assertNull(frequency.getCumPct(2));
    }
}