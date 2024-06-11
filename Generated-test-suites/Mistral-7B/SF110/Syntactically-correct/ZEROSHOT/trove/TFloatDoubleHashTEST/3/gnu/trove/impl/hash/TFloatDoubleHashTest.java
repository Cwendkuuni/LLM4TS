package tests;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Test
 public void testEmptyHash() {
 hash = new TFloatDoubleHash(10);
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testBasicFunctionality() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 assertFalse(hash.contains(4.0f));
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertEquals(3, hash.size());
 }

 @Test
 public void testLoadFactor() {
 hash = new TFloatDoubleHash(6, 0.75f);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);
 hash.put(6.0f, 6.0);
 hash.put(7.0f, 7.0);
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertTrue(hash.contains(4.0f));
 assertTrue(hash.contains(5.0f));
 assertTrue(hash.contains(6.0f));
 assertTrue(hash.contains(7.0f));
 assertEquals(7, hash.size());
 }

 @Test
 public void testNoEntryValues() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
 assertEquals(0.0, hash.getNoEntryValue(), 0.0);
 }

 @Test
 public void testPutNoEntryKey() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);
 hash.put(6.0f, 6.0);
 hash.put(7.0f, 7.0);
 hash.put(8.0f, 8.0, 1.1f, 1.0);
 assertEquals(1.1f, hash.getNoEntryKey(), 0.0f);
 assertEquals(1.0, hash.getNoEntryValue(), 0.0);
 }

 @Test
 public void testRemove() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.remove(2.0f);
 assertFalse(hash.contains(2.0f));
 assertEquals(2, hash.size());
 }

 @Test
 public void testRemoveNonExistentKey() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.remove(4.0f);
 assertEquals(3, hash.size());
 assertFalse(hash.contains(4.0f));
 }

 @Test
 public void testWriteRead() throws IOException {
 byte[] byteArray = new byte[1024];
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream(byteArray);
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 objStream.writeObject(hash);
 objStream.close();
 ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteArray);
 ObjectInputStream objInStream = new ObjectInputStream(byteInStream);
 TFloatDoubleHash readHash = (TFloatDoubleHash) objInStream.readObject();
 objInStream.close();
 assertTrue(hash.equals(readHash));
 }

 @Test
 public void testForEach() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override public boolean execute(float value) {
 assertEquals(value, 1.0f, 0.0f);
 return true;
 }
 };
 hash.forEach(procedure);
 }
}