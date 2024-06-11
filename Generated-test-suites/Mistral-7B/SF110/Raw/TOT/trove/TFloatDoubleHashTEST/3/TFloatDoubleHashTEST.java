import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash(5, 0.75f);
 }

 @Test
 public void testContains() {
 // Test with an empty hash map
 assertFalse(hashMap.contains(Float.NaN));
 // Test with a hash map containing an element
 hashMap.insertKey(1.0f);
 assertTrue(hashMap.contains(1.0f));
 // Test with a hash map containing an element using a different data type
 hashMap.put(1.0f, "Test");
 assertTrue(hashMap.contains(1.0f));
 // Test with a non-existent key
 assertFalse(hashMap.contains(2.0f));
 }

 @Test
 public void testForEach() {
 // Test with an empty hash map
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float val) {
 return true;
 }
 };
 hashMap.forEach(procedure);
 assertTrue(procedure.wasCalled());
 // Test with a hash map containing an element
 hashMap.insertKey(1.0f);
 procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float val) {
 assertEquals(1.0f, val);
 return true;
 }
 };
 hashMap.forEach(procedure);
 assertTrue(procedure.wasCalled());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInitialCapacity() {
 new TFloatDoubleHash(-5, 0.75f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidLoadFactor() {
 new TFloatDoubleHash(5, 1.5f);
 }

 @Test
 public void testGetNoEntryKeyAndValue() {
 assertEquals(0.0f, hashMap.getNoEntryKey());
 assertEquals(0.0, hashMap.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hashMap.setUp(10);
 assertEquals(10, hashMap._set.length);
 }

 @Test
 public void testRemoveAt() {
 hashMap.insertKey(1.0f);
 hashMap.removeAt(0);
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testIndex() {
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 int index1 = hashMap.index(1.0f);
 int index2 = hashMap.index(2.0f);
 int index3 = hashMap.index(3.0f);
 assertEquals(0, index1);
 assertEquals(1, index2);
 assertEquals(2, index3);
 }

 @Test
 public void testInsertKey() {
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 assertEquals(3, hashMap.size());
 }

 @Test
 public void testInsertKeyRehash() {
 // Test with the initial capacity
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 assertEquals(4, hashMap.size());
 // Test with a rehashed array
 hashMap.setUp(10);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.insertKey(5.0f);
 assertEquals(5, hashMap.size());
 }

 @Test
 public void testXinsertKey() {
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.XinsertKey(5.0f);
 assertEquals(5, hashMap.size());
 }
}