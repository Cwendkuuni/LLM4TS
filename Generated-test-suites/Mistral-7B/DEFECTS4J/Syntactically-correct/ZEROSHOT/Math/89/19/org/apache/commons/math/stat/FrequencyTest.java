import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

    @Test
    public void testFrequency() {
        Frequency frequency = new Frequency();

        // Test addValue methods with integers
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(2);
        frequency.addValue(2);

        // Test getCount method with integers
        assertEquals(4, frequency.getCount(1));
        assertEquals(2, frequency.getCount(2));

        // Test getPct method with integers
        assertEquals(0.5, frequency.getPct(1), 0.001);
        assertEquals(0.5, frequency.getPct(2), 0.001);

        // Test getCumFreq method with integers
        assertEquals(4, frequency.getCumFreq(1));
        assertEquals(6, frequency.getCumFreq(2));

        // Test getCumPct method with integers
        assertEquals(0.5, frequency.getCumPct(1), 0.001);
        assertEquals(1.0, frequency.getCumPct(2), 0.001);

        // Test addValue methods with longs
        frequency.clear();
        frequency.addValue(1L);
        frequency.addValue(1L);
        frequency.addValue(2L);
        frequency.addValue(2L);

        // Test getCount method with longs
        assertEquals(4, frequency.getCount(1L));
        assertEquals(2, frequency.getCount(2L));

        // Test getPct method with longs
        assertEquals(0.5, frequency.getPct(1L), 0.001);
        assertEquals(0.5, frequency.getPct(2L), 0.001);

        // Test getCumFreq method with longs
        assertEquals(4, frequency.getCumFreq(1L));
        assertEquals(6, frequency.getCumFreq(2L));

        // Test getCumPct method with longs
        assertEquals(0.5, frequency.getCumPct(1L), 0.001);
        assertEquals(1.0, frequency.getCumPct(2L), 0.001);

        // Test addValue methods with characters
        frequency.clear();
        frequency.addValue('a');
        frequency.addValue('a');
        frequency.addValue('b');
        frequency.addValue('b');

        // Test getCount method with characters
        assertEquals(4, frequency.getCount('a'));
        assertEquals(2, frequency.getCount('b'));

        // Test getPct method with characters
        assertEquals(0.5, frequency.getPct('a'), 0.001);
        assertEquals(0.5, frequency.getPct('b'), 0.001);

        // Test getCumFreq method with characters
        assertEquals(4, frequency.getCumFreq('a'));
        assertEquals(6, frequency.getCumFreq('b'));

        // Test getCumPct method with characters
        assertEquals(0.5, frequency.getCumPct('a'), 0.001);
        assertEquals(1.0, frequency.getCumPct('b'), 0.001);

        // Test addValue method with uncomparable object
        try {
            frequency.addValue(new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test getCount method with uncomparable object
        assertEquals(0, frequency.getCount(new Object()));

        // Test getPct method with uncomparable object
        assertEquals(Double.NaN, frequency.getPct(new Object()), 0.0);

        // Test getCumFreq method with uncomparable object
        assertEquals(0, frequency.getCumFreq(new Object()));

        // Test getCumPct method with uncomparable object
        assertEquals(Double.NaN, frequency.getCumPct(new Object()), 0.0);

        // Test toString method
        assertEquals("Value   Freq.   Pct.   Cum Pct.\n1       4       0.5        0.5\n2       4       0.5        1.0\n", frequency.toString());
    }

    // Add more test methods here if needed
}