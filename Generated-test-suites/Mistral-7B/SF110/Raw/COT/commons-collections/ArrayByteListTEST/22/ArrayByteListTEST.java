package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 private byte[] testData = new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9};

 @Test
 public void testConstructorWithInitialCapacity() {
  ArrayByteList arrayByteList = new ArrayByteList(10);
  Assert.assertEquals(10, arrayByteList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeInitialCapacity() {
  ArrayByteList arrayByteList = new ArrayByteList(-5);
 }

 @Test
 public void testGetMethod() {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  Assert.assertEquals((byte) 0, arrayByteList.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithNegativeIndex() {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  arrayByteList.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithIndexGreaterThanSize() {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  arrayByteList.get(10);
 }

 @Test
 public void testSizeMethod() {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  Assert.assertEquals(10, arrayByteList.size());
 }

 @Test
 public void testRemoveElementAtMethod() {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  byte removedElement = arrayByteList.removeElementAt(0);
  Assert.assertEquals((byte) 0, removedElement);
  Assert.assertEquals(9, arrayByteList.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithNegativeIndex() {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  arrayByteList.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithIndexGreaterThanSize() {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  arrayByteList.removeElementAt(10);
 }

 @Test
 public void testSetMethod() {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  byte newElement = (byte) 10;
  arrayByteList.set(0, newElement);
  Assert.assertEquals(newElement, arrayByteList.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithNegativeIndex() {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  arrayByteList.set(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithIndexGreaterThanSize() {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  arrayByteList.set(10, (byte) 1);
 }

 @Test
 public void testAddMethod() {
  ArrayByteList arrayByteList = new ArrayByteList();
  byte newElement = (byte) 10;
  arrayByteList.add(0, newElement);
  Assert.assertEquals(newElement, arrayByteList.get(0));
  Assert.assertEquals(11, arrayByteList.size());
 }

 @Test
 public void testAddWithNegativeIndex() {
  ArrayByteList arrayByteList = new ArrayByteList();
  arrayByteList.add(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithIndexGreaterThanSize() {
  ArrayByteList arrayByteList = new ArrayByteList();
  arrayByteList.add(10, (byte) 1);
 }

 @Test
 public void testEnsureCapacityMethod() {
  ArrayByteList arrayByteList = new ArrayByteList();
  arrayByteList.ensureCapacity(10);
  Assert.assertEquals(10, arrayByteList.size());
 }

 @Test
 public void testTrimToSizeMethod() {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  arrayByteList.trimToSize();
  Assert.assertEquals(10, arrayByteList.size());
 }

 @Test
 public void testWriteObjectMethod() throws IOException, ClassNotFoundException {
  ArrayByteList arrayByteList = new ArrayByteList(testData);
  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
  ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
  objectOutputStream.writeObject(arrayByteList);
  byte[] byteArray = byteArrayOutputStream.toByteArray();
  ArrayByteList deserializedArrayByteList = (ArrayByteList) new ObjectInputStream(new ByteArrayInputStream(byteArray)).readObject();
  Assert.assertArrayEquals(testData, deserializedArrayByteList.toByteArray());
 }

 @Test
 public void testReadObjectMethod() throws IOException, ClassNotFoundException {
  byte[] inputData = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
  ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(inputData));
  ArrayByteList arrayByteList = (ArrayByteList) objectInputStream.readObject();
  Assert.assertArrayEquals(inputData, arrayByteList.toByteArray());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeWithNegativeIndex() {
  ArrayByteList arrayByteList = new ArrayByteList();
  arrayByteList.checkRange(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeWithIndexGreaterThanSize() {
  ArrayByteList arrayByteList = new ArrayByteList();
  arrayByteList.checkRange(10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeInitialCapacityInCheckRange() {
  ArrayByteList arrayByteList = new ArrayByteList(-5);
 }
}