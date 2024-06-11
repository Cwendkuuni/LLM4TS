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

 @Test
 public void testAddAll() {
 byte[] data = new byte[10];
 ArrayByteList list = new ArrayByteList();
 list.addAll(data);
 Assert.assertEquals(10, list.size());
 }

 @Test
 public void testGet() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 }

 @Test
 public void testSet() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.set(0, (byte) 3);
 Assert.assertEquals((byte) 3, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.removeElementAt(0);
 Assert.assertEquals(1, list.size());
 Assert.assertEquals((byte) 2, list.get(0));
 }

 @Test
 public void testSizeAfterRemoveElementAt() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.removeElementAt(0);
 Assert.assertEquals(1, list.size());
 }

 @Test
 public void testAdd() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add(1, (byte) 0);
 Assert.assertEquals(3, list.size());
 Assert.assertEquals((byte) 0, list.get(1));
 Assert.assertEquals((byte) 1, list.get(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitialCapacityNegative() {
 ArrayByteList list = new ArrayByteList(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetIndexOutOfBounds() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 Assert.assertArrayEquals(new byte[0], list.get(-1));
 Assert.assertArrayEquals(new byte[0], list.get(2));
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList(10);
 list.trimToSize();
 Assert.assertEquals(0, list._data.length - list._size);
 }

 @Test
 public void testSerializability() throws IOException, ClassNotFoundException {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 objOut.writeObject(list);
 ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 ArrayByteList deserList = (ArrayByteList) objIn.readObject();
 Assert.assertEquals(2, deserList.size());
 }

}