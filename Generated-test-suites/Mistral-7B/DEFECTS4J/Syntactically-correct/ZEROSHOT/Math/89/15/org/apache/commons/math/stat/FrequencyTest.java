import org.apache.commons.math.stat.Frequency;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.TreeMap;

public class FrequencyTest {

    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testComparatorConstructor() {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(1, frequency.getCount('1'));

        frequency.addValue(2);
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(2, frequency.getCount(Long.valueOf(2)));
        Assert.assertEquals(2, frequency.getCount('2'));

        frequency.addValue('a');
        Assert.assertEquals(1, frequency.getCount('a'));
        Assert.assertEquals(1, frequency.getCount(Character.valueOf('a')));

        frequency.addValue(null);
        Assert.assertEquals(0, frequency.getCount(null));

        frequency.addValue(new Integer(1));
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(2, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(2, frequency.getCount('1'));

        frequency.addValue(new Integer(1).longValue());
        Assert.assertEquals(3, frequency.getCount(1));
        Assert.assertEquals(3, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(3, frequency.getCount('1'));

        frequency.addValue(1L);
        Assert.assertEquals(3, frequency.getCount(1));
        Assert.assertEquals(3, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(3, frequency.getCount('1'));

        frequency.addValue('1');
        Assert.assertEquals(4, frequency.getCount(1));
        Assert.assertEquals(4, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(4, frequency.getCount('1'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object());
    }

    @Test
    public void testAddValueWithSameValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(2, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(2, frequency.getCount('1'));
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        Assert.assertEquals(0, frequency.getSumFreq());
        Assert.assertNull(frequency.toString());
    }

    @Test
    public void testValuesIterator() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        Iterator<Object> iterator = frequency.valuesIterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next().hashCode());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next().hashCode());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetSumFreq() {
        Frequency frequency = new Frequency();
        Assert.assertEquals(0, frequency.getSumFreq());

        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getSumFreq());

        frequency.addValue(2);
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(3, frequency.getSumFreq());
    }

    @Test
    public void testGetCount() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(1, frequency.getCount('1'));

        frequency.addValue(2);
        Assert.assertEquals(2, frequency.getCount(2));
        Assert.assertEquals(2, frequency.getCount(Long.valueOf(2)));
        Assert.assertEquals(2, frequency.getCount('2'));

        frequency.addValue(1);
        Assert.assertEquals(2, frequency.getCount(1));
        Assert.assertEquals(2, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(2, frequency.getCount('1'));

        frequency.addValue('a');
        Assert.assertEquals(1, frequency.getCount('a'));
        Assert.assertEquals(1, frequency.getCount(Character.valueOf('a')));

        frequency.addValue(null);
        Assert.assertEquals(0, frequency.getCount(null));

        frequency.addValue(new Integer(1));
        Assert.assertEquals(3, frequency.getCount(1));
        Assert.assertEquals(3, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(3, frequency.getCount('1'));

        frequency.addValue(new Integer(1).longValue());
        Assert.assertEquals(4, frequency.getCount(1));
        Assert.assertEquals(4, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(4, frequency.getCount('1'));

        frequency.addValue(1L);
        Assert.assertEquals(4, frequency.getCount(1));
        Assert.assertEquals(4, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(4, frequency.getCount('1'));

        frequency.addValue('1');
        Assert.assertEquals(5, frequency.getCount(1));
        Assert.assertEquals(5, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(5, frequency.getCount('1'));

        frequency.addValue(new Object());
        Assert.assertEquals(5, frequency.getCount(1));
        Assert.assertEquals(5, frequency.getCount(Long.valueOf(1)));
        Assert.assertEquals(5, frequency.getCount('1'));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        Assert.assertEquals(0.0, frequency.getPct(null), 0.001);

        Assert.assertEquals(0.3333333333333333, frequency.getPct(1), 0.001);
        Assert.assertEquals(0.3333333333333333, frequency.getPct(Long.valueOf(1)), 0.001);
        Assert.assertEquals(0.3333333333333333, frequency.getPct('1'), 0.001);

        Assert.assertEquals(0.3333333333333333, frequency.getPct(2), 0.001);
        Assert.assertEquals(0.3333333333333333, frequency.getPct(Long.valueOf(2)), 0.001);
        Assert.assertEquals(0.3333333333333333, frequency.getPct('2'), 0.001);

        Assert.assertEquals(0.3333333333333333, frequency.getPct(3), 0.001);
        Assert.assertEquals(0.3333333333333333, frequency.getPct(Long.valueOf(3)), 0.001);
        Assert.assertEquals(0.3333333333333333, frequency.getPct('3'), 0.001);
    }

    @Test
    public void testGetCumFreq() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        Assert.assertEquals(0, frequency.getCumFreq(null));
        Assert.assertEquals(0, frequency.getCumFreq(Long.valueOf(null)));

        Assert.assertEquals(1, frequency.getCumFreq(1));
        Assert.assertEquals(1, frequency.getCumFreq(Long.valueOf(1)));
        Assert.assertEquals(1, frequency.getCumFreq('1'));

        Assert.assertEquals(2, frequency.getCumFreq(1));
        Assert.assertEquals(2, frequency.getCumFreq(Long.valueOf(1)));
        Assert.assertEquals(2, frequency.getCumFreq('1'));

        Assert.assertEquals(2, frequency.getCumFreq(2));
        Assert.assertEquals(2, frequency.getCumFreq(Long.valueOf(2)));
        Assert.assertEquals(2, frequency.getCumFreq('2'));
    }

    @Test
    public void testGetCumPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);

        Assert.assertEquals(0.0, frequency.getCumPct(null), 0.001);

        Assert.assertEquals(0.3333333333333333, frequency.getCumPct(1), 0.001);
        Assert.assertEquals(0.3333333333333333, frequency.getCumPct(Long.valueOf(1)), 0.001);
        Assert.assertEquals(0.3333333333333333, frequency.getCumPct('1'), 0.001);

        Assert.assertEquals(0.6666666666666667, frequency.getCumPct(2), 0.001);
        Assert.assertEquals(0.6666666666666667, frequency.getCumPct(Long.valueOf(2)), 0.001);
        Assert.assertEquals(0.6666666666666667, frequency.getCumPct('2'), 0.001);
    }
}