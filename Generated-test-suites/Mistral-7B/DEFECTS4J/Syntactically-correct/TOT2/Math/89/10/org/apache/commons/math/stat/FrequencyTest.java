import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testEmptyFrequency() {
        Frequency frequency = new Frequency();
        Assert.assertEquals("Empty frequency distribution should have no values", 0, frequency.getSumFreq());
        Assert.assertEquals("Empty frequency distribution should have no values", 0, frequency.getCount(1));
        Assert.assertEquals("Empty frequency distribution should have no values", 0.0, frequency.getPct(1), 0.001);
        Assert.assertEquals("Empty frequency distribution should have no values", 0.0, frequency.getCumPct(1), 0.001);
    }

    @Test
    public void testAddOneValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals("Frequency distribution with one value should have one value", 1, frequency.getSumFreq());
        Assert.assertEquals("Frequency distribution with one value should have one value", 1, frequency.getCount(1));
        Assert.assertEquals("Frequency distribution with one value should have one value", 1.0 / 1.0, frequency.getPct(1), 0.001);
        Assert.assertEquals("Frequency distribution with one value should have one value", 1.0 / 1.0, frequency.getCumPct(1), 0.001);
    }

    @Test
    public void testAddMultipleValues() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Assert.assertEquals("Frequency distribution with multiple values should have multiple values", 3, frequency.getSumFreq());
        Assert.assertEquals("Frequency distribution with multiple values should have multiple values", 2, frequency.getCount(1));
        Assert.assertEquals("Frequency distribution with multiple values should have multiple values", 1.0 / 3.0, frequency.getPct(1), 0.001);
        Assert.assertEquals("Frequency distribution with multiple values should have multiple values", 1.0 / 3.0, frequency.getCumPct(1), 0.001);
        Assert.assertEquals("Frequency distribution with multiple values should have multiple values", 1.0, frequency.getCumPct(2), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonComparableValueWithComparator() {
        Frequency frequency = new Frequency(new NaturalComparator());
        frequency.addValue(new Object());
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        Assert.assertEquals("ToString should correctly display frequencies",
                "Value \t Freq. \t Pct. \t Cum Pct.\n1 \t\t 2 \t\t 0.6666666666666667 \t\t 0.6666666666666667\n2 \t\t 1 \t\t 0.3333333333333334 \t\t 1.0\n", frequency.toString());
    }
}