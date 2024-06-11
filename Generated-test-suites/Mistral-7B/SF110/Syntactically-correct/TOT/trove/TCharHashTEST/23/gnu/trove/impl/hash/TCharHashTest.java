package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import org.junit.Test;

public class TCharHashTest {

 // Test constructor with default initial capacity and load factor
 @Test
 public void testDefaultConstructor() {
 TCharHash charHash = new TCharHash();
 char defaultValue = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
 assertEquals(0, charHash.size());
 assertNull(charHash.getNoEntryValue());
 assertArrayEquals(new char[] { defaultValue }, charHash._set);
 }

 // Test constructor with initial capacity and default load factor
 @Test
 public void testConstructorWithInitialCapacity() {
 TCharHash charHash = new TCharHash(10);
 char defaultValue = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
 assertEquals(0, charHash.size());
 assertNull(charHash.getNoEntryValue());
 assertArrayEquals(new char[10], charHash._set);
 }

 // Test constructor with initial capacity and load factor
 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TCharHash charHash = new TCharHash(10, 0.75f);
 char defaultValue = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
 assertEquals(0, charHash.size());
 assertNull(charHash.getNoEntryValue());
 assertArrayEquals(new char[14], charHash._set); // 10 * 1.5 (1.5 being 1 + loadFactor)
 }

 // Test constructor with initial capacity, load factor, and custom no_entry_value
 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndCustomNoEntryValue() {
 TCharHash charHash = new TCharHash(10, 0.75f, 'X');
 char customNoEntryValue = 'X';
 assertEquals(0, charHash.size());
 assertEquals(customNoEntryValue, charHash.getNoEntryValue());
 assertArrayEquals(new char[14], charHash._set);
 Arrays.fill(charHash._set, customNoEntryValue);
 }

 // Test getNoEntryValue method
 @Test
 public void testGetNoEntryValue() {
 TCharHash charHash = new TCharHash();
 char defaultValue = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
 charHash.setNoEntryValue('A');
 assertEquals('A', charHash.getNoEntryValue());
 }

 // Test contains method
 @Test
 public void testContains() {
 TCharHash charHash = new TCharHash();
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 }

 // Test forEach method
 @Test
 public void testForEach() {
 TCharHash charHash = new TCharHash();
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 TCharProcedure procedure = new TCharProcedure() {
 public boolean execute(char value) {
 if (value != 'A') {
 return false;
 }
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 // Test removeAt method
 @Test
 public void testRemoveAt() {
 TCharHash charHash = new TCharHash();
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = 1;
 charHash.removeAt(index);
 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('B'));
 }

 // Test index method
 @Test
 public void testIndex() {
 TCharHash charHash = new TCharHash();
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 assertEquals(1, charHash.index('B'));
 assertEquals(-1, charHash.index('D'));
 }

 // Test insertKey method
 @Test
 public void testInsertKey() {
 TCharHash charHash = new TCharHash();
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 char newChar = 'D';
 int index = charHash.insertKey(newChar);
 assertEquals(4, charHash.size());
 assertEquals(index, charHash.index(newChar));
 }
}