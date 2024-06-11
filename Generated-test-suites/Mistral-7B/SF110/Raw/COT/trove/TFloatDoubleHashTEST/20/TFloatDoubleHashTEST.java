package com.example.junit; // replace with your package name

import static org.junit.Assert.*;
import static gnu.trove.HashFunctions.hash;

import org.junit.Test;
import gnu.trove.TFloatProcedure;
import gnu.trove.impl.hash.TFloatDoubleHash;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float> hashMap;

 @Test
 public void testConstructor() {
 hashMap = new TFloatDoubleHash<>();
 assertNotNull(hashMap);
 }

 @Test
 public void testConstructorsWithInitialCapacity() {
 hashMap = new TFloatDoubleHash<>(10);
 assertNotNull(hashMap);
 assertEquals(10, hashMap.size());
 }

 @Test
 public void testConstructorsWithInitialCapacityAndLoadFactor() {
 hashMap = new TFloatDoubleHash<>(10, 0.75f);
 assertNotNull(hashMap);
 assertEquals(10, hashMap.size());
 }

 @Test
 public void testConstructorsWithInitialCapacityLoadFactorAndNoEntryKeyNoEntryValue() {
 hashMap = new TFloatDoubleHash<>(10, 0.75f, 0.0f, 0.0);
 assertNotNull(hashMap);
 assertEquals(10, hashMap.size());
 assertEquals(0.0f, hashMap.getNoEntryKey(), 0.001f);
 assertEquals(0.0, hashMap.getNoEntryValue(), 0.001);
 }

 @Test
 public void testSetUp() {
 hashMap = new TFloatDoubleHash<>(10);
 int capacity = hashMap.setUp(10);
 assertEquals(10, capacity);
 }

 @Test
 public void testGetNoEntryKey() {
 hashMap = new TFloatDoubleHash<>(10);
 hashMap.put(1.0f, 1.0);
 assertEquals(0.0f, hashMap.getNoEntryKey(), 0.001f);
 }

 @Test
 public void testGetNoEntryValue() {
 hashMap = new TFloatDoubleHash<>(10);
 hashMap.put(1.0f, 1.0);
 assertEquals(0.0, hashMap.getNoEntryValue(), 0.001);
 }

 @Test
 public void testContains() {
 hashMap = new TFloatDoubleHash<>();
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 assertTrue(hashMap.contains(1.0f));
 assertTrue(hashMap.contains(2.0f));
 assertFalse(hashMap.contains(3.0f));
 }

 @Test
 public void testForEach() {
 hashMap = new TFloatDoubleHash<>();
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 assertEquals(1.0f, floatValue, 0.001f);
 return true;
 }
 };
 hashMap.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAtNegativeIndex() {
 hashMap = new TFloatDoubleHash<>();
 hashMap.put(1.0f, 1.0);
 hashMap.removeAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBoundsExceptionForNonExistentIndex() {
 hashMap = new TFloatDoubleHash<>();
 hashMap.put(1.0f, 1.0);
 int index = hashMap.size();
 hashMap.removeAt(index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexOutOfBoundsExceptionForNegativeIndexInIndexMethod() {
 hashMap = new TFloatDoubleHash<>();
 hashMap.put(1.0f, 1.0);
 int index = -1;
 assertEquals(-1, hashMap.index(1.0f));
 assertEquals(-1, hashMap.indexRehashed(1.0f, index, hash(1.0f), (byte) 0));
 }

 @Test
 public void testPutSameKeyTwice() {
 hashMap = new TFloatDoubleHash<>();
 hashMap.put(1.0f, 1.0);
 hashMap.put(1.0f, 2.0);
 assertEquals(1.0f, hashMap.get(1.0f), 0.001f);
 assertEquals(2.0, hashMap.get(1.0f), 0.001);
 }

 @Test
 public void testPutNullKey() {
 hashMap = new TFloatDoubleHash<>();
 assertFalse(hashMap.containsKey(null));
 assertFalse(hashMap.containsValue(null));
 try {
 hashMap.put(null, null);
 fail("Should not allow null keys");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNaNKey() {
 hashMap = new TFloatDoubleHash<>();
 assertFalse(hashMap.containsKey(Float.NaN));
 assertFalse(hashMap.containsValue(Float.NaN));
 try {
 hashMap.put(Float.NaN, Float.NaN);
 fail("Should not allow NaN keys");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPutNegativeKey() {
 hashMap = new TFloatDoubleHash<>();
 assertFalse(hashMap.containsKey(-1.0f));
 assertFalse(hashMap.containsValue(-1.0f));
 try {
 hashMap.put(-1.0f, -1.0);
 fail("Should not allow negative keys");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(10);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.writeExternal(out);
 out.close();
 byte[] bytes = byteStream.toByteArray();
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TFloatDoubleHash<Float> hashMapRestored = (TFloatDoubleHash<Float>) in.readObject();
 in.close();
 assertEquals(2, hashMapRestored.size());
 assertEquals(1.0f, hashMapRestored.get(1.0f), 0.001f);
 assertEquals(2.0, hashMapRestored.get(2.0), 0.001f);
 }
}