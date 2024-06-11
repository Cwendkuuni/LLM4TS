package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Test
 public void testTFloatCharHashConstruction() {
 hash = new TFloatCharHash();
 assertNotNull(hash);
 }

 @Test
 public void testTFloatCharHashInitialCapacity() {
 hash = new TFloatCharHash(10);
 assertNotNull(hash);
 assertEquals(10, hash.size());
 }

 @Test
 public void testTFloatCharHashInitialCapacityAndLoadFactor() {
 hash = new TFloatCharHash(10, 0.75f);
 assertNotNull(hash);
 assertEquals(10, hash.size());
 }

 @Test
 public void testTFloatCharHashInitialCapacityLoadFactorAndKeyValue() {
 hash = new TFloatCharHash(10, 0.75f, 1.0f, 'A');
 assertNotNull(hash);
 assertEquals(10, hash.size());
 assertEquals(1.0f, hash.getNoEntryKey());
 assertEquals('A', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TFloatCharHash(5);
 int capacity = hash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsNonExistentValue() {
 hash = new TFloatCharHash(5);
 assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testForEach() {
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return key > 1.0f;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 int index = hash.index(1.0f);
 hash.removeAt(index);
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testIndex() {
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(1.0f);
 int index = hash.index(1.0f);
 assertEquals(index, hash.indexOf(1.0f));
 }

 @Test
 public void testIndexDoesNotExist() {
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 int index = hash.index(3.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 hash = new TFloatCharHash(5);
 int index = hash.insertKey(1.0f);
 assertEquals(1, index);
 }

 @Test
 public void testInsertKeyWithExistingValue() {
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(1.0f);
 int index = hash.insertKey(1.0f);
 assertEquals(-2, index);
 }

 @Test
 public void testInsertKeyRehash() {
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(1.0f);
 hash.insertKey(1.0f);
 hash.insertKey(1.0f);
 hash.insertKey(1.0f);
 int index = hash.insertKey(1.0f);
 assertEquals(-6, index);
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(output);
 hash.writeExternal(out);
 TFloatCharHash hash2 = new TFloatCharHash();
 hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(output.toByteArray())));
 assertTrue(hash2.contains(1.0f));
 assertTrue(hash2.contains(2.0f));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatCharHash hash = new TFloatCharHash();
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(output);
 hash.writeExternal(out);
 ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
 ObjectInput in = new ObjectInput(input);
 TFloatCharHash hash2 = new TFloatCharHash();
 hash2.readExternal(in);
 assertEquals(hash.getNoEntryKey(), hash2.getNoEntryKey());
 assertEquals(hash.getNoEntryValue(), hash2.getNoEntryValue());
 }

}