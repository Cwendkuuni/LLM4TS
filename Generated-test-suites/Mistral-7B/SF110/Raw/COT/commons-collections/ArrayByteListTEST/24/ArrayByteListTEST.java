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
 ArrayByteList listWithInitialCapacity = new ArrayByteList(5);
 Assert.assertEquals(0, listWithInitialCapacity.size());
 Assert.assertNotNull(listWithInitialCapacity._data);
 Assert.assertEquals(5, listWithInitialCapacity._data.length);
 }

 @Test
 public void testConstructorWithByteCollection() {
 ByteCollection byteCollection = new ArrayByteList();
 byteCollection.add((byte) 1);
 byteCollection.add((byte) 2);
 byteCollection.add((byte) 3);
 ArrayByteList listWithByteCollection = new ArrayByteList(byteCollection);
 Assert.assertEquals(3, listWithByteCollection.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeInitialCapacity() {
 Arrays.throwException(new IllegalArgumentException("capacity " + (-1)));
 }

 @Test
 public void testGetMethod() {
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithNegativeIndex() {
 Arrays.throwException(new IndexOutOfBoundsException("Should be at least 0 and less than 3, found -1"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithIndexGreaterThanSize() {
 Arrays.throwException(new IndexOutOfBoundsException("Should be at least 0 and less than 3, found 4"));
 }

 @Test
 public void testSizeMethod() {
 Assert.assertEquals(3, list.size());
 }

 @Test
 public void testRemoveElementAtMethod() {
 byte removedElement = list.removeElementAt(1);
 Assert.assertEquals((byte) 2, removedElement);
 Assert.assertEquals(2, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodWithNegativeIndex() {
 Arrays.throwException(new IndexOutOfBoundsException("Should be at least 0 and less than 2, found -1"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodWithIndexGreaterThanSize() {
 Arrays.throwException(new IndexOutOfBoundsException("Should be at least 0 and less than 3, found 3"));
 }

 @Test
 public void testSetMethod() {
 list.set(0, (byte) 4);
 Assert.assertEquals((byte) 4, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodWithNegativeIndex() {
 Arrays.throwException(new IndexOutOfBoundsException("Should be at least 0 and less than 3, found -1"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodWithIndexGreaterThanSize() {
 Arrays.throwException(new IndexOutOfBoundsException("Should be at least 0 and less than 3, found 3"));
 }

 @Test
 public void testAddMethodWithIndexGreaterThanCurrentSize() {
 list.add(4, (byte) 5);
 Assert.assertEquals(4, list.size());
 Assert.assertEquals((byte) 5, list.get(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodWithNegativeIndex() {
 Arrays.throwException(new IndexOutOfBoundsException("Should be at least 0 and less than 3, found -1"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodWithIndexGreaterThanCurrentSizeAndAddMoreThanCapacity() {
 list.ensureCapacity(10);
 Arrays.throwException(new IndexOutOfBoundsException("Should be at least 0 and less than 7, found 7"));
 }

 @Test
 public void testAddMethodWithIndexLessThanCurrentSize() {
 list.add(1, (byte) 6);
 Assert.assertEquals(4, list.size());
 Assert.assertEquals((byte) 6, list.get(1));
 }

 @Test
 public void testAddMethodWithNewElement() {
 list.add((byte) 7);
 Assert.assertEquals(5, list.size());
 Assert.assertEquals((byte) 7, list.get(4));
 }

 @Test
 public void testEnsureCapacityMethod() {
 list.ensureCapacity(10);
 Assert.assertEquals(10, list._data.length);
 }

 @Test
 public void testEnsureCapacityMethodWithSizeGreaterThanCurrentCapacity() {
 list.ensureCapacity(7);
 Assert.assertEquals(7, list._data.length);
 }

 @Test
 public void testTrimToSizeMethod() {
 list.trimToSize();
 Assert.assertEquals(3, list._data.length);
 }

 @Test
 public void testWriteObjectMethod() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(baos);
 out.writeObject(list);
 byte[] bytes = baos.toByteArray();
 ArrayByteList listFromSerializedData = (ArrayByteList) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 Assert.assertEquals(3, listFromSerializedData.size());
 }

 @Test
 public void testReadObjectMethod() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(baos);
 out.writeObject(list);
 byte[] bytes = baos.toByteArray();
 ArrayByteList listFromSerializedData = new ArrayByteList();
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 listFromSerializedData.readObject(in);
 Assert.assertEquals(3, listFromSerializedData.size());
 }

 @Test(expected = IOException.class)
 public void testWriteObjectMethodWithNullOutputStream() throws IOException {
 Arrays.throwException(new IOException("Cannot write object to null output stream"));
 }

 @Test(expected = ClassNotFoundException.class)
 public void testReadObjectMethodWithNullInputStream() throws IOException, ClassNotFoundException {
 Arrays.throwException(new ClassNotFoundException("Unable to find class for name org.junit.tests.ArrayByteListTest$ArrayByteList@3b5d3485"));
 }

}