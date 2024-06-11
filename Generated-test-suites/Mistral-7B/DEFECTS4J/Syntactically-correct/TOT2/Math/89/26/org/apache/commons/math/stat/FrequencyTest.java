import org.apache.commons.math.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 // Test constructor
 @Test
 public void testEmptyFrequency() {
 Frequency frequency = new Frequency();
 assertEquals(0, frequency.getSumFreq());
 assertNull(frequency.valuesIterator().next());
 }

 // Test constructor with comparator
 @Test
 public void testFrequencyWithComparator() {
 Comparator comparator = new Comparator() {
 @Override
 public int compare(Object o1, Object o2) {
 return ((Comparable) o1).compareTo(o2);
 }
 };
 Frequency frequency = new Frequency(comparator);
 Object[] values = {"apple", "banana", "apple", "orange"};
 for (Object value : values) {
 frequency.addValue(value);
 }
 Object[] expectedValues = {"apple", "banana", "orange"};
 Iterator iterator = frequency.valuesIterator();
 int i = 0;
 while (iterator.hasNext()) {
 assertEquals(expectedValues[i++], iterator.next());
 }
 }

 // Test addValue method with int
 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithIntAndExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 }

 // Test addValue method with long
 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithLongAndExistingValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1L);
 frequency.addValue(1L);
 }

 // Test addValue method with char
 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithCharAndExistingValues() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 }

 // Test addValue method with Object
 @Test(expected = IllegalArgumentException.class)
 public void testAddValueWithNonComparableObject() {
 Frequency frequency = new Frequency();
 frequency.addValue(new Object());
 frequency.addValue(new Object());
 }

 // Test clear method
 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.clear();
 assertEquals(0, frequency.getSumFreq());
 assertNull(frequency.valuesIterator().next());
 }

 // Test getSumFreq method
 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(7, frequency.getSumFreq());
 }

 // Test getCount method with int
 @Test
 public void testGetCountWithInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(2, frequency.getCount(1));
 }

 // Test getCount method with long
 @Test
 public void testGetCountWithLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(2, frequency.getCount(1L));
 }

 // Test getCount method with char
 @Test
 public void testGetCountWithChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('b');
 assertEquals(2, frequency.getCount('a'));
 }

 // Test getCount method with non-existent value
 @Test
 public void testGetCountWithNonExistentValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(0, frequency.getCount('c'));
 }

 // Test getPct method with int
 @Test
 public void testGetPctWithInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(0.2857142857142857, frequency.getPct(1), 0.001);
 }

 // Test getPct method with long
 @Test
 public void testGetPctWithLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(0.2857142857142857, frequency.getPct(1L), 0.001);
 }

 // Test getPct method with char
 @Test
 public void testGetPctWithChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('b');
 assertEquals(0.4285714285714286, frequency.getPct('a'), 0.001);
 }

 // Test getPct method with non-existent value
 @Test
 public void testGetPctWithNonExistentValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(Double.NaN, frequency.getPct('c'));
 }

 // Test getCumFreq method with int
 @Test
 public void testGetCumFreqWithInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(1, frequency.getCumFreq(1));
 assertEquals(3, frequency.getCumFreq(2));
 assertEquals(6, frequency.getCumFreq(3));
 }

 // Test getCumFreq method with long
 @Test
 public void testGetCumFreqWithLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(1, frequency.getCumFreq(1L));
 assertEquals(3, frequency.getCumFreq(2L));
 assertEquals(6, frequency.getCumFreq(3L));
 }

 // Test getCumFreq method with char
 @Test
 public void testGetCumFreqWithChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('b');
 assertEquals(1, frequency.getCumFreq('a'));
 assertEquals(2, frequency.getCumFreq('a'));
 assertEquals(4, frequency.getCumFreq('b'));
 assertEquals(4, frequency.getCumFreq('b'));
 }

 // Test getCumFreq method with non-existent value
 @Test
 public void testGetCumFreqWithNonExistentValue() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(0, frequency.getCumFreq('c'));
 }

 // Test getCumPct method with int
 @Test
 public void testGetCumPctWithInt() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(0.2857142857142857, frequency.getPct(1), 0.001);
 assertEquals(0.4285714285714286, frequency.getPct(2), 0.001);
 assertEquals(0.7142857142857143, frequency.getPct(3), 0.001);
 }

 // Test getCumPct method with long
 @Test
 public void testGetCumPctWithLong() {
 Frequency frequency = new Frequency();
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 frequency.addValue(3);
 assertEquals(0.2857142857142857, frequency.getPct(1L), 0.001);
 assertEquals(0.4285714285714286, frequency.getPct(2L), 0.001);
 assertEquals(0.7142857142857143, frequency.getPct(3L), 0.001);
 }

 // Test getCumPct method with char
 @Test
 public void testGetCumPctWithChar() {
 Frequency frequency = new Frequency();
 frequency.addValue('a');
 frequency.addValue('a');
 frequency.addValue('b');
 frequency.addValue('b');
 assertEquals(0.4285714285714286, frequency.getPct('a'), 0.001);
 assertEquals(0.6142857142857143, frequency.getPct('b'), 0.001);
 assertEquals(1.0, frequency.getPct('a') + frequency.getPct('b'), 0.001);
 }

}