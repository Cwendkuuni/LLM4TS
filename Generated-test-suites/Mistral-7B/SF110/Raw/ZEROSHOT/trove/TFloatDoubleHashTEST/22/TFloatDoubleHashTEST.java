package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;
 private TFloatProcedure procedure;

 @Test
 public void testTFloatDoubleHashConstruction() {
 hash = new TFloatDoubleHash();
 assertEquals(0, hash.size());
 }

 @Test
 public void testTFloatDoubleHashInitialCapacity() {
 hash = new TFloatDoubleHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testTFloatDoubleHashInitialCapacityLoadFactor() {
 hash = new TFloatDoubleHash(10, 0.75f);
 assertEquals(10, hash.size());
 assertEquals(7, hash.capacity());
 }

 @Test
 public void testTFloatDoubleHashInitialCapacityLoadFactorNoEntry() {
 hash = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);
 assertEquals(10, hash.size());
 assertEquals(7, hash.capacity());
 assertEquals(0.0f, hash.getNoEntryKey(), 0.001f);
 assertEquals(0.0, hash.getNoEntryValue(), 0.001);
 }

 @Test
 public void testSetUp() {
 hash = new TFloatDoubleHash(10);
 hash.setUp(15);
 assertEquals(15, hash.size());
 }

 @Test
 public void testContains() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testNoEntryKey() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 assertEquals(0.0f, hash.getNoEntryKey(), 0.001f);
 }

 @Test
 public void testNoEntryValue() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 assertEquals(0.0, hash.getNoEntryValue(), 0.001);
 }

 @Test
 public void testForEach() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 count++;
 return true;
 }
 };
 hash.forEach(procedure);
 assertEquals(3, count);
 }

 @Test
 public void testRemoveAt() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.removeAt(1);
 assertEquals(2, hash.size());
 }

 @Test
 public void testIndex() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 assertEquals(1, hash.index(1.0f));
 }

 @Test
 public void testIndexRehashed() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 assertEquals(-1, hash.index(5.0f));
 }

 @Test
 public void testInsertKey() {
 hash = new TFloatDoubleHash(10);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);
 assertEquals(5, hash.size());
 assertEquals(1, hash.index(1.0f));
 assertEquals(2, hash.index(2.0f));
 assertEquals(3, hash.index(3.0f));
 assertEquals(4, hash.index(4.0f));
 assertEquals(5, hash.index(5.0f));
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatDoubleHash hash1 = new TFloatDoubleHash(10);
 hash1.put(1.0f, 1.0);
 hash1.put(2.0f, 2.0);
 hash1.put(3.0f, 3.0);
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(out);
 hash1.writeExternal(objectOutput);
 ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 TFloatDoubleHash hash2 = new TFloatDoubleHash();
 hash2.readExternal(objectInput);
 assertEquals(hash1, hash2);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatDoubleHash hash1 = new TFloatDoubleHash(10);
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(out);
 hash1.writeExternal(objectOutput);
 ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 TFloatDoubleHash hash2 = new TFloatDoubleHash();
 hash2.readExternal(objectInput);
 assertEquals(hash1, hash2);
 }
}