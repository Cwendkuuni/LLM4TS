package com.example.junit; // replace with your package name

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float> hashMap;

 @Test
 public void testConstructor() {
 hashMap = new TFloatDoubleHash<>();
 assertTrue(hashMap instanceof TFloatDoubleHash);
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 hashMap = new TFloatDoubleHash<>(10);
 assertTrue(hashMap instanceof TFloatDoubleHash);
 assertEquals(10, hashMap.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 hashMap = new TFloatDoubleHash<>(10, 0.75f);
 assertTrue(hashMap instanceof TFloatDoubleHash);
 assertEquals(10, hashMap.capacity());
 assertEquals(8, hashMap.size()); // initial capacity minus one for no entry key and value
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndKeys() {
 hashMap = new TFloatDoubleHash<>(10, 0.75f, 1.0f, 0.0);
 assertTrue(hashMap instanceof TFloatDoubleHash);
 assertEquals(10, hashMap.capacity());
 assertEquals(8, hashMap.size());
 assertEquals(1.0f, hashMap.getNoEntryKey(), 0.0);
 assertEquals(0.0, hashMap.getNoEntryValue(), 0.0);
 }

 @Test
 public void testSetUp() {
 hashMap = new TFloatDoubleHash<>(5);
 assertEquals(5, hashMap.capacity());
 }

 @Test
 public void testContains() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 assertTrue(hashMap.contains(1.0f));
 }

 @Test
 public void testContainsWithEmptyHashmap() {
 hashMap = new TFloatDoubleHash<>(5);
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testForEach() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 1.0f);
 return true;
 }
 };
 hashMap.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.removeAt(0);
 assertEquals(1, hashMap.size());
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testIndex() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 int index = hashMap.index(1.0f);
 assertEquals(0, index);
 }

 @Test
 public void testIndexWithEmptyHashmap() {
 hashMap = new TFloatDoubleHash<>(5);
 int index = hashMap.index(1.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexRehashed() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 int index = hashMap.index(5.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 hashMap = new TFloatDoubleHash<>(5);
 int index = hashMap.insertKey(1.0f);
 assertEquals(1, hashMap.size());
 assertEquals(0, index);
 }

 @Test
 public void testInsertKeyWithEmptyHashmap() {
 hashMap = new TFloatDoubleHash<>(5);
 int index = hashMap.insertKey(1.0f);
 assertEquals(1, hashMap.size());
 assertEquals(0, index);
 }

 @Test
 public void testInsertKeyRehash() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.insertKey(5.0f);
 int index = hashMap.insertKey(6.0f);
 assertEquals(6, hashMap.size()); // the size should be 6 after the insertion
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatDoubleHash<Float> hashMapClone = new TFloatDoubleHash<>(5);
 hashMapClone.insertKey(1.0f);
 hashMapClone.insertKey(2.0f);
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 hashMapClone.writeExternal(objectOutput);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 TFloatDoubleHash<Float> deserializedHashMap = new TFloatDoubleHash<>(5);
 deserializedHashMap.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(1.0f, deserializedHashMap.get(1.0f), 0.0);
 assertEquals(2.0f, deserializedHashMap.get(2.0f), 0.0);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatDoubleHash<Float> hashMapClone = new TFloatDoubleHash<>(5);
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 hashMapClone.writeExternal(objectOutput);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 TFloatDoubleHash<Float> deserializedHashMap = new TFloatDoubleHash<>(5);
 deserializedHashMap.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(1.0f, deserializedHashMap.getNoEntryKey(), 0.0);
 assertEquals(0.0, deserializedHashMap.getNoEntryValue(), 0.0);
 }

}