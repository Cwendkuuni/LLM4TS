package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Test
 public void testConstructorWithInitialCapacity() {
 list = new ArrayByteList(5);
 Assert.assertArrayEquals(new byte[5], list.toByteArray());
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testConstructorWithEmptyByteCollection() {
 list = new ArrayByteList(ByteList.singletonList((byte) 0));
 Assert.assertArrayEquals(new byte[]{0}, list.toByteArray());
 Assert.assertEquals(1, list.size());
 }

 @Test
 public void testGetMethod() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 Assert.assertEquals((byte) 2, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithInvalidIndex() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 list.get(-1);
 }

 @Test
 public void testSizeMethod() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 Assert.assertEquals(5, list.size());
 }

 @Test
 public void testRemoveElementAtMethod() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 final byte removedByte = list.removeElementAt(2);
 Assert.assertEquals((byte) 3, removedByte);
 Assert.assertArrayEquals(new byte[]{1, 2, 4, 4, 5}, list.toByteArray());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodWithInvalidIndex() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 list.removeElementAt(-1);
 }

 @Test
 public void testSetMethod() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 final byte newByte = (byte) 10;
 final byte oldByte = list.set(2, newByte);
 Assert.assertEquals((byte) 3, oldByte);
 Assert.assertArrayEquals(new byte[]{1, 2, 10, 4, 5}, list.toByteArray());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodWithInvalidIndex() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 list.set(-1, (byte) 10);
 }

 @Test
 public void testAddMethod() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 list.add(1, (byte) 10);
 Assert.assertArrayEquals(new byte[]{1, 10, 2, 3, 4, 5}, list.toByteArray());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodWithInvalidIndex() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 list.add(-1, (byte) 10);
 }

 @Test
 public void testEnsureCapacityMethod() {
 list = new ArrayByteList(5);
 list.ensureCapacity(10);
 Assert.assertArrayEquals(new byte[10], list._data);
 }

 @Test
 public void testTrimToSizeMethod() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
 list.trimToSize();
 Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, list._data);
 }

 @Test
 public void testCheckRangeMethodWithValidIndex() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 list.checkRange(2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeMethodWithInvalidIndex() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 list.checkRange(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeIncludingEndpointMethodWithIndexBiggerThanSize() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 list.checkRangeIncludingEndpoint(6);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeIncludingEndpointMethodWithIndexSmallerThanZero() {
 list = new ArrayByteList(new byte[]{1, 2, 3, 4, 5});
 list.checkRangeIncludingEndpoint(-1);
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 final byte[] initialData = new byte[]{1, 2, 3, 4, 5};
 list = new ArrayByteList(initialData);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 objectStream.writeObject(list);
 byte[] serializedData = byteStream.toByteArray();
 ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(serializedData));
 ArrayByteList deserializedList = (ArrayByteList) objectInput.readObject();
 Assert.assertArrayEquals(initialData, deserializedList.toByteArray());
 }
}