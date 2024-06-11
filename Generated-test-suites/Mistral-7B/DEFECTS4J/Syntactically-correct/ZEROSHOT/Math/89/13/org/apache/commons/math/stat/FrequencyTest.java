import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testEmptyFrequency() {
        Frequency frequency = new Frequency();
        assertEquals("empty frequency distribution should have zero frequency for all values", 0, frequency.getSumFreq());
        assertTrue("empty frequency distribution should have no values", frequency.valuesIterator().hasNext() == false);
    }

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        assertEquals("frequency distribution should have one frequency for value 1", 1, frequency.getCount(1));
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(1);
        assertEquals("frequency distribution should have two frequencies for value 1 and one for value 2", 3, frequency.getCount(1));
        assertEquals("frequency distribution should have one frequency for value 2", 1, frequency.getCount(2));
        assertEquals("frequency distribution should have a total frequency of 4", 4, frequency.getSumFreq());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueIllegalArgument() {
        Frequency frequency = new Frequency();
        frequency.addValue('a');
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals("frequency distribution should have a total frequency of 4 before clear", 4, frequency.getSumFreq());
        frequency.clear();
        assertTrue("frequency distribution should have no values after clear", frequency.valuesIterator().hasNext() == false);
        assertEquals("frequency distribution should have a total frequency of 0 after clear", 0, frequency.getSumFreq());
    }

    @Test
    public void testGetValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Object value1 = new Integer(1);
        Object value2 = new Integer(2);
        assertEquals("frequency distribution should return the correct frequency for value 1", 2, frequency.getCount(value1));
        assertEquals("frequency distribution should return the correct frequency for value 2", 1, frequency.getCount(value2));
        assertEquals("frequency distribution should return 0 for a non-existent value", 0, frequency.getCount(new Integer(3)));
    }

    @Test
    public void testGetPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        double pctValue1 = new Double(1.0);
        double pctValue2 = new Double(0.5);
        double totalPct = new Double(2.5);
        assertEquals("frequency distribution should return the correct percentage for value 1", pctValue1, frequency.getPct(new Integer(1)), 0.001);
        assertEquals("frequency distribution should return the correct percentage for value 2", pctValue2, frequency.getPct(new Integer(2)), 0.001);
        assertEquals("frequency distribution should return NaN for a non-existent value", Double.NaN, frequency.getPct(new Integer(3)));
        assertEquals("frequency distribution should return the correct total percentage", totalPct, frequency.getSumPct(), 0.001);
    }

    @Test
    public void testGetCumulativeFreqAndPct() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        Object value0 = new Integer(0);
        Object value1 = new Integer(1);
        Object value2 = new Integer(2);
        long cumFreqValue0 = 0;
        long cumFreqValue1 = 1;
        long cumFreqValue2 = 2;
        double cumPctValue0 = 0.0;
        double cumPctValue1 = 0.333;
        double cumPctValue2 = 0.667;
        double totalCumPct = 1.0;
        assertEquals("frequency distribution should return the correct cumulative frequency for value 0", cumFreqValue0, frequency.getCumFreq(value0));
        assertEquals("frequency distribution should return the correct cumulative percentage for value 0", cumPctValue0, frequency.getCumPct(value0), 0.001);
        assertEquals("frequency distribution should return the correct cumulative frequency for value 1", cumFreqValue1, frequency.getCumFreq(value1));
        assertEquals("frequency distribution should return the correct cumulative percentage for value 1", cumPctValue1, frequency.getCumPct(value1), 0.001);
        assertEquals("frequency distribution should return the correct cumulative frequency for value 2", cumFreqValue2, frequency.getCumFreq(value2));
        assertEquals("frequency distribution should return the correct cumulative percentage for value 2", cumPctValue2, frequency.getCumPct(value2), 0.001);
        assertEquals("frequency distribution should return 0 for a non-existent value", 0, frequency.getCumFreq(new Integer(3)));
        assertEquals("frequency distribution should return NaN for a non-existent value", Double.NaN, frequency.getCumPct(new Integer(3)));
        assertEquals("frequency distribution should return the correct total cumulative percentage", totalCumPct, frequency.getTotalCumPct(), 0.001);
    }

    @Test
    public void testComparator() {
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) o1).compareTo(o2);
            }
        };
        Frequency frequency = new Frequency(comparator);
        frequency.addValue(2);
        frequency.addValue(1);
        Object value1 = new Integer(1);
        Object value2 = new Integer(2);
        assertEquals("frequency distribution with custom comparator should return the correct frequency for value 1", 1, frequency.getCount(value1));
        assertEquals("frequency distribution with custom comparator should return the correct frequency for value 2", 1, frequency.getCount(value2));
        assertEquals("frequency distribution with custom comparator should return the correct total frequency", 2, frequency.getSumFreq());
        assertEquals("frequency distribution with custom comparator should return the correct cumulative frequency for value 1", 1, frequency.getCumFreq(value1));
        assertEquals("frequency distribution with custom comparator should return the correct cumulative frequency for value 2", 2, frequency.getCumFreq(value2));
    }

    @Test
    public void testToString() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        assertEquals("frequency distribution should return the correct string representation", "[Value \t Freq. \t Pct. \t Cum Pct. \n1 \t 2 \t 0.333 \t 0.333\n2 \t 1 \t 0.2 \t 0.533\nTotal \t 3 \t 1.0 \t 1.000]", frequency.toString());
    }
}