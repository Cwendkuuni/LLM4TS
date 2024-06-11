package com.example.junit; // Replace this package with the actual package name

import static org.junit.Assert.*;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TFloatCharHashTest {

 private TFloatCharHash<Float, Character> hash;

 @Test
 public void testConstructor() {
 hash = new TFloatCharHash<>();
 assertNotNull(hash);
 }

 @Test
 public void testConstructorsWithInitialCapacity() {
 hash = new TFloatCharHash(10);
 assertNotNull(hash);
 assertEquals(10, hash.size());
 }

 @Test
 public void testConstructorsWithInitialCapacityAndLoadFactor() {
 hash = new TFloatCharHash(10, 0.75f);
 assertNotNull(hash);
 assertEquals(10, hash.size());
 }

 @Test
 public void testConstructorsWithInitialCapacityLoadFactorAndNoEntryKeyNoEntryValue() {
 hash = new TFloatCharHash(10, 0.75f, 1.0f, 'A');
 assertNotNull(hash);
 assertEquals(10, hash.size());
 assertEquals(1.0f, hash.getNoEntryKey());
 assertEquals('A', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TFloatCharHash<>(10);
 assertEquals(10, hash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUpNegativeInitialCapacity() {
 hash = new TFloatCharHash(-5);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testContains() {
 hash.put(1.0f, 'A');
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsNonExistentKey() {
 hash.put(1.0f, 'A');
 assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testForEach() {
 final int size = 10;
 hash = new TFloatCharHash<>(size);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(Float key) {
 return hash.contains(key);
 }
 };
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 hash.forEach(procedure);
 assertEquals(true, procedure.isSatisfied());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachConcurrentModification() {
 final int size = 10;
 hash = new TFloatCharHash<>(size);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(Float key) {
 hash.remove(key);
 return true;
 }
 };
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.forEach(procedure);
 fail("Expected UnsupportedOperationException");
 }

 @Test
 public void testRemoveAt() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 int index = hash.indexOfKey(1.0f);
 hash.removeAt(index);
 assertFalse(hash.containsKey(1.0f));
 }

 @Test
 public void testIndex() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 int index = hash.indexOfKey(2.0f);
 assertEquals(2, index);
 }

 @Test
 public void testIndexNonExistentKey() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 int index = hash.indexOfKey(4.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexRehashed() {
 final int size = 16;
 hash = new TFloatCharHash<>(size);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 hash.put(4.0f, 'D');
 hash.put(5.0f, 'E');
 hash.put(6.0f, 'F');
 hash.put(7.0f, 'G');
 hash.put(8.0f, 'H');
 hash.put(9.0f, 'I');
 hash.put(10.0f, 'J');
 hash.put(11.0f, 'K');
 hash.put(12.0f, 'L');
 hash.put(13.0f, 'M');
 hash.put(14.0f, 'N');
 hash.put(15.0f, 'O');
 hash.put(16.0f, 'P');

 float key = 9.5f;
 int index = hash.indexOfKey(key);
 assertFalse(hash.containsKey(key));
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 hash = new TFloatCharHash<>(10);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 hash.put(4.0f, 'D');
 hash.put(5.0f, 'E');
 int index = hash.insertKey(6.0f);
 assertEquals(6, index);
 assertFalse(hash.containsKey(5.5f));
 }

 @Test
 public void testInsertKeyRehash() {
 final int size = 10;
 hash = new TFloatCharHash<>(size);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 hash.put(4.0f, 'D');
 hash.put(5.0f, 'E');
 hash.put(6.0f, 'F');
 hash.put(7.0f, 'G');
 hash.put(8.0f, 'H');
 hash.put(9.0f, 'I');
 hash.put(10.0f, 'J');

 float key = 11.5f;
 int index = hash.insertKey(key);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyAt() {
 hash = new TFloatCharHash<>(10);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 hash.put(4.0f, 'D');
 hash.put(5.0f, 'E');
 hash.insertKeyAt(6.0f, 6);
 assertFalse(hash.containsKey(5.5f));
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(outputStream);
 hash.writeExternal(out);
 byte[] output = outputStream.toByteArray();
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
 ObjectInput in = new ObjectInputStream(inputStream);
 hash.readExternal(in);
 fail("Expected an error as input stream is empty.");
 }

 @Test
 public void testReadExternalNormalOperation() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(outputStream);
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(10);
 hash.writeExternal(out);
 byte[] input = outputStream.toByteArray();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
 ObjectInput in = new ObjectInputStream(inputStream);
 TFloatCharHash<Float, Character> hashRead = new TFloatCharHash<>(10);
 hashRead.readExternal(in);
 assertEquals(hash, hashRead);
 }
}