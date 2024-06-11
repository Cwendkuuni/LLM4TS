import org.apache.commons.math.stat.Frequency;
import org.apache.commons.math.stat.Frequency.NaturalComparator;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 @Test
 public void testDefaultConstructor() {
 Frequency frequency = new Frequency();
 // basic functionality test
 frequency.addValue(1);
 frequency.addValue(1);
 long count = frequency.getCount(1L);
 assertEquals(2, count);
 }

 @Test
 public void testComparatorConstructor() {
 Frequency frequency = new Frequency(new NaturalComparator());
 // basic functionality test
 frequency.addValue('a');
 frequency.addValue('a');
 long count = frequency.getCount('a');
 assertEquals(2, count);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('a');
 }

 @Test
 public void testAddValueWithSameValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 long count = frequency.getCount(1L);
 assertEquals(2, count);
 }

 @Test
 public void testAddValueWithDifferentValues() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 long count1 = frequency.getCount(1L);
 long count2 = frequency.getCount(2L);
 assertEquals(1, count1);
 assertEquals(1, count2);
 }

 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.clear();
 long count = frequency.getCount(1L);
 assertEquals(0, count);
 }

 @Test
 public void testValuesIterator() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 Iterator iterator = frequency.valuesIterator();
 long count = 0;
 while (iterator.hasNext()) {
 long value = (Long) iterator.next();
 count += frequency.getCount(value);
 }
 assertEquals(2, count);
 }

 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 long sumFreq = frequency.getSumFreq();
 assertEquals(2, sumFreq);
 }

 @Test
 public void testGetCount() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 long count = frequency.getCount(1L);
 assertEquals(2, count);
 }

 @Test
 public void testGetCountWithNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('a');
 long count = frequency.getCount('a');
 assertEquals(0, count);
 }

 @Test
 public void testGetPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 double pct = frequency.getPct(1L);
 assertEquals(0.5, pct, 0.001);
 }

 @Test
 public void testGetPctWithNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('a');
 double pct = frequency.getPct('a');
 assertEquals(Double.NaN, pct, 0.0);
 }

 @Test
 public void testGetCumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 long cumFreq = frequency.getCumFreq(1L);
 assertEquals(1, cumFreq);
 }

 @Test
 public void testGetCumFreqWithNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('a');
 long cumFreq = frequency.getCumFreq('a');
 assertEquals(0, cumFreq);
 }

 @Test
 public void testGetCumFreqWithLastValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 long cumFreq = frequency.getCumFreq(2L);
 assertEquals(2, cumFreq);
 }

 @Test
 public void testGetCumFreqWithLastValuePlusOne() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(2);
 long cumFreq = frequency.getCumFreq(3L);
 assertEquals(2, cumFreq);
 }

 @Test
 public void testGetCumPct() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 double cumPct = frequency.getCumPct(1L);
 assertEquals(0.5, cumPct, 0.001);
 }

 @Test
 public void testGetCumPctWithNonComparableValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue('a');
 double cumPct = frequency.getCumPct('a');
 assertEquals(Double.NaN, cumPct, 0.0);
 }

 @Test
 public void testGetCumPctWithLastValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 double cumPct = frequency.getCumPct(1L);
 assertEquals(0.5, cumPct, 0.001);
 double cumPctLast = frequency.getCumPct(2L);
 assertEquals(1.0, cumPctLast, 0.001);
 }
}