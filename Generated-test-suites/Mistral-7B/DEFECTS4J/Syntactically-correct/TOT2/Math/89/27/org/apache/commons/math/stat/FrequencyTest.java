import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.FrequencyTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testFrequencyEmpty() {
        Frequency frequency = new Frequency();
        Assert.assertEquals("Empty frequency distribution should have no entries", 0, frequency.getSumFreq());
        Assert.assertEquals("Empty frequency distribution should have no entries", 0, frequency.size());
    }

    @Test
    public void testFrequencyBasic() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        Assert.assertEquals("Total frequency count should be 6", 6, frequency.getSumFreq());
        Assert.assertEquals("Frequency count for value 1 should be 2", 2, frequency.getCount(1));
        Assert.assertEquals("Frequency count for value 1 should be 33.33%", 0.3333, frequency.getPct(1), 0.01);
        Assert.assertEquals("Cumulative frequency for value 1 should be 2", 2, frequency.getCumFreq(1));
        Assert.assertEquals("Cumulative percentage for value 1 should be 33.33%", 0.3333, frequency.getCumPct(1), 0.01);

        Assert.assertEquals("Frequency count for value 2 should be 4", 4, frequency.getCount(2));
        Assert.assertEquals("Frequency count for value 2 should be 66.67%", 0.6667, frequency.getPct(2), 0.01);
        Assert.assertEquals("Cumulative frequency for value 2 should be 6", 6, frequency.getCumFreq(2));
        Assert.assertEquals("Cumulative percentage for value 2 should be 100.00%", 1.0, frequency.getCumPct(2), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFrequencyIllegalArgument() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        frequency.addValue('a'); // should throw an IllegalArgumentException as 'a' is not comparable to integers
    }

    @Test
    public void testFrequencyComparator() {
        Comparator<Comparable<?>> comparator = (o1, o2) -> o1.compareTo(o2);
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(2);
        frequency.addValue(1);

        Assert.assertEquals("Total frequency count should be 2", 2, frequency.getSumFreq());
        Assert.assertEquals("Frequency count for value 1 should be 1", 1, frequency.getCount(1));
        Assert.assertEquals("Frequency count for value 1 should be 50.00%", 0.5, frequency.getPct(1), 0.01);
        Assert.assertEquals("Cumulative frequency for value 1 should be 1", 1, frequency.getCumFreq(1));
        Assert.assertEquals("Cumulative percentage for value 1 should be 50.00%", 0.5, frequency.getCumPct(1), 0.01);

        Assert.assertEquals("Frequency count for value 2 should be 1", 1, frequency.getCount(2));
        Assert.assertEquals("Frequency count for value 2 should be 50.00%", 0.5, frequency.getPct(2), 0.01);
        Assert.assertEquals("Cumulative frequency for value 2 should be 2", 2, frequency.getCumFreq(2));
        Assert.assertEquals("Cumulative percentage for value 2 should be 100.00%", 1.0, frequency.getCumPct(2), 0.01);
    }
}