package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 @Test
 public void testConstructor() {
 ArrayByteList list = new ArrayByteList();
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertEquals(0, list.size());
 Assert.assertEquals(5, list.capacity());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeInitialCapacity() {
 ArrayByteList list = new ArrayByteList(-1);
 // This line should throw an IllegalArgumentException
 }

 @Test
 public void testAddAll() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 ByteList that = new ArrayByteList();
 that.add((byte) 4);
 that.add((byte) 5);
 that.add((byte) 6);

 list.addAll(that);

 Assert.assertEquals(9, list.size());
 Assert.assertEquals(1, list.get(0));
 Assert.assertEquals(2, list.get(1));
 Assert.assertEquals(3, list.get(2));
 Assert.assertEquals(4, list.get(3));
 Assert.assertEquals(5, list.get(4));
 Assert.assertEquals(6, list.get(5));
 Assert.assertEquals(1, list.get(6));
 Assert.assertEquals(2, list.get(7));
 Assert.assertEquals(3, list.get(8));
 }

 @Test
 public void testGet() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 Assert.assertEquals(1, list.get(0));
 Assert.assertEquals(2, list.get(1));
 Assert.assertEquals(3, list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.get(4);
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 byte removed = list.removeElementAt(1);
 Assert.assertEquals(2, removed);
 Assert.assertEquals(2, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.removeElementAt(4);
 }

 @Test
 public void testSet() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 byte oldval = list.set(1, (byte) 10);
 Assert.assertEquals(10, list.get(1));
 Assert.assertEquals(1, oldval);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.set(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.set(4, (byte) 1);
 }

 @Test
 public void testAdd() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.add(0, (byte) 0);
 Assert.assertEquals(4, list.size());
 Assert.assertEquals(0, list.get(0));
 Assert.assertEquals(1, list.get(1));
 Assert.assertEquals(2, list.get(2));
 Assert.assertEquals(3, list.get(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.add(-1, (byte) 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.add(5, (byte) 0);
 }

 @Test
 public void testEnsureCapacity() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.ensureCapacity(10);
 Assert.assertEquals(13, list.capacity());
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.trimToSize();
 Assert.assertEquals(3, list.capacity());
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 out.writeObject(list);
 out.close();

 ByteArrayInputInputStream byteInputStream = new ByteArrayInputStream(byteStream.toByteArray());
 ObjectInputStream in = new ObjectInputStream(byteInputStream);
 ArrayByteList deserializedList = (ArrayByteList) in.readObject();
 in.close();

 Assert.assertEquals(3, deserializedList.size());
 Assert.assertEquals(1, deserializedList.get(0));
 Assert.assertEquals(2, deserializedList.get(1));
 Assert.assertEquals(3, deserializedList.get(2));
 }
}