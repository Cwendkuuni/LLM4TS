import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testEmptyFrequency() {
        Frequency frequency = new Frequency();
        Assert.assertEquals("Empty frequency should have no values", 0, frequency.getSumFreq());
        Assert.assertEquals("Empty frequency should not contain any values", false, frequency.valuesIterator().hasNext());
    }

    @Test
    public void testBasicAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals("Single value added to frequency", 1, frequency.getSumFreq());
        Assert.assertEquals("Single value added to frequency", 1, frequency.getCount(1));
        Assert.assertEquals("Single value added to frequency", 1.0 / 1.0, frequency.getPct(1), 0.001);
        Assert.assertEquals("Single value added to frequency", 1.0 / 1.0, frequency.getCumPct(1), 0.001);
        Assert.assertEquals("Single value added to frequency", 1.0 / 1.0, frequency.getCumPct(1), 0.001);

        frequency.addValue("one");
        Assert.assertEquals("Single string value added to frequency", 1, frequency.getSumFreq());
        Assert.assertEquals("Single string value added to frequency", 1, frequency.getCount("one"));
        Assert.assertEquals("Single string value added to frequency", 1.0 / 1.0, frequency.getPct("one"), 0.001);
        Assert.assertEquals("Single string value added to frequency", 1.0 / 1.0, frequency.getCumPct("one"), 0.001);
        Assert.assertEquals("Single string value added to frequency", 1.0 / 1.0, frequency.getCumPct("one"), 0.001);
    }

    @Test
    public void testAddMultipleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Assert.assertEquals("Three values added to frequency", 3, frequency.getSumFreq());
        Assert.assertEquals("Three values added to frequency", 1, frequency.getCount(1));
        Assert.assertEquals("Three values added to frequency", 2, frequency.getCount(2));
        Assert.assertEquals("Three values added to frequency", 1.0 / 3.0, frequency.getPct(1), 0.001);
        Assert.assertEquals("Three values added to frequency", 1.0 / 3.0, frequency.getPct(2), 0.001);
        Assert.assertEquals("Three values added to frequency", 1.0 / 3.0, frequency.getPct(1), 0.001);
        Assert.assertEquals("Three values added to frequency", 2.0 / 3.0, frequency.getCumPct(2), 0.001);
        Assert.assertEquals("Three values added to frequency", 3.0 / 3.0, frequency.getCumPct(1), 0.001);
        Assert.assertEquals("Three values added to frequency", 3.0 / 3.0, frequency.getCumPct(3), 0.001);

        frequency.clear();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        Assert.assertEquals("Three values added to frequency", 3, frequency.getSumFreq());
        Assert.assertEquals("Three values added to frequency", 1, frequency.getCount(1));
        Assert.assertEquals("Three values added to frequency", 1, frequency.getCount(2));
        Assert.assertEquals("Three values added to frequency", 1, frequency.getCount(3));
        Assert.assertEquals("Three values added to frequency", 1.0 / 3.0, frequency.getPct(1), 0.001);
        Assert.assertEquals("Three values added to frequency", 1.0 / 3.0, frequency.getPct(2), 0.001);
        Assert.assertEquals("Three values added to frequency", 1.0 / 3.0, frequency.getPct(3), 0.001);
        Assert.assertEquals("Three values added to frequency", 1.0 / 3.0, frequency.getCumPct(1), 0.001);
        Assert.assertEquals("Three values added to frequency", 2.0 / 3.0, frequency.getCumPct(2), 0.001);
        Assert.assertEquals("Three values added to frequency", 3.0 / 3.0, frequency.getCumPct(3), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueNull() {
        Frequency frequency = new Frequency();
        frequency.addValue(null);
    }

    @Test
    public void testAddValueNegativeNumber() {
        Frequency frequency = new Frequency();
        frequency.addValue(-1);
        Assert.assertEquals("Negative value added to frequency", 0, frequency.getSumFreq());
        Assert.assertEquals("Negative value added to frequency", 0, frequency.getCount(-1));
        Assert.assertEquals("Negative value added to frequency", 0.0, frequency.getPct(-1), 0.001);
        Assert.assertEquals("Negative value added to frequency", 0.0, frequency.getCumPct(-1), 0.001);
    }

    @Test
    public void testComparator() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return Precision.compareTo(Double.parseDouble(o1.toString()), Double.parseDouble(o2.toString()), 0.001);
            }
        };

        Frequency frequency = new Frequency(comparator);
        frequency.addValue(1.0);
        frequency.addValue(2.0);
        frequency.addValue(1.5);
        Assert.assertEquals("Three values added to frequency with custom comparator", 3, frequency.getSumFreq());
        Assert.assertEquals("Three values added to frequency with custom comparator", 2, frequency.getCount(1.5));
        Assert.assertEquals("Three values added to frequency with custom comparator", 1.0, frequency.getPct(1.0), 0.001);
        Assert.assertEquals("Three values added to frequency with custom comparator", 0.5, frequency.getPct(1.5), 0.001);
        Assert.assertEquals("Three values added to frequency with custom comparator", 0.5, frequency.getCumPct(1.5), 0.001);
        Assert.assertEquals("Three values added to frequency with custom comparator", 1.5, frequency.getCumPct(1.5), 0.001);
        Assert.assertEquals("Three values added to frequency with custom comparator", 1.5, frequency.getCumPct(1.5), 0.001);
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);
        Assert.assertEquals("toString returns correct frequency distribution representation", "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t 1 \t 1/3 \t 1/3\n2 \t 1 \t 1/3 \t 2/3\n3 \t 1 \t 1/3 \t 1\n", frequency.toString());
    }
}