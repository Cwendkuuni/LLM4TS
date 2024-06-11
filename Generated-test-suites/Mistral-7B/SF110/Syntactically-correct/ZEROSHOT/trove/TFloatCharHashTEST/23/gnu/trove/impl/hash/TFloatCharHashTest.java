package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Test
 public void testEmptyHash() {
 hash = new TFloatCharHash();
 assertTrue(hash.isEmpty());
 assertEquals(0, hash.size());
 }

 @Test
 public void testHashWithInitialCapacity() {
 hash = new TFloatCharHash(5);
 assertFalse(hash.isEmpty());
 assertEquals(0, hash.size());
 }

 @Test
 public void testHashWithInitialCapacityAndLoadFactor() {
 hash = new TFloatCharHash(5, 0.75f);
 assertFalse(hash.isEmpty());
 assertEquals(0, hash.size());
 }

 @Test
 public void testHashWithInitialCapacityAndLoadFactorAndNoEntryValues() {
 hash = new TFloatCharHash(5, 0.75f, 1.0f, 'X');;
 assertFalse(hash.isEmpty());
 assertEquals(0, hash.size());
 }

 @Test
 public void testGetNoEntryKey() {
 hash = new TFloatCharHash();
 assertEquals(0.0f, hash.getNoEntryKey(), 0.001f);
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TFloatCharHash();
 assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TFloatCharHash(5);
 assertEquals(5, hash.setUp(5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUpWithNegativeInitialCapacity() {
 hash = new TFloatCharHash(-5);
 }

 @Test
 public void testContains() {
 hash.put(1.0f, 'A');
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsWithNonExistentKey() {
 hash.put(1.0f, 'A');
 assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testForEach() {
 final char expectedValue = 'A';
 hash.put(1.0f, expectedValue);
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return expectedValue == hash.get(key);
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 int indexToRemove = 1;
 hash.removeAt(indexToRemove);
 assertEquals(2, hash.size());
 assertFalse(hash.containsKey(1.0f));
 assertEquals('C', hash.get(2.0f));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWithInvalidIndex() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 int indexOfKey = hash.indexOfKey(2.0f);
 assertEquals(1, indexOfKey);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexWithNonExistentKey() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 int indexOfKey = hash.indexOfKey(4.0f);
 }

 @Test
 public void testInsertKey() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 float keyToInsert = 4.0f;
 int index = hash.insertKey(keyToInsert);
 assertEquals(4, hash.size());
 assertEquals(keyToInsert, hash.get(index));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHash() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 hash.put(4.0f, 'D');
 hash.put(5.0f, 'E');
 hash.put(6.0f, 'F');
 float keyToInsert = 7.0f;
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 hash.writeExternal(objectOutputStream);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 TFloatCharHash loadedHash = new TFloatCharHash();
 loadedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(hash.getNoEntryKey(), loadedHash.getNoEntryKey());
 assertEquals(hash.getNoEntryValue(), loadedHash.getNoEntryValue());
 assertEquals(hash.size(), loadedHash.size());
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 hash.writeExternal(objectOutputStream);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 TFloatCharHash loadedHash = new TFloatCharHash();
 loadedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(hash.getNoEntryKey(), loadedHash.getNoEntryKey());
 assertEquals(hash.getNoEntryValue(), loadedHash.getNoEntryValue());
 assertEquals(hash.size(), loadedHash.size());
 }

}