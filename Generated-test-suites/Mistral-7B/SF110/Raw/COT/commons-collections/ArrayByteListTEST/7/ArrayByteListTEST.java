package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

 private ArrayByteList arrayByteList;

 @Test
 public void testArrayByteListConstructionWithInitialCapacity() {
     arrayByteList = new ArrayByteList(5);
     assertEquals(5, arrayByteList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testArrayByteListConstructionWithNegativeInitialCapacity() {
     arrayByteList = new ArrayByteList(-5);
     fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testArrayByteListCopyConstructor() {
     arrayByteList = new ArrayByteList();
     arrayByteList.add((byte) 1);
     arrayByteList.add((byte) 2);
     ArrayByteList copy = new ArrayByteList(arrayByteList);
     assertEquals(arrayByteList.size(), copy.size());
     for (int i = 0; i < arrayByteList.size(); i++) {
         assertEquals(arrayByteList.get(i), copy.get(i));
     }
 }

 @Test
 public void testGetMethod() {
     arrayByteList = new ArrayByteList();
     arrayByteList.add((byte) 1);
     arrayByteList.add((byte) 2);
     assertEquals((byte) 1, arrayByteList.get(0));
     assertEquals((byte) 2, arrayByteList.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithNegativeIndex() {
     arrayByteList = new ArrayByteList();
     arrayByteList.add((byte) 1);
     arrayByteList.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithIndexGreaterThanSize() {
     arrayByteList = new ArrayByteList();
     arrayByteList.add((byte) 1);
     arrayByteList.get(10);
 }

 @Test
 public void testSizeMethod() {
     arrayByteList = new ArrayByteList();
     assertFalse(arrayByteList.isEmpty());
     arrayByteList.add((byte) 1);
     assertEquals(1, arrayByteList.size());
     arrayByteList.add((byte) 2);
     assertEquals(2, arrayByteList.size());
 }

 @Test
 public void testRemoveElementAtMethod() {
     arrayByteList = new ArrayByteList();
     arrayByteList.add((byte) 1);
     arrayByteList.add((byte) 2);
     assertEquals((byte) 1, arrayByteList.removeElementAt(0));
     assertEquals((byte) 2, arrayByteList.get(0));
     assertEquals(1, arrayByteList.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodWithNegativeIndex() {
     arrayByteList = new ArrayByteList();
     arrayByteList.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodWithIndexGreaterThanSize() {
     arrayByteList = new ArrayByteList();
     arrayByteList.removeElementAt(10);
 }

 @Test
 public void testSetMethod() {
     arrayByteList = new ArrayByteList();
     arrayByteList.add((byte) 1);
     arrayByteList.add((byte) 2);
     arrayByteList.set(0, (byte) 3);
     assertEquals((byte) 3, arrayByteList.get(0));
     assertEquals((byte) 2, arrayByteList.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodWithNegativeIndex() {
     arrayByteList = new ArrayByteList();
     arrayByteList.set(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodWithIndexGreaterThanSize() {
     arrayByteList = new ArrayByteList();
     arrayByteList.set(10, (byte) 1);
 }

 @Test
 public void testAddMethod() {
     arrayByteList = new ArrayByteList();
     arrayByteList.add((byte) 1);
     arrayByteList.add((byte) 2);
     arrayByteList.add(1, (byte) 3);
     assertEquals((byte) 3, arrayByteList.get(1));
     assertEquals(3, arrayByteList.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodWithNegativeIndex() {
     arrayByteList = new ArrayByteList();
     arrayByteList.add(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodWithIndexGreaterThanSize() {
     arrayByteList = new ArrayByteList();
     arrayByteList.add(10, (byte) 1);
 }

 @Test
 public void testEnsureCapacityMethod() {
     arrayByteList = new ArrayByteList();
     arrayByteList.add((byte) 1);
     arrayByteList.add((byte) 2);
     arrayByteList.ensureCapacity(10);
     assertEquals(12, arrayByteList._data.length);
 }

 @Test
 public void testTrimToSizeMethod() {
     arrayByteList = new ArrayByteList();
     arrayByteList.add((byte) 1);
     arrayByteList.add((byte) 2);
     arrayByteList.trimToSize();
     assertEquals(2, arrayByteList._data.length);
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
     ArrayByteList list = new ArrayByteList();
     list.add((byte) 1);
     list.add((byte) 2);
     ByteArrayOutputStream bos = new ByteArrayOutputStream();
     ObjectOutputStream out = new ObjectOutputStream(bos);
     out.writeObject(list);
     ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
     ArrayByteList deserializedList = (ArrayByteList) in.readObject();
     assertEquals(list.size(), deserializedList.size());
     for (int i = 0; i < list.size(); i++) {
         assertEquals(list.get(i), deserializedList.get(i));
     }
 }

}