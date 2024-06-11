package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 @Test
 public void testEmptyList() {
 ArrayByteList list = new ArrayByteList();
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testInitialCapacity() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertEquals(5, list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 ArrayByteList list = new ArrayByteList(-1);
 }

 @Test
 public void testAddAll() {
 ByteCollection byteCollection = new ArrayByteList();
 byteCollection.add((byte) 1);
 byteCollection.add((byte) 2);
 byteCollection.add((byte) 3);
 ArrayByteList list = new ArrayByteList();
 list.addAll(byteCollection);
 Assert.assertEquals(3, list.size());
 }

 @Test
 public void testGet() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.get(3);
 }

 @Test
 public void testSet() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 1, list.set(0, (byte) 4));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 Assert.assertEquals(3, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetOutOfBounds() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.set(3, (byte) 4);
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 1, list.removeElementAt(0));
 Assert.assertEquals(2, list.size());
 Assert.assertEquals((byte) 2, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtOutOfBounds() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.removeElementAt(3);
 }

 @Test
 public void testAdd() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add(0, (byte) 0);
 Assert.assertEquals(4, list.size());
 Assert.assertEquals((byte) 0, list.get(0));
 Assert.assertEquals((byte) 1, list.get(1));
 Assert.assertEquals((byte) 2, list.get(2));
 Assert.assertEquals((byte) 3, list.get(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddOutOfBounds() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add(-1, (byte) 0);
 }

 @Test
 public void testEnsureCapacity() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.add((byte) 6);
 list.add((byte) 7);
 list.add((byte) 8);
 list.add((byte) 9);
 list.add((byte) 10);
 list.ensureCapacity(20);
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.trimToSize();
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteCollection byteCollection = new ArrayByteList();
 byteCollection.add((byte) 1);
 byteCollection.add((byte) 2);
 byteCollection.add((byte) 3);
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 objectOutputStream.writeObject(byteCollection);
 ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
 ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
 Assert.assertEquals(byteCollection.size(), deserializedList.size());
 }

}