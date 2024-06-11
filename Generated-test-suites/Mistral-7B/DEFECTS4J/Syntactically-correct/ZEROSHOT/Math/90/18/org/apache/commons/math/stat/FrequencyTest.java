import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testFrequencyConstructor() {
        Frequency frequency = new Frequency();
        Assert.assertNotNull(frequency);
        Frequency frequencyComparator = new Frequency(new NaturalComparator());
        Assert.assertNotNull(frequencyComparator);
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        long count1 = frequency.getCount(1);
        Assert.assertEquals(2, count1);

        long count2 = frequency.getCount(2);
        Assert.assertEquals(2, count2);

        long countCharA = frequency.getCount('a');
        Assert.assertEquals(2, countCharA);

        long countCharB = frequency.getCount('b');
        Assert.assertEquals(1, countCharB);

        Assert.assertEquals(5, frequency.getSumFreq());
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        double pct1 = frequency.getPct(1);
        Assert.assertEquals(0.4, pct1, Precision.DEFAULT_EPSILON);

        double pct2 = frequency.getPct(2);
        Assert.assertEquals(0.4, pct2, Precision.DEFAULT_EPSILON);

        double pctCharA = frequency.getPct('a');
        Assert.assertEquals(0.4, pctCharA, Precision.DEFAULT_EPSILON);

        double pctCharB = frequency.getPct('b');
        Assert.assertEquals(0.2, pctCharB, Precision.DEFAULT_EPSILON);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        long cumFreq1 = frequency.getCumFreq(1);
        Assert.assertEquals(2, cumFreq1);

        long cumFreq2 = frequency.getCumFreq(2);
        Assert.assertEquals(4, cumFreq2);

        long cumFreqCharA = frequency.getCumFreq('a');
        Assert.assertEquals(2, cumFreqCharA);

        long cumFreqCharB = frequency.getCumFreq('b');
        Assert.assertEquals(5, cumFreqCharB);
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        double cumPct1 = frequency.getCumPct(1);
        Assert.assertEquals(0.4, cumPct1, Precision.DEFAULT_EPSILON);

        double cumPct2 = frequency.getCumPct(2);
        Assert.assertEquals(0.8, cumPct2, Precision.DEFAULT_EPSILON);

        double cumPctCharA = frequency.getCumPct('a');
        Assert.assertEquals(0.8, cumPctCharA, Precision.DEFAULT_EPSILON);

        double cumPctCharB = frequency.getCumPct('b');
        Assert.assertEquals(1.0, cumPctCharB, Precision.DEFAULT_EPSILON);
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');

        frequency.clear();

        long count1 = frequency.getCount(1);
        Assert.assertEquals(0, count1);

        long count2 = frequency.getCount(2);
        Assert.assertEquals(0, count2);

        long countCharA = frequency.getCount('a');
        Assert.assertEquals(0, countCharA);

        long countCharB = frequency.getCount('b');
        Assert.assertEquals(0, countCharB);

        Assert.assertEquals(0, frequency.getSumFreq());
    }
}