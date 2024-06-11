package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 private byte[] initialArray = new byte[10];
 private ArrayByteList list;

 @Test
 public void testArrayByteListConstructorWithInitialCapacity() {
 list = new ArrayByteList(5);
 Assert.assertArrayEquals(new byte[5], list.getData());
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testArrayByteListConstructorWithEmptyByteCollection() {
 list = new ArrayByteList(new ArrayByteList());
 Assert.assertArrayEquals(new byte[0], list.getData());
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testArrayByteListConstructorWithByteCollection() {
 list = new ArrayByteList(new ArrayByteList(initialArray, 5));
 Assert.assertArrayEquals(initialArray, list.getData());
 Assert.assertEquals(5, list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrayByteListConstructorWithNegativeInitialCapacity() {
 list = new ArrayByteList(-5);
 Assert.fail("IllegalArgumentException not thrown");
 }

 @Test
 public void testGetMethod() {
 list = new ArrayByteList(initialArray);
 list.set(0, (byte) 42);
 Assert.assertEquals((byte) 42, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithNegativeIndex() {
 list = new ArrayByteList(initialArray);
 Assert.assertEquals(-1, list.get(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithIndexGreaterThanSize() {
 list = new ArrayByteList(initialArray);
 Assert.assertEquals(-1, list.get(5));
 }

 @Test
 public void testSizeMethod() {
 list = new ArrayByteList(initialArray);
 Assert.assertEquals(initialArray.length, list.size());
 }

 @Test
 public void testRemoveElementAtMethod() {
 list = new ArrayByteList(initialArray);
 byte removedElement = list.removeElementAt(3);
 assertArrayEquals(new byte[] {initialArray[0], initialArray[1], initialArray[2], initialArray[4]}, list.getData());
 Assert.assertEquals(initialArray[3], removedElement);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodWithNegativeIndex() {
 list = new ArrayByteList(initialArray);
 list.removeElementAt(-1);
 Assert.fail("IndexOutOfBoundsException not thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodWithIndexGreaterThanSize() {
 list = new ArrayByteList(initialArray);
 list.removeElementAt(5);
 Assert.fail("IndexOutOfBoundsException not thrown");
 }

 @Test
 public void testSetMethod() {
 list = new ArrayByteList(initialArray);
 list.set(3, (byte) 42);
 Assert.assertArrayEquals(new byte[] {initialArray[0], initialArray[1], initialArray[2], (byte) 42, initialArray[4]}, list.getData());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodWithNegativeIndex() {
 list = new ArrayByteList(initialArray);
 list.set(-1, (byte) 42);
 Assert.fail("IndexOutOfBoundsException not thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodWithIndexGreaterThanSize() {
 list = new ArrayByteList(initialArray);
 list.set(5, (byte) 42);
 Assert.fail("IndexOutOfBoundsException not thrown");
 }

 @Test
 public void testAddMethod() {
 list = new ArrayByteList(initialArray);
 list.add(1, (byte) 42);
 Assert.assertArrayEquals(new byte[] {initialArray[0], initialArray[0], (byte) 42, initialArray[1], initialArray[2], initialArray[3], initialArray[4]}, list.getData());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodWithNegativeIndex() {
 list = new ArrayByteList(initialArray);
 list.add(-1, (byte) 42);
 Assert.fail("IndexOutOfBoundsException not thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodWithIndexGreaterThanSize() {
 list = new ArrayByteList(initialArray);
 list.add(11, (byte) 42);
 Assert.fail("IndexOutOfBoundsException not thrown");
 }

 @Test
 public void testEnsureCapacityMethod() {
 list = new ArrayByteList(initialArray);
 list.ensureCapacity(initialArray.length * 2);
 Assert.assertEquals(initialArray.length * 2, list.getData().length);
 }

 @Test
 public void testTrimToSizeMethod() {
 list = new ArrayByteList(initialArray);
 list.trimToSize();
 Assert.assertEquals(initialArray.length, list.getData().length);
 }

 @Test
 public void testWriteObjectMethod() {
 byte[] copiedArray = new byte[initialArray.length];
 System.arraycopy(initialArray, 0, copiedArray, 0, initialArray.length);
 list = new ArrayByteList(initialArray);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 oos.writeObject(list);
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInputStream ois = new ObjectInputStream(bais);
 ArrayByteList newList = (ArrayByteList) ois.readObject();
 Assert.assertArrayEquals(copiedArray, newList.getData());
 }

 @Test
 public void testReadObjectMethod() {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 oos.writeObject(initialArray);
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInputStream ois = new ObjectInputStream(bais);
 list = new ArrayByteList(new byte[initialArray.length]);
 ois.readObject(list);
 Assert.assertArrayEquals(initialArray, list.getData());
 }

 @Test(expected = IOException.class)
 public void testWriteObjectMethodWithInvalidStream() {
 list = new ArrayByteList(initialArray);
 ByteArrayOutputStream baos = null;
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 oos.writeObject(list);
 Assert.fail("IOException not thrown");
 }

 @Test(expected = ClassNotFoundException.class)
 public void testReadObjectMethodWithInvalidStream() {
 list = new ArrayByteList(initialArray);
 ByteArrayInputStream bais = null;
 ObjectInputStream ois = new ObjectInputStream(bais);
 ois.readObject(list);
 Assert.fail("ClassNotFoundException not thrown");
 }
}