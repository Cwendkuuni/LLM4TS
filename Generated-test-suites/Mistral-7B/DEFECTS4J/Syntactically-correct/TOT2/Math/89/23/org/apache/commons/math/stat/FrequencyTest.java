import org.apache.commons.math.stat.Frequency;
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

        long actualCount = frequency.getCount(1);
        long expectedCount = 2;
        Assert.assertEquals("getCount(1) should return 2", expectedCount, actualCount);

        actualCount = frequency.getCount(2);
        expectedCount = 2;
        Assert.assertEquals("getCount(2) should return 2", expectedCount, actualCount);

        long actualSumFreq = frequency.getSumFreq();
        long expectedSumFreq = 6;
        Assert.assertEquals("getSumFreq() should return 6", expectedSumFreq, actualSumFreq);

        double actualPct = frequency.getPct(1);
        double expectedPct = 0.33;
        Assert.assertEquals("getPct(1) should return 0.33", expectedPct, actualPct, 0.001);

        actualPct = frequency.getPct(2);
        expectedPct = 0.33;
        Assert.assertEquals("getPct(2) should return 0.33", expectedPct, actualPct, 0.001);

        long actualCumFreq = frequency.getCount(1);
        long expectedCumFreq = 2;
        Assert.assertEquals("getCount(1) should return 2 as cumulative frequency", expectedCumFreq, actualCumFreq);

        actualCumFreq = frequency.getCount(2);
        expectedCumFreq = 4;
        Assert.assertEquals("getCount(2) should return 4 as cumulative frequency", expectedCumFreq, actualCumFreq);

        double actualCumPct = frequency.getCumPct(2);
        double expectedCumPct = 0.67;
        Assert.assertEquals("getCumPct(2) should return 0.67", expectedCumPct, actualCumPct, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableObject() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object()); // This object should not be comparable to the values in the frequency distribution
    }

    @Test
    public void testAddValueWithNull() {
        Frequency frequency = new Frequency();
        frequency.addValue(null); // This value should throw an IllegalArgumentException as it's not comparable
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.clear();
        long actualSumFreq = frequency.getSumFreq();
        long expectedSumFreq = 0;
        Assert.assertEquals("getSumFreq() should return 0 after clear", expectedSumFreq, actualSumFreq);
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        Iterator<Object> valuesIterator = frequency.valuesIterator();
        Object actualValue = valuesIterator.next();
        Assert.assertEquals("Values iterator should return 1 as the first value", 1, actualValue);

        actualValue = valuesIterator.next();
        Assert.assertEquals("Values iterator should return 1 as the second value", 1, actualValue);

        actualValue = valuesIterator.next();
        Assert.assertEquals("Values iterator should return 2 as the third value", 2, actualValue);

        actualValue = valuesIterator.next();
        Assert.assertEquals("Values iterator should return 2 as the fourth value", 2, actualValue);

        Assert.assertFalse("Values iterator should return false when calling next() after all values have been retrieved", valuesIterator.hasNext());
    }

    @Test
    public void testComparator() {
        Frequency frequency = new Frequency(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        });
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        long actualCount = frequency.getCount(1);
        long expectedCount = 2;
        Assert.assertEquals("getCount(1) should return 2 with custom comparator", expectedCount, actualCount);

        long actualCumFreq = frequency.getCount(1);
        long expectedCumFreq = 2;
        Assert.assertEquals("getCount(1) should return 2 as cumulative frequency with custom comparator", expectedCumFreq, actualCumFreq);
    }
}