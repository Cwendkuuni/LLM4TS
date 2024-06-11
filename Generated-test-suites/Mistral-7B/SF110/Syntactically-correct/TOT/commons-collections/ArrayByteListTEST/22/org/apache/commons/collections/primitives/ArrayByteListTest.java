package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 @Test
 public void testConstructor() {
 ArrayByteList list = new ArrayByteList();
 Assert.assertEquals(0, list.size());
 Assert.assertNull(list._data);

 ArrayByteList list2 = new ArrayByteList(5);
 Assert.assertEquals(5, list2.size());
 Assert.assertNotNull(list2._data);
 }

 @Test
 public void testAddAll() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 ArrayByteList that = new ArrayByteList();
 that.add((byte) 4);
 that.add((byte) 5);
 that.add((byte) 6);

 list.addAll(that);

 Assert.assertEquals(9, list.size());

 byte[] expected = {1, 2, 3, 4, 5, 6};
 for (int i = 0; i < expected.length; i++) {
 Assert.assertEquals(expected[i], list.get(i));
 }
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
 public void testGetNegativeIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetBeyondEndIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.get(4);
 }

 @Test
 public void testSet() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.set(0, (byte) 4);
 list.set(2, (byte) 5);

 Assert.assertEquals((byte) 4, list.get(0));
 Assert.assertEquals((byte) 5, list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetNegativeIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.set(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetBeyondEndIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.set(4, (byte) 1);
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 Assert.assertEquals((byte) 1, list.removeElementAt(0));
 Assert.assertEquals((byte) 2, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtNegativeIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtBeyondEndIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.removeElementAt(4);
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 out.writeObject(list);

 byte[] byteArray = byteStream.toByteArray();
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteArray));
 ArrayByteList listFromSer = (ArrayByteList) in.readObject();

 Assert.assertEquals(3, listFromSer.size());
 Assert.assertArrayEquals(new byte[] {1, 2, 3}, listFromSer.toByteArray());
 }
}