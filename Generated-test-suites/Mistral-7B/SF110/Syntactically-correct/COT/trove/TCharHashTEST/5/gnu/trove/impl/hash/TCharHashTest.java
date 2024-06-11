package com.example.junit4; // replace with your package name if needed

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import java.util.Arrays;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(10);
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TCharHash charHash = new TCharHash(10);
 assertEquals(10, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TCharHash charHash = new TCharHash(10, 0.75f);
 assertEquals(10, charHash.capacity());
 assertEquals(7, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 char entryValue = 'E';
 TCharHash charHash = new TCharHash(10, 0.75f, entryValue);
 assertEquals(10, charHash.capacity());
 assertEquals(7, charHash.size());
 assertEquals(entryValue, charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 char defaultNoEntryValue = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
 assertEquals(defaultNoEntryValue, charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.capacity());
 assertNotNull(charHash._set);
 }

 @Test
 public void testContains() {
 char key = 'A';
 charHash.insertKey(key);
 assertTrue(charHash.contains(key));
 }

 @Test
 public void testContainsWithNonExistentKey() {
 char nonExistentKey = 'X';
 assertFalse(charHash.contains(nonExistentKey));
 }

 @Test
 public void testForEach() {
 charHash.insertKey('A');
 charHash.insertKey('B');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char value) {
 assertEquals('A', value);
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 charHash.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 char keyToRemove = 'A';
 int index = charHash.insertKey(keyToRemove);
 charHash.removeAt(index);
 assertFalse(charHash.contains(keyToRemove));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWithNonExistentIndex() {
 int nonExistentIndex = 10;
 charHash.removeAt(nonExistentIndex);
 }

 @Test
 public void testIndex() {
 char key = 'A';
 int index = charHash.insertKey(key);
 assertEquals(index, charHash.index(key));
 }

 @Test
 public void testIndexWithNonExistentKey() {
 char nonExistentKey = 'X';
 assertEquals(-1, charHash.index(nonExistentKey));
 }

 @Test
 public void testIndexRehashed() {
 char key = 'X';
 charHash.insertKey(key);
 int index = charHash.index(key);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 char key = 'C';
 int index = charHash.insertKey(key);
 assertEquals(1, charHash.size());
 assertEquals(key, charHash._set[0]);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHashTable() {
 charHash = new TCharHash(3);
 for (char key = 'A'; key <= 'Z'; key++) {
 charHash.insertKey(key);
 }
 }

 @Test
 public void testInsertKeyRehash() {
 char key = 'X';
 int index = charHash.insertKey(key);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyWithExistingKey() {
 char existingKey = 'A';
 charHash.insertKey(existingKey);
 int index = charHash.insertKey(existingKey);
 assertEquals(-index - 1, index);
 }

 @Test
 public void testInsertKeyAt() {
 char key = 'D';
 int index = charHash.insertKey(key);
 charHash.insertKeyAt(index, 'E');
 assertEquals('E', charHash._set[index]);
 }
}