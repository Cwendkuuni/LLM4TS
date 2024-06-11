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
        Assert.assertEquals("Default constructor should initialize an empty Frequency distribution", 0, frequency.getSumFreq());
    }

    @Test
    public void testConstructorWithComparator() {
        Comparator<Object> comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        Assert.assertEquals("Constructor with Comparator should initialize an empty Frequency distribution", 0, frequency.getSumFreq());
    }

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals("Adding an int value should increment the frequency count by 1", 1, frequency.getCount(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNull() {
        Frequency frequency = new Frequency();
        frequency.addValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNotComparable() {
        Frequency frequency = new Frequency();
        frequency.addValue("notComparable");
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        Assert.assertEquals("Adding a long value should increment the frequency count by 1", 1, frequency.getCount(1));
    }

    @Test
    public void testAddValueChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        Assert.assertEquals("Adding a char value should increment the frequency count by 1", 1, frequency.getCount('a'));
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 1 \t 0.1666666666666667 \t 0.5\n";
        Assert.assertEquals("toString method should correctly format the frequency distribution", expected, frequency.toString());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Assert.assertEquals("getSumFreq method should correctly sum up all frequency counts", 4, frequency.getSumFreq());
    }

    @Test
    public void testGetCountInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Assert.assertEquals("getCount method should correctly retrieve the frequency count for an int value", 2, frequency.getCount(1));
    }

    @Test
    public void testGetCountLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        Assert.assertEquals("getCount method should correctly retrieve the frequency count for a long value", 2, frequency.getCount(1L));
    }

    @Test
    public void testGetCountChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        Assert.assertEquals("getCount method should correctly retrieve the frequency count for a char value", 2, frequency.getCount('a'));
    }

    @Test
    public void testGetPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Assert.assertEquals("getPct method should correctly calculate the percentage of an int value", 0.3333333333333333, frequency.getPct(1), 0.001);
    }

    @Test
    public void testGetPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        Assert.assertEquals("getPct method should correctly calculate the percentage of a long value", 0.3333333333333333, frequency.getPct(1L), 0.001);
    }

    @Test
    public void testGetPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        Assert.assertEquals("getPct method should correctly calculate the percentage of a char value", 0.3333333333333333, frequency.getPct('a'), 0.001);
    }

    @Test
    public void testGetCumFreqInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Assert.assertEquals("getCumFreq method should correctly retrieve the cumulative frequency count for an int value", 2, frequency.getCumFreq(1));
    }

    @Test
    public void testGetCumFreqLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        Assert.assertEquals("getCumFreq method should correctly retrieve the cumulative frequency count for a long value", 3, frequency.getCumFreq(1L));
    }

    @Test
    public void testGetCumFreqChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        Assert.assertEquals("getCumFreq method should correctly retrieve the cumulative frequency count for a char value", 2, frequency.getCumFreq('a'));
    }

    @Test
    public void testGetCumPctInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Assert.assertEquals("getCumPct method should correctly calculate the cumulative percentage of an int value", 0.6, frequency.getCumPct(1), 0.001);
    }

    @Test
    public void testGetCumPctLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(1L);
        Assert.assertEquals("getCumPct method should correctly calculate the cumulative percentage of a long value", 0.6, frequency.getCumPct(1L), 0.001);
    }

    @Test
    public void testGetCumPctChar() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        Assert.assertEquals("getCumPct method should correctly calculate the cumulative percentage of a char value", 0.6, frequency.getCumPct('a'), 0.001);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        Assert.assertEquals("clear method should empty the frequency distribution", 0, frequency.getSumFreq());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Iterator<Object> iterator = frequency.valuesIterator();
        Assert.assertEquals("Values iterator should iterate over all values in the frequency distribution", 1, iterator.next());
        Assert.assertEquals("Values iterator should iterate over all values in the frequency distribution", 2, iterator.next());
        Assert.assertFalse("Values iterator should not have any more values after iterating over all of them", iterator.hasNext());
    }
}