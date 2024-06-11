package gnu.trove.impl.hash.test;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float> hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash<>(5, 0.75f);
 }

 // Test the constructors
 @Test
 public void testConstructors() {
 TFloatDoubleHash<Float> hashMap1 = new TFloatDoubleHash<>();
 TFloatDoubleHash<Float> hashMap2 = new TFloatDoubleHash<>(3);
 TFloatDoubleHash<Float> hashMap3 = new TFloatDoubleHash<>(3, 0.8f);
 TFloatDoubleHash<Float> hashMap4 = new TFloatDoubleHash<>(3, 0.8f, 0.0f, 0.0);
 }

 // Test the getNoEntryKey and getNoEntryValue methods
 @Test
 public void testGetNoEntryKeyAndGetNoEntryValue() {
 float noEntryKey = hashMap.getNoEntryKey();
 double noEntryValue = hashMap.getNoEntryValue();
 assertEquals(0.0f, noEntryKey, 0.0);
 assertEquals(0.0, noEntryValue, 0.0);
 }

 // Test the setUp method
 @Test
 public void testSetUp() {
 int capacity = hashMap.setUp(5);
 assertEquals(5, capacity);
 }

 // Test the contains method
 @Test
 public void testContains() {
 float key = 1.0f;
 hashMap.put(key, key);
 assertTrue(hashMap.contains(key));
 }

 @Test
 public void testContainsWithEmptyMap() {
 float key = 1.0f;
 assertFalse(hashMap.contains(key));
 }

 // Test the forEach method
 @Test
 public void testForEach() {
 int count = 0;
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(Float floatValue) {
 count++;
 return true;
 }
 };
 hashMap.forEach(procedure);
 assertEquals(3, count);
 }

 // Test the removeAt method
 @Test
 public void testRemoveAt() {
 float key = 1.0f;
 hashMap.put(key, key);
 int index = hashMap.indexOfKey(key);
 hashMap.removeAt(index);
 assertFalse(hashMap.containsKey(key));
 }

 // Test the index method
 @Test
 public void testIndexWithEmptyMap() {
 float key = 1.0f;
 int index = hashMap.index(key);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexWithKeyPresent() {
 float key = 1.0f;
 hashMap.put(key, key);
 int index = hashMap.index(key);
 assertEquals(0, index);
 }

 // Test the indexRehashed method
 @Test
 public void testIndexRehashedWithEmptyMap() {
 float key = 1.0f;
 int index = hashMap.indexRehashed(key, 0, 0, (byte) 0);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexRehashedWithKeyPresent() {
 float key = 1.0f;
 hashMap.put(key, key);
 int index = hashMap.indexRehashed(key, 0, 0, (byte) 1);
 assertEquals(0, index);
 }

 // Test the insertKey method
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullMap() {
 for (float key = 0.0f; key < 5.0f + 1.0f; key++) {
 hashMap.put(key, key);
 }
 try {
 hashMap.put(5.0f, 5.0f);
 } catch (IllegalStateException e) {
 assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
 }
 }

 // Test the insertKeyRehash method
 @Test
 public void testInsertKeyRehash() {
 float key = 5.0f;
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);
 hashMap.put(5.0f, 5.0);
 assertEquals(5, hashMap.size());
 int index = hashMap.insertKey(6.0f);
 assertEquals(6, index);
 assertEquals(6.0f, hashMap.get(index), 0.0);
 }

 // Test the XinsertKey method
 @Test
 public void testXinsertKey() {
 float key = 5.0f;
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);
 hashMap.put(5.0f, 5.0);
 assertEquals(5, hashMap.size());
 int index = hashMap.XinsertKey(6.0f);
 assertEquals(6, index);
 assertEquals(6.0f, hashMap.get(index), 0.0);
 }

 // Test the writeExternal and readExternal methods
 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(output);
 hashMap.writeExternal(out);
 out.close();
 byte[] bytes = output.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TFloatDoubleHash<Float> hashMap2 = (TFloatDoubleHash<Float>) in.readObject();
 in.close();
 assertEquals(hashMap.size(), hashMap2.size());
 for (Map.Entry<Float, Float> entry : hashMap.entrySet()) {
 assertTrue(hashMap2.containsKey(entry.getKey()));
 assertEquals(entry.getValue(), hashMap2.get(entry.getKey()), 0.0);
 }
 }

}