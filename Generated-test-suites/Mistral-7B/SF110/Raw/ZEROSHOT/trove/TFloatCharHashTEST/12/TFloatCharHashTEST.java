package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Test
 public void testConstructor() {
 hash = new TFloatCharHash();
 assertEquals(0, hash.size());
 }

 @Test
 public void testConstructWithInitialCapacity() {
 hash = new TFloatCharHash(10);
 assertEquals(0, hash.size());
 }

 @Test
 public void testConstructWithInitialCapacityAndLoadFactor() {
 hash = new TFloatCharHash(10, 0.75f);
 assertEquals(0, hash.size());
 }

 @Test
 public void testConstructWithInitialCapacityLoadFactorAndNoEntryKeyAndNoEntryValue() {
 hash = new TFloatCharHash(10, 0.75f, 1.0f, 'X');
 assertEquals(0, hash.size());
 }

 @Test
 public void testGetNoEntryKey() {
 hash = new TFloatCharHash();
 assertEquals(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TFloatCharHash();
 assertEquals('\u0000', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TFloatCharHash(5);
 assertEquals(5, hash.getCapacity());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUpWithNegativeInitialCapacity() {
 hash = new TFloatCharHash(-5);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testContains() {
 hash.put(1.0f, 'A');
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsFalse() {
 hash.put(1.0f, 'A');
 assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testForEach() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 assertFalse("Unexpected value", hash.containsKey(floatValue));
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 int index = hash.indexOf(2.0f);
 hash.removeAt(index);
 assertEquals(2, hash.size());
 assertFalse(hash.containsKey(2.0f));
 }

 @Test
 public void testIndex() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 int index = hash.indexOf(2.0f);
 assertEquals(1, index);
 }

 @Test
 public void testIndexNegative() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 int index = hash.indexOf(4.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 float key = 4.0f;
 int index = hash.insertKey(key);
 assertEquals(4, hash.size());
 assertEquals(index, hash.indexOf(key));
 }

 @Test
 public void testInsertKeyWithExistingKey() {
 hash.put(1.0f, 'A');
 hash.put(1.0f, 'B');
 float key = 1.0f;
 int index = hash.insertKey(key);
 assertEquals(2, hash.size());
 assertEquals(index, hash.indexOf(key));
 }

 @Test
 public void testInsertKeyRehash() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 float key = 4.0f;
 int index = hash.insertKeyRehash(key, -1, -1, (byte) 0);
 assertEquals(4, hash.size());
 assertEquals(index, hash.indexOf(key));
 }

 @Test
 public void testXinsertKey() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 float key = 4.0f;
 int index = hash.XinsertKey(key);
 assertEquals(4, hash.size());
 assertEquals(index, hash.indexOf(key));
 }
}