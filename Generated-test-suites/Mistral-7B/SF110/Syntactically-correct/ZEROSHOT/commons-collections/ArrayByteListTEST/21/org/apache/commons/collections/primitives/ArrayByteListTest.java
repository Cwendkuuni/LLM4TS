package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 private byte[] testData = new byte[]{(byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9};

 @Test
 public void testArrayByteListEmptyConstructor() {
 ArrayByteList list = new ArrayByteList();
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testArrayByteListConstructorInteger() {
 ArrayByteList list = new ArrayByteList(10);
 Assert.assertEquals(0, list.size());
 Assert.assertArrayEquals(new byte[10], list.toByteArray());
 }

 @Test
 public void testArrayByteListConstructorByteCollection() {
 ArrayByteList list = new ArrayByteList(ByteListTest.createByteList(testData));
 Assert.assertEquals(testData.length, list.size());
 Assert.assertArrayEquals(testData, list.toByteArray());
 }

 @Test
 public void testGet() {
 ArrayByteList list = new ArrayByteList(testData);
 for (int i = 0; i < testData.length; i++) {
 byte value = list.get(i);
 Assert.assertEquals(testData[i], value);
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 ArrayByteList list = new ArrayByteList(testData);
 list.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList(testData);
 list.get(testData.length);
 }

 @Test
 public void testSize() {
 ArrayByteList list = new ArrayByteList(testData);
 Assert.assertEquals(testData.length, list.size());
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList list = new ArrayByteList(testData);
 byte removed = list.removeElementAt(0);
 Assert.assertEquals(testData[0], removed);
 Assert.assertEquals(testData.length - 1, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtNegativeIndex() {
 ArrayByteList list = new ArrayByteList(testData);
 list.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList(testData);
 list.removeElementAt(testData.length);
 }

 @Test
 public void testSet() {
 ArrayByteList list = new ArrayByteList(testData);
 list.set(0, (byte) 10);
 byte value = list.get(0);
 Assert.assertEquals((byte) 10, value);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetNegativeIndex() {
 ArrayByteList list = new ArrayByteList(testData);
 list.set(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList(testData);
 list.set(testData.length, (byte) 1);
 }

 @Test
 public void testAdd() {
 ArrayByteList list = new ArrayByteList(testData);
 list.add(0, (byte) 0);
 Assert.assertEquals(testData.length + 1, list.size());
 Assert.assertEquals((byte) 0, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddNegativeIndex() {
 ArrayByteList list = new ArrayByteList(testData);
 list.add(-1, (byte) 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList(testData);
 list.add(testData.length, (byte) 0);
 }

 @Test
 public void testAddAll() {
 byte[] testData2 = new byte[]{(byte) 11, (byte) 12, (byte) 13};
 ArrayByteList list = new ArrayByteList(testData);
 list.addAll(5, ByteListTest.createByteList(testData2));
 Assert.assertEquals(testData.length + testData2.length, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddAllNegativeIndex() {
 byte[] testData2 = new byte[]{(byte) 11, (byte) 12, (byte) 13};
 ArrayByteList list = new ArrayByteList(testData);
 list.addAll(-1, ByteListTest.createByteList(testData2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddAllBeyondLastIndex() {
 byte[] testData2 = new byte[]{(byte) 11, (byte) 12, (byte) 13};
 ArrayByteList list = new ArrayByteList(testData);
 list.addAll(testData.length, ByteListTest.createByteList(testData2));
 }

 @Test
 public void testEnsureCapacity() {
 ArrayByteList list = new ArrayByteList(testData);
 list.ensureCapacity(testData.length * 2);
 Assert.assertEquals(testData.length * 2, list._data.length);
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList(testData);
 list.trimToSize();
 Assert.assertEquals(testData.length, list._data.length);
 }

 @Test
 public void testWriteObject() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream output = new ObjectOutputStream(outputStream);
 ArrayByteList list = new ArrayByteList(testData);
 output.writeObject(list);
 byte[] bytes = outputStream.toByteArray();
 ArrayByteList restoredList = new ArrayByteList((ByteCollection) new ByteArrayInputStream(new ByteArrayInputStream(bytes)));
 Assert.assertEquals(testData.length, restoredList.size());
 Assert.assertArrayEquals(testData, restoredList.toByteArray());
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream output = new ObjectOutputStream(outputStream);
 output.writeObject(testData);
 output.writeByte(0);
 ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
 ArrayByteList list = (ArrayByteList) input.readObject();
 Assert.assertEquals(testData.length, list.size());
 Assert.assertArrayEquals(testData, list.toByteArray());
 }
}