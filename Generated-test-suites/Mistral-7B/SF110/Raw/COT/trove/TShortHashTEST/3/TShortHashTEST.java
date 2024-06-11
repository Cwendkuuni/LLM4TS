package com.example.junitTests;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @BeforeClass
 public static void setupBeforeClass() {
 // Initialize the test object here, if required
 // For example, if all tests need an instance of TShortHash with specific parameters
 // hash = new TShortHash(initialCapacity, loadFactor, no_entry_value);
 }

 @Before
 public void setUp() {
 // Initialize the test object for each test method with specific parameters
 // For example, if each test needs an instance of TShortHash with different parameters
 // hash = new TShortHash(initialCapacity, loadFactor, no_entry_value);
 }

 @Test
 public void testConstructorWithDefaultParameters() {
 hash = new TShortHash();
 assertEquals(0, hash.getNoEntryValue());
 // Perform additional checks as needed
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 hash = new TShortHash(10);
 assertEquals(10, hash.capacity());
 // Perform additional checks as needed
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01f);
 // Perform additional checks as needed
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 hash = new TShortHash(10, 0.75f, 2);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01f);
 assertEquals(2, hash.getNoEntryValue());
 // Perform additional checks as needed
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TShortHash();
 assertEquals(0, hash.getNoEntryValue());
 hash = new TShortHash(0, 1, (short) 1);
 assertEquals((short) 1, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TShortHash(5);
 assertEquals(5, hash.capacity());
 // Perform additional checks as needed
 }

 @Test
 public void testContains() {
 hash = new TShortHash();
 hash.insert((short) 1);
 assertTrue(hash.contains((short) 1));
 hash.insert((short) 2);
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testForEach() {
 hash = new TShortHash();
 hash.insert((short) 1);
 hash.insert((short) 2);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short val) {
 return val != (short) 2;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAtWithEmptyHash() {
 hash = new TShortHash();
 try {
 hash.removeAt(0);
 } catch (IndexOutOfBoundsException e) {
 fail("Expected UnsupportedOperationException");
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWithInvalidIndex() {
 hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 try {
 hash.removeAt(5);
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testIndex() {
 hash = new TShortHash();
 hash.insert((short) 1);
 hash.insert((short) 2);
 assertEquals(-1, hash.index((short) 3));
 assertEquals(0, hash.index((short) 1));
 assertEquals(1, hash.index((short) 2));
 }

 @Test
 public void testIndexRehashed() {
 hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.insert((short) 5);
 short valToFind = (short) 3;
 int index = hash.index(valToFind);
 if (index == -1) {
 int hashCode = HashFunctions.hash(valToFind);
 int indexToFind = hashCode % hash.capacity();
 int probe = 1 + hashCode % (hash.capacity() - 2);
 int loopIndex = indexToFind;
 do {
 indexToFind -= probe;
 if (indexToFind < 0) {
 indexToFind += hash.capacity();
 }
 assertEquals(valToFind, hash._set[indexToFind]);
 index = indexToFind;
 } while (index != loopIndex);
 } else {
 assertEquals(valToFind, hash._set[index]);
 }
 }

 @Test
 public void testInsertKey() {
 hash = new TShortHash(5);
 assertFalse(hash.contains((short) 1));
 hash.insert((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testInsertKeyRehash() {
 hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.insert((short) 5);
 short valToInsert = (short) 6;
 int index = hash.insertKey(valToInsert);
 int expectedIndex = hashCode(valToInsert) % hash.capacity();
 int probe = 1 + hashCode(valToInsert) % (hash.capacity() - 2);
 int loopIndex = expectedIndex;
 do {
 expectedIndex -= probe;
 if (expectedIndex < 0) {
 expectedIndex += hash.capacity();
 }
 assertEquals(valToInsert, hash._set[expectedIndex]);
 index = expectedIndex;
 } while (index != loopIndex);
 assertEquals(index, hash.indexOf((short) valToInsert));
 }

 @Test
 public void testInsertKeyWithFullHash() {
 hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.insert((short) 5);
 short valToInsert = (short) 6;
 try {
 hash.insertKey(valToInsert);
 fail();
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 private int hashCode(short value) {
 return HashFunctions.hash(value) & Integer.MAX_VALUE;
 }

}