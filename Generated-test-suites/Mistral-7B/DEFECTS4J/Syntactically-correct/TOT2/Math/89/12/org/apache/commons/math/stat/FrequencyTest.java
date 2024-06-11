import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Assert;
import org.junit.Test;

public class FrequencyTest1 {
    // Test for default constructor
    @Test
    public void testDefaultConstructor() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);

        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(2, frequency.getCount(2));

        Assert.assertEquals(33.0, frequency.getPct(1), 0.01);
        Assert.assertEquals(66.0, frequency.getPct(2), 0.01);

        Assert.assertEquals(33.0, frequency.getCumPct(1), 0.01);
        Assert.assertEquals(66.0, frequency.getCumPct(2), 0.01);
        Assert.assertEquals(100.0, frequency.getCumPct(3), 0.01);
    }

    // Test for Comparator constructor
    @Test
    public void testComparatorConstructor() {
        Frequency frequency = new Frequency(new NaturalComparator());
        frequency.addValue(2);
        frequency.addValue(1);

        Assert.assertEquals(2, frequency.getSumFreq());
        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));

        Assert.assertEquals(50.0, frequency.getPct(1), 0.01);
        Assert.assertEquals(50.0, frequency.getPct(2), 0.01);

        Assert.assertEquals(50.0, frequency.getCumPct(1), 0.01);
        Assert.assertEquals(100.0, frequency.getCumPct(2), 0.01);
    }

    // Test for addValue method with different data types
    @Test
    public void testAddValueWithDifferentDataTypes() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue('A');

        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(1, frequency.getCount((byte) 65));

        Assert.assertEquals(33.3, frequency.getPct(1), 0.01);
        Assert.assertEquals(33.3, frequency.getPct(2), 0.01);
        Assert.assertEquals(33.3, frequency.getPct((byte) 65), 0.01);

        Assert.assertEquals(33.3, frequency.getCumPct(1), 0.01);
        Assert.assertEquals(66.7, frequency.getCumPct(2), 0.01);
        Assert.assertEquals(100.0, frequency.getCumPct((byte) 65), 0.01);
    }

    // Test for addValue method with same data type but different values
    @Test
    public void testAddValueWithSameDataTypeButDifferentValues() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(3);

        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(1, frequency.getCount(1));
        Assert.assertEquals(1, frequency.getCount(2));
        Assert.assertEquals(1, frequency.getCount(3));

        Assert.assertEquals(33.3, frequency.getPct(1), 0.01);
        Assert.assertEquals(33.3, frequency.getPct(2), 0.01);
        Assert.assertEquals(33.3, frequency.getPct(3), 0.01);

        Assert.assertEquals(33.3, frequency.getCumPct(1), 0.01);
        Assert.assertEquals(66.7, frequency.getCumPct(2), 0.01);
        Assert.assertEquals(100.0, frequency.getCumPct(3), 0.01);
    }

    // Test for addValue method with same data type and same value
    @Test
    public void testAddValueWithSameDataTypeAndSameValue() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);

        Assert.assertEquals(3, frequency.getSumFreq());
        Assert.assertEquals(3, frequency.getCount(1));

        Assert.assertEquals(100.0, frequency.getPct(1), 0.01);
        Assert.assertEquals(100.0, frequency.getCumPct(1), 0.01);
    }

    // Test for addValue method with non-comparable data types
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableDataTypes() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue('A');
    }

    // Test for addValue method with non-comparable data types in the same Frequency object
    @Test(expected = IllegalArgumentException.class)
    public void testAddValueWithNonComparableDataTypesInSameObject() {
        Frequency frequency = new Frequency();

        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue('A');
    }
}