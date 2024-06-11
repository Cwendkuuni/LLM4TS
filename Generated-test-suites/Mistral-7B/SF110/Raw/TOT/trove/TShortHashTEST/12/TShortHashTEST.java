package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.TShortHash;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

import org.junit.Test;

public class TShortHashTest {

 @Test
 public void testEmptyHash() {
 TShortHash hash = new TShortHash(0);
 assertEquals(0, hash.size());
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testBasicFunctionality() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(3, hash.size());
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 }

 @Test
 public void testLoadFactor() {
 TShortHash hash = new TShortHash(5, 0.75f);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 assertEquals(7, hash.size());
 }

 @Test
 public void testCustomNoEntryValue() {
 TShortHash hash = new TShortHash(5, 0, (short) -1);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 assertEquals(-1, hash.getNoEntryValue());
 assertEquals(6, hash.size());
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertTrue(hash.contains((short) 4));
 assertTrue(hash.contains((short) 5));
 assertTrue(hash.contains((short) 6));
 }

 @Test
 public void testRemove() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.remove((short) 3);
 assertEquals(4, hash.size());
 assertFalse(hash.contains((short) 3));
 }

 @Test
 public void testRehash() {
 TShortHash hash = new TShortHash(5);
 for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
 hash.put(i);
 }
 // Check size is correct
 assertEquals(Short.MAX_VALUE, hash.size());
 // Test contains
 for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
 assertTrue(hash.contains(i));
 }
 // Test forEach
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short k) {
 return k > 0;
 }
 };
 hash.forEach(procedure);
 int count = 0;
 for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
 if (i > 0) {
 count++;
 }
 }
 assertTrue(count == hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 TShortHash hash = new TShortHash(5);
 for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
 hash.put(i);
 }
 hash.put((short) 0); // This should throw an exception
 }
}