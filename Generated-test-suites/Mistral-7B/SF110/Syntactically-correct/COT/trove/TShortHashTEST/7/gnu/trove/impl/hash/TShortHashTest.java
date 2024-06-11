import org.junit.Test;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;
import java.util.Arrays;
import static org.junit.Assert.*;

public class TShortHashTest {

 TShortHash hash;
 short[] testData = {-128, 0, 127, 128, 129, 130, 131};

 @Test
 public void testConstructor() {
 hash = new TShortHash(7);
 assertEquals(7, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.size());
 assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
 hash = new TShortHash(10, 0.75f, (short) 1);
 assertEquals(10, hash.size());
 assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
 assertEquals((short) 1, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TShortHash();
 assertEquals(Short.MIN_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 hash = new TShortHash();
 assertFalse(hash.contains((short) 0));
 for (short data : testData) {
 hash.put(data);
 assertTrue(hash.contains(data));
 }
 }

 @Test
 public void testForEach() {
 boolean[] result = new boolean[testData.length];
 hash = new TShortHash(testData.length * 2);
 for (short data : testData) {
 hash.put(data);
 }
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 result[Arrays.asList(testData).indexOf(shortValue)] = true;
 return true;
 }
 };
 hash.forEach(procedure);
 for (int i = 0; i < result.length; i++) {
 assertTrue(result[i]);
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtIndexOutOfBounds() {
 hash = new TShortHash(3);
 hash.put(1);
 hash.put(2);
 hash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 hash = new TShortHash();
 for (short data : testData) {
 hash.put(data);
 int index = hash.index(data);
 assertEquals(index, Arrays.asList(testData).indexOf(data));
 }
 }

 @Test
 public void testIndexRehashed() {
 hash = new TShortHash();
 for (short data : testData) {
 hash.put(data);
 }
 int index = hash.indexRehashed((short) 131, 0, 123, (byte) 1);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 hash = new TShortHash();
 for (short data : testData) {
 hash.put(data);
 }
 short newData = (short) 132;
 int index = hash.insertKey(newData);
 assertEquals(-(newData), index);
 }

 @Test
 public void testInsertKeyAt() {
 hash = new TShortHash();
 for (short data : testData) {
 hash.put(data);
 }
 short newData = (short) 132;
 hash.insertKeyAt(0, newData);
 assertEquals(newData, hash.get(0));
 }
}