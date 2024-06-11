package com.example.junit4.test; // replace with your test package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testConstructor() {
 TFloatCharHash hash = new TFloatCharHash();
 assertNotNull(hash);

 hash = new TFloatCharHash(10);
 assertNotNull(hash);

 hash = new TFloatCharHash(10, 0.5f);
 assertNotNull(hash);

 hash = new TFloatCharHash(10, 0.5f, 0.0f, '\0');
 assertNotNull(hash);
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals(0.0f, hash.getNoEntryKey(), 0.01f);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TFloatCharHash hash = new TFloatCharHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');

 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));

 assertFalse(hash.contains(4.0f));
 }

 @Test
 public void testForEach() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return (value != 2.0f);
 }
 };

 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');

 hash.removeAt(1);
 }

 @Test
 public void testIndex() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');

 assertEquals(1, hash.index(1.0f));
 assertEquals(2, hash.index(2.0f));
 assertEquals(3, hash.index(3.0f));

 assertEquals(-1, hash.index(4.0f));
 }

 @Test
 public void testInsertKey() {
 TFloatCharHash hash = new TFloatCharHash(5);

 int index = hash.insertKey(1.0f);
 assertEquals(1, index);
 assertEquals(1.0f, hash.get(index), 0.01f);

 index = hash.insertKey(2.0f);
 assertEquals(3, index);
 assertEquals(2.0f, hash.get(index), 0.01f);
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');

 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(baos);
 hash.writeExternal(out);
 out.close();

 TFloatCharHash loadedHash = new TFloatCharHash(3);
 loadedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())));

 assertEquals(3, loadedHash.size());
 assertEquals(1.0f, loadedHash.get(0), 0.01f);
 assertEquals(2.0f, loadedHash.get(1), 0.01f);
 assertEquals(3.0f, loadedHash.get(2), 0.01f);
 }

 @Test
 public void testEmptyHash() {
 TFloatCharHash hash = new TFloatCharHash();
 assertFalse(hash.containsKey(0.0f));
 assertFalse(hash.containsValue('\0'));
 }

 @Test
 public void testNullKey() {
 TFloatCharHash hash = new TFloatCharHash(5);
 assertThrows(NullPointerException.class, () -> hash.put(null, 'A'));
 assertThrows(NullPointerException.class, () -> hash.get((Float) null));
 }

 @Test
 public void testNullValue() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, null);
 assertNull(hash.get(0));
 }

 @Test
 public void testHashCollision() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, 'A');
 hash.put(1.0f, 'B');
 }

 @Test
 public void testLoadFactor() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 hash.put(4.0f, 'D');
 hash.put(5.0f, 'E');
 hash.put(6.0f, 'F');

 assertEquals(0.6f, hash.getLoadFactor(), 0.01f);
 }
}