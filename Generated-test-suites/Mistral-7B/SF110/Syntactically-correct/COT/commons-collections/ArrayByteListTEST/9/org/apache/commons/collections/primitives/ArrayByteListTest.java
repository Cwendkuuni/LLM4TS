package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Test
 public void testEmptyArrayByteListConstuctor() {
 list = new ArrayByteList();
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testArrayByteListWithInitialCapacityConstuctor() {
 list = new ArrayByteList(5);
 Assert.assertEquals(0, list.size());
 Assert.assertNotNull(list._data);
 Assert.assertArrayEquals(new byte[5], list._data);
 }

 @Test
 public void testArrayByteListWithByteCollectionConstuctor() {
 ByteCollection byteCollection = new ArrayByteList();
 byteCollection.add((byte) 1);
 byteCollection.add((byte) 2);
 list = new ArrayByteList(byteCollection);
 Assert.assertEquals(2, list.size());
 Assert.assertArrayEquals(new byte[]{1, 2}, list._data);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrayByteListWithNegativeInitialCapacityConstuctor() {
 Assert.assertThrows(IllegalArgumentException.class, () -> new ArrayByteList(-5));
 }

 @Test
 public void testGetMethod() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithNegativeIndex() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithIndexGreaterThanSize() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
 }

 @Test
 public void testSetMethod() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 list.set(0, (byte) 4);
 Assert.assertEquals((byte) 4, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithNegativeIndex() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, (byte) 2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithIndexGreaterThanSize() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.set(5, (byte) 2));
 }

 @Test
 public void testAddMethod() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 Assert.assertEquals(1, list.size());
 list.add((byte) 2);
 Assert.assertEquals(2, list.size());
 list.add(1, (byte) 3);
 Assert.assertEquals(3, list.size());
 Assert.assertArrayEquals(new byte[]{1, 3, 2}, list._data);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithNegativeIndex() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, (byte) 2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithIndexGreaterThanSize() {
 list = new ArrayByteList(5);list.add((byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.add(6, (byte) 2));
 }

 @Test
 public void testRemoveElementAtMethod() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);
 list.set(3, (byte) 4);
 list.set(4, (byte) 5);
 byte removed = list.removeElementAt(2);
 Assert.assertEquals((byte) 3, removed);
 Assert.assertEquals(4, list.size());
 Assert.assertArrayEquals(new byte[]{1, 2, 4, 5}, list._data);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithNegativeIndex() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithIndexGreaterThanSize() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(5));
 }

 @Test
 public void testEnsureCapacityMethod() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.add((byte) 6);
 list.add((byte) 7);
 list.add((byte) 8);
 list.ensureCapacity(10);
 Assert.assertArrayEquals(new byte[10], list._data);
 }

 @Test
 public void testTrimToSizeMethod() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.trimToSize();
 Assert.assertArrayEquals(new byte[5], list._data);
 }

 @Test
 public void testWriteObjectMethod() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 objectOutputStream.writeObject(list);
 byte[] expected = {7, 0, 5, 0, 0, 1, 0, 5, 2, 0, 5, 3, 0, 5, 4, 0, 5, 5};
 Assert.assertArrayEquals(expected, byteArrayOutputStream.toByteArray());
 }

 @Test
 public void testReadObjectMethod() throws IOException, ClassNotFoundException {
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{7, 0, 5, 0, 0, 1, 0, 5, 2, 0, 5, 3, 0, 5, 4, 0, 5, 5});
 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
 list = (ArrayByteList) objectInputStream.readObject();
 Assert.assertEquals(5, list.size());
 Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, list._data);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeWithNegativeIndex() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.checkRange(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeWithIndexGreaterThanSize() {
 list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.checkRange(5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNegativeInitialCapacity() {
 Assert.assertThrows(IllegalArgumentException.class, () -> new ArrayByteList(-5));
 }
}