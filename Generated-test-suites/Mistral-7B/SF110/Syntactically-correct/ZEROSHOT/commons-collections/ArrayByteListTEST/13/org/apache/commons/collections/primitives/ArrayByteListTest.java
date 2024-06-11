package org.apache.commons.collections.primitives;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ArrayByteListTest {

 private ArrayByteList list;

 @Before
 public void setUp() {
 list = new ArrayByteList(3);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 }

 @Test
 public void testEmptyConstructor() {
 ArrayByteList emptyList = new ArrayByteList();
 assertEquals(0, emptyList.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 ArrayByteList listWithInitialCapacity = new ArrayByteList(5);
 assertEquals(0, listWithInitialCapacity.size());
 }

 @Test
 public void testConstructorWithByteCollection() {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 byteStream.write((byte) 1);
 byteStream.write((byte) 2);
 byteStream.write((byte) 3);
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 ArrayByteList listFromByteCollection = new ArrayByteList(byteInput);
 assertEquals(3, listFromByteCollection.size());
 }

 @Test
 public void testGetMethod() {
 assertEquals((byte) 1, list.get(0));
 assertEquals((byte) 2, list.get(1));
 assertEquals((byte) 3, list.get(2));
 }

 @Test
 public void testSizeMethod() {
 assertEquals(3, list.size());
 }

 @Test
 public void testRemoveElementAtMethod() {
 byte removedElement = list.removeElementAt(1);
 assertEquals((byte) 2, removedElement);
 assertEquals(2, list.size());
 }

 @Test
 public void testSetMethod() {
 list.set(0, (byte) 4);
 assertEquals((byte) 4, list.get(0));
 }

 @Test
 public void testAddMethod() {
 list.add(0, (byte) 0);
 list.add(4, (byte) 4);
 assertEquals(5, list.size());
 }

 @Test
 public void testEnsureCapacityMethod() {
 list.ensureCapacity(7);
 assertEquals(7, list._data.length);
 }

 @Test
 public void testTrimToSizeMethod() {
 list.trimToSize();
 assertEquals(3, list._data.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionForNegativeInitialCapacity() {
 assertThrows(IllegalArgumentException.class, () -> new ArrayByteList(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBoundsExceptionForNegativeIndex() {
 assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBoundsExceptionForLargeIndex() {
 assertThrows(IndexOutOfBoundsException.class, () -> list.get(4));
 }

 @Test
 public void testSerializable() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream output = new ObjectOutputStream(byteStream);
 output.writeObject(list);
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 ObjectInputStream input = new ObjectInputStream(byteInput);
 ArrayByteList deserializedList = (ArrayByteList) input.readObject();
 assertEquals(3, deserializedList.size());
 }
}