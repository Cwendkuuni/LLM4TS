package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Test
 public void testConstructorNoArgs() {
 list = new ArrayByteList();
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testConstructorIntArgs() {
 list = new ArrayByteList(5);
 Assert.assertEquals(0, list.size());
 Assert.assertEquals(5, list.capacity());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNegativeIntArgs() {
 list = new ArrayByteList(-5);
 // Test throws exception
 }

 @Test
 public void testAddAllCollection() {
 ByteCollection byteCollection = new ArrayByteList();
 byteCollection.add((byte) 1);
 byteCollection.add((byte) 2);
 list = new ArrayByteList();
 list.addAll(byteCollection);
 Assert.assertEquals(2, list.size());
 }

 @Test
 public void testGet() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 1, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetBeyondLastIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
 }

 @Test
 public void testSize() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals(3, list.size());
 }

 @Test
 public void testRemoveElementAt() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 1, list.removeElementAt(0));
 Assert.assertEquals(2, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtNegativeIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 1, list.removeElementAt(0));
 Assert.assertEquals(2, list.size());
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(-1));
 }

 @Test
 public void testRemoveElementAtBeyondLastIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 1, list.removeElementAt(0));
 Assert.assertEquals(2, list.size());
 Assert.assertEquals((byte) 2, list.get(0));
 Assert.assertEquals((byte) 3, list.get(1));
 Assert.assertEquals(2, list.size());
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(2));
 }

 @Test
 public void testSet() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 2, list.set(1, (byte) 10));
 Assert.assertEquals(3, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetNegativeIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 1, list.set(0, (byte) 10));
 Assert.assertEquals(3, list.size());
 Assert.assertEquals((byte) 10, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, (byte) 10));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetBeyondLastIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals((byte) 1, list.set(0, (byte) 10));
 Assert.assertEquals(3, list.size());
 Assert.assertEquals((byte) 10, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.set(3, (byte) 10));
 }

 @Test
 public void testAdd() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add(4);
 Assert.assertEquals(4, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddNegativeIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add(0, (byte) 4);
 Assert.assertEquals(4, list.size());
 Assert.assertEquals((byte) 4, list.get(0));
 Assert.assertEquals((byte) 1, list.get(1));
 Assert.assertEquals((byte) 2, list.get(2));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, (byte) 5));
 }

 @Test
 public void testAddBeyondLastIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add(4);
 Assert.assertEquals(4, list.size());
 Assert.assertEquals((byte) 4, list.get(0));
 Assert.assertEquals((byte) 1, list.get(1));
 Assert.assertEquals((byte) 2, list.get(2));
 Assert.assertEquals((byte) 3, list.get(3));
 Assert.assertEquals(5, list.capacity());
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.add(5, (byte) 5));
 }

 @Test
 public void testEnsureCapacity() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.ensureCapacity(6);
 Assert.assertEquals(6, list.capacity());
 }

 @Test
 public void testTrimToSize() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.trimToSize();
 Assert.assertEquals(5, list.capacity());
 }

 @Test
 public void testWriteObject() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 objectOutputStream.writeObject(list);
 byte[] byteArray = outputStream.toByteArray();
 ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArray));
 ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
 Assert.assertEquals(3, deserializedList.size());
 Assert.assertEquals((byte) 1, deserializedList.get(0));
 Assert.assertEquals((byte) 2, deserializedList.get(1));
 Assert.assertEquals((byte) 3, deserializedList.get(2));
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 objectOutputStream.writeObject(list);
 outputStream.reset();
 ObjectInputStream objectInputStream = new ObjectInputStream(outputStream);
 ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
 Assert.assertEquals(3, deserializedList.size());
 Assert.assertEquals((byte) 1, deserializedList.get(0));
 Assert.assertEquals((byte) 2, deserializedList.get(1));
 Assert.assertEquals((byte) 3, deserializedList.get(2));
 }

 @Test(expected = IOException.class)
 public void testSerializeNullByteArray() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 list = new ArrayByteList();
 objectOutputStream.writeObject(null);
 }
}