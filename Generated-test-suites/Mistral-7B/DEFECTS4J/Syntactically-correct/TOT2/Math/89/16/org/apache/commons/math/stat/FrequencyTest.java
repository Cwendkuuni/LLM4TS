import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        assertNotNull(frequency);
    }

    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(new NaturalComparator());
        assertNotNull(frequency);
    }

    @Test
    public void testAddValueWithInteger() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long count = frequency.getCount(1);
        assertEquals(1, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue("not comparible");
    }

    @Test
    public void testAddValueWithCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        long count = frequency.getCount('a');
        assertEquals(1, count);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        long count = frequency.getCount(1);
        assertEquals(0, count);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator iterator = frequency.valuesIterator();
        long firstValue = (Long) iterator.next();
        long secondValue = (Long) iterator.next();
        assertEquals(1, firstValue.longValue());
        assertEquals(2, secondValue.longValue());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long sumFreq = frequency.getSumFreq();
        assertEquals(3, sumFreq);
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long count = frequency.getCount(1);
        assertEquals(1, count);
        long count2 = frequency.getCount(2);
        assertEquals(1, count2);
        long count3 = frequency.getCount('a');
        assertEquals(0, count3);
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        double pct = frequency.getPct(1);
        assertEquals(0.333, pct, 0.001);
        double pct2 = frequency.getPct(2);
        assertEquals(0.667, pct2, 0.001);
        double pct3 = frequency.getPct('a');
        assertEquals(0.0, pct3, 0.0);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(1);
        assertEquals(1, cumFreq);
        long cumFreq2 = frequency.getCumFreq(2);
        assertEquals(3, cumFreq2);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        double cumPct = frequency.getCumPct(1);
        assertEquals(0.333, cumPct, 0.001);
        double cumPct2 = frequency.getCumPct(2);
        assertEquals(0.667, cumPct2, 0.001);
        double cumPct3 = frequency.getCumPct(3);
        assertEquals(1.0, cumPct3, 0.001);
    }
}