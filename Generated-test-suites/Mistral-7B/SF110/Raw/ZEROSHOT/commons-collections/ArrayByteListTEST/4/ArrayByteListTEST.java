package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

 @Test
 public void testEmptyList() {
 ArrayByteList list = new ArrayByteList();
 assertEquals(0, list.size());
 }

 @Test
 public void testListWithInitialCapacity() {
 ArrayByteList list = new ArrayByteList(5);
 assertEquals(5, list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 ArrayByteList list = new ArrayByteList(-5);
 fail("IllegalArgumentException should be thrown with negative initialCapacity");
 }

 @Test
 public void testListFromByteCollection() {
 ByteArrayDataSource dataSource = new ByteArrayDataSource();
 ArrayByteList list = new ArrayByteList(dataSource);
 assertEquals(3, list.size());
 }

 @Test
 public void testGetByteValue() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 byte value = list.get(0);
 assertEquals((byte) 1, value);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 try {
 list.get(2);
 } catch (IndexOutOfBoundsException e) {
 // expected exception
 }
 }

 @Test
 public void testSize() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 assertEquals(3, list.size());
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 byte removed = list.removeElementAt(1);
 assertEquals((byte) 2, removed);
 assertEquals(2, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveOutOfBounds() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 try {
 list.removeElementAt(2);
 } catch (IndexOutOfBoundsException e) {
 // expected exception
 }
 }

 @Test
 public void testSetByteValue() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.set(0, (byte) 3);
 assertEquals((byte) 3, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetOutOfBounds() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 try {
 list.set(2, (byte) 3);
 } catch (IndexOutOfBoundsException e) {
 // expected exception
 }
 }

 @Test
 public void testAddByteValue() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 byte added = list.add((byte) 3);
 assertEquals((byte) 3, added);
 assertEquals(3, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddOutOfBounds() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 try {
 list.add(3, (byte) 3);
 } catch (IndexOutOfBoundsException e) {
 // expected exception
 }
 }

 @Test
 public void testEnsureCapacity() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.ensureCapacity(10);
 assertEquals(5, list.size());
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.trimToSize();
 assertEquals(3, list.size());
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayDataSource dataSource = new ByteArrayDataSource();
 ArrayByteList originalList = new ArrayByteList(dataSource);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 out.writeObject(originalList);
 out.close();
 byte[] bytes = byteStream.toByteArray();
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 ArrayByteList deserializedList = (ArrayByteList) in.readObject();
 in.close();
 assertEquals(originalList, deserializedList);
 }
}