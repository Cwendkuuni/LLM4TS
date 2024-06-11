import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testAddValueInt() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        long count = frequency.getCount(1);
        long sumFreq = frequency.getSumFreq();
        long cumFreq = frequency.getCumFreq(1);
        long cumPct = frequency.getCumPct(1);

        assertEquals(1, count);
        assertEquals(1, sumFreq);
        assertEquals(1, cumFreq);
        assertEquals(1.0 / sumFreq, cumPct, 0.01);
    }

    @Test
    public void testAddValueLong() {
        Frequency frequency = new Frequency();
        frequency.addValue(2L);
        long count = frequency.getCount(2);
        long sumFreq = frequency.getSumFreq();
        long cumFreq = frequency.getCumFreq(2);
        long cumPct = frequency.getCumPct(2);

        assertEquals(1, count);
        assertEquals(1, sumFreq);
        assertEquals(1, cumFreq);
        assertEquals(1.0 / sumFreq, cumPct, 0.01);
    }

    @Test
    public void testAddValueCharacter() {
        Frequency frequency = new Frequency();
        frequency.addValue('A');
        long count = frequency.getCount('A');
        long sumFreq = frequency.getSumFreq();
        long cumFreq = frequency.getCumFreq('A');
        long cumPct = frequency.getCumPct('A');

        assertEquals(1, count);
        assertEquals(1, sumFreq);
        assertEquals(1, cumFreq);
        assertEquals(1.0 / sumFreq, cumPct, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueUncomparable() {
        Frequency frequency = new Frequency();
        frequency.addValue(new Object()); // Uncomparable object
    }

    @Test
    public void testClear() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.clear();
        long sumFreq = frequency.getSumFreq();

        assertEquals(0, sumFreq);
    }
}