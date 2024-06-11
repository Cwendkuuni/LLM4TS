package org.junit. Tests;

import static org.junit.Assert.*;

import org.apache.commons.collections.primitives.ArrayByteList;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Before
 public void setup() {
 list = new ArrayByteList();
 }

 // Test constructor with no arguments
 @Test
 public void testArrayByteListEmptyConstructor() {
 assertNotNull(list);
 assertEquals(0, list.size());
 }

 // Test constructor with initial capacity
 @Test
 public void testArrayByteListConstructorWithInitialCapacity() {
 list = new ArrayByteList(5);
 assertNotNull(list);
 assertEquals(0, list.size());
 assertArrayEquals(new byte[5], list._data);
 }

 // Test constructor from ByteCollection
 @Test
 public void testArrayByteListConstructorFromByteCollection() {
 ByteArrayList byteList = new ByteArrayList();
 byteList.add((byte) 1);
 byteList.add((byte) 2);
 list = new ArrayByteList(byteList);
 assertNotNull(list);
 assertEquals(2, list.size());
 for (int i = 0; i < 2; i++) {
 assertEquals(byteList.get(i), list.get(i));
 }
 }

 // Test get method
 @Test
 public void testGet() {
 list.add((byte) 1);
 list.add((byte) 2);
 assertEquals((byte) 1, list.get(0));
 assertEquals((byte) 2, list.get(1));
 }

 // Test size method
 @Test
 public void testSize() {
 assertEquals(0, list.size());
 list.add((byte) 1);
 assertEquals(1, list.size());
 list.add((byte) 2);
 assertEquals(2, list.size());
 }

 // Test removeElementAt method
 @Test
 public void testRemoveElementAt() {
 list.add((byte) 1);
 list.add((byte) 2);
 assertEquals((byte) 1, list.removeElementAt(0));
 assertEquals((byte) 2, list.get(0));
 assertEquals(1, list.size());
 }

 // Test set method
 @Test
 public void testSet() {
 list.add((byte) 1);
 list.add((byte) 2);
 assertEquals((byte) 1, list.set(0, (byte) 3));
 assertEquals((byte) 3, list.get(0));
 assertEquals((byte) 2, list.get(1));
 }

 // Test add method
 @Test
 public void testAdd() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add(4, (byte) 3);
 assertEquals(3, list.size());
 assertEquals((byte) 1, list.get(0));
 assertEquals((byte) 3, list.get(1));
 assertEquals((byte) 2, list.get(2));
 }

 // Test ensureCapacity method
 @Test
 public void testEnsureCapacity() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.ensureCapacity(10);
 assertArrayEquals(new byte[10], list._data);
 }

 // Test trimToSize method
 @Test
 public void testTrimToSize() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.trimToSize();
 assertArrayEquals(new byte[4], list._data);
 }

 // Test checkRange method with valid index
 @Test
 public void testCheckRangeValidIndex() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.checkRange(1);
 }

 // Test checkRange method with negative index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeNegativeIndex() {
 list.checkRange(-1);
 }

 // Test checkRange method with index greater than size
 @Test(expected = IndexOutOfBoundsException.class)
 public void testCheckRangeIndexGreaterThanSize() {
 list.checkRange(4);
 }

 // Test writeObject method
 @Test
 public void testWriteObject() throws IOException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 list.writeObject(objOut);
 objOut.close();
 ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 ArrayByteList listFromStream = (ArrayByteList) objIn.readObject();
 objIn.close();
 assertEquals(list.size(), listFromStream.size());
 for (int i = 0; i < list.size(); i++) {
 assertEquals(list.get(i), listFromStream.get(i));
 }
 }

 // Test readObject method
 @Test
 public void testReadObject() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 list.writeObject(objOut);
 objOut.close();
 ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 ArrayByteList listFromStream = (ArrayByteList) objIn.readObject();
 objIn.close();
 assertArrayEquals(list._data, listFromStream._data);
 assertEquals(list.size(), listFromStream.size());
 }
}