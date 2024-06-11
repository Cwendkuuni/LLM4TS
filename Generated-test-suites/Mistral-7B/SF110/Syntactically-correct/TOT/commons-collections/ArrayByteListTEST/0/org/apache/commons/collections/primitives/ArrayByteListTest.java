package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 @Test
 public void testEmptyArrayByteList() {
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
 ArrayByteList list = new ArrayByteList(-5);
 Assert.fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testAddAll() {
 ByteCollection collection = new ArrayByteList();
 collection.add((byte) 1);
 collection.add((byte) 2);
 ArrayByteList list = new ArrayByteList(collection);

 byte[] expected = {1, 2};
 byte[] actual = new byte[list.size()];
 for (int i = 0; i < list.size(); i++) {
 actual[i] = list.get(i);
 }
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGet() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 list.set(2, (byte) 2);

 byte expected = 1;
 byte actual = list.get(0);
 Assert.assertEquals(expected, actual);

 expected = 2;
 actual = list.get(2);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 ArrayByteList list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);

 list.get(-1);
 }

 @Test
 public void testSize() {
 ArrayByteList list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);

 Assert.assertEquals(3, list.size());
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);

 byte expected = 2;
 byte actual = list.removeElementAt(1);
 Assert.assertEquals(expected, actual);

 Assert.assertEquals(2, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtOutOfBounds() {
 ArrayByteList list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);

 list.removeElementAt(-1);
 }

 @Test
 public void testSet() {
 ArrayByteList list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);

 byte expected = 2;
 byte actual = 0;
 list.set(1, expected);
 actual = list.get(1);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetOutOfBounds() {
 ArrayByteList list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);

 list.set(-1, (byte) 4);
 }

 @Test
 public void testAdd() {
 ArrayByteList list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);

 byte expected = 4;
 byte actual = 0;
 list.add(3, expected);
 actual = list.get(3);
 Assert.assertEquals(expected, actual);

 Assert.assertEquals(4, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddOutOfBounds() {
 ArrayByteList list = new ArrayByteList(3);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);

 list.add(-1, (byte) 4);
 }

 @Test
 public void testEnsureCapacity() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 6);
 list.add((byte) 7);
 list.add((byte) 8);
 list.add((byte) 9);
 list.add((byte) 10);

 list.ensureCapacity(11);
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.trimToSize();
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 objStream.writeObject(list);
 objStream.close();

 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 ObjectInputStream objInput = new ObjectInputStream(byteInput);
 ArrayByteList deserializedList = (ArrayByteList) objInput.readObject();
 objInput.close();

 byte[] expected = new byte[]{1, 2, 3};
 byte[] actual = new byte[deserializedList.size()];
 for (int i = 0; i < deserializedList.size(); i++) {
 actual[i] = deserializedList.get(i);
 }
 Assert.assertArrayEquals(expected, actual);
 }

}