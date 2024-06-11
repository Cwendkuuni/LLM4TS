package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

import java.util.Arrays;

import org.junit.Test;

public class TShortHashTest {

 private TShortHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 // Test constructor with default initial capacity and load factor
 @Test
 public void testDefaultConstructor() {
 assertEquals(5, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01f);
 assertNull(hash.getNoEntryValue());
 }

 // Test constructor with specified initial capacity
 @Test
 public void testCapacityConstructor() {
 assertEquals(10, new TShortHash(10).capacity());
 }

 // Test constructor with specified initial capacity and load factor
 @Test
 public void testCapacityAndLoadFactorConstructor() {
 assertEquals(5, new TShortHash(5, 0.75f).capacity());
 assertEquals(0.75f, new TShortHash(5, 0.75f).loadFactor(), 0.01f);
 }

 // Test constructor with specified initial capacity, load factor, and no_entry_value
 @Test
 public void testCapacityLoadFactorAndNoEntryValueConstructor() {
 assertEquals(5, new TShortHash(5, 0.75f, (short) -128).capacity());
 assertEquals(0.75f, new TShortHash(5, 0.75f, (short) -128).loadFactor(), 0.01f);
 assertEquals((short) -128, new TShortHash(5, 0.75f, (short) -128).getNoEntryValue());
 }

 // Test getNoEntryValue method
 @Test
 public void testGetNoEntryValue() {
 TShortHash hashWithNoEntryValue = new TShortHash(5, 0.75f, (short) 0);
 assertEquals((short) 0, hashWithNoEntryValue.getNoEntryValue());
 }

 // Test setUp method
 @Test
 public void testSetUp() {
 assertEquals(5, hash.capacity());
 assertNull(hash.getNoEntryValue());
 }

 // Test contains method
 @Test
 public void testContains() {
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 // Test forEach method
 @Test
 public void testForEach() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals((short) 1, value);
 return true;
 }
 };
 hash.put((short) 1);
 hash.put((short) 2);
 hash.forEach(procedure);
 }

 // Test removeAt method
 @Test
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 int index = hash.indexOf((short) 1);
 hash.removeAt(index);
 assertFalse(hash.contains((short) 1));
 }

 // Test index method with key present
 @Test
 public void testIndexWithKeyPresent() {
 hash.put((short) 1);
 int index = hash.indexOf((short) 1);
 assertEquals(index, hash.indexOf((short) 1));
 }

 // Test index method with key absent
 @Test
 public void testIndexWithKeyAbsent() {
 assertEquals(-1, hash.indexOf((short) 1));
 }

 // Test indexRehashed method with key present
 @Test
 public void testIndexRehashedWithKeyPresent() {
 hash.put((short) 1);
 hash.put((short) 2);
 int index = hash.indexOf((short) 1);
 assertEquals(index, hash.indexRehashed((short) 1, index, hash.hash((short) 1), hash._states[index]));
 }

 // Test indexRehashed method with key absent
 @Test
 public void testIndexRehashedWithKeyAbsent() {
 assertEquals(-1, hash.indexRehashed((short) 1, -1, hash.hash((short) 1), (byte) 0));
 }

 // Test insertKey method with empty hash
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithEmptyHash() {
 hash.insertKey((short) 1);
 }

 // Test insertKey method with key already present
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithKeyAlreadyPresent() {
 hash.put((short) 1);
 hash.insertKey((short) 1);
 }

 // Test insertKeyRehash method with key already present
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithKeyAlreadyPresent() {
 hash.put((short) 1);
 hash.insertKeyRehash((short) 1, -1, hash.hash((short) 1), (byte) 0);
 }

 // Test insertKeyRehash method with no free or removed slots available
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithNoFreeOrRemovedSlotsAvailable() {
 hash.put((short) 1);
 hash.put((short) 2);
 for (int i = 0; i < hash.capacity(); i++) {
 hash.put((short) i);
 }
 hash.insertKeyRehash((short) 0, -1, hash.hash((short) 0), (byte) 0);
 }

 // Test insertKeyAt method with key already present
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyAtWithKeyAlreadyPresent() {
 hash.put((short) 1);
 hash.insertKeyAt(0, (short) 1);
 }

 // Test insertKeyAt method with index out of bounds
 @Test(expected = IndexOutOfBoundsException.class)
 public void testInsertKeyAtWithIndexOutOfBounds() {
 hash.put((short) 1);
 hash.insertKeyAt(hash.capacity(), (short) 1);
 }

 // Test hash method
 @Test
 public void testHash() {
 assertEquals(19163, hash.hash((short) 101));
 }

 // Test indexOf method with key present
 @Test
 public void testIndexOfWithKeyPresent() {
 hash.put((short) 1);
 int index = hash.indexOf((short) 1);
 assertEquals(index, hash.indexOf((short) 1));
 }

 // Test indexOf method with key absent
 @Test
 public void testIndexOfWithKeyAbsent() {
 assertEquals(-1, hash.indexOf((short) 1));
 }

 // Test size method
 @Test
 public void testSize() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(2, hash.size());
 }

 // Test isEmpty method
 @Test
 public void testIsEmpty() {
 assertTrue(hash.isEmpty());
 hash.put((short) 1);
 assertFalse(hash.isEmpty());
 }

 // Test clear method
 @Test
 public void testClear() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.clear();
 assertFalse(hash.containsKey((short) 1));
 assertFalse(hash.containsKey((short) 2));
 }
}