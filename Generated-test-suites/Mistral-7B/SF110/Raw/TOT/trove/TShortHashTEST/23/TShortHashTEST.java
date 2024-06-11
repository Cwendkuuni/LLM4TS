package gnu.trove.impl.hash.test;

import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hash = new TShortHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 42);
 assertEquals(10, hash.size());
 assertEquals(42, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 42);
 assertEquals(42, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 42);
 assertEquals(10, hash.size());
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.insert((short) 5);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertTrue(hash.contains((short) 4));
 assertTrue(hash.contains((short) 5));
 }

 @Test
 public void testContainsWithEmptyHash() {
 TShortHash hash = new TShortHash();
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testContainsWithNullValue() {
 TShortHash hash = new TShortHash(5);
 assertFalse(hash.contains((short) null));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.insert((short) 5);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals((short) value, value);
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testForEachWithEmptyHash() {
 TShortHash hash = new TShortHash();
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.insert((short) 5);
 hash.removeAt(2);
 assertEquals(4, hash.size());
 assertFalse(hash.contains((short) 3));
 }

 @Test
 public void testRemoveAtWithEmptyHash() {
 TShortHash hash = new TShortHash();
 hash.removeAt(0);
 assertEquals(0, hash.size());
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.insert((short) 5);
 assertEquals(4, hash.index((short) 5));
 assertEquals(-1, hash.index((short) 6));
 }

 @Test
 public void testIndexWithEmptyHash() {
 TShortHash hash = new TShortHash();
 assertEquals(-1, hash.index((short) 1));
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 1);
 assertEquals(2, hash.size());
 }

 @Test
 public void testInsertKeyWithNoFreeSlot() {
 TShortHash hash = new TShortHash(3);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 try {
 hash.insert((short) 4);
 fail("Should have thrown an IllegalStateException");
 } catch (IllegalStateException e) {
 //expected
 }
 }

 @Test
 public void testInsertKeyRehash() {
 TShortHash hash = new TShortHash(3);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 assertEquals(4, hash.size());
 }

 @Test
 public void testInsertKeyRehashWithNoFreeOrRemovedSlot() {
 TShortHash hash = new TShortHash(3);
 hash.insert((short) 1);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 try {
 hash.insert((short) 5);
 fail("Should have thrown an IllegalStateException");
 } catch (IllegalStateException e) {
 //expected
 }
 }
}