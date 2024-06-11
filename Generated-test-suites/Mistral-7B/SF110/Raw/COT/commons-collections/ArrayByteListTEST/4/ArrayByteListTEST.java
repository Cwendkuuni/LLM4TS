package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Test
 public void testConstructorWithInitialCapacity() {
 list = new ArrayByteList(5);
 assertEquals(5, list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeInitialCapacity() {
 list = new ArrayByteList(-1);
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testAddAll() {
 list = new ArrayByteList(2);
 list.add((byte) 1);
 list.add((byte) 2);

 ByteList that = new ArrayByteList(3);
 that.add((byte) 3);
 that.add((byte) 4);
 that.add((byte) 5);

 list.addAll(that);
 assertEquals(8, list.size());
 }

 @Test
 public void testGet() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 byte value = list.get(1);
 assertEquals((byte) 2, value);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithNegativeIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 byte value = list.get(-1);
 fail("IndexOutOfBoundsException should be thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithIndexGreaterThanSize() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 byte value = list.get(4);
 fail("IndexOutOfBoundsException should be thrown");
 }

 @Test
 public void testRemoveElementAt() {
 list = new ArrayByteList(4);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);

 byte removed = list.removeElementAt(1);
 assertEquals((byte) 2, removed);
 assertEquals(3, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithNegativeIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.removeElementAt(-1);
 fail("IndexOutOfBoundsException should be thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithIndexGreaterThanSize() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.removeElementAt(4);
 fail("IndexOutOfBoundsException should be thrown");
 }

 @Test
 public void testSet() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 byte newValue = (byte) 10;
 list.set(1, newValue);
 assertEquals((byte) 10, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithNegativeIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.set(-1, (byte) 10);
 fail("IndexOutOfBoundsException should be thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithIndexGreaterThanSize() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.set(4, (byte) 10);
 fail("IndexOutOfBoundsException should be thrown");
 }

 @Test
 public void testAdd() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.add((byte) 4);
 assertEquals(4, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithNegativeIndex() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.add(-1, (byte) 4);
 fail("IndexOutOfBoundsException should be thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithIndexGreaterThanSize() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.add(4, (byte) 4);
 fail("IndexOutOfBoundsException should be thrown");
 }

 @Test
 public void testEnsureCapacity() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 list.ensureCapacity(10);
 assertEquals(13, list._data.length);
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
 assertEquals(5, list._data.length);
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);

 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 out.writeObject(list);

 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 ObjectInputStream in = new ObjectInputStream(byteInput);
 ArrayByteList deserializedList = (ArrayByteList) in.readObject();

 assertEquals(3, deserializedList.size());
 }
}