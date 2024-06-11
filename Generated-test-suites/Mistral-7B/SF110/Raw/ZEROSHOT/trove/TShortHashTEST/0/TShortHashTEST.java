package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

import org.junit.Test;

public class TShortHashTest {

 private TShortHash hash;

 @Test
 public void testTShortHashConstruction() {
 hash = new TShortHash();
 assertEquals(0, hash.getNoEntryValue());
 }

 @Test
 public void testTShortHashWithInitialCapacity() {
 hash = new TShortHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testTShortHashWithInitialCapacityAndLoadFactor() {
 hash = new TShortHash(10, 0.75f);
 assertEquals(7, (int) Math.ceil(10 * 0.75f));
 }

 @Test
 public void testTShortHashWithInitialCapacityLoadFactorAndNoEntryValue() {
 hash = new TShortHash(10, 0.75f, (short) 123);
 assertEquals(10, hash.size());
 assertEquals(123, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TShortHash(10);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 assertEquals(0, hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 hash = new TShortHash(10);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 assertTrue(hash.contains((short) 1));
 assertFalse(hash.contains((short) 3));
 }

 @Test
 public void testForEach() {
 hash = new TShortHash(10);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);

 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 if (value == 2) {
 fail("Unexpected value in forEach test.");
 }
 return true;
 }
 };

 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 hash = new TShortHash(10);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);

 hash.removeAt(1);
 assertEquals(2, hash.size());
 assertFalse(hash.contains((short) 2));
 }

 @Test
 public void testIndex() {
 hash = new TShortHash(10);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);

 assertEquals(1, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 4));
 }

 @Test
 public void testInsertKey() {
 hash = new TShortHash(10);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);

 assertEquals(-3, hash.insertKey((short) 3));
 assertEquals(4, hash.size());
 assertTrue(hash.contains((short) 3));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 hash = new TShortHash(10);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 4, (short) 4);
 hash.put((short) 5, (short) 5);
 hash.put((short) 6, (short) 6);
 hash.put((short) 7, (short) 7);
 hash.put((short) 8, (short) 8);
 hash.put((short) 9, (short) 9);
 hash.put((short) 10, (short) 10);
 }

}