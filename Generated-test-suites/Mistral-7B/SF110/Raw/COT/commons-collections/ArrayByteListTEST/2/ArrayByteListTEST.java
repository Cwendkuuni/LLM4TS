package org.junit.tests;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Before
 public void init() {
 list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 }

 @Test
 public void testConstructorNoArgs() {
 ArrayByteList emptyList = new ArrayByteList();
 Assert.assertEquals(0, emptyList.size());
 }

 @Test
 public void testConstructorInt() {
 ArrayByteList listWithCapacity = new ArrayByteList(5);
 Assert.assertEquals(0, listWithCapacity.size());
 }

 @Test
 public void testConstructorByteCollection() {
 ByteCollection byteCollection = new ArrayByteList();
 byteCollection.add((byte) 1);
 byteCollection.add((byte) 2);
 byteCollection.add((byte) 3);
 ArrayByteList listFromCollection = new ArrayByteList(byteCollection);
 Assert.assertEquals(3, listFromCollection.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNegativeCapacity() {
 // This should throw an IllegalArgumentException
 ArrayByteList listWithNegativeCapacity = new ArrayByteList(-5);
 }

 @Test
 public void testGet() {
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 // This should throw an IndexOutOfBoundsException
 list.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetBeyondLastIndex() {
 // This should throw an IndexOutOfBoundsException
 list.get(3);
 }

 @Test
 public void testSize() {
 Assert.assertEquals(3, list.size());
 }

 @Test
 public void testRemoveElementAt() {
 byte oldVal = list.removeElementAt(1);
 Assert.assertEquals((byte) 2, oldVal);
 Assert.assertEquals(2, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtNegativeIndex() {
 // This should throw an IndexOutOfBoundsException
 list.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtBeyondLastIndex() {
 // This should throw an IndexOutOfBoundsException
 list.removeElementAt(3);
 }

 @Test
 public void testSet() {
 list.set(1, (byte) 4);
 Assert.assertNotSame((byte) 2, list.get(1));
 Assert.assertEquals((byte) 4, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetNegativeIndex() {
 // This should throw an IndexOutOfBoundsException
 list.set(-1, (byte) 5);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetBeyondLastIndex() {
 // This should throw an IndexOutOfBoundsException
 list.set(3, (byte) 5);
 }

 @Test
 public void testAdd() {
 list.add(0, (byte) 0);
 Assert.assertEquals(4, list.size());
 Assert.assertEquals((byte) 0, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddNegativeIndex() {
 // This should throw an IndexOutOfBoundsException
 list.add(-1, (byte) 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddBeyondLastIndex() {
 // This should throw an IndexOutOfBoundsException
 list.add(4, (byte) 0);
 }

 @Test
 public void testEnsureCapacity() {
 list.ensureCapacity(5);
 Assert.assertEquals(5, list._data.length);
 }

 @Test
 public void testTrimToSize() {
 list.trimToSize();
 Assert.assertEquals(3, list._data.length);
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 out.writeObject(list);
 byte[] serializedData = byteStream.toByteArray();
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(serializedData));
 ArrayByteList deserializedList = (ArrayByteList) in.readObject();
 Assert.assertEquals(list, deserializedList);
 }

}