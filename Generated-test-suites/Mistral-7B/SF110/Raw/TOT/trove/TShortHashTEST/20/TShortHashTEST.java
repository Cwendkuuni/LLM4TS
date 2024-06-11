package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import gnu.trove.procedure.TShortProcedure;
import gnu.trove.impl.hash.TShortHash;

public class TShortHashTest {

 // Test constructor with default parameters
 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.getNoEntryValue());
 // Test empty hash
 assertFalse(hash.contains((short) 1));
 }

 // Test constructor with initial capacity
 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hash = new TShortHash(10);
 assertEquals(10, hash.size());
 // Test empty hash
 assertFalse(hash.contains((short) 1));
 }

 // Test constructor with initial capacity and load factor
 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.size());
 // Test empty hash
 assertFalse(hash.contains((short) 1));
 }

 // Test constructor with initial capacity, load factor and no entry value
 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 100);
 assertEquals(10, hash.size());
 assertEquals((short) 100, hash.getNoEntryValue());
 // Test empty hash
 assertFalse(hash.contains((short) 1));
 }

 // Test getNoEntryValue method
 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 100);
 assertEquals((short) 100, hash.getNoEntryValue());
 }

 // Test setUp method
 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 100);
 assertEquals(10, hash.size());
 // Test hash table is empty
 assertArrayEquals(new short[]{}, hash._set);
 byte[] states = hash._states;
 assertEquals(10, states.length);
 Arrays.fill(states, (byte) 0);
 }

 // Test contains method
 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 }

 // Test forEach method
 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals((short) value, 1);
 return true;
 }
 };
 hash.forEach(procedure);
 }

 // Test removeAt method
 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.removeAt(1);
 assertFalse(hash.contains((short) 2));
 }

 // Test index method
 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(1, hash.index((short) 2));
 assertEquals(-1, hash.index((short) 4));
 }

 // Test insertKey method
 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 assertEquals(5, hash.size());
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertTrue(hash.contains((short) 4));
 assertTrue(hash.contains((short) 5));
 }

 // Test insertKeyRehash method
 @Test
 public void testInsertKeyRehash() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7);
 assertEquals(8, hash.size());
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertTrue(hash.contains((short) 4));
 assertTrue(hash.contains((short) 5));
 assertTrue(hash.contains((short) 6));
 assertTrue(hash.contains((short) 7));
 }

 // Test insertKeyRehash with collision
 @Test
 public void testInsertKeyRehashWithCollision() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7);
 hash.put((short) 8);
 assertEquals(9, hash.size());
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertTrue(hash.contains((short) 4));
 assertTrue(hash.contains((short) 5));
 assertTrue(hash.contains((short) 6));
 assertTrue(hash.contains((short) 7));
 assertTrue(hash.contains((short) 8));
 }

 // Test insertKeyRehash when table is full
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWhenTableIsFull() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7);
 hash.put((short) 8);
 hash.put((short) 9);
 }

}