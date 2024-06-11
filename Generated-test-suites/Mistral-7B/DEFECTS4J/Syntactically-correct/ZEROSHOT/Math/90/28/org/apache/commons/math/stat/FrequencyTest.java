import org.apache.commons.math3.stat.Frequency;
import org.junit.Test;
import static org.junit.Assert.*;

public class FrequencyTest {

 @Test
 public void testDefaultConstructor() {
 Frequency frequency = new Frequency();
 // Test empty frequency distribution
 assertEquals(0, frequency.getSumFreq());
 assertEquals(0, frequency.getCount(1));
 assertEquals(0, frequency.getCount('a'));
 }

 @Test
 public void testConstructorWithComparator() {
 Frequency frequency = new Frequency(new Comparator<Object>() {
 @Override
 public int compare(Object o1, Object o2) {
 return ((Comparable) o1).compareTo(o2);
 }
 });
 // Test empty frequency distribution
 assertEquals(0, frequency.getSumFreq());
 assertEquals(0, frequency.getCount(1));
 assertEquals(0, frequency.getCount('a'));
 }

 @Test
 public void testAddValue() {
 Frequency frequency = new Frequency();
 // Test adding an int value
 frequency.addValue(1);
 assertEquals(1, frequency.getCount(1));
 // Test adding a long value
 frequency.addValue(2);
 assertEquals(1, frequency.getCount(1));
 assertEquals(1, frequency.getCount(2));
 // Test adding a char value
 frequency.addValue('a');
 assertEquals(1, frequency.getCount('a'));
 // Test adding a different type value
 assertThrows(IllegalArgumentException.class, () -> frequency.addValue("test"));
 // Test adding a non-comparable value
 assertThrows(IllegalArgumentException.class, () -> frequency.addValue(new Object()));
 }

 @Test
 public void testAddValueWithSameValue() {
 Frequency frequency = new Frequency();
 // Test adding the same value multiple times
 for (int i = 0; i < 10; i++) {
 frequency.addValue(1);
 }
 assertEquals(10, frequency.getCount(1));
 }

 @Test
 public void testClear() {
 Frequency frequency = new Frequency();
 // Test adding values
 frequency.addValue(1);
 frequency.addValue(2);
 // Test clearing the frequency distribution
 frequency.clear();
 // Test empty frequency distribution
 assertEquals(0, frequency.getSumFreq());
 assertEquals(0, frequency.getCount(1));
 assertEquals(0, frequency.getCount(2));
 }

 @Test
 public void testValuesIterator() {
 Frequency frequency = new Frequency();
 // Test adding values
 frequency.addValue(1);
 frequency.addValue(2);
 // Test getting an iterator over the values
 Iterator<Object> iterator = frequency.valuesIterator();
 // Test iterator has the expected values
 assertEquals(1, iterator.next().intValue());
 assertEquals(2, iterator.next().intValue());
 }

 @Test
 public void testGetSumFreq() {
 Frequency frequency = new Frequency();
 // Test empty frequency distribution
 assertEquals(0, frequency.getSumFreq());
 // Test adding values
 frequency.addValue(1);
 frequency.addValue(2);
 assertEquals(3, frequency.getSumFreq());
 }

 @Test
 public void testGetCount() {
 Frequency frequency = new Frequency();
 // Test empty frequency distribution
 assertEquals(0, frequency.getCount(1));
 assertEquals(0, frequency.getCount('a'));
 // Test adding values
 frequency.addValue(1);
 frequency.addValue(1);
 assertEquals(2, frequency.getCount(1));
 // Test getting the count of a non-existent value
 assertEquals(0, frequency.getCount(3));
 }

 @Test
 public void testGetPct() {
 Frequency frequency = new Frequency();
 // Test empty frequency distribution
 assertEquals(Double.NaN, frequency.getPct(1));
 assertEquals(Double.NaN, frequency.getPct('a'));
 // Test adding values
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 assertEquals(0.3333333333333333, frequency.getPct(1), 0.0001);
 assertEquals(0.3333333333333333, frequency.getPct(1.0), 0.0001);
 assertEquals(0.3333333333333333, frequency.getPct((short) 1), 0.0001);
 assertEquals(0.3333333333333333, frequency.getPct((byte) 1), 0.0001);
 assertEquals(0.3333333333333333, frequency.getPct((char) '1'), 0.0001);
 assertEquals(0.3333333333333333, frequency.getPct((byte) 2), 0.0001);
 }

 @Test
 public void testGetCumFreq() {
 Frequency frequency = new Frequency();
 // Test empty frequency distribution
 assertEquals(0, frequency.getCumFreq(1));
 assertEquals(0, frequency.getCumFreq('a'));
 // Test adding values
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 assertEquals(1, frequency.getCumFreq(1));
 assertEquals(2, frequency.getCumFreq(1.0));
 assertEquals(2, frequency.getCumFreq((short) 1));
 assertEquals(2, frequency.getCumFreq((byte) 1));
 assertEquals(2, frequency.getCumFreq((char) '1'));
 assertEquals(3, frequency.getCumFreq(2));
 assertEquals(3, frequency.getCumFreq(2.0));
 assertEquals(3, frequency.getCumFreq((short) 2));
 assertEquals(3, frequency.getCumFreq((byte) 2));
 assertEquals(3, frequency.getCumFreq((char) '2'));
 }

 @Test
 public void testGetCumPct() {
 Frequency frequency = new Frequency();
 // Test empty frequency distribution
 assertEquals(Double.NaN, frequency.getCumPct(1));
 assertEquals(Double.NaN, frequency.getCumPct('a'));
 // Test adding values
 frequency.addValue(1);
 frequency.addValue(1);
 frequency.addValue(2);
 assertEquals(0.3333333333333333, frequency.getCumPct(1), 0.0001);
 assertEquals(0.3333333333333333, frequency.getCumPct(1.0), 0.0001);
 assertEquals(0.3333333333333333, frequency.getCumPct((short) 1), 0.0001);
 assertEquals(0.3333333333333333, frequency.getCumPct((byte) 1), 0.0001);
 assertEquals(0.3333333333333333, frequency.getCumPct((char) '1'), 0.0001);
 assertEquals(0.6666666666666667, frequency.getCumPct(2), 0.0001);
 assertEquals(0.6666666666666667, frequency.getCumPct(2.0), 0.0001);
 assertEquals(0.6666666666666667, frequency.getCumPct((short) 2), 0.0001);
 assertEquals(0.6666666666666667, frequency.getCumPct((byte) 2), 0.0001);
 assertEquals(0.6666666666666667, frequency.getCumPct((char) '2'), 0.0001);
 }

}