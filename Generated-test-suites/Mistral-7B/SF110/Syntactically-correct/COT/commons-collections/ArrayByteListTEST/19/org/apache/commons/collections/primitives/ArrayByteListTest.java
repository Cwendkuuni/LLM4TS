package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Test
 public void testArrayByteListConstructorWithInitialCapacity() {
 list = new ArrayByteList(5);
 assertEquals(5, list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrayByteListConstructorWithNegativeInitialCapacity() {
 list = new ArrayByteList(-5);
 fail("IllegalArgumentException should have been thrown");
 }

 @Test
 public void testArrayByteListConstructorWithEmptyByteCollection() {
 list = new ArrayByteList(ByteList.EMPTY);
 assertEquals(0, list.size());
 }

 @Test
 public void testArrayByteListGetMethod() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 byte byteValue = list.get(2);
 assertEquals((byte) 3, byteValue);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListCheckRangeMethodWithNegativeIndex() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.checkRange(-1);
 fail("IndexOutOfBoundsException should have been thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListCheckRangeMethodWithIndexGreaterThanSize() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.checkRange(6);
 fail("IndexOutOfBoundsException should have been thrown");
 }

 @Test
 public void testArrayByteListSizeMethod() {
 list = new ArrayByteList(5);
 assertEquals(0, list.size());
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 assertEquals(5, list.size());
 }

 @Test
 public void testArrayByteListRemoveElementAtMethod() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 byte removedByte = list.removeElementAt(2);
 assertEquals((byte) 3, removedByte);
 assertEquals(4, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListRemoveElementAtMethodWithNegativeIndex() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.removeElementAt(-1);
 fail("IndexOutOfBoundsException should have been thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListRemoveElementAtMethodWithIndexGreaterThanSize() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.removeElementAt(6);
 fail("IndexOutOfBoundsException should have been thrown");
 }

 @Test
 public void testArrayByteListSetMethod() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.set(2, (byte) 10);
 assertEquals((byte) 10, list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListSetMethodWithNegativeIndex() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.set(-1, (byte) 10);
 fail("IndexOutOfBoundsException should have been thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListSetMethodWithIndexGreaterThanSize() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.set(6, (byte) 10);
 fail("IndexOutOfBoundsException should have been thrown");
 }

 @Test
 public void testArrayByteListAddMethod() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.add(1, (byte) 10);
 assertEquals(6, list.size());
 assertEquals((byte) 10, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListAddMethodWithNegativeIndex() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.add(-1, (byte) 10);
 fail("IndexOutOfBoundsException should have been thrown");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testArrayByteListAddMethodWithIndexGreaterThanSize() {
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.add(6, (byte) 10);
 fail("IndexOutOfBoundsException should have been thrown");
 }

 @Test
 public void testArrayByteListEnsureCapacityMethod() {
 list = new ArrayByteList(5);
 list.ensureCapacity(10);
 assertEquals(10, list._data.length);
 }

 @Test
 public void testArrayByteListTrimToSizeMethod() {
 list = new ArrayByteList(10);
 list.trimToSize();
 assertEquals(10, list._data.length);
 }

 @Test
 public void testArrayByteListWriteObjectMethod() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 objectStream.writeObject(list);
 ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
 ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
 assertEquals(list.size(), deserializedList.size());
 for (int i = 0; i < list.size(); i++) {
 assertEquals(list.get(i), deserializedList.get(i));
 }
 }

 @Test
 public void testArrayByteListReadObjectMethod() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 objectStream.writeObject(list);
 objectStream.close();
 byteStream.reset();
 ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
 ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
 assertEquals(list.size(), deserializedList.size());
 for (int i = 0; i < list.size(); i++) {
 assertEquals(list.get(i), deserializedList.get(i));
 }
 objectInputStream.close();
 }
}