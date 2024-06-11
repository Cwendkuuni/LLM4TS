package org.apache.commons.collections.primitives;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Before
 public void setUp() {
 list = new ArrayByteList();
 }

 @Test
 public void testEmptyList() {
 assertNotNull(list);
 assertEquals(0, list.size());
 }

 @Test
 public void testCreateWithInitialCapacity() {
 list = new ArrayByteList(5);
 assertEquals(5, list.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 list = new ArrayByteList(-3);
 // Test should fail with IllegalArgumentException
 }

 @Test
 public void testAddElement() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 assertEquals(3, list.size());
 }

 @Test
 public void testGetElement() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 assertEquals((byte) 1, list.get(0));
 assertEquals((byte) 2, list.get(1));
 assertEquals((byte) 3, list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetOutOfBounds() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 assertEquals((byte) 1, list.get(0));
 assertEquals((byte) 2, list.get(1));
 assertEquals((byte) 3, list.get(2));
 list.get(3); // Test should fail with IndexOutOfBoundsException
 }

 @Test
 public void testSetElement() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.set(0, (byte) 4);
 assertEquals((byte) 4, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetOutOfBounds() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.set(3, (byte) 4); // Test should fail with IndexOutOfBoundsException
 }

 @Test
 public void testRemoveElementAt() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 byte removed = list.removeElementAt(1);
 assertEquals((byte) 2, removed);
 assertEquals(2, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveOutOfBounds() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.removeElementAt(3); // Test should fail with IndexOutOfBoundsException
 }

 @Test
 public void testTrimToSize() {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.trimToSize();
 assertSame(list, list.subList(0, list.size()));
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 objOut.writeObject(list);
 ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 ArrayByteList deserializedList = (ArrayByteList) objIn.readObject();
 assertEquals(list, deserializedList);
 }
}