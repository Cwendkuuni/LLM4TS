package org.apache.commons.collections.primitives.test;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Before
 public void setUp() {
 list = new ArrayByteList(5);
 }

 @Test
 public void testEmptyList() {
 assertTrue(list.isEmpty());
 assertEquals(0, list.size());
 }

 @Test
 public void testInitialCapacity() {
 assertEquals(5, list.size());
 }

 @Test
 public void testAddByte() {
 list.add(0, (byte) 1);
 list.add(1, (byte) 2);
 list.add((byte) 3);
 assertEquals(4, list.size());
 assertEquals((byte) 1, list.get(0));
 assertEquals((byte) 2, list.get(1));
 assertEquals((byte) 3, list.get(2));
 assertEquals((byte) 1, list.get(3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 list = new ArrayByteList(-1);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testGetByte() {
 assertEquals((byte) 0, list.get(0));
 }

 @Test
 public void testSetByte() {
 list.set(0, (byte) 1);
 assertEquals((byte) 1, list.get(0));
 }

 @Test
 public void testRemoveElementAt() {
 list.add((byte) 2);
 assertEquals(5, list.size());
 list.removeElementAt(3);
 assertEquals(4, list.size());
 assertEquals((byte) 2, list.get(3));
 }

 @Test
 public void testTrimToSize() {
 list.add((byte) 4);
 list.trimToSize();
 assertEquals(5, list.size());
 }

 @Test
 public void testEnsureCapacity() {
 list.ensureCapacity(10);
 assertEquals(10, list.size());
 }

 @Test
 public void testWriteAndReadObject() throws IOException, ClassNotFoundException {
 // Write object
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(bos);
 out.writeObject(list);
 out.close();

 // Read object
 ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
 ObjectInputStream in = new ObjectInputStream(bis);
 ArrayByteList listCopy = (ArrayByteList) in.readObject();
 in.close();

 assertEquals(4, listCopy.size());
 for (int i = 0; i < listCopy.size(); i++) {
 assertEquals(list.get(i), listCopy.get(i));
 }
 }

}