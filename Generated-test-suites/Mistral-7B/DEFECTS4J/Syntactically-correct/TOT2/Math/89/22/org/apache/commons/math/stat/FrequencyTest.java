import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 // Test constructor
 @Test
 public void testFrequencyConstructor() {
 Frequency frequency = new Frequency();
 }

 // Test addValue method with integers
 @Test
 public void testAddValueInteger() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 long expectedCount = 2;
 long actualCount = frequency.getCount(1);
 assertEquals(expectedCount, actualCount);
 }

 // Test addValue method with longs
 @Test
 public void testAddValueLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 frequency.addValue(2L);
 long expectedCount = 2;
 long actualCount = frequency.getCount(1L);
 assertEquals(expectedCount, actualCount);
 }

 // Test addValue method with characters
 @Test
 public void testAddValueCharacter() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 long expectedCount = 2;
 long actualCount = frequency.getCount('a');
 assertEquals(expectedCount, actualCount);
 }

 // Test addValue method with non-comparable values
 @Test(expected = IllegalArgumentException.class)
 public void testAddValueNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(new Object());
 }

 // Test addValue method with duplicate values
 @Test
 public void testAddValueDuplicateValues() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 long expectedSumFreq = 2;
 long actualSumFreq = frequency.getSumFreq();
 assertEquals(expectedSumFreq, actualSumFreq);
 }

 // Test getSumFreq method
 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 long expectedSumFreq = 4;
 long actualSumFreq = frequency.getSumFreq();
 assertEquals(expectedSumFreq, actualSumFreq);
 }

 // Test getCount method with existing value
 @Test
 public void testGetCountExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 long expectedCount = 2;
 long actualCount = frequency.getCount(1);
 assertEquals(expectedCount, actualCount);
 }

 // Test getCount method with non-existing value
 @Test
 public void testGetCountNonExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 long expectedCount = 0;
 long actualCount = frequency.getCount(2);
 assertEquals(expectedCount, actualCount);
 }

 // Test getPct method with existing value
 @Test
 public void testGetPctExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 double expectedPct = 0.3333;
 double actualPct = frequency.getPct(2);
 assertEquals(expectedPct, actualPct, 0.001);
 }

 // Test getPct method with non-existing value
 @Test
 public void testGetPctNonExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 double expectedPct = 0.0;
 double actualPct = frequency.getPct(3);
 assertEquals(expectedPct, actualPct, 0.001);
 }

 // Test getCumFreq method with existing value
 @Test
 public void testGetCumFreqExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 long expectedCumFreq = 2;
 long actualCumFreq = frequency.getCumFreq(1);
 assertEquals(expectedCumFreq, actualCumFreq);
 }

 // Test getCumFreq method with non-existing value
 @Test
 public void testGetCumFreqNonExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 long expectedCumFreq = 0;
 long actualCumFreq = frequency.getCumFreq(3);
 assertEquals(expectedCumFreq, actualCumFreq);
 }

 // Test getCumPct method with existing value
 @Test
 public void testGetCumPctExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 double expectedCumPct = 0.3333;
 double actualCumPct = frequency.getCumPct(2);
 assertEquals(expectedCumPct, actualCumPct, 0.001);
 }

 // Test getCumPct method with non-existing value
 @Test
 public void testGetCumPctNonExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 double expectedCumPct = 0.0;
 double actualCumPct = frequency.getCumPct(3);
 assertEquals(expectedCumPct, actualCumPct, 0.001);
 }

 // Test clear method
 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.clear();
 long expectedSumFreq = 0;
 long actualSumFreq = frequency.getSumFreq();
 assertEquals(expectedSumFreq, actualSumFreq);
 }

 // Test valuesIterator method
 @Test
 public void testValuesIterator() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 Iterator<Long> valuesIterator = frequency.valuesIterator();
 Long expectedValue = Long.valueOf(1);
 Long actualValue = valuesIterator.next();
 assertEquals(expectedValue, actualValue);
 }
}