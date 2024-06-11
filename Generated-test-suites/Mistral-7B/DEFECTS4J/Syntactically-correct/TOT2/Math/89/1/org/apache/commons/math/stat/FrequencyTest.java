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
        Assert.assertNotNull(frequency);
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testConstructorWithComparator() {
        Comparator comparator = new NaturalComparator();
        Frequency frequency = new Frequency(comparator);
        Assert.assertNotNull(frequency);
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        String result = frequency.toString();
        String expected = "Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.3333333333333333 \t 0.3333333333333333\n2 \t 2 \t 0.3333333333333333 \t 0.6666666666666667\n\n";
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue("notComparable");
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long count = frequency.getCount(1);
        Assert.assertEquals(2, count);
        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals(4, sumFreq);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        Iterator iterator = frequency.valuesIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next().longValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next().longValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next().longValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next().longValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long sumFreq = frequency.getSumFreq();
        Assert.assertEquals(4, sumFreq);
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long count = frequency.getCount(1);
        Assert.assertEquals(2, count);
        count = frequency.getCount(2);
        Assert.assertEquals(2, count);
        count = frequency.getCount(3);
        Assert.assertEquals(0, count);
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double pct = frequency.getPct(1);
        Assert.assertEquals(0.3333333333333333, pct, 0.00001);
        pct = frequency.getPct(2);
        Assert.assertEquals(0.3333333333333333, pct, 0.00001);
        pct = frequency.getPct("notComparable");
        Assert.assertNull(pct);
    }

    @Test   
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        long cumFreq = frequency.getCumFreq(1);
        Assert.assertEquals(1, cumFreq);
        cumFreq = frequency.getCumFreq(2);
        Assert.assertEquals(3, cumFreq);
        cumFreq = frequency.getCumFreq(3);
        Assert.assertEquals(4, cumFreq);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        double cumPct = frequency.getCumPct(1);
        Assert.assertEquals(0.25, cumPct, 0.0001);
        cumPct = frequency.getCumPct(2);
        Assert.assertEquals(0.5, cumPct, 0.0001);
        cumPct = frequency.getCumPct("notComparable");
        Assert.assertNull(cumPct);
    }
}