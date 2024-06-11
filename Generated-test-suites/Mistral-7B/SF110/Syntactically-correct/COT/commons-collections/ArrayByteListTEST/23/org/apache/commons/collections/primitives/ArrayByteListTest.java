package org.junit.tests;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Before
 public void setUp() {
 list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 }

 @Test
 public void testConstructorWithNoArguments() {
 ArrayByteList emptyList = new ArrayByteList();
 Assert.assertEquals(0, emptyList.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 ArrayByteList listWithInitialCapacity = new ArrayByteList(4);
 Assert.assertEquals(0, listWithInitialCapacity.size());
 Assert.assertArrayEquals(new byte[4], listWithInitialCapacity._data);
 }

 @Test
 public void testConstructorWithByteCollection() {
 ByteCollection byteCollection = new ByteArrayList();
 byteCollection.add((byte) 1);
 byteCollection.add((byte) 2);
 byteCollection.add((byte) 3);
 ArrayByteList listFromCollection = new ArrayByteList(byteCollection);
 Assert.assertEquals(3, listFromCollection.size());
 for (int i = 0; i < listFromCollection.size(); i++) {
 Assert.assertEquals(byteCollection.get(i), listFromCollection.get(i));
 }
 }

 @Test
 public void testGet() {
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithNegativeIndex() {
 list.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithIndexGreaterThanSize() {
 list.get(3);
 }

 @Test
 public void testSize() {
 Assert.assertEquals(3, list.size());
 }

 @Test
 public void testRemoveElementAt() {
 byte removedElement = list.removeElementAt(1);
 Assert.assertEquals((byte) 2, removedElement);
 Assert.assertEquals(2, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithNegativeIndex() {
 list.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtWithIndexGreaterThanSize() {
 list.removeElementAt(3);
 }

 @Test
 public void testSet() {
 list.set(0, (byte) 4);
 Assert.assertEquals((byte) 4, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithNegativeIndex() {
 list.set(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetWithIndexGreaterThanSize() {
 list.set(3, (byte) 1);
 }

 @Test
 public void testAddWithIndexAndElement() {
 list.add(1, (byte) 4);
 Assert.assertEquals(4, list.size());
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 4, list.get(1));
 Assert.assertEquals((byte) 2, list.get(2));
 Assert.assertEquals((byte) 3, list.get(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithNegativeIndex() {
 list.add(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddWithIndexGreaterThanSize() {
 list.add(4, (byte) 1);
 }

 @Test
 public void testEnsureCapacity() {
 list.ensureCapacity(5);
 Assert.assertArrayEquals(new byte[8], list._data);
 }

 @Test
 public void testTrimToSize() {
 list.trimToSize();
 Assert.assertArrayEquals(new byte[3], list._data);
 }

 @Test
 public void testWriteObject() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 objectStream.writeObject(list);
 byte[] serializedList = byteStream.toByteArray();
 ArrayByteList deserializedList = (ArrayByteList) new ObjectInputStream(new ByteArrayInputStream(serializedList)).readObject();
 Assert.assertArrayEquals(list._data, deserializedList._data);
 Assert.assertEquals(list.size(), deserializedList.size());
 }

 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ByteArrayInputStream byteStream = new ByteArrayInputStream(new byte[] { 6, 0, 0, 0, 1, 1, 1, 1, 3, 0, 1, 2, 3, 0, 3, 3, 1 });
 ObjectInputStream objectStream = new ObjectInputStream(byteStream);
 ArrayByteList deserializedList = (ArrayByteList) objectStream.readObject();
 Assert.assertArrayEquals(new byte[] { 1, 2, 3 }, deserializedList._data);
 Assert.assertEquals(3, deserializedList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeInitialCapacity() {
 new ArrayByteList(-1);
 }
}