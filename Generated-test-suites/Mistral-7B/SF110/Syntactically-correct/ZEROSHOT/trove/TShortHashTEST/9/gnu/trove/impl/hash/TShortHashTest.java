import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash hash = new TShortHash(11, 0.75f);
 assertEquals(8, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(11, 0.75f, (short) 123);
 assertEquals(8, hash.size());
 assertEquals(123, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TShortHash hash = new TShortHash(-5);
 }

 @Test
 public void testContains() {
 short key = 1;
 hash.put(key, key);
 assertTrue(hash.contains(key));
 }

 @Test
 public void testDoesNotContain() {
 assertFalse(hash.contains((short) 2));
 }

 @Test
 public void testForEachProcedure() {
 short key = 1;
 hash.put(key, key);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short short1) {
 if (short1 == key) {
 return false;
 }
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtIndexOutOfBounds() {
 hash.removeAt(-1);
 }

 @Test
 public void testRemoveAtIndexInBounds() {
 short key = 1;
 hash.put(key, key);
 int index = hash.indexOf(key);
 hash.removeAt(index);
 assertNull(hash.get(key));
 }

 @Test
 public void testIndex() {
 short key = 1;
 hash.put(key, key);
 int index = hash.indexOf(key);
 assertEquals(index, hash.index(key));
 }

 @Test
 public void testLongHashFunction() {
 short key = 1;
 int hash = TShortHash.HashFunctions.hash(key);
 short key2 = 2;
 int hash2 = TShortHash.HashFunctions.hash(key2);
 assertEquals(Math.abs(hash ^ hash2), Math.abs(hash - hash2));
 }

 @Test
 public void testInsertKey() {
 short key = 1;
 int index = hash.insertKey(key);
 assertEquals(key, hash.get(index));
 }

 @Test
 public void testInsertKeyRehash() {
 short key = 1;
 hash.put(key, key);
 int index = hash.indexOf(key);
 hash.removeAt(index);
 short key2 = 2;
 int index2 = hash.insertKey(key2);
 assertEquals(key2, hash.get(index2));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 for (short i = 0; i < hash.size(); i++) {
 hash.put(i, i);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeSlotAvailable() {
 for (short i = 0; i < hash.size(); i++) {
 hash.put(i, i);
 }
 hash.put((short) 128, (short) 128);
 }

 @Test
 public void testNoEntryValue() {
 TShortHash hash = new TShortHash(5, 0);
 assertEquals(0, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash(5);
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }
}