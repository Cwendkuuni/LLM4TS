package test.gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import gnu.trove.procedure.TShortProcedure;
import gnu.trove.impl.hash.TShortHash;

public class TShortHashTest {

 private TShortHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TShortHash(3);
 }

 @Test
 public void testTShortHashConstruction() {
 TShortHash emptyHash = new TShortHash();
 TShortHash initialCapacityHash = new TShortHash(5);
 TShortHash initialCapacityAndLoadFactorHash = new TShortHash(5, 0.75f);
 TShortHash initialCapacityAndLoadFactorAndNoEntryValueHash = new TShortHash(5, 0.75f, (short) 1);

 assertEquals(0, emptyHash.size());
 assertEquals(5, initialCapacityHash.size());
 assertEquals(5, initialCapacityAndLoadFactorHash.size());
 assertEquals(5, initialCapacityAndLoadFactorAndNoEntryValueHash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hashWithNoEntryValue = new TShortHash(3, (short) 1);
 assertEquals((short) 1, hashWithNoEntryValue.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 assertEquals(3, hash.size());
 }

 @Test
 public void testContains() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertTrue(hash.containsKey((Object) (short) 1));
 assertTrue(hash.containsKey((Object) (short) 2));
 assertTrue(hash.containsKey((Object) (short) 3));
 }

 @Test
 public void testForEach() {
 short count = 0;
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short short1) {
 count++;
 return true;
 }
 };
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.forEach(procedure);
 assertEquals(3, count);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAtUnsupported() {
 hash.removeAt(-1);
 }

 @Test
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.removeAt(1);
 assertEquals(2, hash.size());
 assertFalse(hash.contains((short) 2));
 }

 @Test
 public void testIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(0, hash.index((short) 1));
 assertEquals(1, hash.index((short) 2));
 assertEquals(2, hash.index((short) 3));
 }

 @Test
 public void testIndexRehashed() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 assertEquals(-1, hash.index((short) 5));
 }

 @Test
 public void testInsertKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 assertEquals(6, hash.size());
 assertEquals(0, hash.index((short) 5));
 assertEquals(1, hash.index((short) 1));
 assertEquals(5, hash.index((short) 5));
 }

 @Test
 public void testInsertKeyRehash() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7);
 hash.put((short) 8);
 hash.put((short) 9);
 hash.put((short) 10);
 hash.put((short) 11);
 hash.put((short) 12);
 hash.put((short) 13);
 hash.put((short) 14);
 hash.put((short) 15);
 assertEquals(15, hash.size());
 }
}