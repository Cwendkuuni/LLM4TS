package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

import java.util.Arrays;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(11);
 }

 // Test constructor with default arguments
 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 // Test constructor with initialCapacity
 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hash = new TShortHash(11);
 assertEquals(11, hash.size());
 }

 // Test constructor with initialCapacity and loadFactor
 @Test
 public void testInitialCapacityLoadFactorConstructor() {
 TShortHash hash = new TShortHash(11, 0.75f);
 assertEquals(8, hash.size());
 }

 // Test constructor with initialCapacity, loadFactor, and noEntryValue
 @Test
 public void testInitialCapacityLoadFactorNoEntryValueConstructor() {
 TShortHash hash = new TShortHash(11, 0.75f, (short) 123);
 assertEquals(11, hash.size());
 assertEquals((short) 123, hash.getNoEntryValue());
 }

 // Test getNoEntryValue
 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 // Test setUp
 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(11);
 assertEquals(11, hash.size());
 }

 // Test contains
 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertTrue(hash.contains((short) 4));
 assertTrue(hash.contains((short) 5));
 assertFalse(hash.contains((short) 6));
 }

 // Test forEach
 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short short1) {
 assertEquals((short) 1, short1);
 return true;
 }
 };
 hash.forEach(procedure);
 }

 // Test removeAt
 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.removeAt(2);
 assertEquals(4, hash.size());
 }

 // Test index
 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 assertEquals(2, hash.index((short) 3));
 assertEquals(-1, hash.index((short) 6));
 }

 // Test insertKey
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithoutSpace() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 assertThrows(IllegalStateException.class, () -> hash.insertKey((short) 6));
 }

 // Test insertKeyAt
 @Test
 public void testInsertKeyAt() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.insertKeyAt(2, (short) 10);
 assertEquals(6, hash.size());
 assertEquals((short) 10, hash.get(2));
 }

 // Test indexRehashed
 @Test
 public void testIndexRehashed() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 assertEquals(4, hash.index((short) 5));
 }

 // Test insertKeyRehash
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithoutSpace() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7);
 assertThrows(IllegalStateException.class, () -> hash.insertKey((short) 7));
 }
}