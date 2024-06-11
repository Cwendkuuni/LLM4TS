package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 private byte[] testData = new byte[10];

 @Test
 public void testConstructorWithNoArguments() {
  ArrayByteList arrayByteList = new ArrayByteList();
  Assert.assertEquals(0, arrayByteList.size());
  Assert.assertNull(arrayByteList._data);
 }

 @Test
 public void testConstructorWithInitialCapacity() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  Assert.assertEquals(0, arrayByteList.size());
  Assert.assertNotNull(arrayByteList._data);
  Assert.assertEquals(5, arrayByteList._data.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeInitialCapacity() {
  ArrayByteList arrayByteList = new ArrayByteList(-5);
  Assert.fail("Should throw IllegalArgumentException with message 'capacity " + (-5) + "'");
 }

 @Test
 public void testConstructorWithByteCollection() {
  byte[] testData = {1, 2, 3, 4, 5};
  ArrayByteList arrayByteList = new ArrayByteList(new ByteArray(testData));
  Assert.assertEquals(5, arrayByteList.size());
  for (int i = 0; i < testData.length; i++) {
   Assert.assertEquals(testData[i], arrayByteList.get(i));
  }
 }

 @Test
 public void testGet() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.set(0, (byte) 1);
  Assert.assertEquals((byte) 1, arrayByteList.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithNegativeIndex() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.set(0, (byte) 1);
  arrayByteList.get(-1);
  Assert.fail("Should throw IndexOutOfBoundsException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithIndexLargerThanSize() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.set(0, (byte) 1);
  arrayByteList.get(5);
  Assert.fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testSize() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  Assert.assertEquals(0, arrayByteList.size());
  arrayByteList.add((byte) 1);
  Assert.assertEquals(1, arrayByteList.size());
 }

 @Test
 public void testRemoveElementAt() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.add((byte) 1);
  arrayByteList.add((byte) 2);
  Assert.assertEquals(2, arrayByteList.size());
  byte removed = arrayByteList.removeElementAt(0);
  Assert.assertEquals((byte) 1, removed);
  Assert.assertEquals(1, arrayByteList.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithNegativeIndex() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.removeElementAt(-1);
  Assert.fail("Should throw IndexOutOfBoundsException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithIndexLargerThanSize() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.removeElementAt(5);
  Assert.fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testSet() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.set(0, (byte) 1);
  Assert.assertEquals((byte) 1, arrayByteList.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithNegativeIndex() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.set(-1, (byte) 1);
  Assert.fail("Should throw IndexOutOfBoundsException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithIndexLargerThanSize() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.set(5, (byte) 1);
  Assert.fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testAdd() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.add((byte) 1);
  Assert.assertEquals(1, arrayByteList.size());
  Assert.assertEquals((byte) 1, arrayByteList.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithNegativeIndex() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.add(-1, (byte) 1);
  Assert.fail("Should throw IndexOutOfBoundsException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithIndexLargerThanSize() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.add(5, (byte) 1);
  Assert.fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testAddAll() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  byte[] testData = {1, 2, 3};
  arrayByteList.addAll(5, testData);
  Assert.assertEquals(8, arrayByteList.size());
  for (int i = 0; i < testData.length; i++) {
   Assert.assertEquals(testData[i], arrayByteList.get(i + 5));
  }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddAllWithNegativeOffset() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  byte[] testData = {1, 2, 3};
  arrayByteList.addAll(-1, testData);
  Assert.fail("Should throw IndexOutOfBoundsException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddAllWithOffsetLargerThanSize() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  byte[] testData = {1, 2, 3};
  arrayByteList.addAll(6, testData);
  Assert.fail("Should throw IndexOutOfBoundsException");
 }

 @Test
 public void testEnsureCapacity() {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.ensureCapacity(10);
  Assert.assertEquals(16, arrayByteList._data.length);
 }

 @Test
 public void testTrimToSize() {
  ArrayByteList arrayByteList = new ArrayByteList(10);
  Assert.assertEquals(10, arrayByteList.size());
  arrayByteList.trimToSize();
  Assert.assertEquals(10, arrayByteList.size());
  Assert.assertEquals(10, arrayByteList._data.length);
 }

 @Test
 public void testWriteObject() throws IOException {
  ArrayByteList arrayByteList = new ArrayByteList(5);
  arrayByteList.add((byte) 1);
  ByteArrayOutputStream baos = new ByteArrayOutputStream();
  ObjectOutputStream oos = new ObjectOutputStream(baos);
  oos.writeObject(arrayByteList);
  byte[] bytes = baos.toByteArray();
  ArrayByteList arrayByteListFromSerialized = (ArrayByteList) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
  Assert.assertEquals(1, arrayByteListFromSerialized.size());
  Assert.assertEquals((byte) 1, arrayByteListFromSerialized.get(0));
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
  byte[] testData = {1, 2, 3, 4, 5};
  ByteArrayOutputStream baos = new ByteArrayOutputStream();
  ObjectOutputStream oos = new ObjectOutputStream(baos);
  oos.writeObject(testData);
  oos.writeInt(testData.length);
  for (int i = 0; i < testData.length; i++) {
   oos.writeByte(testData[i]);
  }
  ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
  ArrayByteList arrayByteList = (ArrayByteList) ois.readObject();
  Assert.assertEquals(5, arrayByteList.size());
  for (int i = 0; i < testData.length; i++) {
   Assert.assertEquals(testData[i], arrayByteList.get(i));
  }
 }
}