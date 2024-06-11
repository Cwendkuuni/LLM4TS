package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 @Test
 public void testEmptyConstructor() {
 ArrayByteList list = new ArrayByteList();
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertEquals(0, list.size());
 Assert.assertArrayEquals(new byte[5], list._data);
 }

 @Test
 public void testAddAllConstructor() {
 ByteCollection collection = new ArrayByteList();
 collection.add((byte) 1);
 collection.add((byte) 2);
 ArrayByteList list = new ArrayByteList(collection);
 Assert.assertEquals(2, list.size());
 }

 @Test
 public void testGet() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
 }

 @Test
 public void testSize() {
 ArrayByteList list = new ArrayByteList();
 Assert.assertEquals(0, list.size());
 list.add((byte) 1);
 list.add((byte) 2);
 Assert.assertEquals(2, list.size());
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 byte removed = list.removeElementAt(1);
 Assert.assertEquals((byte) 2, removed);
 Assert.assertEquals(2, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtNegativeIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(2));
 }

 @Test
 public void testSet() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.set(0, (byte) 0);
 Assert.assertEquals((byte) 0, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetNegativeIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, (byte) 0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.set(2, (byte) 0));
 }

 @Test
 public void testAdd() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 byte added = list.add((byte) 3);
 Assert.assertEquals(3, list.size());
 Assert.assertEquals((byte) 3, added);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddNegativeIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, (byte) 0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, (byte) 0));
 }

 @Test
 public void testEnsureCapacity() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.ensureCapacity(10);
 Assert.assertArrayEquals(new byte[10], list._data);
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.trimToSize();
 Assert.assertArrayEquals(new byte[3], list._data);
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteCollection collection = new ArrayByteList();
 collection.add((byte) 1);
 collection.add((byte) 2);
 ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteOutput);
 out.writeObject(collection);
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
 ObjectInputStream in = new ObjectInputStream(byteInput);
 ArrayByteList deserializedCollection = (ArrayByteList) in.readObject();
 Assert.assertEquals(2, deserializedCollection.size());
 }

}