package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 private byte[] testData = new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4};

 @Test
 public void testArrayByteListConstructorEmpty() {
  ArrayByteList list = new ArrayByteList();
  Assert.assertEquals(0, list.size());
 }

 @Test
 public void testArrayByteListConstructorInitialCapacity() {
  ArrayByteList list = new ArrayByteList(5);
  Assert.assertEquals(0, list.size());
  Assert.assertArrayEquals(new byte[5], list._data);
 }

 @Test
 public void testArrayByteListConstructorByteCollection() {
  ByteCollection collection = new byte[5].asList();
  ArrayByteList list = new ArrayByteList(collection);
  Assert.assertEquals(5, list.size());
  Assert.assertArrayEquals(collection.toArray(), list._data);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrayByteListConstructorNegativeCapacity() {
  ArrayByteList list = new ArrayByteList(-1);
  Assert.fail(); // Should throw an exception
 }

 @Test
 public void testGetMethod() {
  ArrayByteList list = new ArrayByteList(testData);
  Assert.assertEquals(testData[0], list.get(0));
  Assert.assertEquals(testData[2], list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodOutOfBounds() {
  ArrayByteList list = new ArrayByteList(testData);
  Assert.assertNotEquals(testData[1], list.get(-1));
  Assert.assertNotEquals(testData[5], list.get(5));
 }

 @Test
 public void testSetMethod() {
  ArrayByteList list = new ArrayByteList(testData);
  list.set(1, (byte) 5);
  Assert.assertNotEquals(testData[1], list._data[1]);
  Assert.assertEquals(testData[0], list._data[0]);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodOutOfBounds() {
  ArrayByteList list = new ArrayByteList(testData);
  list.set(-1, (byte) 5);
  Assert.assertNotEquals(testData[0], list._data[0]);
  Assert.assertNotEquals(testData[1], list._data[1]);
  list.set(5, (byte) 5);
  Assert.assertNotEquals(testData[4], list._data[4]);
  Assert.assertEquals(testData[0], list._data[0]);
 }

 @Test
 public void testAddMethod() {
  ArrayByteList list = new ArrayByteList(testData);
  list.add(0, (byte) 1);
  Assert.assertEquals(6, list.size());
  Assert.assertEquals(testData[1], list._data[0]);
  Assert.assertEquals(testData[0], list._data[1]);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodOutOfBounds() {
  ArrayByteList list = new ArrayByteList(testData);
  list.add(-1, (byte) 1);
  Assert.assertNotEquals(testData[0], list._data[0]);
  list.add(6, (byte) 1);
  Assert.assertEquals(7, list.size());
  Assert.assertEquals(testData[0], list._data[0]);
 }

 @Test
 public void testRemoveElementAtMethod() {
  ArrayByteList list = new ArrayByteList(testData);
  byte removed = list.removeElementAt(2);
  Assert.assertEquals(testData[3], removed);
  Assert.assertEquals(4, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodOutOfBounds() {
  ArrayByteList list = new ArrayByteList(testData);
  Assert.assertNull(list.removeElementAt(-1));
  Assert.assertNull(list.removeElementAt(5));
 }

 @Test
 public void testEnsureCapacityMethod() {
  ArrayByteList list = new ArrayByteList(testData);
  list.ensureCapacity(10);
  Assert.assertEquals(10, list._data.length);
 }

 @Test
 public void testTrimToSizeMethod() {
  ArrayByteList list = new ArrayByteList(testData);
  list.trimToSize();
  Assert.assertEquals(5, list._data.length);
 }

 @Test
 public void testSerialization() {
  ArrayByteList list = new ArrayByteList(testData);
  byte[] serialized = serialize(list);
  ArrayByteList deserialized = deserialize(serialized);
  Assert.assertArrayEquals(testData, deserialized._data);
 }

 private byte[] serialize(Object obj) throws IOException {
  ByteArrayOutputStream bos = new ByteArrayOutputStream();
  ObjectOutputStream out = new ObjectOutputStream(bos);
  out.writeObject(obj);
  return bos.toByteArray();
 }

 private ArrayByteList deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
  ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
  ObjectInputStream in = new ObjectInputStream(bis);
  return (ArrayByteList) in.readObject();
 }
}