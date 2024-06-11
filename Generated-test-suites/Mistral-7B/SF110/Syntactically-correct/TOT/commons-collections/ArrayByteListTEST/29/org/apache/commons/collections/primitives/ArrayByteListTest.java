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
 ArrayByteList list = new ArrayByteList(10);
 Assert.assertEquals(10, list.size());
 }

 @Test
 public void testAddAll() {
 ByteCollection collection = new ArrayByteList();
 collection.add((byte) 1);
 collection.add((byte) 2);
 collection.add((byte) 3);

 ArrayByteList list = new ArrayByteList(collection);
 Assert.assertEquals(3, list.size());
 }

 @Test
 public void testGet() {
 ArrayByteList list = new ArrayByteList(new byte[]{1, 2, 3});
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 ArrayByteList list = new ArrayByteList(new byte[]{1, 2, 3});
 list.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList(new byte[]{1, 2, 3});
 list.get(3);
 }

 @Test
 public void testSet() {
 ArrayByteList list = new ArrayByteList(new byte[]{1, 2, 3});
 Assert.assertEquals((byte) 1, list.get(0));
 list.set(0, (byte) 4);
 Assert.assertEquals((byte) 4, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetNegativeIndex() {
 ArrayByteList list = new ArrayByteList(new byte[]{1, 2, 3});
 list.set(-1, (byte) 5);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList(new byte[]{1, 2, 3});
 list.set(3, (byte) 4);
 }

 @Test
 public void testAdd() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 Assert.assertEquals(1, list.size());
 }

 @Test
 public void testAddBeyondCapacity() {
 ArrayByteList list = new ArrayByteList(1);
 list.add((byte) 2);
 list.add((byte) 3);
 Assert.assertEquals(3, list.size());
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
 list.add((byte) 11);
 list.ensureCapacity(20);
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList();
 for (int i = 0; i < 20; i++) {
 list.add((byte) i);
 }
 list.trimToSize();
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream outStream = new ObjectOutputStream(byteStream);
 ArrayByteList list = new ArrayByteList(new byte[]{1, 2, 3});
 outStream.writeObject(list);
 ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
 ArrayByteList deserializedList = (ArrayByteList) inStream.readObject();
 Assert.assertEquals(3, deserializedList.size());
 }
}