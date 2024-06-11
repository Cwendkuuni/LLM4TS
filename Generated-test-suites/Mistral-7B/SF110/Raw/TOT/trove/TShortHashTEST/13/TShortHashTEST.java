import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Test
 public void testDefaultConstructor() {
 hash = new TShortHash();
 assertEquals(0, hash.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 hash = new TShortHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 hash = new TShortHash(10, 0.75f, (short) 123);
 assertEquals(10, hash.size());
 assertEquals((short) 123, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TShortHash();
 hash.setNoEntryValue((short) 456);
 assertEquals((short) 456, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TShortHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testContains() {
 hash = new TShortHash(5);
 hash.insert(1);
 hash.insert(2);
 hash.insert(3);
 hash.insert(4);
 hash.insert(1); // duplicate
 assertTrue(hash.contains(1));
 assertTrue(hash.contains(2));
 assertTrue(hash.contains(3));
 assertTrue(hash.contains(4));
 }

 @Test
 public void testForEach() {
 hash = new TShortHash(5);
 hash.insert(1);
 hash.insert(2);
 hash.insert(3);
 hash.insert(4);
 hash.insert(1); // duplicate
 TShortProcedure procedure = new TShortProcedure() {
 public boolean execute(short value) {
 return value != 0;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 new TShortHash(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 new TShortHash(5, -0.5f);
 }

 @Test
 public void testRemoveAt() {
 hash = new TShortHash(5);
 hash.insert(1);
 hash.insert(2);
 hash.insert(3);
 hash.insert(4);
 hash.removeAt(1);
 assertEquals(3, hash.size());
 }

 @Test
 public void testIndex() {
 hash = new TShortHash(5);
 hash.insert(1);
 hash.insert(2);
 hash.insert(3);
 hash.insert(4);
 hash.insert(1); // duplicate
 assertEquals(0, hash.index(1));
 assertEquals(1, hash.index(2));
 assertEquals(2, hash.index(3));
 assertEquals(3, hash.index(4));
 assertEquals(-1, hash.index(5));
 }

 @Test
 public void testIndexRehashed() {
 hash = new TShortHash(5);
 hash.insert(1);
 hash.insert(2);
 hash.insert(3);
 hash.insert(4);
 hash.insert(1); // duplicate
 assertEquals(0, hash.indexRehashed((short) 1, 0, 1, (byte) 0));
 assertEquals(1, hash.indexRehashed((short) 2, 0, 1, (byte) 0));
 assertEquals(2, hash.indexRehashed((short) 3, 0, 1, (byte) 0));
 assertEquals(3, hash.indexRehashed((short) 4, 0, 1, (byte) 0));
 assertEquals(-1, hash.indexRehashed((short) 5, 0, 1, (byte) 0));
 }

 @Test
 public void testInsertKey() {
 hash = new TShortHash(5);
 assertTrue(hash.insertKey((short) 1) >= 0);
 }

 @Test
 public void testInsertKeyRehash() {
 hash = new TShortHash(5);
 assertTrue(hash.insertKey((short) 1) >= 0);
 hash.insertKey((short) 6);
 hash.insertKey((short) 7);
 hash.insertKey((short) 8);
 hash.insertKey((short) 9);
 hash.insertKey((short) 10);
 assertTrue(hash.insertKey((short) 11) < 0);
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 hash = new TShortHash(5);
 hash.insertKey((short) 1);
 hash.insertKey((short) 2);
 hash.insertKey((short) 3);
 hash.insertKey((short) 4);
 hash.insertKey((short) 5);
 hash.insertKey((short) 6);
 hash.insertKey((short) 7);
 hash.insertKey((short) 8);
 hash.insertKey((short) 9);
 hash.insertKey((short) 10);
 hash.insertKey((short) 11);
 hash.insertKey((short) 12);
 }

}