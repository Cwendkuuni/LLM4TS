import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 // Test case for default constructor
 @Test
 public void testDefaultConstructor() {
 Frequency frequency = new Frequency();
 // Basic functionality test
 frequency.addValue(1);
 frequency.addValue(1);
 long count = frequency.getCount(1);
 assertEquals(2, count);
 }

 // Test case for constructor with Comparator
 @Test
 public void testComparatorConstructor() {
 Frequency frequency = new Frequency(new NaturalComparator());
 // Basic functionality test
 frequency.addValue('A');
 frequency.addValue('A');
 long count = frequency.getCount('A');
 assertEquals(2, count);
 }

 // Test case for addValue method with valid int and long values
 @Test
 public void testAddValueValidIntAndLong() {
 Frequency frequency = new Frequency();
 // Basic functionality test
 frequency.addValue(1);
 frequency.addValue(2);
 long count = frequency.getCount(1);
 assertEquals(2, count);
 }

 // Test case for addValue method with valid char value
 @Test
 public void testAddValueValidChar() {
 Frequency frequency = new Frequency();
 // Basic functionality test
 frequency.addValue('A');
 frequency.addValue('A');
 long count = frequency.getCount('A');
 assertEquals(2, count);
 }

 // Test case for addValue method with null value
 @Test(expected = IllegalArgumentException.class)
 public void testAddValueNullValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(null);
 }

 // Test case for addValue method with non-comparable values
 @Test(expected = IllegalArgumentException.class)
 public void testAddValueNonComparableValues() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('A');
 }

 // Test case for getCount method with valid int and long values
 @Test
 public void testGetCountValidIntAndLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 long count = frequency.getCount(1);
 assertEquals(2, count);
 }

 // Test case for getCount method with valid char value
 @Test
 public void testGetCountValidChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('A');
 frequency.addValue('A');
 long count = frequency.getCount('A');
 assertEquals(2, count);
 }

 // Test case for getCount method with non-comparable value
 @Test
 public void testGetCountNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('A');
 long count = frequency.getCount(1);
 assertEquals(1, count);
 }

 // Test case for getCount method with non-existing value
 @Test
 public void testGetCountNonExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 long count = frequency.getCount(2);
 assertEquals(0, count);
 }

 // Test case for getSumFreq method
 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 long sumFreq = frequency.getSumFreq();
 assertEquals(2, sumFreq);
 }

 // Test case for getPct method with valid int and long values
 @Test
 public void testGetPctValidIntAndLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 double pct = frequency.getPct(1);
 assertEquals(0.5, pct, 0.01);
 }

 // Test case for getPct method with valid char value
 @Test
 public void testGetPctValidChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('A');
 frequency.addValue('A');
 double pct = frequency.getPct('A');
 assertEquals(0.5, pct, 0.01);
 }

 // Test case for getPct method with non-comparable value
 @Test
 public void testGetPctNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('A');
 double pct = frequency.getPct(1);
 assertEquals(0.0, pct, 0.01);
 }

 // Test case for getPct method with non-existing value
 @Test
 public void testGetPctNonExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 double pct = frequency.getPct(2);
 assertEquals(0.0, pct, 0.01);
 }

 // Test case for getCumFreq method with valid int and long values
 @Test
 public void testGetCumFreqValidIntAndLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 long cumFreq = frequency.getCumFreq(1);
 assertEquals(1, cumFreq);
 }

 // Test case for getCumFreq method with valid char value
 @Test
 public void testGetCumFreqValidChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('A');
 frequency.addValue('A');
 long cumFreq = frequency.getCumFreq('A');
 assertEquals(1, cumFreq);
 }

 // Test case for getCumFreq method with non-comparable value
 @Test
 public void testGetCumFreqNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('A');
 long cumFreq = frequency.getCumFreq(1);
 assertEquals(1, cumFreq);
 }

 // Test case for getCumFreq method with value greater than all values in frequency distribution
 @Test
 public void testGetCumFreqValueGreaterThanAllValues() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 long cumFreq = frequency.getCumFreq(2);
 assertEquals(0, cumFreq);
 }

 // Test case for getCumPct method with valid int and long values
 @Test
 public void testGetCumPctValidIntAndLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 double cumPct = frequency.getCumPct(1);
 assertEquals(0.5, cumPct, 0.01);
 }

 // Test case for getCumPct method with valid char value
 @Test
 public void testGetCumPctValidChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('A');
 frequency.addValue('A');
 double cumPct = frequency.getCumPct('A');
 assertEquals(0.5, cumPct, 0.01);
 }

 // Test case for getCumPct method with non-comparable value
 @Test
 public void testGetCumPctNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('A');
 double cumPct = frequency.getCumPct(1);
 assertEquals(0.0, cumPct, 0.01);
 }

 // Test case for getCumPct method with non-existing value
 @Test
 public void testGetCumPctNonExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 double cumPct = frequency.getCumPct(2);
 assertEquals(0.0, cumPct, 0.01);
 }

 // Test case for clear method
 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.clear();
 long count = frequency.getCount(1);
 assertEquals(0, count);
 }
}