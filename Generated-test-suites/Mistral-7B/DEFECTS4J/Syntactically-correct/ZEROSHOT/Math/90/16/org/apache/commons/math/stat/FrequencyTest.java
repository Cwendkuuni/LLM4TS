import org.apache.commons.math3.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency freq = new Frequency();
        // Test addValue methods with different data types
        freq.addValue(1);
        freq.addValue(2);
        freq.addValue(3);
        freq.addValue('a');
        freq.addValue('b');
        freq.addValue('c');

        // Test getCount method
        Assert.assertEquals(3, freq.getCount(1L));
        Assert.assertEquals(1, freq.getCount(2L));
        Assert.assertEquals(1, freq.getCount(3L));
        Assert.assertEquals(1, freq.getCount('a'));
        Assert.assertEquals(1, freq.getCount('b'));
        Assert.assertEquals(1, freq.getCount('c'));

        // Test getPct method
        Assert.assertNotNull(freq.getPct(1L));
        Assert.assertNotNull(freq.getPct(2L));
        Assert.assertNotNull(freq.getPct(3L));
        Assert.assertNotNull(freq.getPct('a'));
        Assert.assertNotNull(freq.getPct('b'));
        Assert.assertNotNull(freq.getPct('c'));

        // Test getCumFreq method
        Assert.assertEquals(3, freq.getCumFreq(1L));
        Assert.assertEquals(4, freq.getCumFreq(2L));
        Assert.assertEquals(5, freq.getCumFreq(3L));
        Assert.assertEquals(5, freq.getCumFreq('a'));
        Assert.assertEquals(6, freq.getCumFreq('b'));
        Assert.assertEquals(7, freq.getCumFreq('c'));

        // Test getCumPct method
        Assert.assertNotNull(freq.getCumPct(1L));
        Assert.assertNotNull(freq.getCumPct(2L));
        Assert.assertNotNull(freq.getCumPct(3L));
        Assert.assertNotNull(freq.getCumPct('a'));
        Assert.assertNotNull(freq.getCumPct('b'));
        Assert.assertNotNull(freq.getCumPct('c'));

        // Test clear method
        freq.clear();
        Assert.assertEquals(0, freq.getSumFreq());
        Assert.assertNull(freq.toString());

        // Test valuesIterator method
        Frequency freq2 = new Frequency();
        freq2.addValue(1);
        freq2.addValue(2);
        freq2.addValue(3);
        Iterator it = freq2.valuesIterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(1, it.next().longValue());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(2, it.next().longValue());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(3, it.next().longValue());
        Assert.assertFalse(it.hasNext());
    }
}