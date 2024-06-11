import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();

        // Test adding values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        // Test getCount
        Assert.assertEquals(3, frequency.getCount(1L));
        Assert.assertEquals(3, frequency.getCount(1));
        Assert.assertEquals(3, frequency.getCount(1L));
        Assert.assertEquals(2, frequency.getCount(2L));
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(2, frequency.getCount(2L));
        Assert.assertEquals(1, frequency.getCount(3L));
        Assert.assertEquals(1, frequency.getCount(3));
        Assert.assertEquals(1, frequency.getCount(3L));

        // Test getPct
        Assert.assertEquals(0.3333, frequency.getPct(1L), 0.001);
        Assert.assertEquals(0.3333, frequency.getPct(1), 0.001);
        Assert.assertEquals(0.3333, frequency.getPct(1L), 0.001);
        Assert.assertEquals(0.2222, frequency.getPct(2L), 0.001);
        Assert.assertEquals(0.2222, frequency.getPct(2), 0.001);
        Assert.assertEquals(0.2222, frequency.getPct(2L), 0.001);
        Assert.assertEquals(0.1111, frequency.getPct(3L), 0.001);
        Assert.assertEquals(0.1111, frequency.getPct(3), 0.001);
        Assert.assertEquals(0.1111, frequency.getPct(3L), 0.001);

        // Test getCumFreq
        Assert.assertEquals(3, frequency.getCumFreq(1L));
        Assert.assertEquals(3, frequency.getCumFreq(1));
        Assert.assertEquals(3, frequency.getCumFreq(1L));
        Assert.assertEquals(5, frequency.getCumFreq(2L));
        Assert.assertEquals(5, frequency.getCumFreq(2));
        Assert.assertEquals(5, frequency.getCumFreq(2L));
        Assert.assertEquals(6, frequency.getCumFreq(3L));
        Assert.assertEquals(6, frequency.getCumFreq(3));
        Assert.assertEquals(6, frequency.getCumFreq(3L));

        // Test getCumPct
        Assert.assertEquals(0.3333, frequency.getCumPct(1L), 0.001);
        Assert.assertEquals(0.3333, frequency.getCumPct(1), 0.001);
        Assert.assertEquals(0.3333, frequency.getCumPct(1L), 0.001);
        Assert.assertEquals(0.5556, frequency.getCumPct(2L), 0.001);
        Assert.assertEquals(0.5556, frequency.getCumPct(2), 0.001);
        Assert.assertEquals(0.5556, frequency.getCumPct(2L), 0.001);
        Assert.assertEquals(0.7778, frequency.getCumPct(3L), 0.001);
        Assert.assertEquals(0.7778, frequency.getCumPct(3), 0.001);
        Assert.assertEquals(0.7778, frequency.getCumPct(3L), 0.001);

        // Test addValue with non-comparable value
        Assert.assertThrows(IllegalArgumentException.class, () -> frequency.addValue("notComparable"));

        // Test addValue with null value
        Assert.assertThrows(IllegalArgumentException.class, () -> frequency.addValue(null));

        // Test addValue with non-Comparable value
        Assert.assertThrows(IllegalArgumentException.class, () -> frequency.addValue(new Object() {}.getClass()));

        // Test addValue with null Comparator
        Frequency frequencyNullComparator = new Frequency(null);
        Assert.assertThrows(IllegalArgumentException.class, () -> frequencyNullComparator.addValue(1L));

        // Test addValue with custom Comparator
        Comparator<Object> customComparator = (o1, o2) -> ((Comparable) o1).compareTo(o2);
        Frequency frequencyCustomComparator = new Frequency(customComparator);
        frequencyCustomComparator.addValue(3);
        frequencyCustomComparator.addValue(1);
        Assert.assertEquals(1, frequencyCustomComparator.getCumFreq(3));
        Assert.assertEquals(1, frequencyCustomComparator.getCumFreq(1));
    }

    @Test
    public void testFrequencyWithComparator() {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        };
        Frequency frequency = new Frequency(comparator);

        // Test adding values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);

        // Test getCount
        Assert.assertEquals(3, frequency.getCount(1));
        Assert.assertEquals(3, frequency.getCount(1));
        Assert.assertEquals(3, frequency.getCount(1));
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(1, frequency.getCount(3));
        Assert.assertEquals(1, frequency.getCount(3));
        Assert.assertEquals(1, frequency.getCount(3));

        // Test getPct
        Assert.assertEquals(0.3333, frequency.getPct(1), 0.001);
        Assert.assertEquals(0.3333, frequency.getPct(1), 0.001);
        Assert.assertEquals(0.3333, frequency.getPct(1), 0.001);
        Assert.assertEquals(0.2222, frequency.getPct(2), 0.001);
        Assert.assertEquals(0.2222, frequency.getPct(2), 0.001);
        Assert.assertEquals(0.2222, frequency.getPct(2), 0.001);
        Assert.assertEquals(0.1111, frequency.getPct(3), 0.001);
        Assert.assertEquals(0.1111, frequency.getPct(3), 0.001);
        Assert.assertEquals(0.1111, frequency.getPct(3), 0.001);

        // Test getCumFreq
        Assert.assertEquals(3, frequency.getCumFreq(1));
        Assert.assertEquals(3, frequency.getCumFreq(1));
        Assert.assertEquals(3, frequency.getCumFreq(1));
        Assert.assertEquals(5, frequency.getCumFreq(2));
        Assert.assertEquals(5, frequency.getCumFreq(2));
        Assert.assertEquals(5, frequency.getCumFreq(2));
        Assert.assertEquals(6, frequency.getCumFreq(3));
        Assert.assertEquals(6, frequency.getCumFreq(3));
        Assert.assertEquals(6, frequency.getCumFreq(3));

        // Test getCumPct
        Assert.assertEquals(0.3333, frequency.getCumPct(1), 0.001);
        Assert.assertEquals(0.3333, frequency.getCumPct(1), 0.001);
        Assert.assertEquals(0.3333, frequency.getCumPct(1), 0.001);
        Assert.assertEquals(0.5556, frequency.getCumPct(2), 0.001);
        Assert.assertEquals(0.5556, frequency.getCumPct(2), 0.001);
        Assert.assertEquals(0.5556, frequency.getCumPct(2), 0.001);
        Assert.assertEquals(0.7778, frequency.getCumPct(3), 0.001);
        Assert.assertEquals(0.7778, frequency.getCumPct(3), 0.001);
        Assert.assertEquals(0.7778, frequency.getCumPct(3), 0.001);

        // Test clear
        frequency.clear();
        Assert.assertEquals(0, frequency.getCount(1));
        Assert.assertEquals(0, frequency.getCount(1));
        Assert.assertEquals(0, frequency.getCount(1));
        Assert.assertEquals(0, frequency.getCount(2));
        Assert.assertEquals(0, frequency.getCount(2));
        Assert.assertEquals(0, frequency.getCount(2));
        Assert.assertEquals(0, frequency.getCount(3));
        Assert.assertEquals(0, frequency.getCount(3));
        Assert.assertEquals(0, frequency.getCount(3));
    }
}