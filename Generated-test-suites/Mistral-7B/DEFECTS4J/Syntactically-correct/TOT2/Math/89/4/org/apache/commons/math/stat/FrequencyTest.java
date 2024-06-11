import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 private Frequency frequency;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 frequency = new Frequency();
 }

 /**
 * Test adding integers to the Frequency object
 */
 @Test
 public void testAddIntegerValues() {
 // Adding values to the Frequency object
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 frequency.addValue(3);
 // Checking the frequency count for each integer value
 assertEquals(3, frequency.getCount(1));
 assertEquals(3, frequency.getCount(2));
 assertEquals(1, frequency.getCount(3));

 // Checking the cumulative frequency and cumulative percentage for each integer value
 assertEquals(3, frequency.getCumFreq(1));
 assertEquals(6, frequency.getCumFreq(2));
 assertEquals(7, frequency.getCumFreq(3));

 double sumFreq = frequency.getSumFreq();
 double totalPct = 100.0;
 assertEquals(3.0 / sumFreq * totalPct, frequency.getPct(1), 0.01);
 assertEquals(3.0 / sumFreq * totalPct, frequency.getPct(2), 0.01);
 assertEquals(1.0 / sumFreq * totalPct, frequency.getPct(3), 0.01);
 }

 /**
 * Test adding long values to the Frequency object
 */
 @Test
 public void testAddLongValues() {
 // Adding values to the Frequency object
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(2L);
 frequency.addValue(2L);
 frequency.addValue(3L);
 // Checking the frequency count for each long value
 assertEquals(3, frequency.getCount(1L));
 assertEquals(3, frequency.getCount(2L));
 assertEquals(1, frequency.getCount(3L));

 // Checking the cumulative frequency and cumulative percentage for each long value
 assertEquals(3, frequency.getCumFreq(1L));
 assertEquals(6, frequency.getCumFreq(2L));
 assertEquals(7, frequency.getCumFreq(3L));

 double sumFreq = frequency.getSumFreq();
 double totalPct = 100.0;
 assertEquals(3.0 / sumFreq * totalPct, frequency.getPct(1L), 0.01);
 assertEquals(3.0 / sumFreq * totalPct, frequency.getPct(2L), 0.01);
 assertEquals(1.0 / sumFreq * totalPct, frequency.getPct(3L), 0.01);
 }

 /**
 * Test adding char values to the Frequency object
 */
 @Test
 public void testAddCharValues() {
 // Adding values to the Frequency object
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('b');
 frequency.addValue('c');
 // Checking the frequency count for each char value
 assertEquals(3, frequency.getCount('a'));
 assertEquals(3, frequency.getCount('b'));
 assertEquals(1, frequency.getCount('c'));

 // Checking the cumulative frequency and cumulative percentage for each char value
 assertEquals(3, frequency.getCumFreq('a'));
 assertEquals(6, frequency.getCumFreq('b'));
 assertEquals(7, frequency.getCumFreq('c'));

 double sumFreq = frequency.getSumFreq();
 double totalPct = 100.0;
 assertEquals(3.0 / sumFreq * totalPct, frequency.getPct('a'), 0.01);
 assertEquals(3.0 / sumFreq * totalPct, frequency.getPct('b'), 0.01);
 assertEquals(1.0 / sumFreq * totalPct, frequency.getPct('c'), 0.01);
 }

 /**
 * Test adding non-comparable values to the Frequency object
 */
 @Test(expected = IllegalArgumentException.class)
 public void testAddNonComparableValues() {
 // Attempting to add a non-comparable value to the Frequency object
 frequency.addValue(new Object());
 }

 /**
 * Test clearing the Frequency object
 */
 @Test
 public void testClear() {
 // Adding values to the Frequency object
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(2);
 frequency.addValue(3);
 // Clearing the Frequency object
 frequency.clear();
 // Checking that all values have been removed
 assertEquals(0, frequency.getCount(1));
 assertEquals(0, frequency.getCount(2));
 assertEquals(0, frequency.getCount(3));
 }
}