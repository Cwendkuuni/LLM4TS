package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(5);
 }

 @Test
 public void testEmptyConstructor() {
 assertEquals(0, hash.size());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 assertEquals(5, hash.size());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 assertEquals(5, hash.size());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 assertEquals(5, hash.size());
 assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
 assertEquals(0.0, hash.getNoEntryValue(), 0.0);
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.5f, 0.5f, 0.5);
 assertEquals(0.5f, hash.getNoEntryKey(), 0.0);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.5f, 0.5f, 0.5);
 assertEquals(0.5, hash.getNoEntryValue(), 0.0);
 }

 @Test
 public void testSetUp() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 assertEquals(5, hash.size());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testDoesNotContain() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testForEach() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return key > 1.0f;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 hash.removeAt(0);
 }

 @Test
 public void testIndex() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 assertEquals(1, hash.index(1.0f));
 assertEquals(-1, hash.index(3.0f));
 }

 @Test
 public void testInsertKey() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 assertEquals(0, hash.size());
 hash.put(1.0f, 1.0);
 assertEquals(1, hash.size());
 }

 @Test
 public void testInsertKeyRehash() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 assertEquals(3, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 for (int i = 0; i < 5; i++) {
 hash.put(i, i);
 }
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutputStream os = new ObjectOutputStream(output);
 hash.writeExternal(os);
 TFloatDoubleHash hash2 = new TFloatDoubleHash();
 hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(output.toByteArray())));
 assertEquals(2, hash2.size());
 assertEquals(1.0f, hash2.get(0.0f), 0.0);
 assertEquals(2.0f, hash2.get(1.0f), 0.0);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatDoubleHash hash = new TFloatDoubleHash();
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutputStream os = new ObjectOutputStream(output);
 hash.writeExternal(os);
 ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
 ObjectInputStream is = new ObjectInputStream(input);
 TFloatDoubleHash hash2 = (TFloatDoubleHash) is.readObject();
 assertEquals(0, hash2.size());
 assertNull(hash2.getNoEntryValue());
 }
}