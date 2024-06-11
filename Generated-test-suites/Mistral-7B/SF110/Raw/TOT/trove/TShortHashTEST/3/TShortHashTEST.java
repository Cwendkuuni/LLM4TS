package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import gnu.trove.TShortHash;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 // Test constructor with default parameters
 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.size());
 }

 // Test constructor with initial capacity and load factor
 @Test
 public void testInitialCapacityAndLoadFactor() {
 TShortHash hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01);
 }

 // Test constructor with initial capacity, load factor, and no entry value
 @Test
 public void testInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 127);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01);
 assertEquals((short) 127, hash.getNoEntryValue());
 }

 // Test getNoEntryValue method
 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 hash.setNoEntryValue((short) 42);
 assertEquals((short) 42, hash.getNoEntryValue());
 }

 // Test setUp method
 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.capacity());
 assertNull(hash._set);
 }

 // Test contains method
 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.insert(1);
 hash.insert(2);
 hash.insert(1); // duplicate key

 assertTrue(hash.contains(1));
 assertTrue(hash.contains(2));
 assertFalse(hash.contains(3));
 }

 // Test forEach method with empty hash
 @Test
 public void testForEachEmptyHash() {
 TShortHash hash = new TShortHash();
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 return false;
 }
 };

 hash.forEach(procedure);
 assertEquals(0, hash.size());
 }

 // Test forEach method with non-empty hash
 @Test
 public void testForEachNonEmptyHash() {
 TShortHash hash = new TShortHash(5);
 hash.insert(1);
 hash.insert(2);
 hash.insert(3);

 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertTrue(value >= 1 && value <= 3);
 return true;
 }
 };

 hash.forEach(procedure);
 assertEquals(3, hash.size());
 }

 // Test removeAt method with empty hash
 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtEmptyHash() {
 TShortHash hash = new TShortHash();
 hash.removeAt(0);
 }

 // Test removeAt method with non-empty hash
 @Test
 public void testRemoveAtNonEmptyHash() {
 TShortHash hash = new TShortHash(5);
 hash.insert(1);
 hash.insert(2);
 hash.insert(3);

 int index = 1;
 hash.removeAt(index);

 assertEquals(2, hash.size());
 assertNotEquals(1, hash.index(2));
 }

 // Test index method with empty hash
 @Test
 public void testIndexEmptyHash() {
 TShortHash hash = new TShortHash();
 assertFalse(hash.containsKey((short) 1));
 }

 // Test index method with non-empty hash
 @Test
 public void testIndexNonEmptyHash() {
 TShortHash hash = new TShortHash(5);
 hash.insert(1);
 hash.insert(2);
 hash.insert(1); // duplicate key

 assertEquals(0, hash.index(1));
 assertEquals(-1, hash.index((short) 3));
 }

 // Test insertKey method
 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash(5);
 hash.insertKey((short) 1);
 hash.insertKey((short) 2);
 hash.insertKey((short) 1); // duplicate key

 assertEquals(2, hash.size());
 assertEquals(0, hash.index(1));
 }

 // Test insertKeyRehash method
 @Test
 public void testInsertKeyRehash() {
 TShortHash hash = new TShortHash(3);
 hash.insertKey((short) 1);
 hash.insertKey((short) 2);
 hash.insertKey((short) 3);
 hash.insertKey((short) 4); // hash collision

 assertEquals(4, hash.size());
 assertEquals(-1, hash.index((short) 5));
 }

 // Test insertKeyAt method
 @Test
 public void testInsertKeyAt() {
 TShortHash hash = new TShortHash(5);
 hash.insertKeyAt(0, (short) 1);
 hash.insertKeyAt(1, (short) 2);
 hash.insertKeyAt(2, (short) 1); // duplicate key

 assertEquals(3, hash.size());
 assertEquals(0, hash.index(1));
 }
}