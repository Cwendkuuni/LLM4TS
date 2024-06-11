package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Test
 public void testConstructor() {
 hash = new TFloatDoubleHash();
 assertEquals(0, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 hash = new TFloatDoubleHash(10);
 assertEquals(0, hash.size());
 assertEquals(10, hash.capacity());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 hash = new TFloatDoubleHash(10, 0.75f);
 assertEquals(0, hash.size());
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.001f);
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndKeys() {
 hash = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);
 assertEquals(0, hash.size());
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.001f);
 assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
 assertEquals(0.0, hash.getNoEntryValue(), 0.0);
 }

 @Test
 public void testSetUp() {
 hash = new TFloatDoubleHash(5);
 assertEquals(5, hash.capacity());
 assertNull(hash._set);
 }

 @Test
 public void testContains() {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(0.5f);
 assertTrue(hash.contains(0.5f));
 }

 @Test
 public void testContainsFalse() {
 hash = new TFloatDoubleHash(5);
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(0.5f);
 hash.insertKey(1.0f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return key == 0.5f;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 hash = new TFloatDoubleHash(5);
 hash.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(0.5f);
 hash.removeAt(0);
 assertEquals(0, hash.size());
 assertFalse(hash.contains(0.5f));
 }

 @Test
 public void testIndex() {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(0.5f);
 hash.insertKey(1.0f);
 hash.insertKey(0.5f);
 assertEquals(1, hash.index(0.5f));
 }

 @Test
 public void testIndexNegative() {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(0.5f);
 hash.insertKey(1.0f);
 hash.insertKey(0.5f);
 assertEquals(-1, hash.index(1.0f));
 }

 @Test
 public void testInsertKey() {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(0.5f);
 assertEquals(1, hash.size());
 assertTrue(hash.contains(0.5f));
 }

 @Test
 public void testInsertKeyRehashed() {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(0.5f);
 hash.insertKey(1.0f);
 hash.insertKey(0.5f);
 hash.insertKey(1.5f);
 hash.insertKey(1.1f);
 assertEquals(5, hash.size());
 assertTrue(hash.contains(0.5f));
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(1.1f));
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 hash = new TFloatDoubleHash(5);
 hash.insertKey(0.5f);
 hash.writeExternal(objectOutputStream);
 objectOutputStream.close();
 byte[] bytes = byteArrayOutputStream.toByteArray();
 ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TFloatDoubleHash hash2 = (TFloatDoubleHash) objectInputStream.readObject();
 objectInputStream.close();
 assertTrue(hash2.contains(0.5f));
 }

}