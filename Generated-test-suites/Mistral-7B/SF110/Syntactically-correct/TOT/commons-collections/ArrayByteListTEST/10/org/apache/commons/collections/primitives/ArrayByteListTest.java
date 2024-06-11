package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 @Test
 public void testConstructorWithInitialCapacity() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertEquals(5, list.size());
 }

 @Test
 public void testConstructorWithEmptyByteCollection() {
 ByteCollection emptyCollection = new ArrayList<>();
 ArrayByteList list = new ArrayByteList((ByteCollection) emptyCollection);
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testGetMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 byte expected = (byte) 1;
 byte actual = list.get(0);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(3);
 list.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(3);
 list.get(4);
 }

 @Test
 public void testSizeMethod() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertEquals(5, list.size());
 }

 @Test
 public void testRemoveElementAtMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 byte removed = list.removeElementAt(1);
 Assert.assertEquals((byte) 2, removed);
 Assert.assertEquals(4, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(3);
 list.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(3);
 list.removeElementAt(4);
 }

 @Test
 public void testSetMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 byte expected = (byte) 1;
 byte actual = list.get(0);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(3);
 list.set(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(3);
 list.set(4, (byte) 1);
 }

 @Test
 public void testAddMethod() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 byte added = list.add((byte) 2);
 Assert.assertEquals(4, list.size());
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(3);
 list.add(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(3);
 list.add(5, (byte) 1);
 }

 @Test
 public void testEnsureCapacityMethod() {
 ArrayByteList list = new ArrayByteList(3);
 list.ensureCapacity(10);
 Assert.assertEquals(10, list._data.length);
 }

 @Test
 public void testTrimToSizeMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.trimToSize();
 Assert.assertEquals(5, list._data.length);
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteCollection collection = new ArrayList<>();
 collection.add((byte) 1);
 collection.add((byte) 2);
 ArrayByteList list = new ArrayByteList((ByteCollection) collection);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 out.writeObject(list);
 byte[] bytes = byteStream.toByteArray();
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 ArrayByteList deserializedList = (ArrayByteList) in.readObject();
 Assert.assertEquals(list.size(), deserializedList.size());
 }

}