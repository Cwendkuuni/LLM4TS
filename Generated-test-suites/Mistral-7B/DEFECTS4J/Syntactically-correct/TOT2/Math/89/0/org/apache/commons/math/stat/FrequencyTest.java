import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        // Basic functionality test
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        // Edge case: empty frequency distribution
        Frequency emptyFrequency = new Frequency();
        Assert.assertEquals(0, emptyFrequency.getCount(1));
        // Error scenario: adding non-comparable value
        Assert.throwsException(IllegalArgumentException.class, () -> frequency.addValue('a'));
    }

    @Test
    public void testComparatorConstructor() {
        Comparator<Comparable<?>> comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(2);
        frequency.addValue(1);
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(1, frequency.getCount(1));
        // Edge case: frequency distribution with comparator, empty frequency distribution
        Frequency emptyFrequency = new Frequency(comparator);
        Assert.assertEquals(0, emptyFrequency.getCount(1));
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.4 \t 0.4 \t\n2 \t 1 \t 0.3 \t 0.7 \t\n";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.clear();
        Assert.assertEquals(0, frequency.getCount(1));
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Comparable<?>> iterator = frequency.valuesIterator();
        Assert.assertEquals(1, iterator.next().compareTo(1));
        Assert.assertEquals(2, iterator.next().compareTo(2));
        // Edge case: empty frequency distribution
        Frequency emptyFrequency = new Frequency();
        iterator = emptyFrequency.valuesIterator();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(3, frequency.getSumFreq());
        // Edge case: empty frequency distribution
        Frequency emptyFrequency = new Frequency();
        Assert.assertEquals(0, emptyFrequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        // Edge case: non-existent value
        Frequency emptyFrequency = new Frequency();
        Assert.assertEquals(0, emptyFrequency.getCount('a'));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(0.4, frequency.getPct(1), 0.0);
        Assert.assertEquals(0.3, frequency.getPct(2), 0.0);
        // Edge case: non-existent value
        Frequency emptyFrequency = new Frequency();
        Assert.assertNull(frequency.getPct('a'));
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(1, frequency.getCumFreq(1));
        Assert.assertEquals(2, frequency.getCumFreq(2));
        Assert.assertEquals(3, frequency.getCumFreq(3));
        // Edge case: non-existent value
        Frequency emptyFrequency = new Frequency();
        Assert.assertEquals(0, emptyFrequency.getCumFreq(1));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Assert.assertEquals(0.4, frequency.getPct(1), 0.0);
        Assert.assertEquals(0.7, frequency.getCumPct(2), 0.0);
        Assert.assertEquals(1.0, frequency.getCumPct(3), 0.0);
        // Edge case: non-existent value
        Frequency emptyFrequency = new Frequency();
        Assert.assertEquals(0.0, frequency.getCumPct(1), 0.0);
    }
}