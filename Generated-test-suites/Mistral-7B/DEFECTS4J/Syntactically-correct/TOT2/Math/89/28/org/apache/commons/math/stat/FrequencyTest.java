import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        Long count1 = (Long) frequency.freqTable.get(1);
        Long count2 = (Long) frequency.freqTable.get(2);
        Long count3 = (Long) frequency.freqTable.get(3);

        Assert.assertEquals(2, count1.longValue());
        Assert.assertEquals(3, count2.longValue());
        Assert.assertEquals(3, count3.longValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithUncomparableValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');

        // This line will throw an IllegalArgumentException
        frequency.addValue(1);
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        long sumFreq = frequency.getSumFreq();

        Assert.assertEquals(7, sumFreq);
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        long count1 = frequency.getCount(1);
        long count2 = frequency.getCount(2);
        long count3 = frequency.getCount(3);

        Assert.assertEquals(2, count1);
        Assert.assertEquals(3, count2);
        Assert.assertEquals(3, count3);
    }

    @Test
    public void testGetZeroCountValue() {
        Frequency frequency = new Frequency();
        long zeroCountValue = frequency.getCount('z');

        Assert.assertEquals(0, zeroCountValue);
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        double pct1 = frequency.getPct(1);
        double pct2 = frequency.getPct(2);
        double pct3 = frequency.getPct(3);

        Assert.assertEquals(0.2857142857142857, pct1, 0.00000000000001); // Approximate result
        Assert.assertEquals(0.4285714285714286, pct2, 0.00000000000001); // Approximate result
        Assert.assertEquals(0.2857142857142857, pct3, 0.00000000000001); // Approximate result
    }

    @Test
    public void testGetNegativeValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(-1);

        long count1 = (Long) frequency.freqTable.get(1);
        long count2 = (Long) frequency.freqTable.get(-1);

        Assert.assertEquals(2, count1.longValue());
        Assert.assertEquals(0, count2.longValue());
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        long cumFreq1 = frequency.getCumFreq(1);
        long cumFreq2 = frequency.getCumFreq(2);
        long cumFreq3 = frequency.getCumFreq(3);

        Assert.assertEquals(1, cumFreq1);
        Assert.assertEquals(3, cumFreq2);
        Assert.assertEquals(6, cumFreq3);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        double cumPct1 = frequency.getCumPct(1);
        double cumPct2 = frequency.getCumPct(2);
        double cumPct3 = frequency.getCumPct(3);

        Assert.assertEquals(0.1428571428571429, cumPct1, 0.00000000000001); // Approximate result
        Assert.assertEquals(0.4285714285714285, cumPct2, 0.00000000000001); // Approximate result
        Assert.assertEquals(0.6857142857142857, cumPct3, 0.00000000000001); // Approximate result
    }

    @Test
    public void testGetCumPctWithZeroValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        double cumPctWithZeroValue = frequency.getCumPct(0);

        Assert.assertEquals(0.0, cumPctWithZeroValue, 0.0);
    }

    @Test
    public void testSortValues() {
        Frequency frequency = new Frequency(new NaturalComparator());
        frequency.addValue(3);
        frequency.addValue(1);
        frequency.addValue(2);

        Iterator<Object> values = frequency.valuesIterator();
        Object[] valuesArray = new Object[frequency.getSumFreq()];
        int i = 0;
        while (values.hasNext()) {
            valuesArray[i++] = values.next();
        }

        Comparator<Object> comparator = new NaturalComparator();
        Assert.assertArrayEquals(valuesArray, comparator.toString().split("\\s+"), 0);
    }
}