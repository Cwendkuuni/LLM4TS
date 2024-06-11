import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();

        // Adding values
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(3);
        frequency.addValue(3);
        frequency.addValue(3);

        // Testing size and sumFreq
        Assert.assertEquals("Size and sumFreq should match", 7, frequency.valuesIterator().size());
        Assert.assertEquals("SumFreq should be correct", 12L, frequency.getSumFreq());

        // Testing getCount, getPct and getCumPct for each value
        Assert.assertEquals("Count for 1 should be 2", 2, frequency.getCount(1));
        Assert.assertEquals("Percentage for 1 should be 16.67%", 0.1667, frequency.getPct(1), 0.001);
        Assert.assertEquals("Cumulative percentage for 1 should be 16.67%", 0.1667, frequency.getCumPct(1), 0.001);
        Assert.assertEquals("Count for 2 should be 2", 2, frequency.getCount(2));
        Assert.assertEquals("Percentage for 2 should be 16.67%", 0.1667, frequency.getPct(2), 0.001);
        Assert.assertEquals("Cumulative percentage for 2 should be 33.33%", 0.3333, frequency.getCumPct(2), 0.001);
        Assert.assertEquals("Count for 3 should be 3", 3, frequency.getCount(3));
        Assert.assertEquals("Percentage for 3 should be 25%", 0.25, frequency.getPct(3), 0.001);
        Assert.assertEquals("Cumulative percentage for 3 should be 58.33%", 0.5833, frequency.getCumPct(3), 0.001);

        // Testing getCount, getPct and getCumPct for non-existent value
        Assert.assertEquals("Count for 4 should be 0", 0, frequency.getCount(4));
        Assert.assertNull("Percentage for 4 should be null", frequency.getPct(4));
        Assert.assertEquals("Cumulative percentage for 4 should be 0%", 0, frequency.getCumPct(4), 0.001);

        // Testing addValue with non-comparable value
        try {
            frequency.addValue("test");
            Assert.fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Testing clear method
        frequency.clear();
        Assert.assertEquals("Size after clear should be 0", 0, frequency.valuesIterator().size());
    }

    @Test
    public void testFrequencyWithComparator() {
        Frequency frequency = new Frequency(StatUtils.INT_ARRAY_COMPARATOR);

        // Adding values
        frequency.addValue(3);
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue(1);

        // Testing size and sumFreq
        Assert.assertEquals("Size and sumFreq should match", 6, frequency.valuesIterator().size());
        Assert.assertEquals("SumFreq should be correct", 6L, frequency.getSumFreq());

        // Testing getCount, getPct and getCumPct for each value
        Assert.assertEquals("Count for 1 should be 3", 3, frequency.getCount(1));
        Assert.assertEquals("Percentage for 1 should be 50%", 0.5, frequency.getPct(1), 0.001);
        Assert.assertEquals("Cumulative percentage for 1 should be 50%", 0.5, frequency.getCumPct(1), 0.001);
        Assert.assertEquals("Count for 2 should be 2", 2, frequency.getCount(2));
        Assert.assertEquals("Percentage for 2 should be 33.33%", 0.3333, frequency.getPct(2), 0.001);
        Assert.assertEquals("Cumulative percentage for 2 should be 83.33%", 0.8333, frequency.getCumPct(2), 0.001);
        Assert.assertEquals("Count for 3 should be 1", 1, frequency.getCount(3));
        Assert.assertEquals("Percentage for 3 should be 16.67%", 0.1667, frequency.getPct(3), 0.001);
        Assert.assertEquals("Cumulative percentage for 3 should be 33.33%", 0.3333, frequency.getCumPct(3), 0.001);

        // Testing getCount, getPct and getCumPct for non-existent value
        Assert.assertEquals("Count for 0 should be 0", 0, frequency.getCount(0));
        Assert.assertNull("Percentage for 0 should be null", frequency.getPct(0));
        Assert.assertEquals("Cumulative percentage for 0 should be 0%", 0, frequency.getCumPct(0), 0.001);
    }
}