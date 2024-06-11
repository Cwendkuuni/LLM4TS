package com.example.junit4; // Replace with your package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import org.junit.Test;

public class TCharHashTest {

 private TCharHash charHash;

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setupBeforeClass() {
 charHash = new TCharHash(11);
 }

 @Test
 public void testConstructor_NoArguments() {
 charHash = new TCharHash();
 assertEquals(0, charHash.size());
 }

 @Test
 public void testConstructor_InitialCapacity() {
 charHash = new TCharHash(10);
 assertEquals(0, charHash.size());
 }

 @Test
 public void testConstructor_InitialCapacityAndLoadFactor() {
 charHash = new TCharHash(10, 0.75f);
 assertEquals(0, charHash.size());
 }

 @Test
 public void testConstructor_InitialCapacityAndLoadFactorAndNoEntryValue() {
 charHash = new TCharHash(10, 0.75f, 'X');
 assertEquals(0, charHash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 char defaultNoEntryValue = TCharHash.class.getDeclaredField("no_entry_value").getChar(null);
 charHash.no_entry_value = defaultNoEntryValue;
 assertEquals(defaultNoEntryValue, charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp_InitialCapacity() {
 int capacity = charHash.setUp(10);
 assertEquals(10, capacity);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContains_NullValue() {
 assertFalse(charHash.contains(null));
 }

 @Test
 public void testContains_ValueInHash() {
 charHash.put('A', 'A');
 assertTrue(charHash.contains('A'));
 }

 @Test
 public void testContains_ValueNotInHash() {
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testForEach_EmptyHash() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charToTest) {
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testForEach_OneElementHash() {
 charHash.put('A', 'A');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charToTest) {
 if (charToTest != 'A') {
 fail("Unexpected character in hash: " + charToTest);
 }
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testForEach_MultipleElementsHash() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charToTest) {
 if (charToTest != 'A' && charToTest != 'B') {
 fail("Unexpected character in hash: " + charToTest);
 }
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt_IndexOutOfBounds() {
 try {
 charHash.removeAt(-1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testRemoveAt_IndexInBounds() {
 charHash.put('A', 'A');
 int index = charHash.index('A');
 charHash.removeAt(index);
 assertEquals(0, charHash.size());
 }

 @Test
 public void testIndex_NullValue() {
 assertEquals(-1, charHash.index(null));
 }

 @Test
 public void testIndex_ValueInHash() {
 charHash.put('A', 'A');
 int index = charHash.index('A');
 assertEquals(index, charHash.indexOf('A'));
 }

 @Test
 public void testIndex_ValueNotInHash() {
 assertEquals(-1, charHash.index('B'));
 }

 @Test
 public void testIndexRehashed_ValueInHash() {
 charHash.put('A', 'A');
 assertEquals(0, charHash.indexRehashed('A', 0, 0, (byte) 1));
 }

 @Test
 public void testIndexRehashed_ValueNotInHash() {
 assertEquals(-1, charHash.indexRehashed('B', 0, 0, (byte) 1));
 }

 @Test
 public void testInsertKey_ValueAlreadyInHash() {
 charHash.put('A', 'A');
 int index = charHash.index('A');
 assertFalse(charHash.insertKey('A'));
 assertEquals(charHash.get(index), 'A');
 }

 @Test
 public void testInsertKey_ValueNotInHash() {
 charHash.put('A', 'A');
 assertFalse(charHash.contains('B'));
 assertTrue(charHash.insertKey('B'));
 assertEquals(1, charHash.size());
 }

 @Test
 public void testInsertKey_HashFull() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 try {
 charHash.insertKey('D');
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 assertEquals(3, charHash.size());
 }

 @Test
 public void testInsertKeyRehash_ValueAlreadyInHash() {
 charHash.put('A', 'A');
 int index = charHash.index('A');
 assertFalse(charHash.insertKey('A'));
 assertEquals(charHash.get(index), 'A');
 }

 @Test
 public void testInsertKeyRehash_ValueNotInHash() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 try {
 charHash.insertKey('D');
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 charHash.insertKey('D');
 assertEquals(4, charHash.size());
 }
}