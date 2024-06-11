package com.example.junit; // replace with your package name

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float> hashMap;

 @Before
 public void setup() {
 hashMap = new TFloatDoubleHash<>(5);
 }

 @Test
 public void testConstructor() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 assertTrue(hashMap instanceof TFloatDoubleHash);

 hashMap = new TFloatDoubleHash<>(10);
 assertTrue(hashMap instanceof TFloatDoubleHash);

 hashMap = new TFloatDoubleHash<>(10, 0.75f);
 assertTrue(hashMap instanceof TFloatDoubleHash);
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 float noEntryKey = hashMap.getNoEntryKey();
 assertEquals(0.0f, noEntryKey, 0);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 double noEntryValue = hashMap.getNoEntryValue();
 assertEquals(0.0, noEntryValue, 0);
 }

 @Test
 public void testSetUp() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 int actualCapacity = hashMap.setUp(5);
 assertEquals(5, actualCapacity);
 }

 @Test
 public void testContains() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 assertTrue(hashMap.contains(1.0f));

 hashMap = new TFloatDoubleHash<>(5);
 assertFalse(hashMap.contains(2.0f));
 }

 @Test
 public void testForEach() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertNotNull(value);
 return true;
 }
 };
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.forEach(procedure);
 assertEquals(2, hashMap.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAtNegativeIndex() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.removeAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBounds() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.removeAt(2); // should be possible to remove the second element
 int index = hashMap.index(2.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexRehashed() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.insertKey(5.0f);

 float keyToFind = 3.0f;
 int index = hashMap.index(keyToFind);
 assertEquals(2, index); // index 2 because of rehashing
 }

 @Test
 public void testInsertKey() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 int index = hashMap.insertKey(1.0f);
 assertEquals(0, index);

 hashMap = new TFloatDoubleHash<>(5);
 index = hashMap.insertKey(1.0f); // should not insert again, returns -1
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyRehash() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.insertKey(4.0f);
 hashMap.insertKey(5.0f);
 hashMap.insertKey(6.0f);

 float keyToFind = 5.0f;
 int index = hashMap.index(keyToFind);
 assertEquals(4, index); // index 4 because of rehashing
 }

 @Test
 public void testWriteExternalAndReadExternal() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);

 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 try {
 hashMap.writeExternal(objectOutput);
 objectOutput.close();
 } catch (IOException e) {
 e.printStackTrace();
 }

 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 ObjectInput objectInput = null;
 try {
 objectInput = new ObjectInput(byteArrayInputStream);
 TFloatDoubleHash<Float> deserializedHashMap = (TFloatDoubleHash<Float>) objectInput.readObject();
 objectInput.close();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 }

 assertEquals(2, deserializedHashMap.size());
 }
}