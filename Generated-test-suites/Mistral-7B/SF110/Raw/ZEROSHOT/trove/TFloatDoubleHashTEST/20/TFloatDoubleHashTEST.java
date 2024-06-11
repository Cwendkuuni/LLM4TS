package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Test
 public void testEmptyHash() {
 hash = new TFloatDoubleHash(5);
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testBasicFunctions() {
 hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);

 assertEquals(3, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertFalse(hash.contains(4.0f));

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return key >= 1.5f;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testLoadFactor() {
 hash = new TFloatDoubleHash(3, 0.5f);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);

 assertEquals(7, hash.capacity());
 assertEquals(4, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertTrue(hash.contains(4.0f));
 assertFalse(hash.contains(5.0f));
 }

 @Test
 public void testSerialization() throws Exception {
 byte[] bytes = new ByteArrayOutputStream().toByteArray(new ObjectOutputStream(new ByteArrayOutputStream())
 .writeObject(new TFloatDoubleHash(5)));
 TFloatDoubleHash hash2 = (TFloatDoubleHash) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();

 assertEquals(5, hash2.capacity());
 assertEquals(0.0f, hash2.getNoEntryKey(), 0.01f);
 assertEquals(0.0, hash2.getNoEntryValue(), 0.01);
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHash() {
 hash = new TFloatDoubleHash(3, 0.5f);
 for (float i = 0.0f; i < 7.0f; i++) {
 hash.put(i, i);
 }
 }
}