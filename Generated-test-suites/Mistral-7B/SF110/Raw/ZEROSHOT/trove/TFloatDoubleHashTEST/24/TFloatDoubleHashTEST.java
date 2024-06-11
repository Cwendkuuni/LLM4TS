package test;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;
import static org.junit.Assert.*;

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
 assertEquals(0.75f, hash.loadFactor(), 0.01f);
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyAndNoEntryValue() {
 hash = new TFloatDoubleHash(10, 0.75f, 0.5f, 1.23d);
 assertEquals(0, hash.size());
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01f);
 assertEquals(0.5f, hash.noEntryKey(), 0.01f);
 assertEquals(1.23d, hash.noEntryValue(), 0.01);
 }

 @Test
 public void testSetUp() {
 hash = new TFloatDoubleHash(10);
 assertEquals(10, hash.capacity());
 assertNotNull(hash._set);
 }

 @Test
 public void testContains() {
 hash = new TFloatDoubleHash(3);
 hash.put(1.2f);
 assertTrue(hash.contains(1.2f));
 assertFalse(hash.contains(2.3f));
 }

 @Test
 public void testForEach() {
 hash = new TFloatDoubleHash(3);
 hash.put(1.2f);
 hash.put(2.3f);
 hash.put(3.4f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float element) {
 assertEquals(element, hash.get(element), 0.01f);
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 hash = new TFloatDoubleHash(3);
 hash.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 hash = new TFloatDoubleHash(3);
 hash.put(1.2f);
 hash.put(2.3f);
 hash.put(3.4f);
 hash.removeAt(1);
 assertEquals(2, hash.size());
 assertFalse(hash.contains(1.2f));
 }

 @Test
 public void testIndex() {
 hash = new TFloatDoubleHash(3);
 hash.put(1.2f);
 hash.put(2.3f);
 hash.put(3.4f);
 assertEquals(2.3f, hash.get(2.3f), 0.01f);
 assertEquals(-1, hash.index(-1.0f));
 }

 @Test
 public void testIndexRehashed() {
 hash = new TFloatDoubleHash(3);
 hash.put(1.2f);
 hash.put(2.3f);
 hash.put(3.4f);
 hash.put(4.5f);
 assertEquals(4.5f, hash.get(4.5f), 0.01f);
 assertEquals(-1, hash.index(5.0f));
 }

 @Test
 public void testInsertKey() {
 hash = new TFloatDoubleHash(3);
 hash.put(1.2f);
 hash.put(2.3f);
 hash.put(3.4f);
 assertEquals(3, hash.size());
 hash.put(4.5f);
 assertEquals(4, hash.size());
 }

 @Test
 public void testInsertKeyRehash() {
 hash = new TFloatDoubleHash(3);
 hash.put(1.2f);
 hash.put(2.3f);
 hash.put(3.4f);
 assertEquals(3, hash.size());
 hash.put(4.5f);
 assertEquals(4, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 hash = new TFloatDoubleHash(3);
 hash.put(1.2f);
 hash.put(2.3f);
 hash.put(3.4f);
 hash.put(4.5f);
 hash.put(5.6f);
 }
}